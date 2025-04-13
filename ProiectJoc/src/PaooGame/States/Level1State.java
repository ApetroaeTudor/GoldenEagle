
package PaooGame.States;

import PaooGame.Config.Constants;
import PaooGame.Maps.Level1;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Level1State extends State  {
    private Level1 level1;
    private BufferedImage backgroundImage;

    public Level1State(RefLinks refLink) {
        super(refLink);
        level1=new Level1();

    }

    @Override
    public void Update() {

    }

    @Override
    public void Draw(Graphics g) {

        BufferedImage backgroundImage=level1.getBackgroundImage();
        g.drawImage(backgroundImage,0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        for(int i = 0; i< Constants.LEVEL1_TILE_NR; ++i){
            int currentID=this.level1.getVisualIDs()[i];
            if(currentID!=-1){
                this.refLink.getTileCache()
                        .getTile(this.level1.getLevel1TexturesPath(),currentID)
                        .Draw(g,(i%70)*16,(i/70)*16);
            }
        }
    }
}

