import java.util.ArrayList;
import java.util.List;

public class PawnMoveStrategy implements  MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position start, Colors col) throws InvalidMoveException {
        List<Position> pozitii = new ArrayList<>();
        //pt pioni am 2 tipuri de mutari
        //alb merge in sus si negru in jos
        //fiecare tip poate merge o casuta sau 2 daca sunt pe linia originala
        //daca e inamic pe diagonala pot captura
        if(col == Colors.White) {
            Position p1 = new Position('A',1);
            p1.setCoord_lit(start.getCoord_lit());
            p1.setCoord_num(start.Coord_num);
            p1.Coord_num += 1;
            // verifica ulterior transformarea
            if(board.getPieceAt(p1)== null) {
                pozitii.add(p1);
                if(start.Coord_num == 2) {
                    Position p2 = new Position('A',1);
                    p2.setCoord_lit(start.getCoord_lit());
                    p2.setCoord_num(start.Coord_num);
                    p2.Coord_num += 2;
                    if(board.getPieceAt(p2)== null) {
                        pozitii.add(p2);
                    }
                }
            }
            Position capt = new Position('A',1);
            capt.setCoord_lit(start.getCoord_lit());
            capt.setCoord_num(start.Coord_num);
            capt.Coord_num += 1;
            capt.Coord_lit += 1;
            if(board.getPieceAt(capt) != null && board.getPieceAt(capt).getColor() == Colors.Black){
                pozitii.add(capt);
                // adauga un capturat
            }
            Position capt2 = new Position('A',1);
            capt2.setCoord_lit(start.getCoord_lit());
            capt2.setCoord_num(start.Coord_num);
            capt2.Coord_num += 1;
            capt2.Coord_lit -= 1;
            if(board.getPieceAt(capt2) != null && board.getPieceAt(capt2).getColor() == Colors.Black){
                pozitii.add(capt2);
                // adauga un capturat
            }

        }
        else if(col == Colors.Black) {
            Position p1 = new Position('A',1);
            p1.setCoord_lit(start.getCoord_lit());
            p1.setCoord_num(start.Coord_num);
            p1.Coord_num -= 1;
            // verifica ulterior transformarea
            if(board.getPieceAt(p1)== null) {
                pozitii.add(p1);
                if(start.Coord_num == 7) {
                    Position p2 = new Position('A',1);
                    p2.setCoord_lit(start.getCoord_lit());
                    p2.setCoord_num(start.Coord_num);
                    p2.Coord_num -= 2;
                    if(board.getPieceAt(p2)== null) {
                        pozitii.add(p2);
                    }
                }
            }
            Position capt = new Position('A',1);
            capt.setCoord_lit(start.getCoord_lit());
            capt.setCoord_num(start.Coord_num);
            capt.Coord_num -= 1;
            capt.Coord_lit += 1;
            if(board.getPieceAt(capt) != null && board.getPieceAt(capt).getColor() == Colors.White){
                pozitii.add(capt);
                // adauga un capturat
            }
            Position capt2 = new Position('A',1);
            capt2.setCoord_lit(start.getCoord_lit());
            capt2.setCoord_num(start.Coord_num);
            capt2.Coord_num -= 1;
            capt2.Coord_lit -= 1;
            if(board.getPieceAt(capt2) != null && board.getPieceAt(capt2).getColor() == Colors.White){
                pozitii.add(capt2);
                // adauga un capturat
            }
        }
        return pozitii;
    }
}
