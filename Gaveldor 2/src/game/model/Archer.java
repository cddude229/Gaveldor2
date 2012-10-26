package game.model;

import util.Constants;

public class Archer extends Piece {

    public Archer(Player owner, Point p, int id) {
        super(owner, p, id, PieceType.ARCHER);
    }

    @Override
    public void attack(Piece opponent) {
        int power = this.defaultAttackPower();

        if (this.isBackAttack(opponent))
            power *= 2;

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
