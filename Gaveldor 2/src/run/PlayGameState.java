package run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.model.PieceType;
import game.model.TerrainType;
import game.run.GameMatch;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayGameState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();

    private GameMatch match = null;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        TerrainType.initTiles();
        PieceType.init();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        match = ((Game) game).match;
        match.getCurrentPC().init(container);
        match.getOtherPC().init(container);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException{
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        match.getCurrentPC().render(container, g);
    }
    
    private void updateActions(GameContainer container, StateBasedGame game, int delta) throws SlickException{
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
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
        updateActions(container, game, delta);
        match.model.applyDelta(delta);
        match.getCurrentPC().update(container, delta);
        match.getOtherPC().update(container, delta);
        updateActions(container, game, delta);
        
        if(match.model.gameState == GameState.WON){
            if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
                game.enterState(MainMenuState.STATE_ID);
            }
        }
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

}
