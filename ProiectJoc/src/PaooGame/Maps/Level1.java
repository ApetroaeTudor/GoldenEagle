package PaooGame.Maps;

import Entities.Entity;
import Entities.Tiger;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;
import PaooGame.Tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Level1 extends Level {


    public Level1(){
        this.VisualTiles=new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.BehaviorTiles=new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.visualIDs=new int[Constants.LEVEL1_TILE_NR];
        this.behaviorIDs=new int[Constants.LEVEL1_TILE_NR];




        String line;
        try
        {
            int i=0;
            BufferedReader br=new BufferedReader(new FileReader(Constants.LEVEL1_TEXTURES_CSV));
            BufferedReader br2=new BufferedReader(new FileReader(Constants.LEVEL1_BEHAVIOR_CSV));
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
//                    this.BehaviorTiles[i/70][i%70]=Integer.parseInt(token);
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

    }


    void update(){
    }


}