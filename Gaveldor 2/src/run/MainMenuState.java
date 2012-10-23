package run;

import game.run.GameException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState {
	
	public static final int STATE_ID = 0;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
        // TODO Auto-generated method stub
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game){
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO
        try {
            ((Game)game).startLocalMatch("/assets/maps/basic");
        } catch (GameException e) {
            // TODO: display in window
            e.printStackTrace();
        }
	    game.enterState(PlayGameState.STATE_ID);
	}

	@Override
	public int getID() {
		return STATE_ID;
	}

}
