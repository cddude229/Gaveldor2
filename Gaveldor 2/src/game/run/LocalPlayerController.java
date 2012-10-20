package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Player;

import java.util.LinkedList;
import java.util.Queue;


public class LocalPlayerController extends PlayerController {

    private final GameUI ui; // for checking user input only (no rendering)
    private int lastUpdateCount;
    
    private final GameModel model; // for getting game state info only (no updating)
    
    private final Queue<Action> actionQueue = new LinkedList<Action>();

    public LocalPlayerController(Player player, GameUI ui, GameModel model) {
        super(player);
        this.ui = ui;
        lastUpdateCount = this.ui.getUpdateCount();
        this.model = model;
        
        
    }

    private void updateActions(){
        //TODO: this is where the game logic goes - add actions to queue
    }

    @Override
    public Action retrieveAction() {
        if (lastUpdateCount < ui.getUpdateCount()){
            if (lastUpdateCount < ui.getUpdateCount() - 1){
                throw new RuntimeException("Local controller missed an update");
            } else{
                updateActions();
                lastUpdateCount++;
            }
        }
        return actionQueue.poll();
    }

    @Override
    public void propagateAction(Action action) {
        //do nothing
    }

}
