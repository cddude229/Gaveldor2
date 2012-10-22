package util;

/*
 * Global constants file
 */
public class Constants {
    
    public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    public static final boolean WINDOW_FULLSCREEN = false;
    
    // the actual size of the tiles in-file
    public static final int TILE_WIDTH = 250, TILE_HEIGHT = 217;
    // the perspective adjustment, given the natural height-to-width ratio of a regular hexagon
    public static final double TILE_PERSPECTIVE_RATIO = (TILE_HEIGHT / Math.sqrt(3)) / (TILE_WIDTH / 2);
    // the size to be used to get proper tile placement
    public static final int TILE_WIDTH_SPACING = (int)(TILE_WIDTH * 3 / 4);
    public static final int  TILE_HEIGHT_SPACING = (int) (TILE_HEIGHT * TILE_PERSPECTIVE_RATIO)/ 2;
    
    public static final int REMOTE_CONNECTION_PORT = 400;
    

    // Action.java
    
    // Constants.java
    
    // GameModel.java
    
    // Piece.java
    public static final int INFANTRY_ATTACK_POWER   = 1;
    public static final int INFANTRY_ATTACK_RANGE   = 1;
    public static final int INFANTRY_MOVE_RANGE     = 1;
    public static final int INFANTRY_HEALTH_POINTS  = 3;
    
    public static final int ARCHER_ATTACK_POWER     = 1;
    public static final int ARCHER_ATTACK_RANGE     = 2;
    public static final int ARCHER_MOVE_RANGE       = 1;
    public static final int ARCHER_HEALTH_POINTS    = 2;
    
    public static final int CAVALRY_ATTACK_POWER    = 1;
    public static final int CAVALRY_ATTACK_RANGE    = 1;
    public static final int CAVALRY_MOVE_RANGE      = 2;
    public static final int CAVALRY_HEALTH_POINTS   = 4;
    
    // Player.java
    
}
