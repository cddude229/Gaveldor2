package game.model;

import org.newdawn.slick.Image;

public class Infantry extends Piece{
    private int health, attackPower, attackRange, moveRange;
    public Infantry(Player owner, Point p) {
        super(owner, p);
        this.health=3;
        this.attackPower=1;
        this.attackRange=1;
        this.moveRange=1;
    }

    @Override
    public void attack(Piece opponent) {
        int power = this.defaultAttackPower();
        if (this.isBackAttack(opponent))
            power*=2;
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
        // TODO Auto-generated method stub
        return this.attackRange;
    }

    @Override
    public int defaultMoveRange() {
        return this.moveRange;
    }

    @Override
    public Image getSprite() {
        return null;
    }

}
