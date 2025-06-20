
package PaooGame.Config;

import java.awt.*;

/**
 * @brief Defines a collection of static final constants used throughout the application.
 *
 * This class centralizes all global constant values, such as window dimensions,
 * tile sizes, level configurations, asset paths, entity properties, database keys,
 * and SQL commands. This approach helps in maintaining consistency and makes it
 * easier to adjust game parameters from a single location.
 *
 * The constants cover various aspects including:
 * - Game window and rendering parameters.
 * - Level dimensions and tile information for multiple game levels.
 * - Paths to game assets (textures, spritesheets, backgrounds).
 * - Entity (hero, enemies, NPC) properties like health, damage, speed, and sprite dimensions.
 * - Item properties and locations.
 * - State identifiers for game states (e.g., menu, levels, fight).
 * - Health bar colors.
 * - Physics constants like gravity.
 * - Database related constants including table name, column names, SQL queries for saving/loading game data and scores.
 * - Enumerations for distinct game element states (EFFECTS, HERO_STATES, ENEMY_STATES).
 */

public class Constants {
    public static final float EPSILON = 0.01f;

    public static final int WINDOW_WIDTH = 1120;//1280;//10 tiles //1920x1080 * 0.7
    public static final int WINDOW_HEIGHT = 720;//720;//768; // 6 tiles

    public static final int TILE_SIZE = 16;

    public static final int LEVEL1_WIDTH = 70;
    public static final int LEVEL1_HEIGHT = 45;
    public static final int LEVEL1_TILE_NR = LEVEL1_WIDTH*LEVEL1_HEIGHT;
    public static final int LEVEL1_PIXEL_WIDTH = Constants.LEVEL1_WIDTH*Constants.TILE_SIZE;
    public static final int LEVEL1_PIXEL_HEIGHT = Constants.LEVEL1_HEIGHT*Constants.TILE_SIZE;


    public static final int LEVEL2_WIDTH = 120;
    public static final int LEVEL2_HEIGHT = 30;
    public static final int LEVEL2_TILE_NR = LEVEL2_WIDTH*LEVEL2_HEIGHT;
    public static final int LEVEL2_PIXEL_WIDTH = Constants.LEVEL2_WIDTH*Constants.TILE_SIZE;
    public static final int LEVEL2_PIXEL_HEIGHT = Constants.LEVEL2_HEIGHT*Constants.TILE_SIZE;

    public static final int LEVEL3_WIDTH = 350;
    public static final int LEVEL3_HEIGHT = 170;
    public static final int LEVEL3_TILE_NR = LEVEL3_WIDTH*LEVEL3_HEIGHT;
    public static final int LEVEL3_PIXEL_WIDTH = Constants.LEVEL3_WIDTH*Constants.TILE_SIZE;
    public static final int LEVEL3_PIXEL_HEIGHT = Constants.LEVEL3_HEIGHT*Constants.TILE_SIZE;


    public static final double MAGIC_NUMBER = 2.27;


    public static final int CHARACTER_TILE_SIZE = 48;

    public static final int ATTACK_EXPLOSION_TILE_SIZE = 112;


    public static final int BOOT_TILE_WIDTH = 34;
    public static final int BOOT_TILE_HEIGHT = 28;
    public static final int ITEM_FLOATING_TILE_SIZE = 48;
    public static final int SAVE_ITEM_TILE_SIZE = 16;
    public static final int BONFIRE_TILE_SIZE = 32;
    public static final int WHIP_POSITION_X = 495;
    public static final int WHIP_POSITION_Y = 1308;

    public static final String BOOSTER_PATH = "res/Items/booster.png";
    public static final int BOOSTER_IMG_WIDTH  = 48;
    public static final int BOOSTER_IMG_HEIGHT = 16;

    public static final int LEVEL1_SAVE1_X = 565;
    public static final int LEVEL1_SAVE1_Y = 450;

    public static final int LEVEL2_SAVE1_X = 950;
    public static final int LEVEL2_SAVE1_Y = 337;

    public static final int LEVEL3_SAVE1_X = 572;
    public static final int LEVEL3_SAVE1_Y = 1010;
    public static final int LEVEL3_SAVE2_X = 1340;
    public static final int LEVEL3_SAVE2_Y = 1106;
    public static final int LEVEL3_SAVE3_X = 2590;
    public static final int LEVEL3_SAVE3_Y = 962;
    public static final int LEVEL3_SAVE4_X = 4260;
    public static final int LEVEL3_SAVE4_Y = 1298;

