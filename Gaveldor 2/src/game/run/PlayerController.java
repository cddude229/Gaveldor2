package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.TerrainType;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import util.Constants;


public abstract class PlayerController {
    
    public final Player player;
    
    public final GameModel model; // for getting game state info only (no updating)
    
    public int displayX = 0, displayY = 0;
    
    public PlayerController(Player player, GameModel model){
        this.player = player;
        this.model = model;
    }
    
    public abstract Action retrieveAction();
    
    public abstract void propagateAction(Action action);
    
    public void render(Graphics g) throws SlickException{
        renderBoard(g);
        renderPieces(g);
        switch (model.gameState){
        case PLAYING:
            renderControllerPlaying(g);
            break;
        case WON:
            renderControllerWon(g);
            break;
        case DISCONNECTED:
            //TODO
            break;
        }
    }
    
    public abstract void renderControllerPlaying(Graphics g) throws SlickException;
    
    private static UnicodeFont f = Constants.loadFont("Arial Monospaced", Font.PLAIN, 40, Color.WHITE);
    public void renderControllerWon(Graphics g) throws SlickException{
        g.setFont(f);
        g.drawString("Player " + model.getCurrentPlayer().id + " Wins!", 0, 0);
    }
    
    public void renderAtPosition(Image image, Graphics g, int x, int y, float centerX, float centerY){
        g.drawImage(image, 
                x * Constants.TILE_WIDTH_SPACING + (Constants.TILE_WIDTH - image.getWidth()) * centerX - displayX,
                y * Constants.TILE_HEIGHT_SPACING + (Constants.TILE_HEIGHT - image.getHeight()) * centerY - displayY);
    }
    
    public void renderBoard(Graphics g){
        for (int i = 0; i < model.map.width; i++){
            for (int j = i % 2; j < model.map.height; j += 2){
                TerrainType terrain = model.map.getTerrain(i, j);
                renderAtPosition(terrain.tile, g, i, j, 0f, 0f);
            }
        }
    }
    
    public void renderPieces(Graphics g){
        for (Piece p : model.getPieces()){
            Image sprite = p.getSprite();
            renderAtPosition(sprite, g, p.getPosition().x, p.getPosition().y, .5f, 1f);
        }
    }
    
}
