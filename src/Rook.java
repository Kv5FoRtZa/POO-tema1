import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public Rook(Colors cul, Position poz) {
        super(cul, poz);
    }
    public List<Position> getPossibleMoves(Board board) throws InvalidMoveException {
        MoveStrategy strategie = new RookMoveStrategy();
        return strategie.getPossibleMoves(board, this.getPosition(), this.getColor());
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