    public static final String YOU_WIN_PATH = "res/Effects/YouWin.png";
    public static final int YOU_WIN_IMG_HEIGHT = 1080;
    public static final int YOU_WIN_IMG_WIDTH = 1920;
    public static final int YOU_WIN_FRAME_NR = 5;

    public static final String MAIN_MENU_BG_PATH = "res/Backgrounds/bgCropped_tilemapMainMenu.png";
    public static final int MAIN_MENU_BG_IMG_WIDTH = 778;
    public static final int MAIN_MENU_BG_IMG_HEIGHT = 500 ;
    public static final int MAIN_MENU_BG_FRAME_NR = 66;

    public static final String WINNING_BG_PATH = "res/Backgrounds/WinningStateBg.png";
    public static final int WINNING_BG_WIDTH = 1300;
    public static final int WINNING_BG_HEIGHT = 800;
    public static final int WINNING_BG_FRAME_NR = 8;

    public static final String LEVEL1_TEXTURES_PATH = "res/Level1/Level1Textures.png";
    public static final String LEVEL1_TEXTURES_CSV = "res/Level1/Level1Textures.csv";
    public static final String LEVEL1_BEHAVIOR_CSV = "res/Level1/Level1Behavior.csv";
    public static final String LEVEL1_BG_PATH = "res/Level1/Level1Background.png";





    public static final String LEVEL2_TEXTURES_PATH = "res/Level2/Level2Textures.png";
    public static final String LEVEL2_TEXTURES_CSV = "res/Level2/Level2Tilemap.csv";
    public static final String LEVEL2_BEHAVIOR_CSV = "res/Level2/Level2Behaviors.csv";
    public static final String LEVEL2_BG_PATH = "res/Level2/Level2Background.png";

    public static final String LEVEL3_TEXTURES_PATH = "res/Level3/Level3Textures.png" ;
    public static final String LEVEL3_TEXTURES_CSV = "res/Level3/Level3Textures.csv";
    public static final String LEVEL3_BEHAVIOR_CSV = "res/Level3/Level3Behaviors.csv";
    public static final String LEVEL3_BG_PATH = "res/Level3/Level3Background.png";

    public static final String DEATH_SCREEN_BG_PATH = "res/Effects/YouDied.jpg";
    public static final String HERO_SPRITE_SHEET_PATH = "res/Characters/Hero/Hero_SpriteSheet1.png";
    public static final String ATTACK_EXPLOSION_SHEET_PATH = "res/Effects/HitExplosion.png";
    public static final String BOOT_ITEM_SHEET_PATH = "res/Items/EscapeItem.png";
    public static final String WHIP_FRAMED_SHEET_PATH = "res/Items/WhipFramed.png";
    public static final String BONFIRE_SHEET_PATH = "res/Items/save_bonfire.png";
    public static final String SAVE_ITEM_PATH = "res/Items/SaveFloppy.png";

    public static final String LEVEL1_STATE = "LEVEL1_STATE";
    public static final String LEVEL2_STATE = "LEVEL2_STATE";
    public static final String LEVEL3_STATE = "LEVEL3_STATE";
    public static final String ABOUT_STATE = "ABOUT_STATE";
    public static final String MENU_STATE = "MENU_STATE";
    public static final String PAUSE_MENU_STATE = "PAUSE_MENU_STATE";
    public static final String SETTINGS_STATE = "SETTINGS_STATE";
    public static final String FIGHT_STATE = "FIGHT_STATE";

    public static final String WHIP_NAME = "WHIP";
    public static final String BONFIRE_NAME = "BONFIRE";
    public static final String SAVE_ITEM_NAME = "SAVE";
    public static final String BOOSTER_ITEM_NAME = "BOOSTER";

    public static final Color GREEN_HEALTH_BAR_COLOR_1 = new Color(0, 255, 0);
    public static final Color GREEN_HEALTH_BAR_COLOR_2 = new Color(0, 150, 0);
    public static final Color YELLOW_HEALTH_BAR_COLOR_1 = new Color(255, 255, 0);
    public static final Color YELLOW_HEALTH_BAR_COLOR_2 = new Color(200, 200, 0);
    public static final Color PURPLE_HEALTH_BAR_COLOR_1 = new Color(148, 0, 211);  // Dark Violet
    public static final Color PURPLE_HEALTH_BAR_COLOR_2 = new Color(186, 85, 211);  // Medium Orchid

