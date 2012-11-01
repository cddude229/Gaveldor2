package game.run;

import game.model.GameModel;
import game.model.Player;

import java.util.HashMap;
import java.util.Map;

public class GameMatch {
    
//    public final GameUI ui;
    public final GameModel model;
    
    private final Map<Player, PlayerController> playerControllers = new HashMap<Player, PlayerController>();


    
    public GameMatch(GameModel model, PlayerController pc1, PlayerController pc2){
//        this.ui = ui;
        this.model = model;
        playerControllers.put(pc1.player, pc1);
        playerControllers.put(pc2.player, pc2);
    }
    
    public PlayerController getCurrentPC(){
        return playerControllers.get(model.getCurrentPlayer());
    }
    
    public PlayerController getOtherPC(){
        return playerControllers.get(model.getOtherPlayer());
    }
}
