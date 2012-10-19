package game.model;

import java.io.Serializable;

public abstract class Action implements Serializable {

    private static final long serialVersionUID = 1L;
    public enum Type {
    	Forfeit,Move,Attack,Response,TurnEnd,HeartBeat,GameStart,Disconnect
    	};
    public Type type;
    
    //TODO: add Action subclasses here
    
    public class ExampleAction extends Action {

        private static final long serialVersionUID = -1848285699363419566L;
        
        public final int someArgument;
        
        public ExampleAction(int someArgument){
            this.someArgument = someArgument;
            this.type = Type.Forfeit;
        }
    }
    
    public class ForfeitAction extends Action {
    	
    	public ForfeitAction() {
    		this.type = Type.Forfeit;
    	}
    }
    
    public class GameStartAction extends Action {
    	
    	public GameStartAction() {
    		this.type = Type.GameStart;
    	}
    }
    
    public class DisconnectAction extends Action {
    	
    	public DisconnectAction() {
    		this.type = Type.Disconnect;
    	}
    }
    
    public class HeartBeatAction extends Action {
    	
    	public HeartBeatAction() {
    		this.type = Type.HeartBeat;
    	}
    }
    
    public class ResponseAction extends Action {
    	
    	public enum Move {Rock,Paper,Scissors};
    	public Move move;
    	
    	public ResponseAction(Move move) {
    		this.move = move;
    	}
    }

    public class AttackAction() extends Action {
    	
    	public final int[2] sourceLocation;
    	public final int[2] attackLocation;
    	
    	public AttackAction(int[] source, int[] attack) {
    		this.sourceLocation = source;
    		this.attackLocation = attack;
    		this.type = Type.Attack;
    	}
    }
    
    public class MoveAction() extends Action {
    	
    	public final int unitID;
    	public final int[2] destination;
    	public final int rotation;
    	
    	public MoveAction(int ID, int[] destination, int rotation) {
    		this.unitID = ID;
    		this.destination = destination;
    		this.type = Type.Move;
    		this.rotation = rotation;
    	}
    }
    
}
