import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.io.*;
import javax.swing.*;

import java.util.List;

import static java.lang.Math.abs;

public class Main {
    static int nr_jocuri = 0;
    static List<User> utilizatori = new ArrayList<>();
    static Map<Integer, Game> ceva = new HashMap<>();
    static Map<Integer, Game> games = new HashMap<>();
    static User current_user;
    private static Main single_inst = null;
    Position selected = null;
    int cnt, puncte,mat,af = 0,puncte_de_afisat;
    Board b1,b2,b3,b4,b5;
    private Main(){
    }
    public static Main getInstance()
    {// getinstance design pattern
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
    public void highlightMovesForSelected( Game joc, Colors col,List<Position> highlighted,Map<Position, JPanel> squareByPos, JFrame app) throws InvalidMoveException {
        //la click pe piesa trebuie sa apara niste highlighturi ca sa vezi ce ai selectat si asa
        clearHighlightsOnly(highlighted,squareByPos);
        Piece piece = joc.tabla.getPieceAt(selected);
        if (piece == null) {
            selected = null;
            return;
        }
        List<Position> moves = piece.getPossibleMoves(joc.tabla);
        // aici da highlight la piesa
        JPanel selSq = squareByPos.get(selected);
        if (selSq != null) {
            selSq.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        }
        // aici da la pozitiile posibilie de mutat
        // nu verifica sa nu fii in sah sau asa dar it is what it is
        for (Position m : moves) {
            JPanel sq = squareByPos.get(m);
            if (sq != null) {
                sq.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                highlighted.add(m);
            }
        }
        app.revalidate();
        app.repaint();
    }
    public void clearHighlightsOnly( List<Position> highlighted,Map<Position, JPanel> squareByPos) {
        //cand nu mai e click, trebuie sterse highlight-urile si de la pozitiile posibilie
        for (Position p : highlighted) {
            JPanel sq = squareByPos.get(p);
            if (sq != null) {
                sq.setBorder(null);
            }
        }
        highlighted.clear();
        // si de la piesa selectata
        if (selected != null) {
            JPanel selSq = squareByPos.get(selected);
            if (selSq != null) {
                selSq.setBorder(null);
            }
        }
    }
    public void clearHighlightsAndSelection(List<Position> highlighted,Map<Position, JPanel> squareByPos) {
        clearHighlightsOnly(highlighted,squareByPos);
        //apeleaza functia care sterge highlighturile si in plus deselecteaza si in memorie(nu doar vizual)
        selected = null;
    }
    public void onSquareClicked(Position clicked, Game joc, Colors col, List<Position> highlighted,Map<Position, JPanel> squareByPos, JFrame app, Main sah,JTextArea history,JLabel p1, JLabel p2, JLabel pct1,JLabel fin) throws Exception {
        if(mat == 1 || mat == 2)
        {// daca e remiza sau mat se face mat 2 sau 1, si nu vreau sa mai mute calculatorul/ sa mai poti juca dupa
            return;
        }
        boolean remiza = true;
        if(b1 != null && b1.tset != null && b2 != null && b2.tset != null && b3 != null && b3.tset != null && b4 != null && b4.tset != null && b5 != null && b5.tset != null){
            if(b1.getAllPieces().equals(b3.getAllPieces()) && b3.getAllPieces().equals(b5.getAllPieces())){
                remiza = joc.draw();
                PointsStrategy pct = new FinalPointsStrategy();
                puncte += pct.modificaPunctaj(null,1);
                mat = 2;
                fin.setText("Partida terminata prin remiza. Va rugam apasati pe tabla inca o data");
                return;
                //remiza pt cazul cu 3 pozitii consecutive cumune
            }
        }
        if(cnt == 1){
            //aici e doar daca muta calculatorul prima data
            //identic ca o mutare normala de calculator
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    TreeSet<ChessPair<Position, Piece>> piese_computer = joc.computer.getOwnedPieces();
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
                            List<Position> mutari_poibile= null;
                            try {
                                mutari_poibile = piesa.getPossibleMoves(joc.tabla);
                            } catch (InvalidMoveException e) {
                                throw new RuntimeException(e);
                            }
                            if(!mutari_poibile.isEmpty()){
                                //daca are mutari, fac o mutare random
                                Random random = new Random();
                                Position fin = mutari_poibile.get(random.nextInt(mutari_poibile.size()));
                                Piece de_eliminat = null;
                                if(joc.tabla.getPieceAt(fin) != null){
                                    de_eliminat = joc.tabla.getPieceAt(fin);
                                }
                                int reusit = 0;
                                try {
                                    reusit = joc.onMoveMade(joc.computer,pozz,fin,-1);
                                    if(de_eliminat != null){
                                        AlegeCaracter lol = new AlegeCaracter();
                                        if(de_eliminat.getColor() == Colors.White){
                                            p2.setText(p2.getText() + lol.alege(Colors.White,de_eliminat.type()));
                                        }
                                        if(de_eliminat.getColor() == Colors.Black){
                                            p1.setText(p1.getText() + lol.alege(Colors.Black,de_eliminat.type()));
                                        }
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                if(reusit != 0){
                                    cnt = 0;
                                    //schimba in regina
                                    if(col == Colors.Black && fin.Coord_lit == 8 && piesa.type() == 'P'){
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
                                    else if(col == Colors.White && fin.Coord_lit == 1 && piesa.type() == 'P'){
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
                                    //adaug in interfata de istoric de mutari
                                    if(!joc.miscari.isEmpty()){
                                        String s = "";
                                        s += joc.ultimaMiscare().getCuloare();
                                        s += ' ';
                                        s += joc.ultimaMiscare().getPlecat();
                                        s += ' ';
                                        s += joc.ultimaMiscare().getAjuns();
                                        s += '\n';
                                        history.append(s);
                                    }
                                    break;
                                }
                                joc.computer.removePiece(fin,piesa);
                                joc.computer.addPiece(pozz,piesa);
                                if(de_eliminat != null) {
                                    joc.p1.addPiece(fin,de_eliminat);
                                }
                                //si ies
                                //daca nu are mutari se alege alta piesa random
                                //nu o sa existe situatie fara mutari pt ca daca nu are mutari este mat si se verifica deja
                            }
                        }
                    }
                    for (Map.Entry<Position, JPanel> e : squareByPos.entrySet()) {
                        Position p = e.getKey();
                        JPanel sq = e.getValue();
                        //aici reface tabla, deseneaza noua piesa pe noul ei loc(da remove la toate si apoi le repune)
                        sq.removeAll();
                        Piece piece = joc.tabla.getPieceAt(p);
                        if (piece != null) {
                            AlegeCaracter cv = new AlegeCaracter();
                            String ceva = "";
                            ceva += cv.alege(joc.tabla.getPieceAt(p).getColor(),joc.tabla.getPieceAt(p).type());
                            JLabel ceva2 = new JLabel(ceva, SwingConstants.CENTER);
                            ceva2.setFont(new Font("", Font.PLAIN, 50));
                            sq.add(ceva2, BorderLayout.CENTER);
                        }
                    }
                    app.revalidate();
                    app.repaint();
                }
            });
        }
        Piece pieceAtClicked = joc.tabla.getPieceAt(clicked);
        if (selected == null) {
            //verifica/ asteapta pana ai selectat o piesa
            //si face highlight-urile pe tabla conform functiilor de highlight
            if (pieceAtClicked != null) {
                if (pieceAtClicked.getColor() == col) {
                    selected = clicked;
                    highlightMovesForSelected( joc, col,highlighted, squareByPos, app);
                } else {
                    clearHighlightsAndSelection(highlighted,squareByPos);
                }
            } else {
                clearHighlightsAndSelection(highlighted,squareByPos);
            }
            return;
        }
        //daca dai click pe una din alea highlighted aka una din alea posibile te muti
        boolean isTarget = false;
        for (Position p : highlighted) {
            if (p.equals(clicked)) {
                isTarget = true;
                break;
            }
        }
        if (isTarget) {
            if(joc.checkForCheckMate(joc.p1)){
                System.out.println("Calculatorul a catigat prin mat");
                PointsStrategy pct = new FinalPointsStrategy();
                puncte += pct.modificaPunctaj(null,-2);
                mat = 1;
                fin.setText("Partida terminata prin mat. Va rugam apasati pe tabla inca o data");
                return;
            }
            //se face mutarea selectata de player
            int reusit = 0;
            Piece de_eliminat = null;
            if(joc.tabla.getPieceAt(clicked) != null){
                de_eliminat = joc.tabla.getPieceAt(clicked);
            }
            try {
                reusit = joc.onMoveMade(joc.p1, selected, clicked,1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (reusit != 0) {
                // se fac verificarile daca au fost capturate piese si se baga in interfata de piese capturate
                if(de_eliminat != null){
                    AlegeCaracter lol = new AlegeCaracter();
                    if(de_eliminat.getColor() == Colors.White){
                        p2.setText(p2.getText() + lol.alege(Colors.White,de_eliminat.type()));
                    }
                    if(de_eliminat.getColor() == Colors.Black){
                        p1.setText(p1.getText() + lol.alege(Colors.Black,de_eliminat.type()));
                    }
                    int ppct = joc.p1.getPoints();
                    String pppct = "";
                    pppct += ppct;
                    pct1.setText(pppct);
                }
                //istoricul de mutari interfata
                if(!joc.miscari.isEmpty()){
                    String s = "";
                    s += joc.ultimaMiscare().getCuloare();
                    s += ' ';
                    s += joc.ultimaMiscare().getPlecat();
                    s += ' ';
                    s += joc.ultimaMiscare().getAjuns();
                    s += '\n';
                    history.append(s);
                }
                //transformarea(identic ca la calculator si ca la varianta veche in terminal)
                if(col == Colors.White && clicked.Coord_num == 8 && joc.tabla.getPieceAt(clicked).type() == 'P'){
                    ChessPair<Position,Piece> chpr = new ChessPair<>();
                    chpr.setValue(joc.tabla.getPieceAt(clicked));
                    chpr.setKey(clicked);
                    joc.tabla.tset.remove(chpr);
                    joc.p1.removePiece(clicked,joc.tabla.getPieceAt(clicked));
                    Piece nou = Factory.setez("Queen",clicked,col);
                    joc.p1.addPiece(clicked,nou);
                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                    chpr2.setValue(nou);
                    chpr2.setKey(clicked);
                    joc.tabla.tset.add(chpr2);
                }
                if(col == Colors.Black && clicked.Coord_num == 1 && joc.tabla.getPieceAt(clicked).type() == 'P'){
                    ChessPair<Position,Piece> chpr = new ChessPair<>();
                    chpr.setValue(joc.tabla.getPieceAt(clicked));
                    chpr.setKey(clicked);
                    joc.tabla.tset.remove(chpr);
                    joc.p1.removePiece(clicked,joc.tabla.getPieceAt(clicked));
                    Piece nou = Factory.setez("Queen",clicked,col);
                    joc.p1.addPiece(clicked,nou);
                    ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                    chpr2.setValue(nou);
                    chpr2.setKey(clicked);
                    joc.tabla.tset.add(chpr2);
                }
                //dupa mutare se scot highlighturile
                //apoi se sterg piesele si pun din nou noile piese(cu o mutare diferit si maybe o piesa capturata)
                clearHighlightsAndSelection(highlighted,squareByPos);
                for (Map.Entry<Position, JPanel> e : squareByPos.entrySet()) {
                    Position p = e.getKey();
                    JPanel sq = e.getValue();
                    sq.removeAll();
                    Piece piece = joc.tabla.getPieceAt(p);
                    if (piece != null) {
                        AlegeCaracter cv = new AlegeCaracter();
                        String ceva = "";
                        ceva += cv.alege(joc.tabla.getPieceAt(p).getColor(),joc.tabla.getPieceAt(p).type());
                        JLabel ceva2 = new JLabel(ceva, SwingConstants.CENTER);
                        ceva2.setFont(new Font("", Font.PLAIN, 50));
                        sq.add(ceva2, BorderLayout.CENTER);
                    }
                }
                app.revalidate();
                app.repaint();
                //dupa ce muta playerul si termina de mutat, apoi muta calculatorul iar
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        TreeSet<ChessPair<Position, Piece>> piese_computer = joc.computer.getOwnedPieces();
                        try {
                            if(joc.checkForCheckMate(joc.computer)){
                                System.out.println("Jucatorul a catigat prin mat");
                                PointsStrategy pct = new FinalPointsStrategy();
                                puncte += pct.modificaPunctaj(null,1);
                                fin.setText("Partida terminata prin mat. Va rugam apasati pe tabla inca o data");
                                mat = 1;
                                return;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        //din nou, logica veche de la jocul din terminal
                        //ia o piesa random si o muta
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
                                List<Position> mutari_poibile= null;
                                try {
                                    mutari_poibile = piesa.getPossibleMoves(joc.tabla);
                                } catch (InvalidMoveException e) {
                                    throw new RuntimeException(e);
                                }
                                if(!mutari_poibile.isEmpty()){
                                    //daca are mutari, fac o mutare random
                                    Random random = new Random();
                                    Position fin = mutari_poibile.get(random.nextInt(mutari_poibile.size()));
                                    Piece de_eliminat = null;
                                    if(joc.tabla.getPieceAt(fin) != null) {
                                        de_eliminat = joc.tabla.getPieceAt(fin);
                                    }
                                    int reusit2 = 0;
                                    try {
                                        reusit2 = joc.onMoveMade(joc.computer,pozz,fin,-1);
                                        if(reusit2 == 0){
                                            //sa nu se salveze in interfata de mutari unele mutari invalide
                                            joc.miscari.removeLast();
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    if(reusit2 != 0){
                                        //tablele vechi, pt verificarea de remiza
                                        b5 = new Board(b4);
                                        b4 = new Board(b3);
                                        b3 = new Board(b2);
                                        b2 = new Board(b1);
                                        b1 = new Board(joc.tabla);
                                        //interfata eliminat identic ca la player
                                        if(de_eliminat != null){
                                            AlegeCaracter lol = new AlegeCaracter();
                                            if(de_eliminat.getColor() == Colors.White){
                                                p2.setText(p2.getText() + lol.alege(Colors.White,de_eliminat.type()));
                                            }
                                            if(de_eliminat.getColor() == Colors.Black){
                                                p1.setText(p1.getText() + lol.alege(Colors.Black,de_eliminat.type()));
                                            }
                                        }
                                        //din nou interfata de istoric de mutari
                                        if(!joc.miscari.isEmpty()){
                                            String s = "";
                                            s += joc.ultimaMiscare().getCuloare();
                                            s += ' ';
                                            s += joc.ultimaMiscare().getPlecat();
                                            s += ' ';
                                            s += joc.ultimaMiscare().getAjuns();
                                            s += '\n';
                                            history.append(s);
                                        }
                                        cnt = 0;
                                        //schimba in regina
                                        if(col == Colors.Black && fin.Coord_num == 8 && joc.tabla.getPieceAt(fin).type() == 'P'){
                                            ChessPair<Position,Piece> chpr = new ChessPair<>();
                                            chpr.setValue(joc.tabla.getPieceAt(fin));
                                            chpr.setKey(fin);
                                            joc.tabla.tset.remove(chpr);
                                            joc.computer.removePiece(fin,joc.tabla.getPieceAt(fin));
                                            Piece nou = Factory.setez("Queen",fin,Colors.White);
                                            joc.computer.addPiece(fin,nou);
                                            ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                            chpr2.setValue(nou);
                                            chpr2.setKey(fin);
                                            joc.tabla.tset.add(chpr2);
                                        }
                                        else if(col == Colors.White && fin.Coord_num == 1 && joc.tabla.getPieceAt(fin).type() == 'P'){
                                            ChessPair<Position,Piece> chpr = new ChessPair<>();
                                            chpr.setValue(joc.tabla.getPieceAt(fin));
                                            chpr.setKey(fin);
                                            joc.tabla.tset.remove(chpr);
                                            joc.computer.removePiece(fin,joc.tabla.getPieceAt(fin));
                                            Piece nou = Factory.setez("Queen",fin,Colors.Black);
                                            joc.computer.addPiece(fin,nou);
                                            ChessPair<Position,Piece> chpr2 = new ChessPair<>();
                                            chpr2.setValue(nou);
                                            chpr2.setKey(fin);
                                            joc.tabla.tset.add(chpr2);
                                        }
                                        break;
                                    }
                                    joc.computer.removePiece(fin,piesa);
                                    joc.computer.addPiece(pozz,piesa);
                                    //si ies
                                    //daca nu are mutari se alege alta piesa random
                                    //nu o sa existe situatie fara mutari pt ca daca nu are mutari este mat si se verifica deja
                                }
                            }
                        }
                        //din nou, se steg toate piesele si repun cele noi cu o mutare diferita
                        for (Map.Entry<Position, JPanel> e : squareByPos.entrySet()) {
                            Position p = e.getKey();
                            JPanel sq = e.getValue();
                            sq.removeAll();
                            Piece piece = joc.tabla.getPieceAt(p);
                            if (piece != null) {
                                AlegeCaracter cv = new AlegeCaracter();
                                String ceva = "";
                                ceva += cv.alege(joc.tabla.getPieceAt(p).getColor(),joc.tabla.getPieceAt(p).type());
                                JLabel ceva2 = new JLabel(ceva, SwingConstants.CENTER);
                                ceva2.setFont(new Font("", Font.PLAIN, 50));
                                sq.add(ceva2, BorderLayout.CENTER);
                            }
                        }
                        app.revalidate();
                        app.repaint();
                    }
                });
            } else {
                //aici e daca playerul nu a fost capabil sa faca o mutare
                //adica a dat click pe ceva highlighted dar imposibil
                //ca de exemplu, dupa mutarea dorita ramanaea in sah
                //atunci ramane tura lui
                clearHighlightsAndSelection(highlighted,squareByPos);
                for (Map.Entry<Position, JPanel> e : squareByPos.entrySet()) {
                    Position p = e.getKey();
                    JPanel sq = e.getValue();
                    sq.removeAll();
                    Piece piece = joc.tabla.getPieceAt(p);
                    if (piece != null) {
                        AlegeCaracter cv = new AlegeCaracter();
                        String ceva = "";
                        ceva += cv.alege(joc.tabla.getPieceAt(p).getColor(),joc.tabla.getPieceAt(p).type());
                        JLabel ceva2 = new JLabel(ceva, SwingConstants.CENTER);
                        ceva2.setFont(new Font("", Font.PLAIN, 50));
                        sq.add(ceva2, BorderLayout.CENTER);
                    }
                }
                app.revalidate();
                app.repaint();
            }
            return;
        }
        //daca nu e in stare sa faca o mutare din orice motiv(invalid, click pe altceva) atunci se sterg highlighturile dupa un click pe ceva invalid
        if (pieceAtClicked != null) {
            if (pieceAtClicked.getColor() == col) {
                clearHighlightsAndSelection(highlighted,squareByPos);
                selected = clicked;
                highlightMovesForSelected( joc, col, highlighted, squareByPos, app);
            } else {
                clearHighlightsAndSelection(highlighted,squareByPos);
            }
        } else {
            clearHighlightsAndSelection(highlighted,squareByPos);
        }
    }
    public void intialize(JFrame app, Colors col, Game joc, Main sah) throws InvalidMoveException {
        puncte = 0;
        JTextArea history = new JTextArea(15, 18);
        history.setEditable(true);
        if(joc.miscari != null){
            //daca dau load, vreau ca mutarile vechi sa apara in istoricul din interfata
            for(Move m : joc.miscari){
                String s = "";
                s += m.getCuloare();
                s += ' ';
                s += m.getPlecat();
                s += ' ';
                s += m.getAjuns();
                s += '\n';
                history.append(s);
            }
        }
        //interfata de piese capturate
        JLabel piese_capturate_alb_1 = new JLabel("Piesele capturate de alb sunt:");
        JLabel piese_capturate_alb_2 = new JLabel("");
        JLabel piese_capturate_negru_1 = new JLabel("Piesele capturate de negru sunt:");
        JLabel piese_capturate_negru_2 = new JLabel("");
        JLabel puncte1 = new JLabel("Punctele catigate in aceasta partida sunt:");
        JLabel fin = new JLabel("Partida este in desfasurare");
        piese_capturate_negru_2.setFont(new Font("", Font.PLAIN, 25));
        piese_capturate_alb_2.setFont(new Font("", Font.PLAIN, 25));
        JLabel puncte2 = new JLabel("0");
        JPanel print_tabla = new JPanel(new GridLayout(9, 9));
        print_tabla.setPreferredSize(new Dimension(900, 900));
        print_tabla.add(new JLabel(""));
        for(int i = 0; i < 8; i ++){
            String literas = "";
            if(col == Colors.White){
                char litera = ((char)('a' + i));
                literas += litera;
            }
            else{
                char litera = ((char)('h' - i));
                literas += litera;
            }
            JLabel fileLabel = new JLabel(String.valueOf(literas), SwingConstants.CENTER);
            fileLabel.setFont(new Font("Arial", Font.BOLD, 14));
            print_tabla.add(fileLabel);
        }
        //se printeaza literele si cidrele in jurul tablei(daca e alb jucatorul e 1 jos, altfel e 8 jos
        //literele sunt mereu la fel
        List<Position> highlighted = new ArrayList<>();
        Map<Position, JPanel> squareByPos = new HashMap<>();
        for(int i = 8; i >= 1; i --){
            String literas = "";
            if(col == Colors.White){
                int litera = (i);
                literas += litera;
            }
            else{
                int litera = (9 - i);
                literas += litera;
            }
            JLabel fileLabel = new JLabel(String.valueOf(literas), SwingConstants.CENTER);
            fileLabel.setFont(new Font("Arial", Font.BOLD, 14));
            print_tabla.add(fileLabel);
            char lit;
            for(int j = 1; j <= 8; j ++){
                //se face tabla cu 2 culori alb si negru,care nu sunt chiar alb si negru dar nu conteaza
                JPanel square = new JPanel(new BorderLayout());
                if((i + j) % 2 == 0){
                    square.setBackground(new Color(244, 167, 67));
                }
                else{
                    square.setBackground(new Color(243, 215, 167));
                }
                int ln;
                if(col == Colors.White){
                    lit = (char)('A' + j - 1);
                    ln = i;
                }
                else {
                    lit = (char)('H' - j + 1);
                    ln = 9 - i;
                }
                Position p = new Position(lit,ln);
                if(joc.tabla.getPieceAt(p) != null){
                    //se bag sia si piesele pe pozitiile create(verific pt fiecare pozitie daca e o piesa pt ca pot fi jocuri loaded)
                    AlegeCaracter cv = new AlegeCaracter();
                    String ceva = "";
                    ceva += cv.alege(joc.tabla.getPieceAt(p).getColor(),joc.tabla.getPieceAt(p).type());
                    JLabel ceva2 = new JLabel(ceva,SwingConstants.CENTER);
                    ceva2.setFont(new Font("", Font.PLAIN, 50));
                    square.add(ceva2);
                }
                square.setOpaque(true);
                squareByPos.put(p, square);
                square.putClientProperty("pos", p);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        //aici se apeleaza jocul
                        //e un listener basically pe fiecare casuta, si reactioneaza la click pe tabla
                        if(mat == 1 || mat == 2) {
                            //daca e mat/remiza/terminat in alt mod, nu mai vreau sa mai inregistreze clickuri
                            if(af == 1){
                                //daca mai sunt listenere deschise, nu vreau sa scriu de 2 ori in json/ sa adun puncte de 2 ori
                                return;
                            }
                            af = 1;
                            if(current_user.getActiveGames().contains(joc)){
                                //daca e joc loaded, il scoatem din memorie
                                int nr = joc.getId();
                                current_user.removeGame(joc);
                                games.remove(joc);
                                Path path2 = Path.of("src/games.json");
                                try {
                                    JsonReaderUtil.writeGames(path2,games);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            // logica de puncte, se aduna, se salveaza alea de afisat
                            puncte_de_afisat = puncte;
                            puncte_de_afisat += joc.p1.getPoints();
                            puncte += current_user.getPoints();
                            puncte += joc.p1.getPoints();
                            current_user.setPoints(puncte);
                            Path path = Path.of("src/accounts.json");
                            utilizatori.add(current_user);
                            try {
                                UserJsonWriter.writeUsers(path, utilizatori);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            //se da load la ecranul de dinal de partida
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(mat == 1) {
                                        FinalPartida(app, sah,"Partida terminata prin sah mat");
                                    }
                                    else{
                                        FinalPartida(app, sah,"Partida terminata prin remiza");
                                    }
                                }
                            });
                            return;
                        }
                        JPanel src = (JPanel) e.getSource();
                        Position clicked = (Position) src.getClientProperty("pos");
                        try {
                            if(mat == 1) {
                                return;
                            }
                            //dupa ce am facut toate verificarile daca nu e gata partida, apelez functia care muta/ da highlight/etc
                            onSquareClicked(clicked,joc,col,highlighted,squareByPos,app,sah,history,piese_capturate_alb_2,piese_capturate_negru_2,puncte2,fin);
                            if(mat == 1) {
                                return;
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                print_tabla.add(square);
            }
        }
        //butoanele secunare
        JPanel ecran_total = new JPanel(new BorderLayout());
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JButton renunta = new JButton("Renunta");
        JButton siesi = new JButton("Salveaza si iesi");
        JLabel status = new JLabel("Fa o mutare sau apasa tabla");
        renunta.setBounds(300, 20, 100, 50);
        siesi.setBounds(170, 20, 100, 50);
        right.add(status);
        right.add(Box.createVerticalStrut(10));
        left.add(Box.createVerticalStrut(10));
        right.add(renunta);
        right.add(siesi);
        renunta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //la renuntare, se fac punctele si salvarea in json
                    //identic ca la mat/ remiza
                    if (current_user.getActiveGames().contains(joc)) {
                        int nr = joc.getId();
                        current_user.removeGame(joc);
                        games.remove(joc);
                        Path path2 = Path.of("src/games.json");
                        try {
                            JsonReaderUtil.writeGames(path2, games);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    joc.resign(true);
                    PointsStrategy pct = new FinalPointsStrategy();
                    puncte += pct.modificaPunctaj(null, -1);
                    mat = 1;
                    puncte_de_afisat = puncte;
                    puncte_de_afisat += joc.p1.getPoints();
                    FinalPartida(app, sah, "Ai renuntat!");
                    puncte += current_user.getPoints();
                    puncte += joc.p1.getPoints();
                    current_user.setPoints(puncte);
                    Path path = Path.of("src/accounts.json");
                    utilizatori.add(current_user);
                    try {
                        UserJsonWriter.writeUsers(path, utilizatori);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        siesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //save and quit aici
                //iesi, si salvezi in lista de jocuri deschise in json
                int vf = 0;
                int nr = 0;
                if (current_user.getActiveGames().contains(joc)) {
                    nr = joc.getId();
                    current_user.removeGame(joc);
                    games.remove(joc);
                    vf = 1;
                }
                if (vf == 0) {
                    nr = games.size();
                    nr++;
                }
                joc.setId(nr);
                current_user.addGame(joc);
                games.put(nr, joc);
                Path path = Path.of("src/accounts.json");
                try {
                    utilizatori.add(current_user);
                    UserJsonWriter.writeUsers(path, utilizatori);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //te intorci in meniu aici
                MainMenu(app, sah);

                try {
                    write();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        //update si bagat toate composnentele
        JScrollPane scroll = new JScrollPane(history);
        right.add(scroll);
        left.add(piese_capturate_alb_1);
        left.add(piese_capturate_alb_2);
        left.add(piese_capturate_negru_1);
        left.add(piese_capturate_negru_2);
        left.add(puncte1);
        left.add(puncte2);
        left.add(fin);
        ecran_total.add(print_tabla, BorderLayout.CENTER);
        ecran_total.add(right, BorderLayout.EAST);
        ecran_total.add(left, BorderLayout.WEST);
        app.setContentPane(ecran_total);
        app.pack();
        app.revalidate();
        app.repaint();
    }
    public void write() throws IOException {
        //scrie in json
        Path path = Path.of("src/games.json");
        JsonReaderUtil.writeGames(path, games);
    }
    public static void login(int ce_faci, JFrame frame, Main sah) throws IOException {
        System.out.println("A inceput procesul de logare");
        //am aceasi functie si pt cont nou si pt cont deja existent
        // se creaza o zona unde se pune userul si parola
        //folosesc aceleasi verificari ca in terminal(daca nu exista deja contul/ daca e cont inexistent)
        //cand se da click cu succes pe logheaza-te, se incarca meniul principal
        /**DACA MERGE LOGARE CU UN CONT NULL(nu e scris nimic si la parola si la cont) inseamna ca exista un cont null in json
         * momentan un cont null nu mai poate fi adaugat, insa posibil sa fi ramas conturi proaste salvate deja in jsonuri
         * in timpul testarii am avut mai multe buguri legate de conturi, am avut 4 sau 5 conturi cu acelasi nume si aceasi parola
         * daca apar conturi dubioase, redescarca arhiva oficiala si refa cele 2 jsonuri
         * unele conturi pot avea in istoric partide imposibile, cu mai multe mutari pe aceasi culoare, din nou sterge jsonurile si redescarca-le
         * **/
            if (ce_faci == 1) {
                //conectare
                //apare username si parola si buton de loghin
                frame.getContentPane().removeAll();
                JPanel panel = new JPanel(null);
                panel.setBounds(0, 0, 1600, 900);
                JLabel userLabel = new JLabel("Username:");
                JLabel passLabel = new JLabel("Parola:");
                JTextField userField = new JTextField();
                JPasswordField passField = new JPasswordField();
                JButton submit = new JButton("Login");
                userLabel.setBounds(620, 280, 100, 30);
                userField.setBounds(700, 280, 250, 30);
                passLabel.setBounds(620, 330, 100, 30);
                passField.setBounds(700, 330, 250, 30);
                submit.setBounds(700, 380, 250, 40);
                panel.add(userLabel);
                panel.add(userField);
                panel.add(passLabel);
                panel.add(passField);
                panel.add(submit);
                frame.add(panel);
                frame.revalidate();
                frame.repaint();
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String user = userField.getText();
                        String pass = new String(passField.getPassword());
                        //verifica sa existe utilizatorul
                        for (User u : utilizatori) {
                            if (u.getEmail().equals(user) && u.getPassword().equals(pass)) {
                                current_user = u;
                                System.out.println("Conectare reusita");
                                LoadGames ld = new LoadGames();
                                current_user = ld.exec(current_user, games);
                                MainMenu(frame, sah);
                                return;
                            }
                        }
                        //daca nu a apelat main menu e clar eroare
                        JOptionPane.showMessageDialog(frame, "Utilizator sau parola gresite", "Eroare", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else if (ce_faci == 2) {
                //cont nou
                frame.getContentPane().removeAll();
                JPanel panel = new JPanel(null);
                panel.setBounds(0, 0, 1600, 900);
                JLabel userLabel = new JLabel("Setati un username:");
                JLabel passLabel = new JLabel("Setati o parola:");
                JTextField userField = new JTextField();
                JPasswordField passField = new JPasswordField();
                JButton submit = new JButton("Adauga Contul");
                userLabel.setBounds(620, 280, 100, 30);
                userField.setBounds(700, 280, 250, 30);
                passLabel.setBounds(620, 330, 100, 30);
                passField.setBounds(700, 330, 250, 30);
                submit.setBounds(700, 380, 250, 40);
                panel.add(userLabel);
                panel.add(userField);
                panel.add(passLabel);
                panel.add(passField);
                panel.add(submit);
                frame.add(panel);
                frame.revalidate();
                frame.repaint();
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String user = userField.getText();
                        String pass = new String(passField.getPassword());
                        if (user.isEmpty() || pass.isEmpty()) {
                            //verifica sa nu fie conturi null
                            JOptionPane.showMessageDialog(frame, "Utilizator nul sau parola inexistenta", "Eroare", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        for (User u : utilizatori) {
                            //verifica sa nu existe deja utilizatorul
                            if (u.getEmail().equals(user)) {
                                JOptionPane.showMessageDialog(frame, "Utilizator deja existent", "Eroare", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        try {
                            //new account face contul nou
                            newAccount(user, pass);
                            LoadGames ld = new LoadGames();
                            current_user = ld.exec(current_user, games);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(frame, "Cont creat cu succes!");
                        MainMenu(frame, sah);
                    }
                });
            }
    }
    public static User newAccount(String email, String password) throws IOException {
        //se adauga un cont nou si se salveza in json
        //se rescriu jsonurile
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
    public void run(Colors col, Colors calc, Game daca_este_load, JFrame app,Main sah) throws Exception {
        mat = 0;
        af = 0;
        utilizatori.remove(current_user);
        Game joc;
        //aici se incarca jocul din memorie/ se creeaza un joc nou
        if(daca_este_load == null){
            //se incarca o tabla noua daca nu se da load la un joc
            joc = new Game(1);
            if(col == Colors.White)
            {
                cnt = 0;
            }
            else {
                cnt = 1;
            }
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
            //cine muta?
            if(ultim_col == Colors.White) {
                if(col == Colors.White){
                    cnt = 1;
                }
                else {
                    cnt = 0;
                }
            }
            else{
                if(col == Colors.White){
                    cnt = 0;
                }
                else {
                    cnt = 1;
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
        //incarc tablele pt vf de remiza
        puncte = 0;
        b1 = joc.tabla;
        b2 = null;
        b3 = null;
        b4 = null;
        b5 = null;
        intialize(app,col,joc,sah);
    }
    private static void LoadGame(JFrame frame, Main sah) throws Exception {
        List<Game> games = current_user.getActiveGames();
        // aici se ia lista de jocuri incarcata din memorie/json
        //si se poate alege un joc(daca exista ofc)
        if (games.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nu exista jocuri salvate", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Integer[] ids = new Integer[games.size()];
        for (int i = 0; i < games.size(); i++) {
            ids[i] = games.get(i).getId();
        }
        Integer selected = (Integer) JOptionPane.showInputDialog(frame, "Alegeti un joc", "Continua joc", JOptionPane.PLAIN_MESSAGE, null, ids, ids[0]);
        if (selected == null) return;
        for (Game g : games) {
            if (g.getId() == selected) {
                //daca exista ii dau run
                sah.run(g.p1.col, g.computer.col, g,frame,sah);
                return;
            }
        }
    }
    private static void AlegeCuloarea(JFrame frame, Main sah) throws Exception {
        //alege culoarea inainte de a incepe un nou joc
        Object[] options = {"Alb", "Negru"};
        int choice = JOptionPane.showOptionDialog(frame, "Alegeti culoarea", "Joc nou", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            sah.run(Colors.White, Colors.Black, null,frame,sah);
        } else if (choice == 1) {
            sah.run(Colors.Black, Colors.White, null,frame,sah);
        }
    }
    public static void inceput(JFrame aplicatie, Main sah){
        //ecranul de logare
        // 3 butoane aka logheaza-te, cont nou si inchide
        JButton log = new JButton("Logeaza-te");
        JButton cont = new JButton("Creeaza un cont nou");
        JButton inchide = new JButton("Inchide aplicatia");
        log.setBounds(650,300,300,50);
        cont.setBounds(650,370,300,50);
        inchide.setBounds(650,440,300,50);
        aplicatie.add(log);
        aplicatie.add(cont);
        aplicatie.add(inchide);
        aplicatie.revalidate();
        aplicatie.repaint();
        inchide.addActionListener(new Close());
        //incarca login-ul cu varianta aleasa
        if (current_user == null) {
            int vf = 0;
            log.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        //se apeleaza functia veche de login
                        login(1, aplicatie, sah);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            cont.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        login(2, aplicatie, sah);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }
    public static void MainMenu(JFrame frame, Main sah) {
        //meniul principal
        //are buton de incepe un joc nou, de a incarca un joc vechi, de delogare si de iesire
        //in colt se vede numele utilizatorului si punctajul.
        //frame.getContentPane().removeAll();
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, 1600, 900);
        //iti vezi si punctele
        JLabel userInfo = new JLabel("Utilizator: " + current_user.getEmail() + " | Punctaj: " + current_user.getPoints());
        userInfo.setBounds(1200, 20, 380, 30);
        JButton newGame = new JButton("Incepe joc nou");
        JButton loadGame = new JButton("Continua joc");
        JButton logout = new JButton("Delogare");
        newGame.setBounds(650, 300, 300, 50);
        loadGame.setBounds(650, 370, 300, 50);
        logout.setBounds(650, 440, 300, 50);
        panel.add(userInfo);
        panel.add(newGame);
        panel.add(loadGame);
        panel.add(logout);
        JButton inchide = new JButton("Inchide aplicatia");
        panel.add(inchide);
        inchide.setBounds(650,510,300,50);
        inchide.addActionListener(new Close());
        // joc nou = alegi o culoare si apoi joci
        //load = alegi la ce dai load si apoi joci
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AlegeCuloarea(frame, sah);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LoadGame(frame, sah);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current_user = null;
                frame.getContentPane().removeAll();
                inceput(frame, sah);
            }
        });
        frame.setContentPane(panel);
        frame.setSize(1600, 900);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }
    public static void FinalPartida(JFrame frame, Main sah, String s) {
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, 1600, 900);
        JLabel label1 = new JLabel(s);
        int puncte_curente = sah.puncte_de_afisat;
        JLabel label2;
        //afisez punctele castigate/pierdute
        if(puncte_curente >= 0)
        {
            label2 = new JLabel("Punctele castigate in aceasta partida sunt " + puncte_curente);
        }
        else {
            label2 = new JLabel("Punctele pierdute in aceasta partida sunt " + abs(puncte_curente));
        }
        //punctele vechi
        JLabel userInfo = new JLabel("Utilizator: " + current_user.getEmail() + " | Punctaj: " + current_user.getPoints());
        userInfo.setBounds(1200, 20, 380, 30);
        label1.setBounds(700, 200, 300, 50);
        label2.setBounds(670, 500, 300, 50);
        JButton newGame = new JButton("Mergi catre meniul principal");
        //intoarce-te sau iesi din aplicatie ca obtiuni
        newGame.setBounds(650, 300, 300, 50);
        panel.add(userInfo);
        panel.add(newGame);
        panel.add(label1);
        panel.add(label2);
        JButton inchide = new JButton("Inchide aplicatia");
        panel.add(inchide);
        inchide.setBounds(650,370,300,50);
        inchide.addActionListener(new Close());
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainMenu(frame,sah);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.setContentPane(panel);
        frame.setSize(1600, 900);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }
    public static void main (String[] args) throws Exception {
        Main sah = Main.getInstance();
        sah.read();
        //in games e json games
        //ofc in utlizatori e json utilizatori
        System.out.println("Bine ai venit");
        JFrame aplicatie = new JFrame();
        aplicatie.setSize(1600,900);
        aplicatie.setLayout(null);
        aplicatie.setVisible(true);
        //ruleaza ecranul de logare
        inceput(aplicatie,sah);
    }
}