import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{
    public Knight(Colors cul, Position poz) {
        super(cul, poz);
    }
    public List<Position> getPossibleMoves(Board board) throws InvalidMoveException {
        //calul are 8 mutari posibile
        //le iau pe toate 8 pe rand
        //verific daca sunt pe tabla/ nu e o piesa a ta acolo
        List<Position> pozitii = new ArrayList<>();
        Position start = getPosition();
        Position de_bagat = new Position('A',1);
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        char litera = start.Coord_lit;
        if(start.Coord_num < 8) {
            de_bagat.Coord_num ++;
            if(litera >= 'C') {
                litera -= 2;
                Position se_baga = new Position('A',1);
                se_baga.setCoord_num(de_bagat.getCoord_num());
                se_baga.setCoord_lit(litera);
                if(board.getPieceAt(se_baga) != null){
                    if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                        pozitii.add(se_baga);
                    }
                }
                else{
                    pozitii.add(se_baga);
                }
                litera += 2;
            }
            if(litera <= 'F') {
                litera += 2;
                Position se_baga = new Position('A',1);
                se_baga.setCoord_num(de_bagat.getCoord_num());
                se_baga.setCoord_lit(litera);
                if(board.getPieceAt(se_baga) != null){
                    if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                        pozitii.add(se_baga);
                    }
                }
                else{
                    pozitii.add(se_baga);
                }
                litera -= 2;
            }
            if(start.Coord_num < 7)
            {
                de_bagat.Coord_num ++;
                if(litera >= 'B') {
                    litera --;
                    Position se_baga = new Position('A',1);
                    se_baga.setCoord_num(de_bagat.getCoord_num());
                    se_baga.setCoord_lit(litera);
                    if(board.getPieceAt(se_baga) != null){
                        if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                            pozitii.add(se_baga);
                        }
                    }
                    else{
                        pozitii.add(se_baga);
                    }
                    litera ++;
                }
                if(litera <= 'G') {
                    litera ++;
                    Position se_baga = new Position('A',1);
                    se_baga.setCoord_num(de_bagat.getCoord_num());
                    se_baga.setCoord_lit(litera);
                    if(board.getPieceAt(se_baga) != null){
                        if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                            pozitii.add(se_baga);
                        }
                    }
                    else{
                        pozitii.add(se_baga);
                    }
                    litera --;
                }
                de_bagat.Coord_num --;
            }
            de_bagat.Coord_num --;
        }
        if(start.Coord_num > 1) {
            de_bagat.Coord_num --;
            if(litera >= 'C') {
                litera -= 2;
                Position se_baga = new Position('A',1);
                se_baga.setCoord_num(de_bagat.getCoord_num());
                se_baga.setCoord_lit(litera);
                if(board.getPieceAt(se_baga) != null){
                    if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                        pozitii.add(se_baga);
                    }
                }
                else{
                    pozitii.add(se_baga);
                }
                litera += 2;
            }
            if(litera <= 'F') {
                litera += 2;
                Position se_baga = new Position('A',1);
                se_baga.setCoord_num(de_bagat.getCoord_num());
                se_baga.setCoord_lit(litera);
                if(board.getPieceAt(se_baga) != null){
                    if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                        pozitii.add(se_baga);
                    }
                }
                else{
                    pozitii.add(se_baga);
                }
                litera -= 2;
            }
            if(start.Coord_num > 2)
            {
                de_bagat.Coord_num --;
                if(litera >= 'B') {
                    litera --;
                    Position se_baga = new Position('A',1);
                    se_baga.setCoord_num(de_bagat.getCoord_num());
                    se_baga.setCoord_lit(litera);
                    if(board.getPieceAt(se_baga) != null){
                        if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                            pozitii.add(se_baga);
                        }
                    }
                    else{
                        pozitii.add(se_baga);
                    }
                    litera ++;
                }
                if(litera <= 'G') {
                    litera ++;
                    Position se_baga = new Position('A',1);
                    se_baga.setCoord_num(de_bagat.getCoord_num());
                    se_baga.setCoord_lit(litera);
                    if(board.getPieceAt(se_baga) != null){
                        if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                            pozitii.add(se_baga);
                        }
                    }
                    else{
                        pozitii.add(se_baga);
                    }
                    litera --;
                }
                de_bagat.Coord_num ++;
            }
            de_bagat.Coord_num ++;
        }
        return pozitii;
    }
    public char type(){
        return 'N';
    }
    public boolean checkForCheck(Board board, Position kingPosition) throws InvalidMoveException {
        List<Position> lst = getPossibleMoves(board);
        for(Position i : lst){
            if(i.compareTo(kingPosition) == 0){
                return true;
            }
        }
        return false;
    }
}
