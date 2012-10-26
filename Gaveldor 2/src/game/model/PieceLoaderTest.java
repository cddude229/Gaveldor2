package game.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PieceLoaderTest {

    @Test
    public void basicTest(){
        try {
//        Player player1 = new Player(1);
//        Player player2 = new Player(2);

        int MAP_WIDTH = 8;
        int MAP_HEIGHT = 8;
        
        
        Set<Piece> pieces = PieceLoader.loadPieces("/assets/maps/basic", MAP_WIDTH, MAP_HEIGHT);
    
        Set<Point> outputPos = new HashSet<Point> ();
        for( Piece p : pieces){
            outputPos.add(p.getPosition());
        }
        
        Set<Point> expected = new HashSet<Point>(
                Arrays.asList(new Point[]{new Point(0,0), new Point(2,0), new Point(5,7), new Point(7, 7)}));
        
        assertEquals(expected, outputPos);
        
        } catch (IOException e) { fail(); }
        
        
    }
}
