
package PaooGame.Config;

public class Constants {
    public static final float EPSILON = 0.01f;

    public static final int WINDOW_WIDTH = 1120;//1280;//10 tiles //1920x1080 * 0.7
    public static final int WINDOW_HEIGHT = 720;//720;//768; // 6 tiles

    public static final int TILE_SIZE = 16;
    public static final int TILE_SCALE_FACTOR = 4;

    public static final int LEVEL1_TILE_NR = 3150;
    //public static final int LEVEL1_TILE_NR = 100 * 45;
    public static final int LEVEL1_WIDTH = 70;
    //public static final int LEVEL1_WIDTH = 100; // în loc de 70
    public static final int LEVEL1_HEIGHT = 45;


    public static final double MAGIC_NUMBER = 2.27;


    public static final int CHARACTER_TILE_SIZE = 48;
    public static final int CHARACTER_LEFT_OFFSET = 15;
    public static final int CHARACTER_RIGHT_OFFSET = 17;
    public static final int CHARACTER_UPPER_OFFSET = 9;
    public static final int CHARACTER_LOWER_OFFSET = 12;
    public static final int COYOTE_TIME_MAX = 10; // ~10 frames of grace (~166ms at 60fps)
    public static final String LEVEL1_TEXTURES_PATH = "res/Level1/Level1Textures.png";
    public static final String LEVEL1_TEXTURES_CSV = "res/Level1/Level1Textures.csv";
    public static final String LEVEL1_BEHAVIOR_CSV = "res/Level1/Level1Behavior.csv";
    public static final String LEVEL1_BG_PATH = "res/Level1/Level1Background.png";
    public static final String HERO_SPRITE_SHEET_PATH = "res/Characters/Hero/Hero_SpriteSheet1.png";
    public static final String LEVEL_ONE_CONTEXT = "res/Level1.Textures";

    public static enum HERO_STATES {
        IDLE, ATTACKING, JUMPING, RUNNING, CROUCHING, FALLING
    }

}
