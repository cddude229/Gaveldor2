package run;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class HostGameState extends BasicGameState {
    
    public static final int STATE_ID = 2;

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        if (((Game)game).match.getOtherPC().isReady()){
            game.enterState(PlayGameState.STATE_ID);
        }
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

}
