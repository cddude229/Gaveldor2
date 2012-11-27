package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.Point;
import game.model.TerrainType;

import java.util.List;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;

public abstract class PlayerController extends StateBasedGame{

    public Game game;
    
    public final Player player;

    public final GameModel model; // for getting game state info only (no updating)

    public boolean justStarted = true;
    public int displayX = 0, displayY = 0;

    public PlayerController(Player player, GameModel model) {
        super(null);
        this.player = player;
        this.model = model;
    }

    public abstract Action retrieveAction();

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
    
    public void renderControllerWon(Graphics g) throws SlickException {
        g.setFont(Constants.TEST_FONT);
        g.drawString("Player " + model.getCurrentPlayer().id + " Wins!", 0, 0);
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

    public void renderBoard(GameContainer container, Graphics g) {
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
            }
        }
    }
    
    public boolean isAnimatingMove(){
        if (model.lastMoved == null){
            return false;
        }
        List<Point> path = model.findValidMoves(model.lastMoved, model.lastMovedPosition, true).get(model.lastMoved.getPosition());
        return model.lastMoved != null && model.sinceLastMoved < Constants.BOARD_MOVE_ANIMATE_TIME * (path.size() - 1);
    }
    
    public void renderPiece(GameContainer container, Graphics g, Piece p){
        if (p.equals(model.lastMoved) && isAnimatingMove()){
            List<Point> path = model.findValidMoves(model.lastMoved, model.lastMovedPosition, true).get(model.lastMoved.getPosition());
            int step = (int)(model.sinceLastMoved / Constants.BOARD_MOVE_ANIMATE_TIME);
            float frac = 1f * model.sinceLastMoved / Constants.BOARD_MOVE_ANIMATE_TIME - step;
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
        } else{
            renderAtPosition(p.getSprite(), g, p.getPosition().x, p.getPosition().y, .5f, 1f);
        }
    }

    public void renderPieces(GameContainer container, Graphics g) {
        for (Piece p : model.getPieces()) {
            renderPiece(container, g, p);
        }
    }
    
    public boolean isCurrentPC(){
        return player.equals(model.getCurrentPlayer());
    }
    
    public void update(GameContainer container, Game game, int delta) throws SlickException{
        this.game = game;
        if (justStarted){
            setDisplayCenterPiecesAverage(container);
        }
        update(container, delta);
        justStarted = false;
    }
}
