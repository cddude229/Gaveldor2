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
    
    public final String name;
    
    //TODO: handle piece deployment information
    
    public final int width, height;
    
    private final TerrainType[][] terrainMap;
    
    private Map(String name, int width, int height, TerrainType[][] terrain){
        this.name = name;
        this.width = width;
        this.height = height;
        this.terrainMap = terrain;
    }
    
    public TerrainType getTerrain(int x, int y){
        TerrainType terrain = terrainMap[x][y];
        if (terrain == null){
            throw new RuntimeException("An invalid map location has been accessed");
        }
        return terrain;
    }
    
    public int getPixelWidth(){
        return (Constants.TILE_WIDTH - Constants.TILE_WIDTH_SPACING) + Constants.TILE_WIDTH_SPACING * width;
    }
    
    public int getPixelHeight(){
        return (Constants.TILE_HEIGHT - Constants.TILE_HEIGHT_SPACING) + Constants.TILE_HEIGHT_SPACING * height;
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
                TerrainType[] row = new TerrainType[2  * width];
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
            return new Map(fileName, terrain.length, terrain[0].length, terrain);
        } finally{
            reader.close();
        }
    }
}
