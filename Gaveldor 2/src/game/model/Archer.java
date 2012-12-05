package game.model;

import util.Constants;

public class Archer extends Piece {

    public Archer(Player owner, Point p, int id) {
        super(owner, p, id, PieceType.ARCHER);
    }
    
    public Archer(Player owner, Point p, int id, int dir) {
        super(owner, p, id, PieceType.ARCHER, dir);
    }

    @Override
    public void attack(Piece opponent, GameModel.AttackResult result) {
        int power = this.defaultAttackPower();

//        // Do double damage for back attack
//        if (this.isBackAttack(opponent))
//            power *= 2;

        // Archers do double damage to cavalry
        if (opponent.pieceType == PieceType.CAVALRY && Constants.ARCHER_2X_VS_CAVALRY) {
            power *= 2;
        }
        switch (result){
        case STRIKE:
            break;
        case CRITICAL:
            power *= 2;
            break;
        case BLOCKED:
            power = 0;
            break;
        }

        opponent.loseHealth(power);

    }

    @Override
    public int defaultHealth() {
        return Constants.ARCHER_HEALTH_POINTS;
    }

    @Override
    public int defaultAttackPower() {
        return Constants.ARCHER_ATTACK_POWER;
    }

    @Override
    public int defaultAttackRange() {
        return Constants.ARCHER_ATTACK_RANGE;
    }

    @Override
    public int defaultMoveRange() {
        return Constants.ARCHER_MOVE_RANGE;
    }

}
