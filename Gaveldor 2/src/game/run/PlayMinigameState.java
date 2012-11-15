package game.run;

import game.model.Action;
import game.model.Action.MinigameMoveAction;
import game.model.GameModel.GameState;
import game.model.MinigameModel;
import game.model.MinigameModel.Move;
import game.model.Piece;
import game.model.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.Constants;
import util.ControlScheme;

public class PlayMinigameState extends PlayerControllerState {
    
    private boolean hasPlayedAttackSound;

    public PlayMinigameState(boolean isLocal) {
        super(GameState.PLAYING_MINIGAME, isLocal);
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        //TODO: initialize minigame state

    }
    
    @Override
    public void enter(GameContainer container, PlayerController pc) throws SlickException{
        hasPlayedAttackSound = false;
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        //TODO: render minigame visuals
        g.setFont(Constants.TEST_FONT);
        g.setColor(Color.white);
        g.drawString("MINIGAME", 0, 0);
        renderSide(container, pc, g, pc.model.getPlayer1(), true);
        renderSide(container, pc, g, pc.model.getPlayer2(), false);
        if (pc.model.getMinigame().hasBothMoves()){
            if (pc.model.getMinigame().isSuccessfulAttack()){
                g.drawString("Hit!", 0, 600);
                if (!hasPlayedAttackSound){
                    pc.model.getMinigame().attackingPiece.pieceType.getAttackSound().play();
                    hasPlayedAttackSound = true;
                }
            } else{
                g.drawString("Blocked!", 0, 600);
            }
        }
    }
    
    private void renderSide(GameContainer container, PlayerController pc, Graphics g, Player player, boolean leftSide) throws SlickException{
        int x = leftSide ? 0 : 400;
        g.setFont(Constants.TEST_FONT);
        g.setColor(Color.white);
        g.drawString(player.toString(), x, 100);
        boolean isAttacking = pc.model.getCurrentPlayer().equals(player);
        Piece piece = isAttacking ?
                pc.model.getMinigame().attackingPiece : pc.model.getMinigame().defendingPiece;
        g.drawImage(piece.getSprite(leftSide ? 0 : 3, 0), x, 200);
        MinigameModel.Move move = isAttacking ?
                pc.model.getMinigame().attackingMove : pc.model.getMinigame().defendingMove;
        if (pc.model.getMinigame().hasBothMoves()){
            g.drawString(move.toString(), x, 500);
        } else{
            g.drawString("Not yet...", x, 500);
        }
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        if (pc.model.getMinigame().hasBothMoves()){
            if (pc.isCurrentPC()){
                if (pc.model.getMinigame().sinceHasBothMoves >= Constants.MINIGAME_WAIT_TIME){
                    pc.actionQueue.add(new Action.MinigameEndAction());
                }
            }
        } else{
            updateLocalMove(container, pc, delta);
        }
    }
    
    private void updateLocalMove(GameContainer container, LocalPlayerController pc, int delta){
        if (pc.player.equals(pc.model.getCurrentPlayer())){
            if (pc.model.getMinigame().attackingMove != null){
                return;
            }
        } else{
            if (pc.model.getMinigame().defendingMove != null){
                return;
            }
        }
        ControlScheme controls = pc.player.equals(pc.model.getPlayer1()) ? Constants.PLAYER_1_CONTROLS : Constants.PLAYER_2_CONTROLS;
        if (pc.model.getMinigame().sinceMoveTimeStart >= Constants.MINIGAME_MOVE_TIME){
            pc.actionQueue.add(new MinigameMoveAction(Move.NONE, pc.player));
        } else if (container.getInput().isKeyDown(controls.minigameLowMove)){
            pc.actionQueue.add(new MinigameMoveAction(Move.LOW, pc.player));
        } else if (container.getInput().isKeyDown(controls.minigameMidMove)){
            pc.actionQueue.add(new MinigameMoveAction(Move.MID, pc.player));
        } else if (container.getInput().isKeyDown(controls.minigameHighMove)){
            pc.actionQueue.add(new MinigameMoveAction(Move.HIGH, pc.player));
        }
    }

}
