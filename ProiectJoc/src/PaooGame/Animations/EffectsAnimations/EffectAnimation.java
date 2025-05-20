package PaooGame.Animations.EffectsAnimations;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @class EffectAnimation
 * @brief Manages and renders animations for visual effects in the game.
 *
 * This class extends the base Animation class to handle specific types of effects,
 * such as attack explosions. It loads animation frames from a spritesheet and
 * controls their playback.
 */
public class EffectAnimation extends Animation {

    private Constants.EFFECTS selectedEffect; ///< The type of effect this animation represents.

    /**
     * @brief Constructs an EffectAnimation object.
     * @param refLink A reference to the game's shared resources and utilities.
     * @param selectedEffect The specific effect to animate (for example ATTACK_EXPLOSION).
     * @param nrOfFrames The total number of frames in the animation sequence.
     * @param animationSpeed The speed at which the animation plays (ticks per frame).
     */
    public EffectAnimation(RefLinks refLink,Constants.EFFECTS selectedEffect,int nrOfFrames,int animationSpeed){
        super();
        this.playOnce = true; ///< Indicates that the animation should play once and then stop.
        this.reflink = refLink;
        this.selectedEffect = selectedEffect;
        this.nrOfFrames = nrOfFrames;
        this.animationSpeed = animationSpeed;
        this.imageSheet = refLink.getTileCache().getEffect(this.selectedEffect); ///< The spritesheet containing the animation frames.
        this.animationArray = new BufferedImage[this.nrOfFrames]; ///< Array to store individual animation frames.

        // Set image dimensions based on the selected effect type.
        switch (selectedEffect){
            case ATTACK_EXPLOSION:
                this.imgWidth = Constants.ATTACK_EXPLOSION_TILE_SIZE;
                this.imgHeight = Constants.ATTACK_EXPLOSION_TILE_SIZE;
                break;
            // Future effect types can be added here.
        }
    }

    /**
     * @brief Loads the animation frames from the spritesheet.
     *
     * This method iterates through the spritesheet, extracting each frame
     * based on the configured image width and height, and stores them in the
     * animationArray.
     */
    @Override
    public void loadAnimation() {
        for(int i = 0; i<this.nrOfFrames; ++i){
            animationArray[i] = this.imageSheet.getSubimage(i*this.imgWidth,0,this.imgWidth,this.imgHeight);
        }
    }

    /**
     * @brief Updates the current state of the animation.
     *
     * This method progresses the animation to the next frame based on the
     * animationSpeed. If the animation is set to play once and has finished,
     * it will not update further.
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
                animationState=0; // Reset to the first frame (or loop if playOnce is false).
            }
        }
    }

    /**
     * @brief Resets the animation to its starting state to play once.
     *
     * This method is used to trigger an animation that should play from the beginning.
     * It resets the finished flag, animation state, and tick counter.
     */
    @Override
    public void triggerOnce() {
        this.isFinished = false;
        this.animationState = 0;
        this.tick = 0;
    }

    /**
     * @brief Renders the current frame of the animation at the specified position.
     * @param g The Graphics context to draw on.
     * @param x The x-coordinate where the animation should be drawn.
     * @param y The y-coordinate where the animation should be drawn.
     * @param flipped A boolean indicating whether the animation should be flipped horizontally.
     * @param scale The scaling factor for the animation.
     *              This parameter can be overridden by effect-specific scaling.
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

            // Apply effect-specific scaling.
            // This overrides the 'scale' parameter if a specific scale is defined for the effect.
            switch (this.selectedEffect){
                case ATTACK_EXPLOSION:
                    scale = 3; // Example: Attack explosion has a fixed scale.
                    break;
            }

            transform.scale(scale, scale); // Apply general or overridden scaling.

            if (flipped) {
                // Apply horizontal flip by translating to the right edge of the image,
                // scaling by -1 in x, which effectively flips it around its y-axis.
                transform.translate(imgWidth, 0);
                transform.scale(-1, 1);
            }

            g2d.drawImage(currentFrame, transform, null); // Draw the transformed current frame.

        } finally {
            g2d.dispose(); // Dispose of the graphics copy to restore the original graphics state.
        }
    }
}