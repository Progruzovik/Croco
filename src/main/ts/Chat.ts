export default class Chat {

    constructor(private div: JQuery<HTMLElement>) {}

    clear() {
        this.div.html(null);
    }

    addMessage(sender: string, text: string) {
        this.div.append("<div><b>" + sender + ":</b> " + text + "</div>");
        this.div.scrollTop(this.div.scrollTop() + this.div.height());
    }
}
