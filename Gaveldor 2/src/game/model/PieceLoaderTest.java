package game.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class PieceLoaderTest {

    @Test
    public void basicTest(){
        try {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        int MAP_HEIGHT = 5;
        int MAP_WIDTH = 5;
        
        
        ArrayList<Piece> pieces = PieceLoader.loadPieces("/assets/pieces/default.map", MAP_WIDTH, MAP_HEIGHT, player1, player2);
    
        ArrayList<Point> outputPos = new ArrayList<Point> ();
        for( Piece p : pieces){
            outputPos.add(p.getPosition());
        }
        
        Point[] expected = {new Point(0,0), new Point(1,0), new Point(1,3)};
        
        assertArrayEquals(expected, outputPos.toArray());
        
        } catch (IOException e) { fail(); }
        
        
    }
}
