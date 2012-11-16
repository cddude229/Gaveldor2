package util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;

import com.aem.sticky.button.SimpleButton;

public abstract class LayoutButton extends SimpleButton {

    public LayoutButton(Image up, Image down, Sound click) {
        super(null, up, down, click);
    }
    
    public abstract Shape calculateShape(GameContainer container);

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
}