    public static final float BASE_ENTITY_GRAVITY = 0.15f;
    public static final float BASE_MAX_ENTITY_FALL_SPEED = 6.0f;
    public static final float DYING_MAX_ENTITY_FALL_SPEED = 2.0f;
    public static final float DYING_ENTITY_GRAVITY = 0.001f;

    public static final String GOBLIN_SPRITE_SHEET_PATH = "res/Characters/npc_goblin.png";
    public static final int GOBLIN_PASSIVE_TILE_WIDTH = 32;
    public static final int GOBLIN_PASSIVE_TILE_HEIGHT = 48;
    public static final int GOBLIN_BOUND_TILE_WIDTH = 32;
    public static final int GOBLIN_BOUND_TILE_HEIGHT = 48;
    public static final int GOBLIN_POS_X = 1030;
    public static final int GOBLIN_POS_Y = 336;
    public static final String GOBLIN_NAME = "Goblin";
    public static final int GOLD_REWARD = 30;
//    public static final String GOBLIN_SPRITE_SHEET_PATH = "res/NPC/npc_goblin.png";

    public static final double HERO_BASE_HEALTH = 100.0;
    public static final double HERO_BASE_DAMAGE = 50.0;
    public static final float HERO_BASE_SPEED = 2.0f;
    public static final float HERO_BASE_JUMP_STRENGTH = -3.5f;
    public static final float HERO_BOOSTED_JUMP_STRENGTH = -5.0f;
    public static final float HERO_LEVEL2_JUMP_STRENGTH = -6.0f;

    public static final int HERO_LEVEL1_STARTING_X = 100;
    public static final int HERO_LEVEL1_STARTING_Y = 420;

    public static final int HERO_LEVEL2_STARTING_X = 415;
    public static final int HERO_LEVEL2_STARTING_Y = 100;
    public static final int BOOSTER1_X = 1280;
    public static final int BOOSTER1_Y = 390;
    public static final int BOOSTER2_X = 1360;
    public static final int BOOSTER2_Y = 345;
    public static final int BOOSTER3_X = 1487;
    public static final int BOOSTER3_Y = 313;


    public static final int HERO_LEVEL3_STARTING_X = 110;
    public static final int HERO_LEVEL3_STARTING_Y = 900;
    public static final int HERO_GRAPPLE_RANGE = 6;





    public static final int TIGER_PASSIVE_TILE_WIDTH = 64;
    public static final int TIGER_PASSIVE_TILE_HEIGHT = 32;
    public static final int TIGER_FIGHTING_TILE_WIDTH = 32;
    public static final int TIGER_FIGHTING_TILE_HEIGHT = 64;
    public static final String TIGER_SPRITE_SHEET_PATH = "res/Enemies/Tiger.png";
    public static final String TIGER_FIGHT_BG_PATH = "res/Backgrounds/TigerFightBg.jpg";
    public static final double TIGER_HEALTH = 100.0;
    public static final double TIGER_DAMAGE = 10.0;
    public static final float TIGER_SPEED =-0.4f;
    public static final float TIGER_DEFENCE = 0f;
    public static final String TIGER_NAME = "Tiger";
    public static final int TIGER1_X = 400;
    public static final int TIGER1_Y = 450;
    public static final int TIGER2_X = 720;
    public static final int TIGER2_Y = 460;


    public static final int BASIC_SKELETON_PASSIVE_TILE_WIDTH = 32;
    public static final int BASIC_SKELETON_PASSIVE_TILE_HEIGHT = 32;
    public static final int BASIC_SKELETON_FIGHTING_TILE_WIDTH = 48;
    public static final int BASIC_SKELETON_FIGHTING_TILE_HEIGHT = 48;
    public static final String BASIC_SKELETON_SPRITE_SHEET_PATH = "res/Enemies/skeleton_basic.png";
    public static final String BASIC_SKELETON_FIGHT_BG_PATH = "res/Backgrounds/BasicSkeletonBG.png";
    public static final double BASIC_SKELETON_HEALTH = 100.0;
    public static final double BASIC_SKELETON_DAMAGE = 15.0;
    public static final float BASIC_SKELETON_SPEED = 0.6f;
    public static final float BASIC_SKELETON_DEFENCE = 0.4f;
    public static final String BASIC_SKELETON_NAME = "Skeleton";
    public static final int BASIC_SKELETON1_X = Constants.HERO_LEVEL2_STARTING_X+300;
    public static final int BASIC_SKELETON1_Y = Constants.HERO_LEVEL2_STARTING_Y;
    public static final int BASIC_SKELETON2_X =1700 ;
    public static final int BASIC_SKELETON2_Y =240 ;



