package de.htwg_konstanz.rmi.hp.socket;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.UUID;


public class HolePunchingRmiSocketFactory implements RMIClientSocketFactory, 
	RMIServerSocketFactory, Serializable {
	
	private static final long serialVersionUID = 3603126460143331294L;
	
	private UUID id;
	
	private InetSocketAddress mediatorSocketAddress;
	
	public HolePunchingRmiSocketFactory(InetSocketAddress mediatorSocketAddress) {
		id = UUID.randomUUID();
		this.mediatorSocketAddress = mediatorSocketAddress;
	}

	public Socket createSocket(String host, int port) throws IOException {
		HolePunchingSocket hpSocket = new HolePunchingSocket();
		return hpSocket.requestConnection(id, mediatorSocketAddress);
	}

	public ServerSocket createServerSocket(int port) throws IOException {
		HolePunchingServerSocket hpSocket = new HolePunchingServerSocket(port, id, mediatorSocketAddress);
		hpSocket.register();
		return hpSocket;
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
	
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(this == o) {
			return true;
		}
		if (o instanceof HolePunchingRmiSocketFactory) {
			HolePunchingRmiSocketFactory toCompare = (HolePunchingRmiSocketFactory) o;
			if(toCompare.id.equals(this.id)) {
				return true;
			}
		}
		return false;
	}
}
