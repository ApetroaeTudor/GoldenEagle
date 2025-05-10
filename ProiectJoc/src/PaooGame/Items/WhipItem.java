package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.FloatingItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;
import java.sql.Ref;

public class WhipItem extends Item{
    private boolean whipFloatingUp = true;

    public WhipItem(RefLinks reflink,int x, int y){
        super();
        this.x = x;
        this.y = y;
        this.itemName = Constants.WHIP_NAME;
        this.itemSheetPath = Constants.WHIP_FRAMED_SHEET_PATH;
        this.hitbox = new Hitbox(this.x,this.y,Constants.ITEM_FLOATING_TILE_SIZE,Constants.ITEM_FLOATING_TILE_SIZE);
        this.floatAnimation = new FloatingItemAnimation(reflink,this.itemSheetPath,4,5,Constants.ITEM_FLOATING_TILE_SIZE,Constants.ITEM_FLOATING_TILE_SIZE);
        this.floatAnimation.loadAnimation();
    }

    @Override
    public void updateItem(){
        this.floatAnimation.updateAnimation();
    }

    @Override
    public void drawItem(Graphics g){
        this.floatAnimation.paintAnimation(g,Constants.WHIP_POSITION_X,this.y,false);
        executeWhipFloating();
    }

    private void executeWhipFloating(){
        if(this.whipFloatingUp){
            this.floatingTick++;
            if(this.floatingTick>=this.floatingTickCap){
                this.y+=1;
                this.floatingTick = 0;
            }
            if(this.y>=Constants.WHIP_POSITION_Y+4){
                this.whipFloatingUp = false;
            }
        }
        else{
            this.floatingTick++;
            if(this.floatingTick>=this.floatingTickCap){
                this.y-=1;
                this.floatingTick = 0;
            }
            if(this.y<=Constants.WHIP_POSITION_Y-4){
                this.whipFloatingUp = true;
            }
        }
    }

}
