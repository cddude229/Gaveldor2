package game.model;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Resources;

public enum TerrainType {

    FIELD('L'){
        @Override
        public boolean enterable(PieceType p) {
            return true;
        }
    },
    SWAMP('S') {
        @Override
        public boolean enterable(PieceType p) {
            return p == PieceType.INFANTRY;
        }
    },
    MOUNTAINS('M') {
        @Override
        public boolean enterable(PieceType p) {
            return false;
        }
    },
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
        this.repChar = Character.toUpperCase(repChar);
    }
    
    abstract public boolean enterable(PieceType p);
    
    public static TerrainType getByRepChar(char repChar){
        return byRepChar.get(Character.toUpperCase(repChar));
    }
    
    public static void initTiles() throws SlickException{
        FIELD.tile = Resources.getImage("/assets/graphics/terrain/field.png").getScaledCopy(.5f);
        MOUNTAINS.tile = Resources.getImage("/assets/graphics/terrain/mountains.png").getScaledCopy(.5f);
    }
}

