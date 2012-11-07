package game.model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import util.Constants;

public class Archer extends Piece {

    public Archer(Player owner, Point p, int id) {
        super(owner, p, id, PieceType.ARCHER);
    }

    @Override
    public void attack(Piece opponent) {
        Sound fx;
        try {
            fx = new Sound("/assets/audio/effects/bow3.ogg");
            fx.play();
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int power = this.defaultAttackPower();

        // Do double damage for back attack
        if (this.isBackAttack(opponent))
            power *= 2;

        // Archers do double damage to cavalry
        if (opponent.pieceType == PieceType.CAVALRY) {
            power *= 2;
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
