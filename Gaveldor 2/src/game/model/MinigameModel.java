package game.model;

@Deprecated
public class MinigameModel{
    public final Piece attackingPiece, defendingPiece;
    
    public final boolean backAttack;
    public final Move bonusMove;

    public static enum Move {
        HIGH, MID, LOW, NONE;
    }
    public Move attackingMove = null, defendingMove = null;
    
    public long sinceMoveTimeStart = 0;
    
    public long sinceHasBothMoves = 0;
    
    public MinigameModel(Piece attacking, Piece defending, boolean backAttack, Move bonusMove){
        attackingPiece = attacking;
        defendingPiece = defending;
        this.backAttack = backAttack;
        this.bonusMove = bonusMove;
        if (backAttack){
            defendingMove = Move.NONE;
        }
    }
    
    public boolean hasBothMoves(){
        return attackingMove != null && defendingMove != null;
    }
    
    public boolean isSuccessfulAttack(){
        return hasBothMoves() && attackingMove != Move.NONE && attackingMove != defendingMove;
    }
}
