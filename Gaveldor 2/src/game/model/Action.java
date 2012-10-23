package game.model;

import java.io.Serializable;

public abstract class Action implements Serializable {

    public static final long serialVersionUID = 1L;
    public enum Type {
    	FORFEIT,MOVE,FACE,ATTACK,TURN_END,GAME_START,DISCONNECT/*,HEART_BEAT,RESPONSE*/
    	};
    	
    public int playerID;
    public enum Move {ROCK,PAPER,SCISSORS};
    public Type type;
    
    
    public static class ForfeitAction extends Action {
    	
        private static final long serialVersionUID = -1991155911677095030L;

        public ForfeitAction(Player player) {
    		this.type = Type.FORFEIT;
    		this.playerID = player.id;
    	}
    }
    
    public static class GameStartAction extends Action {
    	
        private static final long serialVersionUID = 6305597468815847402L;

        public GameStartAction(Player player) {
    		this.type = Type.GAME_START;
    		this.playerID = player.id;
    	}
    }
    
    public static class DisconnectAction extends Action {
    	
        private static final long serialVersionUID = -5895590625354734189L;

        public DisconnectAction(Player player) {
    		this.type = Type.DISCONNECT;
    		this.playerID = player.id;
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

    public static class AttackAction extends Action {

        private static final long serialVersionUID = 7192637502453282800L;
        public final int pieceID, targetID;
    	
    	public AttackAction(Piece piece, Piece target) {
    	    pieceID = piece.pieceId;
    	    playerID = piece.owner.id;
    	    targetID = target.pieceId;
    		this.type = Type.ATTACK;
    	}
    }
    
    public static class MoveAction extends Action {
    	
        private static final long serialVersionUID = 8582750212791110715L;
        public final int pieceID;
    	public final Point destination;
    	
    	public MoveAction(Piece piece, Point destination) {
    		this.pieceID = piece.pieceId;
            this.playerID = piece.owner.id;
    		this.destination = destination;
    		this.type = Type.MOVE;
    	}
    }
    
    public static class FaceAction extends Action {
        
        private static final long serialVersionUID = 8582750212791110715L;
        public final int pieceID;
        public final int direction;
        
        public FaceAction(Piece piece, int direction) {
            this.pieceID = piece.pieceId;
            this.playerID = piece.owner.id;
            this.direction = direction;
            this.type = Type.FACE;
        }
    }
    
    public static class TurnEndAction extends Action {

        private static final long serialVersionUID = -4025822878826670623L;

        public TurnEndAction(Player player) {
            this.type = Type.TURN_END;
            this.playerID = player.id;
        }
    }
    
}
