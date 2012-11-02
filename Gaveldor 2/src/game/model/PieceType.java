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
    
//    private String getSpriteRef(int player, int health){
//        return "/assets/graphics/units/player" + player + "/" + name().toLowerCase()
//                + "_p" + player + "_h" + health + ".png";
//    }
    
    public static void initSprites(){
        for (PieceType type : PieceType.values()){
            for (int player : new int[]{1, 2}){
                for (int health = 1; health <= type.defaultHealth; health++){
                    for (int direction = 0; direction < 6; direction++){
                        Image im;
                        String name = type.name().toLowerCase();
                        String ref = "/assets/graphics/units/" + name + "/" + name
                                + "_p" + player + "_h" + health + "_d1" + ".png";
                        if (direction == 0){
                            im = Resources.getImage(ref);
                        } else{
                            im = type.sprites[player - 1][health - 1][0].copy();
                        }
                        im.rotate(360f / 6 * direction);
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
