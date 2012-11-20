package game.model;

import game.model.Action.BoardMoveAction;
import game.model.Action.ForfeitAction;
import game.model.Action.MinigameMoveAction;
import game.model.Action.TurnEndAction;
import game.model.Piece.TurnState;
import game.run.GameException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameModel {

    private final Player[] players;
    private int currentPlayerIndex = 0;

    public final Map map;
    private Set<Piece> pieces;

    public static enum GameState {
        SETTING_UP, PLAYING_BOARD, PLAYING_MINIGAME, WON, DISCONNECTED;
        
        public int getPCStateID(){
            return ordinal();
        }
    }

    public GameState gameState = GameState.SETTING_UP;
    
    private MinigameModel minigame;
    public GameModel(String name) throws GameException {
        players = new Player[]{new Player(1), new Player(2)};

        map = Map.loadMap(name);
    }
    
    public Piece lastMoved;
    public Point lastMovedPosition;
    public int lastMovedDirection;
    public long sinceLastMoved;

    public void setup() {
        pieces = map.createPieces(players);
        currentPlayerIndex = 0;
        lastMoved = null;
        minigame = null;
    }

    public Player getPlayer1(){
        return players[0];
    }
    
    public Player getPlayer2(){
        return players[1];
    }
    
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }
    
    public void setCurrentPlayer(int playerID){
        for (int i = 0; i < players.length; i++){
            if (players[i].id == playerID){
                currentPlayerIndex = i;
                return;
            }
        }
        throw new IllegalArgumentException("The given player is not one of this game's players");
    }
    
    public void setCurrentPlayer(Player player){
        setCurrentPlayer(player.id);
    }

    public Player getOtherPlayer() {
        return players[1 - currentPlayerIndex];
    }

    private void switchCurrentAndOtherPlayers() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }

    private void endTurn() {
        for (Piece p : pieces) {
            if (p.owner.equals(getCurrentPlayer())) {
                p.turnState = TurnState.MOVING;
            }
        }
        lastMoved = null;
        switchCurrentAndOtherPlayers();
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
    
    public boolean isValidMove(Piece unit, Point p){
        return isValidMove(unit, p, unit.getPosition(), true);
    }
    public boolean isValidMove(Piece unit, Point p, boolean blocking){
        return isValidMove(unit, p, unit.getPosition(), blocking);
    }
    public boolean isValidMove(Piece unit, Point p, Point currentPoint, boolean blocking){
        return findValidMoves(unit, currentPoint, blocking).containsKey(p);
    }

    /**
     * Find valid moves for given piece at current time
     * @param unit
     * @return
     */
    public HashMap<Point, List<Point>> findValidMoves(Piece unit){
        return findValidMoves(unit, true);
    }
    
    /**
     * Find valid moves for given piece
     * @param unit The unit in question
     * @param blocking If true, filter out points with opponent in them
     * @return
     */
    public HashMap<Point, List<Point>> findValidMoves(Piece unit, boolean blocking){
        return findValidMoves(unit, unit.getPosition(), blocking);
    }
    
    /**
     * Find valid moves for given piece at point p
     * @param unit The unit in question
     * @param p Point we're currently checking from
     * @param blocking If true, filter out points with opponent in them
     * @return
     */
    public HashMap<Point, List<Point>> findValidMoves(Piece unit, Point p, boolean blocking){
        HashMap<Point, List<Point>> returnMap = findValidMoves(unit.defaultMoveRange(), unit, p, blocking);
        
        // Apply our filters
        
        // First, remove any spots that contain units besides ourself
        Set<Point> points = returnMap.keySet();
        Set<Point> removal = new HashSet<Point>();
        for(Point examiningPoint : points){
            Piece unitAtPoint = getPieceByPosition(examiningPoint);
            if(unitAtPoint != null && unitAtPoint.equals(unit) == false){
                removal.add(examiningPoint);
            }
        }
        
        points.removeAll(removal);
        
        return returnMap;
    }
    
    /**
     * HELPER: Find valid moves recursively for unit at point p with distance dist left
     * @param dist The remaining distance to check
     * @param unit The currently selected unit
     * @param p The point we're recursing from
     * @param blocking If true, filter out points with opponent in them
     * @return
     */
    private HashMap<Point, List<Point>> findValidMoves(int dist, Piece unit, Point p, boolean blocking){
        HashMap<Point, List<Point>> returnMap = new HashMap<Point, List<Point>>();
        Piece unitAtPoint = getPieceByPosition(p);
        TerrainType terrain = map.getTerrain(p);
        
        // Can we enter the terrain? If not, skip
        // Is this spot inhabited by a unit on the other team? (do we care?)
        if(terrain == null || terrain.enterable(unit)){
            if(blocking == false || unitAtPoint == null || unit.owner.equals(unitAtPoint.owner)){
                // We can move through our own pieces (or just don't care)
                List<Point> path = new ArrayList<Point>();
                path.add(p);
                returnMap.put(p, path);
            }
        }
        
        // Check case where we don't need to recurse
        if(dist <= 0){
            return returnMap;
        }
        
        // Ok, now case of distance remaining
        Point[] points = Piece.getPointsFromPoint(p, 1);
        for(Point recursePoint : points){
            if(isValidPosition(recursePoint) == false) continue; // Only scan valid spots
            
            // Load our points to check
            HashMap<Point, List<Point>> checkPoints = findValidMoves(dist-1, unit, recursePoint, blocking);
            
            // Iterate over
            for(Point toMerge : checkPoints.keySet()){
                if(returnMap.containsKey(toMerge)) continue; // Don't merge if we already have a shorter path (BFS)
                
                List<Point> path = checkPoints.get(toMerge);
                path.add(0, p); // Prepend
                
                returnMap.put(toMerge, path);
            }
        }
        
        // golden
        return returnMap;
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
            setCurrentPlayer(forfeitPacket.playerID);
            setCurrentPlayer(getOtherPlayer());
            gameState = GameState.WON;
            break;
        case BOARD_MOVE:
            BoardMoveAction movePacket = (BoardMoveAction) action;
            piece = getPieceByID(movePacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.MOVING;
            lastMoved = piece;
            lastMovedPosition = piece.getPosition();
            System.out.println(lastMovedPosition);
            lastMovedDirection = piece.getDirection();
            sinceLastMoved = 0;
            piece.setPosition(movePacket.destination);
            piece.setDirection(movePacket.direction);
            if (movePacket.targetID == -1){
                piece.turnState = TurnState.DONE;
            } else{
                Piece target = getPieceByID(movePacket.targetID);
                assert target != null;
                assert !piece.owner.equals(target.owner);
                minigame = new MinigameModel(piece, target);
            }
            break;
        case MINIGAME_START:
            assert minigame != null;
            gameState = GameState.PLAYING_MINIGAME;
            break;
        case MINIGAME_MOVE:
            MinigameMoveAction mmmPacket = (MinigameMoveAction)action;
            Player player = mmmPacket.playerID == players[0].id ? players[0] : players[1];
            if (player.equals(getCurrentPlayer())){
                assert player.equals(minigame.attackingPiece.owner);
                assert minigame.attackingMove == null;
                minigame.attackingMove = mmmPacket.move;
            } else{
                assert player.equals(minigame.defendingPiece.owner);
                assert minigame.defendingMove == null;
                minigame.defendingMove = mmmPacket.move;
            }
            break;
        case MINIGAME_END:
            assert minigame.hasBothMoves();
            if (minigame.isSuccessfulAttack()){
                minigame.attackingPiece.attack(minigame.defendingPiece);
                if (!minigame.defendingPiece.isAlive()) {
                    pieces.remove(minigame.defendingPiece);
                }
            }
            minigame.attackingPiece.turnState = TurnState.DONE;
            gameState = GameState.PLAYING_BOARD;
            if (!hasAnyPieces(minigame.defendingPiece.owner)) {
                setCurrentPlayer(minigame.defendingPiece.owner);
                setCurrentPlayer(getOtherPlayer());
                gameState = GameState.WON;
            }
            minigame = null;
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
        switch (gameState){
        case PLAYING_BOARD:
            sinceLastMoved += delta;
            break;
        case PLAYING_MINIGAME:
            if (minigame.sinceMoveTimeStart == 0){ // a temp fix for the minigame loading issue
                minigame.sinceMoveTimeStart = 1;
            } else{
                minigame.sinceMoveTimeStart += delta;
            }
            if (minigame.hasBothMoves()){
                minigame.sinceHasBothMoves += delta;
            }
            break;
        default:
            break;
        }
    }
}
