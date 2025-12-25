import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Scanner;


import java.util.List;
public class Main {
    static int nr_jocuri = 0;
    static List<User> utilizatori = new ArrayList<>();
    static Map<Integer, Game> ceva = new HashMap<>();
    static Map<Integer, Game> games = new HashMap<>();
    static User current_user;
    private static Main single_inst = null;
    private Main(){
    }
    public static Main getInstance()
    {
        if (single_inst == null)
            single_inst = new Main();

        return single_inst;
    }
    public void read() throws IOException, ParseException {
        //citeste din json
        Path path2 = Path.of("src/games.json");
        games = JsonReaderUtil.readGamesAsMap(path2);
        Path path = Path.of("src/accounts.json");
        utilizatori = UserJsonReader.readUsers(path);
    }
    public void write() throws IOException {
        //scrie in json
        Path path = Path.of("src/games.json");
        JsonReaderUtil.writeGames(path, games);
    }
    public static void login() throws IOException {
        System.out.println("Doresti sa te conectezi sau sa creezi un cont nou?");
        System.out.println("Pentru conectare apasati 1, pentru a crea un cont nou apasati 2");
        while (true) {
            //merge pana e logat
            Scanner myObj = new Scanner(System.in);
            String input = myObj.nextLine();
            if (input.charAt(0) == '1') {
                //conectare
                System.out.println("Introdu emailul");
                Scanner cont = new Scanner(System.in);
                String cont_nume_s = myObj.nextLine();
                System.out.println("Introdu parola");
                Scanner contp = new Scanner(System.in);
                String cont_parola_s = myObj.nextLine();
                int gasit = 0;
                for (User u : utilizatori) {
                    if (Objects.equals(u.getEmail(), cont_nume_s)) {
                        if (Objects.equals(u.getPassword(), cont_parola_s)) {
                            System.out.println("Conectare reusita");
                            current_user = u;
                            //daca exista contul esti logat
                            gasit = 1;
                            break;
                        }
                    }
                }
                if (gasit == 1) {
                    break;
                } else {
                    System.out.println("Utilizatorul sau parola gresite");
                    System.out.println("Pentru conectare apasati 1, pentru a crea un cont nou apasati 2");
                    //daca nu exista contul te intorci sus
                    //nu e exceptie
                }
            } else if (input.charAt(0) == '2') {
                //cont nou
                System.out.println("Seteaza numele contului");
                Scanner cont = new Scanner(System.in);
                String cont_nume_s = myObj.nextLine();
                System.out.println("Seteaza parola");
                Scanner contp = new Scanner(System.in);
                String cont_parola_s = myObj.nextLine();
                int gasit = 0;
                for (User u : utilizatori) {
                    if (Objects.equals(u.getEmail(), cont_nume_s)) {
                        if (Objects.equals(u.getPassword(), cont_parola_s)) {
                            System.out.println("Utilizatorul cu aceste date deja exista");
                            //nu poti face 2 conturi care deja exista
                            //nu e exceptie
                            gasit = 1;
                            break;
                        }
                    }
                }
                if (gasit == 0) {
                    //se face cont nou
                    utilizatori.add(newAccount(cont_nume_s, cont_parola_s));
                    break;
                } else {
                    System.out.println("Pentru conectare apasati 1, pentru aa crea un cont nou apasati 2");
                }
            } else {
                try{
                    throw new InvalidCommandException("Nu ai introdus o varianta valida");
                }catch(InvalidCommandException e){
                    System.out.println("Error: " + e.getMessage());
                }

            }
        }
    }
    public static User newAccount(String email, String password) throws IOException {
        //se adauga un cont nou si se salveza in json
        User user_nou = new User(email,password);
        user_nou.setPoints(0);
        user_nou.setGames(null);
        System.out.println("Cont nou creat cu succes");
        current_user = user_nou;
        Path path = Path.of("src/accounts.json");
        utilizatori.add(user_nou);
        UserJsonWriter.writeUsers(path, utilizatori);
        return user_nou;
    }
    public void run(Colors col, Colors calc, Game daca_este_load) throws Exception {
        utilizatori.remove(current_user);
        Game joc;
        int cnt;
        if(daca_este_load == null){
            //se incarca o tabla noua daca nu se da load la un joc
            joc = new Game(1);
            cnt = 0;
            joc.start();
            joc.p1 = new Player(current_user.getEmail(),col);
            joc.p1.initialise_pieces();
            joc.computer = new Player("calculator",calc);
            joc.computer.initialise_pieces();
        }
        else{
            //se ia tabla din istoric
            joc = daca_este_load;
            Move ultima_mutare = joc.miscari.getLast();
            Colors ultim_col = ultima_mutare.getCuloare();
            if(ultim_col == calc){
                if(col == Colors.White){
                    cnt = 0;
                }
                else{
                    cnt = 1;
                }
            }
            else{
                if(col == Colors.White){
                    cnt = 1;
                }
                else{
                    cnt = 0;
                }
            }
            //incarc piesele
            joc.p1 = new Player(current_user.getEmail(),col);
            joc.computer = new Player("calculator",calc);
            for(ChessPair<Position,Piece> cp : joc.tabla.tset){
                Position de_adaugat = cp.getKey();
                Piece piesa_de_adaugat = joc.tabla.getPieceAt(de_adaugat);
                if(piesa_de_adaugat.getColor() == col){
                    joc.p1.addPiece(de_adaugat,piesa_de_adaugat);
                    if(piesa_de_adaugat.type() == 'K'){
                        joc.p1.regep = de_adaugat;
                    }
                }
                else{
                    joc.computer.addPiece(de_adaugat,piesa_de_adaugat);
                    if(piesa_de_adaugat.type() == 'K'){
                        joc.computer.regep = de_adaugat;
                    }
                }
            }
        }
        int puncte = 0;
        Board b1,b2,b3;
        b1 = joc.tabla;
        b2 = null;
        b3 = null;
        while (true){
            boolean remiza = true;
            if(b1 == b2 && b3 == b2){
                remiza = joc.draw();
                //remiza pt cazul cu 3 pozitii consecutive cumune
            }
            if(!remiza){
                break;
            }
            if(cnt % 2 == 0 && col == Colors.White){
                //jucatorul este alb si muta
                if(joc.checkForCheckMate(joc.p1)){
                    System.out.println("Calculatorul a catigat prin mat");
                    PointsStrategy pct = new FinalPointsStrategy();
                    puncte += pct.modificaPunctaj(null,-2);
                    break;
                }
                //se afiseaza tabla
                System.out.println("Pozitia de pe tabla este:");
                for(int j = 8; j > 0; j --){
                    Position afs = new Position('A',j);
                    char lit = 'A';
                    System.out.print(afs.Coord_num + " | ");
                    for(int i = 0; i < 8; i ++)
                    {
                        afs.setCoord_lit(lit);
                        Piece piesa = joc.tabla.getPieceAt(afs);
                        if(piesa != null){
                            System.out.print(piesa.type() + "-");
                            if(piesa.getColor() == Colors.Black)
                            {
                                System.out.print("B ");
                            }
                            else{
                                System.out.print("W ");
                            }
                        }
                        else {
                            System.out.print("... ");
                        }
                        lit ++;
                    }
                    System.out.println(" |");
                }
                System.out.println("     A   B   C   D   E   F   G   H    ");
                System.out.println("Faceti o mutare sau selectati o piesa");
                System.out.println("Pentru a renunta scrieti renunt");
                System.out.println("Pentru a iesi din partida curenta si a putea reveni asupra ei scrieti ies");
                // se face o mutare/ iese / renunta
                Scanner myObj = new Scanner(System.in);
                String mutare = myObj.nextLine();
                int lungime = mutare.length();
                if(lungime == 5){
                    char litera = mutare.charAt(0);
                    int cifra = mutare.charAt(1) - 48;
                    Position from = null;
                    try{
                        from = new Position(litera,cifra);
                    }catch(InvalidMoveException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    char literaf = mutare.charAt(3);
                    int cifraf = mutare.charAt(4) - 48;
                    Position to = null;
                    try{
                        to = new Position(literaf,cifraf);
                    }catch(InvalidMoveException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    // daca muti piesa opusa, ramane mutarea ta
                    Piece de_eliminat = null;
                    int ok = 0;
                    if(joc.tabla.getPieceAt(to) != null){
                        if(joc.tabla.getPieceAt(to).getColor() != col){
                            de_eliminat = joc.tabla.getPieceAt(to);
                            ok = 1;
                        }
                    }
                    int reusit = 0;
                    if(from != null && to != null){
                        try{
                            reusit = joc.addMove(joc.p1,from,to);
                            Piece transformat = joc.tabla.getPieceAt(to);
                            if(to.Coord_num == 8 && transformat.type() == 'P'){
                                System.out.println("Alegeti in ce doriti sa se transforme: Regina,Tura,Nebun,Cal");
                                Scanner scn_trs = new Scanner(System.in);
                                String trs_str = myObj.nextLine();
                                ChessPair<Position,Piece> chpr = new ChessPair<>();
                                chpr.setValue(transformat);
                                chpr.setKey(to);
                                joc.tabla.tset.remove(chpr);
                                joc.p1.removePiece(to,transformat);
                                if(trs_str.equals("Regina")){
                                    Piece nou = Factory.setez("Queen",to,Colors.White);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else if(trs_str.equals("Tura")){
                                    Piece nou = Factory.setez("Rook",to,Colors.White);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else if(trs_str.equals("Nebun")){
                                    Piece nou = Factory.setez("Bishop",to,Colors.White);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else if(trs_str.equals("Cal")){
                                    Piece nou = Factory.setez("Knight",to,Colors.White);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else{
                                    System.out.println("Nu vrei sa transformi?");
                                    System.out.println("Ai ramas fara pion");
                                }
                            }
                            if(reusit == 0){
                                throw new InvalidMoveException("Mutarea nu a putut fi efectuata");
                            }
                        }catch(InvalidMoveException e){
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    if(reusit == 0){
                        cnt --;
                    }
                    else{
                        if(ok == 1)
                            joc.computer.removePiece(to,de_eliminat);
                    }
                }
                else if(lungime == 2){
                    //sa vezi mutarile posibile ale unei piese
                    // poti si pe a le unei piese adverse
                    char litera = mutare.charAt(0);
                    int cifra = mutare.charAt(1) - 48;
                    Position cautata = new Position(litera,cifra);
                    if(joc.tabla.getPieceAt(cautata) != null)
                    {
                        Piece piesa = joc.tabla.getPieceAt(cautata);
                        System.out.println(piesa.getPossibleMoves(joc.tabla));
                    }
                    else{
                        System.out.println("Nu exista piesa pe acea pozitie");
                    }
                    cnt --;
                }
                else if(mutare.equals("renunt")){
                    //renunta gen
                    joc.resign(true);
                    PointsStrategy pct = new FinalPointsStrategy();
                    puncte += pct.modificaPunctaj(null,-1);
                    break;
                }
                else if(mutare.equals("ies")){
                    int vf = 0;
                    int nr = 0;
                    if(current_user.getActiveGames().contains(joc)){
                        nr = joc.getId();
                        current_user.removeGame(joc);
                        games.remove(joc);

                        vf = 1;
                    }
                    if(vf == 0){
                        nr = games.size();
                        nr ++;
                    }
                    joc.setId(nr);
                    current_user.addGame(joc);
                    games.put(nr, joc);
                    write();
                    //jocul se salveaza in memorie
                    break;
                }
                else{
                    try{
                        throw new InvalidCommandException("Nu au fost introduse date de intrare corecte.");
                    }catch(InvalidCommandException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    cnt --;
                }
            }
            else if(cnt % 2 == 1 && col == Colors.Black){
                //acelasi lucru dar jucatorul este negru
                if(joc.checkForCheckMate(joc.p1)){
                    System.out.println("Calculatorul a catigat prin mat");
                    PointsStrategy pct = new FinalPointsStrategy();
                    puncte += pct.modificaPunctaj(null,-2);
                    break;
                }
                System.out.println("Pozitia de pe tabla este:");
                for(int j = 8; j > 0; j --){
                    Position afs = new Position('A',j);
                    char lit = 'A';
                    System.out.print(afs.Coord_num + " | ");
                    for(int i = 0; i < 8; i ++)
                    {
                        afs.setCoord_lit(lit);
                        Piece piesa = joc.tabla.getPieceAt(afs);
                        if(piesa != null){
                            System.out.print(piesa.type() + "-");
                            if(piesa.getColor() == Colors.Black)
                            {
                                System.out.print("B ");
                            }
                            else{
                                System.out.print("W ");
                            }
                        }
                        else {
                            System.out.print("... ");
                        }
                        lit ++;
                    }
                    System.out.println(" |");
                }
                System.out.println("     A   B   C   D   E   F   G   H    ");
                System.out.println("Faceti o mutare sau selectati o piesa");
                System.out.println("Pentru a renunta scrieti renunt");
                System.out.println("Pentru a iesi din partida curenta si a putea reveni asupra ei scrieti ies");
                Scanner myObj = new Scanner(System.in);
                String mutare = myObj.nextLine();
                int lungime = mutare.length();
                if(lungime == 5){
                    char litera = mutare.charAt(0);
                    int cifra = mutare.charAt(1) - 48;
                    Position from = null;
                    try{
                        from = new Position(litera,cifra);
                    }catch(InvalidMoveException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    char literaf = mutare.charAt(3);
                    int cifraf = mutare.charAt(4) - 48;
                    Position to = null;
                    try{
                        to = new Position(literaf,cifraf);
                    }catch(InvalidMoveException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    // daca muti piesa opusa, ramane mutarea ta
                    Piece de_eliminat = null;
                    int ok = 0;
                    if(joc.tabla.getPieceAt(to) != null){
                        if(joc.tabla.getPieceAt(to).getColor() != col){
                            de_eliminat = joc.tabla.getPieceAt(to);
                            ok = 1;
                        }
                    }
                    int reusit = 0;
                    if(from != null && to != null){
                        try{
                            reusit = joc.addMove(joc.p1,from,to);
                            Piece transformat = joc.tabla.getPieceAt(to);
                            if(to.Coord_num == 1 && transformat.type() == 'P'){
                                System.out.println("Alegeti in ce doriti sa se transforme: Regina,Tura,Nebun,Cal");
                                Scanner scn_trs = new Scanner(System.in);
                                String trs_str = myObj.nextLine();
                                ChessPair<Position,Piece> chpr = new ChessPair<>();
                                chpr.setValue(transformat);
                                chpr.setKey(to);
                                joc.tabla.tset.remove(chpr);
                                joc.p1.removePiece(to,transformat);
                                if(trs_str.equals("Regina")){
                                    Piece nou = Factory.setez("Queen",to,Colors.Black);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else if(trs_str.equals("Tura")){
                                    Piece nou = Factory.setez("Rook",to,Colors.Black);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else if(trs_str.equals("Nebun")){
                                    Piece nou = Factory.setez("Bishop",to,Colors.Black);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else if(trs_str.equals("Cal")){
                                    Piece nou = Factory.setez("Knight",to,Colors.Black);
                                    joc.p1.addPiece(to,nou);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(to);
                                    joc.tabla.tset.add(chpr2);
                                }
                                else{
                                    //pion pe ultima linie e inutil
                                    System.out.println("Nu vrei sa transformi?");
                                    System.out.println("Ai ramas fara pion");
                                }
                            }
                            if(reusit == 0){
                                throw new InvalidMoveException("Mutarea nu a putut fi efectuata");
                            }
                        }catch(InvalidMoveException e){
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    if(reusit == 0){
                        cnt --;
                    }
                    else{
                        if(ok == 1)
                            joc.computer.removePiece(to,de_eliminat);
                    }
                }
                else if(lungime == 2){
                    char litera = mutare.charAt(0);
                    int cifra = mutare.charAt(1) - 48;
                    Position cautata = new Position(litera,cifra);
                    if(joc.tabla.getPieceAt(cautata) != null)
                    {
                        Piece piesa = joc.tabla.getPieceAt(cautata);
                        System.out.println(piesa.getPossibleMoves(joc.tabla));
                    }
                    else{
                        System.out.println("Nu exista piesa pe acea pozitie");
                    }
                    cnt --;
                }
                else if(mutare.equals("renunt")){
                    joc.resign(true);
                    PointsStrategy pct = new FinalPointsStrategy();
                    puncte += pct.modificaPunctaj(null,-1);
                    break;
                }
                else if(mutare.equals("ies")){

                    int vf = 0;
                    int nr = 0;
                    if(current_user.getActiveGames().contains(joc)){
                        nr = joc.getId();
                        current_user.removeGame(joc);
                        games.remove(joc);

                        vf = 1;
                    }
                    if(vf == 0){
                        nr = games.size();
                        nr ++;
                    }
                    joc.setId(nr);
                    current_user.addGame(joc);
                    games.put(nr, joc);
                    write();
                    //jocul se salveaza in memorie
                    break;
                }
                else{
                    try{
                        throw new InvalidCommandException("Nu au fost introduse date de intrare corecte.");
                    }catch(InvalidCommandException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    cnt --;
                }
            }
            else{
                //muta calculatorul

                b3 = b2;
                b2 = b1;
                b1 = new Board(joc.tabla);
                // salvez ultimele 3 pozitii in caz de remiza
                TreeSet<ChessPair<Position, Piece>> piese_computer = joc.computer.getOwnedPieces();
                if(joc.checkForCheckMate(joc.computer)){
                    System.out.println("Jucatorul a catigat prin mat");
                    PointsStrategy pct = new FinalPointsStrategy();
                    puncte += pct.modificaPunctaj(null,1);
                    break;
                }
                int nr_piese = piese_computer.size();
                while(true) {
                    Random r = new Random();
                    //aleg o piesa random
                    int r1 = r.nextInt(nr_piese);
                    int cnt_int = 0;
                    ChessPair<Position, Piece> piesa_dorita = new ChessPair<Position, Piece>();
                    for(ChessPair<Position, Piece> i : piese_computer){
                        //iau piesa random
                        if(cnt_int == r1){
                            piesa_dorita = i;
                            break;
                        }
                        cnt_int ++;
                    }
                    if(piesa_dorita != null){
                        Position pozz = piesa_dorita.getKey();
                        Piece piesa = piesa_dorita.getValue();
                        if(piesa.type() == 'K'){
                            int i = 1;
                            i ++;
                            i --;
                        }
                        List<Position> mutari_poibile= piesa.getPossibleMoves(joc.tabla);
                        if(!mutari_poibile.isEmpty()){
                            //daca are mutari, fac o mutare random
                            Random random = new Random();
                            Position fin = mutari_poibile.get(random.nextInt(mutari_poibile.size()));
                            Piece de_eliminat = null;
                            int ok = 0;
                            if(joc.tabla.getPieceAt(fin) != null){
                                if(joc.tabla.getPieceAt(fin).getColor() != calc){
                                    de_eliminat = joc.tabla.getPieceAt(fin);
                                    ok = 1;
                                }
                            }
                            int reusit = joc.addMove(joc.computer,pozz,fin);
                            if(reusit != 0){
                                //schimba in regina
                                if(calc == Colors.White && fin.Coord_lit == 8 && piesa.type() == 'P'){
                                    joc.computer.removePiece(fin,piesa);
                                    Piece nou = Factory.setez("Queen",fin,Colors.White);
                                    joc.computer.addPiece(fin,nou);
                                    ChessPair<Position,Piece> chpr = new ChessPair<>();
                                    chpr.setValue(piesa);
                                    chpr.setKey(fin);
                                    joc.tabla.tset.remove(chpr);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(fin);
                                }
                                else if(calc == Colors.Black && fin.Coord_lit == 1 && piesa.type() == 'P'){
                                    joc.computer.removePiece(fin,piesa);
                                    Piece nou = Factory.setez("Queen",fin,Colors.Black);
                                    joc.computer.addPiece(fin,nou);
                                    ChessPair<Position,Piece> chpr = new ChessPair<>();
                                    chpr.setValue(piesa);
                                    chpr.setKey(fin);
                                    joc.tabla.tset.remove(chpr);
                                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                    chpr2.setValue(nou);
                                    chpr2.setKey(fin);
                                    joc.tabla.tset.add(chpr2);
                                }
                                if(ok == 1)
                                    joc.p1.removePiece(fin,de_eliminat);
                                break;
                            }
                            joc.computer.removePiece(fin,piesa);
                            joc.computer.addPiece(pozz,piesa);

                            //joc.addMove(joc.computer,fin,pozz);
                            //si ies
                            //daca nu are mutari se alege alta piesa random
                            //nu o sa existe situatie fara mutari pt ca daca nu are mutari este mat si se verifica deja
                        }
                    }
                }
            }
            cnt ++;

        }
        puncte += current_user.getPoints();
        puncte += joc.p1.getPoints();
        current_user.setPoints(puncte);
        Path path = Path.of("src/accounts.json");
        utilizatori.add(current_user);
        UserJsonWriter.writeUsers(path, utilizatori);
        //aici adaug punctajul nou in json
    }
    public static void main (String[] args) throws Exception {
        Main sah = Main.getInstance();
        sah.read();
        //in games e json games
        //ofc in utlizatori e json utilizatori
        System.out.println("Bine ai venit");
        while(true){
            if(current_user == null){
                login();
                List<Game> jocuri = current_user.getActiveGames();
                List<Game> jocuri_de_bagat = new ArrayList<>();
                for(Map.Entry<Integer, Game> iterator : games.entrySet()){
                    int id_joc = iterator.getKey();
                    Game joc = (iterator.getValue());
                    //am toate id-urile si jocurile din games
                    if(jocuri != null){
                        for(Game iterator2 : jocuri){
                            int id_intern = iterator2.getId();
                            if(id_joc == id_intern){
                                //jocul trebuie adaugat;
                                jocuri_de_bagat.add(joc);
                                break;
                            }
                        }
                    }
                }
                current_user.setGames(jocuri_de_bagat);
            }
            else{
                System.out.println("Pentru a incepe un nou joc apasa 1");
                System.out.println("Pentru a te deloga apasa 2");
                System.out.println("Pentru a continua un joc apasa 3");
                System.out.println("Pentru a va vedea punctajul apasati 4");
                Scanner myObj = new Scanner(System.in);
                String mutare = myObj.nextLine();
                if(mutare.charAt(0) == '4'){
                    System.out.println(current_user.getPoints());
                }
                else if(mutare.charAt(0) == '1'){
                    System.out.println("Pentru a juca cu alb scrie alb, iar pentru a juca cu negru scrie negru");
                    Scanner culoare = new Scanner(System.in);
                    String col = culoare.nextLine();
                    //incep o moua partida de la 0
                    if(Objects.equals(col, "alb")){
                        Colors h = Colors.White;
                        Colors clc = Colors.Black;
                        sah.run(h,clc,null);
                    }
                    else if(Objects.equals(col, "negru")){
                        Colors h = Colors.Black;
                        Colors clc = Colors.White;
                        sah.run(h,clc,null);
                    }
                    else{
                        try{
                            throw new InvalidCommandException("Nu au fost introduse date de intrare corecte. Introduceti alb sau negru");
                        }catch(InvalidCommandException e){
                            System.out.println("Error: " + e.getMessage());
                         }
                    }
                }
                else if(mutare.charAt(0) == '2'){
                    current_user = null;
                    //se delogheaza
                }
                else if(mutare.charAt(0) == '3'){
                    //aici trebuie sa dea load la un joc din memorie
                    if(current_user != null){
                        List<Game> g = current_user.getActiveGames();
                        if(g.size() != 0){
                            List<Integer> iduri = new ArrayList<>();
                            for(Game itr : g){
                                int id_joc = itr.getId();
                                iduri.add(id_joc);
                            }
                            System.out.println("Alegeti un joc pentru a fi coninuat dintre urmatoarele");
                            System.out.println(iduri);
                            Scanner id_scan = new Scanner(System.in);
                            int id_citit = id_scan.nextInt();
                            //alege un joc userul
                            System.out.println("Se va incarca jocul " + id_citit);
                            int cnt = 0;
                            int vf = 0;
                            for(Game itr : g){
                                int id_joc = itr.resume();
                                if(id_joc == id_citit)
                                {
                                    vf = 1;
                                    break;
                                }
                                cnt ++;

                            }
                            if(vf == 1){
                                //se deschide jocul ales
                                Game i_se_da_load = g.get(cnt);
                                sah.run(i_se_da_load.p1.col,i_se_da_load.computer.col,i_se_da_load);
                            }
                            else{
                                try{
                                    throw new InvalidCommandException("Nu au fost introduse date de intrare corecte. Introduceti 1 2 sau 3");
                                }catch(InvalidCommandException e){
                                    System.out.println("Error: " + e.getMessage());
                                }

                            }
                        }
                        else{
                            System.out.println("Nu exista jocuri incepute");
                        }
                    }
                }
                else{
                    try{
                        throw new InvalidCommandException("Nu au fost introduse date de intrare corecte. Introduceti 1 2 sau 3");
                    }catch(InvalidCommandException e){
                    System.out.println("Error: " + e.getMessage());
                    }
                }

            }
        }
    }
}

