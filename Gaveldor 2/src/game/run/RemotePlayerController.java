package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Player;

import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public abstract class RemotePlayerController extends PlayerController {
    
    private final NetworkingController networkingController;
    
    public RemotePlayerController(Player player, GameModel model, NetworkingController networkingController){
        super(player, model);
        this.networkingController = networkingController;
        this.networkingController.start();
    }

    @Override
    public Action retrieveAction(){
        return networkingController.getAction();
    }

    @Override
    public void propagateAction(Action action) {
        networkingController.sendAction(action);
    }
    

    public void renderControllerPlaying(Graphics g) throws SlickException{
        //TODO: render the remote player's actions
    }
    
    public static class HostRemotePlayerController extends RemotePlayerController{

        public HostRemotePlayerController(Player player, GameModel model, int port) {
            super(player, model, new NetworkingController(port)); 
        }
        
    }
    
    public static class ClientRemotePlayerController extends RemotePlayerController{

        public ClientRemotePlayerController(Player player, GameModel model, String host, int port) throws IOException{
            super(player, model, new NetworkingController(host, port)); 
        }
        
    }
}
