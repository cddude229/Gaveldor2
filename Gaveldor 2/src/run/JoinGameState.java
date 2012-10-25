package run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class JoinGameState extends BasicGameState {
    
    public static final int STATE_ID = Game.allocateStateID();

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        if (((Game)game).match.model.gameState != GameState.SETTING_UP){
            System.out.println(((Game)game).match.model.gameState);
            game.enterState(PlayGameState.STATE_ID);
        }
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

}
