package run;

import game.run.GameException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;

public class MainMenuState extends BasicGameState {
	
	public static final int STATE_ID = Game.allocateStateID();
	private int buttonCount = 0;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	    Rectangle rect = new Rectangle(0, 0, 0, 0);
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
            if (container.getInput().isKeyPressed(Input.KEY_L)){
                ((Game)game).startLocalMatch("/assets/maps/basic");
                game.enterState(PlayGameState.STATE_ID);
            } else if (container.getInput().isKeyPressed(Input.KEY_H)){
                ((Game)game).startHostRemoteMatch("/assets/maps/basic");
                game.enterState(HostGameState.STATE_ID);
            } else if (container.getInput().isKeyPressed(Input.KEY_C)){
                ((Game)game).startClientRemoteMatch("/assets/maps/basic", "localhost");
                game.enterState(JoinGameState.STATE_ID);
            }
        } catch (GameException e) {
            // TODO: display in window
            e.printStackTrace();
        }
	}

	@Override
	public int getID() {
		return STATE_ID;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @return an int[] containing the screen location of the buttons
	 */
	public int[] generateLocation(int width, int height) {
	    int scnWidth = Constants.WINDOW_WIDTH;
	    int scnHeight = Constants.WINDOW_HEIGHT;
	    int locWidth = scnWidth/2-width/2;
	    int locHeight = scnHeight/2-(height/2 + 20 * this.buttonCount);
	    this.buttonCount += 1;
	    return new int[] {locWidth,locHeight};
	}

}
