export default class DrawArea {

    static readonly QUADS_ON_SIDE = 20;
    private static readonly COLORS = ["#000000", "#964b00", "#9b2d30", "#ff0000", "#ffc0cb", "#ffa500",
        "#ffff00", "#90ee90", "#008000", "#42aaff", "#0000ff", "#30d5c8", "#8b00ff", "#ffffff"];

    readonly quadLength: number;

    private isMouseDown: boolean = false;

    readonly recordedQuads: number[] = [];
    private readonly context: CanvasRenderingContext2D;

    constructor(readonly canvas: HTMLCanvasElement,
                readonly selectColor: HTMLSelectElement) {
        this.quadLength = canvas.width / DrawArea.QUADS_ON_SIDE;
        this.context = canvas.getContext("2d");
        this.drawGrid();
    }

    checkMouseDown(): boolean {
        return this.isMouseDown;
    }

    setMouseDown(value: boolean) {
        if (this.isMouseDown != value) {
            this.isMouseDown = value;
            if (!this.isMouseDown) {
                this.recordedQuads.length = 0;
            }
        }
    }

    drawQuad(x: number, y: number, color: number) {
        this.context.fillStyle = DrawArea.COLORS[color];
        this.context.fillRect(x + this.context.lineWidth, y + this.context.lineWidth,
            this.quadLength - this.context.lineWidth * 2, this.quadLength - this.context.lineWidth * 2);
    }

    clear() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.width);
        this.drawGrid();
    }

    private drawGrid() {
        this.context.globalAlpha = 0.8;
        this.context.fillStyle = DrawArea.COLORS[0];
        for (let i: number = 0; i <= DrawArea.QUADS_ON_SIDE; i++) {
            this.context.beginPath();
            this.context.moveTo(i * this.quadLength, 0);
            this.context.lineTo(i * this.quadLength, this.canvas.height);
            this.context.stroke();

            this.context.beginPath();
            this.context.moveTo(0, i * this.quadLength);
            this.context.lineTo(this.canvas.width, i * this.quadLength);
            this.context.stroke();
        }
        this.context.globalAlpha = 1;
    }
}
