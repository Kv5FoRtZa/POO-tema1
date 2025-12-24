import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Board {
    public TreeSet<ChessPair<Position, Piece>> tset;
    public TreeSet<ChessPair<Position, Piece>> getAllPieces(){
        return tset;
    }
    public Board(){
        this.tset = new TreeSet<>();
    }
    public Board(Board b){
        this.tset = b.tset;
    }
    public Piece getPieceAt (Position obj) {
        for (ChessPair<Position, Piece> chessPair : tset) {
            if (chessPair.getKey().equals(obj)) {
                return chessPair.getValue();
            }
        }
        return null;
    }
    public void setPieceAt(Position obj, Piece pic) {
        ChessPair<Position, Piece> toRemove = null;
        for (ChessPair<Position, Piece> chessPair : tset) {
            if (chessPair.getKey().equals(obj)) {
                toRemove = chessPair;
                break;
            }
        }
        if (toRemove != null) {
            tset.remove(toRemove);
        }
        tset.add(new ChessPair<>(obj, pic));
    }
    public void initialise() throws InvalidMoveException {
        //aici initializez o tabla noua
        //iau fiecare piesa si o pun pe pozitia ei de start
        // se face acest lucru de 32 de ori(fiecare piesa)
        int p_pion = 2;
        int p_piesa = 1;
        //pioni
        Position pos = new Position('A',p_pion);
        Piece pion = new Pawn(Colors.White,pos);
        ChessPair<Position, Piece> de_adaugat = new ChessPair<Position, Piece>(pos,pion);
        this.tset.add(de_adaugat);

        Position pos2 = new Position('B',p_pion);
        Piece pion2 = new Pawn(Colors.White,pos2);
        ChessPair<Position, Piece> de_adaugat2 = new ChessPair<Position, Piece>(pos2,pion2);
        this.tset.add(de_adaugat2);

        Position pos3 = new Position('C',p_pion);
        Piece pion3 = new Pawn(Colors.White,pos3);
        ChessPair<Position, Piece> de_adaugat3 = new ChessPair<Position, Piece>(pos3,pion3);
        this.tset.add(de_adaugat3);

        Position pos4 = new Position('D',p_pion);
        Piece pion4 = new Pawn(Colors.White,pos4);
        ChessPair<Position, Piece> de_adaugat4 = new ChessPair<Position, Piece>(pos4,pion4);
        this.tset.add(de_adaugat4);

        Position pos5 = new Position('E',p_pion);
        Piece pion5 = new Pawn(Colors.White,pos5);
        ChessPair<Position, Piece> de_adaugat5 = new ChessPair<Position, Piece>(pos5,pion5);
        this.tset.add(de_adaugat5);

        Position pos6 = new Position('F',p_pion);
        Piece pion6 = new Pawn(Colors.White,pos6);
        ChessPair<Position, Piece> de_adaugat6 = new ChessPair<Position, Piece>(pos6,pion6);
        this.tset.add(de_adaugat6);

        Position pos7 = new Position('G',p_pion);
        Piece pion7 = new Pawn(Colors.White,pos7);
        ChessPair<Position, Piece> de_adaugat7 = new ChessPair<Position, Piece>(pos7,pion7);
        this.tset.add(de_adaugat7);

        Position pos8 = new Position('H',p_pion);
        Piece pion8 = new Pawn(Colors.White,pos8);
        ChessPair<Position, Piece> de_adaugat8 = new ChessPair<Position, Piece>(pos8,pion8);
        this.tset.add(de_adaugat8);
        //turn
        Position pos9 = new Position('A',p_piesa);
        Piece turn = new Rook(Colors.White,pos9);
        ChessPair<Position, Piece> de_adaugat9 = new ChessPair<Position, Piece>(pos9,turn);
        this.tset.add(de_adaugat9);

        Position pos10 = new Position('H',p_piesa);
        Piece turn2 = new Rook(Colors.White,pos10);
        ChessPair<Position, Piece> de_adaugat10 = new ChessPair<Position, Piece>(pos10,turn2);
        this.tset.add(de_adaugat10);
        //nebun
        Position pos11 = new Position('C',p_piesa);
        Piece nebun = new Bishop(Colors.White,pos11);
        ChessPair<Position, Piece> de_adaugat11 = new ChessPair<Position, Piece>(pos11,nebun);
        this.tset.add(de_adaugat11);

        Position pos12 = new Position('F',p_piesa);
        Piece nebun2 = new Bishop(Colors.White,pos12);
        ChessPair<Position, Piece> de_adaugat12 = new ChessPair<Position, Piece>(pos12,nebun2);
        this.tset.add(de_adaugat12);
        //cal
        Position pos13 = new Position('B',p_piesa);
        Piece cal = new Knight(Colors.White,pos13);
        ChessPair<Position, Piece> de_adaugat13 = new ChessPair<Position, Piece>(pos13,cal);
        this.tset.add(de_adaugat13);

        Position pos14 = new Position('G',p_piesa);
        Piece cal2 = new Knight(Colors.White,pos14);
        ChessPair<Position, Piece> de_adaugat14 = new ChessPair<Position, Piece>(pos14,cal2);
        this.tset.add(de_adaugat14);
        //Dama
        Position pos15 = new Position('D',p_piesa);
        Piece dama = new Queen(Colors.White,pos15);
        ChessPair<Position, Piece> de_adaugat15 = new ChessPair<Position, Piece>(pos15,dama);
        this.tset.add(de_adaugat15);
        //rege
        Position pos16 = new Position('E',p_piesa);
        Piece rege = new King(Colors.White,pos16);
        ChessPair<Position, Piece> de_adaugat16 = new ChessPair<Position, Piece>(pos16,rege);
        this.tset.add(de_adaugat16);
        //negru
        p_pion = 7;
        p_piesa = 8;
        //pioni
        Position pos17 = new Position('A',p_pion);
        Piece pion17 = new Pawn(Colors.Black,pos17);
        ChessPair<Position, Piece> de_adaugat17 = new ChessPair<Position, Piece>(pos17,pion17);
        this.tset.add(de_adaugat17);

        Position pos18 = new Position('B',p_pion);
        Piece pion18 = new Pawn(Colors.Black,pos18);
        ChessPair<Position, Piece> de_adaugat18 = new ChessPair<Position, Piece>(pos18,pion18);
        this.tset.add(de_adaugat18);

        Position pos19 = new Position('C',p_pion);
        Piece pion19 = new Pawn(Colors.Black,pos19);
        ChessPair<Position, Piece> de_adaugat19 = new ChessPair<Position, Piece>(pos19,pion19);
        this.tset.add(de_adaugat19);

        Position pos20 = new Position('D',p_pion);
        Piece pion20 = new Pawn(Colors.Black,pos20);
        ChessPair<Position, Piece> de_adaugat20 = new ChessPair<Position, Piece>(pos20,pion20);
        this.tset.add(de_adaugat20);

        Position pos21 = new Position('E',p_pion);
        Piece pion21 = new Pawn(Colors.Black,pos21);
        ChessPair<Position, Piece> de_adaugat21 = new ChessPair<Position, Piece>(pos21,pion21);
        this.tset.add(de_adaugat21);

        Position pos22 = new Position('F',p_pion);
        Piece pion22 = new Pawn(Colors.Black,pos22);
        ChessPair<Position, Piece> de_adaugat22 = new ChessPair<Position, Piece>(pos22,pion22);
        this.tset.add(de_adaugat22);

        Position pos23 = new Position('G',p_pion);
        Piece pion23 = new Pawn(Colors.Black,pos23);
        ChessPair<Position, Piece> de_adaugat23 = new ChessPair<Position, Piece>(pos23,pion23);
        this.tset.add(de_adaugat23);

        Position pos24 = new Position('H',p_pion);
        Piece pion24 = new Pawn(Colors.Black,pos24);
        ChessPair<Position, Piece> de_adaugat24 = new ChessPair<Position, Piece>(pos24,pion24);
        this.tset.add(de_adaugat24);
        //turn
        Position pos25 = new Position('A',p_piesa);
        Piece turn3 = new Rook(Colors.Black,pos25);
        ChessPair<Position, Piece> de_adaugat25 = new ChessPair<Position, Piece>(pos25,turn3);
        this.tset.add(de_adaugat25);

        Position pos26 = new Position('H',p_piesa);
        Piece turn4 = new Rook(Colors.Black,pos26);
        ChessPair<Position, Piece> de_adaugat26 = new ChessPair<Position, Piece>(pos26,turn4);
        this.tset.add(de_adaugat26);
        //nebun
        Position pos27 = new Position('C',p_piesa);
        Piece nebun3 = new Bishop(Colors.Black,pos27);
        ChessPair<Position, Piece> de_adaugat27 = new ChessPair<Position, Piece>(pos27,nebun3);
        this.tset.add(de_adaugat27);

        Position pos28 = new Position('F',p_piesa);
        Piece nebun4 = new Bishop(Colors.Black,pos28);
        ChessPair<Position, Piece> de_adaugat28 = new ChessPair<Position, Piece>(pos28,nebun4);
        this.tset.add(de_adaugat28);
        //cal
        Position pos29 = new Position('B',p_piesa);
        Piece cal3 = new Knight(Colors.Black,pos29);
        ChessPair<Position, Piece> de_adaugat29 = new ChessPair<Position, Piece>(pos29,cal3);
        this.tset.add(de_adaugat29);

        Position pos30= new Position('G',p_piesa);
        Piece cal4 = new Knight(Colors.Black,pos30);
        ChessPair<Position, Piece> de_adaugat30 = new ChessPair<Position, Piece>(pos30,cal4);
        this.tset.add(de_adaugat30);
        //Dama
        Position pos31 = new Position('D',p_piesa);
        Piece dama2= new Queen(Colors.Black,pos31);
        ChessPair<Position, Piece> de_adaugat31 = new ChessPair<Position, Piece>(pos31,dama2);
        this.tset.add(de_adaugat31);
        //rege
        Position pos32 = new Position('E',p_piesa);
        Piece rege2 = new King(Colors.Black,pos32);
        ChessPair<Position, Piece> de_adaugat32 = new ChessPair<Position, Piece>(pos32,rege2);
        this.tset.add(de_adaugat32);
    }
    public void movePiece(Position from, Position to) {
        //movePiece e folosit la verificarea de sah mat
        //e o mutare fortata, care elimina si readauga o piesa
        //il folosesc deoarece la verificarea de sah trebuie sa mut fara a captura
        Piece piesa;
        piesa = getPieceAt(from);
        ChessPair<Position,Piece> pereche = new ChessPair<>(from,piesa);
        this.tset.remove(pereche);
        piesa.setPosition(to);
        ChessPair<Position,Piece> pereche_add = new ChessPair<>(to,piesa);
        this.tset.add(pereche_add);
    }

    public boolean isValidMove(Position from, Position to) throws Exception {
        if(!from.inside()) {
            throw new InvalidMoveException("Pozitia initiala nu e pe tabla");
        }
        if(!to.inside()) {
            throw new InvalidMoveException("Pozitia finala nu e pe tabla");
        }
        if(to.inside()){
            //daca e inauntru
            //verific sa am o piesa care se muta
            //apoi verific ce tip de piesa exista acolo
            //ii caut toate mutarile posibile
            //si daca to este in lista de mutari posibile mut
            Piece p = getPieceAt(from);
            p.setPosition(from);
            List<Position> ivan = new ArrayList<>();
            if(p == null){
                throw new InvalidMoveException("Nu ai o piesa pe pozitia ceruta");
            }
            if(p.type() == 'P'){
                ivan = p.getPossibleMoves(this);
                for(Position i : ivan){
                    if(i.compareTo(to) == 0){
                        return true;
                    }
                }
            }
            else if(p.type() == 'N'){
                ivan = p.getPossibleMoves(this);
                for(Position i : ivan){
                    if(i.compareTo(to) == 0){
                        return true;
                    }
                }
                return false;
            }
            else if(p.type() == 'B'){
                ivan = p.getPossibleMoves(this);
                for(Position i : ivan){
                    if(i.compareTo(to) == 0){
                        return true;
                    }
                }
            }
            else if(p.type() == 'R'){
                ivan = p.getPossibleMoves(this);
                for(Position i : ivan){
                    if(i.compareTo(to) == 0){
                        return true;
                    }
                }
            }
            else if(p.type() == 'Q'){
                ivan = p.getPossibleMoves(this);
                for(Position i : ivan){
                    if(i.compareTo(to) == 0){
                        return true;
                    }
                }
            }
            else if(p.type() == 'K'){
                ivan = p.getPossibleMoves(this);
                for(Position i : ivan){
                    if(i.compareTo(to) == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
