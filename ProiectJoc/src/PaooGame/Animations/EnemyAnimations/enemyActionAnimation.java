package PaooGame.Animations.EnemyAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class enemyActionAnimation extends Animation {

    private Constants.ENEMY_STATES purpose;
    private String entityName;

    public enemyActionAnimation(RefLinks reflink, Constants.ENEMY_STATES purpose, int nrOfFrames, int animationSpeed, String entityName){
        super();
        this.purpose = purpose;
        this.reflink = reflink;
        this.nrOfFrames = nrOfFrames;
        this.entityName = entityName;

        if(entityName == Constants.TIGER_NAME){
            this.imageSheet = reflink.getTileCache().getEnemySheetByState(purpose,Constants.TIGER_NAME);
            switch (purpose){
                case FALLING:
                case WALKING:
                    this.imgWidth = Constants.TIGER_PASSIVE_TILE_WIDTH;
                    this.imgHeight = Constants.TIGER_PASSIVE_TILE_HEIGHT;
                    break;
                case IN_FIGHT_IDLE:
                    this.imgWidth = Constants.TIGER_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.TIGER_FIGHTING_TILE_HEIGHT;
                    this.playOnce = false;
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.TIGER_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.TIGER_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true;
                    break;
            }

        }
        else if(entityName == Constants.BASIC_SKELETON_NAME){
            this.imageSheet = reflink.getTileCache().getEnemySheetByState(purpose,Constants.BASIC_SKELETON_NAME);
            switch (purpose){
                case FALLING:
                case WALKING:
                    this.imgWidth = Constants.BASIC_SKELETON_PASSIVE_TILE_WIDTH;
                    this.imgHeight = Constants.BASIC_SKELETON_PASSIVE_TILE_HEIGHT;
                    break;
                case IN_FIGHT_IDLE:
                    this.imgWidth = Constants.BASIC_SKELETON_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.BASIC_SKELETON_FIGHTING_TILE_HEIGHT;
                    this.playOnce = false;
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.BASIC_SKELETON_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.BASIC_SKELETON_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true;
                    break;
            }
        }
        else if(entityName == Constants.WIZARD_NAME){
            this.imageSheet = reflink.getTileCache().getEnemySheetByState(purpose,Constants.WIZARD_NAME);
            switch (purpose){
                case FALLING:
                case WALKING:
                    this.imgWidth = Constants.WIZARD_PASSIVE_TILE_WIDTH;
                    this.imgHeight = Constants.WIZARD_PASSIVE_TILE_HEIGHT;
                    break;
                case IN_FIGHT_IDLE:
                    this.imgWidth = Constants.WIZARD_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.WIZARD_FIGHTING_TILE_HEIGHT;
                    this.playOnce = false;
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.WIZARD_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.WIZARD_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true;
                    break;
            }

        }


        this.animationArray = new BufferedImage[this.nrOfFrames];

        this.animationSpeed = animationSpeed;
    }

    @Override
    public void loadAnimation(){
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
            if(this.entityName == "Tiger"){
                scale = 0.5;
            }
            if(this.purpose == Constants.ENEMY_STATES.IN_FIGHT_IDLE || this.purpose == Constants.ENEMY_STATES.IN_FIGHT_ATTACKING){
                scale = 5;
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



