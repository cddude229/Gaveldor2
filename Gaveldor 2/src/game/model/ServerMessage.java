package game.model;


import java.io.Serializable;

public abstract class ServerMessage implements Serializable {

    public static final long serialVersionUID = 5L;
    public enum ServerType {
    	PLAYER_SELECT;/*,HEART_BEAT,RESPONSE*/
    	}
    	
    	public final ServerType type;
    	
    	public ServerMessage(ServerType type){
    	    this.type = type;
    	}
    
    
    public static class PlayerSelectMessage extends ServerMessage {
    	
		private static final long serialVersionUID = 2873158248892424179L;
		public final boolean host;
		public final String mapName;
        
        public PlayerSelectMessage(boolean host, String mapName) {
    		super(ServerType.PLAYER_SELECT);
    		this.host = host;
    		this.mapName = mapName;
    	}
    }
}
