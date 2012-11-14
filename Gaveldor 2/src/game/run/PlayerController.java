package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.TerrainType;

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
    
    public void setDisplayCenter(GameContainer container, int tileX, int tileY){
        setDisplayPoint(container,
                getPixelX(tileX, container.getWidth(), .5f),
                getPixelY(tileY, container.getHeight(), .5f));
        }
    
    
    public void renderControllerWon(Graphics g) throws SlickException {
        g.setFont(Constants.TEST_FONT);
        g.drawString("Player " + model.getCurrentPlayer().id + " Wins!", 0, 0);
    }
    
    public static int getPixelX(int x, int width, float centerX){
        return Math.round(x * Constants.TILE_WIDTH_SPACING + (Constants.TILE_WIDTH - width) * centerX);
    }
    
    public static int getPixelY(int y, int height, float centerY){
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
        return model.lastMoved != null && model.sinceLastMoved < Constants.BOARD_MOVE_ANIMATE_TIME;
    }
    
    public void renderPiece(GameContainer container, Graphics g, Piece p){
        if (p.equals(model.lastMoved) && isAnimatingMove()){
            float frac = 1f * model.sinceLastMoved / Constants.BOARD_MOVE_ANIMATE_TIME;
            int x = Math.round(getPixelX(model.lastMovedPosition.x, p.getSprite().getWidth(), .5f) * (1f - frac)
                    + getPixelX(p.getPosition().x, p.getSprite().getWidth(), .5f) * frac);
            int y = Math.round(getPixelY(model.lastMovedPosition.y, p.getSprite().getHeight(), 1f) * (1f - frac)
                    + getPixelY(p.getPosition().y, p.getSprite().getHeight(), 1f) * frac);
            setDisplayPoint(container,
                    x + (p.getSprite().getWidth() - container.getWidth()) / 2,
                    y + (p.getSprite().getHeight()- container.getHeight()) / 2);
            g.drawImage(p.getSprite(), x - displayX, y - displayY);
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
        update(container, delta);
    }
}
