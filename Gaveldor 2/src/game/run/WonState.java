package game.run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class WonState extends PlayerControllerState {

    public WonState(boolean isLocal) {
        super(GameState.WON, isLocal);
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        pc.renderBoard(container, g);
        pc.renderPieces(g);
        pc.renderControllerWon(g);
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        //TODO
    }

}
