package game.run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.model.Piece;
import game.model.Piece.TurnState;
import game.model.Point;
import game.model.TerrainType;

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
    private GameContainer gameContainer;
    //private PlayerController stateGame;
    
    private Button[] sidebarButtons;
    
    private boolean wasAnimatingMove = false;

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        gameContainer = container;
        //stateGame = pc;
        if (isLocal){
            initLocal(container, (LocalPlayerController)pc);
        }
    }
    
    public void initLocal(GameContainer container, final LocalPlayerController pc) throws SlickException{
        hoverOverlay = Resources.getImage("/assets/graphics/ui/hover.png").getScaledCopy(.5f);
        movableOverlay = Resources.getImage("/assets/graphics/ui/movable.png").getScaledCopy(.5f);
        faceableArrows = Resources.getImage("/assets/graphics/ui/arrows.png").getScaledCopy(.5f);
        attackableOverlay = Resources.getImage("/assets/graphics/ui/attackable.png").getScaledCopy(.5f);
        

        sidebarButtons = new Button[]{
                Helpful.makeButton(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH / 2, 250, "End Turn", new ClickListener(){
                    @Override
                    public void onClick(Button clicked, float mx, float my) {
                        endTurn(pc);
                    }
                    @Override
                    public void onRightClick(Button clicked, float mx, float my) {
                    }
                    @Override
                    public void onDoubleClick(Button clicked, float mx, float my) {
                    }
                }),
                Helpful.makeButton(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH / 2, 350, "Cancel", new ClickListener(){
                    @Override
                    public void onClick(Button clicked, float mx, float my) {
                        if (pc.selectedPiece != null){
                            clearSelection(pc);
                        }
                    }
                    @Override
                    public void onRightClick(Button clicked, float mx, float my) {
                    }
                    @Override
                    public void onDoubleClick(Button clicked, float mx, float my) {
                    }
                }),
                Helpful.makeButton(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH / 2, 550, "Mute", new ClickListener(){
                    @Override
                    public void onClick(Button clicked, float mx, float my) {
                        mute();  
                    }
                    @Override
                    public void onRightClick(Button clicked, float mx, float my) {
                    }
                    @Override
                    public void onDoubleClick(Button clicked, float mx, float my) {
                    }
                }),
                Helpful.makeButton(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH / 2, 650, "Exit Game", new ClickListener(){
                    @Override
                    public void onClick(Button clicked, float mx, float my) {
                         gameContainer.exit();
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
    public void enter(GameContainer container, PlayerController pc) throws SlickException{
        wasAnimatingMove = false;
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        pc.renderBoard(container, g);
        pc.renderPieces(container, g);
        if (isLocal){
            renderLocal(container, (LocalPlayerController)pc, g);
        }
    }
    
    public void renderMinimap(GameContainer container, Graphics g, LocalPlayerController pc, int x, int y) throws SlickException{
        //TODO clean up constants
        final int minimapWidth = 200, minimapHeight = 200;
        float scale = Math.min(1f * minimapWidth / pc.model.map.getPixelWidth() , 1f * minimapHeight / pc.model.map.getPixelHeight());
        int width = Math.round(pc.model.map.getPixelWidth() * scale);
        int height = Math.round(pc.model.map.getPixelHeight() * scale);
        int xi = x + (minimapWidth - width) / 2, yi = y + (minimapHeight - height) / 2;
        g.setColor(Color.black);
        g.fillRect(x, y, minimapWidth, minimapHeight);
        g.setColor(Color.white);
        for (int j = 0; j < pc.model.map.height; j++) {
            for (int i = j % 2; i < pc.model.map.width; i += 2) {
                g.drawOval(
                        PlayerController.getPixelX(i, Constants.TILE_WIDTH, .5f) * scale + xi,
                        PlayerController.getPixelY(j, Constants.TILE_HEIGHT, .5f) * scale + yi,
                        Constants.TILE_WIDTH * scale, Constants.TILE_HEIGHT * scale);
            }
        }
        for (Piece p : pc.model.getPieces()) {
            g.setColor(p.owner.id == 1 ? Color.blue : Color.orange); //TODO: add minimap assets
            g.fillOval(
                        PlayerController.getPixelX(p.getPosition().x, Constants.TILE_WIDTH, .5f) * scale + xi,
                        PlayerController.getPixelY(p.getPosition().y, Constants.TILE_HEIGHT, .5f) * scale + yi,
                        Constants.TILE_WIDTH * scale, Constants.TILE_HEIGHT * scale);
        }
        g.setColor(Color.white);
        float rl = Math.max(pc.displayX * scale + xi, x), rt = Math.max(pc.displayY * scale + yi, y),
                rr = Math.min((pc.displayX + container.getWidth()) * scale + xi, x + minimapWidth),
                rb = Math.min((pc.displayY + container.getWidth()) * scale + yi, y + minimapHeight);
        
        g.drawRect(rl, rt, rr - rl, rb - rt);
    }
    
    public void renderLocalSidebar(GameContainer container, LocalPlayerController pc, Graphics g) throws SlickException{
        g.setColor(new Color(0x77000000));
        g.fillRect(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH, 0, Constants.BOARD_SIDEBAR_WIDTH, container.getHeight());
        g.setColor(Color.white);
        g.drawString(pc.player.toString(), container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH + 10, 50);
        renderMinimap(container, g, pc, container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH, 0);
        for (Button b : sidebarButtons){
            b.render(container, g);
        }
    }
    
    public void updateLocalSidebar(GameContainer container, LocalPlayerController pc, int delta){
        for (Button b : sidebarButtons){
            b.update(container, delta);
        }
    }
    
    public void renderLocal(GameContainer container, LocalPlayerController pc, Graphics g) throws SlickException {
        if (pc.isAnimatingMove()){
            
        } else if (wasAnimatingMove){
            if (pc.model.getMinigame() != null){
                pc.actionQueue.add(new Action.MinigameStartAction());
            }
            wasAnimatingMove = false;
        } else{
            Point position = PlayBoardState.getTileCoords(container.getInput().getMouseX() + pc.displayX, container.getInput().getMouseY()
                    + pc.displayY);
            if (pc.model.isValidPosition(position)) {
                pc.renderAtPosition(hoverOverlay, g, position.x, position.y, 0f, 0f);
            }
    
            if (pc.selectedPiece != null) {
                if (pc.player.equals(pc.selectedPiece.owner)){
                    switch (pc.selectedPiece.turnState) {
                    case MOVING:
                        if (pc.selectedPieceMove == null){
                            for (Point p : pc.selectedPiece.getValidMoves()) {
                                // Don't do anything if pos isn't even valid
                                if(pc.model.isValidPosition(p) == false){
                                    continue;
                                }
                                
                                // Ok, load stuff and check terrain?
                                Piece piece = pc.model.getPieceByPosition(p);
                                TerrainType t = pc.model.map.getTerrain(p);
                                if(t != null && t.enterable(pc.selectedPiece) == false){
                                    continue; // Skip this square for rendering movement
                                }
    
                                // Go go go!
                                if (piece == null || piece == pc.selectedPiece) {
                                    pc.renderAtPosition(movableOverlay, g, p.x, p.y, 0f, 0f);
                                }
                            }
                        } else if (pc.selectedPieceFace == -1){
                            pc.renderAtPosition(faceableArrows, g, pc.selectedPieceMove.x, pc.selectedPieceMove.y, 0.5f, 0.5f);
                            // TODO: add full tile overlays
                        } else{
                            for (Point pos : pc.selectedPiece.getValidAttacks(pc.selectedPieceMove, pc.selectedPieceFace)) {
                                if (pc.model.isValidPosition(pos)) {
                                    Piece p = pc.model.getPieceByPosition(pos);
                                    if (p != null && !p.owner.equals(pc.selectedPiece.owner)){
                                        pc.renderAtPosition(attackableOverlay, g, pos.x, pos.y, 0f, 0f);
                                    }
                                }
                            }
                            //TODO: use different overlay
                            pc.renderAtPosition(attackableOverlay, g, pc.selectedPieceMove.x, pc.selectedPieceMove.y, 0f, 0f);
                        }
                        break;
                    case DONE:
                        // do nothing
                        break;
                    default:
                        throw new RuntimeException();
                    }
                } else{
                    for (Point p : pc.selectedPiece.getValidMoves()) {
                        if (pc.model.isValidPosition(p)) {
                            pc.renderAtPosition(movableOverlay, g, p.x, p.y, 0f, 0f);
                        }
                    }
                }
            }
            renderLocalSidebar(container, pc, g);
        }
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        if (pc.isCurrentPC() && !pc.isAnimatingMove()){
            container.getInput().addListener(stickyListener);
        } else{
            container.getInput().removeListener(stickyListener);
        }
        
        if (pc.isCurrentPC()){
            if (pc.isAnimatingMove()){
                //TODO
            } else{
                pc.updateMousePan(container, pc, delta);
                updateLocalSidebar(container, pc, delta);
                
                if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) &&
                        container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    Point position = PlayBoardState.getTileCoords(container.getInput().getMouseX() + pc.displayX, container.getInput().getMouseY()
                            + pc.displayY);
                    Piece piece = pc.model.getPieceByPosition(position);
                    
                    // Get terrain type, but only if valid position (index out of bounds error otherwise)
                    TerrainType t = null;
                    if(pc.model.isValidPosition(position)){
                        t = pc.model.map.getTerrain(position);
                    }
                    
                    if (pc.selectedPiece == null || !pc.player.equals(pc.selectedPiece.owner)){
                        if (piece != null && !(pc.player.equals(piece.owner) && piece.turnState == TurnState.DONE)) {
                            pc.selectedPiece = piece;
                            pc.setDisplayCenter(container, piece.getPosition().x, piece.getPosition().y);
                        }
                    } else {
                        switch (pc.selectedPiece.turnState) {
                        case MOVING:
                            if (pc.selectedPieceMove == null){
                                if (pc.model.isValidPosition(position)
                                        && Arrays.asList(pc.selectedPiece.getValidMoves()).contains(position)
                                        && (piece == null || piece == pc.selectedPiece)
                                        && (t == null || t.enterable(pc.selectedPiece))) {
                                    pc.selectedPieceMove = position;
                                }
                            } else if (pc.selectedPieceFace == -1){
                                int direction = Piece.pointsToDirection(position, pc.selectedPieceMove);
                                if (direction != -1) {
                                    pc.selectedPieceFace = direction;
                                }
                            } else{
                                if (pc.model.isValidPosition(position)
                                        && (Arrays.asList(pc.selectedPiece.getValidAttacks(pc.selectedPieceMove, pc.selectedPieceFace)).contains(position) && piece != null
                                        && !piece.owner.equals(pc.selectedPiece.owner)) || position.equals(pc.selectedPieceMove)) {
                                    pc.actionQueue.add(new Action.BoardMoveAction(
                                            pc.selectedPiece, pc.selectedPieceMove, pc.selectedPieceFace,
                                            position.equals(pc.selectedPieceMove)? null: piece));
                                    clearSelection(pc);
                                    wasAnimatingMove = true;
                                }
                            }
                            break;
                        case DONE:
                        default:
                            throw new RuntimeException();
                        }
                    }
                }
            }
        
            if (container.getInput().isKeyPressed(Input.KEY_E)) {
                endTurn(pc);
            }
        }
    }

    private void endTurn(LocalPlayerController pc){
        clearSelection(pc);
        pc.actionQueue.add(new Action.TurnEndAction(pc.player));
    }
    
    private void clearSelection(LocalPlayerController pc){
        pc.selectedPiece = null;
        pc.selectedPieceMove = null;
        pc.selectedPieceFace = -1;
    }

    public static Point getTileCoords(int pixelX, int pixelY){
        double x = 1.0 * pixelX / Constants.TILE_WIDTH_SPACING / 2;
        double y = (1.0 * pixelY + (Constants.TILE_HEIGHT - Constants.TILE_HEIGHT_SPACING)) / Constants.TILE_HEIGHT_SPACING;
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
    
    public void mute() {
        System.out.println("Reached mute!");
        if (gameContainer.isMusicOn() | gameContainer.isSoundOn()) {
            gameContainer.setMusicOn(false);
            gameContainer.setSoundOn(false);
            System.out.println("Trying to mute");
        }
        else {
            System.out.println("Trying to unmute.");
            gameContainer.setSoundOn(true);
            gameContainer.setMusicOn(true);
        }
        
    }
}
