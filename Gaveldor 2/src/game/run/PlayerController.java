package game.run;

import game.model.Action;
import game.model.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public abstract class PlayerController {
    
    public final Player player;
    
    public PlayerController(Player player){
        this.player = player;
    }
    
    public abstract Action retrieveAction();
    
    public abstract void propagateAction(Action action);
    
    public abstract void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;
}
