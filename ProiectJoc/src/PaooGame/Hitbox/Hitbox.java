package PaooGame.Hitbox;

import java.awt.*;

public class Hitbox {

    private int x,y,width,height;
    //x,y sunt coordonatele punctului dreapta sus
    //y mai mare coboara in jos

    public Hitbox(int x,int y, int width, int height) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

    public void update_Hitbox(int xToAdd,int yToAdd,int newWidth,int newHeight) {
        if(newWidth!=0 && newHeight!=0) {
            setWidth(newWidth);
            setHeight(newHeight);
        }
        setX(getX()+xToAdd);
        setY(getY()+yToAdd);
    }


    public void print_Hitbox(Graphics g) {
        Color originalColor=g.getColor();
        g.setColor(Color.red);
        g.drawRect(this.x,this.y,this.width,this.height);
        g.setColor(originalColor);
    }

    public static boolean check_HitboxCollision(Hitbox hitbox1, Hitbox hitbox2) {
        //intai verific intersectia pe axa x
        boolean isOnX=false;
        if( (hitbox2.x >= hitbox1.x && hitbox2.x <= hitbox1.x+hitbox1.width) ||
                (hitbox1.x >=hitbox2.x && hitbox1.x<=hitbox2.x+ hitbox2.width) ){
            isOnX=true;
        }
        //verific intersectia pe axa y
        boolean isOnY=false;
        if( (hitbox2.y >=hitbox1.y && hitbox2.y <=hitbox1.y+hitbox1.height) ||
                (hitbox1.y>=hitbox2.y && hitbox1.y<=hitbox2.y+hitbox2.height) ){
            isOnY=true;
        }

        return isOnX&&isOnY;
    }


    public void setX(int x) {
        this.x=x;
    }
    public void setY(int y) {
        this.y=y;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public void setWidth(int width) { this.width=width; }
    public void setHeight(int height) { this.height=height; }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }


}
