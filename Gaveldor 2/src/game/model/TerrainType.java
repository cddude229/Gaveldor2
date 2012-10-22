package game.model;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Resources;

public enum TerrainType {

    OPEN_LAND('L'){
    },
    FOREST('F'),
    WATER('W'),
    //TODO
    ;
    
    
    private static final Map<Character, TerrainType> byRepChar = new HashMap<Character, TerrainType>();
    static{
        for (TerrainType t : TerrainType.values()){
            byRepChar.put(t.repChar, t);
        }
    }
    
    public final char repChar;
    
    public Image tile = null;
    
    private TerrainType(char repChar){
        this.repChar = repChar;
    }
    
    public static TerrainType getByRepChar(char repChar){
        return byRepChar.get(repChar);
    }
    
    public static void initTiles() throws SlickException{
        OPEN_LAND.tile = Resources.getImage("/assets/graphics/test.png");
    }
}

