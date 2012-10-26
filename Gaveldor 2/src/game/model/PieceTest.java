package game.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PieceTest {
    public class BackAttack {
        public final Point piece2Point;
        public final int[] piece1Dirs;
        public final int[] piece2DirsValid;
        public final int[] piece2DirsInvalid;

        public BackAttack(int[] piece1Dirs, Point piece2Point, int[] piece2DirsValid, int[] piece2DirsInvalid) {
            this.piece1Dirs = piece1Dirs;
            this.piece2Point = piece2Point;
            this.piece2DirsValid = piece2DirsValid;
            this.piece2DirsInvalid = piece2DirsInvalid;
        }
    }

    @Test
    public void backAttackCheck() {
        Player player1 = new Player(1);
        Point piece1InitialPoint = new Point(2, 4);
        Piece piece1 = new Archer(player1, piece1InitialPoint, 0);

        BackAttack[] backAttacksToCheck = new BackAttack[] {
                // Create 'em
                // Check them at 2,2
                new BackAttack(new int[] { 5, 0, 1 }, new Point(2, 2), new int[] { 5, 0, 1 }, new int[] { 2, 3, 4 }),
                // Check them at 1,3
                new BackAttack(new int[] { 5, 0, 1 }, new Point(1, 3), new int[] { 4, 5, 0 }, new int[] { 1, 2, 3 }),
                // Check them at 1,5
                new BackAttack(new int[] { 5, 0, 1 }, new Point(3, 3), new int[] { 0, 1, 2 }, new int[] { 3, 4, 5 })
        // Done
        };

        // CHECK FOR VALID BACK ATTACKS
        Piece piece2;
        String str;
        for (BackAttack ba : backAttacksToCheck) {
            for (int j : ba.piece1Dirs) {
                piece1.setDirection(j);
                piece2 = new Archer(player1, ba.piece2Point, 1);
                for (int i : ba.piece2DirsValid) {
                    piece2.setDirection(i);
                    str = String.format("Should back attack piece1Dir: %d, piece2Dir: %d, piece2Point: %s", j, i,
                            ba.piece2Point);
                    assertTrue(str, piece1.isBackAttack(piece2));
                }
                for (int i : ba.piece2DirsInvalid) {
                    piece2.setDirection(i);
                    str = String.format("Should NOT back attack piece1Dir: %d, piece2Dir: %d, piece2Point: %s", j, i,
                            ba.piece2Point);
                    assertTrue(str, piece1.isBackAttack(piece2) == false);
                }
            }
        }
    }
}
