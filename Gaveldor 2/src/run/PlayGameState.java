package run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.model.PieceType;
import game.model.TerrainType;
import game.run.GameMatch;
import game.run.PlayerController;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Resources;

public class PlayGameState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();

    private GameMatch match = null;
    
    private Music music;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        TerrainType.initAssets();
        PieceType.initAssets();
        PlayerController.initAssets();
        music = Resources.getMusic("/assets/audio/music/DarkKnight.ogg");
        music.setVolume(util.Constants.BATTLE_START_VOLUME);
        music.fade(util.Constants.BATTLE_FADE_DURATION, util.Constants.BATTLE_END_VOLUME, false);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        match = ((Game) game).match;
        match.getCurrentPC().init(container);
        match.getOtherPC().init(container);
        if (!music.playing()){
            music.loop();
        }
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
        match.getCurrentPC().update(container, game, delta);
        match.getOtherPC().update(container, game, delta);
        updateActions(container, game, delta);
        
        if((match.model.gameState == GameState.WON) || (match.model.gameState == GameState.DISCONNECTED)){
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
