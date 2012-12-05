package game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Constants;
import util.Resources;

/**
 * A class for the data pertaining to a map of the initial board setup, distinct
 * from GameModel's representation of a game in progress
 */
public class Map {

    public final String name;

    public final int width, height;

    private final TerrainType[][] terrainMap;

    private final Set<Piece> pieces;

    private Map(String name, int width, int height, TerrainType[][] terrain, Set<Piece> pieces) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.terrainMap = terrain;
        this.pieces = pieces;
    }

    public TerrainType getTerrain(int x, int y) {
        TerrainType terrain = terrainMap[x][y];
        if (terrain == null) {
            throw new RuntimeException("An invalid map location has been accessed");
        }
        return terrain;
    }
    
    public TerrainType getTerrain(Point p){
        return getTerrain(p.x, p.y);
    }

    public int getPixelWidth() {
        return (Constants.TILE_WIDTH - Constants.TILE_WIDTH_SPACING) + Constants.TILE_WIDTH_SPACING * width;
    }

    public int getPixelHeight() {
        return (Constants.TILE_HEIGHT - Constants.TILE_HEIGHT_SPACING) + Constants.TILE_HEIGHT_SPACING * height;
    }

    public Set<Piece> createPieces(Player[] players) {
        Set<Piece> piecesNew = new HashSet<Piece>();
        for (Piece p : pieces) {
            try {
                piecesNew.add(p.getClass().getConstructor(Player.class, Point.class, int.class, int.class)
                        .newInstance(players[p.owner.id], p.getPosition(), p.id, p.getDirection()));
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        return piecesNew;
    }

    public static Map loadMap(String name){
        try {
            TerrainType[][] terrain = loadTerrain(name);
            int width = terrain.length, height = terrain[0].length;
            Set<Piece> pieces = Map.loadPieces(name, width, height);
            return new Map(name, width, height, terrain, pieces);
        } catch (IOException e) {
            throw new RuntimeException("There was an I/O error while reading the map file", e);
        }
    }

    private static TerrainType[][] loadTerrain(String name) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(name + ".map")));
        try {
            List<TerrainType[]> rows = new ArrayList<TerrainType[]>();
            String rowLine;
            int width = -1;
            for (int j = 0; (rowLine = reader.readLine()) != null; j++) {
                rowLine = rowLine.replaceAll("\\s", "");
                if (width == -1) {
                    width = rowLine.length();
                } else {
                    if (width != rowLine.length()) {
                        throw new IOException("Map file rows do not have uniform width");
                    }
                }
                TerrainType[] row = new TerrainType[2 * width];
                for (int i = 0; i < width; i++) {
                    row[2 * i + j % 2] = TerrainType.getByRepChar(rowLine.charAt(i));
                }
                rows.add(row);
            }
            if (width == -1) {
                throw new RuntimeException("Map file is empty");
            }
            TerrainType[][] terrainFlipped = rows.toArray(new TerrainType[rows.size()][]);
            TerrainType[][] terrain = new TerrainType[terrainFlipped[0].length][terrainFlipped.length];
            for (int i = 0; i < terrainFlipped[0].length; i++) {
                for (int j = 0; j < terrainFlipped.length; j++) {
                    terrain[i][j] = terrainFlipped[j][i];
                }
            }
            return terrain;
        } finally {
            reader.close();
        }
    }

    public static Set<Piece> loadPieces(String name, int mapWidth, int mapHeight) throws IOException {
        Player p1 = new Player(0), p2 = new Player(1);
        int idCounter = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(name + ".pieces")));
        try {
    
            Set<Piece> pieces = new HashSet<Piece>();
    
            String rowLine;
    
            for (int j = 0; ((rowLine = reader.readLine()) != null) && (j < mapHeight); j++) {
    
                rowLine = rowLine.replaceAll("\\s", ""); // remove spaces
    
                // make player one piece tokens into single char
                rowLine = rowLine.replaceAll("1i", "d");
                rowLine = rowLine.replaceAll("1a", "e");
                rowLine = rowLine.replaceAll("1c", "f");
    
                // same for player two
                rowLine = rowLine.replaceAll("2i", "x");
                rowLine = rowLine.replaceAll("2a", "y");
                rowLine = rowLine.replaceAll("2c", "z");
    
                // System.out.println(rowLine);
                
                for (int i = 0; /*(i < mapWidth) &&*/ (i < rowLine.length()); i++) {
                    int x = (i/2) * 2 + j % 2;
                    int y = j;
                    switch (rowLine.charAt(i)) {
    
                    case 'd':
                        // System.out.println(rowLine.charAt(i+1));
                        pieces.add(new Infantry(p1, new Point(x, y), idCounter++, rowLine.charAt(i+1)));
                        break;
                    case 'e':
                        // System.out.println(rowLine.charAt(i+1));
                        pieces.add(new Archer(p1, new Point(x, y), idCounter++, rowLine.charAt(i+1)));
                        break;
                    case 'f':
                        // System.out.println(rowLine.charAt(i+1));
                        pieces.add(new Cavalry(p1, new Point(x, y), idCounter++, rowLine.charAt(i+1)));
                        break;
                    case 'x':
                        // System.out.println(rowLine.charAt(i+1));
                        pieces.add(new Infantry(p2, new Point(x, y), idCounter++, rowLine.charAt(i+1)));
                        break;
                    case 'y':
                        // System.out.println(rowLine.charAt(i+1));
                        pieces.add(new Archer(p2, new Point(x, y), idCounter++, rowLine.charAt(i+1)));
                        break;
                    case 'z':
                        // System.out.println(rowLine.charAt(i+1));
                        pieces.add(new Cavalry(p2, new Point(x, y), idCounter++, rowLine.charAt(i+1)));
                        break;
                    }
                }
            }
            return pieces;
    
        } finally {
            reader.close();
        }
    }
}
