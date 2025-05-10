package PaooGame.Animations.ItemsAnimations;

import PaooGame.Animations.Animation;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class FloatingItemAnimation extends Animation {
    private String itemSheetPath;

    public FloatingItemAnimation(RefLinks reflink,String itemSheetPath,int nrOfFrames,int animationSpeed, int imgWidth,int imgHeight){
        super();
        this.playOnce = false;
        this.reflink = reflink;
        this.itemSheetPath = itemSheetPath;
        this.nrOfFrames = nrOfFrames;
        this.animationSpeed = animationSpeed;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.imageSheet = reflink.getTileCache().getSpecial(this.itemSheetPath,this.imgWidth,this.imgHeight,this.nrOfFrames);
        this.animationArray = new BufferedImage[this.nrOfFrames];

    }

    @Override
    public void loadAnimation() {
        for(int i = 0; i<this.nrOfFrames; ++i){
            animationArray[i] = this.imageSheet.getSubimage(i*this.imgWidth,0,this.imgWidth,this.imgHeight);
        }
    }

    @Override
    public void updateAnimation() {
        if(this.playOnce && this.isFinished){
            return;
        }
        tick++;
        if(tick>=animationSpeed) {
            tick=0;
            animationState++;
            if(animationState>=nrOfFrames) {
                this.isFinished = true;
                animationState=0;
            }
        }
    }

    @Override
    public void triggerOnce() {
        this.isFinished = false;
        this.animationState = 0;
        this.tick = 0;

    }

    @Override
    public void paintAnimation(Graphics g, int x, int y, boolean flipped) {

        if(this.playOnce && this.isFinished){
            return;
        }

        if (animationArray == null || animationArray[animationState] == null) {
            return; // Do nothing if animation isn't ready
        }

        BufferedImage currentFrame = animationArray[animationState];
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the graphics context

        try {
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
            double scale = 1;

            transform.scale(scale, scale);

            if (flipped) {
                transform.translate(imgWidth, 0);
                transform.scale(-1, 1);
            }

            g2d.drawImage(currentFrame, transform, null);

        } finally {
            g2d.dispose();
        }
    }
}
