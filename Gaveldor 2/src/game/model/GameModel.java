package game.model;

import game.model.Action.AttackAction;
import game.model.Action.FaceAction;
import game.model.Action.ForfeitAction;
import game.model.Action.MakeMinigameMoveAction;
import game.model.Action.MoveAction;
import game.model.Action.TurnEndAction;
import game.model.Piece.TurnState;
import game.run.GameException;

import java.util.Set;

public class GameModel {

    // TODO

    private final Player player1, player2;
    private boolean player1IsCurrent = true;

    public final Map map;
    private Set<Piece> pieces;

    public static enum GameState {
        SETTING_UP, PLAYING_BOARD, PLAYING_MINIGAME, WON, DISCONNECTED;
    }

    public GameState gameState = GameState.SETTING_UP;
    
    private MinigameModel minigame = null;

    public GameModel(String name) throws GameException {
        player1 = new Player(1);
        player2 = new Player(2);

        map = Map.loadMap(name);
    }

    public void setup() {
        pieces = map.createPieces(player1, player2);
    }

    public Player getCurrentPlayer() {
        return player1IsCurrent ? player1 : player2;
    }

    public Player getOtherPlayer() {
        return player1IsCurrent ? player2 : player1;
    }

    private void switchCurrentAndOtherPlayers() {
        player1IsCurrent ^= true;
    }

    private void endTurn() {
        for (Piece p : pieces) {
            if (p.owner.equals(getCurrentPlayer())) {
                p.turnState = TurnState.MOVING;
            }
        }
        switchCurrentAndOtherPlayers();
        // TODO
    }

    public boolean isValidPosition(Point p) {
        if (p.x < 0 || p.x >= map.width) {
            return false;
        }
        if (p.y < 0 || p.y >= map.height) {
            return false;
        }
        return true;
    }

    public Set<Piece> getPieces() {
        return pieces;
    }
    
    public MinigameModel getMinigame(){
        return minigame;
    }

    public Piece getPieceByPosition(Point p) {
        if (!isValidPosition(p)) {
            return null;
        }
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(p)) {
                return piece;
            }
        }
        return null;
    }

    public Piece getPieceByID(int id) {
        for (Piece piece : pieces) {
            if (piece.id == id) {
                return piece;
            }
        }
        return null;
    }

    public boolean hasAnyPieces(Player player) {
        for (Piece p : pieces) {
            if (p.owner.equals(player)) {
                return true;
            }
        }
        return false;
    }

    public void applyAction(Action action) {
        System.out.println(action.type);
        Piece piece;
        switch (action.type) {
        case GAME_START:
            setup();
            gameState = GameState.PLAYING_BOARD;
            break;
        case DISCONNECT:
            gameState = GameState.DISCONNECTED;
            break;
        case FORFEIT:
            ForfeitAction forfeitPacket = (ForfeitAction) action;
            player1IsCurrent = forfeitPacket.playerID != player1.id;
            gameState = GameState.WON;
            break;
        case MOVE:
            MoveAction movePacket = (MoveAction) action;
            piece = getPieceByID(movePacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.MOVING;
            piece.setPosition(movePacket.destination);
            piece.turnState = TurnState.TURNING;
            break;
        case FACE:
            FaceAction facePacket = (FaceAction) action;
            piece = getPieceByID(facePacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.TURNING;
            piece.setDirection(facePacket.direction);
            piece.turnState = TurnState.ATTACKING;
            break;
        case ATTACK:
            AttackAction attackPacket = (AttackAction) action;
            piece = getPieceByID(attackPacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.ATTACKING;
            Piece target = getPieceByID(attackPacket.targetID);
            assert target != null;
            assert !piece.owner.equals(target.owner);
            gameState = GameState.PLAYING_MINIGAME;
            minigame = new MinigameModel(piece, target);
            break;
        case MAKE_MINIGAME_MOVE:
            MakeMinigameMoveAction mmmPacket = (MakeMinigameMoveAction)action;
            Player player = mmmPacket.playerID == player1.id ? player1 : player2;
            if (player.equals(getCurrentPlayer())){
                assert player.equals(minigame.attackingPiece.owner);
                assert minigame.attackingMove == null;
                minigame.attackingMove = mmmPacket.move;
            } else{
                assert player.equals(minigame.defendingPiece.owner);
                assert minigame.defendingMove == null;
                minigame.defendingMove = mmmPacket.move;
            }
            if (minigame.attackingMove != null && minigame.defendingMove != null){
                if (minigame.attackingMove == MinigameModel.Move.NONE){
                    //TODO
                } else if (minigame.attackingMove == minigame.defendingMove){
                    //TODO
                } else{
                    minigame.attackingPiece.attack(minigame.defendingPiece);
                    if (!minigame.defendingPiece.isAlive()) {
                        pieces.remove(minigame.defendingPiece);
                    }
                }
                minigame.attackingPiece.turnState = TurnState.DONE;
                gameState = GameState.PLAYING_BOARD;
                if (!hasAnyPieces(minigame.defendingPiece.owner)) {
                    player1IsCurrent = !minigame.defendingPiece.owner.equals(player1);
                    gameState = GameState.WON;
                }
                minigame = null;
            }
            break;
        case TURN_END:
            TurnEndAction turnEndPacket = (TurnEndAction) action;
            assert (getCurrentPlayer().id == turnEndPacket.playerID);
            endTurn();
            break;
        default:
            throw new RuntimeException();
        }
    }
    
    public void applyDelta(int delta){
        //TODO: time-dependent game logic goes here (e.g. minigame timing)
        if (gameState == GameState.PLAYING_MINIGAME){
            minigame.moveTime += delta;
        }
    }
}
