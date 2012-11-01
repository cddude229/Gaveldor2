package de.htwg_konstanz.rmi.demo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class HPHelloClient {

	public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
		
		System.setProperty("java.security.policy", HPHelloClient.class
				.getClassLoader().getResource("resources/client.policy")
				.toExternalForm());

		System.setSecurityManager(new SecurityManager());
		
		
		Registry registry = LocateRegistry.getRegistry("141.37.121.130");
		Hello server = (Hello) registry.lookup("HPhello");

		System.out.println(server.getHello());

	}
	
}
