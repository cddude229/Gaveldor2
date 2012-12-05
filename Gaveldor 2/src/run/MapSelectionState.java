package run;

import game.run.GameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Resources;

import com.aem.sticky.StickyListener;
import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class MapSelectionState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    
    public static String map = "/assets/maps/";
    
    public static MatchType match;
    public static enum MatchType {
        LOCAL,
        HOST,
        JOIN,
        MATCH
    }
    
    private SimpleButton backBtn;
    private StickyListener listener;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private String instructionTxt;
    ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
    ArrayList<String> maps = new ArrayList<String>();

    @Override
    /**
     * Builds buttons and adds listeners to game. This isn't fully functional.
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        instructionTxt = "Please select a map or hit back to return to the main menu.";
        listener = new StickyListener();
        maps = this.getMapNames();
        this.buildButtons(container, game);
        for (SimpleButton button : buttons) {
            listener.add(button);
        }
        listener.add(backBtn);
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
        for (SimpleButton btn: buttons){
            btn.render(container, g);
        }
        g.drawString(instructionTxt, 200, 50);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
        ((Game)game).toggleFullscreenCheck((AppGameContainer)container);
        backBtn.update(container, delta);
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
    public void buildButtons(GameContainer container, StateBasedGame game) throws SlickException {
        ArrayList<int[]> locations = new ArrayList<int[]>();
        int yLoc = 150;
        int xLoc = 75;
        for (int i = 0; i < 6; i++) {
            if (i == 5){
                locations.add(new int[] {this.getxLoc(container, bWidth), 600});
            }
            else{
                locations.add(new int[] {xLoc, yLoc});
                xLoc += 300;
                if (i == 2){
                    yLoc += 100;
                    xLoc = 75;
                }
            }
        }
        ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
        for (int i = 0; i< locations.size(); i++){
            rects.add(new Rectangle(locations.get(i)[0],locations.get(i)[1],bWidth,bHeight));
        }
        // create rectangles for buttons
        Rectangle backRect = new Rectangle(locations.get(5)[0], locations.get(5)[1], bWidth, bHeight);

        // create play Image
        Sound s = null;
        ArrayList<Image> images = this.makeImages();
        // add buttons
        backBtn = new SimpleButton(backRect, images.get(0), images.get(1), s);
        
        for (int i = 0; i<rects.size()-2;i++){
            buttons.add(new SimpleButton(rects.get(i),images.get(2*i+2),images.get(2*i+3),s));
        }

        // create listeners
        createListeners(container,game);
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
        for (int i = 0; i <buttons.size(); i ++){
            final int index = i;
            System.out.println(index);
            buttons.get(i).addListener(new ClickListener(){

                @Override
                public void onClick(Button clicked, float mx, float my) {
                    map += maps.get(index);
                    switch(match){
                    case LOCAL :
                        try {
                            ((Game) game).startLocalMatch(map);
                            game.enterState(PlayGameState.STATE_ID);
                        } catch (GameException e) {
                            e.printStackTrace(); 
                        }
                        break;
                    case HOST :
                        game.enterState(HostGameState.STATE_ID);
                        break;
                    }
                }

                @Override
                public void onRightClick(Button clicked, float mx, float my) {}
                @Override
                public void onDoubleClick(Button clicked, float mx, float my) {}
                });
        }
    }
    
    public ArrayList<Image> makeImages() throws SlickException {
        ArrayList<Image> images = new ArrayList<Image>();
        Image im;
        Image clickPlay;
        for (int i = 0; i <6; i++){
            switch (i){
            case 0:
//              im = new Image("assets/graphics/buttons/localmatch/local_back.png");
//              clickPlay = new Image("assets/graphics/buttons/localmatch/local_back_hover.png");
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
                im = new Image(bWidth, bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                clickPlay = new Image(bWidth, bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 2:
                im = new Image(bWidth, bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                clickPlay = new Image(bWidth, bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 3:
                im = new Image(bWidth, bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                clickPlay = new Image(bWidth, bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                break;
                
            case 4:
                im = new Image(bWidth, bHeight);
                im.getGraphics().setColor(Color.blue);
                im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                clickPlay = new Image(bWidth, bHeight);
                clickPlay.getGraphics().setColor(Color.yellow);
                clickPlay.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
                clickPlay.getGraphics().setColor(Color.black);
                clickPlay.getGraphics().drawString(maps.get(i-1), 0, 0);
                
                im.getGraphics().flush();
                clickPlay.getGraphics().flush();
                images.add(im);
                images.add(clickPlay);
                
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            }
        }
        return images;
    }

    public boolean isValidMap(String selection) throws IOException{
       return getMapNames().contains(selection);
    }
    
    public ArrayList<String> getMapNames(){
        ArrayList<String> validMaps = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream("/assets/maps/AllMaps.index")));
        try {
            String rowLine;
            while(((rowLine = reader.readLine()) != null)){
                validMaps.add(rowLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return validMaps;
    }
}
