package PaooGame.Maps;

import PaooGame.Config.Constants;

import java.awt.image.BufferedImage;

public class Level3 extends Level{

    public Level3(){
        this.visualTiles = new int[Constants.LEVEL3_WIDTH][Constants.LEVEL3_HEIGHT];
        this.visualIDs = new int[Constants.LEVEL3_TILE_NR];
        this.behaviorIDs = new int[Constants.LEVEL3_TILE_NR];
        setIDs(Constants.LEVEL3_TEXTURES_CSV,Constants.LEVEL3_BEHAVIOR_CSV);
    }
}
