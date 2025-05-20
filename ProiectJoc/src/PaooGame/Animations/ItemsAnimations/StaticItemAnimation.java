package PaooGame.Animations.ItemsAnimations;

import PaooGame.Animations.Animation;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @class StaticItemAnimation
 * @brief Manages and renders animations for static items, potentially with a full-screen display option.
 *
 * This class extends the base Animation class. While named "StaticItemAnimation",
 * its animation update logic suggests it can still animate (looping by default).
 * It primarily loads animation frames from a specified spritesheet and provides
 * a method to paint the animation scaled to fit the entire screen.
 */
public class StaticItemAnimation extends Animation{
    private String itemSheetPath; ///< The file path to the spritesheet for this item's animation.

    /**
     * @brief Constructs a StaticItemAnimation object.
     * @param reflink A reference to the game's shared resources and utilities.
     * @param itemSheetPath The path to the image file containing the animation frames for the item.
     * @param nrOfFrames The total number of frames in the animation sequence.
     * @param animationSpeed The speed at which the animation plays (ticks per frame).
     * @param imgWidth The width of a single animation frame in the spritesheet.
     * @param imgHeight The height of a single animation frame in the spritesheet.
     */
    public StaticItemAnimation(RefLinks reflink, String itemSheetPath, int nrOfFrames, int animationSpeed, int imgWidth,int imgHeight){
        super();
        this.playOnce = false; ///< Indicates that the animation should loop continuously by default.
        this.reflink = reflink;
        this.itemSheetPath = itemSheetPath;
        this.nrOfFrames = nrOfFrames;
        this.animationSpeed = animationSpeed;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        // Load the spritesheet using the TileCache.
        this.imageSheet = reflink.getTileCache().getSpecial(this.itemSheetPath,this.imgWidth,this.imgHeight,this.nrOfFrames);
        this.animationArray = new BufferedImage[this.nrOfFrames]; ///< Array to store individual animation frames.
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
        for(int i = 0; i<this.nrOfFrames; ++i){
            animationArray[i] = this.imageSheet.getSubimage(i*this.imgWidth,0,this.imgWidth,this.imgHeight);
        }
    }

    /**
     * @brief Updates the current state of the animation.
     *
     * This method progresses the animation to the next frame based on the
     * animationSpeed. By default (`playOnce` is false), the animation will loop.
     * If `playOnce` were true and the animation finished, it would stop updating.
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
     * @brief Resets the animation to its starting state.
     *
     * Allows the animation to be explicitly reset to play from the beginning.
     * It resets the finished flag, animation state (current frame), and tick counter.
     */
    @Override
    public void triggerOnce() {
        this.isFinished = false;
        this.animationState = 0;
        this.tick = 0;
    }

    /**
     * @brief Renders the current frame of the item animation at the specified position and scale.
     * @param g The Graphics context to draw on.
     * @param x The x-coordinate where the animation should be drawn.
     * @param y The y-coordinate where the animation should be drawn.
     * @param flipped A boolean indicating whether the animation should be flipped horizontally.
     * @param scale The scaling factor for the animation.
     */
    @Override
    public void paintAnimation(Graphics g, int x, int y, boolean flipped,double scale) {

        if(this.playOnce && this.isFinished){
            return; // Do not draw if playOnce is true and animation is finished.
        }

        if (animationArray == null || animationArray[animationState] == null) {
            return; // Do nothing if animation frames are not loaded or current frame is null.
        }

        BufferedImage currentFrame = animationArray[animationState];
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the graphics context.

        try {
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);

            transform.scale(scale, scale); // Apply the specified scaling.

            if (flipped) {
                // Apply horizontal flip if requested.
                transform.translate(imgWidth, 0);
                transform.scale(-1, 1);
            }

            g2d.drawImage(currentFrame, transform, null); // Draw the transformed current frame.

        } finally {
            g2d.dispose(); // Dispose of the graphics copy.
        }
    }

    /**
     * @brief Renders the current frame of the animation scaled to fit the entire screen.
     * @param g The Graphics context to draw on.
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     */
    public void paintFullScreen(Graphics g, int screenWidth, int screenHeight) {
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the graphics context.
        try {
            if (animationArray == null || animationArray[animationState] == null) {
                return; // Do nothing if animation frames are not loaded or current frame is null.
            }

            AffineTransform transform = new AffineTransform(); // Identity transform, starts at (0,0).
            // Calculate scaling factors to fit the image to the screen dimensions.
            double scaleX = (double) screenWidth / animationArray[animationState].getWidth();
            double scaleY = (double) screenHeight / animationArray[animationState].getHeight();

            transform.scale(scaleX, scaleY); // Apply scaling.

            g2d.drawImage(animationArray[animationState], transform, null); // Draw the scaled image.
        } finally {
            g2d.dispose(); // Dispose of the graphics copy.
        }
    }
}