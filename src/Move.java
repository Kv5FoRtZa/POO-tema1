public class Move {
    private Colors culoare;
    private Position plecat, ajuns;
    private Piece pc;
    // pt a avea un tip de data Move
    public Move(Colors col, Position p1,Position p2, Piece piesa){
        this.culoare = col;
        this.plecat = p1;
        this.ajuns = p2;
        this.pc = piesa;
    }
    public Colors getCuloare() {
        return culoare;
    }

    public Position getAjuns() {
        return ajuns;
    }

    public Position getPlecat() {
        return plecat;
    }
}
