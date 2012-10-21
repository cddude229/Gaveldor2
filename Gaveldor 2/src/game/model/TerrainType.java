package game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    static{
        try {
            OPEN_LAND.tile = Resources.getImage("/assets/graphics/test.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }
    
    
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
    
    public static TerrainType[][] loadMap(String fileName) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(fileName)));
        try{
            List<TerrainType[]> rows = new ArrayList<TerrainType[]>();
            String rowLine;
            int width = -1;
            for (int j = 0; (rowLine = reader.readLine()) != null; j++){
                rowLine = rowLine.replaceAll("\\s", "");
                if (width == -1){
                    width = rowLine.length();
                } else{
                    if (width != rowLine.length()){
                        throw new RuntimeException("Rows do not have uniform width");
                    }
                }
                TerrainType[] row = new TerrainType[2  * width + 1];
                for (int i = 0; i < width; i++){
                    row[2 * i + j % 2] = TerrainType.getByRepChar(rowLine.charAt(i));
                }
                rows.add(row);
            }
            if (width == -1){
                throw new RuntimeException("Map file is empty");
            }
            TerrainType[][] mapFlipped = rows.toArray(new TerrainType[rows.size()][]);
            TerrainType[][] map = new TerrainType[mapFlipped[0].length][mapFlipped.length];
            for (int i = 0; i < mapFlipped[0].length; i++){
                for (int j = 0; j < mapFlipped.length; j++){
                    map[i][j] = mapFlipped[j][i];
                }
            }
            return map;
        } finally{
            reader.close();
        }
    }
}
