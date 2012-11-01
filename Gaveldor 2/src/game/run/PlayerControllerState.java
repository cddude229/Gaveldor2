package game.run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.aem.sticky.StickyListener;

public abstract class PlayerControllerState extends BasicGameState {
    
    public final GameState gameState;
    
    public final boolean isLocal;
    
    public final StickyListener stickyListener = new StickyListener();
    
    public PlayerControllerState(GameState gameState, boolean isLocal){
        this.gameState = gameState;
        this.isLocal = isLocal;
    }

    @Override
    public int getID() {
        return gameState.ordinal();
    }
    
    @Override
    public final void init(GameContainer container, StateBasedGame game) throws SlickException{
        init(container, (PlayerController)game);
    }
    
    public abstract void init(GameContainer container, PlayerController pc) throws SlickException;
    
    @Override
    public final void enter(GameContainer container, StateBasedGame game) throws SlickException{
        container.getInput().addListener(stickyListener);
        enter(container, (PlayerController)game);
        
    }

    public void enter(GameContainer container, PlayerController pc) throws SlickException{};

    @Override
    public final void leave(GameContainer container, StateBasedGame game) throws SlickException{
        container.getInput().removeListener(stickyListener);
        leave(container, (PlayerController)game);
        
    }

    public void leave(GameContainer container, PlayerController pc) throws SlickException{};
    
    @Override
    public final void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
        PlayerController pc = (PlayerController)game;
        if (gameState == pc.model.gameState){
            render(container, pc, g);
        }
    }
    
    public abstract void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException;
    
    @Override
    public final void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
        update(container, (PlayerController)game, delta);
    }

    public final void update(GameContainer container, PlayerController pc, int delta) throws SlickException{
        if (gameState != pc.model.gameState){
            pc.enterState(pc.model.gameState.getPCStateID());
            return; //do we want?
        }
        if (isLocal){
            updateLocal(container, (LocalPlayerController)pc, delta);
        } else{
            //do nothing
        }
    }
    public abstract void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException;
}
