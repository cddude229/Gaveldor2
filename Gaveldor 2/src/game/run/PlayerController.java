package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Piece.TurnState;
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

    public final GameModel model; // for getting game state info only (no
                                  // updating)

    public int displayX = 0, displayY = 0;

    public PlayerController(Player player, GameModel model) {
        this.player = player;
        this.model = model;
    }

    public abstract Action retrieveAction();

    public abstract void propagateAction(Action action);

    public void render(Graphics g) throws SlickException {
        switch (model.gameState) {
        case SETTING_UP:
            throw new RuntimeException();
        case PLAYING_BOARD:
            renderBoard(g);
            renderPieces(g);
            renderControllerPlayingBoard(g);
            break;
        case PLAYING_MINIGAME:
            renderPlayingMinigame(g);
            break;
        case WON:
            renderBoard(g);
            renderPieces(g);
            renderControllerWon(g);
            break;
        case DISCONNECTED:
            // TODO
            break;
        }
    }

    public abstract void renderControllerPlayingBoard(Graphics g) throws SlickException;

    private static UnicodeFont f = Constants.loadFont("Arial Monospaced", Font.PLAIN, 40, Color.WHITE);

    public void renderPlayingMinigame(Graphics g){
        //TODO, obviously
        g.setFont(f);
        g.drawString("MINIGAME", 0, 0);
        g.drawString(String.valueOf(model.getMinigame().attackingMove), 0, 100);
        g.drawString(String.valueOf(model.getMinigame().defendingMove), 400, 100);
    }
    
    public void renderControllerWon(Graphics g) throws SlickException {
        g.setFont(f);
        g.drawString("Player " + model.getCurrentPlayer().id + " Wins!", 0, 0);
    }

    public void renderAtPosition(Image image, Graphics g, int x, int y, float centerX, float centerY) {
        g.drawImage(image, x * Constants.TILE_WIDTH_SPACING + (Constants.TILE_WIDTH - image.getWidth()) * centerX
                - displayX, y * Constants.TILE_HEIGHT_SPACING + (Constants.TILE_HEIGHT - image.getHeight()) * centerY
                - displayY);
    }

    public void renderBoard(Graphics g) {
        for (int j = 0; j < model.map.height; j++) {
            for (int i = j % 2; i < model.map.width; i += 2) {
                TerrainType terrain = model.map.getTerrain(i, j);
                renderAtPosition(terrain.tile, g, i, j, 0f, 0f);
            }
        }
    }

    public void renderPieces(Graphics g) {
        for (Piece p : model.getPieces()) {
            Image sprite = p.getSprite();
            if (p.turnState == TurnState.DONE){
                try {
                    sprite.setFilter(0xFFFFFFFF);
                } catch (SlickException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            renderAtPosition(sprite, g, p.getPosition().x, p.getPosition().y, .5f, 1f);
        }
    }

}
