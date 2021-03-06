package game.model;

import org.newdawn.slick.Image;

public abstract class Piece {
    private int currentHealth, currentDirection;
    private Point point;
    public final Player owner;
    public final int id;
    public final PieceType pieceType;

    public static enum TurnState {
        MOVING, DONE;
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
    
    public Piece(Player owner, Point p, int id, PieceType pieceType, int dir) {
        this.owner = owner;
        currentHealth = defaultHealth();
        setPosition(p);
        this.id = id;
        this.pieceType = pieceType;
        this.currentDirection = (dir%6);
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
     * Back attack at current position?
     */
    final public boolean isBackAttack(Piece opponent) {
        boolean ret = isBackAttack(this.getPosition(), opponent);
        System.out.println("isbackattack: "+ret);
        return ret;
    }
    
    /**
     * Back attack at given position?
     * @param p Point to check
     * @param opponent
     * @return
     */
    final public boolean isBackAttack(Point p, Piece opponent){
        int attackDir = -1;
        Point o = opponent.getPosition();
        Point[] ret = Piece.getPointsFromPoint(p, 1);
        
        for (int i = 0; i < ret.length; i++) {
            if (ret[i].equals(opponent.getPosition())) {
                System.out.println("isBackAttack: Matched d=1");
                attackDir = i;
            }
        }
        
        // Ok, check d2 now as well
        ret = Piece.getPointsFromPoint(p, 2);
        for (int i = 0; i < ret.length; i++) {
            if (ret[i].equals(opponent.getPosition())){
                System.out.println("isBackAttack: Matched d=2");
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
     * Return list of valid locations that piece can face
     */
    final public Point[] getValidFacings() {
        return getValidFacings(this.getPosition());
    }
    final public Point[] getValidFacings(Point p){
        return Piece.getPointsFromPoint(p, 1);
    }

    /**
     * Attack the opposing piece
     * 
     * @param opponent
     */
    abstract public void attack(Piece opponent, GameModel.AttackResult result);

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
    public Image getSprite(int direction, int status) {
        return pieceType.getSprite(owner.id, getHealth(), direction, status);
    }
    
    public Image getSprite(){
        return getSprite(getDirection(), this.turnState == TurnState.DONE ? 1 : 0);
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
