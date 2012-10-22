package run;

import game.model.GameModel;
import game.run.GameMatch;
import game.run.GameUI;
import game.run.LocalPlayerController;

import java.io.IOException;

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
        //TODO
        GameUI ui = new GameUI();
        GameModel model;
        try {
            model = new GameModel("/assets/maps/basic");
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
        ((Game)game).match = new GameMatch(ui, model,
                new LocalPlayerController(model.getCurrentPlayer(), model, ui),
                new LocalPlayerController(model.getOtherPlayer(), model, ui));
        
	    game.enterState(PlayGameState.STATE_ID);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		return STATE_ID;
	}

}
