import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadGames {
    //load game
    //la tema 1 era in main
    //se ocupa cu incarcarea in memorie a jocurilor vechi
    public User exec(User current_user,Map<Integer, Game> games){
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
        return current_user;
    }
}
