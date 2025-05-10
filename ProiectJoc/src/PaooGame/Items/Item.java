package PaooGame.Items;

import PaooGame.Animations.Animation;
import PaooGame.Hitbox.Hitbox;

import java.awt.*;

public abstract class Item {
    protected int x;
    protected int y;
    protected String itemName;
    protected String itemSheetPath;
    protected Hitbox hitbox;
    protected Animation animation;

    public Item(){}

    public Hitbox getHitbox() {return this.hitbox;}
    public String getItemName() {return this.itemName;}
    public String getItemSheetPath() {return this.itemSheetPath;}

    public abstract void drawItem(Graphics g);
    public abstract void updateItem();


}
