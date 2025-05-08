
package PaooGame.Config;

import java.awt.*;

public class Constants {
    public static final float EPSILON = 0.01f;

    public static final int WINDOW_WIDTH = 1120;//1280;//10 tiles //1920x1080 * 0.7
    public static final int WINDOW_HEIGHT = 720;//720;//768; // 6 tiles

    public static final int TILE_SIZE = 16;

    public static final int LEVEL1_WIDTH = 70;
    public static final int LEVEL1_HEIGHT = 45;
    public static final int LEVEL1_TILE_NR = LEVEL1_WIDTH*LEVEL1_HEIGHT;


    public static final int LEVEL2_WIDTH = 120;
    public static final int LEVEL2_HEIGHT = 30;
    public static final int LEVEL2_TILE_NR = LEVEL2_WIDTH*LEVEL2_HEIGHT;

    public static final int LEVEL3_WIDTH = 350;
    public static final int LEVEL3_HEIGHT = 170;
    public static final int LEVEL3_TILE_NR = LEVEL3_WIDTH*LEVEL3_HEIGHT;


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

    public static final String LEVEL2_TEXTURES_PATH = "res/Level2/Level2Textures.png";
    public static final String LEVEL2_TEXTURES_CSV = "res/Level2/Level2Tilemap.csv";
    public static final String LEVEL2_BEHAVIOR_CSV = "res/Level2/Level2Behaviors.csv";
    public static final String LEVEL2_BG_PATH = "res/Level2/Level2Background.png";

    public static final String TIGER_FIGHT_BG_PATH = "res/Level1/TigerFightBg.jpg";
    public static final String DEATH_SCREEN_BG_PATH = "res/Effects/YouDied.jpg";
    public static final String HERO_SPRITE_SHEET_PATH = "res/Characters/Hero/Hero_SpriteSheet1.png";
    public static final String TIGER_SPRITE_SHEET_PATH = "res/Enemies/Tiger.png";
    public static final String ATTACK_EXPLOSION_SHEET_PATH = "res/Effects/HitExplosion.png";

    public static final String LEVEL1_STATE = "LEVEL1_STATE";
    public static final String LEVEL2_STATE = "LEVEL2_STATE";
    public static final String ABOUT_STATE = "ABOUT_STATE";
    public static final String MENU_STATE = "MENU_STATE";
    public static final String PAUSE_MENU_STATE = "PAUSE_MENU_STATE";
    public static final String SETTINGS_STATE = "SETTINGS_STATE";
    public static final String FIGHT_STATE = "FIGHT_STATE";

    public static final Color GREEN_HEALTH_BAR_COLOR_1 = new Color(0, 255, 0);
    public static final Color GREEN_HEALTH_BAR_COLOR_2 = new Color(0, 150, 0);
    public static final Color YELLOW_HEALTH_BAR_COLOR_1 = new Color(255, 255, 0);
    public static final Color YELLOW_HEALTH_BAR_COLOR_2 = new Color(200, 200, 0);

    public static final float BASE_ENTITY_GRAVITY = 0.15f;
    public static final float BASE_MAX_ENTITY_FALL_SPEED = 6.0f;
    public static final float DYING_MAX_ENTITY_FALL_SPEED = 2.0f;
    public static final float DYING_ENTITY_GRAVITY = 0.001f;

    public static final double HERO_BASE_HEALTH = 100.0;
    public static final double HERO_BASE_DAMAGE = 100.0;
    public static final float HERO_BASE_SPEED = 2.0f;
    public static final float HERO_BASE_JUMP_STRENGTH = -3.5f;
    public static final float HERO_LEVEL2_JUMP_STRENGTH = -4.0f;
    public static final int HERO_LEVEL1_STARTING_X = 100;
    public static final int HERO_LEVEL1_STARTING_Y = 420;
    public static final int HERO_LEVEL2_STARTING_X = 415;
    public static final int HERO_LEVEL2_STARTING_Y = 100;



    public static final double TIGER_HEALTH = 100.0;
    public static final double TIGER_DAMAGE = 20.0;
    public static final float TIGER_SPEED =-0.4f;

    //exit level1 x<= (735,930); y<=aprox 650


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
