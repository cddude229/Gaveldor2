package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.Constants;

public class LocalPlayerController extends PlayerController {

    public final Queue<Action> actionQueue = new LinkedList<Action>();

    public Piece selectedPiece = null;
    public Point selectedPieceMove = null;
    public long sinceSelectedPieceMove = 0L;
    public int selectedPieceFace = -1;

    public LocalPlayerController(Player player, GameModel model) {
        super(player, model);
    }

    @Override
    public boolean isAnimatingMove(){
        if (selectedPieceMove == null){
            return false;
        }
        List<Point> path = model.findValidMoves(selectedPiece, selectedPiece.getPosition(), true).get(selectedPieceMove);
        return sinceSelectedPieceMove < Constants.BOARD_MOVE_ANIMATE_TIME * (path.size() - 1);
    }
    
    @Override
    public void renderPiece(GameContainer container, Graphics g, Piece p) {
        if (p.equals(selectedPiece)){
            if (isAnimatingMove()){
                renderPieceMoving(container, g, p, p.getPosition(), selectedPieceMove, sinceSelectedPieceMove);
            } else{
                Point pos = p.getPosition();
                int dir = p.getDirection();
                if (p.equals(selectedPiece)){
                    if (selectedPieceMove != null){
                        pos = selectedPieceMove;
                        if (selectedPieceFace == -1){
                            List<Point> path = model.findValidMoves(selectedPiece, selectedPiece.getPosition(), true).get(selectedPieceMove);
                            if (path.size() > 1){
                                dir = Piece.pointsToDirection(path.get(path.size() - 1), path.get(path.size() - 2));
                            }
                        }
                    }
                    if (selectedPieceFace != -1) {
                        dir = selectedPieceFace;
                    }
                }
                Image sprite = p.getSprite(dir, p.owner.equals(player) ? 2 : 0);
                renderAtPosition(sprite, g, pos.x, pos.y, .5f, 1f);
            }
        } else{
            super.renderPiece(container, g, p);
        }
    }
    
    private int deltaMousePanAxis(GameContainer container, int size, int mousePos, int lowKey, int highKey, int delta){
        float frac;
        if (container.getInput().isKeyDown(lowKey)){
            frac = 0;
        } else if (container.getInput().isKeyDown(highKey)){
            frac = 1;
        } else{
            frac = 1f * mousePos / size;
            frac = Math.max(frac, 0);
            frac = Math.min(frac,  1);
        }
        float threshold = .05f;
        float scale = .02f;
        if (frac < threshold) {
            return Math.round(-(threshold - frac) * size * scale * delta);
        } else if (frac >= 1f - threshold) {
            return Math.round((frac - (1f - threshold)) * size * scale * delta);
        } else{
            return 0;
        }
    }

    public void updateMousePan(GameContainer container, LocalPlayerController pc, int delta) {
        pc.setDisplayPoint(container,
                pc.displayX + deltaMousePanAxis(container,
                        container.getWidth(), container.getInput().getMouseX(), Input.KEY_LEFT, Input.KEY_RIGHT,
                delta),
                pc.displayY + deltaMousePanAxis(container,
                        container.getHeight(), container.getInput().getMouseY(), Input.KEY_UP, Input.KEY_DOWN,
                delta)
        );
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
        addState(new DisconnectedState(true));
        addState(new WonState(true));
    }

}
