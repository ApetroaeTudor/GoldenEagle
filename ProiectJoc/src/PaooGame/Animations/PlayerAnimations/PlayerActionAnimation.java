package PaooGame.Animations.PlayerAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @class PlayerActionAnimation
 * @brief Manages and renders animations for various player actions.
 *
 * This class extends the base Animation class to handle animations specific to the player character,
 * such as walking, jumping, attacking, etc. It loads animation frames from spritesheets
 * based on the player's current state (purpose).
 */
public class PlayerActionAnimation extends Animation {

    private Constants.HERO_STATES purpose; ///< The current action or state of the player (e.g., WALKING, JUMPING).

    /**
     * @brief Constructs a PlayerActionAnimation object.
     * @param reflink A reference to the game's shared resources and utilities.
     * @param purpose The specific action or state the animation represents.
     * @param nrOfFrames The total number of frames in the animation sequence.
     * @param animationSpeed The speed at which the animation plays (ticks per frame).
     *                       A higher value means a slower animation.
     */
    public PlayerActionAnimation(RefLinks reflink, Constants.HERO_STATES purpose, int nrOfFrames, int animationSpeed){
        super();
        this.purpose=purpose;

        this.reflink=reflink;
        // Load the spritesheet based on the player's state.
        this.imageSheet=reflink.getTileCache().getHeroState(purpose);
        this.nrOfFrames=nrOfFrames;
        // Assuming player character frames have a consistent size defined in Constants.
        this.imgHeight =Constants.CHARACTER_TILE_SIZE;
        this.imgWidth =Constants.CHARACTER_TILE_SIZE;
        this.animationArray=new BufferedImage[this.nrOfFrames]; ///< Array to store individual animation frames.

        this.animationSpeed=animationSpeed; // Higher value means slower animation.
        this.playOnce = false; ///< By default, player animations loop (e.g., walking). Can be overridden.
    }

    /**
     * @brief Loads the animation frames from the spritesheet.
     *
     * This method iterates through the spritesheet (imageSheet), extracting each frame
     * based on the configured image width (imgWidth) and height (imgHeight),
     * and stores them in the animationArray.
     */
    @Override
    public void loadAnimation() {
        for(int i=0;i<this.nrOfFrames;++i){
            animationArray[i]=this.imageSheet.getSubimage(i*this.imgWidth,0,this.imgWidth,this.imgHeight);
        }
    }

    /**
     * @brief Updates the current state of the animation.
     *
     * This method progresses the animation to the next frame based on the
     * animationSpeed. If `playOnce` is true and the animation has finished,
     * it will stop updating. Otherwise, it will loop.
     */
    @Override
    public void updateAnimation() {
        if(this.playOnce && this.isFinished){
            return; // Stop updating if playOnce is true and animation is finished.
        }

        tick++;
        if(tick>=animationSpeed) {
            tick=0;
            animationState++; // Move to the next frame.
            if(animationState>=nrOfFrames) {
                this.isFinished = true; // Mark as finished (relevant if playOnce is true).
                animationState=0; // Reset to the first frame to loop.
            }
        }
    }

    /**
     * @brief Resets the animation to its starting state to play once.
     *
     * This method is used to trigger an animation that should play from the beginning,
     * typically for actions that play once (e.g., a specific attack). It resets the
     * finished flag, animation state (current frame), and tick counter.
     */
    @Override
    public void triggerOnce() {
        this.isFinished = false;
        this.animationState = 0;
        this.tick = 0;
    }

    /**
     * @brief Renders the current frame of the player animation at the specified position.
     * @param g The Graphics context to draw on.
     * @param x The x-coordinate where the animation should be drawn.
     * @param y The y-coordinate where the animation should be drawn.
     * @param flipped A boolean indicating whether the animation should be flipped horizontally.
     * @param scale The scaling factor for the animation (Note: current implementation does not use this 'scale' parameter for player).
     */
    @Override
    public void paintAnimation(Graphics g, int x, int y,boolean flipped,double scale) {
        if(this.playOnce && this.isFinished){
            return; // Do not draw if playOnce is true and animation is finished.
        }
        if (animationArray == null || animationArray[animationState] == null) { // Check if animation is ready
            return;
        }

        if (flipped){
            BufferedImage currentFrame=animationArray[animationState];
            Graphics2D g2d = (Graphics2D) g.create(); // 1. Create a copy of the graphics context.
            try {
                // 2. Define the transformation:
                //    - Translate to the right edge of the image (at the drawing x-coordinate).
                //    - Scale by -1 in the x-direction to flip horizontally.
                //    - The y-coordinate remains the same.
                AffineTransform transform = AffineTransform.getTranslateInstance(x + imgWidth, y);
                transform.scale(-1, 1); // Flip horizontally around the translated origin.

                // 3. Draw the image using the Graphics2D object and the defined transform.
                g2d.drawImage(currentFrame, transform, null);

            } finally {
                g2d.dispose(); // 4. Dispose of the graphics copy to restore the original graphics state.
            }
        }
        else{
            // Draw the image normally, without flipping.
            // The current implementation draws at original imgWidth and imgHeight, 'scale' parameter is not used here.
            g.drawImage(animationArray[animationState], x, y, this.imgWidth, this.imgHeight, null);
        }
    }
}