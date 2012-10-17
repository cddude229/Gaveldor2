package game.model;

import java.io.Serializable;

public abstract class Action implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //TODO: add Action subclasses here
    
    public class ExampleAction extends Action{

        private static final long serialVersionUID = -1848285699363419566L;
        
        public final int someArgument;
        
        public ExampleAction(int someArgument){
            this.someArgument = someArgument;
        }
    }

}
