package run;

import game.run.GameException;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class MapSelectionState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    
    private static String map = "/assets/maps/";
    
    public static MatchType match;
    public static enum MatchType {
        LOCAL,
        HOST,
        JOIN,
        MATCH
    }
    
    private SimpleButton mapBtn;
    private SimpleButton backBtn;
    private TextField mapBox;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private String instructionTxt;
    ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();

    @Override
    /**
     * Builds buttons and adds listeners to game. This isn't fully functional.
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        instructionTxt = "Please enter map name.";
        listener = new StickyListener();
        buttons = this.buildButtons(container, game);
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
    public void render(GameContainer container, StateBasedGame game, Graphics g){
        backBtn.render(container, g);
        mapBtn.render(container, g);
        mapBox.render(container, g);
        g.drawString(instructionTxt, mapBox.getX(), mapBox.getY() -50);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta){
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
    public int getxLoc(GameContainer container, int width) {
        int scnWidth = container.getWidth();
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
            locations.add(new int[] { this.getxLoc(container, bWidth), yLoc });
            yLoc += 100;
        }
        // create rectangles for buttons
        Rectangle backRect = new Rectangle(locations.get(5)[0] - 150, locations.get(5)[1], bWidth, bHeight);
        Rectangle mapRect = new Rectangle(locations.get(5)[0] + 150, locations.get(5)[1], bWidth, bHeight);

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();
        Font defaultFont = images.get(0).getGraphics().getFont();
        mapBox = new TextField(container,defaultFont,locations.get(2)[0],locations.get(2)[1],bWidth,bHeight);
        mapBox.setBackgroundColor(Color.white);
        mapBox.setTextColor(Color.black);
        mapBox.setText("basic");
        
        // add buttons
        backBtn = new SimpleButton(backRect, images.get(0), images.get(1), s);
        mapBtn = new SimpleButton(mapRect, images.get(2), images.get(3), s);

        // create listeners
        createListeners(container,game);

        // add to array of buttons
        ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
        buttons.add(mapBtn);
        buttons.add(backBtn);
        return buttons;
    }

    /**
     * Adds the listeners to the system. Currently only the playbutton is
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
        mapBtn.addListener(new ClickListener(){
            public void onClick(Button clicked, float mx, float my) {
                String entry = mapBox.getText();
                System.out.println("clicked MAP");
                System.out.println(entry);
                mapBox.setText("Choose a Map");
                if(isValidMap(entry)){
                    map += entry;
                    
                    try {
                        ((Game) game).startLocalMatch("/assets/maps/basic");
                    } catch (GameException e) {
                        e.printStackTrace();
                        
                    }
                    game.enterState(PlayGameState.STATE_ID);
                }
                
                
                
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
            case 1:
                im.getGraphics().drawString("Select", 0, 0);
                clickPlay.getGraphics().drawString("Select", 0, 0);
                break;
            }
            im.getGraphics().flush();
            clickPlay.getGraphics().flush();
            images.add(im);
            images.add(clickPlay);
        }
        return images;
    }

    public boolean isValidMap(String selection){
        return selection.equals("basic");
    }
}
