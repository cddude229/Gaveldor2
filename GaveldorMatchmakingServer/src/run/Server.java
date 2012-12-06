package run;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import game.model.ServerMessage.PlayerSelectMessage;

public class Server extends Thread{
	private static Server server;
	private final static int PORT = 1600;
	private ServerSocket serverSocket;
	private final static int max_connections = 500;
	private Integer num_connections = 0;
	public Socket firstSocket;
	public Socket secondSocket;
	private RelayThread firstSocketRelay;
	private RelayThread secondSocketRelay;
	
	/**
	 * Initialize a server instance on the specified port
	 * @param port
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
	}

	/**
     * Start a chat server.
     */
    public static void main(String[] args) {
    	try {
			server = new Server(PORT);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void run() {
    	try {
    		if (server != null)	// The server was initialized using the main loop
    			server.serve();
    		else
    			serve();		// The server was initialized outside of the main loop
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Listens on the specified port for connection requests
     * Spins off new connections in their own threads
     * @throws IOException
     */
	private void serve() throws IOException {
        while (true) {
        	if (this.firstSocket == null) {
	            // Block until a client connects
	            System.out.println("Waiting for a connection");
	            this.firstSocket = serverSocket.accept();
	            System.out.println("Got a connection!");
	            
	            firstSocketRelay = new RelayThread(this.firstSocket,this);
	            firstSocketRelay.start();
	            
	            if (num_connections >= max_connections) {
	            	// Too many connections. Close connection
	            	this.firstSocket.close();
	            	this.firstSocket = null;
	            	continue;
	            }
        	}
        	if (this.secondSocket == null) {
	            this.secondSocket = serverSocket.accept();
	            
	            secondSocketRelay = new RelayThread(this.secondSocket,this);
	            secondSocketRelay.start();
	            
	            if (num_connections >= max_connections) {
	            	// Too many connections. Close connection
	            	this.firstSocket.close();
	            	this.secondSocket.close();
	            	continue;
	            }
        	}
        	if (this.firstSocket != null & this.secondSocket != null) {
	            String mapName = chooseMap();

	            sendStartMessage(firstSocket,mapName,true);
	            sendStartMessage(secondSocket,mapName,false);
	            
	            firstSocketRelay.setOutputStream(secondSocket);
	            secondSocketRelay.setOutputStream(firstSocket);
	            
	            this.firstSocket = null;
	            this.secondSocket = null;
	            synchronized (num_connections) { 
	            	num_connections = num_connections + 2;
	            }
        	}
        }
    }
	
    private void sendStartMessage(Socket socket, String mapName, boolean host) {
    	PlayerSelectMessage output = new PlayerSelectMessage(host,mapName);
        ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
	        out.writeObject(output);
	        out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String chooseMap() {    
	    String[] maps = {"agincourt", "basic", "largefield", "mountain", "thermopylae"};
		return "/assets/maps/" + maps[((int)(Math.random()*5))];
	}

	public void connectionClosed() {
		synchronized (num_connections) { 
			num_connections = num_connections - 2;
		}
    }
	
	

}
