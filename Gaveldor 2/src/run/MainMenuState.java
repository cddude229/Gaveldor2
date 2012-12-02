package run;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class MainMenuState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private StickyListener listener;
    private GameContainer container;
    private StateBasedGame game;
    private static final int bWidth = 150;
    private static final int bHeight = 50;
    ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();

    @Override
    /**
     * Builds buttons and adds listeners to game. This isn't fully functional.
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Music music = new Music("assets/audio/music/TheHaunting.ogg");
        music.loop();
        music.setVolume(util.Constants.MENU_VOLUME);
        this.container = container;
        this.game = game;
        listener = new StickyListener();
        buttons = this.buildButtons();
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
        for (SimpleButton button: buttons){
            button.render(container, g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
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
    public ArrayList<SimpleButton> buildButtons() throws SlickException {
        
        //create possible locations for buttons
        ArrayList<int[]> locations = new ArrayList<int[]>();
        int yLoc = 75;
        for (int i = 0; i < 7; i++) {
            locations.add(new int[] { this.getxLoc(container, bWidth), yLoc });
            yLoc += 100;
        }
        
        //create rectangles for buttons
        ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
        for (int i = 0; i < locations.size(); i++){
            rects.add(new Rectangle(locations.get(i)[0],locations.get(i)[1],bWidth,bHeight));
        }

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();
        
        //create the buttons
        ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
        for (int i = 0; i < rects.size(); i++){
            buttons.add(new SimpleButton(rects.get(i), images.get(2*i), images.get(2*i + 1),s));
        }

        // create listeners
        createListeners(game, buttons);
        return buttons;
    }

    /**
     * Adds the listeners to the system. Editing the listeners must be in order of button appearance
     *
     */
    private void createListeners(final StateBasedGame game, ArrayList<SimpleButton> buttons) {
        for (int i = 0; i < buttons.size(); i++){
            switch (i){

            case 0:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
                        /*
                        try {
                            ((Game) game).startLocalMatch("/assets/maps/basic");
                        } catch (GameException e) {
                            e.printStackTrace();
                        }
                        game.enterState(PlayGameState.STATE_ID);
                        */
                        MapSelectionState.match = MapSelectionState.MatchType.LOCAL;
                        game.enterState(MapSelectionState.STATE_ID);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;

            case 1:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
                        game.enterState(HostGameState.STATE_ID);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;

            case 2:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
                        game.enterState(JoinGameState.STATE_ID);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;

            case 3:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
                        game.enterState(MatchMakingState.STATE_ID);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;

            case 4:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
                        game.enterState(InstructionState.STATE_ID);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;

            case 5:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
                        game.enterState(CreditsState.STATE_ID);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;

            case 6:
                buttons.get(i).addListener(new ClickListener(){
                    public void onClick(Button clicked, float mx, float my) {
                        container.exit();
                        System.exit(0);
                    }

                    public void onDoubleClick(Button clicked, float mx, float my) {}
                    public void onRightClick(Button clicked, float mx, float my) {}
                });
                break;
            }
        }
    }
    
    /**
     * Creates the images and the corresponding image text
     * @return images, a list of images used for each of the buttons
     * @throws SlickException
     */
    public ArrayList<Image> makeImages() throws SlickException {
        ArrayList<Image> images = new ArrayList<Image>();
        Image im;
        Image clickPlay;
        for (int i = 0; i <7; i++){
            switch (i){
            
            case 0:
                im = new Image("assets/graphics/buttons/mainmenu/local_match.png");
                clickPlay = new Image("assets/graphics/buttons/mainmenu/local_match_hover.png");
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 1:
                im = new Image(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Host a Match", 0, 0);
                
                clickPlay = new Image(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Host a Match", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 2:
                im = new Image(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Join a Match", 0, 0);
                
                clickPlay = new Image(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Join a Match", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 3:
                im = new Image(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Matchmaking", 0, 0);
                
                clickPlay = new Image(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Matchmaking", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 4:
                im = new Image(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Instructions", 0, 0);
                
                clickPlay = new Image(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Instructions", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 5:
                im = new Image(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Credits", 0, 0);
                
                clickPlay = new Image(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Credits", 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 6:
                im = new Image(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Exit", 0, 0);
                
                clickPlay = new Image(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Exit", 0, 0);
                
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
