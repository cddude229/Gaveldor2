package run;

import game.model.Action;
import game.model.GameModel;
import game.run.GameMatch;
import game.run.GameUI;
import game.run.LocalPlayerController;
import game.run.RemotePlayerController;

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
        //TODO
        GameUI ui = new GameUI();
        GameModel model = new GameModel();
        match = new GameMatch(ui, model,
                new LocalPlayerController(null, ui, model),
                new RemotePlayerController(null));
        
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        // TODO Auto-generated method stub
        
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
