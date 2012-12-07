package run;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
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

public class MainMenuState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private StickyListener listener;
    private GameContainer container;
    private StateBasedGame game;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
    private static Image bgImage;
    private Music music;

    @Override
    /**
     * Builds buttons and adds listeners to game. This isn't fully functional.
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        music = Resources.getMusic("/assets/audio/music/TheHaunting.ogg");
        music.setVolume(util.Constants.MENU_VOLUME);
        bgImage = Resources.getImage("/assets/graphics/ui/main_menu_bg.png");
        bgImage.getGraphics().flush();
        this.container = container;
        this.game = game;
        listener = new StickyListener();
        buttons = this.buildButtons();
        game.enterState(MainMenuState.STATE_ID);
        for (MenuButton button : buttons) {
            listener.add(button);
        }

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.getInput().addListener(listener);
        if (!music.playing()){
            music.loop();
        }
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game){
        container.getInput().removeListener(listener);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //int titleWidth = g.getFont().getWidth("Welcome to Gaveldor 2: The Engaveling of Ambidextria");
        //g.drawString("Welcome to Gaveldor 2: The Engaveling of Ambidextria", (container.getWidth() - titleWidth)/2, 0);
        
        g.drawImage(bgImage, container.getWidth()/2-Constants.WINDOW_WIDTH/2,
                                container.getHeight()/2-Constants.WINDOW_HEIGHT/2);
        //this.getxLoc(container,1024),this.getyLoc(container,768));
        for (MenuButton button: buttons){
            button.render(container, g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
        for (MenuButton button : buttons) {
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
     * @return an int containing the screen location of the buttons
     */
    public int getxLoc(GameContainer container, int width) {
        int scnWidth = container.getWidth();
        int xLoc = scnWidth / 2 - width / 2;
        return xLoc;
    }

    /**
     * 
     * @param width
     * @param height
     * @return an int containing the screen location of the buttons
     */
    public int getyLoc(GameContainer container, int height) {
        int scnHeight = container.getHeight();
        int yLoc = scnHeight / 2 - height / 2;
        return yLoc;
    }
    
    /**
     * This function builds the buttons and adds the listeners. returning them
     * in an arrayList. The arrayList is useful for update iterations.
     * 
     * @return an arrayList of the five buttons
     * @throws SlickException
     */
    public ArrayList<MenuButton> buildButtons() throws SlickException {
        
        //create possible locations for buttons
        ArrayList<int[]> locations = new ArrayList<int[]>();
        //int yLoc = 75;
        int x_offset = -50;
        int y_offset = -160;
        for (int i = 0; i < 7; i++) {
            locations.add(new int[] { container.getWidth()/2+x_offset,//this.getxLoc(container, bWidth)+x_offset, 
                                        container.getHeight()/2+y_offset});
                                      //this.getyLoc(container, bHeight)+y_offset});
            //locations.add(new int[] { this.getxLoc(container, bWidth), yLoc });
            //yLoc += 100;
            y_offset += 70;
        }
        
        //create rectangles for buttons
        ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
        for (int i = 0; i < locations.size(); i++){
            rects.add(new Rectangle(locations.get(i)[0],locations.get(i)[1],bWidth,bHeight));
        }

        // create play Image
        Sound s = Resources.getSound("/assets/audio/effects/click.ogg");
        ArrayList<Image> images = this.makeImages();
        
        //create the buttons
        ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
        for (int i = 0; i < rects.size(); i++){
            buttons.add(new MenuButton(rects.get(i), images.get(2*i), images.get(2*i + 1),s));
        }

        // create listeners
        createListeners(game, buttons);
        return buttons;
    }

    /**
     * Adds the listeners to the system. Editing the listeners must be in order of button appearance
     *
     */
    private void createListeners(final StateBasedGame game, ArrayList<MenuButton> buttons) {
        for (int i = 0; i < buttons.size(); i++){
            switch (i){

            case 0:
                buttons.get(i).addListener(new ClickListener() {

                    public void onClick(Button clicked, float mx, float my) {
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
                im = Resources.getImage("/assets/graphics/buttons/mainmenu/local_match.png");
                clickPlay = Resources.getImage("/assets/graphics/buttons/mainmenu/local_match_hover.png");
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 1:
                im = Resources.getImage("assets/graphics/buttons/mainmenu/host_match.png");
                clickPlay = Resources.getImage("assets/graphics/buttons/mainmenu/host_match_hover.png");
/*                im = Resources.getImage(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Host a Match", 0, 0);
                
                clickPlay = Resources.getImage(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Host a Match", 0, 0);
*/                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 2:
                im = Resources.getImage("assets/graphics/buttons/mainmenu/join_match.png");
                clickPlay = Resources.getImage("assets/graphics/buttons/mainmenu/join_match_hover.png");
/*                im = Resources.getImage(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Join a Match", 0, 0);
                
                clickPlay = Resources.getImage(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Join a Match", 0, 0);
*/                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 3:
                im = Resources.getImage("assets/graphics/buttons/mainmenu/matchmaking.png");
                clickPlay = Resources.getImage("assets/graphics/buttons/mainmenu/matchmaking_hover.png");
/*                im = Resources.getImage(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Matchmaking", 0, 0);
                
                clickPlay = Resources.getImage(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Matchmaking", 0, 0);
*/                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 4:
                im = Resources.getImage("assets/graphics/buttons/mainmenu/instructions.png");
                clickPlay = Resources.getImage("assets/graphics/buttons/mainmenu/instructions_hover.png");
/*                im = Resources.getImage(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Instructions", 0, 0);
                
                clickPlay = Resources.getImage(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Instructions", 0, 0);
*/                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 5:
                im = Resources.getImage("assets/graphics/buttons/mainmenu/credits.png");
                clickPlay = Resources.getImage("assets/graphics/buttons/mainmenu/credits_hover.png");
/*                im = Resources.getImage(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Credits", 0, 0);
                
                clickPlay = Resources.getImage(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Credits", 0, 0);
*/                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 6:
                im = Resources.getImage("assets/graphics/buttons/mainmenu/exit.png");
                clickPlay = Resources.getImage("assets/graphics/buttons/mainmenu/exit_hover.png");
/*                im = Resources.getImage(bWidth,bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString("Exit", 0, 0);
                
                clickPlay = Resources.getImage(bWidth,bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString("Exit", 0, 0);
*/                
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
