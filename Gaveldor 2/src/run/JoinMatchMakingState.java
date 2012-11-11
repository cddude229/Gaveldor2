package run;

import game.run.GameException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class JoinMatchMakingState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private SimpleButton backBtn;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private String hostIP = "";
    
    private ServerSocket socketSource = null;
    private Socket socket = null;
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        listener = new StickyListener();
        ArrayList<int[]> locations = new ArrayList<int[]>();
        int yLoc = 75;
        for (int i = 0; i < 6; i++) {
            locations.add(new int[] { this.getxLoc(container, bWidth), yLoc});
            yLoc += 100;
        }
        // create rectangles for buttons
        Rectangle backRect = new Rectangle(locations.get(5)[0], locations.get(5)[1], bWidth, bHeight);

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();

        // add button
        backBtn = new SimpleButton(backRect, images.get(0), images.get(1), s);

        // create listeners
        createListeners(container,game);
        listener.add(backBtn);
    }
    
    public static String getIPAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.getInput().addListener(listener);
        hostIP = getIPAddress();

    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game){
        container.getInput().removeListener(listener);
        try {
            if (socket != null){
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        backBtn.render(container, g);
        g.drawString("Waiting For Player to Connect", 300, 100);
        g.drawString("Your External IP:" + hostIP, 300, 200);
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (socket != null && socket.isConnected()){
            try {
                ((Game)game).startHostRemoteMatch("/assets/maps/basic", socket);
            } catch (GameException e) {
                // TODO
                throw new RuntimeException(e);
            }
            socket = null;
            game.enterState(PlayGameState.STATE_ID);
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
        Image im = new Image(bWidth, bHeight);
        im.getGraphics().setColor(Color.blue);
        im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        im.getGraphics().setColor(Color.white);
        Image clickPlay = new Image(bWidth, bHeight);
        clickPlay.getGraphics().setColor(Color.green);
        clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        clickPlay.getGraphics().setColor(Color.white);
        im.getGraphics().drawString("Back", 0, 0);
        clickPlay.getGraphics().drawString("Back", 0, 0);
        im.getGraphics().flush();
        clickPlay.getGraphics().flush();
        images.add(im);
        images.add(clickPlay);
        return images;
    }

}

