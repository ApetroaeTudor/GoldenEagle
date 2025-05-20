package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

public class BoosterItem extends Item {

    public BoosterItem(RefLinks reflink, int x, int y){
        super();
        this.x =x;
        this.y =y;
        this.itemName = Constants.BOOSTER_ITEM_NAME;
        this.itemSheetPath = Constants.BOOSTER_PATH;
        this.hitbox = new Hitbox(this.x, this.y,Constants.BOOSTER_IMG_WIDTH,Constants.BOOSTER_IMG_HEIGHT);
        this.animation = new StaticItemAnimation(reflink,this.itemSheetPath,1,1,Constants.BOOSTER_IMG_WIDTH,Constants.BOOSTER_IMG_HEIGHT);
        this.animation.loadAnimation();
    }

    @Override
    public void drawItem(Graphics g) {
        this.animation.paintAnimation(g,this.x,this.y,false,1);
    }

    @Override
    public void updateItem() {
        this.animation.updateAnimation();
    }
}
