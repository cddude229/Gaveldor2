package de.htwg_konstanz.rmi.hp.mediator;


public class MediatorStarter {

	public static void main(String[] args) {
		int port = 8765;
		try {
			port = Integer.valueOf(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			// no port argument --> start with default port
		} catch (NumberFormatException e) {
			// wrong port format
			System.out.println("Unrecognized argument " + args[0]
					+ "; you can optionally specify a port number.");
			System.exit(0);
		}	
		new Mediator(port);

	}

}
