import * as $ from "jquery";
import ContextDrawer from "./ContextDrawer";

enum Role { Idler, Queued, Guesser, Painter, Winner }

export namespace Hub {

    const DELETE = "DELETE";

    let role: Role;
    let drawer: ContextDrawer;

    export function init() {
        $.getJSON("/api/player/name", (data: any) => $("#inputName").val(data.name));
        $("#btnQueue").click(onBtnQueueClick);
        const canvas = $("#canvas") as JQuery<HTMLCanvasElement>;
        drawer = new ContextDrawer(canvas[0].getContext("2d"));
        canvas.click((e: JQuery.Event) =>
            drawer.addQuad(e.pageX - canvas.offset().left, e.pageY - canvas.offset().top));
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
