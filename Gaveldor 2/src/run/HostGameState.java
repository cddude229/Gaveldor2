package run;

import game.model.Action;
import game.model.GameModel.GameState;
import game.run.GameMatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import util.Resources;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class HostGameState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private SimpleButton backBtn;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private String hostIP = "";
    ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        listener = new StickyListener();
        buttons = this.buildButtons(container, game);
        container.getInput().addListener(listener);
        for (SimpleButton button : buttons) {
            listener.add(button);
        }
        
        URL whatismyip = null;
        String ip = "";
        try {
            whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(
                            whatismyip.openStream()));
            ip = in.readLine(); //you get the IP as a String
        } catch (IOException e) {
            try {
                whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
            } catch (MalformedURLException d) {
                d.printStackTrace();
            }
            try {
                in = new BufferedReader(new InputStreamReader(
                                whatismyip.openStream()));
                ip = in.readLine(); //you get the IP as a String
            } catch (IOException f) {
                hostIP = "Cannot connect to Internet";
            }
        }
        hostIP = ip;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        backBtn.render(container, g);
        g.drawString("Waiting For Player to Connect", 300, 100);
        g.drawString("Your External IP:" + hostIP, 300, 200);
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        GameMatch match = ((Game) game).match;
        Action action;
        for (SimpleButton button : buttons) {
            button.update(container, delta);
        }
        while ((action = match.getOtherPC().retrieveAction()) != null) {
            match.getOtherPC().propagateAction(action);
            match.model.applyAction(action);
            
            if (match.model.gameState != GameState.SETTING_UP) {
                if (match.model.gameState == GameState.DISCONNECTED) {
                    // TODO
                } else {
                    game.enterState(PlayGameState.STATE_ID);
                    break;
                }
            }
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
            System.out.println(yLoc);
            locations.add(new int[] { this.getxLoc(bWidth), yLoc });
            yLoc += 100;
        }
        // create rectangles for buttons
        Rectangle backRect = new Rectangle(locations.get(5)[0] - 300, locations.get(5)[1], bWidth, bHeight);

        // create play Image
        Sound s = Resources.getSound("/assets/audio/swordSlash.ogg");
        ArrayList<Image> images = this.makeImages();
        System.out.println(images.size());

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
                System.out.println("true");
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

}

