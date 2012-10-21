package game.model;

import game.model.Action.AttackAction;
import game.model.Action.DisconnectAction;
import game.model.Action.ForfeitAction;
import game.model.Action.GameStartAction;
import game.model.Action.MoveAction;
import game.model.Action.TurnEndAction;

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
        switch(action.type) {
        case ATTACK:
            AttackAction attackPacket = (AttackAction) action;
            
        case DISCONNECT:
            DisconnectAction disconnectPacket = (DisconnectAction) action;
           
        case FORFEIT:
            ForfeitAction forfeitPacket = (ForfeitAction) action;
            
        case GAME_START:
            GameStartAction gameStartPacket = (GameStartAction) action;
            
        case MOVE:
            MoveAction movePacket = (MoveAction) action;
            
        case TURN_END:
            TurnEndAction turnEndPacket = (TurnEndAction) action;
            
        default:
            DisconnectAction defaultPacket  = (DisconnectAction) action;
            
        }
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
