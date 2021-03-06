package game.run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.model.Piece;
import game.model.Piece.TurnState;
import game.model.Player;
import game.model.Point;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import util.Constants;
import util.LayoutButton;
import util.Resources;

import com.aem.sticky.button.Button;
import com.aem.sticky.button.events.ClickListener;

public class PlayBoardState extends PlayerControllerState {

    public PlayBoardState(boolean isLocal) {
        super(GameState.PLAYING_BOARD, isLocal);
    }
    
    private static Image hoverOverlay, movableOverlay, faceableArrows, attackableOverlay,
            minimapBlue, minimapEmpty, minimapGrey, minimapOrange, minimapSelected;
    private GameContainer gameContainer;
    //private PlayerController stateGame;
    
    public static String tutorialString = ""; //Constants.NONE_SELECTED;
    
    private SidebarButton[] sidebarButtons;
    
    private boolean wasAnimatingMove = false;
    
    public static void initAssets() throws SlickException{
        hoverOverlay = Resources.getImage("/assets/graphics/ui/hover.png").getScaledCopy(.5f);
        movableOverlay = Resources.getImage("/assets/graphics/ui/movable.png").getScaledCopy(.5f);
        faceableArrows = Resources.getImage("/assets/graphics/ui/arrows.png").getScaledCopy(.5f);
        attackableOverlay = Resources.getImage("/assets/graphics/ui/attackable.png").getScaledCopy(.5f);
        minimapBlue = Resources.getImage("/assets/graphics/ui/minimap/minimap_blue.png");
        minimapEmpty = Resources.getImage("/assets/graphics/ui/minimap/minimap_empty.png");
        minimapGrey = Resources.getImage("/assets/graphics/ui/minimap/minimap_grey.png");
        minimapOrange = Resources.getImage("/assets/graphics/ui/minimap/minimap_orange.png");
        minimapSelected = Resources.getImage("/assets/graphics/ui/minimap/minimap_selected.png");
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        gameContainer = container;
        initSidebar(container, pc);
    }
    
    private class SidebarButton extends LayoutButton{
        
        private static final int WIDTH = 200, HEIGHT = 50;
        
        private final int y;
        
        public SidebarButton(int y, String text, ClickListener listener, Player player) throws SlickException {
            super(Resources.getImage("/assets/graphics/buttons/playgame/" + text + "_player" + player.id + ".png"), 
                    Resources.getImage("/assets/graphics/buttons/playgame/" + text + "_player" + player.id + "_hover.png"), 
                    null);
            this.y = y;
            this.addListener(listener);
        }

        @Override
        public Shape calculateShape(GameContainer container) {
            return new Rectangle(
                    container.getWidth() - (Constants.BOARD_SIDEBAR_WIDTH + WIDTH) / 2,
                    y, WIDTH, HEIGHT);
        }
    }
    
    public void initSidebar(GameContainer container, final PlayerController pc) throws SlickException{

        final Player player = isLocal ? pc.player : pc.model.getOtherPlayer(pc.player);
        
        ClickListener endTurnListener = new ClickListener(){
            @Override
            public void onClick(Button clicked, float mx, float my) {
                if (((LocalPlayerController)pc).selectedPiece == null) {
                    endTurn(((LocalPlayerController)pc));
                }
            }
            @Override
            public void onRightClick(Button clicked, float mx, float my) {
            }
            @Override
            public void onDoubleClick(Button clicked, float mx, float my) {
            }
        },
        cancelListener = new ClickListener(){
            @Override
            public void onClick(Button clicked, float mx, float my) {
                if (((LocalPlayerController)pc).selectedPiece != null){
                    clearSelection(((LocalPlayerController)pc));
                }
            }
            @Override
            public void onRightClick(Button clicked, float mx, float my) {
            }
            @Override
            public void onDoubleClick(Button clicked, float mx, float my) {
            }
        },
        muteListener = new ClickListener(){
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
        },
        forfeitListener = new ClickListener(){
            @Override
            public void onClick(Button clicked, float mx, float my) {
                pc.actionQueue.add(new Action.ForfeitAction(player));
//                 pc.game.enterState(MainMenuState.STATE_ID);
            }
            @Override
            public void onRightClick(Button clicked, float mx, float my) {
            }
            @Override
            public void onDoubleClick(Button clicked, float mx, float my) {
            }
        };
        
        if (isLocal){
            sidebarButtons = new SidebarButton[]{
                    new SidebarButton(250, "end_turn", endTurnListener, player),
                    new SidebarButton(250, "cancel", cancelListener, player),
                    new SidebarButton(550, "mute", muteListener, player),
                    new SidebarButton(650, "forfeit", forfeitListener, player),
            };
        } else{
            sidebarButtons = new SidebarButton[]{
                    new SidebarButton(550, "mute", muteListener, player),
                    new SidebarButton(650, "forfeit", forfeitListener, player),
            };
        }
//        if (pc.player.equals(pc.model.getPlayer1()) || !Constants.PLAYER2_ORANGE_SIDEBAR){
//            sidebarButtons = new SidebarButton[]{
//                    new SidebarButton(250, "end_turn", endTurnListener, 1),
//                    new SidebarButton(250, "cancel", cancelListener, 1),
//                    new SidebarButton(550, "mute", muteListener, 1),
//                    new SidebarButton(650, "forfeit", forfeitListener, 1),
//            };
//        } else{
//            sidebarButtons = new SidebarButton[]{
//                    new SidebarButton(250, "end_turn", endTurnListener, 2),
//                    new SidebarButton(250, "cancel", cancelListener, 2),
//                    new SidebarButton(550, "mute", muteListener, 2),
//                    new SidebarButton(650, "forfeit", forfeitListener, 2),
//            };
//        }
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
        if (isLocal){
            renderLocal(container, (LocalPlayerController)pc, g);
        }
        if (!pc.isAnimatingMove()){
            renderSidebar(container, pc, g);
        }
        pc.renderAttack(container, g);
        renderHeader(container, g, pc);
    }
    
