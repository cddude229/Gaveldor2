package game.model;

import org.newdawn.slick.Image;

public abstract class Piece {
    private int currentHealth, currentDirection;
    private Point point;
    private final Player owner;
    
    /**
     * Only pass in the default position.
     * @param x
     * @param y
     */
    public Piece(Player owner, Point p){
        this.owner = owner;
        currentHealth = defaultHealth();
        setPosition(p);
    }
    
    /**
     * Remaining health of the piece. 0 if dead.
     * @return
     */
    final public int getHealth(){
        return currentHealth;
    }
    
    /**
     * Return whether or not a back attack
     */
    final public boolean isBackAttack(Piece opponent){
        int attackDir=-1;
        Point p = this.getPosition();
        Point [] ret = {new Point(p.x,p.y-2), new Point(
                p.x+1,p.y-1), new Point(p.x+1,p.y+1),
                new Point(p.x,p.y+2), new Point(p.x-1,p.y+1),
                new Point(p.x-1,p.y-1)};
        for(int i=0;i<ret.length;i++)
        {
            if(ret[i].equals(opponent.getPosition()))
                attackDir=i;
        }
        int oppDir=opponent.getDirection();
        if (attackDir==oppDir || attackDir==(oppDir+1)%6 || attackDir==(oppDir-1)%6)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Remove health i from the piece.  Min out at 0.
     * @param i
     * @return
     */
    final public int loseHealth(int i){
        currentHealth -= i;
        if(currentHealth < 0){
            currentHealth = 0;
        }
        return currentHealth;
    }
    
    /**
     * Current direction of piece
     * @return
     */
    final public int getDirection(){
        return currentDirection;
    }
    
    /**
     * Set the new direction.
     * @param newDir Must be in range 0-5 (0 = north, go clockwise)
     */
    final public void setDirection(int newDir){
        assert(newDir >= 0 && newDir < 6);
        currentDirection = newDir;
    }
    
    /**
     * Is the piece still alive?
     * @return
     */
    final public boolean isAlive(){
        return currentHealth > 0;
    }
    
    /**
     * Where is the piece currently?
     * @return
     */
    final public Point getPosition(){
        return point;
    }
    
    /**
     * Update the piece's position
     * @param p
     */
    final public void setPosition(Point p){
        this.point = p;
    }
    
    /**
     * Can the piece move to this spot?
     * @param p
     * @return
     */
    final public boolean isValidMove(Point p){
        for(Point p2 : getValidMoves()){
            if(p.equals(p2)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return a list of all the pieces valid moves
     * @return
     */
    final public Point[] getValidMoves(){
        // TODO
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Can the piece attack this spot, if a unit is there?
     * @param p
     * @return
     */
    final public boolean isValidAttack(Point p){
        for(Point p2 : getValidAttacks()){
            if(p.equals(p2)){
                return true;
            }
        }
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Return the list of where they can attack, if opponents are there
     * @return
     */
    final public Point[] getValidAttacks(){
        // TODO
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Attack the opposing piece
     * @param opponent
     */
    abstract public void attack(Piece opponent);
    
    /**
     * What's their default health?
     * @return
     */
    abstract public int defaultHealth();
    
    /**
     * What's their default attack power?
     * @return
     */
    abstract public int defaultAttackPower();
    
    /**
     * What's their normal attack range?
     * @return
     */
    abstract public int defaultAttackRange();
    
    /**
     * What's their normal movement range?
     * @return
     */
    abstract public int defaultMoveRange();
    
    /**
     * Return the path to this piece's sprite
     * @return
     */
    abstract public Image getSprite();
    // TODO: Todd, what's the sprite we want to return?
}
