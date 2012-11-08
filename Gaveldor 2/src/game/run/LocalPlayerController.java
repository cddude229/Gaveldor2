package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.Point;

import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class LocalPlayerController extends PlayerController {

    public final Queue<Action> actionQueue = new LinkedList<Action>();

    public Piece selectedPiece = null;
    public Point selectedPieceMove = null;
    public int selectedPieceFace = -1;

    public LocalPlayerController(Player player, GameModel model) {
        super(player, model);
        if (player.equals(model.getCurrentPlayer())) {
            actionQueue.add(new Action.GameStartAction());
        }
    }

    @Override
    public void renderPiece(GameContainer container, Graphics g, Piece p) {
        if (p.equals(selectedPiece)){
            Point pos = p.getPosition();
            int dir = p.getDirection();
            if (p.equals(selectedPiece)){
                if (selectedPieceMove != null){
                    pos = selectedPieceMove;
                }
                if (selectedPieceFace != -1){
                    dir = selectedPieceFace;
                }
            }
            renderAtPosition(p.getSprite(dir), g, pos.x, pos.y, .5f, 1f);
        } else{
            super.renderPiece(container, g, p);
        }
    }

    public void updateMousePan(GameContainer container, LocalPlayerController pc, int delta) {
        double placementX = ((double)container.getInput().getMouseX()) / container.getWidth();
        double placementY = ((double)container.getInput().getMouseY()) / container.getHeight();
        placementX = Math.max(placementX, 0);
        placementX = Math.min(placementX, 1);
        int x = displayX, y = displayY;
        if (placementX < .1) {
            x -= (.1 - placementX) * .25 * container.getWidth();
        } else if (placementX >= .9) {
            x += (placementX - .9) * .25 * container.getHeight();
        }

        placementY = Math.max(placementY, 0);
        placementY = Math.min(placementY, 1);
        if (placementY < .1) {
            y -= (.1 - placementY) * .25 * container.getWidth();
        } else if (placementY >= .9) {
            y += (placementY - .9) * .25 * container.getHeight();
        }
        
        pc.setDisplayPoint(container, x, y);
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
