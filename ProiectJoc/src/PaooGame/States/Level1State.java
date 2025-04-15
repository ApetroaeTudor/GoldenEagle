
package PaooGame.States;
import PaooGame.Items.Hero; // adaugă importul pentru Hero
import PaooGame.Config.Constants;
import PaooGame.Maps.Level1;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Level1State extends State {
    private Level1 level1;
    private Hero hero;
    private BufferedImage backgroundImage;

    public Level1State(RefLinks refLink) {
        super(refLink);
        level1 = new Level1();
        hero = new Hero(refLink, 100, 470);
    }

    @Override
    public void Update() {
        hero.Update();
    }

    @Override
    public void Draw(Graphics g) {
        BufferedImage backgroundImage = level1.getBackgroundImage();
        g.drawImage(backgroundImage, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);

        for (int i = 0; i < Constants.LEVEL1_TILE_NR; ++i) {
            int currentID = this.level1.getVisualIDs()[i];
            if (currentID != -1) {
                this.refLink.getTileCache()
                        .getTile(this.level1.getLevel1TexturesPath(), currentID)
                        .Draw(g, (i % 70) * 16, (i / 70) * 16);
            }
        }
        hero.Draw(g); // desenează eroul după tile-uri
    }
}

