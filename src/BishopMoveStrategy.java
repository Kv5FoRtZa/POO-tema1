import java.util.ArrayList;
import java.util.List;

public class BishopMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position start, Colors col) throws InvalidMoveException {
        //nebunul merge pe diagonala
        // iau cele 4 diagonale si merg pana iese de pe tabla
        //sau loveste o piesa(care poate fi capturata sau nu)
        List<Position> pozitii = new ArrayList<>();
        Position de_bagat = new Position('A',1);
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num ++;
        de_bagat.Coord_lit ++;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != col){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_num ++;
            de_bagat.Coord_lit ++;
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num --;
        de_bagat.Coord_lit ++;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != col){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_num --;
            de_bagat.Coord_lit ++;
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num --;
        de_bagat.Coord_lit --;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != col){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_num --;
            de_bagat.Coord_lit --;
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num ++;
        de_bagat.Coord_lit --;
        while (de_bagat.inside()){
            Position se_baga = new Position('A',1);
            se_baga.setCoord_num(de_bagat.getCoord_num());
            se_baga.setCoord_lit(de_bagat.getCoord_lit());
            if(board.getPieceAt(se_baga) != null){
                if(board.getPieceAt(se_baga).getColor() != col){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_num ++;
            de_bagat.Coord_lit --;
        }
        return pozitii;
    }
}
