package run;

import game.run.GameException;
import game.run.GameMatch;

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
	    try {
            ((Game)game).startLocalMatch("/assets/maps/basic");
        } catch (GameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
	    GameMatch match = ((Game)game).match;
	    if (match.getCurrentPC().isReady() && match.getOtherPC().isReady()){
	        game.enterState(PlayGameState.STATE_ID);
	    }

	}

	@Override
	public int getID() {
		return STATE_ID;
	}

}
