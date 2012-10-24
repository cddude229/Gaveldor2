package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public abstract class PlayerController {
    
    public final Player player;
    
    public final GameModel model; // for getting game state info only (no updating)
    
    public PlayerController(Player player, GameModel model){
        this.player = player;
        this.model = model;
    }
    
    public abstract Action retrieveAction();
    
    public abstract void propagateAction(Action action);
    
    public abstract void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;
    
}
