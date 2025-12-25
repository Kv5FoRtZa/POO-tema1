public class CapturePointsStrategy implements PointsStrategy{
    @Override
    public int modificaPunctaj(Piece piesa, int fnl) {
        if(fnl == 0 && piesa != null){
            if(piesa.type() == 'P'){
                return 10;
            }
            else if(piesa.type() == 'B' || piesa.type() == 'N'){
                return 30;
            }
            else if(piesa.type() == 'R'){
                return 50;
            }
            else if(piesa.type() == 'Q'){
                return 90;
            }
            else{
                return 0;
            }
        }
        return 0;
    }
}
