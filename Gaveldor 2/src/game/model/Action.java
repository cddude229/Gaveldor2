package game.model;

import java.io.Serializable;

public abstract class Action implements Serializable {

    public static final long serialVersionUID = 1L;
    public enum Type {
    	FORFEIT,MOVE,ATTACK,TURN_END,GAME_START,DISCONNECT/*,HEART_BEAT,RESPONSE*/
    	};
    	
    public int player;
    public enum Move {ROCK,PAPER,SCISSORS};
    public Type type;
    
    
    public static class ForfeitAction extends Action {
    	
        private static final long serialVersionUID = -1991155911677095030L;

        public ForfeitAction(int player) {
    		this.type = Type.FORFEIT;
    		this.player = player;
    	}
    }
    
    public static class GameStartAction extends Action {
    	
        private static final long serialVersionUID = 6305597468815847402L;

        public GameStartAction(int player) {
    		this.type = Type.GAME_START;
    		this.player = player;
    	}
    }
    
    public static class DisconnectAction extends Action {
    	
        private static final long serialVersionUID = -5895590625354734189L;

        public DisconnectAction(int player) {
    		this.type = Type.DISCONNECT;
    		this.player = player;
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
        public final Point sourceLocation;
    	public final Point attackLocation;
    	
    	public AttackAction(Point source, Point attack, int player) {
    		this.sourceLocation = source;
    		this.attackLocation = attack;
    		this.type = Type.ATTACK;
    		this.player = player;
    	}
    }
    
    public static class MoveAction extends Action {
    	
        private static final long serialVersionUID = 8582750212791110715L;
        public final int unitID;
    	public final Point destination;
    	public final int rotation;
    	
    	public MoveAction(int ID, Point destination, int rotation, int player) {
    		this.unitID = ID;
    		this.destination = destination;
    		this.type = Type.MOVE;
    		this.rotation = rotation;
    		this.player = player;
    	}
    }
    
    public static class TurnEndAction extends Action {

        private static final long serialVersionUID = -4025822878826670623L;

        public TurnEndAction(int player) {
            this.type = Type.TURN_END;
            this.player = player;
        }
    }
    
}
