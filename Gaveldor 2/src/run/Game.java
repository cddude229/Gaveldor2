package run;


import game.model.GameModel;
import game.run.GameException;
import game.run.GameMatch;
import game.run.GameUI;
import game.run.LocalPlayerController;
import game.run.RemotePlayerController.ClientRemotePlayerController;
import game.run.RemotePlayerController.HostRemotePlayerController;

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
	
	public void startLocalMatch(String mapName) throws GameException{
        GameUI ui = new GameUI();
        GameModel model;
        model = new GameModel(mapName);
        match = new GameMatch(ui, model,
                new LocalPlayerController(model.getCurrentPlayer(), model, ui),
                new LocalPlayerController(model.getOtherPlayer(), model, ui));
	}
	
	public void startHostRemoteMatch(String mapName) throws GameException{
        GameUI ui = new GameUI();
        GameModel model;
        model = new GameModel(mapName);
        match = new GameMatch(ui, model,
                new LocalPlayerController(model.getCurrentPlayer(), model, ui),
                new HostRemotePlayerController(model.getOtherPlayer(), model, Constants.REMOTE_CONNECTION_PORT));
	}
    
    public void startClientRemoteMatch(String mapName, String address) throws GameException{
        GameUI ui = new GameUI();
        GameModel model;
        model = new GameModel(mapName);
        try{
            match = new GameMatch(ui, model,
                    new ClientRemotePlayerController(model.getOtherPlayer(), model, address, Constants.REMOTE_CONNECTION_PORT),
                    new LocalPlayerController(model.getCurrentPlayer(), model, ui));
        } catch (IOException e){
            throw new GameException("A connection could not be established to that host", e);
        }
    }

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
		addState(new HostGameState());
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
