package PaooGame.Maps;

import PaooGame.Config.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Level2 extends Level{

    public Level2(){
        this.visualTiles = new int[Constants.LEVEL2_WIDTH][Constants.LEVEL2_HEIGHT];
        this.behaviorTiles = new int[Constants.LEVEL2_WIDTH][Constants.LEVEL2_HEIGHT];
        this.visualIDs = new int[Constants.LEVEL2_TILE_NR];
        this.behaviorIDs = new int[Constants.LEVEL2_TILE_NR];

        String line;

        try{
            int i = 0;
            BufferedReader br = new BufferedReader(new FileReader(Constants.LEVEL2_TEXTURES_CSV));
            BufferedReader br2 = new BufferedReader(new FileReader(Constants.LEVEL2_BEHAVIOR_CSV));

            while((line = br.readLine())!=null){
                String [] tokens;
                tokens = line.split(",");
                for(String token: tokens){
                    visualIDs[i++]=Integer.parseInt(token);
                }
            }
            i = 0;
            while( (line = br2.readLine())!=null ){
                String [] tokens;
                tokens = line.split(",");
                for(String token : tokens){
                    behaviorIDs[i++] = Integer.parseInt(token);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("File not found in loading tiles in level2");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IoException In loading tiles in level2");
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.out.println("NumberFormatException in loading tiles in level2");
        }
    }
}
