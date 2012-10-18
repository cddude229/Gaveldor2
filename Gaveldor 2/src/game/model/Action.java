package game.model;

import java.io.Serializable;

public abstract class Action implements Serializable {

    private static final long serialVersionUID = 1L;
    public enum Type {
    	Forfeit,Move,Attack,Response,TurnEnd,HeartBeat
    	};
    public Type type;
    
    //TODO: add Action subclasses here
    
    public class ExampleAction extends Action {

        private static final long serialVersionUID = -1848285699363419566L;
        
        public final int someArgument;
        
        public ExampleAction(int someArgument){
            this.someArgument = someArgument;
        }
    }
    
    public class ForfeitAction extends Action {
    	
    	public ForfeitAction() {
    		this.type = Type.Forfeit;
    	}
    }
    
    public class HeartBeatAction extends Action {
    	
    	public HeartBeatAction() {
    		this.type = Type.HeartBeat;
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
    	
    	public MoveAction(int ID, int[] destination) {
    		this.unitID = ID;
    		this.destination = destination;
    		this.type = Type.Move;
    	}
    }
    
}
