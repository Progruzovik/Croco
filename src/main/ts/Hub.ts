import * as $ from "jquery";
import Chat from "./Chat";
import ContextDrawer from "./ContextDrawer";

enum Role { Idler, Queued, Guesser, Painter, Winner }

export namespace Hub {

    const DELETE = "delete";

    let role: Role;
    const inputName = $("#inputName")[0] as HTMLInputElement;
    const canvas = $("#canvas") as JQuery<HTMLCanvasElement>;
    const drawer = new ContextDrawer(canvas.width(), canvas[0].getContext("2d"));
    const chat = new Chat($("#divChat"));

    export function init() {
        $.getJSON("/api/player/name", (data: any) => inputName.value = data.name);
        $("#btnQueue").click(onBtnQueueClick);
        canvas.mousemove(false);
        canvas.click(onCanvasClick);
        $("#formMessage").submit(onFormMessageSubmit);
        $("#btnClear").click(() => $.ajax("/api/lobby/quads", { method: DELETE, success: () => drawer.clear() }));
        setInterval(onUpdated, 500);
    }

    function onUpdated() {
        $.getJSON("/api/player/role", (data: { roleCode: number }) => {
            if (role != data.roleCode) {
                role = data.roleCode;
                if (role == Role.Painter || role == Role.Guesser) {
                    drawer.clear();
                }
                $("#txtStatus").html("Role: " + Role[role]);
                $("#btnQueue").html(role == Role.Queued ? "Get out of queue" : "Get in queue");
                ($("#inputMessage")[0] as HTMLInputElement).disabled = role != Role.Guesser;
                ($("#btnMessage")[0] as HTMLButtonElement).disabled = role != Role.Guesser;
                ($("#selectColor")[0] as HTMLSelectElement).disabled = role != Role.Painter;
                ($("#btnClear")[0] as HTMLButtonElement).disabled = role != Role.Painter;
            }
            if (role == Role.Guesser) {
                $.getJSON("/api/lobby/game", updateGame);
            } else if (role == Role.Painter) {
                $.getJSON("api/lobby/messages", (data : { messages: string[] }) => updateChat(data.messages));
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

    function onCanvasClick(e: JQuery.Event) {
        if (role == Role.Painter) {
            const x: number = e.pageX - canvas.offset().left;
            const y: number = e.pageY - canvas.offset().top;
            const quadX: number = x - x % drawer.quadLength;
            const quadY: number = y - y % drawer.quadLength;
            const number: number = quadY / drawer.quadLength * ContextDrawer.QUADS_ON_SIDE + quadX / drawer.quadLength;
            const color: number = ($("#selectColor")[0] as HTMLSelectElement).selectedIndex;
            $.post("/api/lobby/quad/" + number, "color=" + color, () => drawer.drawQuad(quadX, quadY, color));
        }
    }

    function onFormMessageSubmit(e: JQuery.Event) {
        e.preventDefault();
        const input = $("#inputMessage")[0] as HTMLInputElement;
        if (input.value.length > 0) {
            $.post("/api/lobby/message", "text=" + input.value, () => {
                chat.addMessage(inputName.value, input.value);
                input.value = null;
            });
        }
    }

    function updateGame(
        data: { messages: any[], quads: { number: number, color: number }[], quadsRemoved: boolean }) {
        if (data.quadsRemoved) {
            drawer.clear();
        }
        for (const quad of data.quads) {
            const quadX: number = quad.number % ContextDrawer.QUADS_ON_SIDE * drawer.quadLength;
            const quadY: number = Math.floor(quad.number / ContextDrawer.QUADS_ON_SIDE) * drawer.quadLength;
            drawer.drawQuad(quadX, quadY, quad.color);
        }
        updateChat(data.messages);
    }

    function updateChat(messages: any[]) {
        chat.clear();
        for (const message of messages) {
            chat.addMessage(message.sender, message.text);
        }
    }
}
