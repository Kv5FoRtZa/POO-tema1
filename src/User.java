import java.util.ArrayList;
import java.util.List;

public class User {
    //user, are o lista de jocuri in principiu si alte date basic
    // folosit in main pt load la istoric si logare
    private String email,password;
    private List<Game> jocuri;
    private int elo;
    public User(String email, String parola){
        this.email =  email;
        this.password = parola;
        this.elo = 0;
        this.jocuri = new ArrayList<>();
    }

    public User() {

    }

    public void addGame(Game game){
        jocuri.add(game);
    }

    public void removeGame(Game game){
        jocuri.remove(game);
    }
    public List<Game> getActiveGames(){
        return jocuri;
    }
    public int getPoints(){
        return this.elo;
    }
    public void setPoints(int points){
        this.elo = points;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public void setEmail(String em){
        this.email = em;
    }
    public void setPassword(String pss){
        this.password = pss;
    }

    public void setGames(List<Game> jocuri) {
        this.jocuri = jocuri;
    }
}
