package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.TerrainType;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;

public abstract class PlayerController extends StateBasedGame{

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

    
    public void setDisplayPoint(int pixelX, int pixelY){
        displayX = pixelX;
        displayX = Math.max(displayX, -Constants.WINDOW_WIDTH / 2);
        displayX = Math.min(displayX, model.map.getPixelWidth() - Constants.WINDOW_WIDTH / 2);
        displayY = pixelY;
        displayY = Math.max(displayY, -Constants.WINDOW_HEIGHT / 2);
        displayY = Math.min(displayY, model.map.getPixelHeight() - Constants.WINDOW_HEIGHT / 2);
    }
    
    public void setDisplayCenter(int tileX, int tileY){
        System.out.println(tileX + ", " + tileY);
        setDisplayPoint(
                tileX * Constants.TILE_WIDTH_SPACING + Constants.TILE_WIDTH / 2 - Constants.WINDOW_WIDTH / 2,
                tileY * Constants.TILE_HEIGHT_SPACING + Constants.TILE_HEIGHT / 2 - Constants.WINDOW_HEIGHT / 2);
    }
    
    
    public void renderControllerWon(Graphics g) throws SlickException {
        g.setFont(Constants.testFont);
        g.drawString("Player " + model.getCurrentPlayer().id + " Wins!", 0, 0);
    }

    public void renderAtPosition(Image image, Graphics g, int x, int y, float centerX, float centerY) {
        g.drawImage(image, x * Constants.TILE_WIDTH_SPACING + (Constants.TILE_WIDTH - image.getWidth()) * centerX
                - displayX, y * Constants.TILE_HEIGHT_SPACING + (Constants.TILE_HEIGHT - image.getHeight()) * centerY
                - displayY);
    }

    public void renderBoard(Graphics g) {
        int w = Constants.WINDOW_WIDTH / Constants.TILE_WIDTH_SPACING / 2;
        int h = (Constants.WINDOW_HEIGHT + (Constants.TILE_HEIGHT - Constants.TILE_HEIGHT_SPACING)) / Constants.TILE_HEIGHT_SPACING;
        for (int j = - h; j < model.map.height + h; j++) {
            for (int i = j % 2 - w - 2; i < model.map.width + w + 2; i += 2) {
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

    public void renderPieces(Graphics g) {
        for (Piece p : model.getPieces()) {
            Image sprite = p.getSprite();
            renderAtPosition(sprite, g, p.getPosition().x, p.getPosition().y, .5f, 1f);
        }
    }
    
    public boolean isCurrentPC(){
        return player.equals(model.getCurrentPlayer());
    }
}
