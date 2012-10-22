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
    
    public static final int STATE_ID = 1;
    
    private GameMatch match = null;

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        TerrainType.initTiles();
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game){
        match = ((Game)game).match;
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        match.getCurrentPC().render(container, game, g);
        
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        match.ui.update(container, game, delta);
        Action action;
        while ((action = match.getCurrentPC().retrieveAction()) != null){
            match.getOtherPC().propagateAction(action);
            match.model.applyAction(action); // must come second because END TURN action switches current and other
        }
    }
    

    @Override
    public int getID() {
        return STATE_ID;
    }

}
