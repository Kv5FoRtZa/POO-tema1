import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;

import java.util.List;
public class Main {
    static int nr_jocuri = 0;
    static List<User> utilizatori = new ArrayList<>();
    static Map<Integer, Game> ceva = new HashMap<>();
    static Map<Integer, Game> games = new HashMap<>();
    static User current_user;
    private static Main single_inst = null;
    Position selected = null;
    int cnt, puncte,mat,af = 0,puncte_de_afisat;
    Board b1,b2,b3;
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
    public void highlightMovesForSelected( Game joc, Colors col,List<Position> highlighted,Map<Position, JPanel> squareByPos, JFrame app) throws InvalidMoveException {
        clearHighlightsOnly(highlighted,squareByPos);

        Piece piece = joc.tabla.getPieceAt(selected);
        if (piece == null) {
            selected = null;
            return;
        }

        List<Position> moves = piece.getPossibleMoves(joc.tabla);

        // highlight selection
        JPanel selSq = squareByPos.get(selected);
        if (selSq != null) {
            selSq.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        }

        // highlight targets
        for (Position m : moves) {
            JPanel sq = squareByPos.get(m);
            if (sq != null) {
                sq.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                highlighted.add(m);
            }
        }

        app.revalidate();
        app.repaint();
    }
    public void clearHighlightsOnly( List<Position> highlighted,Map<Position, JPanel> squareByPos) {
        // clear highlighted target borders
        for (Position p : highlighted) {
            JPanel sq = squareByPos.get(p);
            if (sq != null) {
                sq.setBorder(null);
            }
        }
        highlighted.clear();

        // clear selected border
        if (selected != null) {
            JPanel selSq = squareByPos.get(selected);
            if (selSq != null) {
                selSq.setBorder(null);
            }
        }
    }
    public void clearHighlightsAndSelection(List<Position> highlighted,Map<Position, JPanel> squareByPos) {
        clearHighlightsOnly(highlighted,squareByPos);
        selected = null;
    }
    public void onSquareClicked(Position clicked, Game joc, Colors col, List<Position> highlighted,Map<Position, JPanel> squareByPos, JFrame app, Main sah,JTextArea history,JLabel p1, JLabel p2, JLabel pct1,JLabel fin) throws Exception {
        if(mat == 1 || mat == 2)
        {
            return;
        }
        boolean remiza = true;
        if(b1 == b2 && b3 == b2){
            remiza = joc.draw();
            PointsStrategy pct = new FinalPointsStrategy();
            puncte += pct.modificaPunctaj(null,1);
            mat = 2;
            fin.setText("Partida terminata prin remiza. Va rugam apasati pe tabla inca o data");
            return;
            //remiza pt cazul cu 3 pozitii consecutive cumune
        }
        if(cnt == 1){
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
                            if(piesa.type() == 'K'){
                                int i = 1;
                                i ++;
                                i --;
                            }
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
            // first click: must be a human piece
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

        // second click: if clicked is a highlighted target -> attempt move
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

                // computer random move (after UI updates)
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
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    if(reusit2 != 0){
                                        b3 = b2;
                                        b2 = b1;
                                        b1 = new Board(joc.tabla);
                                        if(de_eliminat != null){
                                            AlegeCaracter lol = new AlegeCaracter();
                                            if(de_eliminat.getColor() == Colors.White){
                                                p2.setText(p2.getText() + lol.alege(Colors.White,de_eliminat.type()));
                                            }
                                            if(de_eliminat.getColor() == Colors.Black){
                                                p1.setText(p1.getText() + lol.alege(Colors.Black,de_eliminat.type()));
                                            }
                                        }
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
        // not a target: allow reselecting another own piece
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
        JTextArea history = new JTextArea(15, 18);
        history.setEditable(true);
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
                JPanel square = new JPanel(new BorderLayout());
                if((i + j) % 2 == 0){
                    square.setBackground(new Color(244, 165, 72));
                }
                else{
                    square.setBackground(new Color(243, 215, 168));
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
                        if(mat == 1 || mat == 2) {
                            if(af == 1){
                                return;
                            }
                            af = 1;
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
        renunta.addActionListener(e -> {
            try {
                joc.resign(true);
                PointsStrategy pct = new FinalPointsStrategy();
                puncte += pct.modificaPunctaj(null,-1);
                mat = 1;
                FinalPartida(app,sah,"Ai renuntat!");
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
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        siesi.addActionListener(e -> {
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
            MainMenu(app,sah);
            try {
                write();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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
         * **/
            if (ce_faci == 1) {
                //conectare
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
                submit.addActionListener(e -> {
                    String user = userField.getText();
                    String pass = new String(passField.getPassword());
                    //verifica sa existe utilizatorul
                    for (User u : utilizatori) {
                        if (u.getEmail().equals(user) && u.getPassword().equals(pass)) {
                            current_user = u;
                            System.out.println("Conectare reusita");
                            LoadGames ld = new LoadGames();
                            current_user = ld.exec(current_user,games);
                            MainMenu(frame,sah);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(frame,
                            "Utilizator sau parola gresite",
                            "Eroare",
                            JOptionPane.ERROR_MESSAGE);
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
                submit.addActionListener(e -> {
                    String user = userField.getText();
                    String pass = new String(passField.getPassword());
                    if(user.isEmpty() || pass.isEmpty()){
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
                        utilizatori.add(newAccount(user, pass));
                        LoadGames ld = new LoadGames();
                        current_user = ld.exec(current_user,games);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(frame, "Cont creat cu succes!");
                    MainMenu(frame,sah);
                    return;
                });
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
    public void run(Colors col, Colors calc, Game daca_este_load, JFrame app,Main sah) throws Exception {
        mat = 0;
        af = 0;
        utilizatori.remove(current_user);
        Game joc;
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
            if(ultim_col == Colors.White) {
                cnt = 1;
            }
            else{
                cnt = 0;
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
        puncte = 0;
        b1 = joc.tabla;
        b2 = null;
        b3 = null;
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
        Integer[] ids = games.stream().map(Game::getId).toArray(Integer[]::new);
        Integer selected = (Integer) JOptionPane.showInputDialog(frame, "Alegeti un joc", "Continua joc", JOptionPane.PLAIN_MESSAGE, null, ids, ids[0]
        );
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
        // while(true){
        if(current_user == null){
            int vf = 0;
            log.addActionListener(e -> {
                try {
                    //se apeleaza functia veche de login
                    login(1,aplicatie,sah);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            cont.addActionListener(e -> {
                try {
                    login(2,aplicatie,sah);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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
        newGame.addActionListener(e -> {
            try {
                AlegeCuloarea(frame,sah);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        loadGame.addActionListener(e -> {
            try {
                LoadGame(frame,sah);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        logout.addActionListener(e -> {
            current_user = null;
            frame.getContentPane().removeAll();
            inceput(frame,sah);
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
        JLabel label2 = new JLabel("Punctele castigate in aceasta partida sunt " + puncte_curente);
        JLabel userInfo = new JLabel("Utilizator: " + current_user.getEmail() + " | Punctaj: " + current_user.getPoints());
        userInfo.setBounds(1200, 20, 380, 30);
        label1.setBounds(700, 200, 300, 50);
        label2.setBounds(670, 500, 300, 50);
        JButton newGame = new JButton("Mergi catre meniul principal");
        newGame.setBounds(650, 300, 300, 50);
        panel.add(userInfo);
        panel.add(newGame);
        panel.add(label1);
        panel.add(label2);
        JButton inchide = new JButton("Inchide aplicatia");
        panel.add(inchide);
        inchide.setBounds(650,370,300,50);
        inchide.addActionListener(new Close());
        newGame.addActionListener(e -> {
            try {
                MainMenu(frame,sah);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
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