package game.run;

import game.model.Action;
import game.model.Player;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public abstract class RemotePlayerController extends PlayerController {

    //TODO: establish a network connection and create an action queue
    
    private final NetworkingController networkingController;
    
    public RemotePlayerController(Player player, NetworkingController networkingController){
        super(player);
        this.networkingController = networkingController;
    }


    @Override
    public Action retrieveAction(){
        return networkingController.getAction();
    }

    @Override
    public void propagateAction(Action action) {
        networkingController.sendAction(action);
    }
    

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
        //TODO
    }
    
    public static class HostRemotePlayerController extends RemotePlayerController{

        public HostRemotePlayerController(Player player, int port) {
            super(player, new NetworkingController(port)); 
        }
        
    }
    
    public static class ClientRemotePlayerController extends RemotePlayerController{

        public ClientRemotePlayerController(Player player, String host, int port) throws IOException{
            super(player, new NetworkingController(host, port)); 
        }
        
    }
}
