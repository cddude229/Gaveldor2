package game.run;

import game.model.Action;
import game.model.Action.MakeMinigameMoveAction;
import game.model.GameModel;
import game.model.MinigameModel.Move;
import game.model.Piece;
import game.model.Player;
import game.model.Point;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.Constants;
import util.Resources;

public class LocalPlayerController extends PlayerController {

    private final GameUI ui; // for checking user input only (no rendering)
    private int lastUpdateCount;

    private final Queue<Action> actionQueue = new LinkedList<Action>();

    private Piece selectedPiece = null;
    
    private static Image hoverOverlay, movableOverlay, faceableArrows, attackableOverlay;

    public LocalPlayerController(Player player, GameModel model, GameUI ui) {
        super(player, model);
        this.ui = ui;
        lastUpdateCount = this.ui.getUpdateCount();
        if (player.equals(model.getCurrentPlayer())) {
            actionQueue.add(new Action.GameStartAction());
        }
    }
    
    @Override
    public void setup(){
        selectedPiece = null;
    }
    
    public static void initImages(){
        hoverOverlay = Resources.getImage("/assets/graphics/ui/hover.png");
        movableOverlay = Resources.getImage("/assets/graphics/ui/movable.png");
        faceableArrows = Resources.getImage("/assets/graphics/ui/arrows.png");
        attackableOverlay = Resources.getImage("/assets/graphics/ui/attackable.png");
    }

    private void updateMousePan() {
        double placementX = (double) ui.getInput().getMouseX() / Constants.WINDOW_WIDTH, placementY = (double) ui
                .getInput().getMouseY() / Constants.WINDOW_HEIGHT;
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

    private void update() {
        switch (model.gameState) {
        case SETTING_UP:
            // do nothing?
            break;
        case PLAYING_BOARD:
            if (model.getCurrentPlayer().equals(player)){
                if (ui.getInput().isKeyDown(Input.KEY_ESCAPE)){
                    actionQueue.add(new Action.ForfeitAction(player));
                } else{
                    updatePlayingBoardCurrent();
                }
            }
            break;
        case PLAYING_MINIGAME:
            if (model.getCurrentPlayer().equals(player)){
                updatePlayingMinigameAttack();
            } else{
                updatePlayingMinigameDefend();
            }
            //TODO
            break;
        case DISCONNECTED:
            // TODO
            break;
        case WON:
            // TODO
            break;
        default:
            throw new RuntimeException();
        }
    }

    private void updatePlayingBoardCurrent() {
        updateMousePan();
        if (ui.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            Point position = GameUI.getTileCoords(ui.getInput().getMouseX() + displayX, ui.getInput().getMouseY()
                    + displayY);
            System.out.println(position);
            Piece piece = model.getPieceByPosition(position);

            if (ui.getInput().isKeyDown(Input.KEY_LSHIFT)) {
                if (piece != null && piece.owner.equals(player) && !piece.equals(selectedPiece)) {
                    selectedPiece = piece;
                }
            } else if (selectedPiece != null) {
                switch (selectedPiece.turnState) {
                case MOVING:
                    if (model.isValidPosition(position)
                            && Arrays.asList(selectedPiece.getValidMoves()).contains(position)
                            && (piece == null || piece == selectedPiece)) {
                        actionQueue.add(new Action.MoveAction(selectedPiece, position));
                    } else {
                        // TODO: do nothing?
                    }
                    break;
                case TURNING:
                    int direction = Piece.pointsToDirection(position, selectedPiece.getPosition());
                    if (direction != -1) {
                        actionQueue.add(new Action.FaceAction(selectedPiece, direction));
                    } else {
                        // TODO: do nothing?
                    }
                    break;
                case ATTACKING:
                    if (model.isValidPosition(position)
                            && Arrays.asList(selectedPiece.getValidAttacks()).contains(position) && piece != null
                            && !piece.owner.equals(selectedPiece.owner)) {
                        actionQueue.add(new Action.AttackAction(selectedPiece, piece));
                    } else {
                        // TODO: do nothing?
                    }
                    break;
                case DONE:
                    // do nothing
                    break;
                default:
                    throw new RuntimeException();
                }
            }
        }

        if (ui.getInput().isKeyPressed(Input.KEY_E)) {
            selectedPiece = null;
            actionQueue.add(new Action.TurnEndAction(player));
        }
    }
    
    public void updatePlayingMinigameAttack(){
        if (model.getMinigame().attackingMove == null){
            if (model.getMinigame().moveTime >= Constants.MINIGAME_MOVE_TIME){
                actionQueue.add(new MakeMinigameMoveAction(Move.NONE, player));
            } else if (ui.getInput().isKeyDown(Input.KEY_A)){
                actionQueue.add(new MakeMinigameMoveAction(Move.HIGH, player));
            } else if (ui.getInput().isKeyDown(Input.KEY_S)){
                actionQueue.add(new MakeMinigameMoveAction(Move.MID, player));
            } else if (ui.getInput().isKeyDown(Input.KEY_D)){
                actionQueue.add(new MakeMinigameMoveAction(Move.LOW, player));
            }
        }
    }
    
    public void updatePlayingMinigameDefend(){
        if (model.getMinigame().defendingMove == null){
            if (model.getMinigame().moveTime >= Constants.MINIGAME_MOVE_TIME){
                actionQueue.add(new MakeMinigameMoveAction(Move.NONE, player));
            } else if (ui.getInput().isKeyDown(Input.KEY_J)){
                actionQueue.add(new MakeMinigameMoveAction(Move.HIGH, player));
            } else if (ui.getInput().isKeyDown(Input.KEY_K)){
                actionQueue.add(new MakeMinigameMoveAction(Move.MID, player));
            } else if (ui.getInput().isKeyDown(Input.KEY_L)){
                actionQueue.add(new MakeMinigameMoveAction(Move.LOW, player));
            }
        }
    }

    @Override
    public Action retrieveAction() {
        if (lastUpdateCount < ui.getUpdateCount()) {
            update();
            lastUpdateCount = ui.getUpdateCount();
        }
        return actionQueue.poll();
    }

    @Override
    public void propagateAction(Action action) {
        // do nothing
    }

    @Override
    public void renderControllerPlayingBoard(Graphics g) throws SlickException {
        Point position = GameUI.getTileCoords(ui.getInput().getMouseX() + displayX, ui.getInput().getMouseY()
                + displayY);
        if (model.isValidPosition(position)) {
            renderAtPosition(hoverOverlay, g, position.x, position.y, 0f, 0f);
        }

        if (selectedPiece != null) {
            switch (selectedPiece.turnState) {
            case MOVING:
                for (Point p : selectedPiece.getValidMoves()) {
                    if (model.isValidPosition(p)) {
                        renderAtPosition(movableOverlay, g, p.x, p.y, 0f, 0f);
                    }
                }
                break;
            case TURNING:
                renderAtPosition(faceableArrows, g, selectedPiece.getPosition().x, selectedPiece.getPosition().y, 0.5f, 0.5f);
                // TODO
                break;
            case ATTACKING:
                for (Point p : selectedPiece.getValidAttacks()) {
                    if (model.isValidPosition(p)) {
                        renderAtPosition(attackableOverlay, g, p.x, p.y, 0f, 0f);
                    }
                }
                break;
            case DONE:
                // do nothing
                break;
            default:
                throw new RuntimeException();
            }
        }
    }

}
