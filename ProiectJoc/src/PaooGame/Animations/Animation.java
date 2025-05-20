package PaooGame.Animations;

import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @class Animation
 * @brief Abstract base class for managing and rendering sprite animations.
 *
 * This class provides the foundational structure and common properties for all
 * types of animations used in the game, such as player animations, enemy animations,
 * item animations, and visual effects. It defines abstract methods for loading,
 * updating, and painting animations, which must be implemented by concrete subclasses.
 */
public abstract class Animation {

    protected BufferedImage imageSheet; ///< The spritesheet containing all frames for the animation.
    protected BufferedImage[] animationArray; ///< An array storing individual frames extracted from the imageSheet.

    protected int animationState; ///< The current frame index of the animation.
    protected int animationSpeed; ///< The speed of the animation (ticks per frame change). Higher value means slower animation.
    protected int nrOfFrames; ///< The total number of frames in this animation sequence.

    protected boolean isFinished = true; ///< Flag indicating if a 'playOnce' animation has completed its sequence. Defaults to true.
    protected boolean playOnce = false; ///< Flag indicating if the animation should play only once and then stop. Defaults to false (looping).

    protected RefLinks reflink; ///< A reference to shared game resources and utilities.

    protected int tick; ///< A counter used with animationSpeed to control frame progression.

    protected int imgWidth; ///< The width of a single frame in the animation.
    protected int imgHeight; ///< The height of a single frame in the animation.

    /**
     * @brief Default constructor for the Animation class.
     * Initializes common animation properties to default values.
     */
    protected Animation(){
        animationState = 0;
        animationSpeed = 0;
        nrOfFrames = 0;
        tick = 0;
        imgHeight = 0;
        imgWidth = 0;
    }

    /**
     * @brief Abstract method to load animation frames.
     * Concrete subclasses must implement this to populate the animationArray
     * from the imageSheet.
     */
    public abstract void loadAnimation();

    /**
     * @brief Abstract method to update the animation's state.
     * Concrete subclasses must implement this to advance the animation frame
     * based on timing and animationSpeed.
     */
    public abstract void updateAnimation();

    /**
     * @brief Abstract method to trigger or reset a 'playOnce' animation.
     * Concrete subclasses must implement this to allow an animation that plays
     * once to be started or restarted from its beginning.
     */
    public abstract void triggerOnce();

    /**
     * @brief Abstract method to paint the current animation frame.
     * @param g The Graphics context to draw on.
     * @param x The x-coordinate where the animation should be drawn.
     * @param y The y-coordinate where the animation should be drawn.
     * @param flipped A boolean indicating whether the animation should be rendered flipped horizontally.
     * @param scale The scaling factor to apply to the animation when drawing.
     */
    public abstract void paintAnimation(Graphics g, int x, int y,boolean flipped,double scale);

    /**
     * @brief Gets the finished status of the animation.
     * @return True if a 'playOnce' animation has completed, false otherwise.
     *         For looping animations, this might be true momentarily at the end of a loop before resetting.
     */
    public boolean getIsFinished(){return this.isFinished;}

    /**
     * @brief Sets whether the animation should play once or loop.
     * @param playOnce True if the animation should play once and then stop; false if it should loop.
     */
    public void setPlayOnce(boolean playOnce) {this.playOnce = playOnce;}

    /**
     * @brief Gets whether the animation is set to play once.
     * @return True if the animation is set to play once, false if it's set to loop.
     */
    public boolean getPlayOnce(){return this.playOnce;}

    /**
     * @brief Sets the finished status of the animation.
     * This is typically used internally by the animation logic but can be set externally if needed.
     * @param isFinished True to mark the animation as finished, false otherwise.
     */
    public void setIsFinished(boolean isFinished){this.isFinished = isFinished;}
}