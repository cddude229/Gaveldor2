package game.model;

/*
 * Global constants file
 */
public class Constants {
    
    public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    public static final boolean WINDOW_FULLSCREEN = false;
    
    // the actual size of the tiles in-file
    public static final int TILE_WIDTH = 100, TILE_HEIGHT = 80;
    // the perspective adjustment, given the natural height-to-width ratio of a regular hexagon
    public static final double TILE_PERSPECTIVE_RATIO = (TILE_HEIGHT * Math.sqrt(3) / 2) / TILE_WIDTH;
    // the size to be used to get proper tile placement
    public static final int TILE_WIDTH_SCALED = TILE_WIDTH / 2,
            TILE_HEIGHT_SCALED = (int) (TILE_HEIGHT * TILE_PERSPECTIVE_RATIO);
    

    // Action.java
    
    // Constants.java
    
    // GameModel.java
    
    // Piece.java
    
    // Player.java
    
}
