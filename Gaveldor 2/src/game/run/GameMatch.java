package game.run;

import game.model.GameModel;
import game.model.Player;

public class GameMatch {
    
//    public final GameUI ui;
    public final GameModel model;
    
    public final PlayerController pc1, pc2;


    
    public GameMatch(GameModel model, PlayerController pc1, PlayerController pc2){
//        this.ui = ui;
        this.model = model;
        this.pc1 = pc1;
        this.pc2 = pc2;
    }
    
    public PlayerController getPCByPlayer(Player player){
        if (pc1.player.equals(player)){
            return pc1;
        } else if (pc2.player.equals(player)){
            return pc2;
        } else{
            throw new IllegalArgumentException();
        }
    }
    
    public PlayerController getCurrentPC(){
        return getPCByPlayer(model.getCurrentPlayer());
    }
    
    public PlayerController getOtherPC(){
        return getPCByPlayer(model.getOtherPlayer());
    }
}
