package game.model;


public class GameModel {
    
    //TODO
    
    private final Player player1, player2;
    private boolean player1IsCurrent = true;
    
    public GameModel(){
        player1 = new Player(1);
        player2 = new Player(2);
    }
    
    public Player getCurrentPlayer(){
        return player1IsCurrent ? player1 : player2;
    }
    
    public Player getOtherPlayer(){
        return player1IsCurrent ? player2 : player1;
    }
    
    private void switchCurrentAndOtherPlayers(){
        player1IsCurrent ^= true;
    }
    
    public void applyAction(Action action){
        //TODO: handle various action subclasses
    }

}
