package game.run;

import game.model.Action.MinigameMoveAction;
import game.model.GameModel.GameState;
import game.model.MinigameModel.Move;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.Constants;

public class PlayMinigameState extends PlayerControllerState {

    public PlayMinigameState(boolean isLocal) {
        super(GameState.PLAYING_MINIGAME, isLocal);
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        //TODO, obviously
        g.setFont(Constants.testFont);
        g.setColor(Color.white);
        g.drawString("MINIGAME", 0, 0);
        g.drawString(pc.model.getCurrentPlayer().toString(), 0, 100);
        g.drawString(pc.model.getOtherPlayer().toString(), 400, 100);
        g.drawString(String.valueOf(pc.model.getMinigame().attackingMove), 0, 200);
        g.drawString(String.valueOf(pc.model.getMinigame().defendingMove), 400, 200);
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        updateLocalMove(container, pc, delta);
    }
    
    private void updateLocalMove(GameContainer container, LocalPlayerController pc, int delta){
        if (pc.model.getMinigame().moveTime >= Constants.MINIGAME_MOVE_TIME){
            pc.actionQueue.add(new MinigameMoveAction(Move.NONE, pc.player));
        } else if (container.getInput().isKeyDown(pc.controls.minigameLowMove)){
            pc.actionQueue.add(new MinigameMoveAction(Move.LOW, pc.player));
        } else if (container.getInput().isKeyDown(pc.controls.minigameMidMove)){
            pc.actionQueue.add(new MinigameMoveAction(Move.MID, pc.player));
        } else if (container.getInput().isKeyDown(pc.controls.minigameHighMove)){
            pc.actionQueue.add(new MinigameMoveAction(Move.HIGH, pc.player));
        }
    }

}
