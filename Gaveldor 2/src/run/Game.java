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
        GameModel model;
        model = new GameModel(mapName);
        match = new GameMatch(model,
                new LocalPlayerController(model.getCurrentPlayer(), model, Constants.PLAYER_1_CONTROLS),
                new LocalPlayerController(model.getOtherPlayer(), model, Constants.PLAYER_2_CONTROLS));
    }

    public void startHostRemoteMatch(String mapName, Socket socket) throws GameException {
        GameModel model;
        model = new GameModel(mapName);
        match = new GameMatch(model,
                new LocalPlayerController(model.getCurrentPlayer(), model, Constants.PLAYER_1_CONTROLS),
                new RemotePlayerController(model.getOtherPlayer(), model, socket));
    }

    public void startClientRemoteMatch(String mapName, Socket socket) throws GameException {
        GameModel model;
        model = new GameModel(mapName);
        match = new GameMatch(model, new RemotePlayerController(model.getCurrentPlayer(), model, socket),
                new LocalPlayerController(model.getOtherPlayer(), model, Constants.PLAYER_2_CONTROLS));

    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new MainMenuState());
        addState(new HostGameState());
        addState(new PlayGameState());
        addState(new JoinGameState());
        addState(new CreditsState());
        addState(new InstructionState());
        addState(new HostMatchMakingState());
        addState(new JoinMatchMakingState());
    }

    public static void main(String[] args) throws SlickException, IOException, URISyntaxException {
        Resources.setupLWJGLNatives("/lwjgl_natives");

        Game start = new Game();
        AppGameContainer app = new AppGameContainer(start);
        app.setVerbose(false);
        app.setDisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, false);
        app.setVSync(true);
        app.setShowFPS(false);
        app.start();
    }

}
