package game.run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.Constants;
import util.Helpful;

import com.aem.sticky.button.Button;
import com.aem.sticky.button.events.ClickListener;

public class WonState extends PlayerControllerState {

    private Button backButton;
    
    public WonState(boolean isLocal) {
        super(GameState.WON, isLocal);
    }

    @Override
    public void init(GameContainer container, PlayerController pc) throws SlickException {
        backButton = Helpful.makeButton(container.getWidth() - Constants.BOARD_SIDEBAR_WIDTH / 2, 450, "Back to Main Menu", new ClickListener(){
            @Override
            public void onClick(Button clicked, float mx, float my) {
                
            }
            @Override
            public void onRightClick(Button clicked, float mx, float my) {
            }
            @Override
            public void onDoubleClick(Button clicked, float mx, float my) {
            }
        });
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        pc.renderBoard(container, g);
        pc.renderControllerWon(g);
        //render won state
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        //update local won state
    }

}
