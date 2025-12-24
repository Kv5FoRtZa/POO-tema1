import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public final class JsonReaderUtil {

    private JsonReaderUtil() {}

    public static Map<Integer, Game> readGamesAsMap(Path path)
            throws IOException, ParseException {

        Map<Integer, Game> map = new HashMap<>();

        if (path == null || !Files.exists(path)) {
            return map;
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            JSONParser parser = new JSONParser();
            JSONArray gamesArr = (JSONArray) parser.parse(reader);

            for (Object gObj : gamesArr) {
                JSONObject gJson = (JSONObject) gObj;

                int id = ((Long) gJson.get("id")).intValue();
                Game game = new Game(id);

                JSONArray playersArr = (JSONArray) gJson.get("players");

                JSONObject p1Json = (JSONObject) playersArr.get(0);
                JSONObject p2Json = (JSONObject) playersArr.get(1);

                game.p1 = new Player(
                        (String) p1Json.get("email"),
                        parseColor((String) p1Json.get("color"))
                );

                game.computer = new Player(
                        (String) p2Json.get("email"),
                        parseColor((String) p2Json.get("color"))
                );

                JSONArray boardArr = (JSONArray) gJson.get("board");
                for (Object bObj : boardArr) {
                    JSONObject bJson = (JSONObject) bObj;

                    char type = ((String) bJson.get("type")).charAt(0);
                    Colors color = parseColor((String) bJson.get("color"));

                    String posStr = (String) bJson.get("position");
                    Position pos = new Position(
                            posStr.charAt(0),
                            Character.getNumericValue(posStr.charAt(1))
                    );

                    Piece piece = createPiece(type, color, pos);
                    game.tabla.setPieceAt(pos, piece);
                }

                JSONArray movesArr = (JSONArray) gJson.get("moves");
                if (movesArr != null) {
                    for (Object mObj : movesArr) {
                        JSONObject mJson = (JSONObject) mObj;

                        Colors col = parseColor((String) mJson.get("playerColor"));

                        Position from = parsePosition((String) mJson.get("from"));
                        Position to = parsePosition((String) mJson.get("to"));

                        Piece moved = game.tabla.getPieceAt(to);
                        game.miscari.add(new Move(col, from, to, moved));
                    }
                }

                map.put(id, game);
            }
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
    public static void writeGames(Path path, Map<Integer, Game> games)
            throws IOException {

        JSONArray gamesArray = new JSONArray();

        for (Game game : games.values()) {
            JSONObject gJson = new JSONObject();

            gJson.put("id", game.getId());

            // -------- players --------
            JSONArray playersArr = new JSONArray();
            playersArr.add(playerToJson(game.p1));
            playersArr.add(playerToJson(game.computer));
            gJson.put("players", playersArr);

            // -------- board --------
            JSONArray boardArr = new JSONArray();
            for (ChessPair<Position, Piece> cp : game.tabla.getAllPieces()) {
                Piece p = cp.getValue();
                JSONObject pieceJson = new JSONObject();

                pieceJson.put("type", String.valueOf(p.type()));
                pieceJson.put("color", p.getColor().name().toUpperCase());
                pieceJson.put("position", p.getPosition().toString());

                boardArr.add(pieceJson);
            }
            gJson.put("board", boardArr);

            // -------- moves --------
            JSONArray movesArr = new JSONArray();
            for (Move m : game.miscari) {
                JSONObject mJson = new JSONObject();

                mJson.put("playerColor", m.getCuloare().name().toUpperCase());
                mJson.put("from", m.getPlecat().toString());
                mJson.put("to", m.getAjuns().toString());

                movesArr.add(mJson);
            }
            gJson.put("moves", movesArr);

            gamesArray.add(gJson);
        }

        Files.write(
                path,
                gamesArray.toJSONString().getBytes(StandardCharsets.UTF_8)
        );
    }


    private static JSONObject playerToJson(Player p) {
        JSONObject obj = new JSONObject();
        obj.put("email", p.getEmail());
        obj.put("color", p.getCol().name().toUpperCase());
        return obj;
    }

    private static Colors parseColor(String s) {
        return s.equalsIgnoreCase("WHITE") ? Colors.White : Colors.Black;
    }

    private static Position parsePosition(String s) throws InvalidMoveException {
        return new Position(s.charAt(0), Character.getNumericValue(s.charAt(1)));
    }

    private static Piece createPiece(char t, Colors c, Position p) {
        return switch (t) {
            case 'P' -> Factory.setez("Pawn",p,c);
            case 'R' -> Factory.setez("Rook",p,c);
            case 'N' -> Factory.setez("Knight",p,c);
            case 'B' -> Factory.setez("Bishop",p,c);
            case 'Q' -> Factory.setez("Queen",p,c);
            case 'K' -> Factory.setez("King",p,c);
            default -> throw new IllegalArgumentException("Unknown piece: " + t);
        };
    }
}