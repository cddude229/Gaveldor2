package game.model;

import game.model.Action.BoardMoveAction;
import game.model.Action.ForfeitAction;
import game.model.Action.GameStartAction;
import game.model.Action.TurnEndAction;
import game.model.Piece.TurnState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

import util.Resources;

public class GameModel {

    private final Player[] players;
    private int currentPlayerIndex = 0;

    public Map map;
    private Set<Piece> pieces;

    public static enum GameState {
        SETTING_UP, PLAYING_BOARD, WON, DISCONNECTED;
        
        public int getPCStateID(){
            return ordinal();
        }
    }
    

    public static enum AttackResult{
        STRIKE, BLOCKED, CRITICAL;
        
        public Image image;
        public Sound sound;
        
        public static void initAssets(){
            STRIKE.image = Resources.getImage("/assets/graphics/ui/strike_markers/strike.png");
            BLOCKED.image = Resources.getImage("/assets/graphics/ui/strike_markers/blocked.png");
            CRITICAL.image = Resources.getImage("/assets/graphics/ui/strike_markers/critical.png");
        
            //TODO: initialize attack result sounds
            CRITICAL.sound = Resources.getSound("/assets/audio/effects/sword_crit.ogg");
            BLOCKED.sound = Resources.getSound("/assets/audio/effects/sword_block2.ogg");
            STRIKE.sound = Resources.getSound("/assets/audio/effects/sword_hit.ogg");
            
        }
    }

    public GameState gameState = GameState.SETTING_UP;
    
    public GameModel(){
        players = new Player[]{new Player(1), new Player(2)};
    }
    
    public long sinceTurnStart;
    
    public Piece lastMoved;
    public boolean lastMovedJustHappened;
    public Point lastMovedPosition;
    public int lastMovedDirection;
    public AttackResult lastMovedAttackResult;
    public long sinceLastMoved;

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
    
