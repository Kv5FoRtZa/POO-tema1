import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserJsonReader {

    public static List<User> readUsers(Path path)
            throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        List<User> users = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            JSONArray array = (JSONArray) parser.parse(reader);

            for (Object o : array) {
                JSONObject obj = (JSONObject) o;

                User user = new User();
                user.setEmail((String) obj.get("email"));
                user.setPassword((String) obj.get("password"));
                user.setPoints(((Long) obj.get("points")).intValue());

                JSONArray gamesArray = (JSONArray) obj.get("games");
                List<Game> games = new ArrayList<>();

                for (Object g : gamesArray) {
                    int gameId = ((Long) g).intValue();
                    games.add(new Game(gameId));
                }

                user.setGames(games);
                users.add(user);
            }
        }

        return users;
    }
}