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
        setIDs(Constants.LEVEL2_TEXTURES_CSV,Constants.LEVEL2_BEHAVIOR_CSV);
    }
}
