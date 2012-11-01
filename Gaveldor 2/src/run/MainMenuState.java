package run;

import game.run.GameException;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class MainMenuState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private SimpleButton playBtn;
    private SimpleButton instructBtn;
    private SimpleButton hostBtn;
    private SimpleButton joinBtn;
    private SimpleButton hostMatchBtn;
    private SimpleButton findMatchBtn;
    private SimpleButton creditBtn;
    private SimpleButton exitBtn;
    private StickyListener listener;
    private static final int bWidth = 150;
    private static final int bHeight = 50;
    ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();

    @Override
    /**
     * Builds buttons and adds listeners to game. This isn't fully functional.
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

        listener = new StickyListener();
        buttons = this.buildButtons(container, game);
        game.enterState(MainMenuState.STATE_ID);
        for (SimpleButton button : buttons) {
            listener.add(button);
        }

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.getInput().addListener(listener);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game){
        container.getInput().removeListener(listener);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawString("Welcome to Gaveldor 2: The Engaveling of Ambidextria", 250, 10);
        //g.drawString("In game shift + click moves characters", 0, 150);
        for (SimpleButton button: buttons){
            button.render(container, g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // TODO
        for (SimpleButton button : buttons) {
            button.update(container, delta);
        }
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
    public int getxLoc(int width) {
        int scnWidth = Constants.WINDOW_WIDTH;
        int xLoc = scnWidth / 2 - width / 2;
        return xLoc;
    }

    /**
     * This function builds the buttons and adds the listeners. returning them
     * in an arrayList. The arrayList is useful for update iterations.
     * 
     * @return an arrayList of the five buttons
     * @throws SlickException
     */
    public ArrayList<SimpleButton> buildButtons(GameContainer container, StateBasedGame game) throws SlickException {
        ArrayList<int[]> locations = new ArrayList<int[]>();
        int yLoc = 75;
        for (int i = 0; i < 8; i++) {
            locations.add(new int[] { this.getxLoc(bWidth), yLoc });
            yLoc += 75;
        }
        // create rectangles for buttons
        Rectangle playRect = new Rectangle(locations.get(0)[0], locations.get(0)[1], bWidth, bHeight);
        Rectangle hostRect = new Rectangle(locations.get(1)[0], locations.get(1)[1], bWidth, bHeight);
        Rectangle joinRect = new Rectangle(locations.get(2)[0], locations.get(2)[1], bWidth, bHeight);
        Rectangle joinMatchRect = new Rectangle(locations.get(3)[0], locations.get(3)[1], bWidth, bHeight);
        Rectangle findMatchRect = new Rectangle(locations.get(4)[0], locations.get(4)[1], bWidth, bHeight);
        Rectangle instructRect = new Rectangle(locations.get(5)[0], locations.get(5)[1], bWidth, bHeight);
        Rectangle creditRect = new Rectangle(locations.get(6)[0], locations.get(6)[1], bWidth, bHeight);
        Rectangle exitRect = new Rectangle(locations.get(7)[0], locations.get(7)[1], bWidth, bHeight);

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();

        // add buttons
        playBtn = new SimpleButton(playRect, images.get(0), images.get(1), s);
        hostBtn = new SimpleButton(hostRect, images.get(2), images.get(3), s);
        joinBtn = new SimpleButton(joinRect, images.get(4), images.get(5), s);
        hostMatchBtn = new SimpleButton(joinMatchRect, images.get(6),images.get(7), s);
        findMatchBtn = new SimpleButton(findMatchRect, images.get(8),images.get(9), s);
        instructBtn = new SimpleButton(instructRect, images.get(10), images.get(11), s);
        creditBtn = new SimpleButton(creditRect, images.get(12), images.get(13), s);
        exitBtn = new SimpleButton(exitRect, images.get(14), images.get(15), s);

        // create listeners
        createListeners(container,game);

        // add to array of buttons
        ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
        buttons.add(playBtn);
        buttons.add(hostBtn);
        buttons.add(joinBtn);
        buttons.add(hostMatchBtn);
        buttons.add(findMatchBtn);
        buttons.add(instructBtn);
        buttons.add(creditBtn);
        buttons.add(exitBtn);
        return buttons;
    }

    /**
     * Adds the listeners to the system. Currently only the playbutton is
     * implemented.
     */
    private void createListeners(final GameContainer container, final StateBasedGame game) {
        playBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                try {
                    ((Game) game).startLocalMatch("/assets/maps/basic");
                } catch (GameException e) {
                    e.printStackTrace();
                }
                game.enterState(PlayGameState.STATE_ID);
            }

            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        exitBtn.addListener(new ClickListener(){
            public void onClick(Button clicked, float mx, float my) {
                container.exit();
                System.exit(0);
            }

            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        
        hostBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
//                try {
//                    ((Game) game).startHostRemoteMatch("/assets/maps/basic");
                    game.enterState(HostGameState.STATE_ID);
//                } catch (GameException e) {
//                    e.printStackTrace();
//                }
            }
            
            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        
        instructBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                game.enterState(InstructionState.STATE_ID);
            }
            
            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });

        joinBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                game.enterState(JoinGameState.STATE_ID);
            }
            
            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        
        creditBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                game.enterState(CreditsState.STATE_ID);
            }
            
            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        
        hostMatchBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                game.enterState(HostMatchMakingState.STATE_ID);
            }
            
            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
        
        findMatchBtn.addListener(new ClickListener() {

            public void onClick(Button clicked, float mx, float my) {
                game.enterState(JoinMatchMakingState.STATE_ID);
            }
            
            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });
    }
    
    public ArrayList<Image> makeImages() throws SlickException {
        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i <8; i++){
            Image im = new Image(bWidth, bHeight);
            im.getGraphics().setColor(Color.blue);
            im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
            im.getGraphics().setColor(Color.white);
            Image clickPlay = new Image(bWidth, bHeight);
            clickPlay.getGraphics().setColor(Color.green);
            clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
            clickPlay.getGraphics().setColor(Color.white);
            switch (i){
            case 0:
                im.getGraphics().drawString("Play Local Match", 0, 0);
                clickPlay.getGraphics().drawString("Play Local Match", 0, 0);
                break;
            case 1:
                im.getGraphics().drawString("Host a Match", 0, 0);
                clickPlay.getGraphics().drawString("Host a Match", 0, 0);
                break;
            case 2:
                im.getGraphics().drawString("Join a Match", 0, 0);
                clickPlay.getGraphics().drawString("Join a Match", 0, 0);
                break;
            case 3:
                im.getGraphics().drawString("Host matchmaking", 0, 0);
                clickPlay.getGraphics().drawString("Host matchmaking", 0, 0);
                break;
            case 4:
                im.getGraphics().drawString("Join matchmaking", 0, 0);
                clickPlay.getGraphics().drawString("Join matchmaking", 0, 0);
                break;
            case 5:
                im.getGraphics().drawString("Instructions", 0, 0);
                clickPlay.getGraphics().drawString("Instructions", 0, 0);
                break;
            case 6:
                im.getGraphics().drawString("Credits", 0, 0);
                clickPlay.getGraphics().drawString("Credits", 0, 0);
                break;
            case 7:
                im.getGraphics().drawString("Exit", 0, 0);
                clickPlay.getGraphics().drawString("Exit", 0, 0);
                break;
            }
            im.getGraphics().flush();
            clickPlay.getGraphics().flush();
            images.add(im);
            images.add(clickPlay);
        }
        return images;
    }

}
