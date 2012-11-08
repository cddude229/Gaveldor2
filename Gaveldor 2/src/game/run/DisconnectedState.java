package game.run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.Constants;

public class DisconnectedState extends PlayerControllerState {

    public DisconnectedState(boolean isLocal) {
        super(GameState.DISCONNECTED, isLocal);
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        //TODO: initialize disconnected state
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        pc.renderBoard(container, g);
        pc.renderPieces(container, g);
        g.setFont(Constants.TEST_FONT);
        g.drawString("DISCONNECTED: the connection was lost", 0, 100);
        //TODO: render disconnected state
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        //TODO: update local disconnected state
    }

}
