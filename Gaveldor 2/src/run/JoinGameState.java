package run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.run.GameMatch;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class JoinGameState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        GameMatch match = ((Game) game).match;
        match.model.applyDelta(delta);
        Action action;
        while ((action = match.getCurrentPC().retrieveAction()) != null) {
            match.getOtherPC().propagateAction(action);
            match.model.applyAction(action);
            if (match.model.gameState != GameState.SETTING_UP) {
                if (match.model.gameState == GameState.DISCONNECTED) {
                    // TODO
                } else {
                    game.enterState(PlayGameState.STATE_ID);
                    break;
                }
            }
        }
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

}
