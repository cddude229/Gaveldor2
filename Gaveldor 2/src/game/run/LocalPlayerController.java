package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.Point;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;


public class LocalPlayerController extends PlayerController {

    private final GameUI ui; // for checking user input only (no rendering)
    private int lastUpdateCount;
    
    private final Queue<Action> actionQueue = new LinkedList<Action>();
    
    private int displayX = 0, displayY = 0;
    
    private Piece selectedPiece = null;

    public LocalPlayerController(Player player, GameModel model, GameUI ui) {
        super(player, model);
        this.ui = ui;
        lastUpdateCount = this.ui.getUpdateCount();
    }
    
    public boolean isReady(){
        return true;
    }
    
    private void updatePan(){
        double placementX = (double)ui.getInput().getMouseX() / Constants.WINDOW_WIDTH,
                placementY = (double)ui.getInput().getMouseY() / Constants.WINDOW_HEIGHT;
        //TODO: clean up ALL these constants
        placementX = Math.max(placementX, 0);
        placementX = Math.min(placementX, 1);
        if (placementX < .1){
            displayX -= (.1 - placementX) * .25 * Constants.WINDOW_WIDTH;
        } else if (placementX >= .9){
            displayX += (placementX - .9) * .25 * Constants.WINDOW_WIDTH;
        }
        displayX = Math.max(displayX, 0);
        displayX = Math.min(displayX, model.map.getPixelWidth() - Constants.WINDOW_WIDTH);

        placementY = Math.max(placementY, 0);
        placementY = Math.min(placementY, 1);
        if (placementY < .1){
            displayY -= (.1 - placementY) * .25 * Constants.WINDOW_HEIGHT;
        } else if (placementY >= .9){
            displayY += (placementY - .9) * .25 * Constants.WINDOW_HEIGHT;
        }
        displayY = Math.max(displayY, 0);
        displayY = Math.min(displayY, model.map.getPixelHeight() - Constants.WINDOW_HEIGHT);
    }

    private void update(){
        
        updatePan();
        
        if (ui.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            Point position = GameUI.getTileCoords(ui.getInput().getMouseX() + displayX, ui.getInput().getMouseY() + displayY);
            Piece piece = model.getPieceByPosition(position);
            
            if (piece != null && !piece.equals(selectedPiece)){
                if (piece.owner.equals(player)){
                    selectedPiece = piece;
                }
            } else if (selectedPiece != null){
                switch(selectedPiece.turnState){
                case MOVING:
                    if (model.isValidPosition(position) && Arrays.asList(selectedPiece.getValidMoves()).contains(position)
                            && (piece == null || piece == selectedPiece)){
                        actionQueue.add(new Action.MoveAction(selectedPiece, position));
                    } else{
                        //TODO: do nothing?
                    }
                    break;
                case FACING:
                    int direction = Piece.PointsToDirection(position, selectedPiece.getPosition());
                    if (direction != -1){
                        actionQueue.add(new Action.FaceAction(selectedPiece, direction));
                    } else{
                        //TODO: do nothing?
                    }
                case ATTACKING:
                    if (model.isValidPosition(position) && Arrays.asList(selectedPiece.getValidMoves()).contains(position)
                            && piece != null && !piece.owner.equals(selectedPiece.owner)){
                        actionQueue.add(new Action.AttackAction(selectedPiece.getPosition(), piece.getPosition(), selectedPiece.owner.id));
                    } else{
                        //TODO: do nothing?
                    }
                    break;
                case DONE:
                    //do nothing
                    break;
                default:
                    throw new RuntimeException();
                }
            }
        }
        
        if (ui.getInput().isKeyPressed(Input.KEY_E)){
            actionQueue.add(new Action.TurnEndAction(player));
        }
    }

    @Override
    public Action retrieveAction() {
        if (lastUpdateCount < ui.getUpdateCount()){
            update();
            lastUpdateCount = ui.getUpdateCount();
        }
        return actionQueue.poll();
    }

    @Override
    public void propagateAction(Action action) {
        //do nothing
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        model.renderBoard(g, -displayX, -displayY);
    }

}
