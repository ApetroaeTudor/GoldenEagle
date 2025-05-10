package PaooGame.Items;

import PaooGame.Animations.Animation;
import PaooGame.Hitbox.Hitbox;

import java.awt.*;

public abstract class Item {
    protected int x;
    protected int y;
    protected String itemName;
    protected String itemSheetPath;
    protected int floatingTick;
    protected int floatingTickCap;
    protected Hitbox hitbox;
    protected Animation floatAnimation;

    public Item(){
        this.floatingTick = 0;
        this.floatingTickCap = 5;
    }

    public Hitbox getHitbox() {return this.hitbox;}
    public String getItemName() {return this.itemName;}
    public String getItemSheetPath() {return this.itemSheetPath;}

    public abstract void drawItem(Graphics g);
    public abstract void updateItem();


}
