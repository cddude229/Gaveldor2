package game.model;


public class MinigameModel{
    public final Piece attackingPiece, defendingPiece;

    public static enum Move {
        HIGH, MID, LOW, NONE;
    }
    public Move attackingMove = null, defendingMove = null;
    
    public long moveTime = 0;
    
    public MinigameModel(Piece attacking, Piece defending){
        attackingPiece = attacking;
        defendingPiece = defending;
    }
}
