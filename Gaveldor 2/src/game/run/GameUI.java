package game.run;

import game.model.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;

public class GameUI {
    
    private GameContainer container;
    private StateBasedGame game;
    private int delta;
    
    private int updateCount = 0;
    
    public GameContainer getContainer(){
        return container;
    }
    
    public Input getInput(){
        return container.getInput();
    }
    
    public StateBasedGame getGame(){
        return game;
    }
    
    public int getDelta(){
        return delta;
    }
    
    public int getUpdateCount(){
        return updateCount;
    }
    
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        this.container = container;
        this.game = game;
        this.delta = delta;
        updateCount++;
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
