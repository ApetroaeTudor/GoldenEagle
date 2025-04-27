package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

import PaooGame.Config.Constants;
import PaooGame.Config.Constants.*;

public class Tile
{
    public static final int TILE_WIDTH  = 48;
    public static final int TILE_HEIGHT = 48;

    protected BufferedImage img;
    protected final int id;

    public Tile(BufferedImage image, int id)
    {
        img = image;
        this.id = id;
    }

    public void Update()
    {

    }

    public static Tile loadTile(BufferedImage sheet, int id){
        int row=id/32;
        int col=id%32;
        BufferedImage tileImage=sheet.getSubimage(col*Constants.TILE_SIZE,row*Constants.TILE_SIZE,Constants.TILE_SIZE,Constants.TILE_SIZE);

        return new Tile(tileImage,id);
    }

    public void Draw(Graphics g, int x, int y)
    {
        g.drawImage(img, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
    }

    public int GetId()
    {
        return id;
    }
}