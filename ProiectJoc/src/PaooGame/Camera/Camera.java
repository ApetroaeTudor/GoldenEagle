package PaooGame.Camera;

import PaooGame.Config.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * @class Camera
 * @brief Manages the game's viewport, including its position (offset) and zoom level (scale).
 *
 * The Camera class is responsible for transforming the game world coordinates
 * into screen coordinates, allowing the player to see different parts of the level
 * and providing zoom effects.
 */
public class Camera {
    private double xOffset, yOffset; ///< The x and y coordinates of the top-left corner of the camera's view in the game world.
    private double scale = 2.3;      ///< The zoom level of the camera. Higher values mean more zoom (objects appear larger).

    /**
     * @brief Constructs a Camera object with an initial position.
     * @param xOffset The initial x-offset of the camera in the game world.
     * @param yOffset The initial y-offset of the camera in the game world.
     */
    public Camera(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * @brief Applies the camera's transformation to a Graphics2D context.
     *
     * This method sets up the transformation matrix for the graphics context,
     * effectively moving and scaling the "view" into the game world.
     * All subsequent drawing operations on the g2d object will be affected by this transformation.
     *
     * @param g2d The Graphics2D context to which the camera transformation will be applied.
     */
    public void apply(Graphics2D g2d) {
        AffineTransform transform = new AffineTransform();
        // 1. Translate the world so that the camera's (xOffset, yOffset) becomes the new origin (0,0) on screen.
        //    The translation is negative because we are moving the world, not the camera.
        //    A "Magic Number" is applied in order to fit the current implementation.
        transform.translate(-xOffset * Constants.MAGIC_NUMBER, -yOffset);
        // 2. Scale the world around the new origin (0,0).
        transform.scale(scale, scale);
        // 3. Apply this combined transformation to the graphics context.
        g2d.transform(transform);
    }

    /**
     * @brief Gets the current scale (zoom level) of the camera.
     * @return The current scale value.
     */
    public double getScale() {
        return scale;
    }

    /**
     * @brief Sets the scale (zoom level) of the camera.
     * @param scale The new scale value.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * @brief Gets the current x-offset of the camera.
     * @return The current x-offset value.
     */
    public double getxOffset() {
        return xOffset;
    }

    /**
     * @brief Gets the current y-offset of the camera.
     * @return The current y-offset value.
     */
    public double getyOffset() {
        return yOffset;
    }

    /**
     * @brief Sets the absolute position (x and y offsets) of the camera.
     * @param x The new x-offset for the camera.
     * @param y The new y-offset for the camera.
     */
    public void setPosition(double x, double y) {
        this.xOffset = x;
        this.yOffset = y;
    }

    /**
     * @brief Updates the camera's position by adding delta values to its current offsets.
     * This is used for smooth camera movement.
     * @param x The amount to add to the current x-offset.
     * @param y The amount to add to the current y-offset.
     */
    public void updatePosition(double x, double y){
        this.xOffset += x;
        this.yOffset += y;
    }
}