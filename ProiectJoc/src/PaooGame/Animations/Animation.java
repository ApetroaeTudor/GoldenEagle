package PaooGame.Animations;

import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Animation {


    protected BufferedImage imageSheet;
    protected BufferedImage[] animationArray;

    protected int animationState;
    protected int animationSpeed;
    protected int nrOfFrames;

    protected boolean isFinished = true;
    protected boolean playOnce = false;

    protected RefLinks reflink;

    protected int tick;

    protected int imgWidth;
    protected int imgHeight;

    protected Animation(){
        animationState=0;
        animationSpeed=0;
        nrOfFrames=0;
        tick=0;
        imgHeight =0;
        imgWidth =0;
    }

    public abstract void loadAnimation();
    public abstract void updateAnimation();

    public abstract void triggerOnce();

    public abstract void paintAnimation(Graphics g, int x, int y,boolean flipped,double scale);

    public boolean getIsFinished(){return this.isFinished;}
    public boolean getPlayOnce(){return this.playOnce;}
    public void setIsFinished(boolean isFinished){this.isFinished = isFinished;}
}