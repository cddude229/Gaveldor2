package de.htwg_konstanz.rmi.hp.socket;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HolePunchingRemoteObject extends UnicastRemoteObject {

	private static final long serialVersionUID = -246789676359150232L;

	/*
	 * Dummy constructor to call super constructor with single custom rmi factory
	 * (same for client and server)
	 */
	private HolePunchingRemoteObject(int port, HolePunchingRmiSocketFactory fac) throws RemoteException {
		super(port, fac, fac);
	}
	
	/**
	 * Creates and exports a new HolePunchingRemoteObject object using an
     * anonymous port.
	 * @param mediatorInetSocketAddress IP Socket Address of the mediator
	 * @throws RemoteException if failed to export object
	 */
	protected HolePunchingRemoteObject(InetSocketAddress mediatorInetSocketAddress) throws RemoteException {
		this(0, new HolePunchingRmiSocketFactory(mediatorInetSocketAddress));
	}
	
	/**
	 * Creates and exports a new HolePunchingRemoteObject object using the
     * particular supplied port.
     * @param port the port number on which the remote object receives calls
     * (if <code>port</code> is zero, an anonymous port is chosen)
	 * @param mediatorInetSocketAddress IP Socket Address of the mediator
	 * @throws RemoteException if failed to export object
	 */
	protected HolePunchingRemoteObject(int port, InetSocketAddress mediatorInetSocketAddress) throws RemoteException {
		this(port, new HolePunchingRmiSocketFactory(mediatorInetSocketAddress));
	}

	/**
	 * Exports the remote object to make it ready to establish connections via
	 * hole punching.
	 * 
	 * @param obj
	 *            the remote object to be exported
	 * @param port
	 *            the port to export the given object on
	 * @param mediatorInetSocketAddress
	 *            IP Socket Address of the mediator
	 * @return remote object stub
	 * @throws RemoteException
	 *             if the export fails
	 */
	public static Remote exportObject(Remote obj, int port, InetSocketAddress mediatorInetSocketAddress) throws RemoteException {
		HolePunchingRmiSocketFactory fac = new HolePunchingRmiSocketFactory(mediatorInetSocketAddress);
		return UnicastRemoteObject.exportObject(obj, port, fac, fac);
	}


	
}
