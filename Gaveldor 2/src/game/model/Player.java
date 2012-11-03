package game.model;

public class Player {
    
    public final int id;
    
    public Player(int id){
        this.id = id;
    }
    
    public String toString(){
        return "Player " + id;
    }
}
