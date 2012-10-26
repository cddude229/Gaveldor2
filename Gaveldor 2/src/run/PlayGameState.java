package run;

import game.model.Action;
import game.model.TerrainType;
import game.run.GameMatch;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayGameState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();

    private GameMatch match = null;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        TerrainType.initTiles();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        match = ((Game) game).match;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        match.getCurrentPC().render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        match.ui.update(container, game, delta);
        while (true){
            Action action = match.getCurrentPC().retrieveAction();
            if (action != null){
                match.getOtherPC().propagateAction(action);
                match.model.applyAction(action);
                continue;
            }
            action = match.getOtherPC().retrieveAction();
            if (action != null){
                match.getCurrentPC().propagateAction(action);
                match.model.applyAction(action);
                continue;
            }
            break;
        }
//        Action action;
//        while ((action = match.getCurrentPC().retrieveAction()) != null) {
//            match.getOtherPC().propagateAction(action);
//            match.model.applyAction(action);
//        }
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

}