    public static final int WIZARD_HEALTH_BAR_WIDTH = 600;
    public static final int WIZARD_HEALTH_BAR_HEIGHT = 60;
    public static final int WIZARD_PASSIVE_TILE_NR = 7;
    public static final int WIZARD_ATTACKING_TILE_NR = 8;
    public static final int WIZARD_IN_FIGHT_IDLE_TILE_NR = 6;
    public static final int WIZARD_PASSIVE_TILE_WIDTH = 80;
    public static final int WIZARD_PASSIVE_TILE_HEIGHT = 80;
    public static final int WIZARD_FIGHTING_TILE_WIDTH = 144;
    public static final int WIZARD_FIGHTING_TILE_HEIGHT = 112;
    public static final double WIZARD_HEALTH = 100f;
    public static final double WIZARD_DAMAGE = 30f;
    public static final float WIZARD_SPEED = -1f;
    public static final float WIZARD_DEFENCE = 0.7f;
    public static final String WIZARD_SPRITE_SHEET_PATH = "res/Enemies/wizard.png";
    public static final String WIZARD_FIGHT_BG_PATH = "res/Backgrounds/WizardBg.jpg";
    public static final String WIZARD_NAME = "EVIL WIZARD";
    public static final int WIZARD_X = 5150;
    public static final int WIZARD_Y = 1200;


    public static final int MINOTAUR_HEALTH_BAR_WIDTH = 300;
    public static final int MINOTAUR_HEALTH_BAR_HEIGHT = 30;
    public static final int MINOTAUR_PASSIVE_TILE_WIDTH = 128;
    public static final int MINOTAUR_PASSIVE_TILE_HEIGHT = 112;
    public static final int MINOTAUR_FIGHTING_TILE_WIDTH = 224;
    public static final int MINOTAUR_FIGHTING_TILE_HEIGHT = 128;
    public static final int MINOTAUR_PASSIVE_TILE_NR = 12;
    public static final int MINOTAUR_IN_FIGHT_IDLE_TILE_NR = 16;
    public static final int MINOTAUR_ATTACKING_TILE_NR = 16;
    public static final double MINOTAUR_HEALTH = 100f;
    public static final double MINOTAUR_DAMAGE = 30f;
    public static final float MINOTAUR_SPEED = -0.5f;
    public static final float MINOTAUR_DEFENCE = 0.2f;
    public static final String MINOTAUR_SPRITE_SHEET_PATH = "res/Enemies/minotaur.png";
    public static final String MINOTAUR_FIGHT_BG_PATH = "res/Backgrounds/MinotaurBG.png";
    public static final String MINOTAUR_NAME = "MINOTAUR";
    public static final int MINOTAUR1_X = 710;
    public static final int MINOTAUR1_Y = 1000;
    public static final int MINOTAUR2_X = 2968;
    public static final int MINOTAUR2_Y = 610;

    public static final int GHOST_HEALTH_BAR_WIDTH = 200;
    public static final int GHOST_HEALTH_BAR_HEIGHT = 20;
    public static final int GHOST_PASSIVE_TILE_WIDTH = 32;
    public static final int GHOST_PASSIVE_TILE_HEIGHT = 32;
    public static final int GHOST_FIGHTING_TILE_WIDTH = 32;
    public static final int GHOST_FIGHTING_TILE_HEIGHT = 32;
    public static final int GHOST_PASSIVE_TILE_NR = 4;
    public static final int GHOST_IN_FIGHT_IDLE_TILE_NR = 8;
    public static final int GHOST_ATTACKING_TILE_NR = 3;
    public static final double GHOST_HEALTH = 100f;
    public static final double GHOST_DAMAGE = 5f;
    public static final float GHOST_SPEED = -0.3f;
    public static final float GHOST_DEFENCE = 0f;
    public static final String GHOST_SPRITE_SHEET_PATH = "res/Enemies/ghost2.png";
    public static final String GHOST_FIGHT_BG_PATH = "res/Backgrounds/GhostBG.png";
    public static final String GHOST_NAME = "Ghost";
    public static final int GHOST1_X = 3312;
    public static final int GHOST1_Y = 1970;
    public static final int GHOST2_X = 3852;
    public static final int GHOST2_Y = 1890;

