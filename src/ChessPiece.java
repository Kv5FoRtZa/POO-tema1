import java.util.List;
public interface ChessPiece {
    List<Position> getPossibleMoves(Board board) throws InvalidMoveException;
    char type();
    boolean checkForCheck(Board board, Position kingPosition) throws InvalidMoveException;
}
