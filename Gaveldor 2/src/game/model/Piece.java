package game.model;

import org.newdawn.slick.Image;

import util.Helpful;

public abstract class Piece {
    private int currentHealth, currentDirection;
    private Point point;
    public final Player owner;
    public final int id;
    public final PieceType pieceType;

    public static enum TurnState {
        MOVING, ATTACKING, DONE;
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
            Point[] ret = Piece.getPointsFromPoint(p, 1);

            for (int i = 0; i < ret.length; i++) {
                if (ret[i].equals(opponent.getPosition())) {
                    attackDir = i;
                }
            }
        } else {
            Point[] ret = Piece.getPointsFromPoint(p, 2);
            for (int i = 0; i < ret.length; i++) {
                if (ret[i].equals(opponent.getPosition()))
                    attackDir = i / 2;
            }
        }

        int oppDir = opponent.getDirection();
        return attackDir == oppDir || attackDir == ((oppDir + 1) % 6) || attackDir == ((oppDir - 1 + 6) % 6);
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
     *            Must be in range 0-5 (0 = east, go clockwise)
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
            return Helpful.arrayConcatAll(
                    Piece.getPointsFromPoint(p, 1),
                    Piece.getPointsFromPoint(p, 0)
            );
        case 2:
            return Helpful.arrayConcatAll(
                    Piece.getPointsFromPoint(p, 2),
                    Piece.getPointsFromPoint(p, 1),
                    Piece.getPointsFromPoint(p, 0)
            );
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
        return Piece.getPointsFromPoint(this.point, 1);
    }

    /**
     * Return the list of where they can attack, if opponents are there
     * 
     * @return
     */
    final public Point[] getValidAttacks(Point p, int dir) {
        Point[] ret;
        switch (defaultAttackRange()) {
        case 1:
            ret = Piece.getPointsFromPoint(p, 1);
            return new Point[] {
                    ret[dir],
                    ret[(dir + 1 + 6) % 6],
                    ret[(dir - 1 + 6) % 6]
            };
        case 2:
            ret = Helpful.arrayConcatAll(
                    Piece.getPointsFromPoint(p, 2),
                    Piece.getPointsFromPoint(p, 1)
            );
            return new Point[] {
                    ret[(dir * 2) % 12], //
                    // We need to shift by + 12 first because (-2 % 12) == -2
                    ret[(dir * 2 - 2 + 12) % 12], //
                    ret[(dir * 2 - 1 + 12) % 12],//
                    ret[(dir * 2 + 2) % 12],//
                    ret[(dir * 2 + 1) % 12],//
                    // Dist = 1 attacks
                    ret[dir + 12],//
                    ret[(dir + 1) % 6 + 12],//
                    ret[(dir - 1 + 6) % 6 + 12] //
            };
        default:
            throw new RuntimeException("Not yet implemented for d >= 3");
        }
    }
    
    public final Point[] getValidAttacks(){
        return getValidAttacks(this.getPosition(), this.getDirection());
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
    public int defaultHealth(){
        return pieceType.defaultHealth;
    }

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
    public Image getSprite(int direction) {
        return pieceType.getSprite(owner.id, getHealth(), direction);
    }
    
    public Image getSprite(){
        return getSprite(getDirection());
    }

    public static int pointsToDirection(Point to, Point from) {
        // NOTE: Only takes into account distance = 1
        // Mostly used for facing direction
    
        int dx = to.x - from.x, dy = to.y - from.y;
        int direction = -1;
        switch (dy) {
        case -1:
            switch (dx) {
            case -1:
                direction = 4;
                break;
            case 1:
                direction = 5;
            }
            break;
        case 0:
            switch (dx) {
            case -2:
                direction = 3;
                break;
            case 2:
                direction = 0;
            }
            break;
        case 1:
            switch (dx) {
            case -1:
                direction = 2;
                break;
            case 1:
                direction = 1;
            }
            break;
        }
        return direction;
    }
    
    /**
     * Given a point and dist d, return all points dist d from point
     * @param p
     * @param dist
     * @return List of points. No guarantee that they are valid.  They will be in order (0-5)
     */
    public static Point[] getPointsFromPoint(Point p, int dist){
        if(dist == 0){
            return new Point[]{
                p
            };
        } else if(dist == 1){
            return new Point[]{
                new Point(p.x + 2, p.y), // 0
                new Point(p.x + 1, p.y + 1), // 1
                new Point(p.x - 1, p.y + 1), // 2
                new Point(p.x - 2, p.y), // 3
                new Point(p.x - 1, p.y - 1), // 4
                new Point(p.x + 1, p.y - 1) // 5
            };
        } else if(dist == 2){
            return new Point[]{
                new Point(p.x + 4, p.y), // 0
                new Point(p.x + 3, p.y + 1), // 0.5
                new Point(p.x + 2, p.y + 2), // 1
                new Point(p.x, p.y + 2), // 1.5
                new Point(p.x - 2, p.y + 2), // 2
                new Point(p.x - 3, p.y + 1), // 2.5
                new Point(p.x - 4, p.y), // 3
                new Point(p.x - 3, p.y - 1), // 3.5
                new Point(p.x - 2, p.y - 2), // 4
                new Point(p.x, p.y - 2), // 4.5
                new Point(p.x + 2, p.y - 2), // 5
                new Point(p.x + 3, p.y - 1) // 5.5
            };
        }
        throw new RuntimeException("Piece.getPointsFromPoint() not yet implemented for d >= 3");
    }
}
