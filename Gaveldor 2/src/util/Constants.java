package util;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.Effect;

/*
 * Global constants file
 */
public class Constants {
    public static final int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;
    public static final int BOARD_SIDEBAR_WIDTH = 200;
    
    // the actual size of the tiles in-file
    public static final int TILE_WIDTH = 200, TILE_HEIGHT = 150;
    // the size to be used to get proper tile placement
    public static final int TILE_WIDTH_SPACING  = (int) (TILE_WIDTH / 2);
    public static final int TILE_HEIGHT_SPACING = (int)(TILE_HEIGHT * 3 / 4);
    
    public static final int REMOTE_CONNECTION_PORT = 1400;
    public static final String MATCHMAKING_SERVER_IP = "18.189.43.23";
    public static final int SERVER_PORT = 1600;

    // This is just until map selection is online
    public static final String defaultMap = "";
    
    //Music Volume
    public static final float MENU_VOLUME = 0.4f;
    public static final float BATTLE_START_VOLUME = 0.01f;
    public static final float BATTLE_END_VOLUME = 0.25f;
    public static final int BATTLE_FADE_DURATION = 10;
    
    // Action.java
    
    // Constants.java
    
    // GameModel.java
    
    // Piece.java
    public static final int INFANTRY_ATTACK_POWER   = 1;
    public static final int INFANTRY_ATTACK_RANGE   = 1;
    public static final int INFANTRY_MOVE_RANGE     = 2;
    public static final int INFANTRY_HEALTH_POINTS  = 3;
    
    public static final int ARCHER_ATTACK_POWER     = 1;
    public static final int ARCHER_ATTACK_RANGE     = 2;
    public static final int ARCHER_MOVE_RANGE       = 2;
    public static final int ARCHER_HEALTH_POINTS    = 2;
    
    public static final int CAVALRY_ATTACK_POWER    = 1;
    public static final int CAVALRY_ATTACK_RANGE    = 1;
    public static final int CAVALRY_MOVE_RANGE      = 3;
    public static final int CAVALRY_HEALTH_POINTS   = 4;
    
    // Player.java
    
    @SuppressWarnings("unchecked")
    public static UnicodeFont loadFont(String name, int style, int size, Color color){
        try{
            UnicodeFont font = new UnicodeFont(new Font(name, style, size));
            font.addAsciiGlyphs();
            ((List<Effect>) font.getEffects()).add(new ColorEffect(color));
            font.loadGlyphs();
            return font;
        } catch (SlickException e){
            throw new RuntimeException(e);
        }
    }
    
    //all times in milliseconds
    public static final long MINIGAME_MOVE_TIME = 5000L;
    public static final long MINIGAME_WAIT_TIME = 1500L;
    public static final long BOARD_MOVE_ANIMATE_TIME = 500L;
    
    public static final UnicodeFont PRIMARY_FONT = loadFont("Arial Monospaced", Font.PLAIN, 40, Color.WHITE);
    public static final UnicodeFont TUTORIAL_FONT = loadFont("Arial Monospaced", Font.PLAIN, 24, Color.WHITE);
    
    public static final ControlScheme
            PLAYER_1_CONTROLS = new ControlScheme("A", "S", "D"),
            PLAYER_2_CONTROLS = new ControlScheme("J", "K", "L");
    
    // PlayBoardState
    // Tutorial strings

    public static final String NONE_SELECTED =  "Click a unit\n" +
    		                                    "  to move\n\n"+
                                                "Or \"End Turn\"";
   /* public static final String MOVING = "Click to move\n" +
                                        "Click to face\n" +
                                        "Click where to attack\n" +
                                        "     or\n" +
                                        "Click the piece\n\n";
    */
    public static final String MOVING = "Click spot to\n" +
    		                            "   move to";
    public static final String FACING = "Click space\n" +
    		                            "  to face";
    public static final String ATTACK = "  Click unit\n" +
                                        "  to attack\n" +
                                        "     or\n" +
                                        "Click the piece\n";
    public static final String DONE = "Hit \"End Turn\"";
    
    
    
    // FEATURE FLAGS
    public static final boolean ARCHER_2X_VS_CAVALRY = false;
    
    public static final boolean PLAYER2_ORANGE_SIDEBAR = true;
    
    public static final boolean SHOW_ATTACK_WHILE_MOVING = true;
    
    public static final boolean TURN_TIME_LIMIT_ON = false;
    public static final long TURN_TIME_LIMIT_PER_PIECE = 10000L;
    
    public static final long ATTACK_DISPLAY_TIME = 2000L;
    public static final int ATTACK_DISPLAY_FLOAT_MIN_DIST = 50;
    public static final int ATTACK_DISPLAY_FLOAT_MAX_DIST = 100;
}
