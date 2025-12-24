public class Factory {
    public Factory(){
    }
    public static Piece setez(String nume, Position pos, Colors col){
        if("Rook".compareTo(nume) == 0){
            Piece tura = new Rook(col,pos);
            return tura;
        }
        else if("Pawn".compareTo(nume) == 0){
            Piece pion = new Pawn(col,pos);
            return pion;
        }
        else if("King".compareTo(nume) == 0){
            Piece rege = new King(col,pos);
            return rege;
        }
        else if("Bishop".compareTo(nume) == 0){
            Piece nebun = new Bishop(col,pos);
            return nebun;
        }
        else if("Knight".compareTo(nume) == 0){
            Piece cal = new Knight(col,pos);
            return cal;
        }
        else if("Queen".compareTo(nume) == 0){
            Piece regina = new Queen(col,pos);
            return regina;
        }
        else{
            return null;
        }
    }
}
