package run;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
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

public class CreditsState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private SimpleButton backBtn;
    private StickyListener listener;
    private GameContainer container;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
    String[] credits;
    HashMap<String,int[]> nameLocations = new HashMap<String,int[]>();
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        listener = new StickyListener();
        buttons = this.buildButtons(container, game);
        container.getInput().addListener(listener);
        for (SimpleButton button : buttons) {
            listener.add(button);
        }
        credits = new String[] {"Credits","Chris Dessonville: Co-Producer","Ben Greenberg: Co-Producer","Lane Pertusi: Artists","Calvin Lewis: Sound","Todd Layton: Slick Master","Andres Romero: Networking", "Jeremy Sharpe: Game Logic","Kevin White: Menu"};
        generateLocations(credits);
        
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        backBtn.render(container, g);
        for (String line: this.nameLocations.keySet()){
            g.drawString(line, nameLocations.get(line)[0], nameLocations.get(line)[1]);
        }
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
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
        for (int i = 0; i < 6; i++) {
            locations.add(new int[] { this.getxLoc(bWidth), yLoc });
            yLoc += 100;
        }
        // create rectangles for buttons
        Rectangle backRect = new Rectangle(locations.get(5)[0] - 300, locations.get(5)[1], bWidth, bHeight);

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();

        // add buttons
        backBtn = new SimpleButton(backRect, images.get(0), images.get(1), s);

        // create listeners
        createListeners(container,game);

        // add to array of buttons
        ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
        buttons.add(backBtn);
        return buttons;
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
    }
    
    public ArrayList<Image> makeImages() throws SlickException {
        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i <6; i++){
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
                im.getGraphics().drawString("Back", 0, 0);
                clickPlay.getGraphics().drawString("Back", 0, 0);
                break;
            }
            im.getGraphics().flush();
            clickPlay.getGraphics().flush();
            images.add(im);
            images.add(clickPlay);
        }
        return images;
    }
    
    private void generateLocations(String[] credits){
        Font defFont = container.getDefaultFont();
        int yLoc = 50;
        for (String line : credits){
            int width = defFont.getWidth(line);
            this.nameLocations.put(line, new int[] {(Constants.WINDOW_WIDTH-width)/2,yLoc});
            yLoc += 50;
        }
    }

}

