import Chat from "./Chat";
import DrawArea from "./DrawArea";
import {default as axios, AxiosResponse} from "axios";

export namespace Hub {

    enum Role { Idler, Queued, Guesser, Painter, Winner }

    let role: Role;

    const inputName = document.getElementById("inputName") as HTMLInputElement;
    const drawArea = new DrawArea(document.getElementById("canvas") as HTMLCanvasElement,
        document.getElementById("selectColor") as HTMLSelectElement);
    const chat = new Chat(document.getElementById("divChat"));

    export function init() {
        axios.get("/api/player/name").then((response: AxiosResponse) => inputName.value = response.data.name);
        document.getElementById("btnQueue").onclick = onBtnQueueClick;
        drawArea.canvas.onmousedown = onCanvasMouseDown;
        drawArea.canvas.onmousemove = onCanvasMouseMoved;
        drawArea.canvas.onmouseup = onCanvasMouseUp;
        drawArea.canvas.onmouseout = onCanvasMouseUp;
        document.getElementById("formMessage").onsubmit = onFormMessageSubmit;
        document.getElementById("btnClear").onclick
            = () => axios.delete("/api/lobby/quads").then((response: AxiosResponse) => drawArea.clear());
        setInterval(onUpdated, 500);
    }

    function onUpdated() {
        if (role == Role.Guesser || role == Role.Painter) {
            axios.get("/api/lobby/players").then(updatePlayers);
            if (role == Role.Guesser) {
                axios.get("/api/lobby/game").then(updateGame);
            } else if (role == Role.Painter) {
                axios.get("api/lobby/messages").then((response: AxiosResponse) => updateChat(response.data.messages));
            }
        }
        axios.get("/api/player/role").then((response: AxiosResponse) => {
            if (role != response.data.roleCode) {
                role = response.data.roleCode;
                if (role == Role.Guesser || role == Role.Painter) {
                    chat.clear();
                    axios.get("/api/lobby/game", updateGame);
                    if (role == Role.Guesser) {
                        document.getElementById("divKeyword").innerHTML = null;
                    } else {
                        axios.get("/api/lobby/keyword").then(
                            (response: AxiosResponse) => document.getElementById("divKeyword").innerHTML
                                = "<b>Keyword: </b>" + response.data.keyword);
                    }
                }
                document.getElementById("txtStatus").innerHTML = "Role: " + Role[role];
                document.getElementById("btnQueue").innerHTML
                    = role == Role.Queued ? "Get out of queue" : "Get in queue";
                (document.getElementById("inputMessage") as HTMLInputElement).disabled = role != Role.Guesser;
                (document.getElementById("btnMessage") as HTMLButtonElement).disabled = role != Role.Guesser;
                drawArea.selectColor.disabled = role != Role.Painter;
                (document.getElementById("btnClear") as HTMLButtonElement).disabled = role != Role.Painter;
            }
        });
    }

    function onBtnQueueClick() {
        if (role == Role.Queued) {
            axios.delete("/api/player/queue");
        } else {
            axios.post("/api/player/name", "value=" + inputName.value);
            axios.post("/api/player/queue");
        }
    }

    function onCanvasMouseDown(e: MouseEvent) {
        if (role == Role.Painter) {
            drawArea.setMouseDown(true);
            onCanvasMouseMoved(e);
        }
    }

    function onCanvasMouseMoved(e: MouseEvent) {
        e.preventDefault();
        if (role == Role.Painter && drawArea.checkMouseDown()) {
            const quadX: number = e.offsetX - e.offsetX % drawArea.quadLength;
            const quadY: number = e.offsetY - e.offsetY % drawArea.quadLength;
            const number: number = quadX / drawArea.quadLength + quadY / drawArea.quadLength * DrawArea.QUADS_ON_SIDE;
            if (drawArea.recordedQuads.indexOf(number) == -1) {
                drawArea.recordedQuads.push(number);
                drawArea.drawQuad(quadX, quadY, drawArea.selectColor.selectedIndex);
            }
        }
    }

    function onCanvasMouseUp() {
        if (role == Role.Painter) {
            if (drawArea.selectColor.selectedIndex == drawArea.selectColor.length - 1) {
                for (const number of drawArea.recordedQuads) {
                    axios.delete("/api/lobby/quad/" + number);
                }
            } else {
                for (const number of drawArea.recordedQuads) {
                    axios.post("/api/lobby/quad/" + number, "color=" + drawArea.selectColor.selectedIndex);
                }
            }
            drawArea.setMouseDown(false);
        }
    }

    function onFormMessageSubmit(e: MouseEvent) {
        e.preventDefault();
        const input = document.getElementById("inputMessage") as HTMLInputElement;
        if (input.value.length > 0) {
            axios.post("/api/lobby/message", "text=" + input.value).then(() => {
                chat.addMessage(inputName.value, input.value);
                chat.scrollBottom();
                input.value = null;
            });
        }
    }

    function updatePlayers(response: AxiosResponse) {
        let players = "";
        if (response.data.painter) {
            players += "<b>Painter:</b> " + response.data.painter.name + "<br />";
        }
        players += "<b>Guessers:</b>";
        for (const guesser of response.data.guessers) {
            players += ' ' + guesser.name;
        }
        if (response.data.winner) {
            players += "<br /><b>Winner:</b> " + response.data.winner.name;
        }
        document.getElementById("divPlayers").innerHTML = players;
    }

    function updateGame(response: AxiosResponse) {
        if (response.data.quadsRemoved) {
            drawArea.clear();
        }
        for (const quad of response.data.quads) {
            drawArea.drawQuad(quad.number % DrawArea.QUADS_ON_SIDE * drawArea.quadLength,
                Math.floor(quad.number / DrawArea.QUADS_ON_SIDE) * drawArea.quadLength, quad.color);
        }
        updateChat(response.data.messages);
    }

    function updateChat(messages: any[]) {
        if (role == Role.Painter) {
            messages = messages.splice(chat.getMessagesCount());
        } else {
            chat.clear();
        }
        if (messages.length > 0) {
            for (const message of messages) {
                chat.addMessage(message.sender, message.text, message.number,
                    role == Role.Painter, message.marked);
            }
            chat.scrollBottom();
        }
    }
}
