package game.run;

import game.model.Action;
import game.model.Action.DisconnectAction;
import game.model.Action.GameStartAction;

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
    public Socket socket = null;
	public int port;
	public final boolean isHosting;
    
	/**
	 *Constructor for non-hosting player.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
    public NetworkingController(String IP, int port, ArrayDeque<Action> sendDeque, ArrayDeque<Action> receiveDeque) throws UnknownHostException, IOException {
        this.socket = new Socket(IP,port);
		this.port = port;
        this.sendables = sendDeque;
        this.receivables = receiveDeque;
		this.isHosting = false;
    }
	
    /**
     * Constructor for hosting player.
     */
	public NetworkingController(int port, ArrayDeque<Action> sendDeque, ArrayDeque<Action> receiveDeque) {
        this.socket = null;
		this.port = port;
        this.sendables = sendDeque;
        this.receivables = receiveDeque;
		this.isHosting = true;
    }
    
    @Override
    public void run() {
	    if (this.isHosting) {
			ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(this.port);
                this.socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
			this.sendables.add(new GameStartAction()); // Need an instance?
		}
		
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(this.socket.getInputStream());
            out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
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
                Action line = (Action) in.readObject();
                System.out.println("Received" + line);
                if (line.equals(null)) {
                    break;
                }
                else {
                    HandleRequest(line);
                }
            }
            this.receivables.add(new DisconnectAction());
            out.close();
            in.close();
            this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
    }
    
    /**
     * Likely to be moved into the game logic, where the switch can actually handle all the types.
     */
    private void HandleRequest(Action input) {
        switch(input.type) {
        case Attack:
		    synchronized(this.sendables) {
                this.sendables.add(input.new HeartBeatAction());
			}
			synchronized(this.receivables) {
			    this.receivables.add(input);
			}
        default:
            synchronized(this.receivables) {
                this.receivables.add(input);
            }
        }
    }
}
