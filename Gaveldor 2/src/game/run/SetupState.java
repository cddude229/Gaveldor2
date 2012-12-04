package game.run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SetupState extends PlayerControllerState {

    public SetupState(boolean isLocal) {
        super(GameState.SETTING_UP, isLocal);
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {

    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {

    }

}
