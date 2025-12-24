import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserJsonWriter {

    public static void writeUsers(Path path, List<User> users)
            throws IOException {

        JSONArray usersArray = new JSONArray();

        for (User user : users) {
            JSONObject obj = new JSONObject();

            obj.put("email", user.getEmail());
            obj.put("password", user.getPassword());
            obj.put("points", user.getPoints());

            JSONArray gamesArray = new JSONArray();
            if(user.getActiveGames() != null){
                for (Game game : user.getActiveGames()) {
                    gamesArray.add(game.getId());
                }
            }
            obj.put("games", gamesArray);
            usersArray.add(obj);
        }

        Files.write(
                path,
                usersArray.toJSONString().getBytes(StandardCharsets.UTF_8)
        );
    }
}