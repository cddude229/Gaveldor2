package game.run;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameUI {
    
    private GameContainer container;
    private StateBasedGame game;
    private int delta;
    
    private int updateCount = 0;
    
    public GameContainer getContainer(){
        return container;
    }
    
    public StateBasedGame getGame(){
        return game;
    }
    
    public int getDelta(){
        return delta;
    }
    
    public int getUpdateCount(){
        return updateCount;
    }
    
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        this.container = container;
        this.game = game;
        this.delta = delta;
        updateCount++;
    }
}
