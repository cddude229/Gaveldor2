package de.htwg_konstanz.rmi.demo;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Hello extends Remote {
	String getHello() throws RemoteException;
}
