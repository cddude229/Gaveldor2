package game.model;

final public class Point {
    public final int x, y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Point){
            Point p = (Point) o;
            return p.x == this.x && p.y == this.y;
        }
        return o.hashCode() == this.hashCode();
    }
    
    @Override
    public int hashCode(){
        return 10000*y + x;
    }
}