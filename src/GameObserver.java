public interface GameObserver {
    int onMoveMade(Player p, Position from,Position to, int psc) throws Exception ;
    void onPieceCaptured(Piece piece, int psc);
    void onPlayerSwitch(Player currentPlayer);

}
