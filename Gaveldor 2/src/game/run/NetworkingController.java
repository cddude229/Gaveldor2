package game.run;

import game.model.Action;
import game.model.Action.DisconnectAction;
import game.model.Action.GameStartAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;

public class NetworkingController {
    private ArrayDeque<Action> sendables;
    private ArrayDeque<Action> receivables;
    private Socket socket = null;
    private final int port;
    private final boolean isHosting;

    // private GameException threadException = null;

    /**
     * Constructor for non-hosting player.
     * 
     * @throws IOException
     * @throws UnknownHostException
     */
    public NetworkingController(String IP, int port) throws UnknownHostException, IOException {
        this.socket = new Socket(IP, port);
        this.port = port;
        this.sendables = new ArrayDeque<Action>();
        this.receivables = new ArrayDeque<Action>();
        this.isHosting = false;
    }

    /**
     * Constructor for hosting player.
     */
    public NetworkingController(int port) {
        this.socket = null;
        this.port = port;
        this.sendables = new ArrayDeque<Action>();
        this.receivables = new ArrayDeque<Action>();
        this.isHosting = true;
    }

    public void start() {
        new Thread(new StreamStarter()).start();
    }

    public void stop() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            socket = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class StreamStarter implements Runnable {

        @Override
        public void run() {
            // ObjectInputStream in;
            // ObjectOutputStream out;
            try {
                if (NetworkingController.this.isHosting) {
                    ServerSocket serverSocket = new ServerSocket(NetworkingController.this.port);
                    NetworkingController.this.socket = serverSocket.accept();
                    NetworkingController.this.sendables.add(new GameStartAction());
                    NetworkingController.this.receivables.add(new GameStartAction());
                }
                ObjectOutputStream out = new ObjectOutputStream(NetworkingController.this.socket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(NetworkingController.this.socket.getInputStream());
                new Thread(new InputStreamHandler(in)).start();
                new Thread(new OutputStreamHandler(out)).start();
            } catch (IOException e) {
                NetworkingController.this.receivables.add(new DisconnectAction());
                stop();
            }
        }

    }

    private class InputStreamHandler implements Runnable {

        private final ObjectInputStream in;

        public InputStreamHandler(ObjectInputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Action line;
                    System.out.println("Recieving");
                    try {
                        line = (Action) in.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Received " + line.type);
                    NetworkingController.this.receivables.add(line);
                }
            } catch (IOException e) {
                NetworkingController.this.receivables.add(new DisconnectAction());
                stop();
            }
        }

    }

    private class OutputStreamHandler implements Runnable {

        private final ObjectOutputStream out;

        public OutputStreamHandler(ObjectOutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (NetworkingController.this.sendables) {
                        Action output = NetworkingController.this.sendables.poll();
                        if (output != null) {
                            System.out.println("Sending " + output.type);
                            out.writeObject(output);
                            System.out.println("Sent");
                        }
                    }
                    out.flush();
                }
            } catch (IOException e) {
                NetworkingController.this.receivables.add(new DisconnectAction());
                stop();
            }
        }

    }

    public Action getAction() {
        synchronized (this.receivables) {
            return this.receivables.poll();
        }
    }

    public void sendAction(Action input) {
        synchronized (this.sendables) {
            this.sendables.add(input);
        }
    }

    // public void throwThreadExceptionIfNecessary() throws GameException{
    // if (threadException != null){
    // throw threadException;
    // }
    // }
}
