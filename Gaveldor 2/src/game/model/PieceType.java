package game.model;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

import util.Constants;
import util.Resources;

public enum PieceType {
    INFANTRY(Constants.INFANTRY_HEALTH_POINTS, "/assets/audio/effects/sword.ogg"),
    ARCHER(Constants.ARCHER_HEALTH_POINTS, "/assets/audio/effects/bow3.ogg"),
    CAVALRY(Constants.CAVALRY_HEALTH_POINTS, "/assets/audio/effects/sword.ogg"),
    ;
    
    
    public final int defaultHealth;
    private final Image[][][] sprites;
    
    private final String attackSoundPath;
    private Sound attackSound;
    
    private PieceType(int defaultHealth, String attackSoundPath){
        this.defaultHealth = defaultHealth;
        sprites = new Image[2][defaultHealth][6];
        this.attackSoundPath = attackSoundPath;
    }
    
    public static void init(){
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
            
            type.attackSound = Resources.getSound(type.attackSoundPath);
        }
    }
    
    public Image getSprite(int player, int health, int direction){
        return sprites[player - 1][health - 1][direction];
    }
    
    public Sound getAttackSound(){
        return attackSound;
    }
}
