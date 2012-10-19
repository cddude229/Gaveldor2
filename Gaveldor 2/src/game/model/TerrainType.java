package game.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public final char repChar;
    
    private TerrainType(char repChar){
        this.repChar = repChar;
    }
    
    public static TerrainType getByRepChar(char repChar){
        return byRepChar.get(repChar);
    }
    
    
    public static TerrainType[][] loadMap(String fileName) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        try{
            List<TerrainType[]> rows = new ArrayList<TerrainType[]>();
            String rowLine;
            int width = -1;
            int j = 0;
            while ((rowLine = reader.readLine()) != null){
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
