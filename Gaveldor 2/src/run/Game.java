package run;

import java.io.IOException;
import java.net.URISyntaxException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Resources;

public class Game extends StateBasedGame {

	public Game() {
		super("Gaveldor 2");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
	}
	
	public static void main(String[] args) throws SlickException, IOException, URISyntaxException{
	    Resources.setupLWJGLNatives("/lwjgl_natives");
	    
        AppGameContainer app = new AppGameContainer(new Game());
        app.setVerbose(false);
        app.setDisplayMode(800, 600, false); //TODO: arbitrary width and height
        app.setVSync(true);
        app.setShowFPS(false);
        app.start();
	}

}