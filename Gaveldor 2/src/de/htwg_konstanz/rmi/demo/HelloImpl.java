package de.htwg_konstanz.rmi.demo;

import java.rmi.RemoteException;


public class HelloImpl implements Hello {

	public String getHello() throws RemoteException {
		return "hello, world";
	}

}
