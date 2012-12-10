package run;

import game.run.MatchmakingNetworkingController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

public class MatchMakingState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private MenuButton backBtn;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private static Image bgImage;
    private String hostIP = "";
    public String mapName = "";
    public boolean host = false;
    public boolean setupDone = false;
    
    
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
        Sound s = Resources.getSound("/assets/audio/effects/click.ogg");
        ArrayList<Image> images = this.makeImages();

        // add button
        backBtn = new MenuButton(backRect, images.get(0), images.get(1), s);

        // create listeners
        createListeners(container,game);
        listener.add(backBtn);
        
        bgImage = Resources.getImage("/assets/graphics/ui/menu_bg.png");
        bgImage.getGraphics().flush();
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
        socket = new Socket();
        final MatchMakingState state = this;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    socket.connect(new InetSocketAddress(Constants.MATCHMAKING_SERVER_IP, Constants.SERVER_PORT));
                    new MatchmakingNetworkingController(socket,state).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        
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
        g.drawImage(bgImage, container.getWidth()/2-Constants.WINDOW_WIDTH/2,
                container.getHeight()/2-Constants.WINDOW_HEIGHT/2);
        backBtn.render(container, g);
        int w1 = g.getFont().getWidth("Waiting For Another Player to Connect to MatchMaking");
        g.drawString("Waiting For Another Player to Connect to MatchMaking", 
                    (container.getWidth()-w1)/2, container.getHeight()/2-300);
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
        backBtn.update(container, delta);
        if (socket != null && setupDone){
            if (host) {
                ((Game)game).startHostRemoteMatch(mapName, socket);
                socket = null;
                game.enterState(PlayGameState.STATE_ID);
            }
            else {
                ((Game)game).startClientRemoteMatch(socket);
                socket = null;
                game.enterState(PlayGameState.STATE_ID);
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
      Image im = Resources.getImage("assets/graphics/buttons/general/back.png");
      Image clickPlay = Resources.getImage("assets/graphics/buttons/general/back_hover.png");
/*        Image im = Resources.getImage(bWidth, bHeight);
        im.getGraphics().setColor(Color.blue);
        im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        im.getGraphics().setColor(Color.white);
        Image clickPlay = Resources.getImage(bWidth, bHeight);
        clickPlay.getGraphics().setColor(Color.yellow);
        clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        clickPlay.getGraphics().setColor(Color.black);
        im.getGraphics().drawString("Back", 0, 0);
        clickPlay.getGraphics().drawString("Back", 0, 0);
*/        im.getGraphics().flush();
        clickPlay.getGraphics().flush();
        images.add(im);
        images.add(clickPlay);
        return images;
    }

}

