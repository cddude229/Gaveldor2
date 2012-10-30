package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;

import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import util.Constants;

public class LocalPlayerController extends PlayerController {

    public final Queue<Action> actionQueue = new LinkedList<Action>();

    public Piece selectedPiece = null;

    public LocalPlayerController(Player player, GameModel model) {
        super(player, model);
        if (player.equals(model.getCurrentPlayer())) {
            actionQueue.add(new Action.GameStartAction());
        }
    }

    public void updateMousePan(GameContainer container, LocalPlayerController pc, int delta) {
        double placementX = ((double)container.getInput().getMouseX()) / Constants.WINDOW_WIDTH;
        double placementY = ((double)container.getInput().getMouseY()) / Constants.WINDOW_HEIGHT;
        // TODO: clean up ALL these constants
        placementX = Math.max(placementX, 0);
        placementX = Math.min(placementX, 1);
        if (placementX < .1) {
            displayX -= (.1 - placementX) * .25 * Constants.WINDOW_WIDTH;
        } else if (placementX >= .9) {
            displayX += (placementX - .9) * .25 * Constants.WINDOW_WIDTH;
        }
        displayX = Math.max(displayX, 0);
        int maxX = model.map.getPixelWidth() - Constants.WINDOW_WIDTH;
        maxX = Math.max(maxX, maxX / 2);
        displayX = Math.min(displayX, maxX);

        placementY = Math.max(placementY, 0);
        placementY = Math.min(placementY, 1);
        if (placementY < .1) {
            displayY -= (.1 - placementY) * .25 * Constants.WINDOW_HEIGHT;
        } else if (placementY >= .9) {
            displayY += (placementY - .9) * .25 * Constants.WINDOW_HEIGHT;
        }
        displayY = Math.max(displayY, 0);
        int maxY = model.map.getPixelHeight() - Constants.WINDOW_HEIGHT;
        maxY = Math.max(maxY, maxY / 2);
        displayY = Math.min(displayY, maxY);
    }

    @Override
    public Action retrieveAction() {
        return actionQueue.poll();
    }

    @Override
    public void propagateAction(Action action) {
        // do nothing
    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new SetupState(true));
        addState(new PlayBoardState(true));
        addState(new PlayMinigameState(true));
        addState(new DisconnectedState(true));
        addState(new WonState(true));
    }

}
