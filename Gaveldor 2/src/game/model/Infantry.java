package game.model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Constants;
import util.Resources;

public class Infantry extends Piece{
    private int health, attackPower, attackRange, moveRange;
    public Infantry(Player owner, Point p) {
        super(owner, p);
        this.health = Constants.INFANTRY_HEALTH_POINTS;
        this.attackPower = Constants.INFANTRY_ATTACK_POWER;
        this.attackRange = Constants.INFANTRY_ATTACK_RANGE;
        this.moveRange = Constants.INFANTRY_MOVE_RANGE;
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
        return this.attackRange;
    }

    @Override
    public int defaultMoveRange() {
        return this.moveRange;
    }

    @Override
    public Image getSprite() {
        try {
            return Resources.getImage("/assets/graphics/test_infantry.jpg");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }
}