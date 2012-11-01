package de.htwg_konstanz.rmi.hp.socket;

import java.net.Socket;

public class FoundSocket {
	
	private Socket foundSocket;
	

	public synchronized void setFoundSocket(Socket foundSocket) {
		if(this.foundSocket == null) {
			this.foundSocket = foundSocket;
		}
	}

	public Socket getFoundSocket() {
		return foundSocket;
	}
}
