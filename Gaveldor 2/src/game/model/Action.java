package game.model;


import java.io.Serializable;

public abstract class Action implements Serializable {

    public static final long serialVersionUID = 1L;
    public enum ActionType {
    	FORFEIT,BOARD_MOVE,TURN_END,GAME_START,DISCONNECT, @Deprecated MINIGAME_START, @Deprecated MINIGAME_MOVE, @Deprecated MINIGAME_END;
    	}
    	
    	public final ActionType type;
    	
    	public Action(ActionType type){
    	    this.type = type;
    	}
    
    
    public static class ForfeitAction extends Action {
    	
        private static final long serialVersionUID = -1991155911677095030L;

        public final int playerID;
        
        public ForfeitAction(Player player) {
    		super(ActionType.FORFEIT);
    		this.playerID = player.id;
    	}
    }
    
    public static class GameStartAction extends Action {
    	
        private static final long serialVersionUID = 6305597468815847402L;

        public final String mapName;
        
        public GameStartAction(String mapName) {
    		super(ActionType.GAME_START);
    		this.mapName = mapName;
    	}
    }
    
    public static class DisconnectAction extends Action {
    	
        private static final long serialVersionUID = -5895590625354734189L;

        public DisconnectAction() {
    		super(ActionType.DISCONNECT);
    	}
    }
    
    public static class BoardMoveAction extends Action {
    	
        private static final long serialVersionUID = 8582750212791110715L;
        public final int pieceID;
    	public final Point destination;
        public final int direction;
        public final int targetID;
        public final GameModel.AttackResult randomAttackResult;
    	
    	public BoardMoveAction(Piece piece, Point destination, int direction, Piece target) {
            super(ActionType.BOARD_MOVE);
    		this.pieceID = piece.id;
    		this.destination = destination;
    		this.direction = direction;
    		targetID = target == null ? -1 : target.id;
    		int rand = (int)(Math.random() * 8);
    		switch (rand){
    		case 0:
    		    randomAttackResult = GameModel.AttackResult.BLOCKED;
    		    break;
    		case 7:
    		    randomAttackResult = GameModel.AttackResult.CRITICAL;
    		    break;
    		default:
    		    randomAttackResult = GameModel.AttackResult.STRIKE;
    		    break;
    		}
    	}
    }
    
    public static class TurnEndAction extends Action {

        private static final long serialVersionUID = -4025822878826670623L;

        public final int playerID;
        
        public TurnEndAction(Player player) {
            super(ActionType.TURN_END);
            this.playerID = player.id;
        }
    }
    
}
