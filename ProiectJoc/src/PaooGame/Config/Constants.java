
package PaooGame.Config;

import java.awt.*;

public class Constants {
    public static final float EPSILON = 0.01f;

    public static final int WINDOW_WIDTH = 1120;//1280;//10 tiles //1920x1080 * 0.7
    public static final int WINDOW_HEIGHT = 720;//720;//768; // 6 tiles

    public static final int TILE_SIZE = 16;

    public static final int LEVEL1_TILE_NR = 3150;
    public static final int LEVEL1_WIDTH = 70;
    public static final int LEVEL1_HEIGHT = 45;


    public static final double MAGIC_NUMBER = 2.27;


    public static final int CHARACTER_TILE_SIZE = 48;
    public static final int TIGER_PASSIVE_TILE_WIDTH = 64;
    public static final int TIGER_PASSIVE_TILE_HEIGHT = 32;
    public static final int TIGER_FIGHTING_TILE_WIDTH = 32;
    public static final int TIGER_FIGHTING_TILE_HEIGHT = 64;
    public static final int ATTACK_EXPLOSION_TILE_SIZE = 112;


    public static final String LEVEL1_TEXTURES_PATH = "res/Level1/Level1Textures.png";
    public static final String LEVEL1_TEXTURES_CSV = "res/Level1/Level1Textures.csv";
    public static final String LEVEL1_BEHAVIOR_CSV = "res/Level1/Level1Behavior.csv";
    public static final String LEVEL1_BG_PATH = "res/Level1/Level1Background.png";
    public static final String TIGER_FIGHT_BG_PATH = "res/Level1/TigerFightBg.jpg";
    public static final String DEATH_SCREEN_BG_PATH = "res/Effects/YouDied.jpg";
    public static final String HERO_SPRITE_SHEET_PATH = "res/Characters/Hero/Hero_SpriteSheet1.png";
    public static final String TIGER_SPRITE_SHEET_PATH = "res/Enemies/Tiger.png";
    public static final String ATTACK_EXPLOSION_SHEET_PATH = "res/Effects/HitExplosion.png";

    public static final String LEVEL1_STATE = "LEVEL1_STATE";
    public static final String ABOUT_STATE = "ABOUT_STATE";
    public static final String MENU_STATE = "MENU_STATE";
    public static final String PAUSE_MENU_STATE = "PAUSE_MENU_STATE";
    public static final String SETTINGS_STATE = "SETTINGS_STATE";
    public static final String FIGHT_STATE = "FIGHT_STATE";

    public static final Color GREEN_HEALTH_BAR_COLOR_1 = new Color(0, 255, 0);
    public static final Color GREEN_HEALTH_BAR_COLOR_2 = new Color(0, 150, 0);
    public static final Color YELLOW_HEALTH_BAR_COLOR_1 = new Color(255, 255, 0);
    public static final Color YELLOW_HEALTH_BAR_COLOR_2 = new Color(200, 200, 0);

    public static final double HERO_BASE_HEALTH = 100.0;
    public static final double HERO_BASE_DAMAGE = 40.0;
    public static final float HERO_BASE_SPEED = 2.0f;

    public static final double TIGER_HEALTH = 100.0;
    public static final double TIGER_DAMAGE = 20.0;
    public static final float TIGER_SPEED =-0.4f;


    public static enum EFFECTS{
        ATTACK_EXPLOSION
    }

    public static enum HERO_STATES {
        IDLE, ATTACKING, JUMPING, RUNNING, CROUCHING, FALLING
    }

    public static enum ENEMY_STATES {
        FALLING,WALKING,IN_FIGHT_IDLE,IN_FIGHT_ATTACKING
    }

}
