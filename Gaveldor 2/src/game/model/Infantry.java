package game.model;

import util.Constants;

public class Infantry extends Piece {
    public Infantry(Player owner, Point p, int id) {
        super(owner, p, id, PieceType.INFANTRY);
    }
    
    public Infantry(Player owner, Point p, int id, int dir) {
        super(owner, p, id, PieceType.INFANTRY, dir);
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
        return Constants.INFANTRY_HEALTH_POINTS;
    }

    @Override
    public int defaultAttackPower() {
        return Constants.INFANTRY_ATTACK_POWER;
    }

    @Override
    public int defaultAttackRange() {
        return Constants.INFANTRY_ATTACK_RANGE;
    }

    @Override
    public int defaultMoveRange() {
        return Constants.INFANTRY_MOVE_RANGE;
    }
}