package run;

import game.model.GameModel;
import game.run.GameException;
import game.run.GameMatch;
import game.run.LocalPlayerController;
import game.run.RemotePlayerController;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;
import util.Resources;

public class Game extends StateBasedGame {

    public GameMatch match = null;

    public Game() {
        super("Gaveldor 2");
    }

    private static int nextStateID = 0;

    public static int allocateStateID() {
        return nextStateID++;
    }

    public void startLocalMatch(String mapName) throws GameException {
        GameModel model = new GameModel();
        match = new GameMatch(model,
                new LocalPlayerController(model.getCurrentPlayer(), model),
                new LocalPlayerController(model.getOtherPlayer(), model),
                mapName);
    }

    public void startHostRemoteMatch(String mapName, Socket socket) throws GameException {
        GameModel model = new GameModel();
        match = new GameMatch(model,
                new LocalPlayerController(model.getCurrentPlayer(), model),
                new RemotePlayerController(model.getOtherPlayer(), model, socket),
                mapName);
    }

    public void startClientRemoteMatch(String mapName, Socket socket) throws GameException {
        GameModel model = new GameModel();
        match = new GameMatch(model,
                new RemotePlayerController(model.getCurrentPlayer(), model, socket),
                new LocalPlayerController(model.getOtherPlayer(), model),
                mapName);

    }
    
    private void makeFullscreen(AppGameContainer container) throws SlickException{
        int width = container.getScreenWidth(), height = container.getScreenHeight();
        if (width >= Constants.WINDOW_WIDTH && height >= Constants.WINDOW_HEIGHT){
            container.setDisplayMode(width, height, true);
        }
    }
    
    private void makeWindowed(AppGameContainer container) throws SlickException{
        container.setDisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, false);
    }
    
    public void toggleFullscreenCheck(AppGameContainer container) throws SlickException{
        if (container.getInput().isKeyPressed(Input.KEY_F) && container.getInput().isKeyDown(Input.KEY_F)){
            if (container.isFullscreen()){
                makeWindowed(container);
            } else{
                makeFullscreen(container);
                int stateCount = this.getStateCount();
                for (int stateID = 1; stateID < stateCount; stateID = stateID + 1);
            }
        }
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new MainMenuState());
        addState(new HostGameState());
        addState(new PlayGameState());
        addState(new JoinGameState());
        addState(new CreditsState());
        addState(new InstructionState());
        addState(new MatchMakingState());
        addState(new MapSelectionState());
    }

    public static void main(String[] args) throws SlickException, IOException, URISyntaxException {
        Resources.setupLWJGLNatives("/lwjgl_natives");

        Game game = new Game();
        AppGameContainer container = new AppGameContainer(game);
        container.setVerbose(false);
        game.makeWindowed(container);
        container.setVSync(true);
        container.setShowFPS(false);
        container.start();
    }

}
