package de.htwg_konstanz.rmi.demo;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.htwg_konstanz.rmi.hp.socket.HolePunchingRemoteObject;




public class HPHelloSetup  {

	public static void main(String[] args) throws UnknownHostException, RemoteException, 
			MalformedURLException, NotBoundException, InterruptedException, AlreadyBoundException {

		System.setProperty("java.rmi.server.codebase", "http://141.37.121.130/~icermi/hello.jar " +
				"http://141.37.121.130/~icermi/HPSocketFactory.jar");

		Hello stub = (Hello) HolePunchingRemoteObject.exportObject(
				new HelloImpl(), 0, new InetSocketAddress("141.37.121.130", 8765));		

		Registry registry = LocateRegistry.getRegistry("141.37.121.130");
		registry.rebind("HPhello", stub);
		System.out.println("successfully registered.");
	}

}
