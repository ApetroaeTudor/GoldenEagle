package PaooGame.Animations.PlayerAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PlayerActionAnimation extends Animation {

    private Constants.HERO_STATES purpose;

    public PlayerActionAnimation(RefLinks reflink, Constants.HERO_STATES purpose, int nrOfFrames, int animationSpeed){
        super();
        this.purpose=purpose;


        this.reflink=reflink;
        this.imageSheet=reflink.getTileCache().getHeroState(purpose);
        this.nrOfFrames=nrOfFrames;
        this.ImgHeight=Constants.CHARACTER_TILE_SIZE;
        this.ImgWidth=Constants.CHARACTER_TILE_SIZE;
        this.animationArray=new BufferedImage[this.nrOfFrames];


        this.animationSpeed=animationSpeed; //mai mare =  mai lent
    }

    @Override
    public void loadAnimation() {
        for(int i=0;i<this.nrOfFrames;++i){
            animationArray[i]=this.imageSheet.getSubimage(i*this.ImgWidth,0,this.ImgWidth,this.ImgHeight);
        }
    }

    @Override
    public void updateAnimation() {
        tick++;
        if(tick>=animationSpeed) {
            tick=0;
            animationState++;
            if(animationState>=nrOfFrames) {
                animationState=0;
            }
        }
    }

    @Override
    public void paintAnimation(Graphics g, int x, int y,boolean flipped) {
        if (flipped){
            BufferedImage currentFrame=animationArray[animationState];
            Graphics2D g2d = (Graphics2D) g.create(); // 1. Create a copy
            try {
                // 2. Define the transformation (Translate to right edge, then scale)
                AffineTransform transform = AffineTransform.getTranslateInstance(x + ImgWidth, y);
                transform.scale(-1, 1); // Flip horizontally around the translated origin

                // 3. Draw using the Graphics2D object and the transform
                g2d.drawImage(currentFrame, transform, null);

            } finally {
                g2d.dispose(); // 4. Dispose of the copy to restore original graphics state
            }
        }
        else{
            g.drawImage(animationArray[animationState], x, y, this.ImgWidth, this.ImgHeight, null);
        }


    }
}