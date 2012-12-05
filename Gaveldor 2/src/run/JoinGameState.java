package run;

import game.run.GameException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
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

import util.Constants;
import util.MenuButton;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.events.ClickListener;

public class JoinGameState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private MenuButton connectBtn;
    private MenuButton backBtn;
    private TextField ipBox;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private String instructionTxt;
    ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
    
    private Socket socket = null;

    @Override
    /**
     * Builds buttons and adds listeners to game. This isn't fully functional.
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        instructionTxt = "Please enter the Host IP.";
        listener = new StickyListener();
        buttons = this.buildButtons(container, game);
        ipBox.setAcceptingInput(false);
        for (MenuButton button : buttons) {
            listener.add(button);
        }

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.getInput().addListener(listener);
        ipBox.setAcceptingInput(true);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game){
        container.getInput().removeListener(listener);
        ipBox.setAcceptingInput(false);
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
    public void render(GameContainer container, StateBasedGame game, Graphics g){
        backBtn.render(container, g);
        connectBtn.render(container, g);
        ipBox.render(container, g);
        g.drawString(instructionTxt, ipBox.getX(), ipBox.getY() -50);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
        for (MenuButton button : buttons) {
            button.update(container, delta);
        }
        if (socket != null && socket.isConnected()){
            try {
                ((Game)game).startClientRemoteMatch(socket);
            } catch (GameException e) {
                //TODO: display error message
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
     * This function builds the buttons and adds the listeners. returning them
     * in an arrayList. The arrayList is useful for update iterations.
     * 
     * @return an arrayList of the five buttons
     * @throws SlickException
     */
    public ArrayList<MenuButton> buildButtons(GameContainer container, StateBasedGame game) throws SlickException {
        ArrayList<int[]> locations = new ArrayList<int[]>();
        int yLoc = 75;
        for (int i = 0; i < 6; i++) {
            locations.add(new int[] { this.getxLoc(container, bWidth), yLoc });
            yLoc += 100;
        }
        // create rectangles for buttons
        Rectangle backRect = new Rectangle(locations.get(5)[0] - 150, locations.get(5)[1], bWidth, bHeight);
        Rectangle connectRect = new Rectangle(locations.get(5)[0] + 150, locations.get(5)[1], bWidth, bHeight);

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();
        Font defaultFont = images.get(0).getGraphics().getFont();
        ipBox = new TextField(container,defaultFont,locations.get(2)[0],locations.get(2)[1],bWidth,bHeight);
        ipBox.setBackgroundColor(Color.white);
        ipBox.setTextColor(Color.black);
        
        // add buttons
        backBtn = new MenuButton(backRect, images.get(0), images.get(1), s);
        connectBtn = new MenuButton(connectRect, images.get(2), images.get(3), s);

        // create listeners
        createListeners(container,game);

        // add to array of buttons
        ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(connectBtn);
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
        connectBtn.addListener(new ClickListener(){
            public void onClick(Button clicked, float mx, float my) {
                socket = new Socket();
                new Thread(new Runnable(){
                    private final String ip = ipBox.getText();
                    @Override
                    public void run() {
                        try {
                            socket.connect(new InetSocketAddress(ip, Constants.REMOTE_CONNECTION_PORT));
                        }catch (IOException e) {
                          instructionTxt = "A connection could not be established. Please try again.";
                          ipBox.setText("");
                          ipBox.setCursorVisible(true);
                        }
                    }
                }).start();
            }

            public void onDoubleClick(Button clicked, float mx, float my) {}
            public void onRightClick(Button clicked, float mx, float my) {}
        });

    }
    
    public ArrayList<Image> makeImages() throws SlickException {
        ArrayList<Image> images = new ArrayList<Image>();
        Image im;
        Image clickPlay;
        for (int i = 0; i <6; i++){
            switch (i){
            case 0:
//              im = new Image("assets/graphics/buttons/joinmatch/connect.png");
//              clickPlay = new Image("assets/graphics/buttons/joinmatch/connect_hover.png");
                im = new Image(bWidth, bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Back", 0, 0);
                
                clickPlay = new Image(bWidth, bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Back", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
            case 1:
//              im = new Image("assets/graphics/buttons/joinmatch/join_match_back.png");
//              clickPlay = new Image("assets/graphics/buttons/joinmatch/join_match_back_hover.png");
                im = new Image(bWidth, bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Connect", 0, 0);
                
                clickPlay = new Image(bWidth, bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Connect", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
            }
        }
        return images;
    }

}
