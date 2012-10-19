package game.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

    OPEN_LAND('L'),
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
    
    private static Image tileset;
    
    public final char repChar;
    

    // TODO: We need a way for them to map a TerrainType to a tile.  That should be enclosed here.
    public final int tileIndex = -1;
    private Image tile = null;
    
    private TerrainType(char repChar){
        this.repChar = repChar;
    }
    
    public static TerrainType getByRepChar(char repChar){
        return byRepChar.get(repChar);
    }
    
    public static void loadTileset(String path) throws SlickException{
        tileset = Resources.getImage(path);
    }
    
    public Image getTile(){
        if (tile == null){
            tile = tileset.getSubImage(tileIndex, 0, 1, 1); //TODO: actual coordinates and dimensions
        }
        return tile;
    }
    
    public static TerrainType[][] loadMap(String fileName) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
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
                    TerrainType[] row = new TerrainType[2  * width + 1];
                    for (int i = 0; i < width; i++){
                        row[2 * i + j % 2] = TerrainType.getByRepChar(rowLine.charAt(i));
                    }
                    rows.add(row);
                }
                j++;
            }
            if (width == -1){
                throw new RuntimeException("Map file is empty");
            }
            return rows.toArray(new TerrainType[rows.size()][]);
        } finally{
            reader.close();
        }
    }
}
