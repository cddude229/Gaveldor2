package game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import util.Resources;

public class PieceLoader {

    public static Set<Piece> loadPieces(String name, int mapWidth, int mapHeight) throws IOException{
        Player p1 = new Player(1), p2 = new Player(2);
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(name + ".pieces")));
        try{

            Set<Piece> pieces = new HashSet<Piece> ();
            
            String rowLine;

            for (int j = 0; ((rowLine = reader.readLine()) != null) && (j < mapHeight); j++){
                
                rowLine = rowLine.replaceAll("\\s", "");    // remove spaces
                
                // make player one piece tokens into single char
                rowLine = rowLine.replaceAll("1i", "1");
                rowLine = rowLine.replaceAll("1a", "2");
                rowLine = rowLine.replaceAll("1c", "3");
                
                // same for player two
                rowLine = rowLine.replaceAll("2i", "a");
                rowLine = rowLine.replaceAll("2a", "b");
                rowLine = rowLine.replaceAll("2c", "c");
                
                
                
                for( int i=0; (i < mapWidth) && (i < rowLine.length()); i++){
                    int x = i * 2 + j % 2;
                    int y = j;
                    switch(rowLine.charAt(i)){
                    
                    case '1' :
                        
                        pieces.add(new Infantry(p1, new Point(x, y)));
                        break;
                    case '2' :
                        // pieces.put(new Archer(p1, new Point(i,j)), new Point(i,j));
                        break;
                    case '3' :
                        // pieces.put(new Cavalry(p1, new Point(i,j)), new Point(i,j));
                        break;
                    case 'a' :
                        
                        pieces.add(new Infantry(p2, new Point(x, y)));
                        break;
                    case 'b' :
                        // pieces.put(new Archer(p2, new Point(i,j)), new Point(i,j));
                        break;
                    case 'c' :
                        // pieces.put(new Cavalry(p1, new Point(i,j)), new Point(i,j));
                        break;
                    }
                }                           
            }
            return pieces;
            
            
        } finally{
            reader.close();
        }
    }
}
