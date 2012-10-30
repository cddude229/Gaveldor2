package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Player;

import java.net.Socket;

import org.newdawn.slick.Graphics;
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
    

    public void renderControllerPlayingBoard(Graphics g) throws SlickException{
        //TODO: render the remote player's actions
    }
}
