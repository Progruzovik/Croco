import * as $ from "jquery";

export default class Chat {

    private static readonly RADIO_PLUS = "radioPlus";
    private static readonly RADIO_MINUS = "radioMinus";

    private _messagesNumber: number = 0;

    constructor(private readonly div: JQuery<HTMLElement>) {}

    get messagesNumber(): number {
        return this._messagesNumber;
    }

    scrollBottom() {
        this.div.scrollTop(this.div[0].scrollHeight);
    }

    addMessage(sender: string, text: string, number: number = -1,
               withRadio: boolean = false, isMarked: boolean = null) {
        if (number == -1) {
            number = this._messagesNumber;
        } else if (number < this._messagesNumber) {
            return -1;
        }

        this._messagesNumber++;
        let messageContent = "<div id='message" + number + "'><b>" + sender + ":</b> " + text;
        if (withRadio) {
            messageContent += "<br /><label for='" + Chat.RADIO_PLUS + number
                + "'>+</label><input type='radio' id='radioPlus" + number + "' /> <label for='" + Chat.RADIO_MINUS
                + number + "'>-</label><input type='radio' id='radioMinus" + number + "' />";
        } else {
            if (isMarked == true) {
                messageContent += " <b>+</b>";
            } else if (isMarked == false) {
                messageContent += " <b>-</b>";
            }
        }
        this.div.append(messageContent + "</div><hr />");

        if (withRadio) {
            const radioPlus = $('#' + Chat.RADIO_PLUS + number) as JQuery<HTMLInputElement>;
            radioPlus[0].name = "mark" + number;
            radioPlus[0].checked = isMarked == true;
            radioPlus.click(() => $.post("/api/lobby/mark/" + number, "marked=1"));
            const radioMinus = $('#' + Chat.RADIO_MINUS + number) as JQuery<HTMLInputElement>;
            radioMinus[0].name = radioPlus[0].name;
            radioMinus[0].checked = isMarked == false;
            radioMinus.click(() => $.post("/api/lobby/mark/" + number, "marked=0"));
        }
    }

    clear() {
        this._messagesNumber = 0;
        this.div.html(null);
    }
}
