package game.model;

import util.Constants;

public class Cavalry extends Piece {

    public Cavalry(Player owner, Point p, int id) {
        super(owner, p, id);
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
