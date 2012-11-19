package util;

import game.model.MinigameModel;

import java.util.EnumMap;
import java.util.Map;

import org.newdawn.slick.Input;

public class ControlScheme {
    
    public final Map<MinigameModel.Move, String> keys;
    
//    public final String minigameLowKey, minigameMidKey, minigameHighKey;
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
    
    public ControlScheme(String minigameHighKey, String minigameMidKey, String minigameLowKey){
        keys = new EnumMap<MinigameModel.Move, String>(MinigameModel.Move.class);
        keys.put(MinigameModel.Move.HIGH, minigameHighKey);
        keys.put(MinigameModel.Move.MID, minigameMidKey);
        keys.put(MinigameModel.Move.LOW, minigameLowKey);
        
//        this.minigameLowKey = minigameLowKey;
//        this.minigameMidKey = minigameMidKey;
//        this.minigameHighKey = minigameHighKey;
        
        this.minigameLowMove = keyToInputCode(minigameLowKey);
        this.minigameMidMove = keyToInputCode(minigameMidKey);
        this.minigameHighMove = keyToInputCode(minigameHighKey);
    }
}
