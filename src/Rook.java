import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public Rook(Colors cul, Position poz) {
        super(cul, poz);
    }
    public List<Position> getPossibleMoves(Board board) throws InvalidMoveException {
        // tura merge pe linie sau pe coloana
        //bag toate pozitiile de pe tabla si neocupate de piese de aceasi culoare
        //daca e o piesa inamica se poate captura piesa
        List<Position> pozitii = new ArrayList<>();
        Position start = getPosition();
        Position de_bagat = new Position('A',1);
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num ++;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_num ++;
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num --;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_num --;
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_lit --;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_lit --;
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_lit ++;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != this.getColor()){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_lit ++;
        }
        return pozitii;
    }
    public char type(){
        return 'R';
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

