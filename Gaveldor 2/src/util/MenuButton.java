package util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.aem.sticky.button.SimpleButton;

public class MenuButton extends SimpleButton {
    
    private static final int WIDTH = 200, HEIGHT = 50;
    private Rectangle initRect;
    public MenuButton(Rectangle rect, Image up, Image down, Sound click) {
        super(rect, up, down, click);
        initRect = rect;
    }
    
    @Override
    public void render(GameContainer container, Graphics g){
        setShape(calculateShape(container));
        super.render(container, g);
    }
    
    @Override
    public void update(GameContainer container, int delta){
        setShape(calculateShape(container));
        super.update(container, delta);
    }

    public Shape calculateShape(GameContainer container) {
        if (container.isFullscreen()){
            return new Rectangle(
                    (float) container.getScreenWidth()/Constants.WINDOW_WIDTH * initRect.getX(),
                    (float) container.getScreenHeight()/Constants.WINDOW_HEIGHT * initRect.getY(), WIDTH, HEIGHT);
        }
        
        else {
            return initRect;
        }
    }

}
