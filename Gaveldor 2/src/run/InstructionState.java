package run;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;
import util.MenuButton;
import util.Resources;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.events.ClickListener;

public class InstructionState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private MenuButton backBtn,fbtn,bbtn;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private ArrayList<Image> images;
    private int page;
    private Rectangle backRect,frect,brect;
    private int w,h;
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        page=0;
        w=container.getWidth();
        h = container.getHeight();
        listener = new StickyListener();
        ArrayList<int[]> locations = new ArrayList<int[]>();
        int yLoc = 75;
        for (int i = 0; i < 6; i++) {
            locations.add(new int[] { this.getxLoc(container, bWidth), yLoc });
            yLoc += 100;
        }
        // create rectangles for buttons
        backRect = new Rectangle(w/2-bWidth/2,h-bHeight - 10, bWidth, bHeight);
        frect = new Rectangle(15*w/20, 9*h/10, 19*w/20, h-30);
        brect = new Rectangle(w/20, 9*h/10, 5*w/20, h-30);

        // create play Image
        Sound s = Resources.getSound("/assets/audio/effects/click.ogg");
        images = this.makeImages();

        // add buttons
        backBtn = new MenuButton(backRect, images.get(0), images.get(1), s);
        fbtn = new MenuButton(frect, images.get(2).getScaledCopy(w/5,h/10-10),images.get(2).getScaledCopy(w/5,h/10-10),s);
        bbtn = new MenuButton(brect, images.get(3).getScaledCopy(w/5,h/10-10),images.get(3).getScaledCopy(w/5,h/10-10),s);

        // create listeners
        createListeners(container,game);
        listener.add(backBtn);
        listener.add(fbtn);
        listener.add(bbtn);
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.getInput().addListener(listener);
        page = 0;
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game){
        container.getInput().removeListener(listener);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        float deltaW,deltaH;
        if (container.getWidth()<=1100 && container.getHeight()<=800)
        {
            deltaW = (container.getWidth()/(float) Constants.WINDOW_WIDTH - 1) * Constants.WINDOW_WIDTH;
            deltaH = (float) 0.0001 * (container.getHeight()/(float) Constants.WINDOW_HEIGHT - 1) * Constants.WINDOW_HEIGHT;
            g.drawImage(images.get(page+4), 0, 0,Constants.WINDOW_WIDTH + deltaW,Constants.WINDOW_HEIGHT- Constants.WINDOW_HEIGHT/10 - deltaH,0,0,1280,800);
            backBtn.render(container, g);
            fbtn.render(container, g);
            bbtn.render(container, g);
        }
        else
        {
            deltaW = (1100/(float) Constants.WINDOW_WIDTH - 1) * Constants.WINDOW_WIDTH;
            deltaH = (float) 0.0001 * (800/(float) Constants.WINDOW_HEIGHT - 1) * Constants.WINDOW_HEIGHT;
            g.drawImage(images.get(page+4), (container.getWidth()-(Constants.WINDOW_WIDTH + deltaW)), 0,Constants.WINDOW_WIDTH + deltaW,Constants.WINDOW_HEIGHT- Constants.WINDOW_HEIGHT/10 - deltaH,0,0,1280,800);
            backBtn.render(container, g);
            fbtn.render(container, g);
            bbtn.render(container, g);
        }
        
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
        backBtn.update(container, delta);
        fbtn.update(container, delta);
        bbtn.update(container, delta);    
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

    /**
     * 
     * @param width
     * @param height
     * @return an int[] containing the screen location of the buttons
     */
    public int getxLoc(GameContainer container, int width) {
        int scnWidth = container.getWidth();
        int xLoc = scnWidth / 2 - width / 2;
        return xLoc;
    }
    
    /**
     * Adds the listeners to the system. Currently only the play button is
     * implemented.
     */
    private void createListeners(final GameContainer container, final StateBasedGame game) {
        backBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                game.enterState(MainMenuState.STATE_ID);
            }

            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        fbtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                page=((page+1)%(images.size()-4));
            }

            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        bbtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                page=((page-1)%(images.size()-4));
                if (page<0){
                    page=images.size()-5;
                }
            }

            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
    }
    
    public ArrayList<Image> makeImages() throws SlickException {
        ArrayList<Image> images = new ArrayList<Image>();
        Image im = Resources.getImage("assets/graphics/buttons/general/back.png");
        Image clickPlay = Resources.getImage("assets/graphics/buttons/general/back_hover.png");
        /*Image im = new Image(bWidth, bHeight);
        im.getGraphics().setColor(Color.blue);
        im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        im.getGraphics().setColor(Color.white);
        Image clickPlay = new Image(bWidth, bHeight);
        clickPlay.getGraphics().setColor(Color.yellow);
        clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        clickPlay.getGraphics().setColor(Color.black);
        im.getGraphics().drawString("Main Menu", 0, 0);
        clickPlay.getGraphics().drawString("Main Menu", 0, 0);
        */im.getGraphics().flush();
        clickPlay.getGraphics().flush();
        images.add(im);
        images.add(clickPlay);
        Image wel = Resources.getImage("/assets/graphics/instructions/welcome.png");
        wel.getGraphics().flush();
        Image move = Resources.getImage("/assets/graphics/instructions/movement.png");
        move.getGraphics().flush();
        Image attack = Resources.getImage("/assets/graphics/instructions/attack.png");
        attack.getGraphics().flush();
        Image unit = Resources.getImage("/assets/graphics/instructions/unit.png");
        unit.getGraphics().flush();
        Image terrain = Resources.getImage("/assets/graphics/instructions/terrain.png");
        terrain.getGraphics().flush();
        Image misc = Resources.getImage("/assets/graphics/instructions/misc.png");
        misc.getGraphics().flush();
        Image forward = Resources.getImage("/assets/graphics/instructions/forward.png");
        forward.getGraphics().flush();
        Image backward = Resources.getImage("/assets/graphics/instructions/backward.png");
        backward.getGraphics().flush();
        images.add(forward);
        images.add(backward);
        images.add(wel);
        images.add(move);
        images.add(attack);
        images.add(unit);
        images.add(terrain);
        images.add(misc);
        return images;
    }

}

