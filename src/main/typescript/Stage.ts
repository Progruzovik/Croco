import * as $ from "jquery";

export namespace Stage {
    export function run() {
        $("#button").click(onButtonClicked);
    }

    function onButtonClicked() {
        console.log("Clicked!");
    }
}
