package game.model;

import org.newdawn.slick.Image;

import util.Constants;

public class Archer extends Piece {
    private int health, attackPower, attackRange, moveRange;

    public Archer(Player owner, Point p, int id) {
        super(owner, p, id);
        this.health = Constants.ARCHER_HEALTH_POINTS;
        this.attackPower = Constants.ARCHER_ATTACK_POWER;
        this.attackRange = Constants.ARCHER_ATTACK_RANGE;
        this.moveRange = Constants.ARCHER_MOVE_RANGE;
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
        return this.health;
    }

    @Override
    public int defaultAttackPower() {
        return this.attackPower;
    }

    @Override
    public int defaultAttackRange() {
        return this.attackRange;
    }

    @Override
    public int defaultMoveRange() {
        return this.moveRange;
    }

    @Override
    public Image getSprite() {
        // TODO Auto-generated method stub
        return null;
    }

}