    public static final int STRONG_SKELETON_HEALTH_BAR_WIDTH = 300;
    public static final int STRONG_SKELETON_HEALTH_BAR_HEIGHT = 30;
    public static final int STRONG_SKELETON_PASSIVE_TILE_NR = 12;
    public static final int STRONG_SKELETON_PASSIVE_TILE_WIDTH = 48;
    public static final int STRONG_SKELETON_PASSIVE_TILE_HEIGHT = 32;
    public static final int STRONG_SKELETON_FIGHTING_TILE_WIDTH = 64;
    public static final int STRONG_SKELETON_FIGHTING_TILE_HEIGHT = 48;
    public static final int STRONG_SKELETON_ATTACKING_TILE_NR = 13;
    public static final int STRONG_SKELETON_IN_FIGHT_IDLE_TILE_NR = 4;
    public static final double STRONG_SKELETON_HEALTH = 100f;
    public static final double STRONG_SKELETON_DAMAGE = 25f;
    public static final float STRONG_SKELETON_SPEED = -0.5f;
    public static final float STRONG_SKELETON_DEFENCE = 0.4f;
    public static final String STRONG_SKELETON_SPRITE_SHEET_PATH = "res/Enemies/StrongSkeleton.png";
    public static final String STRONG_SKELETON_BG_PATH = "res/Backgrounds/StrongSkeletonBG.png";
    public static final String STRONG_SKELETON_NAME = "Skullcrusher";
    public static final int STRONG_SKELETON1_X = 1020;
    public static final int STRONG_SKELETON1_Y = 320;




    public static final String LEVEL_1 = "LEVEL_1";
    public static final String LEVEL_2 = "LEVEL_2";
    public static final String LEVEL_3 = "LEVEL_3";


    public static enum EFFECTS{
        ATTACK_EXPLOSION
    }

    public static enum HERO_STATES {
        IDLE, ATTACKING, JUMPING, RUNNING, CROUCHING, FALLING
    }

    public static enum ENEMY_STATES {
        FALLING,WALKING,IN_FIGHT_IDLE,IN_FIGHT_ATTACKING
    }

    public static final String CURRENT_STATE = "CURRENT_STATE";

    public static final String HERO_HEALTH = "HERO_HEALTH";
    public static final String HERO_X = "HERO_X";
    public static final String HERO_Y = "HERO_Y";
    public static final String HERO_HAS_WHIP = "HERO_HAS_WHIP";
    public static final String HERO_NR_OF_FLEES = "HERO_NR_OF_FLEES";
    public static final String HERO_NR_OF_COLLECTED_SAVES = "HERO_NR_OF_COLLECTED_SAVES";
    public static final String HERO_NR_OF_FINISHED_LEVELS = "HERO_NR_OF_FINISHED_LEVELS";
    public static final String HERO_GOLD = "HERO_GOLD";


    public static final String TIGER0_HEALTH = "TIGER0_HEALTH";
    public static final String TIGER1_HEALTH = "TIGER1_HEALTH";

    public static final String BASIC_SKELETON0_HEALTH = "BASIC_SKELETON0_HEALTH";
    public static final String BASIC_SKELETON1_HEALTH = "BASIC_SKELETON1_HEALTH";
    public static final String STRONG_SKELETON0_HEALTH = "STRONG_SKELETON0_HEALTH";

    public static final String BOSS_HEALTH = "BOSS_HEALTH";
    public static final String MINOTAUR0_HEALTH = "MINOTAUR0_HEALTH";
    public static final String MINOTAUR1_HEALTH = "MINOTAUR1_HEALTH";
    public static final String GHOST0_HEALTH = "GHOST0_HEALTH";
    public static final String GHOST1_HEALTH = "GHOST1_HEALTH";

    public static final String TIMESTAMP = "TIMESTAMP";

    public static final int NR_OF_DB_CONSTANTS = 19;


    public static String[] ALL_DATA_MANAGEMENT_CONSTANTS = {
        TIMESTAMP,
            CURRENT_STATE,
            HERO_HEALTH,
            HERO_X,
            HERO_Y,
            HERO_HAS_WHIP,
            HERO_NR_OF_FLEES,
            HERO_NR_OF_COLLECTED_SAVES,
            HERO_NR_OF_FINISHED_LEVELS,
            HERO_GOLD,
            TIGER0_HEALTH,
            TIGER1_HEALTH,
            BASIC_SKELETON0_HEALTH,
            BASIC_SKELETON1_HEALTH,
            STRONG_SKELETON0_HEALTH,
            BOSS_HEALTH,
            MINOTAUR0_HEALTH,
            MINOTAUR1_HEALTH,
            GHOST0_HEALTH,
            GHOST1_HEALTH
    };


