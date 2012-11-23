package run;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

public class Server extends Thread{
	private static Server server;
	private final static int PORT = 1400;
	private ServerSocket serverSocket;
	private final static int max_connections = 500;
	private Integer num_connections = 0;
	
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
            // Block until a client connects
            Socket firstSocket = serverSocket.accept();
            if (num_connections >= max_connections) {
            	// Too many connections. Close connection
            	firstSocket.close();
            	continue;
            }
            Socket secondSocket = serverSocket.accept();
            if (num_connections >= max_connections) {
            	// Too many connections. Close connection
            	firstSocket.close();
            	secondSocket.close();
            	continue;
            }
            RelayThread connection1 = new RelayThread(firstSocket,secondSocket,this);
            RelayThread connection2 = new RelayThread(firstSocket,secondSocket,this);
            connection1.start();
            connection2.start();
            synchronized (num_connections) { 
            	num_connections = num_connections + 2;
            }
        }
    }
	
    public void connectionClosed() {
		synchronized (num_connections) { 
			num_connections = num_connections - 2;
		}
    }
	
	

}
