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
    private final Image[][] sprites;
    private PieceType(int defaultHealth){
        this.defaultHealth = defaultHealth;
        sprites = new Image[2][defaultHealth];
    }
    
    private String getSpriteRef(int player, int health){
        return "/assets/graphics/units/player" + player + "/" + name().toLowerCase()
                + "_p" + player + "_h" + health + ".png";
    }
    
    public static void initSprites(){
        for (PieceType type : PieceType.values()){
            for (int player : new int[]{1, 2}){
                for (int health = 1; health <= type.defaultHealth; health++){
                    type.sprites[player - 1][health - 1] = Resources.getImage(type.getSpriteRef(player, health));
                }
            }
        }
    }
    
    public Image getSprite(int player, int health){
        return sprites[player - 1][health - 1];
    }
}
