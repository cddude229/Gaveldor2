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
            Point p = GameUI.getTileCoords(ui.getInput().getMouseX() + displayX, ui.getInput().getMouseY() + displayY);
            if (model.isValidCoord(p)){
                System.out.println(p);
            }
            if (selectedPiece == null){
                //TODO: set selectedPiece to piece at coordinates
            } else{
                //TODO: move selectedPiece to coordinates if valid
            }
        }
        //TODO: this is where to add actions to the queue
    }

    @Override
    public Action retrieveAction() {
        if (lastUpdateCount < ui.getUpdateCount()){
            if (lastUpdateCount < ui.getUpdateCount() - 1){
                throw new RuntimeException("Local controller missed an update");
            } else{
                update();
                lastUpdateCount++;
            }
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
