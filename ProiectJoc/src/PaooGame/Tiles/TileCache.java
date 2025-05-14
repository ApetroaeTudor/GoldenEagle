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
public class TileCache {

    private TileCache(){

    }

    private static TileCache instance=null;

    public static TileCache getInstance(){
        if(instance==null){
            return new TileCache();
        }
        return instance;
    }

    private static final Map<Entry<String,Integer>,Tile> cache = new HashMap<>();
    //mapare context-id-tileCuTextura
    private static final Map<String,BufferedImage> tileSheets =new HashMap<>();

    private static final Map<String,BufferedImage> backgrounds=new HashMap<>();

    private static final Map<String,BufferedImage> heroStatesSheets =new HashMap<>();
    private static final Map<String,BufferedImage> enemyStatesSheets =new HashMap<>();
    private static final Map<String,BufferedImage> effectsSheets =new HashMap<>();
    private static final Map<String,BufferedImage> specialsSheets =new HashMap<>();

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



    public BufferedImage getBackground(String path){
        if(backgrounds.containsKey(path)){
            return backgrounds.get(path);
        }
        else{
            try{
                File f=new File(path);
                if(!f.exists()){
                    System.err.println("Background file not found: "+ Constants.LEVEL1_BG_PATH);

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

    public BufferedImage getEffect(Constants.EFFECTS selectEffect){
        BufferedImage returnIMG = null;
        try{
            File effectFileSheet = null;
            switch (selectEffect){
                case ATTACK_EXPLOSION:
                    effectFileSheet = new File(Constants.ATTACK_EXPLOSION_SHEET_PATH);
                    if(!effectFileSheet.exists()){
                        System.err.println("Error working with effect sheet file");
                    }
                    if(!effectsSheets.containsKey("ATTACK_EXPLOSION")){
                        effectsSheets.put("ATTACK_EXPLOSION",ImageIO.read(effectFileSheet).getSubimage(0,0,112*8,112));
                    }
                    returnIMG = effectsSheets.get("ATTACK_EXPLOSION");
                    break;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return returnIMG;
    }

    public BufferedImage getSpecial(String specialPath,int tileWidth,int tileHeight, int nrOfFrames){
        BufferedImage returnIMG = null;
        try{
            File specialFileSheet = new File(specialPath);
            if(!specialFileSheet.exists()){
                System.err.println("Error working with special sheet file");
            }
            if(!specialsSheets.containsKey(specialPath)){
                specialsSheets.put(specialPath,ImageIO.read(specialFileSheet).getSubimage(0,0,tileWidth*nrOfFrames,tileHeight));
            }
            returnIMG = specialsSheets.get(specialPath);

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return returnIMG;
    }

    public BufferedImage getEnemySheetByState(Constants.ENEMY_STATES state, String enemyType){
        BufferedImage returnIMG = null;
        File enemyFileSheet;
        String sheetPath = "";
        int passiveTileWidth=0;
        int passiveTileHeight=0;
        int inFightTileWidth=0;
        int inFightTileHeight=0;
        String fallingMessage="";
        String walkingMessage="";
        String inFightIdleMessage="";
        String inFightAttackingMessage="";
        int walkingAnimationNrOfTiles=0;
        int inFightIdleNrOfTiles=0;
        int inFightAttackingNrOfTiles=0;

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
                fallingMessage = "TIGER_FALLING";
                inFightIdleMessage = "TIGER_IN_FIGHT_IDLE";
                inFightAttackingMessage = "TIGER_IN_FIGHT_ATTACKING";

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
                walkingMessage = "BASIC_SKELETON_WALKING";
                fallingMessage = "BASIC_SKELETON_FALLING";
                inFightIdleMessage = "BASIC_SKELETON_IN_FIGHT_IDLE";
                inFightAttackingMessage = "BASIC_SKELETON_IN_FIGHT_ATTACKING";
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
                walkingMessage = "WIZARD_WALKING";
                fallingMessage = "WIZARD_FALLING";
                inFightIdleMessage = "WIZARD_IN_FIGHT_IDLE";
                inFightAttackingMessage = "WIZARD_IN_FIGHT_ATTACKING";
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
                walkingMessage = "MINOTAUR_WALKING";
                fallingMessage = "MINOTAUR_FALLING";
                inFightIdleMessage = "MINOTAUR_IN_FIGHT_IDLE";
                inFightAttackingMessage = "MINOTAUR_IN_FIGHT_ATTACKING";
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
                walkingMessage = "GHOST_WALKING";
                fallingMessage = "GHOST_FALLING";
                inFightIdleMessage = "GHOST_IN_FIGHT_IDLE";
                inFightAttackingMessage = "GHOST_IN_FIGHT_ATTACKING";
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
                walkingMessage = "STRONG_SKELETON_WALKING";
                fallingMessage = "STRONG_SKELETON_FALLING";
                inFightIdleMessage = "STRONG_SKELETON_IN_FIGHT_IDLE";
                inFightAttackingMessage = "STRONG_SKELETON_IN_FIGHT_ATTACKING";
                break;
        }
        try{
            enemyFileSheet = new File(sheetPath);
                    if(!enemyFileSheet.exists()){
                        System.err.println("No file found at path " + sheetPath);
                    }
                    switch(state){
                        case FALLING:
                            if(!enemyStatesSheets.containsKey(fallingMessage)){
                                enemyStatesSheets.put(fallingMessage,ImageIO.read(enemyFileSheet).getSubimage(0,passiveTileHeight*0,passiveTileWidth*1,passiveTileHeight));
                                //scot doar prima imagine pentru ca nu am animatie de falling pe tigru
                            }
                            returnIMG = enemyStatesSheets.get(fallingMessage);
                            break;
                        case WALKING:
                            if(!enemyStatesSheets.containsKey(walkingMessage)){
                                enemyStatesSheets.put(walkingMessage,ImageIO.read(enemyFileSheet).getSubimage(0,passiveTileHeight*0+passiveTileWidth*0,passiveTileWidth*walkingAnimationNrOfTiles,passiveTileHeight));
                            }
                            returnIMG = enemyStatesSheets.get(walkingMessage);
                            break;
                        case IN_FIGHT_IDLE:
                            if(!enemyStatesSheets.containsKey(inFightIdleMessage)){
                                enemyStatesSheets.put(inFightIdleMessage,ImageIO.read(enemyFileSheet).getSubimage(0,passiveTileHeight*1+inFightTileHeight*1,inFightIdleNrOfTiles*inFightTileWidth,inFightTileHeight));
                                //aici imaginea e portrait
                            }
                            returnIMG = enemyStatesSheets.get(inFightIdleMessage);
                            break;
                        case IN_FIGHT_ATTACKING:
                            if(!enemyStatesSheets.containsKey(inFightAttackingMessage)){
                                enemyStatesSheets.put(inFightAttackingMessage,ImageIO.read(enemyFileSheet).getSubimage(0,passiveTileHeight*1+inFightTileHeight*0,inFightTileWidth*inFightAttackingNrOfTiles,inFightTileHeight));
                            }
                            returnIMG = enemyStatesSheets.get(inFightAttackingMessage);
                            break;
                    }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return returnIMG;




    }


    public BufferedImage getHeroState(Constants.HERO_STATES state){
        BufferedImage returnIMG=null;
        try{
            File heroFileSheet=new File(Constants.HERO_SPRITE_SHEET_PATH);
            if(!heroFileSheet.exists()){
                System.err.println("No file found at path " + Constants.HERO_SPRITE_SHEET_PATH);
            }
            switch (state){
                case IDLE:
                    if(!heroStatesSheets.containsKey("IDLE"))
                        heroStatesSheets.put("IDLE",ImageIO.read(heroFileSheet).getSubimage(0,48*0,48*10,48));
                    returnIMG= heroStatesSheets.get("IDLE");
                    break;
                case RUNNING:
                    if(!heroStatesSheets.containsKey("RUNNING"))
                        heroStatesSheets.put("RUNNING",ImageIO.read(heroFileSheet).getSubimage(0,48*1,48*10,48));
                    returnIMG= heroStatesSheets.get("RUNNING");
                    break;
                case JUMPING:
                    if(!heroStatesSheets.containsKey("JUMPING"))
                        heroStatesSheets.put("JUMPING",ImageIO.read(heroFileSheet).getSubimage(0,48*2,48*10,48));
                    returnIMG= heroStatesSheets.get("JUMPING");
                    break;
                case ATTACKING:
                    if(!heroStatesSheets.containsKey("ATTACKING"))
                        heroStatesSheets.put("ATTACKING",ImageIO.read(heroFileSheet).getSubimage(0,48*3,48*10,48));
                    returnIMG= heroStatesSheets.get("ATTACKING");
                    break;
                case CROUCHING:
                    if(!heroStatesSheets.containsKey("CROUCHING"))
                        heroStatesSheets.put("CROUCHING",ImageIO.read(heroFileSheet).getSubimage(0,48*4,48*10,48));
                    returnIMG= heroStatesSheets.get("CROUCHING");
                    break;
                case FALLING:
                    if(!heroStatesSheets.containsKey("FALLING"))
                        heroStatesSheets.put("FALLING",ImageIO.read(heroFileSheet).getSubimage(0,48*2,48*10,48));
                    returnIMG= heroStatesSheets.get("FALLING");
                    break;
                default:
                    System.err.println("INVALID STATE GIVEN");
                    break;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return returnIMG;
    }
}