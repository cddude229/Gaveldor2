package game.model;

import java.io.Serializable;

public abstract class Action implements Serializable {

    public static final long serialVersionUID = 1L;
    public enum Type {
    	FORFEIT,MOVE,ATTACK,RESPONSE,TURN_END,HEART_BEAT,GAME_START,DISCONNECT
    	};
    	
    public enum Move {ROCK,PAPER,SCISSORS};
    public Type type;
    
    
    public class ForfeitAction extends Action {
    	
        private static final long serialVersionUID = -1991155911677095030L;

        public ForfeitAction() {
    		this.type = Type.FORFEIT;
    	}
    }
    
    public class GameStartAction extends Action {
    	
        private static final long serialVersionUID = 6305597468815847402L;

        public GameStartAction() {
    		this.type = Type.GAME_START;
    	}
    }
    
    public class DisconnectAction extends Action {
    	
        private static final long serialVersionUID = -5895590625354734189L;

        public DisconnectAction() {
    		this.type = Type.DISCONNECT;
    	}
    }
    
    public class HeartBeatAction extends Action {
    	
        private static final long serialVersionUID = 989976207306981770L;

        public HeartBeatAction() {
    		this.type = Type.HEART_BEAT;
    	}
    }
    
    public class ResponseAction extends Action {

        private static final long serialVersionUID = 9022778930576890264L;
    	public Move move;
    	
    	public ResponseAction(Move move) {
    		this.move = move;
    	}
    }

    public class AttackAction extends Action {

        private static final long serialVersionUID = 7192637502453282800L;
        public final int[] sourceLocation;
    	public final int[] attackLocation;
    	
    	public AttackAction(int[] source, int[] attack) {
    		this.sourceLocation = source;
    		this.attackLocation = attack;
    		this.type = Type.ATTACK;
    	}
    }
    
    public class MoveAction extends Action {
    	
        private static final long serialVersionUID = 8582750212791110715L;
        public final int unitID;
    	public final int[] destination;
    	public final int rotation;
    	
    	public MoveAction(int ID, int[] destination, int rotation) {
    		this.unitID = ID;
    		this.destination = destination;
    		this.type = Type.MOVE;
    		this.rotation = rotation;
    	}
    }
    
}
