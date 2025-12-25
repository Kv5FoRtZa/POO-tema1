public class FinalPointsStrategy implements PointsStrategy{
    @Override
    public int modificaPunctaj(Piece piesa, int fin) {
        if(piesa == null){
            if(fin == -2){
                return -300;
            }
            if(fin == -1){
                return -150;
            }
            if(fin == 1){
                return 300;
            }
        }
        return 0;
    }
}
