package de.htwg_konstanz.rmi.hp.socket;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class HolePuncher {

	private static ThreadGroup userGroup = Thread.currentThread().getThreadGroup();
	
	public Socket doHolePunching(InetSocketAddress remoteInetSocketAddress, InetSocketAddress localInetSocketAddress, int ownPort)
			throws ConnectException {
		FoundSocket foundSocket = new FoundSocket();
		
		SocketConnector socketToLocal = new SocketConnector(localInetSocketAddress, ownPort, foundSocket);
		SocketConnector socketToRemote = new SocketConnector(remoteInetSocketAddress, ownPort, foundSocket);
		SocketListener socketListener = new SocketListener(ownPort, foundSocket);

		socketToLocal.setDaemon(true);
		socketListener.setDaemon(true);
		socketToRemote.setDaemon(true);
		
		socketToLocal.setName("Socket to Local: " + localInetSocketAddress.getHostName());
		socketToRemote.setName("Socket to Remote: " + remoteInetSocketAddress.getHostName());
		socketListener.setName("Socket Listener on: " + ownPort);

		try {
			synchronized (foundSocket) {
				socketListener.start();
				socketToLocal.start();
				if (!remoteInetSocketAddress.equals(localInetSocketAddress)) {
					socketToRemote.start();
				}
				foundSocket.wait(60000);
				socketToLocal.interrupt();
				socketToRemote.interrupt();
				socketListener.stopThread();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Socket s = foundSocket.getFoundSocket();
		//no Socket found
		if (s == null) {
			throw new ConnectException(
					"Can't establish Hole Punching Connection");
		}
		return s;
	}

	private class SocketListener extends Thread {

		private int ownPort;
		private FoundSocket foundSocket;
		private ServerSocket ss;

		public SocketListener(int ownPort, FoundSocket foundSocket) {
			super(userGroup, "HP Socket Listener on: " + ownPort);
			this.ownPort = ownPort;
			this.foundSocket = foundSocket;
			try {
				this.ss = new ServerSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public synchronized void stopThread() {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				ss.setReuseAddress(true);
				ss.bind(new InetSocketAddress(InetAddress.getLocalHost(), ownPort));
				Socket s = ss.accept();
				synchronized (foundSocket) {
					foundSocket.setFoundSocket(s);
					foundSocket.notify();
					return;
				}
			} catch (SocketException e) {
				//Socket gets closed for stoping thread
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private class SocketConnector extends Thread {

		private InetSocketAddress inetSocketAddress;
		private int ownPort;
		private FoundSocket foundSocket;

		public SocketConnector(InetSocketAddress inetSocketAddress, int ownPort,
				FoundSocket foundSocket) {
			super(userGroup, "HP Socket Connector: " + inetSocketAddress.getHostName());
			this.inetSocketAddress  = inetSocketAddress;
			this.ownPort = ownPort;
			this.foundSocket = foundSocket;
		}

		public void run() {
			while (!isInterrupted()) {
				try {
					Socket s = new Socket();
					s.setReuseAddress(true);
					s.bind(new InetSocketAddress(ownPort));
					s.connect(inetSocketAddress);
					synchronized (foundSocket) {
						foundSocket.setFoundSocket(s);
						foundSocket.notify();
						return;
					}
				} catch (BindException e) {
					// other Socket is already found
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						//reinterrupt itself because interrupt status will be cleared
						Thread.currentThread().interrupt();
					}
				} catch (SocketException e) {
					// i.e connection refused.. so try again
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						//reinterrupt itself because interrupt status will be cleared
						Thread.currentThread().interrupt();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
