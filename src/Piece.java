public abstract class Piece implements ChessPiece {
    Colors str;
    Position pos;
    //piesa generala
    public Piece(Colors cul, Position poz) {
        this.str = cul;
        this.pos = poz;
    }
    public Colors getColor() {
        return str;
    }
    public Position getPosition(){
        return pos;
    }

    public void setPosition(Position pos2) {
        this.pos = pos2;
    }
}
