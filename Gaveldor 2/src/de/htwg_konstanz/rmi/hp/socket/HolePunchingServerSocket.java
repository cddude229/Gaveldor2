package de.htwg_konstanz.rmi.hp.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import de.htwg_konstanz.rmi.hp.mediator.StuntEndpoint;

public class HolePunchingServerSocket extends ServerSocket {

	private Socket mediatorClient = null;
	private int port;
	private UUID id;
	private InetSocketAddress mediatorSocketAddress;

	public HolePunchingServerSocket(int port, UUID id,
			InetSocketAddress mediatorSocketAddress) throws IOException {
		this.port = port;
		this.id = id;
		this.mediatorSocketAddress = mediatorSocketAddress;
	}

	public void register() throws IOException {
		mediatorClient = new Socket();
		mediatorClient.setReuseAddress(true);
		mediatorClient.bind(new InetSocketAddress(port));
		try {
			mediatorClient.connect(mediatorSocketAddress);
		} catch (IOException e) {
			throw new IOException("Can't connect to mediator");
		}

		StuntEndpoint endpoint = new StuntEndpoint(id, new InetSocketAddress(
				InetAddress.getLocalHost(), mediatorClient.getLocalPort()));
		ObjectOutputStream out = new ObjectOutputStream(mediatorClient
				.getOutputStream());
		out.writeObject("register");
		out.writeObject(endpoint);
		out.flush();
		ObjectInputStream in = new ObjectInputStream(mediatorClient
				.getInputStream());
		//wait until registration is finished
		boolean success = in.readBoolean();
	}

	public Socket accept() throws IOException {
		StuntEndpoint clientEndpoint = null;
		try {
			ObjectInputStream in = new ObjectInputStream(mediatorClient
					.getInputStream());
			clientEndpoint = (StuntEndpoint) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//Something is wrong with Mediator, try to reregister
			System.err.println("Connection lost to mediator, trying to reregister..");
			e.printStackTrace();
			register();
		}
		HolePuncher hp = new HolePuncher();
		return hp.doHolePunching(clientEndpoint.getRemoteInetSocketAddress(),
				clientEndpoint.getLocalInetSocketAddress(), mediatorClient
						.getLocalPort());
	}
}
