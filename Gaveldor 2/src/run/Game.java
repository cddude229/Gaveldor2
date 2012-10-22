package run;


import game.model.GameModel;
import game.run.GameMatch;
import game.run.GameUI;
import game.run.LocalPlayerController;

import java.io.IOException;
import java.net.URISyntaxException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;
import util.Resources;

public class Game extends StateBasedGame {
    
    public GameMatch match = null;
    
	public Game() {
		super("Gaveldor 2");
	}
	
	public boolean startLocalMatch(String mapName){
        GameUI ui = new GameUI();
        GameModel model;
        try {
            model = new GameModel(mapName);
        } catch (IOException e) {
            return false;
        }
        match = new GameMatch(ui, model,
                new LocalPlayerController(model.getCurrentPlayer(), model, ui),
                new LocalPlayerController(model.getOtherPlayer(), model, ui));
        return true;
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
		addState(new PlayGameState());
	}
	
	public static void main(String[] args) throws SlickException, IOException, URISyntaxException{
	    Resources.setupLWJGLNatives("/lwjgl_natives");
	    
        AppGameContainer app = new AppGameContainer(new Game());
        app.setVerbose(false);
        app.setDisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.WINDOW_FULLSCREEN);
        app.setVSync(true);
        app.setShowFPS(false);
        app.start();
	}

}
