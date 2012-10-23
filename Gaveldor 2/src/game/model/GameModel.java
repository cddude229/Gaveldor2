package game.model;

import game.model.Action.AttackAction;
import game.model.Action.DisconnectAction;
import game.model.Action.FaceAction;
import game.model.Action.ForfeitAction;
import game.model.Action.GameStartAction;
import game.model.Action.MoveAction;
import game.model.Action.TurnEndAction;
import game.model.Piece.TurnState;
import game.run.GameException;

import java.util.Set;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import util.Constants;


public class GameModel {
    
    //TODO
    
    private final Player player1, player2;
    private boolean player1IsCurrent = true;
    
    public final Map map;
    private final Set<Piece> pieces;
    
    public GameModel(String name) throws GameException{
        player1 = new Player(1);
        player2 = new Player(2);
        
        map = Map.loadMap(name);
        
        pieces = map.createPieces(player1, player2);
        
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
        for (Piece p : pieces){
            if (p.owner.equals(getCurrentPlayer())){
                p.turnState = TurnState.MOVING;
            }
        }
        switchCurrentAndOtherPlayers();
        //TODO
    }
    
    public boolean isValidPosition(Point p){
        if (p.x < 0 || p.x >= map.width){
            return false;
        }
        if (p.y < 0 || p.y >= map.height){
            return false;
        }
        return true;
    }
    
    public Piece getPieceByPosition(Point p){
        if (!isValidPosition(p)){
            return null;
        }
        for (Piece piece : pieces){
            if (piece.getPosition().equals(p)){
                return piece;
            }
        }
        return null;
    }
    
    public void applyAction(Action action){
        System.out.println(action.type);
        switch(action.type) {
        case ATTACK:
            AttackAction attackPacket = (AttackAction) action;
            //TODO
            break;
        case DISCONNECT:
            DisconnectAction disconnectPacket = (DisconnectAction) action;
            //TODO
           break;
        case FORFEIT:
            ForfeitAction forfeitPacket = (ForfeitAction) action;
            //TODO
            break;
        case GAME_START:
            GameStartAction gameStartPacket = (GameStartAction) action;
            //TODO
            break;
        case MOVE:
            MoveAction movePacket = (MoveAction) action;
            Piece piece = getPieceByPosition(movePacket.source);
            assert piece.owner.id == movePacket.player;
            assert piece.turnState == TurnState.MOVING;
            piece.setPosition(movePacket.destination);
            piece.turnState = TurnState.FACING;
            //TODO
            break;
        case FACE:
            FaceAction facePacket = (FaceAction) action;
            piece = getPieceByPosition(facePacket.source);
            assert piece.owner.id == facePacket.player;
            assert piece.turnState == TurnState.FACING;
            piece.setDirection(facePacket.direction);
            piece.turnState = TurnState.ATTACKING;
            break;
        case TURN_END:
            TurnEndAction turnEndPacket = (TurnEndAction) action;
            assert (getCurrentPlayer().id == turnEndPacket.player);
            endTurn();
            break;
        default:
            DisconnectAction defaultPacket  = (DisconnectAction) action; //why?
            //TODO
            break;
        }
    }
    
    public void renderBoard(Graphics g, int offsetX, int offsetY){
        for (int i = 0; i < map.width; i++){
            for (int j = i % 2; j < map.height; j += 2){
                TerrainType terrain = map.getTerrain(i, j);
                g.drawImage(terrain.tile, i * Constants.TILE_WIDTH_SPACING + offsetX, j * Constants.TILE_HEIGHT_SPACING + offsetY);
            }
        }
        for (Piece p : pieces){
            Image sprite = p.getSprite();
            int x = p.getPosition().x * Constants.TILE_WIDTH_SPACING + (Constants.TILE_WIDTH - sprite.getWidth()) / 2 + offsetX;
            int y = p.getPosition().y * Constants.TILE_HEIGHT_SPACING + (Constants.TILE_HEIGHT - sprite.getHeight()) + offsetY;
            g.drawImage(p.getSprite(), x, y);
        }
    }
}
