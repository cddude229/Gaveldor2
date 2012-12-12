package game.run;

import game.model.GameModel.GameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import run.MainMenuState;
import util.MenuButton;
import util.Resources;

import com.aem.sticky.button.Button;
import com.aem.sticky.button.events.ClickListener;

public class DisconnectedState extends PlayerControllerState {

    private MenuButton menuBtn;
    
    public DisconnectedState(boolean isLocal) {
        super(GameState.DISCONNECTED, isLocal);
    }

    @Override
    public void init(GameContainer container, final PlayerController pc) throws SlickException {
        Rectangle rect = new Rectangle((container.getWidth()-100)/2, container.getHeight()-100,200,50);
        Image mainImg = Resources.getImage("/assets/graphics/buttons/general/main_menu.png");
        Image mainImgHvr = Resources.getImage("/assets/graphics/buttons/general/main_menu_hover.png");
        Sound s = Resources.getSound("/assets/audio/effects/click.ogg");
        menuBtn = new MenuButton(rect,mainImg,mainImgHvr,s);
        menuBtn.addListener(new ClickListener(){
            @Override
            public void onClick(Button clicked, float mx, float my) {
                pc.game.enterState(MainMenuState.STATE_ID);   
            }
            @Override
            public void onRightClick(Button clicked, float mx, float my) {}
            @Override
            public void onDoubleClick(Button clicked, float mx, float my) {}
        });
        this.stickyListener.add(menuBtn);
    }

    @Override
    public void render(GameContainer container, PlayerController pc, Graphics g) throws SlickException {
        pc.renderBoard(container, g);
        pc.renderHeaderText(container, g, "The Connection was Lost");
        menuBtn.render(container, g);
    }

    @Override
    public void updateLocal(GameContainer container, LocalPlayerController pc, int delta) throws SlickException {
        menuBtn.update(container, delta);
    }

}
