package game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import util.Resources;

/**
 * A class for the data pertaining to a map of the initial board setup,
 * distinct from GameModel's representation of a game in progress
 */
public class Map {
    
    //TODO: handle piece deployment information
    
    public final int width, height;
    
    public final TerrainType[][] terrain;
    
    private Map(int width, int height, TerrainType[][] terrain){
        this.width = width;
        this.height = height;
        this.terrain = terrain;
    }

    public static Map loadMap(String fileName) throws IOException{
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
            TerrainType[][] terrainFlipped = rows.toArray(new TerrainType[rows.size()][]);
            TerrainType[][] terrain = new TerrainType[terrainFlipped[0].length][terrainFlipped.length];
            for (int i = 0; i < terrainFlipped[0].length; i++){
                for (int j = 0; j < terrainFlipped.length; j++){
                    terrain[i][j] = terrainFlipped[j][i];
                }
            }
            return new Map(terrain.length, terrain[0].length, terrain);
        } finally{
            reader.close();
        }
    }
}
