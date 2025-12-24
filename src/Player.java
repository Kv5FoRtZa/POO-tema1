import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {
    String name;
    Colors col;
    Position regep;
    private List<Piece> capt;
    private TreeSet<ChessPair<Position, Piece> > piese_detinute;
    private int puncte;
    public Player(String name, Colors cul){
        this.name = name;
        this.col = cul;
        this.puncte = 0;
        this.capt = new ArrayList<>();
        this.piese_detinute = new TreeSet<>();;
    }
    void  initialise_pieces() throws InvalidMoveException {
        //aici e ca la initializarea de tabla, doar ca initializez pt alb apoi pt negru
        // e nevoie ca fiecare player sa aiba piesele lui, pt capturari si pt mat
        // initializez doar piesele lui pe culoarea lui
        if(this.col == Colors.White)
        {
            regep = new Position('E',1);
            int p_pion = 2;
            int p_piesa = 1;
            //pioni
            Position pos = new Position('A',p_pion);
            Piece pion = new Pawn(Colors.White,pos);
            ChessPair<Position, Piece> de_adaugat = new ChessPair<Position, Piece>(pos,pion);
            this.piese_detinute.add(de_adaugat);

            Position pos2 = new Position('B',p_pion);
            Piece pion2 = new Pawn(Colors.White,pos2);
            ChessPair<Position, Piece> de_adaugat2 = new ChessPair<Position, Piece>(pos2,pion2);
            this.piese_detinute.add(de_adaugat2);

            Position pos3 = new Position('C',p_pion);
            Piece pion3 = new Pawn(Colors.White,pos3);
            ChessPair<Position, Piece> de_adaugat3 = new ChessPair<Position, Piece>(pos3,pion3);
            this.piese_detinute.add(de_adaugat3);

            Position pos4 = new Position('D',p_pion);
            Piece pion4 = new Pawn(Colors.White,pos4);
            ChessPair<Position, Piece> de_adaugat4 = new ChessPair<Position, Piece>(pos4,pion4);
            this.piese_detinute.add(de_adaugat4);

            Position pos5 = new Position('E',p_pion);
            Piece pion5 = new Pawn(Colors.White,pos5);
            ChessPair<Position, Piece> de_adaugat5 = new ChessPair<Position, Piece>(pos5,pion5);
            this.piese_detinute.add(de_adaugat5);

            Position pos6 = new Position('F',p_pion);
            Piece pion6 = new Pawn(Colors.White,pos6);
            ChessPair<Position, Piece> de_adaugat6 = new ChessPair<Position, Piece>(pos6,pion6);
            this.piese_detinute.add(de_adaugat6);

            Position pos7 = new Position('G',p_pion);
            Piece pion7 = new Pawn(Colors.White,pos7);
            ChessPair<Position, Piece> de_adaugat7 = new ChessPair<Position, Piece>(pos7,pion7);
            this.piese_detinute.add(de_adaugat7);

            Position pos8 = new Position('H',p_pion);
            Piece pion8 = new Pawn(Colors.White,pos8);
            ChessPair<Position, Piece> de_adaugat8 = new ChessPair<Position, Piece>(pos8,pion8);
            this.piese_detinute.add(de_adaugat8);
            //turn
            Position pos9 = new Position('A',p_piesa);
            Piece turn = new Rook(Colors.White,pos9);
            ChessPair<Position, Piece> de_adaugat9 = new ChessPair<Position, Piece>(pos9,turn);
            this.piese_detinute.add(de_adaugat9);

            Position pos10 = new Position('H',p_piesa);
            Piece turn2 = new Rook(Colors.White,pos10);
            ChessPair<Position, Piece> de_adaugat10 = new ChessPair<Position, Piece>(pos10,turn2);
            this.piese_detinute.add(de_adaugat10);
            //nebun
            Position pos11 = new Position('C',p_piesa);
            Piece nebun = new Bishop(Colors.White,pos11);
            ChessPair<Position, Piece> de_adaugat11 = new ChessPair<Position, Piece>(pos11,nebun);
            this.piese_detinute.add(de_adaugat11);

            Position pos12 = new Position('F',p_piesa);
            Piece nebun2 = new Bishop(Colors.White,pos12);
            ChessPair<Position, Piece> de_adaugat12 = new ChessPair<Position, Piece>(pos12,nebun2);
            this.piese_detinute.add(de_adaugat12);
            //cal
            Position pos13 = new Position('B',p_piesa);
            Piece cal = new Knight(Colors.White,pos13);
            ChessPair<Position, Piece> de_adaugat13 = new ChessPair<Position, Piece>(pos13,cal);
            this.piese_detinute.add(de_adaugat13);

            Position pos14 = new Position('G',p_piesa);
            Piece cal2 = new Knight(Colors.White,pos14);
            ChessPair<Position, Piece> de_adaugat14 = new ChessPair<Position, Piece>(pos14,cal2);
            this.piese_detinute.add(de_adaugat14);
            //Dama
            Position pos15 = new Position('D',p_piesa);
            Piece dama = new Queen(Colors.White,pos15);
            ChessPair<Position, Piece> de_adaugat15 = new ChessPair<Position, Piece>(pos15,dama);
            this.piese_detinute.add(de_adaugat15);
            //rege
            Position pos16 = new Position('E',p_piesa);
            Piece rege = new King(Colors.White,pos16);
            ChessPair<Position, Piece> de_adaugat16 = new ChessPair<Position, Piece>(pos16,rege);
            this.piese_detinute.add(de_adaugat16);
        }
        if(this.col == Colors.Black)
        {
            regep = new Position('E',8);
            int p_pion = 7;
            int p_piesa = 8;
            //pioni
            Position pos17 = new Position('A',p_pion);
            Piece pion17 = new Pawn(Colors.Black,pos17);
            ChessPair<Position, Piece> de_adaugat17 = new ChessPair<Position, Piece>(pos17,pion17);
            this.piese_detinute.add(de_adaugat17);

            Position pos18 = new Position('B',p_pion);
            Piece pion18 = new Pawn(Colors.Black,pos18);
            ChessPair<Position, Piece> de_adaugat18 = new ChessPair<Position, Piece>(pos18,pion18);
            this.piese_detinute.add(de_adaugat18);

            Position pos19 = new Position('C',p_pion);
            Piece pion19 = new Pawn(Colors.Black,pos19);
            ChessPair<Position, Piece> de_adaugat19 = new ChessPair<Position, Piece>(pos19,pion19);
            this.piese_detinute.add(de_adaugat19);

            Position pos20 = new Position('D',p_pion);
            Piece pion20 = new Pawn(Colors.Black,pos20);
            ChessPair<Position, Piece> de_adaugat20 = new ChessPair<Position, Piece>(pos20,pion20);
            this.piese_detinute.add(de_adaugat20);

            Position pos21 = new Position('E',p_pion);
            Piece pion21 = new Pawn(Colors.Black,pos21);
            ChessPair<Position, Piece> de_adaugat21 = new ChessPair<Position, Piece>(pos21,pion21);
            this.piese_detinute.add(de_adaugat21);

            Position pos22 = new Position('F',p_pion);
            Piece pion22 = new Pawn(Colors.Black,pos22);
            ChessPair<Position, Piece> de_adaugat22 = new ChessPair<Position, Piece>(pos22,pion22);
            this.piese_detinute.add(de_adaugat22);

            Position pos23 = new Position('G',p_pion);
            Piece pion23 = new Pawn(Colors.Black,pos23);
            ChessPair<Position, Piece> de_adaugat23 = new ChessPair<Position, Piece>(pos23,pion23);
            this.piese_detinute.add(de_adaugat23);

            Position pos24 = new Position('H',p_pion);
            Piece pion24 = new Pawn(Colors.Black,pos24);
            ChessPair<Position, Piece> de_adaugat24 = new ChessPair<Position, Piece>(pos24,pion24);
            this.piese_detinute.add(de_adaugat24);
            //turn
            Position pos25 = new Position('A',p_piesa);
            Piece turn3 = new Rook(Colors.Black,pos25);
            ChessPair<Position, Piece> de_adaugat25 = new ChessPair<Position, Piece>(pos25,turn3);
            this.piese_detinute.add(de_adaugat25);

            Position pos26 = new Position('H',p_piesa);
            Piece turn4 = new Rook(Colors.Black,pos26);
            ChessPair<Position, Piece> de_adaugat26 = new ChessPair<Position, Piece>(pos26,turn4);
            this.piese_detinute.add(de_adaugat26);
            //nebun
            Position pos27 = new Position('C',p_piesa);
            Piece nebun3 = new Bishop(Colors.Black,pos27);
            ChessPair<Position, Piece> de_adaugat27 = new ChessPair<Position, Piece>(pos27,nebun3);
            this.piese_detinute.add(de_adaugat27);

            Position pos28 = new Position('F',p_piesa);
            Piece nebun4 = new Bishop(Colors.Black,pos28);
            ChessPair<Position, Piece> de_adaugat28 = new ChessPair<Position, Piece>(pos28,nebun4);
            this.piese_detinute.add(de_adaugat28);
            //cal
            Position pos29 = new Position('B',p_piesa);
            Piece cal3 = new Knight(Colors.Black,pos29);
            ChessPair<Position, Piece> de_adaugat29 = new ChessPair<Position, Piece>(pos29,cal3);
            this.piese_detinute.add(de_adaugat29);

            Position pos30= new Position('G',p_piesa);
            Piece cal4 = new Knight(Colors.Black,pos30);
            ChessPair<Position, Piece> de_adaugat30 = new ChessPair<Position, Piece>(pos30,cal4);
            this.piese_detinute.add(de_adaugat30);
            //Dama
            Position pos31 = new Position('D',p_piesa);
            Piece dama2= new Queen(Colors.Black,pos31);
            ChessPair<Position, Piece> de_adaugat31 = new ChessPair<Position, Piece>(pos31,dama2);
            this.piese_detinute.add(de_adaugat31);
            //rege
            Position pos32 = new Position('E',p_piesa);
            Piece rege2 = new King(Colors.Black,pos32);
            ChessPair<Position, Piece> de_adaugat32 = new ChessPair<Position, Piece>(pos32,rege2);
            this.piese_detinute.add(de_adaugat32);
        }
    }
    public int makeMove(Position from, Position to, Board board) throws Exception {
        int valid = 0;
        //prima data vad ca e mutare valida
        if(board.isValidMove(from,to)){
            Piece piesa = board.getPieceAt(from);
            //apoi ca e piesa jucatorului curent
            if(this.col != piesa.getColor()){
                throw new InvalidMoveException("Celalat jucator muta");
            }
            else{
                // se ia piesa de pe pozitia respectiva
                ChessPair<Position, Piece> caut = new ChessPair<Position, Piece>(from,piesa);
                for(ChessPair<Position, Piece> iterator : this.piese_detinute){
                    if(iterator.compareTo(caut) == 0){
                        Piece capturat = board.getPieceAt(to);
                        if(capturat != null){
                            if(capturat.getColor() != piesa.getColor()){
                                //daca se captureaza se adauga puncte
                                capt.add(capturat);
                                if(capturat.type() == 'P'){
                                    this.puncte += 10;
                                }
                                if(capturat.type() == 'N'){
                                    this.puncte += 30;
                                }
                                if(capturat.type() == 'B'){
                                    this.puncte += 30;
                                }
                                if(capturat.type() == 'R'){
                                    this.puncte += 50;
                                }
                                if(capturat.type() == 'Q'){
                                    this.puncte += 90;
                                }
                            }
                        }
                        //se muta piesa
                        this.piese_detinute.remove(caut);
                        caut.setKey(to);
                        this.piese_detinute.add(caut);
                        break;
                    }
                }
                // se baga in lista de piese capturate
                Piece de_capturat = board.getPieceAt(to);
                if(de_capturat != null && board.getPieceAt(to).getColor() != board.getPieceAt(from).getColor()){
                    this.capt.add(de_capturat);
                    ChessPair<Position,Piece> de_eliminat = new ChessPair<>(to,de_capturat);
                    board.tset.remove(de_eliminat);
                }
                if(piesa.type() == 'K'){
                    // se tine minte pos rege(pt sah si mat)
                    regep = to;
                }
                board.movePiece(from, to);
                valid = 1;
                int vf = 1;
                // se verifica toate piesele inamicului
                // daca dupa mutare esti in sah, nu ai voie sa muti
                for(int i = 1; i <= 8; i ++){
                    for(int j = 0; j < 8; j ++){
                        char c = 'A';
                        c += (char) j;
                        Position de_cautat = new Position(c,i);
                        Piece inamic = board.getPieceAt(de_cautat);
                        if(inamic != null && this.col != inamic.getColor()){
                            if(board.isValidMove(de_cautat,regep)){
                                vf = 0;
                                break;
                            }
                        }
                    }
                }
                if(vf == 0){
                    if(piesa.type() == 'K'){
                        regep = from;
                    }
                    //daca esti in sah dupa mutare nu ai voie sa muti
                    board.movePiece(to, from);
                    valid = 0;
                    return valid;
                    //throw new InvalidMoveException("Mutare imposibila esti in sah");
                }
            }
        }
        //System.out.println("Mutarea nu este valida");
        return valid;
    }
    public List<Piece> getCapturedPieces(){
        return this.capt;
    }
    public String getEmail() {
        return name;
    }

    public TreeSet<ChessPair<Position, Piece>> getOwnedPieces(){
        return this.piese_detinute;
    }
    public void removePiece(Position p,Piece pi){
        ChessPair<Position, Piece> chp = new ChessPair<>(p,pi);
        piese_detinute.remove(chp);
    }
    public void addPiece(Position p,Piece pi){
        ChessPair<Position, Piece> chp = new ChessPair<>(p,pi);
        piese_detinute.add(chp);
    }
    public Colors getCol(){
        return col;
    }
    public void setPoints(int points){
        this.puncte = points;
    }
    public int getPoints(){
        return this.puncte;
    }
}
