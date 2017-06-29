export default class ContextDrawer {

    private static readonly QUADS_ON_SIDE = 40;

    private readonly quadLength: number;

    constructor(canvasLength: number,
                private readonly context: CanvasRenderingContext2D)
    {
        this.quadLength = canvasLength / ContextDrawer.QUADS_ON_SIDE;

        context.globalAlpha = 0.2;
        for (let i: number = 0; i <= ContextDrawer.QUADS_ON_SIDE; i++) {
            context.beginPath();
            context.moveTo(i * this.quadLength, 0);
            context.lineTo(i * this.quadLength, canvasLength);
            context.stroke();

            context.beginPath();
            context.moveTo(0, i * this.quadLength);
            context.lineTo(canvasLength, i * this.quadLength);
            context.stroke();
        }
        context.globalAlpha = 1;
    }

    addQuad(x: number, y: number) {
        this.context.fillRect(x - x % this.quadLength, y - y % this.quadLength, this.quadLength, this.quadLength);
    }
}
