package game.model;

import game.model.Action.AttackAction;
import game.model.Action.DisconnectAction;
import game.model.Action.ForfeitAction;
import game.model.Action.GameStartAction;
import game.model.Action.MoveAction;
import game.model.Action.TurnEndAction;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;


public class GameModel {
    
    //TODO
    
    private final Player player1, player2;
    private boolean player1IsCurrent = true;
    
    public final Map map;
    private final ArrayList<Piece> pieces;
    
    public GameModel(String name) throws IOException{
        player1 = new Player(1);
        player2 = new Player(2);
        
        map = Map.loadMap(name);
        
        pieces = PieceLoader.loadPieces(name, map.width, map.height, player1, player2);
        
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
    
    private void endTurn(){
        switchCurrentAndOtherPlayers();
        //TODO
    }
    
    public boolean isValidCoord(Point p){
        if (p.x < 0 || p.x >= map.width){
            return false;
        }
        if (p.y < 0 || p.y >= map.height){
            return false;
        }
        return true;
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
            DisconnectAction defaultPacket  = (DisconnectAction) action; //why?
            
        }
    }
    
    public void renderBoard(Graphics g, int offsetX, int offsetY){
        for (int i = 0; i < map.width; i++){
            for (int j = i % 2; j < map.height; j += 2){
                TerrainType terrain = map.getTerrain(i, j);
                g.drawImage(terrain.tile, i * Constants.TILE_WIDTH_SPACING + offsetX, j * Constants.TILE_HEIGHT_SPACING + offsetY);
            }
        }
        //TODO: render pieces
    }
}
