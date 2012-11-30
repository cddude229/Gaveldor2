package game.run;

import game.model.ServerMessage;
import game.model.ServerMessage.PlayerSelectMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import run.MatchMakingState;

public class MatchmakingNetworkingController {
    private Socket socket = null;
    private MatchMakingState state;

    public MatchmakingNetworkingController(Socket socket, MatchMakingState state) {
        this.socket = socket;
        this.state = state;
    }

    public void start() {
        try {
            ObjectInputStream in = new ObjectInputStream(MatchmakingNetworkingController.this.socket.getInputStream());
            new Thread(new InputStreamHandler(in)).start();
            System.out.println("Waiting for packet");
        } catch (IOException e) {
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
                    ServerMessage line;
                    System.out.println("Recieving");
                    try {
                        line = (ServerMessage) in.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    PlayerSelectMessage actualLine = (PlayerSelectMessage) line;
                    System.out.println("Received " + line.type);
                    state.host =  actualLine.host;
                    state.mapName = actualLine.mapName;
                    state.setupDone = true;
                    break;
                }
            } catch (IOException e) {
            }
        }
    }

}