    public static final String DB_PATH = "src/PaooGame/DatabaseManaging/myDB.db";
    public static final String DB_URL = "jdbc:sqlite:src/PaooGame/DatabaseManaging/myDB.db";

    public static final int DB_MAX_ENTRIES = 5;

    public static final String CREATE_TABLE_CMD = "CREATE TABLE IF NOT EXISTS GameData("+
                                                    Constants.TIMESTAMP + " PRIMARY KEY, "+
                                                    Constants.CURRENT_STATE + " INTEGER, "+
                                                    Constants.HERO_HEALTH + " INTEGER, "+
                                                    Constants.HERO_X + " INTEGER, "+
                                                    Constants.HERO_Y + " INTEGER, "+
                                                    Constants.HERO_HAS_WHIP + " INTEGER, "+
                                                    Constants.HERO_NR_OF_FLEES + " INTEGER, "+
                                                    Constants.HERO_NR_OF_COLLECTED_SAVES + " INTEGER, "+
                                                    Constants.HERO_NR_OF_FINISHED_LEVELS + " INTEGER, "+
                                                    Constants.HERO_GOLD + " INTEGER, "+
                                                    Constants.TIGER0_HEALTH + " INTEGER, "+
                                                    Constants.TIGER1_HEALTH + " INTEGER, "+
                                                    Constants.BASIC_SKELETON0_HEALTH + " INTEGER, "+
                                                    Constants.BASIC_SKELETON1_HEALTH + " INTEGER, "+
                                                    Constants.STRONG_SKELETON0_HEALTH + " INTEGER, "+
                                                    Constants.BOSS_HEALTH + " INTEGER, "+
                                                    Constants.MINOTAUR0_HEALTH + " INTEGER, "+
                                                    Constants.MINOTAUR1_HEALTH + " INTEGER, "+
                                                    Constants.GHOST0_HEALTH + " INTEGER, "+
                                                    Constants.GHOST1_HEALTH + " INTEGER);";

    public static final String INSERT_CMD = "INSERT INTO GameData("+
                                                    Constants.TIMESTAMP + ", "+
                                                    Constants.CURRENT_STATE + ", "+
                                                    Constants.HERO_HEALTH + ", "+
                                                    Constants.HERO_X + ", "+
                                                    Constants.HERO_Y + ", "+
                                                    Constants.HERO_HAS_WHIP + ", "+
                                                    Constants.HERO_NR_OF_FLEES + ", "+
                                                    Constants.HERO_NR_OF_COLLECTED_SAVES + ", "+
                                                    Constants.HERO_NR_OF_FINISHED_LEVELS + ", "+
                                                    Constants.HERO_GOLD + ", "+
                                                    Constants.TIGER0_HEALTH + ", "+
                                                    Constants.TIGER1_HEALTH + ", "+
                                                    Constants.BASIC_SKELETON0_HEALTH + ", "+
                                                    Constants.BASIC_SKELETON1_HEALTH + ", "+
                                                    Constants.STRONG_SKELETON0_HEALTH + ", "+
                                                    Constants.BOSS_HEALTH + ", "+
                                                    Constants.MINOTAUR0_HEALTH + ", "+
                                                    Constants.MINOTAUR1_HEALTH + ", "+
                                                    Constants.GHOST0_HEALTH + ", "+
                                                    Constants.GHOST1_HEALTH + ") VALUES ("+
                                                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public static final String QUERY_SELECT_LATEST_SAVE_CMD = "SELECT * FROM GameData "+
                                                        "ORDER BY " + Constants.TIMESTAMP + " DESC "+
                                                        "LIMIT 1;";

    public static final String DELETE_OLDEST_ENTRY_CMD = "DELETE FROM GameData WHERE rowid = " +
            "(SELECT rowid FROM GameData WHERE TIMESTAMP > 0 ORDER BY TIMESTAMP ASC LIMIT 1)";



    public static final String QUERY_NR_OF_ENTRIES_CMD = "SELECT COUNT(*) FROM GameData;";

    public static final String GET_SCORE_LINE_CMD = "SELECT * FROM GameData WHERE TIMESTAMP = -2;";

    public static final String UPDATE_SCORE_LINE_CMD = "UPDATE GameData SET CURRENT_STATE = ? WHERE TIMESTAMP = -2;";
}
