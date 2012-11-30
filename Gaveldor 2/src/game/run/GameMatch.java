package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Player;

import org.newdawn.slick.Music;

public class GameMatch {
    
    public final GameModel model;
    public final PlayerController pc1, pc2;


    
    public GameMatch(GameModel model, PlayerController pc1, PlayerController pc2){
        this.model = model;
        this.pc1 = pc1;
        this.pc2 = pc2;
        try {
        Music music = new Music("assets/audio/music/DarkKnight.ogg");
        music.loop();
        music.setVolume(util.Constants.BATTLE_START_VOLUME);
        music.fade(util.Constants.BATTLE_FADE_DURATION, util.Constants.BATTLE_END_VOLUME, false);
        } catch (Exception e) {
            
        }
    }
    
    public void startGameLocally(String mapName){
        if (pc1 instanceof LocalPlayerController && pc1.player.equals(model.getCurrentPlayer())){
            ((LocalPlayerController)pc1).actionQueue.add(new Action.GameStartAction(mapName));
        }
    }
    
    public PlayerController getPCByPlayer(Player player){
        if (pc1.player.equals(player)){
            return pc1;
        } else if (pc2.player.equals(player)){
            return pc2;
        } else{
            throw new IllegalArgumentException();
        }
    }
    
    public PlayerController getCurrentPC(){
        return getPCByPlayer(model.getCurrentPlayer());
    }
    
    public PlayerController getOtherPC(){
        return getPCByPlayer(model.getOtherPlayer());
    }
}
