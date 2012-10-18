package game.run;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayDeque;

import game.model.Action;

public class NetworkingController implements Runnable{
    private ArrayDeque<Action> sendables;
	private ArrayDeque<Action> receivables;
    public Socket socket = null;
	public int port;
	public final boolean isHosting;
    
    public NetworkingController(String IP, int port, ArrayDeque<Action> sendDeque, ArrayDeque<Action> receiveDeque) {
        this.socket = new Socket(IP,port);
		this.port = port;
        this.sendables = sendDeque;
        this.receivables = receiveDeque;
		this.isHosting = false;
    }
	
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
			serverSocket = new ServerSocket(this.port);
			this.socket = serverSocket.accept();
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
            out.close();
            in.close();
            this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
    }
    
    private void HandleRequest(Action input) {
        switch(input.getType()) {
        case GameStart:
		    synchronized(this.sendables) {
                this.sendables.add(/*new Action("Received" */));
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
