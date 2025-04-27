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

    protected RefLinks reflink;

    protected int tick;

    protected int ImgWidth;
    protected int ImgHeight;

    protected Animation(){
        animationState=0;
        animationSpeed=0;
        nrOfFrames=0;
        tick=0;
        ImgHeight=0;
        ImgWidth=0;
    }

    public abstract void loadAnimation();
    public abstract void updateAnimation();

    public abstract void paintAnimation(Graphics g, int x, int y,boolean flipped);
}