    public void renderHeader(GameContainer container, Graphics g, PlayerController pc){
        int piecesMoved = 0;
        for (Piece p : pc.model.getPieces()){
            if (p.owner.equals(pc.player) && p.turnState == Piece.TurnState.DONE){
                piecesMoved++;
            }
        }
        if(piecesMoved == pc.model.numberOfPieces(pc.player)){tutorialString = Constants.DONE;}
        String str = pc.player.toString() + ": " + piecesMoved + "/" + pc.model.numberOfPieces(pc.player) + " Pieces Moved";
        pc.renderHeaderText(container, g, str);
    }
    
    public void renderMinimap(GameContainer container, Graphics g, PlayerController pc, int x, int y) throws SlickException{
        final int minimapWidth = 200, minimapHeight = 200;
        float scale = Math.min(1f * minimapWidth / pc.model.map.getPixelWidth() , 1f * minimapHeight / pc.model.map.getPixelHeight());
        int width = Math.round(pc.model.map.getPixelWidth() * scale);
        int height = Math.round(pc.model.map.getPixelHeight() * scale);
        int xi = x + (minimapWidth - width) / 2, yi = y + (minimapHeight - height) / 2;
        g.setColor(Color.black);
        g.fillRect(x, y, minimapWidth, minimapHeight);
        for (int j = 0; j < pc.model.map.height; j++) {
            for (int i = j % 2; i < pc.model.map.width; i += 2) {
                minimapEmpty.draw(
                        PlayerController.getPixelX(i, Constants.TILE_WIDTH, .5f) * scale + xi,
                        PlayerController.getPixelY(j, Constants.TILE_HEIGHT, .5f) * scale + yi,
                        scale * Constants.TILE_WIDTH / minimapEmpty.getWidth());
            }
        }
        for (Piece p : pc.model.getPieces()) {
            Image im = p.owner.equals(pc.player) && isLocal && p.equals(((LocalPlayerController)pc).selectedPiece) ? minimapSelected : p.turnState == TurnState.DONE ? minimapGrey : p.owner.id == 1 ? minimapBlue : minimapOrange;
            im.draw(
                    PlayerController.getPixelX(p.getPosition().x, Constants.TILE_WIDTH, .5f) * scale + xi,
                    PlayerController.getPixelY(p.getPosition().y, Constants.TILE_HEIGHT, .5f) * scale + yi,
                    scale * Constants.TILE_WIDTH / im.getWidth());
        }
        g.setColor(Color.white);
        float rl = Math.max(pc.displayX * scale + xi, x), rt = Math.max(pc.displayY * scale + yi, y),
                rr = Math.min((pc.displayX + container.getWidth()) * scale + xi, x + minimapWidth),
                rb = Math.min((pc.displayY + container.getHeight()) * scale + yi, y + minimapHeight);
        g.drawRect(rl, rt, rr - rl, rb - rt);
    }
    
