package game.model;

import java.io.IOException;

import org.newdawn.slick.Graphics;


public class GameModel {
    
    //TODO
    
    private final Player player1, player2;
    private boolean player1IsCurrent = true;
    
    private final TerrainType[][] map;
    
    public GameModel(String mapFileName) throws IOException{
        player1 = new Player(1);
        player2 = new Player(2);
        
        map = TerrainType.loadMap(mapFileName);
    }
    
    public Player getCurrentPlayer(){
        return player1IsCurrent ? player1 : player2;
    }
    
    public Player getOtherPlayer(){
        return player1IsCurrent ? player2 : player1;
    }
    
    private void switchCurrentAndOtherPlayers(){
        player1IsCurrent ^= true;
    }
    
    public void applyAction(Action action){
        //TODO: handle various action subclasses
    }
    
    public void renderMap(Graphics g){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                TerrainType terrain = map[i][j];
                if (terrain == null){
                    continue;
                }
                g.drawImage(terrain.tile, i * Constants.TILE_WIDTH_SPACING, j * Constants.TILE_HEIGHT_SPACING);
            }
        }
    }
}
