package PaooGame.Tiles;
import Entities.Entity;
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
    private static final Map<String,BufferedImage> tilesheets=new HashMap<>();

    private static final Map<String,BufferedImage> backgrounds=new HashMap<>();

    private static final Map<String,BufferedImage> HeroStates=new HashMap<>();
    private static final Map<String,BufferedImage> TigerStates=new HashMap<>();
    private static final Map<String,BufferedImage> Effects=new HashMap<>();

    public Tile getTile(String path, int id){
        Entry<String,Integer> key=new AbstractMap.SimpleEntry<>(path,id);
        if(cache.containsKey(key)){
            return cache.get(key);
        }

        BufferedImage sheet= tilesheets.computeIfAbsent(path,p->{
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
                    if(!Effects.containsKey("ATTACK_EXPLOSION")){
                        Effects.put("ATTACK_EXPLOSION",ImageIO.read(effectFileSheet).getSubimage(0,0,112*8,112));
                    }
                    returnIMG = Effects.get("ATTACK_EXPLOSION");
                    break;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return returnIMG;
    }

    public BufferedImage getTigerState(Constants.ENEMY_STATES state){
        BufferedImage returnIMG = null;
        try{
            File tigerFileSheet = new File(Constants.TIGER_SPRITE_SHEET_PATH);
            if(!tigerFileSheet.exists()){
                System.err.println("No file found at path " + Constants.TIGER_SPRITE_SHEET_PATH);
            }
            switch(state){
                case FALLING:
                    if(!TigerStates.containsKey("FALLING")){
                        TigerStates.put("FALLING",ImageIO.read(tigerFileSheet).getSubimage(0,32*0,64*1,32));
                        //scot doar prima imagine pentru ca nu am animatie de falling pe tigru
                    }
                    returnIMG = TigerStates.get("FALLING");
                    break;
                case WALKING:
                    if(!TigerStates.containsKey("WALKING")){
                        TigerStates.put("WALKING",ImageIO.read(tigerFileSheet).getSubimage(0,32*0+64*0,64*4,32));
                    }
                    returnIMG = TigerStates.get("WALKING");
                    break;
                case IN_FIGHT_IDLE:
                    if(!TigerStates.containsKey("IN_FIGHT_IDLE")){
                        TigerStates.put("IN_FIGHT_IDLE",ImageIO.read(tigerFileSheet).getSubimage(0,32*1+64*1,32*1,64));
                        //aici imaginea e portrait
                    }
                    returnIMG = TigerStates.get("IN_FIGHT_IDLE");
                    break;
                case IN_FIGHT_ATTACKING:
                    if(!TigerStates.containsKey("IN_FIGHT_ATTACKING")){
                        TigerStates.put("IN_FIGHT_ATTACKING",ImageIO.read(tigerFileSheet).getSubimage(0,32*1+64*0,32*4,64));
                    }
                    returnIMG = TigerStates.get("IN_FIGHT_ATTACKING");
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
                    if(!HeroStates.containsKey("IDLE"))
                        HeroStates.put("IDLE",ImageIO.read(heroFileSheet).getSubimage(0,48*0,48*10,48));
                    returnIMG=HeroStates.get("IDLE");
                    break;
                case RUNNING:
                    if(!HeroStates.containsKey("RUNNING"))
                        HeroStates.put("RUNNING",ImageIO.read(heroFileSheet).getSubimage(0,48*1,48*10,48));
                    returnIMG=HeroStates.get("RUNNING");
                    break;
                case JUMPING:
                    if(!HeroStates.containsKey("JUMPING"))
                        HeroStates.put("JUMPING",ImageIO.read(heroFileSheet).getSubimage(0,48*2,48*10,48));
                    returnIMG=HeroStates.get("JUMPING");
                    break;
                case ATTACKING:
                    if(!HeroStates.containsKey("ATTACKING"))
                        HeroStates.put("ATTACKING",ImageIO.read(heroFileSheet).getSubimage(0,48*3,48*10,48));
                    returnIMG=HeroStates.get("ATTACKING");
                    break;
                case CROUCHING:
                    if(!HeroStates.containsKey("CROUCHING"))
                        HeroStates.put("CROUCHING",ImageIO.read(heroFileSheet).getSubimage(0,48*4,48*10,48));
                    returnIMG=HeroStates.get("CROUCHING");
                    break;
                case FALLING:
                    if(!HeroStates.containsKey("FALLING"))
                        HeroStates.put("FALLING",ImageIO.read(heroFileSheet).getSubimage(0,48*2,48*10,48));
                    returnIMG=HeroStates.get("FALLING");
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