    public Player getOtherPlayer(Player player){
        if (player.equals(players[0])){
            return players[1];
        } else if (player.equals(players[1])){
            return players[0];
        } else{
            throw new RuntimeException();
        }
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
        lastMovedJustHappened = false;
        sinceTurnStart = 0;
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
            } else {
                return returnMap;
            }
        } else {
            return returnMap;
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
                List<Point> path = checkPoints.get(toMerge);
                path.add(0, p); // Prepend

                if(returnMap.containsKey(toMerge)){
                    // We need to make sure the new path is shorter
                    if(returnMap.get(toMerge).size() <= path.size()){
                        continue; // Don't merge if we already have a shorter path (BFS)
                    }
                }
                
                returnMap.put(toMerge, path);
            }
        }
        
        // golden
        return returnMap;
    }
    
    public boolean isValidAttack(Piece unit, Point target){
        return isValidAttack(unit, target, unit.getPosition());
    }
    public boolean isValidAttack(Piece unit, Point target, Point currentPoint){
        return findValidAttacks(unit, currentPoint).contains(target);
    }

    /**
     * Find valid attacks for given piece at current time
     * @param unit
     * @return
     */
    public Set<Point> findValidAttacks(Piece unit){
        return findValidAttacks(unit, unit.getPosition());
    }
    
    public Set<Point> findValidAttacks(Piece unit, int dir){
        return findValidAttacks(unit, unit.getPosition(), dir);
    }
    
    /**
     * Find valid attacks for given piece at point p
     * @param unit The unit in question
     * @param p Point we're currently checking from
     * @return
     */
    public Set<Point> findValidAttacks(Piece unit, Point p){
        Set<Point> points = findValidAttacks(unit.defaultAttackRange(), unit, p);
        
        // First, remove any spots that contain units besides ourself
        Set<Point> keep = new HashSet<Point>();
        for(Point examiningPoint : points){
            Piece unitAtPoint = getPieceByPosition(examiningPoint);
            if(unitAtPoint != null && unitAtPoint.owner.equals(unit.owner) == false){
                keep.add(examiningPoint);
            }
        }
        
        return keep;
    }
    
    /**
     * HELPER: Find valid attacks recursively for unit at point p with distance dist left
     * @param dist The remaining distance to check
     * @param unit The currently selected unit
     * @param p The point we're recursing from
     * @return
     */
    private Set<Point> findValidAttacks(int dist, Piece unit, Point p){
        Set<Point> points = new HashSet<Point>();
        points.add(p);
        
        // Check case where we don't need to recurse
        if(dist <= 0){
            return points;
        }
        
        // Ok, now case of distance remaining
        Point[] checkPoints = Piece.getPointsFromPoint(p, 1);
        for(Point recursePoint : checkPoints){
            if(isValidPosition(recursePoint) == false) continue; // Only scan valid spots

            points.addAll(findValidAttacks(dist-1, unit, recursePoint));
        }

        return points;
    }
    
    /**
     * Find valid attacks for given piece at point p
     * @param unit The unit in question
     * @param p Point we're currently checking from
     * @return
     */
    public Set<Point> findValidAttacks(Piece unit, Point p, int dir){
        Set<Point> points = findValidAttacks(unit.defaultAttackRange(), unit, p, dir);
        
        // First, remove any spots that contain units besides ourself
        Set<Point> keep = new HashSet<Point>();
        for(Point examiningPoint : points){
            Piece unitAtPoint = getPieceByPosition(examiningPoint);
            if(unitAtPoint != null && unitAtPoint.owner.equals(unit.owner) == false){
                keep.add(examiningPoint);
            }
        }
        
        return keep;
    }
    
    /**
     * HELPER: Find valid attacks recursively for unit at point p with distance dist left
     * @param dist The remaining distance to check
     * @param unit The currently selected unit
     * @param p The point we're recursing from
     * @param dir The direction we're facing
     * @return
     */
    private Set<Point> findValidAttacks(int dist, Piece unit, Point p, int dir){
        Set<Point> points = new HashSet<Point>();
        points.add(p);
        
        // Check case where we don't need to recurse
        if(dist <= 0){
            return points;
        }
        
        // Ok, now case of distance remaining
        Point[] checkPoints = Piece.getPointsFromPoint(p, 1);
        checkPoints = new Point[] {
                checkPoints[dir],
                checkPoints[(dir + 1 + 6) % 6],
                checkPoints[(dir - 1 + 6) % 6]
        };
        for(Point recursePoint : checkPoints){
            if(isValidPosition(recursePoint) == false) continue; // Only scan valid spots

            points.addAll(findValidAttacks(dist-1, unit, recursePoint, dir));
        }

        return points;
    }

    public Set<Piece> getPieces() {
        return pieces;
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
    
    public int numberOfPieces(Player player) {
        int c = 0;
        for (Piece p : pieces) {
            if (p.owner.equals(player)) {
                c++;
            }
        }
        return c;
    }
    
    public void giveLoss(Player loser){
        setCurrentPlayer(loser);
        setCurrentPlayer(getOtherPlayer());
        gameState = GameState.WON;
    }
    
    public void giveLoss(int loserID){
        setCurrentPlayer(loserID);
        setCurrentPlayer(getOtherPlayer());
        gameState = GameState.WON;
    }

    public void applyAction(Action action) {
        System.out.println(action.type);
        Piece piece;
        switch (action.type) {
        case GAME_START:
            GameStartAction gameStartPacket = (GameStartAction)action;
            map = Map.loadMap(gameStartPacket.mapName);
            pieces = map.createPieces(players);
            currentPlayerIndex = 0;
            lastMoved = null;
            lastMovedJustHappened = false;
            sinceTurnStart = 0L;
            gameState = GameState.PLAYING_BOARD;
            break;
        case DISCONNECT:
            if (gameState != GameState.WON){
                gameState = GameState.DISCONNECTED;
            }
            break;
        case FORFEIT:
            ForfeitAction forfeitPacket = (ForfeitAction) action;
            giveLoss(forfeitPacket.playerID);
            break;
        case BOARD_MOVE:
            BoardMoveAction movePacket = (BoardMoveAction) action;
            piece = getPieceByID(movePacket.pieceID);
            assert piece != null;
            assert piece.turnState == TurnState.MOVING;
            lastMoved = piece;
            lastMovedJustHappened = true;
            lastMovedPosition = piece.getPosition();
            lastMovedDirection = piece.getDirection();
            sinceLastMoved = 0;
            piece.setPosition(movePacket.destination);
            piece.setDirection(movePacket.direction);
            if (movePacket.targetID == -1){
                lastMovedAttackResult = null;
            } else{
                Piece target = getPieceByID(movePacket.targetID);
                assert target != null;
                assert !piece.owner.equals(target.owner);
                AttackResult attackResult = piece.isBackAttack(target) ? AttackResult.CRITICAL : movePacket.randomAttackResult;
                lastMovedAttackResult = attackResult;
                piece.attack(target, attackResult);
                if (!target.isAlive()){
                    pieces.remove(target);
                }
                if (!hasAnyPieces(target.owner)){
                    giveLoss(target.owner);
                }
            }
            piece.turnState = TurnState.DONE;
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
            lastMovedJustHappened = false;
            sinceLastMoved += sinceLastMoved == 0 ? 1 : delta;
            sinceTurnStart += sinceTurnStart == 0 ? 1 : delta;
            break;
        default:
            break;
        }
    }
}
