package game.model;

public abstract class Piece {
    private int currentHealth, x, y, currentDirection;
    private final Player owner;
    
    /**
     * Only pass in the default position.
     * @param x
     * @param y
     */
    public Piece(Player owner, int x, int y){
        this.owner = owner;
        currentHealth = defaultHealth();
        setPosition(x, y);
    }
    
    /**
     * Remaining health of the piece. 0 if dead.
     * @return
     */
    final public int getHealth(){
        return currentHealth;
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
    final public int[] getPosition(){
        return new int[]{ x, y };
    }
    
    /**
     * Update the piece's position
     * @param x
     * @param y
     */
    final public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Can the piece move to this spot?
     * @param x
     * @param y
     * @return
     */
    final public boolean isValidMove(int x, int y){
        // TODO
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Return a list of all the pieces valid moves
     * @return
     */
    final public int[][] getValidMoves(){
        // TODO
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Can the piece attack this spot, if a unit is there?
     * @param x
     * @param y
     * @return
     */
    final public boolean isValidAttack(int x, int y){
        // TODO
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Return the list of where they can attack, if opponents are there
     * @return
     */
    final public int[][] getValidAttacks(){
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
    abstract public String getSprite();
    // TODO: Todd, what's the sprite we want to return?
    

}
