package game.run;

import game.model.Action;
import game.model.Player;


public abstract class PlayerController {
    
    public final Player player;
    
    public PlayerController(Player player){
        this.player = player;
    }
    
    public abstract Action retrieveAction();
    
    public abstract void propagateAction(Action action);
}
