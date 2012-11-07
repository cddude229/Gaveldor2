package game.model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import util.Constants;

public class Infantry extends Piece {
    public Infantry(Player owner, Point p, int id) {
        super(owner, p, id, PieceType.INFANTRY);
    }

    @Override
    public void attack(Piece opponent) {
        Sound fx;
        try {
            fx = new Sound("/assets/audio/effects/sword.ogg");
            fx.play();
        } catch (SlickException e) {
            // TODO: move sound loading to PieceType initialization
            e.printStackTrace();
        }
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