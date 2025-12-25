import java.awt.*;
import java.util.*;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(Colors cul, Position poz) {
        super(cul, poz);
    }
    public List<Position> getPossibleMoves(Board board) throws InvalidMoveException {
        MoveStrategy strategie = new PawnMoveStrategy();
        return strategie.getPossibleMoves(board, this.getPosition(), this.getColor());
    }
    public char type(){
        return 'P';
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
