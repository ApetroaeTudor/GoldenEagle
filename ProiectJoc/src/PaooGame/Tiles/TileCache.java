package PaooGame.Tiles;
import PaooGame.Config.Constants;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Manages caching of game tiles, spritesheets, and background images to optimize loading and memory usage.
 * Implements the Singleton pattern to ensure a single instance of the cache.
 */
public class TileCache {

    /**
     * Private constructor to prevent instantiation from outside the class.
     * This is part of the Singleton pattern.
     */
    private TileCache(){

    }

    /**
     * The single instance of the TileCache.
     */
    private static TileCache instance=null;

    /**
     * Provides access to the single instance of the TileCache.
     * If an instance does not exist, it creates one.
     * @return The singleton instance of TileCache.
     */
    public static TileCache getInstance(){
        if(instance==null){
            instance = new TileCache(); // Corrected Singleton instantiation
        }
        return instance;
    }

    /**
     * Cache for individual Tile objects, keyed by a pair of (tilesheet path, tile ID).
     */
    private static final Map<Entry<String,Integer>,Tile> cache = new HashMap<>();
    /**
     * Cache for loaded tilesheet images, keyed by their file path.
     */
    private static final Map<String,BufferedImage> tileSheets =new HashMap<>();

    /**
     * Cache for background images, keyed by their file path.
     */
    private static final Map<String,BufferedImage> backgrounds=new HashMap<>();

    /**
     * Cache for hero animation spritesheets, keyed by a string identifier (e.g., state name).
     */
    private static final Map<String,BufferedImage> heroStatesSheets =new HashMap<>();
    /**
     * Cache for enemy animation spritesheets, keyed by a string identifier (e.g., enemy type and state).
     */
    private static final Map<String,BufferedImage> enemyStatesSheets =new HashMap<>();
    /**
     * Cache for effect animation spritesheets, keyed by a string identifier (e.g., effect name).
     */
    private static final Map<String,BufferedImage> effectsSheets =new HashMap<>();
    /**
     * Cache for special ability animation spritesheets, keyed by their file path.
     */
    private static final Map<String,BufferedImage> specialsSheets =new HashMap<>();

