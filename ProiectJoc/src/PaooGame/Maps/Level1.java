package PaooGame.Maps;

import PaooGame.Config.Constants;
import PaooGame.RefLinks;
import PaooGame.Tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Level1 {
    private int [][] VisualTiles;
    private int [][] BehaviorTiles;
    int[] visualIDs;
    int[] behaviorIDs;


    private final String Level1TexturesPath="res/Level1/Level1Textures.png";
    private final String Level1TexturesCSV="res/Level1/Level1Textures.csv";
    private final String Level1BehaviorCSV="res/Level1/Level1Behavior.csv";
    private final String Level1BackgroundPath="res/Level1/Level1Background.png";
    private BufferedImage backgroundImage=null;

    public int[] getVisualIDs(){return this.visualIDs;}
    public int[] getBehaviorIDs(){return this.behaviorIDs;}
    public int[][] getVisualTiles(){return this.VisualTiles;}
    public int[][] getBehaviorTiles(){return this.BehaviorTiles;}
    public String getLevel1TexturesPath(){return this.Level1TexturesPath;}
    public String getLevel1TexturesCSV(){return this.Level1TexturesCSV;}
    public String getLevel1BehaviorCSV(){return this.Level1BehaviorCSV;}
    public String getLevel1BackgroundPath(){return this.Level1BackgroundPath;}
    public BufferedImage getBackgroundImage(){
        if(backgroundImage==null){
            try{
                File f=new File(this.Level1BackgroundPath);
                if(!f.exists()){
                    System.err.println("Background file not found: "+ this.Level1BackgroundPath);
                    this.backgroundImage=null;
                }
                else{
                    this.backgroundImage= ImageIO.read(f);
                }
            } catch (IOException e) {
                e.printStackTrace();
                backgroundImage=null;
            }
        }
        return backgroundImage;
    }

    public Level1(){
        this.VisualTiles=new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.BehaviorTiles=new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.visualIDs=new int[Constants.LEVEL1_TILE_NR];
        this.behaviorIDs=new int[Constants.LEVEL1_TILE_NR];




        String line;
        try
        {
            int i=0;
            BufferedReader br=new BufferedReader(new FileReader(Level1TexturesCSV));
            BufferedReader br2=new BufferedReader(new FileReader(Level1BehaviorCSV));
            while((line = br.readLine())!=null){
                String[] tokens;
                tokens=line.split(",");
                for(String token: tokens){
                    visualIDs[i++]=Integer.parseInt(token);
                }
            }
            i=0;
            while((line=br2.readLine())!=null){
                String[] tokens;
                tokens=line.split(",");
                for(String token: tokens){
                    behaviorIDs[i++]=Integer.parseInt(token);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("file not found");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("IoException");
        }
        catch(NumberFormatException e){
            e.printStackTrace();
            System.err.println("NumberFormatException");
        }

        //loading tiles
    }

    void update(){

    }



//    void draw(){
//        for(int i=0;i<3150;i++){
//            reflink.getTileCache().getTile(Level1TexturesPath,visualIDs[i]).Draw(reflink.g);
//        }
//    }


}
