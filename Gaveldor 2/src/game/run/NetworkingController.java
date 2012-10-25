package game.run;

import game.model.Action;
import game.model.Action.DisconnectAction;
import game.model.Action.GameStartAction;
import game.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;

public class NetworkingController implements Runnable{
    private ArrayDeque<Action> sendables;
	private ArrayDeque<Action> receivables;
    private Socket socket = null;
	private final int port;
	private final boolean isHosting;
	
	private GameException threadException = null;
    
	/**
	 *Constructor for non-hosting player.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
    public NetworkingController(String IP, int port) throws UnknownHostException, IOException {
        this.socket = new Socket(IP,port);
		this.port = port;
        this.sendables = new ArrayDeque<Action>();
        this.receivables = new ArrayDeque<Action>();
		this.isHosting = false;
		new Thread(this).start();
    }
	
    /**
     * Constructor for hosting player.
     */
	public NetworkingController(int port) {
        this.socket = null;
		this.port = port;
        this.sendables = new ArrayDeque<Action>();
        this.receivables = new ArrayDeque<Action>();
		this.isHosting = true;
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        ObjectInputStream in;
        ObjectOutputStream out;
        try {
            if (this.isHosting) {
        		ServerSocket serverSocket = new ServerSocket(this.port);
                this.socket = serverSocket.accept();
        		this.sendables.add(new GameStartAction(new Player(2))); //TODO: get Players from model
        		this.receivables.add(new GameStartAction(new Player(1)));
        	}
            in = new ObjectInputStream(this.socket.getInputStream());
            out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            threadException = new GameException("A connection could not be established", e);
            return;
        }
		
        try {
            while (true) {
                if (this.sendables.size() > 0) {
                    synchronized(this.sendables) {
                        Action output = this.sendables.pop();
                        System.out.println("Sending" + output);
                        out.writeObject(output);
                    }
                    out.flush();
                }
                Action line;
                if (in.available() > 0) {
                    try{
                        line = (Action) in.readObject();
                    } catch (ClassNotFoundException e){
                        throw new RuntimeException(e);
                    }
                    System.out.println("Received" + line);
                    if (line.equals(null)) {
                        break;
                    }
                    else {
                        this.receivables.add(line);
                    }
                }
            }
            int otherPlayer = 1;
            if (this.isHosting) {
                otherPlayer = 2;
            }
            this.receivables.add(new DisconnectAction(new Player(otherPlayer))); //TODO: get otherPlayer from model
            out.close();
            in.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            threadException = new GameException("The connection was lost", e);
        }
        
    }
    
    public Action getAction() {
        synchronized(this.receivables) {
            return this.receivables.poll();
        }
    }
    
    public void sendAction(Action input) {
        synchronized(this.sendables) {
            this.sendables.add(input);
        }
    }
    
    public void throwThreadExceptionIfNecessary() throws GameException{
        if (threadException != null){
            throw threadException;
        }
    }
}
