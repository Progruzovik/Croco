import * as $ from "jquery";
import DrawController from "./DrawController";

enum Role { Idler, Queued, Guesser, Painter, Winner }

export namespace Stage {

    const DELETE = "DELETE";

    let role: Role;
    let drawController: DrawController;

    export function init() {
        $.getJSON("/api/player/name", (data: any) => $("#inputName").val(data.name));
        $("#btnQueue").click(onBtnQueueClick);
        const canvas = $("#canvas") as JQuery<HTMLCanvasElement>;
        drawController = new DrawController(canvas[0].getContext("2d"));
        canvas.click((e: JQuery.Event) =>
            drawController.addQuad(e.pageX - canvas.offset().left, e.pageY - canvas.offset().top));
        $("#btnMessage").click(() => alert("Message sent!"));

        setInterval(onUpdated, 500);
    }

    function onUpdated() {
        $.getJSON("/api/player/role", (data: any) => {
            role = data.roleCode;
            $("#txtStatus").html("Role: " + Role[role]);
            $("#btnQueue").html(role == Role.Queued ? "Get out of queue" : "Get in queue");
        });
    }

    function onBtnQueueClick() {
        if (role == Role.Queued) {
            $.ajax("/api/player/queue", { method: DELETE });
        } else {
            $.post("/api/player/name", "value=" + $("#inputName").val());
            $.post("/api/player/queue");
        }
    }
}
