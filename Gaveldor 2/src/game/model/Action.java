package game.model;


import java.io.Serializable;

public abstract class Action implements Serializable {

    public static final long serialVersionUID = 1L;
    public enum ActionType {
    	FORFEIT,BOARD_MOVE,TURN_END,GAME_START,DISCONNECT, MINIGAME_START, MINIGAME_MOVE, MINIGAME_END;/*,HEART_BEAT,RESPONSE*/
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

        public GameStartAction() {
    		super(ActionType.GAME_START);
    	}
    }
    
    public static class DisconnectAction extends Action {
    	
        private static final long serialVersionUID = -5895590625354734189L;

        public DisconnectAction() {
    		super(ActionType.DISCONNECT);
    	}
    }
    
/*    public static class HeartBeatAction extends Action {
    	
        private static final long serialVersionUID = 989976207306981770L;

        public HeartBeatAction(int player) {
    		this.type = Type.HEART_BEAT;
    		this.player = player;
    	}
    }
    
    public static class ResponseAction extends Action {

        private static final long serialVersionUID = 9022778930576890264L;
    	public Move move;
    	
    	public ResponseAction(Move move, int player) {
    		this.move = move;
    		this.player = player;
    	}
    } */
    
    public static class MinigameStartAction extends Action{

        private static final long serialVersionUID = 4273914302579028775L;

        public MinigameStartAction() {
            super(ActionType.MINIGAME_START);
        }
        
    }
    
    public static class MinigameMoveAction extends Action{

        private static final long serialVersionUID = 9022778930576890264L;
        
        public final int playerID;
        public final MinigameModel.Move move;
        
        public MinigameMoveAction(MinigameModel.Move move, Player player) {
            super(ActionType.MINIGAME_MOVE);
            this.move = move;
            this.playerID = player.id;
        }
    }
    
    public static class MinigameEndAction extends Action{

        private static final long serialVersionUID = -6447315522947765038L;

        public MinigameEndAction() {
            super(ActionType.MINIGAME_END);
        }
        
    }
    
    public static class BoardMoveAction extends Action {
    	
        private static final long serialVersionUID = 8582750212791110715L;
        public final int pieceID;
    	public final Point destination;
        public final int direction;
        public final int targetID;
    	
    	public BoardMoveAction(Piece piece, Point destination, int direction, Piece target) {
            super(ActionType.BOARD_MOVE);
    		this.pieceID = piece.id;
    		this.destination = destination;
    		this.direction = direction;
    		targetID = target == null ? -1 : target.id;
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
