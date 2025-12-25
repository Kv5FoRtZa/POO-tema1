import java.util.ArrayList;
import java.util.List;

public class QueenMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from, Colors col) throws InvalidMoveException {
        //combinare de tura + nebun
        //se iau toate cele 4 diagonala si linia si coloana
        //se baga toate pozitiile neocupate de nimeni/ unde se poate captura/ de pe tabla
        List<Position> pozitii = new ArrayList<>();
        Position start = from;
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
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num ++;
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
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
        de_bagat.Coord_num --;
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
        }
        de_bagat.setCoord_num(start.getCoord_num());
        de_bagat.setCoord_lit(start.getCoord_lit());
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
                if(board.getPieceAt(se_baga).getColor() != col){
                    pozitii.add(se_baga);
                }
                break;
            }
            pozitii.add(se_baga);
            de_bagat.Coord_lit ++;
        }
        return pozitii;
    }
}
