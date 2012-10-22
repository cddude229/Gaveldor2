package game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import util.Resources;

public class PieceLoader {

    public static ArrayList<Piece> loadPieces(String fileName, int mapWidth, int mapHeight, Player p1, Player p2) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(fileName)));
        try{

            ArrayList<Piece> pieces = new ArrayList<Piece> ();
            
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
                    switch(rowLine.charAt(i)){
                    case 1 :
                        pieces.add(new Infantry(p1, new Point(i,j)));
                        break;
                    case 2 :
                        // pieces.put(new Archer(p1, new Point(i,j)), new Point(i,j));
                        break;
                    case 3 :
                        // pieces.put(new Cavalry(p1, new Point(i,j)), new Point(i,j));
                        break;
                    case 'a' :
                        pieces.add(new Infantry(p2, new Point(i,j)));
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
