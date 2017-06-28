export class DrawController {

    private static readonly CELL_LENGTH = 10;

    constructor(
        private context: CanvasRenderingContext2D)
    {}

    addQuad(x: number, y: number) {
        this.context.fillRect(x - x % DrawController.CELL_LENGTH, y - y % DrawController.CELL_LENGTH,
            DrawController.CELL_LENGTH, DrawController.CELL_LENGTH);
    }
}
