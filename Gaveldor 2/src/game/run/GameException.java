package game.run;


/**
 * A class for exception which relate to the functionality of the game;
 * they should always be caught near the main update loops, and used
 * to inform the UI of some error
 */
public class GameException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public GameException(String message){
        super(message);
    }
    
    public GameException(String message, Throwable cause){
        super(message, cause);
    }

}
