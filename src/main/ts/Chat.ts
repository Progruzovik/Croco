import axios from "axios";

export default class Chat {

    private static readonly RADIO_PLUS = "radioPlus";
    private static readonly RADIO_MINUS = "radioMinus";

    private messagesCount: number = 0;

    constructor(private readonly div: HTMLElement) {}

    getMessagesCount(): number {
        return this.messagesCount;
    }

    scrollBottom() {
        this.div.scrollTop = this.div.scrollHeight;
    }

    addMessage(sender: string, text: string, number: number = -1,
               withRadio: boolean = false, isMarked: boolean = null) {
        if (number == -1) {
            number = this.messagesCount;
        }
        this.messagesCount++;

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
        this.div.innerHTML += messageContent + "</div><hr />";

        if (withRadio) {
            const radioPlus = document.getElementById(Chat.RADIO_PLUS + number) as HTMLInputElement;
            radioPlus.name = "mark" + number;
            radioPlus.checked = isMarked == true;
            radioPlus.onclick = () => axios.post("/api/lobby/mark/" + number, "marked=1");
            const radioMinus = document.getElementById(Chat.RADIO_MINUS + number) as HTMLInputElement;
            radioMinus.name = radioPlus.name;
            radioMinus.checked = isMarked == false;
            radioMinus.onclick = () => axios.post("/api/lobby/mark/" + number, "marked=0");
        }
    }

    clear() {
        this.messagesCount = 0;
        this.div.innerHTML = null;
    }
}
