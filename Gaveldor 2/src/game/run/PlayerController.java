package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.Point;
import game.model.TerrainType;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;

public abstract class PlayerController extends StateBasedGame{

    public StateBasedGame game;
    
    public final Player player;
    
    public final Queue<Action> actionQueue = new LinkedList<Action>();

    public final GameModel model; // for getting game state info only (no updating)

    public boolean justStarted = true;
    public int displayX = 0, displayY = 0;

    public PlayerController(Player player, GameModel model) {
        super(null);
        this.player = player;
        this.model = model;
    }
    
    public static void initAssets() throws SlickException{
        GameModel.AttackResult.initAssets();
        PlayBoardState.initAssets();
    }

    public Action retrieveAction(){
        return actionQueue.poll();
    }

    public abstract void propagateAction(Action action);

    
    public void setDisplayPoint(GameContainer container, int pixelX, int pixelY){
        displayX = pixelX;
        displayX = Math.max(displayX, -container.getWidth() / 2);
        displayX = Math.min(displayX, model.map.getPixelWidth() - container.getWidth() / 2);
        displayY = pixelY;
        displayY = Math.max(displayY, -container.getHeight() / 2);
        displayY = Math.min(displayY, model.map.getPixelHeight() - container.getHeight() / 2);
    }
    
    public void setDisplayCenter(GameContainer container, float tileX, float tileY){
        setDisplayPoint(container,
                getPixelX(tileX, container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH / 2, .5f),
                getPixelY(tileY, container.getHeight(), .5f));
    }
    
    public void setDisplayCenterPiecesAverage(GameContainer container){
        int x = 0, y = 0, count = 0;
        for (Piece p : model.getPieces()){
            if (p.owner.equals(player)){
                x += p.getPosition().x;
                y += p.getPosition().y;
                count++;
            }
        }
        setDisplayCenter(container, 1f * x / count, 1f * y / count);
    }
    
    public void renderControllerWon(GameContainer container, Graphics g) throws SlickException {
        this.renderHeaderText(container, g, model.getCurrentPlayer().toString() + " Wins!");
    }
    
    public static int getPixelX(float x, int width, float centerX){
        return Math.round(x * Constants.TILE_WIDTH_SPACING + (Constants.TILE_WIDTH - width) * centerX);
    }
    
    public static int getPixelY(float y, int height, float centerY){
        return Math.round(y * Constants.TILE_HEIGHT_SPACING + (Constants.TILE_HEIGHT - height) * centerY);
    }

    public void renderAtPosition(Image image, Graphics g, int x, int y, float centerX, float centerY) {
        g.drawImage(image,
                getPixelX(x, image.getWidth(), centerX) - displayX,
                getPixelY(y, image.getHeight(), centerY)- displayY);
    }
    
    public abstract boolean isAnimatingMove();
    
    public void renderHeaderText(GameContainer container, Graphics g, String str){
        g.setColor(Color.white);
        g.setFont(Constants.PRIMARY_FONT);
        g.drawString(str, (container.getWidth() - g.getFont().getWidth(str)) / 2, g.getFont().getHeight(str) / 2);
    }

    
    public void renderBoard(GameContainer container, Graphics g){
        int w = (int) Math.ceil(1.0 * container.getWidth() / Constants.TILE_WIDTH_SPACING / 2) + 1;
        int h = (int) Math.ceil(1.0 * (container.getHeight() + (Constants.TILE_HEIGHT - Constants.TILE_HEIGHT_SPACING)) / Constants.TILE_HEIGHT_SPACING);
        for (int j = - h; j < model.map.height + h; j++) {
            for (int i = j % 2 - (w + w % 2); i < model.map.width + w; i += 2) {
                TerrainType terrain;
                if (j < 0 || j >= model.map.height || i < 0 || i >= model.map.width){
                    terrain = TerrainType.MOUNTAINS;
                } else{
                    terrain = model.map.getTerrain(i, j);
                }
                renderAtPosition(terrain.tile, g, i, j, 0f, 0f);
//                Piece p = getPieceByRenderPosition(new Point(i, j));
//                if (p != null){
//                    renderPiece(container, g, p);
//                }
            }
        }

        for (Piece p : model.getPieces()) {
            renderPiece(container, g, p);
        }
    }
    
    public void renderPieceMoving(GameContainer container, Graphics g, Piece p, Point oldPos, Point newPos, long sinceStart){
        List<Point> path = model.findValidMoves(p, oldPos, true).get(newPos);
        int step = (int)(sinceStart / Constants.BOARD_MOVE_ANIMATE_TIME);
        float frac = 1f * sinceStart / Constants.BOARD_MOVE_ANIMATE_TIME - step;
        Point cur = path.get(step), next = path.get(step + 1);
        int x = Math.round(getPixelX(cur.x, p.getSprite().getWidth(), .5f) * (1f - frac)
                + getPixelX(next.x, p.getSprite().getWidth(), .5f) * frac);
        int y = Math.round(getPixelY(cur.y, p.getSprite().getHeight(), 1f) * (1f - frac)
                + getPixelY(next.y, p.getSprite().getHeight(), 1f) * frac);
        Image sprite = p.getSprite(Piece.pointsToDirection(next, cur), 0);
        setDisplayPoint(container,
                x + (sprite.getWidth() - container.getWidth()) / 2,
                y + (sprite.getHeight()- container.getHeight()) / 2);
        g.drawImage(sprite, x - displayX, y - displayY);
    }
    
    public void renderPiece(GameContainer container, Graphics g, Piece p){
        renderAtPosition(p.getSprite(), g, p.getPosition().x, p.getPosition().y, .5f, 1f);
    }
    
    public void renderAttack(GameContainer container, Graphics g){
        if (model.lastMoved != null && model.lastMovedAttackResult != null && model.sinceLastMoved < Constants.ATTACK_DISPLAY_TIME){
            g.setColor(model.lastMovedAttackResult == GameModel.AttackResult.CRITICAL ? Color.red : Color.white);
            g.setFont(Constants.PRIMARY_FONT);
            Image im = model.lastMovedAttackResult.image;
            float frac = 1f * model.sinceLastMoved / Constants.ATTACK_DISPLAY_TIME;
            g.drawImage(im,
                    getPixelX(model.lastMoved.getPosition().x, im.getWidth(), .5f) - displayX,
                    getPixelY(model.lastMoved.getPosition().y, im.getHeight(), .5f) - displayY
                    - (Constants.ATTACK_DISPLAY_FLOAT_MIN_DIST + (1 - (float)Math.pow(1 - frac, 3)) * (Constants.ATTACK_DISPLAY_FLOAT_MAX_DIST - Constants.ATTACK_DISPLAY_FLOAT_MIN_DIST)));
            if (model.lastMovedJustHappened && model.lastMovedAttackResult.sound != null){
                model.lastMovedAttackResult.sound.play();
            }
        }
    }
    
    public boolean isCurrentPC(){
        return player.equals(model.getCurrentPlayer());
    }
    
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
        this.game = game;
        update(container, delta);
    }
}
