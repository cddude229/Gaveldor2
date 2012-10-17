package game.run;

import game.model.Action;
import game.model.Player;


public class RemotePlayerController extends PlayerController {

    //TODO: establish a network connection and creat an action queue

    public RemotePlayerController(Player player) {
        super(player);
    }

    @Override
    public Action retrieveAction(){
        //TODO: get action from the the network
        return null;
    }

    @Override
    public void propagateAction(Action action) {
        //TODO: send action along the network
    }

}
