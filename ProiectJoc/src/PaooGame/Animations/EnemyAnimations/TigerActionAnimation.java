package PaooGame.Animations.EnemyAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TigerActionAnimation extends Animation {

    private Constants.ENEMY_STATES purpose;

    public TigerActionAnimation(RefLinks reflink, Constants.ENEMY_STATES purpose, int nrOfFrames, int animationSpeed){
        super();
        this.purpose = purpose;
        this.reflink = reflink;
        this.imageSheet = reflink.getTileCache().getTigerState(purpose);
        this.nrOfFrames = nrOfFrames;
        switch (purpose){
            case WALKING:
                this.ImgWidth = Constants.TIGER_PASSIVE_TILE_WIDTH;
                this.ImgHeight = Constants.TIGER_PASSIVE_TILE_HEIGHT;
                break;
            default:
                this.ImgWidth = Constants.TIGER_FIGHTING_TILE_WIDTH;
                this.ImgHeight = Constants.TIGER_FIGHTING_TILE_HEIGHT;
                break;
        }

        this.animationArray = new BufferedImage[this.nrOfFrames];

        this.animationSpeed = animationSpeed;
    }

    @Override
    public void loadAnimation(){
        for(int i = 0; i<this.nrOfFrames; ++i){

            animationArray[i] = this.imageSheet.getSubimage(i*this.ImgWidth,0,this.ImgWidth,this.ImgHeight);
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
    public void paintAnimation(Graphics g, int x, int y, boolean flipped) {
        // Ensure animationArray is loaded and valid
        if (animationArray == null || animationArray[animationState] == null) {
            // Optionally draw a placeholder or log an error
            // g.setColor(Color.RED);
            // g.fillRect(x, y, (int)(ImgWidth * scale), (int)(ImgHeight * scale)); // Draw red box of scaled size
            return; // Do nothing if animation isn't ready
        }

        BufferedImage currentFrame = animationArray[animationState];
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the graphics context

        try {
            // 1. Create the core transformation: Translate to position (x, y) and Scale
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
            transform.scale(0.5, 0.5); // Apply the 0.5 scaling factor

            // 2. If flipped, modify the transform
            if (flipped) {
                // Translate the origin to the right edge *of the scaled image*
                // The width used here should be the original width, as the scaling
                // factor is already applied to the coordinate system by transform.scale()
                transform.translate(ImgWidth, 0);
                // Flip horizontally by scaling X by -1
                transform.scale(-1, 1);
            }

            // 3. Draw the image using the final transform
            // This drawImage version uses the transform to handle position, scale, and flip
            g2d.drawImage(currentFrame, transform, null);

        } finally {
            g2d.dispose(); // Dispose of the graphics copy to restore original state
        }
    }


    }



