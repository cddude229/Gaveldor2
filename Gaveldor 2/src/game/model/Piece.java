package game.model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import util.Resources;

public abstract class Piece {
    private int currentHealth, currentDirection;
    private Point point;
    public final Player owner;
    public final int id;
    public final PieceType pieceType;

    public static enum TurnState {
        MOVING, TURNING, ATTACKING, DONE;
    }

    public static enum PieceType {
        ARCHER, CAVALRY, INFANTRY
    }

    public TurnState turnState = TurnState.MOVING;

    /**
     * Only pass in the default position.
     * 
     * @param x
     * @param y
     */
    public Piece(Player owner, Point p, int id, PieceType pieceType) {
        this.owner = owner;
        currentHealth = defaultHealth();
        setPosition(p);
        // pieceId = idCounter++;
        this.id = id;
        this.pieceType = pieceType;
    }

    /**
     * Remaining health of the piece. 0 if dead.
     * 
     * @return
     */
    final public int getHealth() {
        return currentHealth;
    }

    /**
     * Return whether or not a back attack
     */
    final public boolean isBackAttack(Piece opponent) {
        int attackDir = -1;
        Point p = this.getPosition();
        Point o = opponent.getPosition();
        if (Math.abs(p.x - o.x) + Math.abs(p.y - o.y) == 2 || !(Math.abs(p.x - o.x) == 2 && p.y == o.y)) {
            Point[] ret = { new Point(p.x, p.y - 2), // 0
                    new Point(p.x + 1, p.y - 1), // 1
                    new Point(p.x + 1, p.y + 1), // 2
                    new Point(p.x, p.y + 2), // 3
                    new Point(p.x - 1, p.y + 1), // 4
                    new Point(p.x - 1, p.y - 1) // 5
            };

            for (int i = 0; i < ret.length; i++) {
                if (ret[i].equals(opponent.getPosition())) {
                    attackDir = i;
                }
            }
        } else {
            Point[] ret = { new Point(p.x, p.y - 4), // 0
                    new Point(p.x + 1, p.y - 3), // .5
                    new Point(p.x + 2, p.y - 2), // 1
                    new Point(p.x + 2, p.y), // 1.5
                    new Point(p.x + 2, p.y + 2), // 2
                    new Point(p.x + 1, p.y + 3), // 2.5
                    new Point(p.x, p.y + 4), // 3
                    new Point(p.x - 1, p.y + 3), // 3.5
                    new Point(p.x - 2, p.y + 2), // 4
                    new Point(p.x - 2, p.y), // 5
                    new Point(p.x - 2, p.y - 2), // 5.5
                    new Point(p.x - 1, p.y - 3), // 6
            };
            for (int i = 0; i < ret.length; i++) {
                if (ret[i].equals(opponent.getPosition()))
                    attackDir = i / 2;
            }
        }

        int oppDir = opponent.getDirection();
        return attackDir == oppDir || attackDir == ((oppDir + 1) % 6) || attackDir == ((oppDir - 1) % 6);
    }

    /**
     * Remove health i from the piece. Min out at 0.
     * 
     * @param i
     * @return
     */
    final public int loseHealth(int i) {
        currentHealth -= i;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
        return currentHealth;
    }

    /**
     * Current direction of piece
     * 
     * @return
     */
    final public int getDirection() {
        return currentDirection;
    }

    /**
     * Set the new direction.
     * 
     * @param newDir
     *            Must be in range 0-5 (0 = north, go clockwise)
     */
    final public void setDirection(int newDir) {
        assert (newDir >= 0 && newDir < 6);
        currentDirection = newDir;
    }

    /**
     * Is the piece still alive?
     * 
     * @return
     */
    final public boolean isAlive() {
        return currentHealth > 0;
    }

    /**
     * Where is the piece currently?
     * 
     * @return
     */
    final public Point getPosition() {
        return point;
    }

    /**
     * Update the piece's position
     * 
     * @param p
     */
    final public void setPosition(Point p) {
        this.point = p;
    }

