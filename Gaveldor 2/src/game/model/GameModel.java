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


public class GameModel {
    
    //TODO
    
    private final Player player1, player2;
    private boolean player1IsCurrent = true;
    
    public final Map map;
    private final Set<Piece> pieces;
    
    public static enum GameState{
        PLAYING,
        WON,
        DISCONNECTED,
        ;
    }
    public GameState gameState = GameState.PLAYING;
    
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
    
    public Set<Piece> getPieces(){
        return pieces;
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
    
    public Piece getPieceByID(int id){
        for (Piece piece : pieces){
            if (piece.pieceId == id){
                return piece;
            }
        }
        return null;
    }
    
    public boolean hasAnyPieces(Player player){
        for (Piece p : pieces){
            if (p.owner.equals(player)){
                return true;
            }
        }
        return false;
    }
    
    public void applyAction(Action action){
        System.out.println(action.type);
        switch(action.type) {
        case ATTACK:
            AttackAction attackPacket = (AttackAction) action;
            Piece piece = getPieceByID(attackPacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.ATTACKING;
            Piece target = getPieceByID(attackPacket.targetID);
            assert target != null;
            assert !piece.owner.equals(target.owner);
            piece.attack(target);
            if (!target.isAlive()){
                pieces.remove(target);
            }
            piece.turnState = TurnState.DONE;
            if (!hasAnyPieces(target.owner)){
                player1IsCurrent = !target.owner.equals(player1);
                gameState = GameState.WON;
            }
            break;
        case DISCONNECT:
            DisconnectAction disconnectPacket = (DisconnectAction) action;
            gameState = GameState.DISCONNECTED;
           break;
        case FORFEIT:
            ForfeitAction forfeitPacket = (ForfeitAction) action;
            player1IsCurrent = forfeitPacket.playerID != player1.id;
            gameState = GameState.WON;
            break;
        case GAME_START:
            GameStartAction gameStartPacket = (GameStartAction) action;
            //TODO
            break;
        case MOVE:
            MoveAction movePacket = (MoveAction) action;
            piece = getPieceByID(movePacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.MOVING;
            piece.setPosition(movePacket.destination);
            piece.turnState = TurnState.TURNING;
            //TODO
            break;
        case FACE:
            FaceAction facePacket = (FaceAction) action;
            piece = getPieceByID(facePacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.TURNING;
            piece.setDirection(facePacket.direction);
            piece.turnState = TurnState.ATTACKING;
            break;
        case TURN_END:
            TurnEndAction turnEndPacket = (TurnEndAction) action;
            assert (getCurrentPlayer().id == turnEndPacket.playerID);
            endTurn();
            break;
        default:
            DisconnectAction defaultPacket = (DisconnectAction) action; //why?
            //TODO
            break;
        }
    }
}
