package PaooGame.Animations.EnemyAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @class enemyActionAnimation
 * @brief Manages and renders animations for various enemy actions.
 *
 * This class extends the base Animation class to handle animations specific to enemies,
 * such as walking, falling, idling in a fight, or attacking. It loads animation
 * frames from spritesheets based on the enemy type and its current state (purpose).
 */
public class enemyActionAnimation extends Animation {

    private Constants.ENEMY_STATES purpose; ///< The current action/state of the enemy (e.g., WALKING, IN_FIGHT_ATTACKING).
    private String entityName; ///< The name identifier of the enemy type (e.g., TIGER_NAME, BASIC_SKELETON_NAME).

    /**
     * @brief Constructs an enemyActionAnimation object.
     * @param reflink A reference to the game's shared resources and utilities.
     * @param purpose The specific action or state the animation represents.
     * @param nrOfFrames The total number of frames in the animation sequence.
     * @param animationSpeed The speed at which the animation plays (ticks per frame).
     * @param entityName The name of the enemy entity this animation is for.
     */
    public enemyActionAnimation(RefLinks reflink, Constants.ENEMY_STATES purpose, int nrOfFrames, int animationSpeed, String entityName){
        super();
        this.purpose = purpose;
        this.reflink = reflink;
        this.nrOfFrames = nrOfFrames;
        this.entityName = entityName;

        // Configure animation properties based on the enemy name and purpose.
        if(entityName.equals(Constants.TIGER_NAME)){ // Use .equals for string comparison
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
                    this.playOnce = false; // Idle animations usually loop.
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.TIGER_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.TIGER_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true; // Attack animations usually play once.
                    break;
            }

        }
        else if(entityName.equals(Constants.BASIC_SKELETON_NAME)){
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
        else if(entityName.equals(Constants.WIZARD_NAME)){
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
        else if(entityName.equals(Constants.MINOTAUR_NAME)){
            this.imageSheet = reflink.getTileCache().getEnemySheetByState(purpose,Constants.MINOTAUR_NAME);
            switch (purpose){
                case FALLING:
                case WALKING:
                    this.imgWidth = Constants.MINOTAUR_PASSIVE_TILE_WIDTH;
                    this.imgHeight = Constants.MINOTAUR_PASSIVE_TILE_HEIGHT;
                    break;
                case IN_FIGHT_IDLE:
                    this.imgWidth = Constants.MINOTAUR_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.MINOTAUR_FIGHTING_TILE_HEIGHT;
                    this.playOnce = false;
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.MINOTAUR_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.MINOTAUR_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true;
                    break;
            }
        }
        else if(entityName.equals(Constants.GHOST_NAME)){
            this.imageSheet = reflink.getTileCache().getEnemySheetByState(purpose,Constants.GHOST_NAME);
            switch (purpose){
                case FALLING:
                case WALKING:
                    this.imgWidth = Constants.GHOST_PASSIVE_TILE_WIDTH;
                    this.imgHeight = Constants.GHOST_PASSIVE_TILE_HEIGHT;
                    break;
                case IN_FIGHT_IDLE:
                    this.imgWidth = Constants.GHOST_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.GHOST_FIGHTING_TILE_HEIGHT;
                    this.playOnce = false;
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.GHOST_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.GHOST_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true;
                    break;
            }
        }
        else if(entityName.equals(Constants.STRONG_SKELETON_NAME)){
            this.imageSheet = reflink.getTileCache().getEnemySheetByState(purpose,Constants.STRONG_SKELETON_NAME);
            switch (purpose){
                case FALLING:
                case WALKING:
                    this.imgWidth = Constants.STRONG_SKELETON_PASSIVE_TILE_WIDTH;
                    this.imgHeight = Constants.STRONG_SKELETON_PASSIVE_TILE_HEIGHT;
                    break;
                case IN_FIGHT_IDLE:
                    this.imgWidth = Constants.STRONG_SKELETON_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.STRONG_SKELETON_FIGHTING_TILE_HEIGHT;
                    this.playOnce = false;
                    break;
                case IN_FIGHT_ATTACKING:
                    this.imgWidth = Constants.STRONG_SKELETON_FIGHTING_TILE_WIDTH;
                    this.imgHeight = Constants.STRONG_SKELETON_FIGHTING_TILE_HEIGHT;
                    this.playOnce = true;
                    break;
            }
        }


        this.animationArray = new BufferedImage[this.nrOfFrames]; ///< Array to store individual animation frames.
        this.animationSpeed = animationSpeed; ///< The speed of the animation playback.
    }

    /**
     * @brief Loads the animation frames from the spritesheet.
     *
     * This method iterates through the spritesheet (imageSheet), extracting each frame
     * based on the configured image width (imgWidth) and height (imgHeight),
     * and stores them in the animationArray.
     */
    @Override
    public void loadAnimation(){
        for(int i = 0; i<this.nrOfFrames; ++i){
            animationArray[i] = this.imageSheet.getSubimage(i*this.imgWidth,0,this.imgWidth,this.imgHeight);
        }
    }

    /**
     * @brief Updates the current state of the animation.
     *
     * This method progresses the animation to the next frame based on the
     * animationSpeed. If the animation is set to play once (playOnce is true)
     * and has finished (isFinished is true), it will not update further.
     */
    @Override
    public void updateAnimation() {
        if(this.playOnce && this.isFinished){
            return; // Stop updating if the animation plays once and is finished.
        }
        tick++;
        if(tick>=animationSpeed) {
            tick=0;
            animationState++; // Move to the next frame.
            if(animationState>=nrOfFrames) {
                this.isFinished = true; // Mark as finished if it's the last frame.
                animationState=0; // Reset to the first frame (loops if playOnce is false).
            }
        }
    }

    /**
     * @brief Resets the animation to its starting state to play once.
     *
     * This method is used to trigger an animation that should play from the beginning,
     * typically for actions like attacks that play once. It resets the finished flag,
     * animation state (current frame), and tick counter.
     */
    @Override
    public void triggerOnce() {
        this.isFinished = false;
        this.animationState = 0;
        this.tick = 0;
    }

    /**
     * @brief Renders the current frame of the enemy animation at the specified position.
     * @param g The Graphics context to draw on.
     * @param x The x-coordinate where the animation should be drawn.
     * @param y The y-coordinate where the animation should be drawn.
     * @param flipped A boolean indicating whether the animation should be flipped horizontally.
     * @param scale The scaling factor for the animation. This can be overridden by specific enemy/purpose conditions.
     */
    @Override
    public void paintAnimation(Graphics g, int x, int y, boolean flipped,double scale) {

        if(this.playOnce && this.isFinished){
            return; // Do not draw if the animation plays once and is finished.
        }

        if (animationArray == null || animationArray[animationState] == null) {
            return; // Do nothing if animation frames are not loaded or current frame is null.
        }

        BufferedImage currentFrame = animationArray[animationState];
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the graphics context to avoid altering the original.

        try {
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);

            // Apply enemy-specific or purpose-specific scaling.
            if(this.entityName.equals(Constants.TIGER_NAME) || this.entityName.equals(Constants.MINOTAUR_NAME)){ // Use .equals
                scale = 0.5; // Specific scale for Tiger and Minotaur.
            }
            if(this.purpose == Constants.ENEMY_STATES.IN_FIGHT_IDLE || this.purpose == Constants.ENEMY_STATES.IN_FIGHT_ATTACKING){
                scale = 5; // Default scale for in-fight animations.
                if(this.entityName.equals(Constants.GHOST_NAME)){ // Use .equals
                    scale = 15; // Larger scale for Ghost in fight.
                }
                if(this.entityName.equals(Constants.BASIC_SKELETON_NAME) || this.entityName.equals(Constants.STRONG_SKELETON_NAME)){ // Use .equals
                    scale = 10; // Larger scale for Skeletons in fight.
                }
            }

            transform.scale(scale, scale); // Apply the determined scaling.

            if (flipped) {
                // Apply horizontal flip by translating to the right edge of the image,
                // then scaling by -1 in the x-direction, effectively flipping it around its y-axis.
                transform.translate(imgWidth, 0);
                transform.scale(-1, 1);
            }

            g2d.drawImage(currentFrame, transform, null); // Draw the transformed current frame.

        } finally {
            g2d.dispose(); // Dispose of the graphics copy to restore the original graphics state.
        }
    }
}