package PaooGame.Maps;

import PaooGame.Config.Constants;

import java.io.*;

public class Level1 extends Level {


    public Level1(){
        this.visualTiles =new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.behaviorTiles =new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.visualIDs = new int[Constants.LEVEL1_TILE_NR];
        this.behaviorIDs = new int[Constants.LEVEL1_TILE_NR];
        setIDs(Constants.LEVEL1_TEXTURES_CSV,Constants.LEVEL1_BEHAVIOR_CSV);
    }
    
}