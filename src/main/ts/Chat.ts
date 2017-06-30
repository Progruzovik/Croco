export default class Chat {

    static readonly MESSAGE = "message";
    static readonly RADIO_PLUS = "radioPlus";
    static readonly RADIO_MINUS = "radioMinus";

    private _messagesNumber: number = 0;

    constructor(private readonly div: JQuery<HTMLElement>) {}

    get messagesNumber(): number {
        return this._messagesNumber;
    }

    scrollBottom() {
        this.div.scrollTop(this.div[0].scrollHeight);
    }

    addMessage(number :number, sender: string, text: string, withRadio: boolean, isMarked: boolean = null): number {
        if (number == -1) {
            number = this._messagesNumber;
        } else if (number < this._messagesNumber) {
            return -1;
        }

        this._messagesNumber++;
        let message = "<div id='" + Chat.MESSAGE + number + "'><b>" + sender + ":</b> " + text;
        if (withRadio) {
            message += "<br /><label for='" + Chat.RADIO_PLUS + number + "'>+</label><input type='radio' id='radioPlus"
                + number + "' name='mark" + number + "' /> <label for='" + Chat.RADIO_MINUS + number
                + "'>-</label><input type='radio' id='radioMinus" + number + "' name='mark"+ number + "' />";
        } else {
            if (isMarked == true) {
                message += " <b>+</b>";
            } else if (isMarked == false) {
                message += " <b>-</b>";
            }
        }
        this.div.append(message + "</div><hr />");

        return number;
    }

    clear() {
        this._messagesNumber = 0;
        this.div.html(null);
    }
}