    /**
     * Can the piece move to this spot?
     * 
     * @param p
     * @return
     */
    final public boolean isValidMove(Point p) {
        for (Point p2 : getValidMoves()) {
            if (p.equals(p2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a list of all the pieces valid moves
     * 
     * @return
     */
    final public Point[] getValidMoves() {
        Point p = this.getPosition();
        switch (defaultMoveRange()) {
        case 1:
            return new Point[] { new Point(p.x, p.y - 2), new Point(p.x + 1, p.y - 1), new Point(p.x + 1, p.y + 1),
                    new Point(p.x, p.y + 2), new Point(p.x - 1, p.y + 1), new Point(p.x - 1, p.y - 1),
                    new Point(p.x, p.y) };
        case 2:
            return new Point[] {
                    new Point(p.x, p.y - 4), // 0
                    new Point(p.x + 1, p.y - 3), // .5
                    new Point(p.x + 2, p.y - 2), // 1
                    new Point(p.x + 2, p.y), // 1.5
                    new Point(p.x + 2, p.y + 2), // 2
                    new Point(p.x + 1, p.y + 3), // 2.5
                    new Point(p.x, p.y + 4), // 3
                    new Point(p.x - 1, p.y + 3), // 3.5
                    new Point(p.x - 2, p.y + 2), // 4
                    new Point(p.x - 2, p.y), // 5
                    new Point(p.x - 2, p.y - 2), // 5.5
                    new Point(p.x - 1, p.y - 3), // 6
                    new Point(p.x, p.y - 2), // move length 1
                    new Point(p.x + 1, p.y - 1), new Point(p.x + 1, p.y + 1), new Point(p.x, p.y + 2),
                    new Point(p.x - 1, p.y + 1), new Point(p.x - 1, p.y - 1), new Point(p.x, p.y) };
        default:
            throw new RuntimeException("Support for move dist>2 currently not supported");
        }
    }

    /**
     * Can the piece attack this spot, if a unit is there?
     * 
     * @param p
     * @return
     */
    final public boolean isValidAttack(Point p) {
        for (Point p2 : getValidAttacks()) {
            if (p.equals(p2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return list of valid locations that piece can face
     */
    final public Point[] getValidFacings() {
        Point p = this.point;
        return new Point[] { new Point(p.x, p.y - 2), new Point(p.x + 1, p.y - 1), new Point(p.x + 1, p.y + 1),
                new Point(p.x, p.y + 2), new Point(p.x - 1, p.y + 1), new Point(p.x - 1, p.y - 1), new Point(p.x, p.y) };
    }

    /**
     * Return the list of where they can attack, if opponents are there
     * 
     * @return
     */
    final public Point[] getValidAttacks() {
        // TODO: This only takes into account dist=1, not dist=2.
        Point p = this.getPosition();
        int dir = this.getDirection();
        if (defaultAttackRange() == 1) {
            Point[] ret = new Point[] { new Point(p.x, p.y - 2), new Point(p.x + 1, p.y - 1),
                    new Point(p.x + 1, p.y + 1), new Point(p.x, p.y + 2), new Point(p.x - 1, p.y + 1),
                    new Point(p.x - 1, p.y - 1), new Point(p.x, p.y) };
            return new Point[] { ret[dir], ret[(dir + 1 + 6) % 6], ret[(dir - 1 + 6) % 6] };
        } else {
            Point[] ret = new Point[] {
                    new Point(p.x, p.y - 4), // 0
                    new Point(p.x + 1, p.y - 3), // .5
                    new Point(p.x + 2, p.y - 2), // 1
                    new Point(p.x + 2, p.y), // 1.5
                    new Point(p.x + 2, p.y + 2), // 2
                    new Point(p.x + 1, p.y + 3), // 2.5
                    new Point(p.x, p.y + 4), // 3
                    new Point(p.x - 1, p.y + 3), // 3.5
                    new Point(p.x - 2, p.y + 2), // 4
                    new Point(p.x - 2, p.y), // 5
                    new Point(p.x - 2, p.y - 2), // 5.5
                    new Point(p.x - 1, p.y - 3), // 6
                    new Point(p.x, p.y - 2), // move length 1
                    new Point(p.x + 1, p.y - 1), new Point(p.x + 1, p.y + 1), new Point(p.x, p.y + 2),
                    new Point(p.x - 1, p.y + 1), new Point(p.x - 1, p.y - 1), new Point(p.x, p.y) };
            return new Point[] { ret[(dir * 2) % 12], ret[(dir * 2 - 2) % 12], ret[(dir * 2 - 1) % 12],
                    ret[(dir * 2 + 2) % 12], ret[(dir * 2 + 1) % 12], ret[dir + 12], ret[(dir + 1) % 6 + 12],
                    ret[(dir - 1) % 6 + 12] };
        }
    }

    /**
     * Attack the opposing piece
     * 
     * @param opponent
     */
    abstract public void attack(Piece opponent);

    /**
     * What's their default health?
     * 
     * @return
     */
    abstract public int defaultHealth();

    /**
     * What's their default attack power?
     * 
     * @return
     */
    abstract public int defaultAttackPower();

    /**
     * What's their normal attack range?
     * 
     * @return
     */
    abstract public int defaultAttackRange();

    /**
     * What's their normal movement range?
     * 
     * @return
     */
    abstract public int defaultMoveRange();

    /**
     * Return this piece's sprite
     * 
     * @return
     */
    public Image getSprite() {
        String name = "/assets/graphics/units/player" + owner.id + "/" + getClass().getSimpleName().toLowerCase()
                + "_p" + owner.id + "_h" + getHealth() + ".png";
        try {
            Image im = Resources.getImage(name);
            im.rotate(360f / 6 * getDirection());
            return im;
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public static int PointsToDirection(Point to, Point from) {
        int dx = to.x - from.x, dy = to.y - from.y;
        int direction = -1;
        switch (dx) {
        case -1:
            switch (dy) {
            case -1:
                direction = 5;
                break;
            case 1:
                direction = 4;
            }
            break;
        case 0:
            switch (dy) {
            case -2:
                direction = 0;
                break;
            case 2:
                direction = 3;
            }
            break;
        case 1:
            switch (dy) {
            case -1:
                direction = 1;
                break;
            case 1:
                direction = 2;
            }
            break;
        }
        return direction;
    }

}
