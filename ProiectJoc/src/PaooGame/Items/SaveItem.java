package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

public class SaveItem extends Item{


    public SaveItem(RefLinks reflink,int x,int y){
        super();
        this.x = x;
        this.y = y;
        this.itemName = Constants.BONFIRE_NAME;
        this.itemSheetPath = Constants.BONFIRE_SHEET_PATH;

        this.hitbox = new Hitbox(this.x,this.y,Constants.BONFIRE_TILE_SIZE,Constants.BONFIRE_TILE_SIZE);
        this.animation = new StaticItemAnimation(reflink,this.itemSheetPath,4,5,Constants.BONFIRE_TILE_SIZE,Constants.BONFIRE_TILE_SIZE);
        this.animation.loadAnimation();
    }




    @Override
    public void drawItem(Graphics g) {
        this.animation.paintAnimation(g,this.x,this.y,false);
    }

    @Override
    public void updateItem() {
        this.animation.updateAnimation();
    }
}
