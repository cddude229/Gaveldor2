package util;

import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.aem.sticky.button.Button;
import com.aem.sticky.button.SimpleButton;
import com.aem.sticky.button.events.ClickListener;

public class Helpful {
    // FROM: http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java
    public static <T> T[] arrayConcatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
    
    public static Button makeButton(int x, int y, String text, ClickListener listener){
        int width = 200, height = 50;
        Rectangle shape = new Rectangle(x - width / 2, y - height / 2, width, height);
        try{
            Image up = new Image(width, height);
            Image down = up.copy();
            up.getGraphics().setColor(Color.blue);
            down.getGraphics().setColor(Color.green);
            for (Image im : new Image[]{up, down}){
                im.getGraphics().fillRect(0, 0, width, height);
                im.getGraphics().setColor(Color.white);
                im.getGraphics().drawString(text, 0, 0);
                im.getGraphics().flush();
            }
            SimpleButton b = new SimpleButton(shape, up, down, null);
            if (listener != null){
                b.addListener(listener);
            }
            return b;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}