    public void renderSidebar(GameContainer container, PlayerController pc, Graphics g) throws SlickException{
        g.setColor(new Color(0x77000000));
        g.fillRect(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH, 0, Constants.BOARD_SIDEBAR_WIDTH, container.getHeight());
        g.setColor(Color.white);
        g.setFont(Constants.PRIMARY_FONT);
        //g.drawString(pc.player.toString(), container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH + 10, 200);
        renderMinimap(container, g, pc, container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH, 0);
        
        g.setFont(Constants.TUTORIAL_FONT);
        g.drawString(tutorialString, container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH + 30, 350);
        
        for (Button b : sidebarButtons){
            //End Turn button
            if (isLocal){
                if (b.equals(sidebarButtons[0])) {
                    if (((LocalPlayerController)pc).selectedPiece == null) {
                        b.render(container, g);
                    }
                }
                //Cancel button
                else if (b.equals(sidebarButtons[1])) {
                    if (((LocalPlayerController)pc).selectedPiece != null) {
                        b.render(container, g);
                    }
                }
                else {
                    b.render(container, g);
                }
            } else{
                b.render(container, g);
            }
        }
    }
    
    public void updateSidebar(GameContainer container, PlayerController pc, int delta){
        if (pc.player.equals(pc.model.getCurrentPlayer())){
            for (Button b : sidebarButtons){
                b.update(container, delta);
            }
        }
    }
    
    public void renderLocal(GameContainer container, LocalPlayerController pc, Graphics g) throws SlickException {
        if (pc.isAnimatingMove()){
            
        } else if (wasAnimatingMove){
            wasAnimatingMove = false;
        } else{
            Point position = PlayBoardState.getTileCoords(
                    container.getInput().getMouseX() + pc.displayX,
                    container.getInput().getMouseY() + pc.displayY);
            if (pc.model.isValidPosition(position) && container.getInput().getMouseX() < container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH) {
                pc.renderAtPosition(hoverOverlay, g, position.x, position.y, 0f, 0f);
            }
            
            Piece piece = pc.selectedPiece;
            if (container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && container.getInput().getMouseX() < container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH){
                Piece hoveredPiece = pc.model.getPieceByPosition(position);
                if (hoveredPiece != null){
                    piece = hoveredPiece;
                }
            }
    
            if (piece != null) {
                if (piece.equals(pc.selectedPiece) && piece.turnState == Piece.TurnState.MOVING){
                    tutorialString = Constants.MOVING;
                } else{
                    if (pc.isCurrentPC()) {
                        tutorialString = Constants.NONE_SELECTED;
                    }
                    else {
                        tutorialString = "";
                    }
                }
                if (!piece.equals(pc.selectedPiece) || pc.selectedPieceMove == null){
                    Set<Point> moves = pc.model.findValidMoves(piece, true).keySet();
                    Set<Point> attacks = new HashSet<Point>();
                    for (Point p : moves) {
                        pc.renderAtPosition(movableOverlay, g, p.x, p.y, 0f, 0f);
                        if(Constants.SHOW_ATTACK_WHILE_MOVING){
                            for (Point loc : pc.model.findValidAttacks(piece, p)) {
                                if (!moves.contains(loc) && !attacks.contains(loc)) {
                                    attacks.add(loc);
                                }
                            }
                        }
                    }
                    for (Point p : attacks){
                        pc.renderAtPosition(attackableOverlay, g, p.x, p.y, 0f, 0f);
                    }
                } else if (pc.selectedPieceFace == -1){
                    tutorialString = Constants.FACING;
                    pc.renderAtPosition(faceableArrows, g, pc.selectedPieceMove.x, pc.selectedPieceMove.y, 0.5f, 0.5f);
                } else{
                    tutorialString = Constants.ATTACK;
                    for (Point loc : pc.model.findValidAttacks(pc.selectedPiece, pc.selectedPieceMove, pc.selectedPieceFace)) {
                        Piece atPoint = pc.model.getPieceByPosition(loc);
                        if (pc.model.isValidPosition(loc) && atPoint != null && !atPoint.owner.equals(pc.player)) {
                            pc.renderAtPosition(attackableOverlay, g, loc.x, loc.y, 0f, 0f);
                        }
                    }
                    pc.renderAtPosition(movableOverlay, g, pc.selectedPieceMove.x, pc.selectedPieceMove.y, 0f, 0f);
                }
            }
            else {
                if (pc.isCurrentPC()) {
                    tutorialString = Constants.NONE_SELECTED;
                }
                else {
                    tutorialString = "";
                }
            }
        }
        if (Constants.TURN_TIME_LIMIT_ON){
            float timeFrac = 1f * (timeLimit(pc) - pc.model.sinceTurnStart) / timeLimit(pc);
            g.setColor(Color.red);
            g.fillRect(0, container.getHeight() * (1 - timeFrac), 20, container.getHeight() * timeFrac);
        }
    }
    
