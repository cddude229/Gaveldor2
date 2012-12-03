package game.model;

import util.Constants;

public class Cavalry extends Piece {

    public Cavalry(Player owner, Point p, int id) {
        super(owner, p, id, PieceType.CAVALRY);
    }
    
    public Cavalry(Player owner, Point p, int id, int dir) {
        super(owner, p, id, PieceType.CAVALRY, dir);
    }

    @Override
    public void attack(Piece opponent, GameModel.AttackResult result) {
        int power = this.defaultAttackPower();

//        if (this.isBackAttack(opponent))
//            power *= 2;
        switch (result){
        case HIT:
            break;
        case CRIT:
            power *= 2;
            break;
        case MISS:
            power = 0;
            break;
        }
        
        opponent.loseHealth(power);

    }

    @Override
    public int defaultHealth() {
        return Constants.CAVALRY_HEALTH_POINTS;
    }

    @Override
    public int defaultAttackPower() {
        return Constants.CAVALRY_ATTACK_POWER;
    }

    @Override
    public int defaultAttackRange() {
        return Constants.CAVALRY_ATTACK_RANGE;
    }

    @Override
    public int defaultMoveRange() {
        return Constants.CAVALRY_MOVE_RANGE;
    }

}
