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
                g.drawString(
                        pc.model.getMinigame().attackingMove == MinigameModel.Move.NONE ?
                                "Fumble!" : "Block!", 0, 600);
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

        
        int frontX = 500;
        
        ControlScheme controls = player.equals(pc.model.getPlayer1()) ? Constants.PLAYER_1_CONTROLS : Constants.PLAYER_2_CONTROLS;
        

        int width, height;
        MinigameModel.Move move;
        if (isAttacking){
            width = 200;
            height = 50;
            move = pc.model.getMinigame().attackingMove;
        } else{
            width = 50;
            height = 125;
            move = pc.model.getMinigame().defendingMove;
        }
        
        for (MinigameModel.Move m : new MinigameModel.Move[]{
                MinigameModel.Move.HIGH, MinigameModel.Move.MID, MinigameModel.Move.LOW}){
            int y = 150 * (m.ordinal() + 1);
            int x = frontX - width;
            if (pc.model.getMinigame().hasBothMoves() && m == move){
                if (isAttacking){
                    float frac = 1f * pc.model.getMinigame().sinceHasBothMoves / Constants.MINIGAME_WAIT_TIME;
                    x += 50 * Math.min(2 * frac, 1);
                }
                g.setColor(Color.gray);
                fillRectSide(container, g, x, y - height / 2, width, height, leftSide);
            } else{
                if (pc.model.getMinigame().hasBothMoves() && !isAttacking && m == pc.model.getMinigame().attackingMove){
                    g.setColor(Color.red);
                    fillRectSide(container, g, x, y - height / 2, width, height, leftSide);
                } else{
                    g.setColor(Color.white);
                    drawRectSide(container, g, x, y - height / 2, width, height, leftSide);
                }
                if (!pc.model.getMinigame().hasBothMoves()){
                    drawStringSide(container, g,
                            controls.keys.get(m), x + 5, y - g.getFont().getHeight(controls.keys.get(m)) / 2, leftSide);
                }
            }
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
