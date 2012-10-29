package game.model;


import game.model.MinigameModel.Move;

import java.io.Serializable;

public abstract class Action implements Serializable {

    public static final long serialVersionUID = 1L;
    public enum ActionType {
    	FORFEIT,MOVE,FACE,ATTACK,TURN_END,GAME_START,DISCONNECT, MAKE_MINIGAME_MOVE;/*,HEART_BEAT,RESPONSE*/
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
    
    public static class MakeMinigameMoveAction extends Action{

        private static final long serialVersionUID = 9022778930576890264L;
        
        public final int playerID;
        public final Move move;
        
        public MakeMinigameMoveAction(Move move, Player player) {
            super(ActionType.MAKE_MINIGAME_MOVE);
            this.move = move;
            this.playerID = player.id;
        }
    }

    public static class AttackAction extends Action {

        private static final long serialVersionUID = 7192637502453282800L;
        public final int pieceID, targetID;
    	
    	public AttackAction(Piece piece, Piece target) {
            super(ActionType.ATTACK);
    	    pieceID = piece.id;
    	    targetID = target.id;
    	}
    }
    
    public static class MoveAction extends Action {
    	
        private static final long serialVersionUID = 8582750212791110715L;
        public final int pieceID;
    	public final Point destination;
    	
    	public MoveAction(Piece piece, Point destination) {
            super(ActionType.MOVE);
    		this.pieceID = piece.id;
    		this.destination = destination;
    	}
    }
    
    public static class FaceAction extends Action {
        
        private static final long serialVersionUID = 8582750212791110715L;
        public final int pieceID;
        public final int direction;
        
        public FaceAction(Piece piece, int direction) {
            super(ActionType.FACE);
            this.pieceID = piece.id;
            this.direction = direction;
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
