package game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    private final Set<Piece> pieces;
    
    private Map(String name, int width, int height, TerrainType[][] terrain, Set<Piece> pieces){
        this.name = name;
        this.width = width;
        this.height = height;
        this.terrainMap = terrain;
        this.pieces = pieces;
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
    
    public Set<Piece> createPieces(Player player1, Player player2){
        Set<Piece> piecesNew = new HashSet<Piece>();
        for (Piece p : pieces){
            try {
                piecesNew.add(p.getClass().getConstructor(Player.class, Point.class).newInstance(
                        p.owner.id == 1 ? player1 : player2, p.getPoint()));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return piecesNew;
    }
    
    public static Map loadMap(String name) throws IOException{
        TerrainType[][] terrain = loadTerrain(name);
        int width = terrain.length, height = terrain[0].length;
        Set<Piece> pieces = PieceLoader.loadPieces(name, width, height);
        return new Map(name, width, height, terrain, pieces);
    }

    private static TerrainType[][] loadTerrain(String name) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(name + ".map")));
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
            return terrain;
        } finally{
            reader.close();
        }
    }
}
