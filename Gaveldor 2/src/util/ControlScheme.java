package util;

import org.newdawn.slick.Input;

public class ControlScheme {
    
    public final String minigameLowKey, minigameMidKey, minigameHighKey;
    public final int minigameLowMove, minigameMidMove, minigameHighMove;
    
    private static int keyToInputCode(String keyName){
        try {
            return Input.class.getField("KEY_" + keyName.toUpperCase()).getInt(null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ControlScheme(String minigameLowKey, String minigameMidKey, String minigameHighKey){
        this.minigameLowKey = minigameLowKey;
        this.minigameMidKey = minigameMidKey;
        this.minigameHighKey = minigameHighKey;
        
        this.minigameLowMove = keyToInputCode(minigameLowKey);
        this.minigameMidMove = keyToInputCode(minigameMidKey);
        this.minigameHighMove = keyToInputCode(minigameHighKey);
    }
}
