package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Player;

import java.net.Socket;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;


public class RemotePlayerController extends PlayerController {
    
    private final NetworkingController networkingController;
    
    private RemotePlayerController(Player player, GameModel model, NetworkingController networkingController){
        super(player, model);
        this.networkingController = networkingController;
        this.networkingController.start();
    }

    public RemotePlayerController(Player player, GameModel model, Socket socket) {
        this(player, model, new NetworkingController(socket)); 
    }

    @Override
    public Action retrieveAction(){
        return networkingController.getAction();
    }

    @Override
    public void propagateAction(Action action) {
        networkingController.sendAction(action);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new SetupState(false));
        addState(new PlayBoardState(false));
        addState(new PlayMinigameState(false));
        addState(new DisconnectedState(false));
        addState(new WonState(false));
    }
}
