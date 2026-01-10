import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Game implements GameObserver{
    int ident;
    Board tabla;
    Player p1,computer;
    List<Move> miscari;
    public Game(int id) {
        this.ident = id;
        this.tabla = new Board();
        this.p1 = new Player("player",Colors.Black);
        this.computer = new Player("calculator",Colors.Black);
        this.miscari = new ArrayList<>();
    }
    public Move ultimaMiscare(){
        return miscari.getLast();
    }
    public int getId(){
        return this.ident;
    }
    public void setId(int i){
        this.ident = i;
    }
    public void start() throws InvalidMoveException {
        this.miscari = new ArrayList<>();
        this.tabla.initialise();
    }
    public int resume(){
        //in main am o lista de jocuri
        //am nevoie de id-ul lui pt resume
        return this.ident;
    }
    public Player switchPlayer(Player p1){
        return this.computer;
    }
    public boolean checkForCheckMate(Player ply) throws Exception {
        //se iau toate piesele
        //se verifica mutarile lor posibilie
        //daca esti in sah dupa orice mutare = mat
        boolean vf = true;
        TreeSet<ChessPair<Position, Piece>> piese_computer = ply.getOwnedPieces();
        for(ChessPair<Position, Piece> i : piese_computer){
            //aici se ia piesa
            Position pos = i.getKey();
            Piece piesa = tabla.getPieceAt(pos);
            if(!piesa.getPossibleMoves(tabla).isEmpty()){
                for(Position catre : piesa.getPossibleMoves(tabla)){
                    Piece capt = tabla.getPieceAt(catre);//piesa capturata
                    Piece se_muta = null;//piesa mutata
                    if(capt != null){
                        se_muta = piesa;
                        tabla.movePiece(pos,catre);
                        tabla.setPieceAt(catre,se_muta);
                        // captureaza o piesa
                    }
                    else{
                        tabla.movePiece(pos,catre);
                    }
                    if(piesa.type() == 'K'){
                        ply.regep = catre;
                    }
                    //aici e mutata piesa noua
                    int vf_intern = 1;//caut o singura mutare in urma careia nu exista un inamic care sa atace regele
                    for(int q = 1; q <= 8; q ++){
                        for(int j = 0; j < 8; j ++){
                            char c = 'A';
                            c += (char) j;
                            Position de_cautat = new Position(c,q);
                            Piece inamic = tabla.getPieceAt(de_cautat);
                            if(inamic != null && ply.col != inamic.getColor()){
                                if(tabla.isValidMove(de_cautat,ply.regep)){
                                    vf_intern = 0;
                                    break;
                                    //pe mutarea asta regele inca e in sah
                                }
                            }
                        }
                    }
                    //vf_intern ramane 1 daca regele nu e in sah
                    //apoi se reface pozitia oficiala
                    if(se_muta == null)
                    {
                        tabla.movePiece(catre,pos);
                    }
                    else{
                        tabla.setPieceAt(catre,capt);
                        ChessPair<Position, Piece> chp = new ChessPair<>();
                        chp.setKey(catre);
                        chp.setValue(capt);
                        tabla.tset.add(chp);
                        tabla.setPieceAt(pos,se_muta);
                    }
                    if(piesa.type() == 'K'){
                        ply.regep = pos;
                    }
                    if(vf_intern == 1){
                        //daca nu mai e in sah pt o mutare
                        vf = false;
                        //nu e sah mat
                        return vf;
                    }
                }
            }
        }
        return vf;
    }
    public boolean resign(boolean b){
        return b;
    }
    public boolean draw(){
        return false;
    }
    public int addMove(Player p, Position from, Position to) throws Exception {
        //se adauga o mutare in joc
        //apoi se face mutarea aceea pe tabla
        Piece  piesa = null;
        try{
            piesa = this.tabla.getPieceAt(from);
            if(piesa == null){
                throw new InvalidMoveException("Nu exista piesa pe aceasta pozitie");
            }
        }catch (InvalidMoveException e){
            System.out.println("Error: " + e.getMessage());
        }
        if(piesa != null){
            Colors col = p.getCol();
            Move miscare_de_adaugat = new Move(col,from,to,piesa);
            this.miscari.add(miscare_de_adaugat);
            return p.makeMove(from,to,tabla);
        }
        return 0;
    }

    @Override
    public int onMoveMade(Player p, Position from,Position to, int psc) throws Exception {
        if(tabla.getPieceAt(to) != null && tabla.getPieceAt(to).getColor() != p.col){
            if(psc == 1)
            {
                onPieceCaptured(tabla.getPieceAt(to), 1);
            }
            else{
                onPieceCaptured(tabla.getPieceAt(to), -1);
            }
        }
        return addMove(p,from,to);
    }

    @Override
    public void onPieceCaptured(Piece piece, int psc) {
        Position pos = piece.getPosition();
        if(psc == 1){
            computer.removePiece(pos,piece);
        }
        else{
            p1.removePiece(pos,piece);
        }
    }

    @Override
    public void onPlayerSwitch(Player currentPlayer) {
        switchPlayer(p1);
        //call switchPlayer
        //intoarce tabla
    }
}
