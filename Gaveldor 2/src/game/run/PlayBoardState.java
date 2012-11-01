package game.run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.model.Piece;
import game.model.Point;

import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.Constants;
import util.Helpful;
import util.Resources;

import com.aem.sticky.button.Button;
import com.aem.sticky.button.events.ClickListener;

public class PlayBoardState extends PlayerControllerState {

    public PlayBoardState(boolean isLocal) {
        super(GameState.PLAYING_BOARD, isLocal);
    }
    
    private Image hoverOverlay, movableOverlay, faceableArrows, attackableOverlay;
    
    private Button[] sidebarButtons;

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        if (isLocal){
            initLocal(container, (LocalPlayerController)pc);
        }
    }
    
    public void initLocal(GameContainer container, final LocalPlayerController pc) throws SlickException{
        hoverOverlay = Resources.getImage("/assets/graphics/ui/hover.png");
        movableOverlay = Resources.getImage("/assets/graphics/ui/movable.png");
        faceableArrows = Resources.getImage("/assets/graphics/ui/arrows.png");
        attackableOverlay = Resources.getImage("/assets/graphics/ui/attackable.png");
        

        sidebarButtons = new Button[]{
                Helpful.makeButton(Constants.WINDOW_WIDTH - Constants.BOARD_SIDEBAR_WIDTH / 2, 50, "End Turn", new ClickListener(){
                    
                    @Override
                    public void onClick(Button clicked, float mx, float my) {
                        if (pc.player.equals(pc.model.getCurrentPlayer())){
                            endTurn(pc);
                        }
                    }

                    @Override
                    public void onRightClick(Button clicked, float mx, float my) {
                    }

                    @Override
                    public void onDoubleClick(Button clicked, float mx, float my) {
                    }
                    
                }),
        };
        for (Button b : sidebarButtons){
            stickyListener.add(b);
        }
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        pc.renderBoard(g);
        pc.renderPieces(g);
        if (isLocal){
            renderLocal(container, (LocalPlayerController)pc, g);
        }
    }
    
    public void renderLocalSidebar(GameContainer container, Graphics g){
        g.setColor(new Color(0x77000000));
        g.fillRect(Constants.WINDOW_WIDTH - Constants.BOARD_SIDEBAR_WIDTH, 0, Constants.BOARD_SIDEBAR_WIDTH, Constants.WINDOW_HEIGHT);
        for (Button b : sidebarButtons){
            b.render(container, g);
        }
    }
    
    public void updateLocalSidebar(GameContainer container, int delta){
        for (Button b : sidebarButtons){
            b.update(container, delta);
        }
    }
    
    public void renderLocal(GameContainer container, LocalPlayerController pc, Graphics g) throws SlickException {
        Point position = PlayBoardState.getTileCoords(container.getInput().getMouseX() + pc.displayX, container.getInput().getMouseY()
                + pc.displayY);
        if (pc.model.isValidPosition(position)) {
            pc.renderAtPosition(hoverOverlay, g, position.x, position.y, 0f, 0f);
        }

        if (pc.selectedPiece != null) {
            switch (pc.selectedPiece.turnState) {
            case MOVING:
                if (pc.selectedPieceMove == null){
                    for (Point p : pc.selectedPiece.getValidMoves()) {
                        if (pc.model.isValidPosition(p)) {
                            pc.renderAtPosition(movableOverlay, g, p.x, p.y, 0f, 0f);
                        }
                    }
                } else if (pc.selectedPieceFace == -1){
                    pc.renderAtPosition(faceableArrows, g, pc.selectedPieceMove.x, pc.selectedPieceMove.y, 0.5f, 0.5f);
                    // TODO
                } else{
                    for (Point pos : pc.selectedPiece.getValidAttacks(pc.selectedPieceMove, pc.selectedPieceFace)) {
                        if (pc.model.isValidPosition(pos)) {
                            Piece p = pc.model.getPieceByPosition(pos);
                            if (p != null && !p.owner.equals(pc.selectedPiece.owner)){
                                pc.renderAtPosition(attackableOverlay, g, pos.x, pos.y, 0f, 0f);
                            }
                        }
                    }
                }
                break;
            case TURNING:
                throw new RuntimeException();
            case ATTACKING:
                break;
            case DONE:
                // do nothing
                break;
            default:
                throw new RuntimeException();
            }
        }
        renderLocalSidebar(container, g);
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        if (pc.isCurrentPC()){
            pc.updateMousePan(container, pc, delta);
            updateLocalSidebar(container, delta);
            
            if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                Point position = PlayBoardState.getTileCoords(container.getInput().getMouseX() + pc.displayX, container.getInput().getMouseY()
                        + pc.displayY);
                Piece piece = pc.model.getPieceByPosition(position);
        
                if (container.getInput().isKeyDown(Input.KEY_LSHIFT)) {
                    if (piece != null && piece.owner.equals(pc.player) && !piece.equals(pc.selectedPiece)) {
                        pc.selectedPiece = piece;
                        pc.selectedPieceMove = null;
                        pc.selectedPieceFace = -1;
                    }
                } else if (pc.selectedPiece != null) {
                    switch (pc.selectedPiece.turnState) {
                    case MOVING:
                        if (pc.selectedPieceMove == null){
                            if (pc.model.isValidPosition(position)
                                    && Arrays.asList(pc.selectedPiece.getValidMoves()).contains(position)
                                    && (piece == null || piece == pc.selectedPiece)) {
                                pc.selectedPieceMove = position;
                            } else {
                                // TODO: do nothing?
                            }
                        } else if (pc.selectedPieceFace == -1){
                            int direction = Piece.pointsToDirection(position, pc.selectedPieceMove);
                            if (direction != -1) {
                                pc.selectedPieceFace = direction;
                                boolean any = false;
                                for (Point pos : pc.selectedPiece.getValidAttacks(pc.selectedPieceMove, pc.selectedPieceFace)) {
                                    Piece p = pc.model.getPieceByPosition(pos);
                                    if (p != null && !p.owner.equals(pc.selectedPiece.owner)){
                                        System.out.println(pos);
                                        any = true;
                                        break;
                                    }
                                }
                                if (!any){
                                    pc.actionQueue.add(new Action.MoveAction(pc.selectedPiece, pc.selectedPieceMove));
                                    pc.actionQueue.add(new Action.FaceAction(pc.selectedPiece, pc.selectedPieceFace));
                                }
                            } else {
                                // TODO: do nothing?
                            }
                        } else{
                            if (pc.model.isValidPosition(position)
                                    && Arrays.asList(pc.selectedPiece.getValidAttacks(pc.selectedPieceMove, pc.selectedPieceFace)).contains(position) && piece != null
                                    && !piece.owner.equals(pc.selectedPiece.owner)) {
                                pc.actionQueue.add(new Action.MoveAction(pc.selectedPiece, pc.selectedPieceMove));
                                pc.actionQueue.add(new Action.FaceAction(pc.selectedPiece, pc.selectedPieceFace));
                                pc.actionQueue.add(new Action.AttackAction(pc.selectedPiece, piece));
                            } else {
                                // TODO: do nothing?
                            }
                        }
                        break;
                    case TURNING:
                        throw new RuntimeException();
                    case ATTACKING:
                        throw new RuntimeException();
                    case DONE:
                        pc.selectedPiece = null;
                        pc.selectedPieceMove = null;
                        pc.selectedPieceFace = -1;
                        break;
                    default:
                        throw new RuntimeException();
                    }
                }
            }
        
            if (container.getInput().isKeyPressed(Input.KEY_E)) {
                endTurn(pc);
            }
        }
    }

    private void endTurn(LocalPlayerController pc){
        pc.selectedPiece = null;
        pc.selectedPieceMove = null;
        pc.selectedPieceFace = -1;
        pc.actionQueue.add(new Action.TurnEndAction(pc.player));
    }

    public static Point getTileCoords(int pixelX, int pixelY){
        double y = (1.0 * pixelY + (Constants.TILE_HEIGHT - Constants.TILE_HEIGHT_SPACING)) / Constants.TILE_HEIGHT_SPACING;
        double x = 1.0 * pixelX / Constants.TILE_WIDTH_SPACING / 2;
        double z = -0.5 * y - x;
               x = -0.5 * y + x;
        int iy = (int)Math.floor(y+0.5);
        int ix = (int)Math.floor(x+0.5);
        int iz = (int)Math.floor(z+0.5);
        int s = iy+ix+iz;
        if( s != 0){
            double abs_dy = Math.abs(iy-y);
            double abs_dx = Math.abs(ix-x);
            double abs_dz = Math.abs(iz-z);
            if( abs_dy >= abs_dx && abs_dy >= abs_dz )
                iy -= s;
            else if( abs_dx >= abs_dy && abs_dx >= abs_dz )
                ix -= s;
            else
                iz -= s;
        }
        iy -= 1;
        ix -= 1;
        return new Point(ix - iz, iy);
    }
}
