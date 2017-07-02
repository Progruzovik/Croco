import Chat from "./Chat";
import DrawArea from "./DrawArea";
import * as $ from "jquery";

enum Role { Idler, Queued, Guesser, Painter, Winner }

export namespace Hub {

    const DELETE = "delete";

    let role: Role;

    const inputName = $("#inputName")[0] as HTMLInputElement;
    const drawArea = new DrawArea($("#canvas") as JQuery<HTMLCanvasElement>,
        $("#selectColor")[0] as HTMLSelectElement);
    const chat = new Chat($("#divChat"));

    export function init() {
        $.getJSON("/api/player/name", (data: { readonly name: string }) => inputName.value = data.name);
        $("#btnQueue").click(onBtnQueueClick);
        drawArea.canvas.mousedown(onCanvasMouseDown);
        drawArea.canvas.mousemove(onCanvasMouseMoved);
        drawArea.canvas.mouseup(onCanvasMouseUp);
        drawArea.canvas.mouseout(onCanvasMouseUp);
        $("#formMessage").submit(onFormMessageSubmit);
        $("#btnClear").click(() => $.ajax("/api/lobby/quads", { method: DELETE, success: () => drawArea.clear() }));
        setInterval(onUpdated, 500);
    }

    function onUpdated() {
        $.getJSON("/api/player/role", (data: { roleCode: number }) => {
            if (role != data.roleCode) {
                role = data.roleCode;
                if (role == Role.Painter || role == Role.Guesser) {
                    drawArea.clear();
                }
                $("#txtStatus").html("Role: " + Role[role]);
                $("#btnQueue").html(role == Role.Queued ? "Get out of queue" : "Get in queue");
                ($("#inputMessage")[0] as HTMLInputElement).disabled = role != Role.Guesser;
                ($("#btnMessage")[0] as HTMLButtonElement).disabled = role != Role.Guesser;
                drawArea.selectColor.disabled = role != Role.Painter;
                ($("#btnClear")[0] as HTMLButtonElement).disabled = role != Role.Painter;
            }
            if (role == Role.Guesser) {
                $.getJSON("/api/lobby/game", updateGame);
            } else if (role == Role.Painter) {
                $.getJSON("api/lobby/messages", (data : { messages: any[] }) => updateChat(data.messages));
            }
        });
    }

    function onBtnQueueClick() {
        if (role == Role.Queued) {
            $.ajax("/api/player/queue", { method: DELETE });
        } else {
            $.post("/api/player/name", "value=" + inputName.value);
            $.post("/api/player/queue");
        }
    }

    function onCanvasMouseDown() {
        if (role == Role.Painter) {
            drawArea.isMouseDown = true;
        }
    }

    function onCanvasMouseMoved(e: JQuery.Event): boolean {
        if (role == Role.Painter && drawArea.isMouseDown) {
            const x: number = e.pageX - drawArea.canvas.offset().left;
            const y: number = e.pageY - drawArea.canvas.offset().top;
            const quadX: number = x - x % drawArea.quadLength;
            const quadY: number = y - y % drawArea.quadLength;
            const number: number = quadX / drawArea.quadLength + quadY / drawArea.quadLength * DrawArea.QUADS_ON_SIDE;
            if (!drawArea.checkQuadRecorded(number)) {
                $.post("/api/lobby/quad/" + number, "color=" + drawArea.selectColor.selectedIndex,
                    () => drawArea.recordQuad(number, quadX, quadY));
            }
        }
        return false;
    }

    function onCanvasMouseUp() {
        if (role == Role.Painter) {
            drawArea.isMouseDown = false;
        }
    }

    function onFormMessageSubmit(e: JQuery.Event) {
        e.preventDefault();
        const input = $("#inputMessage")[0] as HTMLInputElement;
        if (input.value.length > 0) {
            $.post("/api/lobby/message", "text=" + input.value, () => {
                chat.addMessage(inputName.value, input.value);
                chat.scrollBottom();
                input.value = null;
            });
        }
    }

    function updateGame(data: { quadsRemoved: boolean, quads: { readonly color: number, readonly number: number }[],
        messages: any[] }) {
        if (data.quadsRemoved) {
            drawArea.clear();
        }
        for (const quad of data.quads) {
            drawArea.drawQuad(quad.number % DrawArea.QUADS_ON_SIDE * drawArea.quadLength,
                Math.floor(quad.number / DrawArea.QUADS_ON_SIDE) * drawArea.quadLength, quad.color);
        }
        updateChat(data.messages);
    }

    function updateChat(messages: { readonly number: number, readonly sender: string,
        readonly text: string, readonly marked: boolean }[]) {
        if (role == Role.Painter) {
            messages = messages.splice(chat.messagesNumber);
        } else {
            chat.clear();
        }
        if (messages.length > 0) {
            for (const message of messages) {
                chat.addMessage(message.sender, message.text, message.number, role == Role.Painter, message.marked);
            }
            if (role == Role.Painter) {
                chat.scrollBottom();
            }
        }
    }
}
