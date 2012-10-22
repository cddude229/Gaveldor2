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


public class LocalPlayerController extends PlayerController {

    private final GameUI ui; // for checking user input only (no rendering)
    private int lastUpdateCount;
    
    private final Queue<Action> actionQueue = new LinkedList<Action>();
    
    private Piece selectedPiece = null;

    public LocalPlayerController(Player player, GameModel model, GameUI ui) {
        super(player, model);
        this.ui = ui;
        lastUpdateCount = this.ui.getUpdateCount();
        
        
    }

    private void updateActions(){
        if (ui.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            Point p = ui.getTileCoords(ui.getInput().getMouseX(), ui.getInput().getMouseY());
            if (model.isValidCoord(p)){
                System.out.println(p);
            }
            if (selectedPiece == null){
                //TODO: set selectedPiece to piece at coordinates
            } else{
                //TODO: move selectedPiece to coordinates if valid
            }
        }
        //TODO: this is where the game logic goes - add actions to queue
    }

    @Override
    public Action retrieveAction() {
        if (lastUpdateCount < ui.getUpdateCount()){
            if (lastUpdateCount < ui.getUpdateCount() - 1){
                throw new RuntimeException("Local controller missed an update");
            } else{
                updateActions();
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
        model.renderMap(g);
    }

}
