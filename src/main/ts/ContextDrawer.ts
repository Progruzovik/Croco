export default class ContextDrawer {

    static readonly COLORS = ["#000000", "#964b00", "#9b2d30", "#ff0000", "#ffc0cb",
        "#ffa500", "#ffff00", "#90ee90", "#008000", "#42aaff", "#0000ff", "#30d5c8", "#8b00ff"];
    static readonly QUADS_ON_SIDE = 20;

    readonly quadLength: number;
    private _isMouseDown: boolean = false;
    private readonly recordedQuads = new Set<number>();

    constructor(private readonly canvasLength: number,
                private readonly context: CanvasRenderingContext2D) {
        this.quadLength = canvasLength / ContextDrawer.QUADS_ON_SIDE;
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

    recordQuad(number: number, x: number, y: number, color: number) {
        this.recordedQuads.add(number);
        this.drawQuad(x, y, color);
    }

    drawQuad(x: number, y: number, color: number) {
        this.context.fillStyle = ContextDrawer.COLORS[color];
        this.context.fillRect(x, y, this.quadLength, this.quadLength);
    }

    clear() {
        this.context.clearRect(0, 0, this.canvasLength, this.canvasLength);
        this.drawGrid();
    }

    private drawGrid() {
        this.context.globalAlpha = 0.2;
        this.context.fillStyle = "#000000";
        for (let i: number = 0; i <= ContextDrawer.QUADS_ON_SIDE; i++) {
            this.context.beginPath();
            this.context.moveTo(i * this.quadLength, 0);
            this.context.lineTo(i * this.quadLength, this.canvasLength);
            this.context.stroke();

            this.context.beginPath();
            this.context.moveTo(0, i * this.quadLength);
            this.context.lineTo(this.canvasLength, i * this.quadLength);
            this.context.stroke();
        }
        this.context.globalAlpha = 1;
    }
}
