import * as $ from "jquery";
import {DrawController} from "./DrawController";

export class Stage {

    private readonly drawController: DrawController;

    constructor() {
        let canvas = $("#canvas") as JQuery<HTMLCanvasElement>;
        this.drawController = new DrawController(canvas[0].getContext("2d"));
        canvas.click((e: JQuery.Event) =>
            this.drawController.addQuad(e.pageX - canvas.offset().left, e.pageY - canvas.offset().top));

        $("#button").click(() => alert("Clicked!"));
    }
}