    /**
     * Retrieves a specific Tile from a tilesheet.
     * If the Tile or the tilesheet is already cached, it returns the cached version.
     * Otherwise, it loads the tilesheet, extracts the Tile, caches both, and then returns the Tile.
     *
     * @param path The file path to the tilesheet.
     * @param id The ID of the tile within the tilesheet.
     * @return The requested Tile object, or null if loading fails or the tile ID is invalid.
     */
    public Tile getTile(String path, int id){
        Entry<String,Integer> key=new AbstractMap.SimpleEntry<>(path,id);
        if(cache.containsKey(key)){
            return cache.get(key);
        }

        BufferedImage sheet= tileSheets.computeIfAbsent(path, p->{
            try{
                File f= new File(p);
                if(!f.exists()){
                    System.err.println("Error! tilesheet not found" + p);
                    return null;
                }
                return ImageIO.read(f);
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        });

        if(sheet==null){
            System.err.println("Error: could not get tilesheet for path: " +path);
            return null;
        }


        Tile tile=Tile.loadTile(sheet,id);
        if(tile!=null){
            cache.put(key,tile);
        }
        else{
            System.err.println("Tile with id: "+id + "from sheet: "+path+" did not load correctly");
        }
        return tile;

    }


    /**
     * Retrieves a background image.
     * If the background image is already cached, it returns the cached version.
     * Otherwise, it loads the image, caches it, and then returns it.
     *
     * @param path The file path to the background image.
     * @return The requested BufferedImage for the background, or null if loading fails.
     */
    public BufferedImage getBackground(String path){
        if(backgrounds.containsKey(path)){
            return backgrounds.get(path);
        }
        else{
            try{
                File f=new File(path);
                if(!f.exists()){
                    System.err.println("Background file not found: "+ path); // Corrected path in error message

                }
                else{
                    backgrounds.put(path,ImageIO.read(f));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return backgrounds.get(path);

    }

    /**
     * Retrieves an effect animation spritesheet.
     * Currently supports ATTACK_EXPLOSION.
     * If the effect spritesheet is already cached, it returns the cached version.
     * Otherwise, it loads the spritesheet, extracts the relevant subimage, caches it, and then returns it.
     *
     * @param selectEffect The type of effect to retrieve, as defined in {@link PaooGame.Config.Constants.EFFECTS}.
     * @return The BufferedImage for the effect animation, or null if loading fails or the effect type is not supported.
     */
    public BufferedImage getEffect(Constants.EFFECTS selectEffect){
        BufferedImage returnIMG = null;
        try{
            File effectFileSheet = null;
            String cacheKey = null; // Added cache key for clarity
            String sheetPath = null;
            int subImageX = 0, subImageY = 0, subImageWidth = 0, subImageHeight = 0;

            switch (selectEffect){
                case ATTACK_EXPLOSION:
                    sheetPath = Constants.ATTACK_EXPLOSION_SHEET_PATH;
                    cacheKey = "ATTACK_EXPLOSION";
                    subImageX = 0;
                    subImageY = 0;
                    subImageWidth = 112 * 8;
                    subImageHeight = 112;
                    break;
                // Add other cases for different effects here if needed
            }

            if (sheetPath == null) {
                System.err.println("Unsupported effect type: " + selectEffect);
                return null;
            }

            if(effectsSheets.containsKey(cacheKey)){
                returnIMG = effectsSheets.get(cacheKey);
            } else {
                effectFileSheet = new File(sheetPath);
                if(!effectFileSheet.exists()){
                    System.err.println("Error working with effect sheet file: " + sheetPath);
                    return null;
                }
                BufferedImage fullSheet = ImageIO.read(effectFileSheet);
                effectsSheets.put(cacheKey, fullSheet.getSubimage(subImageX, subImageY, subImageWidth, subImageHeight));
                returnIMG = effectsSheets.get(cacheKey);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return returnIMG;
    }

    /**
     * Retrieves a special ability animation spritesheet.
     * If the spritesheet is already cached, it returns the cached version.
     * Otherwise, it loads the spritesheet, extracts the relevant subimage based on tile dimensions and frame count,
     * caches it, and then returns it.
     *
     * @param specialPath The file path to the special ability spritesheet.
     * @param tileWidth The width of a single frame/tile in the spritesheet.
     * @param tileHeight The height of a single frame/tile in the spritesheet.
     * @param nrOfFrames The number of frames in the animation strip.
     * @return The BufferedImage for the special ability animation, or null if loading fails.
     */
    public BufferedImage getSpecial(String specialPath,int tileWidth,int tileHeight, int nrOfFrames){
        BufferedImage returnIMG = null;
        try{
            if(specialsSheets.containsKey(specialPath)){
                returnIMG = specialsSheets.get(specialPath);
            } else {
                File specialFileSheet = new File(specialPath);
                if(!specialFileSheet.exists()){
                    System.err.println("Error working with special sheet file: " + specialPath);
                    return null;
                }
                BufferedImage fullSheet = ImageIO.read(specialFileSheet);
                specialsSheets.put(specialPath,fullSheet.getSubimage(0,0,tileWidth*nrOfFrames,tileHeight));
                returnIMG = specialsSheets.get(specialPath);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return returnIMG;
    }

    /**
     * Retrieves an enemy animation spritesheet based on the enemy's type and current state.
     * If the spritesheet for the given type and state is already cached, it returns the cached version.
     * Otherwise, it loads the main enemy spritesheet, extracts the relevant subimage for the state,
     * caches it, and then returns it.
     *
     * @param state The current state of the enemy (e.g., WALKING, ATTACKING), as defined in {@link PaooGame.Config.Constants.ENEMY_STATES}.
     * @param enemyType The type of the enemy (e.g., TIGER_NAME, BASIC_SKELETON_NAME), as defined in {@link PaooGame.Config.Constants}.
     * @return The BufferedImage for the enemy's current animation state, or null if loading fails or parameters are invalid.
     */
    public BufferedImage getEnemySheetByState(Constants.ENEMY_STATES state, String enemyType){
        BufferedImage returnIMG = null;
        File enemyFileSheet;
        String sheetPath = "";
        int passiveTileWidth=0;
        int passiveTileHeight=0;
        int inFightTileWidth=0;
        int inFightTileHeight=0;
        String fallingMessageKey=""; // Renamed for clarity as cache key
        String walkingMessageKey=""; // Renamed for clarity as cache key
        String inFightIdleMessageKey=""; // Renamed for clarity as cache key
        String inFightAttackingMessageKey=""; // Renamed for clarity as cache key
        int walkingAnimationNrOfTiles=0;
        int inFightIdleNrOfTiles=0;
        int inFightAttackingNrOfTiles=0;
        int subImageX = 0, subImageY = 0, subImageWidth = 0, subImageHeight = 0; // For getSubimage parameters
        String currentCacheKey = null;


        switch (enemyType){
            case Constants.TIGER_NAME:
                sheetPath = Constants.TIGER_SPRITE_SHEET_PATH;
                passiveTileHeight = Constants.TIGER_PASSIVE_TILE_HEIGHT;
                passiveTileWidth = Constants.TIGER_PASSIVE_TILE_WIDTH;
                inFightTileWidth = Constants.TIGER_FIGHTING_TILE_WIDTH;
                inFightTileHeight = Constants.TIGER_FIGHTING_TILE_HEIGHT;
                walkingAnimationNrOfTiles = 4;
                inFightIdleNrOfTiles = 1;
                inFightAttackingNrOfTiles = 4;
                fallingMessageKey = "TIGER_FALLING";
                walkingMessageKey = "TIGER_WALKING"; // Added missing walking key
                inFightIdleMessageKey = "TIGER_IN_FIGHT_IDLE";
                inFightAttackingMessageKey = "TIGER_IN_FIGHT_ATTACKING";

                break;
            case Constants.BASIC_SKELETON_NAME:
                sheetPath = Constants.BASIC_SKELETON_SPRITE_SHEET_PATH;
                passiveTileHeight = Constants.BASIC_SKELETON_PASSIVE_TILE_HEIGHT;
                passiveTileWidth = Constants.BASIC_SKELETON_PASSIVE_TILE_WIDTH;
                inFightTileWidth = Constants.BASIC_SKELETON_FIGHTING_TILE_WIDTH;
                inFightTileHeight = Constants.BASIC_SKELETON_FIGHTING_TILE_HEIGHT;
                walkingAnimationNrOfTiles = 13;
                inFightIdleNrOfTiles = 11;
                inFightAttackingNrOfTiles = 18;
                walkingMessageKey = "BASIC_SKELETON_WALKING";
                fallingMessageKey = "BASIC_SKELETON_FALLING";
                inFightIdleMessageKey = "BASIC_SKELETON_IN_FIGHT_IDLE";
                inFightAttackingMessageKey = "BASIC_SKELETON_IN_FIGHT_ATTACKING";
                break;
            case Constants.WIZARD_NAME:
                sheetPath = Constants.WIZARD_SPRITE_SHEET_PATH;
                passiveTileHeight = Constants.WIZARD_PASSIVE_TILE_HEIGHT;
                passiveTileWidth = Constants.WIZARD_PASSIVE_TILE_WIDTH;
                inFightTileWidth = Constants.WIZARD_FIGHTING_TILE_WIDTH;
                inFightTileHeight = Constants.WIZARD_FIGHTING_TILE_HEIGHT;
                walkingAnimationNrOfTiles = Constants.WIZARD_PASSIVE_TILE_NR;
                inFightIdleNrOfTiles = Constants.WIZARD_IN_FIGHT_IDLE_TILE_NR;
                inFightAttackingNrOfTiles = Constants.WIZARD_ATTACKING_TILE_NR;
                walkingMessageKey = "WIZARD_WALKING";
                fallingMessageKey = "WIZARD_FALLING";
                inFightIdleMessageKey = "WIZARD_IN_FIGHT_IDLE";
                inFightAttackingMessageKey = "WIZARD_IN_FIGHT_ATTACKING";
                break;
            case Constants.MINOTAUR_NAME:
                sheetPath = Constants.MINOTAUR_SPRITE_SHEET_PATH;
                passiveTileHeight = Constants.MINOTAUR_PASSIVE_TILE_HEIGHT;
                passiveTileWidth = Constants.MINOTAUR_PASSIVE_TILE_WIDTH;
                inFightTileWidth = Constants.MINOTAUR_FIGHTING_TILE_WIDTH;
                inFightTileHeight = Constants.MINOTAUR_FIGHTING_TILE_HEIGHT;
                walkingAnimationNrOfTiles = Constants.MINOTAUR_PASSIVE_TILE_NR;
                inFightIdleNrOfTiles = Constants.MINOTAUR_IN_FIGHT_IDLE_TILE_NR;
                inFightAttackingNrOfTiles = Constants.MINOTAUR_ATTACKING_TILE_NR;
                walkingMessageKey = "MINOTAUR_WALKING";
                fallingMessageKey = "MINOTAUR_FALLING";
                inFightIdleMessageKey = "MINOTAUR_IN_FIGHT_IDLE";
                inFightAttackingMessageKey = "MINOTAUR_IN_FIGHT_ATTACKING";
                break;
            case Constants.GHOST_NAME:
                sheetPath = Constants.GHOST_SPRITE_SHEET_PATH;
                passiveTileHeight = Constants.GHOST_PASSIVE_TILE_HEIGHT;
                passiveTileWidth = Constants.GHOST_PASSIVE_TILE_WIDTH;
                inFightTileWidth = Constants.GHOST_FIGHTING_TILE_WIDTH;
                inFightTileHeight = Constants.GHOST_FIGHTING_TILE_HEIGHT;
                walkingAnimationNrOfTiles = Constants.GHOST_PASSIVE_TILE_NR;
                inFightIdleNrOfTiles = Constants.GHOST_IN_FIGHT_IDLE_TILE_NR;
                inFightAttackingNrOfTiles = Constants.GHOST_ATTACKING_TILE_NR;
                walkingMessageKey = "GHOST_WALKING";
                fallingMessageKey = "GHOST_FALLING";
                inFightIdleMessageKey = "GHOST_IN_FIGHT_IDLE";
                inFightAttackingMessageKey = "GHOST_IN_FIGHT_ATTACKING";
                break;
            case Constants.STRONG_SKELETON_NAME:
                sheetPath = Constants.STRONG_SKELETON_SPRITE_SHEET_PATH;
                passiveTileHeight = Constants.STRONG_SKELETON_PASSIVE_TILE_HEIGHT;
                passiveTileWidth = Constants.STRONG_SKELETON_PASSIVE_TILE_WIDTH;
                inFightTileWidth = Constants.STRONG_SKELETON_FIGHTING_TILE_WIDTH;
                inFightTileHeight = Constants.STRONG_SKELETON_FIGHTING_TILE_HEIGHT;
                walkingAnimationNrOfTiles = Constants.STRONG_SKELETON_PASSIVE_TILE_NR;
                inFightIdleNrOfTiles = Constants.STRONG_SKELETON_IN_FIGHT_IDLE_TILE_NR;
                inFightAttackingNrOfTiles = Constants.STRONG_SKELETON_ATTACKING_TILE_NR;
                walkingMessageKey = "STRONG_SKELETON_WALKING";
                fallingMessageKey = "STRONG_SKELETON_FALLING";
                inFightIdleMessageKey = "STRONG_SKELETON_IN_FIGHT_IDLE";
                inFightAttackingMessageKey = "STRONG_SKELETON_IN_FIGHT_ATTACKING";
                break;
            default:
                System.err.println("Unknown enemy type: " + enemyType);
                return null;
        }

        // Determine subimage parameters based on state
        switch(state){
            case FALLING:
                currentCacheKey = fallingMessageKey;
                subImageX = 0;
                subImageY = passiveTileHeight * 0; // Assuming falling is the first row of passive animations
                subImageWidth = passiveTileWidth * 1; // Assuming single frame for falling
                subImageHeight = passiveTileHeight;
                break;
            case WALKING:
                currentCacheKey = walkingMessageKey;
                subImageX = 0; // Assuming walking starts at x=0 on its row
                subImageY = passiveTileHeight * 0; // Assuming walking is the first row (or adjust if different)
                subImageWidth = passiveTileWidth * walkingAnimationNrOfTiles;
                subImageHeight = passiveTileHeight;
                break;
            case IN_FIGHT_IDLE:
                currentCacheKey = inFightIdleMessageKey;
                subImageX = 0;
                // The y-coordinate needs to account for previous rows of animations.
                // Assuming passive animations (like walking, falling) are above fighting animations.
                // And within fighting, idle might be after attacking or vice-versa.
                // This specific calculation: passiveTileHeight*1 + inFightTileHeight*1
                // suggests one row of passive animations and then idle is the second row of fighting animations.
                // This needs to be consistent with the spritesheet layout.
                // For Tiger: passive (height 48) row 0. Fighting animations start after that.
                // In_Fight_Idle (height 96) is at y = 48 (passive) + 96 (attacking) = 144, if attacking is row 0 of fighting.
                // The original code has subImageY = passiveTileHeight*1 + inFightTileHeight*1
                // Let's assume spritesheet layout is:
                // Row 0: Passive (walking/falling)
                // Row 1: In_Fight_Attacking
                // Row 2: In_Fight_Idle
                // So, for IN_FIGHT_IDLE, Y = passiveTileHeight (for the passive row) + inFightTileHeight (for the attacking row)
                subImageY = passiveTileHeight + inFightTileHeight; // Adjusted based on typical sprite sheet layouts
                subImageWidth = inFightTileWidth * inFightIdleNrOfTiles;
                subImageHeight = inFightTileHeight;
                break;
            case IN_FIGHT_ATTACKING:
                currentCacheKey = inFightAttackingMessageKey;
                subImageX = 0;
                // Assuming attacking is the first row of "in-fight" animations, directly below passive animations.
                subImageY = passiveTileHeight; // Y starts after the passive animation row(s)
                subImageWidth = inFightTileWidth * inFightAttackingNrOfTiles;
                subImageHeight = inFightTileHeight;
                break;
            default:
                System.err.println("Unknown enemy state: " + state);
                return null;
        }


        try{
            if (enemyStatesSheets.containsKey(currentCacheKey)) {
                returnIMG = enemyStatesSheets.get(currentCacheKey);
            } else {
                enemyFileSheet = new File(sheetPath);
                if(!enemyFileSheet.exists()){
                    System.err.println("No file found at path " + sheetPath);
                    return null;
                }
                BufferedImage fullSheet = ImageIO.read(enemyFileSheet);
                // Validate subimage dimensions against full sheet dimensions
                if (subImageX + subImageWidth > fullSheet.getWidth() || subImageY + subImageHeight > fullSheet.getHeight() || subImageX < 0 || subImageY < 0) {
                    System.err.println("Error: Subimage dimensions are out of bounds for sheet: " + sheetPath);
                    System.err.println("Sheet: " + fullSheet.getWidth() + "x" + fullSheet.getHeight());
                    System.err.println("Subimage: x=" + subImageX + ", y=" + subImageY + ", w=" + subImageWidth + ", h=" + subImageHeight);
                    System.err.println("Enemy: " + enemyType + ", State: " + state);
                    return null;
                }
                enemyStatesSheets.put(currentCacheKey, fullSheet.getSubimage(subImageX, subImageY, subImageWidth, subImageHeight));
                returnIMG = enemyStatesSheets.get(currentCacheKey);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (java.awt.image.RasterFormatException rfe) { // Catch specific error for bad subimage
            System.err.println("RasterFormatException for enemy: " + enemyType + ", state: " + state + " from sheet: " + sheetPath);
            System.err.println("Subimage parameters: x=" + subImageX + ", y=" + subImageY + ", width=" + subImageWidth + ", height=" + subImageHeight);
            rfe.printStackTrace();
            return null;
        }

        return returnIMG;
    }


    /**
     * Retrieves a hero animation spritesheet based on the hero's current state.
     * If the spritesheet for the given state is already cached, it returns the cached version.
     * Otherwise, it loads the main hero spritesheet, extracts the relevant subimage for the state,
     * caches it, and then returns it.
     *
     * @param state The current state of the hero (e.g., IDLE, RUNNING), as defined in {@link PaooGame.Config.Constants.HERO_STATES}.
     * @return The BufferedImage for the hero's current animation state, or null if loading fails or the state is invalid.
     */
    public BufferedImage getHeroState(Constants.HERO_STATES state){
        BufferedImage returnIMG=null;
        String cacheKey = state.name(); // Use enum name as cache key
        int subImageX = 0;
        int subImageY = 0;
        int subImageWidth = 48 * 10; // Assuming 10 frames, 48px wide each
        int subImageHeight = 48;     // Assuming 48px high

        if(heroStatesSheets.containsKey(cacheKey)){
            return heroStatesSheets.get(cacheKey);
        }

        try{
            File heroFileSheet=new File(Constants.HERO_SPRITE_SHEET_PATH);
            if(!heroFileSheet.exists()){
                System.err.println("No file found at path " + Constants.HERO_SPRITE_SHEET_PATH);
                return null;
            }
            BufferedImage fullSheet = ImageIO.read(heroFileSheet);

            switch (state){
                case IDLE:
                    subImageY = 48 * 0;
                    break;
                case RUNNING:
                    subImageY = 48 * 1;
                    break;
                case JUMPING: // JUMPING and FALLING might share sprites or be distinct
                    subImageY = 48 * 2;
                    break;
                case ATTACKING:
                    subImageY = 48 * 3;
                    break;
                case CROUCHING:
                    subImageY = 48 * 4;
                    break;
                case FALLING: // Often uses same/similar sprites as JUMPING
                    subImageY = 48 * 2; // Example: uses jumping sprites; adjust if FALLING has its own row
                    // If FALLING is intended to be row 5, then subImageY = 48 * 5;
                    break;
                default:
                    System.err.println("INVALID HERO STATE GIVEN: " + state);
                    return null;
            }
            // Validate subimage dimensions
            if (subImageX + subImageWidth > fullSheet.getWidth() || subImageY + subImageHeight > fullSheet.getHeight()) {
                System.err.println("Error: Subimage dimensions are out of bounds for hero sheet for state: " + state);
                return null;
            }

            returnIMG = fullSheet.getSubimage(subImageX, subImageY, subImageWidth, subImageHeight);
            heroStatesSheets.put(cacheKey, returnIMG);

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (java.awt.image.RasterFormatException rfe) {
            System.err.println("RasterFormatException for hero state: " + state + " from sheet: " + Constants.HERO_SPRITE_SHEET_PATH);
            System.err.println("Subimage parameters: x=" + subImageX + ", y=" + subImageY + ", width=" + subImageWidth + ", height=" + subImageHeight);
            rfe.printStackTrace();
            return null;
        }
        return returnIMG;
    }
}