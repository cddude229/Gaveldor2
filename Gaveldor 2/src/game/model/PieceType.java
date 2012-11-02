package game.model;

import org.newdawn.slick.Image;

import util.Constants;
import util.Resources;

public enum PieceType {
    INFANTRY(Constants.INFANTRY_HEALTH_POINTS),
    ARCHER(Constants.ARCHER_HEALTH_POINTS),
    CAVALRY(Constants.CAVALRY_HEALTH_POINTS),
    ;
    
    
    public final int defaultHealth;
    private final Image[][][] sprites;
    private PieceType(int defaultHealth){
        this.defaultHealth = defaultHealth;
        sprites = new Image[2][defaultHealth][6];
    }
    
    public static void initSprites(){
        int[] m = new int[]{1, 2, 5, 4, 3, 0};
        for (PieceType type : PieceType.values()){
            for (int player : new int[]{1, 2}){
                for (int health = 1; health <= type.defaultHealth; health++){
                    for (int direction  = 0; direction < 6; direction++){
                        String name = type.name().toLowerCase();
                        String ref = "/assets/graphics/units/" + name + "/" + name
                                + "_p" + player + "_h" + health + "_d" + (m[direction] % 3) + ".png";
                        Image im = Resources.getImage(ref).getScaledCopy(.5f);
                        if (m[direction] >= 3){
                            im = im.getFlippedCopy(true, false);
                        }
                        type.sprites[player - 1][health - 1][direction] = im;
                    }
                }
            }
        }
    }
    
    public Image getSprite(int player, int health, int direction){
        return sprites[player - 1][health - 1][direction];
    }
}