    private long timeLimit(LocalPlayerController pc){
        return Constants.TURN_TIME_LIMIT_PER_PIECE * pc.model.numberOfPieces(pc.player);
    }
    
    @Override
    public void updateEither(GameContainer container, PlayerController pc, int delta) throws SlickException{
        if (pc.justStarted){
            pc.setDisplayCenterPiecesAverage(container);
        }
        pc.justStarted = false;
        
        if (pc.isCurrentPC() && !pc.isAnimatingMove()){
            container.getInput().addListener(stickyListener);
            updateSidebar(container, pc, delta);
        } else{
            container.getInput().removeListener(stickyListener);
        }
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        if (pc.isCurrentPC()){
            if (pc.isAnimatingMove()){
                pc.sinceSelectedPieceMove += delta;
            } else{
                pc.updateMousePan(container, pc, delta);
                
                if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) &&
                        container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
                    if (container.getInput().getMouseX() < container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH){
                        
                        Point position = PlayBoardState.getTileCoords(container.getInput().getMouseX() + pc.displayX, container.getInput().getMouseY()
                                + pc.displayY);
                        Piece piece = pc.model.getPieceByPosition(position);
                        
                        if (piece != null && pc.player.equals(piece.owner) && (piece.turnState != Piece.TurnState.DONE) && (pc.selectedPiece == null || !pc.player.equals(pc.selectedPiece.owner) ||
                                !pc.selectedPiece.equals(piece) && pc.selectedPiece.turnState == Piece.TurnState.MOVING && pc.selectedPieceMove == null)){
                            clearSelection(pc);
                            pc.selectedPiece = piece;
                            pc.setDisplayCenter(container, piece.getPosition().x, piece.getPosition().y);
                        } else if (pc.selectedPiece != null && pc.player.equals(pc.selectedPiece.owner)){
                            switch (pc.selectedPiece.turnState) {
                            case MOVING:
                                // tutorialString = "Click to Move";
                                if (pc.selectedPieceMove == null){
                                    if (pc.model.findValidMoves(pc.selectedPiece).containsKey(position)) {
                                        // findValidMoves checks terrain, piece, and position validity
                                        pc.selectedPieceMove = position;
                                        pc.sinceSelectedPieceMove = 0L;
                                        //tutorialString = Constants.FACING;
//                                        pc.setDisplayCenter(container, position.x, position.y);
                                    }
                                } else if (pc.selectedPieceFace == -1){
                                    // tutorialString = "Click to Face";
                                    int direction = Piece.pointsToDirection(position, pc.selectedPieceMove);
                                    if (direction != -1) {
                                        pc.selectedPieceFace = direction;
                                        
                                        if (pc.model.findValidAttacks(pc.selectedPiece, pc.selectedPieceMove, pc.selectedPieceFace).isEmpty()){
                                            System.out.println("No valid attacks");
                                            pc.actionQueue.add(new Action.BoardMoveAction(
                                                    pc.selectedPiece, pc.selectedPieceMove, pc.selectedPieceFace, null));
                                            clearSelection(pc);
                                        }
                                    }
                                } else{
                                    if (pc.model.isValidPosition(position)
                                            && (pc.model.findValidAttacks(pc.selectedPiece, pc.selectedPieceMove, pc.selectedPieceFace).contains(position) && piece != null
                                            && !piece.owner.equals(pc.selectedPiece.owner)) || position.equals(pc.selectedPieceMove)) {
                                        pc.actionQueue.add(new Action.BoardMoveAction(
                                                pc.selectedPiece, pc.selectedPieceMove, pc.selectedPieceFace,
                                                position.equals(pc.selectedPieceMove) ? null: piece));
                                        clearSelection(pc);
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
            }
        
            if (pc.selectedPiece == null && container.getInput().isKeyDown(Input.KEY_E) && container.getInput().isKeyPressed(Input.KEY_E)) {
                endTurn(pc);
            }
        }
    }

    private void endTurn(LocalPlayerController pc){
        clearSelection(pc);
        tutorialString = "";
        pc.actionQueue.add(new Action.TurnEndAction(pc.player));
    }
    
    private void clearSelection(LocalPlayerController pc){
        pc.selectedPiece = null;
        pc.selectedPieceMove = null;
        pc.selectedPieceFace = -1;
        tutorialString = Constants.NONE_SELECTED;
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
        if (gameContainer.isMusicOn() || gameContainer.isSoundOn()) {
            gameContainer.setMusicOn(false);
            gameContainer.setSoundOn(false);
        }
        else {
            gameContainer.setSoundOn(true);
            gameContainer.setMusicOn(true);
        }
        
    }
}
