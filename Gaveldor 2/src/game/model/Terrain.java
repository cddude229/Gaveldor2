package game.model;

import org.newdawn.slick.Image;


public abstract class Terrain {
    private final Point point;
    private final TerrainType terrainType;
    public Terrain(Point p, TerrainType t)
    {
        this.point=p;
        this.terrainType=t;
    }
    
    public Point getPosition()
    {
        return this.point;
    }
    
    abstract public boolean enterable(PieceType p);
    
    abstract public Image getSprite();
}
