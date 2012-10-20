package game.run;

import game.model.Action;
import game.model.Player;

import java.io.IOException;


public abstract class RemotePlayerController extends PlayerController {

    //TODO: establish a network connection and create an action queue
    
    private final NetworkingController networkingController;
    
    public RemotePlayerController(Player player, NetworkingController networkingController){
        super(player);
        this.networkingController = networkingController;
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
