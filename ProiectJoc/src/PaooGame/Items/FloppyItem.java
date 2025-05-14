package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.FloatingItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

public class FloppyItem extends Item{
    private boolean floppyFloatingUp = true;
    protected int floatingTick;
    protected int floatingTickCap;

    private int initialY;



    public FloppyItem(RefLinks reflink, int x, int y){
        super();
        this.initialY = y;
        this.floatingTick = 0;
        this.floatingTickCap = 5;
        this.x = x;
        this.y = y;
        this.itemName = Constants.SAVE_ITEM_NAME;
        this.itemSheetPath = Constants.SAVE_ITEM_PATH;
        this.hitbox = new Hitbox(this.x,this.y,Constants.SAVE_ITEM_TILE_SIZE,Constants.SAVE_ITEM_TILE_SIZE);
        this.animation = new FloatingItemAnimation(reflink,this.itemSheetPath,18,5,Constants.SAVE_ITEM_TILE_SIZE,Constants.SAVE_ITEM_TILE_SIZE);
        this.animation.loadAnimation();
    }

    @Override
    public void updateItem(){
        this.animation.updateAnimation();
    }

    @Override
    public void drawItem(Graphics g){
        this.animation.paintAnimation(g,this.x,this.y,false,1);
        executeWhipFloating();
    }

    private void executeWhipFloating(){
        if(this.floppyFloatingUp){
            this.floatingTick++;
            if(this.floatingTick>=this.floatingTickCap){
                this.y+=1;
                this.floatingTick = 0;
            }
            if(this.y>=this.initialY+4){
                this.floppyFloatingUp = false;
            }
        }
        else{
            this.floatingTick++;
            if(this.floatingTick>=this.floatingTickCap){
                this.y-=1;
                this.floatingTick = 0;
            }
            if(this.y<=this.initialY-4){
                this.floppyFloatingUp = true;
            }
        }
    }


}
