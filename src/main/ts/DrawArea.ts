export default class DrawArea {

    static readonly QUADS_ON_SIDE = 20;
    private static readonly COLORS = ["#000000", "#964b00", "#9b2d30", "#ff0000", "#ffc0cb", "#ffa500",
        "#ffff00", "#90ee90", "#008000", "#42aaff", "#0000ff", "#30d5c8", "#8b00ff", "#ffffff"];

    readonly quadLength: number;
    private _isMouseDown: boolean = false;

    private readonly recordedQuads = new Set<number>();
    private readonly context: CanvasRenderingContext2D;

    constructor(readonly canvas: JQuery<HTMLCanvasElement>,
                readonly selectColor: HTMLSelectElement) {
        this.quadLength = canvas.width() / DrawArea.QUADS_ON_SIDE;
        this.context = canvas[0].getContext("2d");
        this.drawGrid();
    }

    get isMouseDown(): boolean {
        return this._isMouseDown;
    }

    set isMouseDown(value: boolean) {
        if (this._isMouseDown != value) {
            this._isMouseDown = value;
            if (!this._isMouseDown) {
                this.recordedQuads.clear();
            }
        }
    }

    checkQuadRecorded(number: number): boolean {
        return this.recordedQuads.has(number);
    }

    recordQuad(number: number, x: number, y: number) {
        this.recordedQuads.add(number);
        this.drawQuad(x, y, this.selectColor.selectedIndex);
    }

    drawQuad(x: number, y: number, color: number) {
        this.context.fillStyle = DrawArea.COLORS[color];
        this.context.fillRect(x + this.context.lineWidth, y + this.context.lineWidth,
            this.quadLength - this.context.lineWidth * 2, this.quadLength - this.context.lineWidth * 2);
    }

    clear() {
        this.context.clearRect(0, 0, this.canvas.width(), this.canvas.width());
        this.drawGrid();
    }

    private drawGrid() {
        this.context.globalAlpha = 0.8;
        this.context.fillStyle = DrawArea.COLORS[0];
        for (let i: number = 0; i <= DrawArea.QUADS_ON_SIDE; i++) {
            this.context.beginPath();
            this.context.moveTo(i * this.quadLength, 0);
            this.context.lineTo(i * this.quadLength, this.canvas.width());
            this.context.stroke();

            this.context.beginPath();
            this.context.moveTo(0, i * this.quadLength);
            this.context.lineTo(this.canvas.width(), i * this.quadLength);
            this.context.stroke();
        }
        this.context.globalAlpha = 1;
    }
}
