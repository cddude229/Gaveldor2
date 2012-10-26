package run;

import game.run.GameException;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Constants;
import util.Resources;

import com.aem.sticky.button.SimpleButton;

public class MainMenuState extends BasicGameState {

    public static final int STATE_ID = Game.allocateStateID();
    private int buttonCount = 0;
    private SimpleButton playBtn;
    private SimpleButton instructBtn;
    private SimpleButton connectBtn;
    private SimpleButton creditBtn;
    private SimpleButton exitBtn;
    private static final int bWidth = 200;
    private static final int bHeight = 50;
    private ArrayList<int[]> locations = new ArrayList<int[]>();

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        int yLoc = 75;
        for (int i = 0; i < 5; i++) {
            locations.add(new int[] { this.getxLoc(bWidth), yLoc });
            yLoc += 100;
        }
        Rectangle playRect = new Rectangle(locations.get(0)[0], locations.get(0)[1], bWidth, bHeight);
        Rectangle instructRect = new Rectangle(locations.get(1)[0], locations.get(1)[1], bWidth, bHeight);
        Rectangle connectRect = new Rectangle(locations.get(2)[0], locations.get(2)[1], bWidth, bHeight);
        Rectangle creditRect = new Rectangle(locations.get(3)[0], locations.get(3)[1], bWidth, bHeight);
        Rectangle exitRect = new Rectangle(locations.get(4)[0], locations.get(4)[1], bWidth, bHeight);

        Sound s = Resources.getSound("/assets/audio/swordSlash.ogg");
        Image im = new Image(bWidth, bHeight);
        im.getGraphics().setColor(Color.blue);
        im.getGraphics().fillRect(0, 0, im.getWidth(), im.getHeight());
        im.getGraphics().flush();
        playBtn = new SimpleButton(playRect, im, im, s);
        instructBtn = new SimpleButton(instructRect, im, im, s);
        connectBtn = new SimpleButton(connectRect, im, im, s);
        creditBtn = new SimpleButton(creditRect, im, im, s);
        exitBtn = new SimpleButton(exitRect, im, im, s);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawString("Welcome to Galvedor 2: The Engaveling of Ambidextria", 0, 0);
        playBtn.render(container, g);
        instructBtn.render(container, g);
        connectBtn.render(container, g);
        creditBtn.render(container, g);
        exitBtn.render(container, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // TODO
        try {
            if (container.getInput().isKeyPressed(Input.KEY_L)) {
                ((Game) game).startLocalMatch("/assets/maps/basic");
                game.enterState(PlayGameState.STATE_ID);
            } else if (container.getInput().isKeyPressed(Input.KEY_H)) {
                ((Game) game).startHostRemoteMatch("/assets/maps/basic");
                game.enterState(HostGameState.STATE_ID);
            } else if (container.getInput().isKeyPressed(Input.KEY_C)) {
                ((Game) game).startClientRemoteMatch("/assets/maps/basic", "localhost");
                game.enterState(JoinGameState.STATE_ID);
            }
        } catch (GameException e) {
            // TODO: display in window
            e.printStackTrace();
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

}
