package PaooGame.Animations.NpcAnimations;

import PaooGame.Animations.Animation;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @class StaticAnimation
 * @brief Represents a static, non-animated image.
 *
 * This class extends the base Animation class but is designed to display a single,
 * unchanging image. It does not perform any animation updates or loading of multiple frames.
 * It's suitable for entities or elements that have a fixed visual representation.
 */
public class StaticAnimation extends Animation {
    private BufferedImage frame; ///< The single image frame to be displayed.

    /**
     * @brief Constructs a StaticAnimation object with a single image frame.
     * @param frame The BufferedImage that represents the static image.
     */
    public StaticAnimation(BufferedImage frame) {
        this.frame = frame; // Stores the single frame.
        this.imgWidth = frame.getWidth();  ///< The width of the static image.
        this.imgHeight = frame.getHeight(); ///< The height of the static image.
    }

    /**
     * @brief No operation for static animation.
     * Since it's a single static frame, no loading of an animation sequence is needed.
     */
    @Override
    public void loadAnimation() {} // Not necessary for a static image.

    /**
     * @brief No operation for static animation.
     * A static image does not change over time, so no update logic is required.
     */
    @Override
    public void updateAnimation() {} // No updates as it's static.

    /**
     * @brief No operation for static animation.
     * The concept of "triggering once" does not apply to a static image.
     */
    @Override
    public void triggerOnce() {} // Not applicable for a static image.

    /**
     * @brief Renders the static image at the specified position and scale.
     * @param g The Graphics context to draw on.
     * @param x The x-coordinate where the image should be drawn.
     * @param y The y-coordinate where the image should be drawn.
     * @param flipped A boolean indicating whether the image should be flipped horizontally.
     * @param scale The scaling factor for the image.
     */
    @Override
    public void paintAnimation(Graphics g, int x, int y, boolean flipped, double scale) {
        // Calculate the dimensions for drawing based on the scale.
        int drawWidth = (int)(imgWidth * scale);
        int drawHeight = (int)(imgHeight * scale);

        if (flipped) {
            // Draw the image flipped horizontally.
            // To flip, draw from x + drawWidth with a negative width.
            g.drawImage(frame, x + drawWidth, y, -drawWidth, drawHeight, null);
        } else {
            // Draw the image normally.
            g.drawImage(frame, x, y, drawWidth, drawHeight, null);
        }
    }
}