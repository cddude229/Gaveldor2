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
import org.newdawn.slick.Image;
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
    
    private int transformX(GameContainer container, int x, int width, boolean leftSide){
        if (leftSide){
            return x;
        } else{
            return container.getWidth() - x - width;
        }
    }
    
    private void drawStringSide(GameContainer container, Graphics g, String text, int x, int y, boolean leftSide){
        x = transformX(container, x, g.getFont().getWidth(text), leftSide);
        g.drawString(text, x, y);
    }
    
    private void drawImageSide(GameContainer container, Graphics g, Image im, int x, int y, boolean leftSide){
        x = transformX(container, x, im.getWidth(), leftSide);
        g.drawImage(im, x, y);
    }
    
    private void drawRectSide(GameContainer container, Graphics g, int x, int y, int width, int height, boolean leftSide){
        x = transformX(container, x, width, leftSide);
        g.drawRect(x, y, width, height);
    }
    
    private void fillRectSide(GameContainer container, Graphics g, int x, int y, int width, int height, boolean leftSide){
        x = transformX(container, x, width, leftSide);
        g.fillRect(x, y, width, height);
    }
    
    private void renderSide(GameContainer container, PlayerController pc, Graphics g, Player player, boolean leftSide) throws SlickException{
        int backX = 50;
        g.setFont(Constants.TEST_FONT);
        g.setColor(Color.white);
        drawStringSide(container, g, player.toString(), backX, 100, leftSide);
        boolean isAttacking = pc.model.getCurrentPlayer().equals(player);
        Piece piece = isAttacking ?
                pc.model.getMinigame().attackingPiece : pc.model.getMinigame().defendingPiece;
        drawImageSide(container, g, piece.getSprite(leftSide ? 0 : 3, 0), backX, 200, leftSide);


        int frontX = 300;
        for (int y : new int[]{150, 300, 450}){
            drawRectSide(container, g, frontX, y, 200, 100, leftSide);
        }
        ControlScheme controls = player.equals(pc.model.getPlayer1()) ? Constants.PLAYER_1_CONTROLS : Constants.PLAYER_2_CONTROLS;
        drawStringSide(container, g, controls.minigameHighKey, frontX + 50, 150, leftSide);
        drawStringSide(container, g, controls.minigameMidKey, frontX + 50, 300, leftSide);
        drawStringSide(container, g, controls.minigameLowKey, frontX + 50, 450, leftSide);
        MinigameModel.Move move = isAttacking ?
                pc.model.getMinigame().attackingMove : pc.model.getMinigame().defendingMove;
        if (pc.model.getMinigame().hasBothMoves()){
            switch (move){
            case HIGH:
                fillRectSide(container, g, frontX, 150, 200, 100, leftSide);
                break;
            case MID:
                fillRectSide(container, g, frontX, 300, 200, 100, leftSide);
                break;
            case LOW:
                fillRectSide(container, g, frontX, 450, 200, 100, leftSide);
                break;
            case NONE:
                break;
            }
        } else{
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
