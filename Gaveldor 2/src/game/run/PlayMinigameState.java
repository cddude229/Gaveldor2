package game.run;

import game.model.Action.MakeMinigameMoveAction;
import game.model.GameModel.GameState;
import game.model.MinigameModel.Move;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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
        g.drawString("MINIGAME", 0, 0);
        g.drawString(String.valueOf(pc.model.getMinigame().attackingMove), 0, 100);
        g.drawString(String.valueOf(pc.model.getMinigame().defendingMove), 400, 100);
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        if (pc.model.getCurrentPlayer().equals(pc.player)){
            updateLocalAttack(container, pc, delta);
        } else{
            updateLocalDefend(container, pc, delta);
        }
    }
    
    public void updateLocalAttack(GameContainer container, LocalPlayerController pc, int delta){
        if (pc.model.getMinigame().attackingMove == null){
            if (pc.model.getMinigame().moveTime >= Constants.MINIGAME_MOVE_TIME){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.NONE, pc.player));
            } else if (container.getInput().isKeyDown(Input.KEY_A)){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.HIGH, pc.player));
            } else if (container.getInput().isKeyDown(Input.KEY_S)){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.MID, pc.player));
            } else if (container.getInput().isKeyDown(Input.KEY_D)){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.LOW, pc.player));
            }
        }
    }
    
    public void updateLocalDefend(GameContainer container, LocalPlayerController pc, int delta){
        if (pc.model.getMinigame().defendingMove == null){
            if (pc.model.getMinigame().moveTime >= Constants.MINIGAME_MOVE_TIME){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.NONE, pc.player));
            } else if (container.getInput().isKeyDown(Input.KEY_J)){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.HIGH, pc.player));
            } else if (container.getInput().isKeyDown(Input.KEY_K)){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.MID, pc.player));
            } else if (container.getInput().isKeyDown(Input.KEY_L)){
                pc.actionQueue.add(new MakeMinigameMoveAction(Move.LOW, pc.player));
            }
        }
    }

}
