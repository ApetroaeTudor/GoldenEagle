package PaooGame.Animations.EffectsAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class EffectAnimation extends Animation {

    private Constants.EFFECTS selectedEffect;

    public EffectAnimation(RefLinks refLink,Constants.EFFECTS selectedEffect,int nrOfFrames,int animationSpeed){
        super();
        this.playOnce = true;
        this.reflink = refLink;
        this.selectedEffect = selectedEffect;
        this.nrOfFrames = nrOfFrames;
        this.animationSpeed = animationSpeed;
        this.imageSheet = refLink.getTileCache().getEffect(this.selectedEffect);
        this.animationArray = new BufferedImage[this.nrOfFrames];
        switch (selectedEffect){
            case ATTACK_EXPLOSION:
                this.imgWidth = Constants.ATTACK_EXPLOSION_TILE_SIZE;
                this.imgHeight = Constants.ATTACK_EXPLOSION_TILE_SIZE;
                break;
        }
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
    public void paintAnimation(Graphics g, int x, int y, boolean flipped,double scale) {

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
            switch (this.selectedEffect){
                case ATTACK_EXPLOSION:
                    scale = 3;
            }



            transform.scale(scale, scale);

            if (flipped) {
                transform.translate(imgWidth, 0);
                transform.scale(-1, 1);
            }

            g2d.drawImage(currentFrame, transform, null);

        } finally {
            g2d.dispose(); // Dispose of the graphics copy to restore original state
        }
    }
}
