package PaooGame.Hitbox;

import java.awt.*;

/**
 * @class Hitbox
 * @brief Represents a rectangular area used for collision detection.
 *
 * This class defines a simple box with a
 * position (x, y) and dimensions (width, height). It provides methods
 * for checking intersections with other hitboxes, updating its position
 * and size, and accessing its properties.
 */
public class Hitbox {

    private float x;      ///< The x-coordinate of the top-left corner of the hitbox.
    private float y;      ///< The y-coordinate of the top-left corner of the hitbox.
    private float width;  ///< The width of the hitbox.
    private float height; ///< The height of the hitbox.

    /**
     * @brief Constructs a Hitbox object with specified position and dimensions.
     * @param x The initial x-coordinate of the hitbox.
     * @param y The initial y-coordinate of the hitbox.
     * @param width The initial width of the hitbox. Must be non-negative.
     * @param height The initial height of the hitbox. Must be non-negative.
     * @throws IllegalArgumentException if width or height are negative.
     */
    public Hitbox(float x, float y, float width, float height) {
        // Basic validation to prevent negative dimensions
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Hitbox dimensions cannot be negative.\n");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @brief Checks if this hitbox intersects with another hitbox.
     * @param other The other Hitbox object to check for intersection.
     * @return True if the hitboxes intersect, false otherwise. Returns false if 'other' is null.
     */
    public boolean intersects(Hitbox other) {
        if (other == null) {
            return false;
        }
        return this.x < other.x + other.width &&   // This left edge is left of other right edge
                this.x + this.width > other.x &&    // This right edge is right of other left edge
                this.y < other.y + other.height &&  // This top edge is above other bottom edge
                this.y + this.height > other.y;     // This bottom edge is below other top edge
    }


    /**
     * @brief Gets the x-coordinate of the top-left corner of the hitbox.
     * @return The current x-coordinate.
     */
    public float getX() {
        return this.x;
    }

    /**
     * @brief Gets the y-coordinate of the top-left corner of the hitbox.
     * @return The current y-coordinate.
     */
    public float getY() {
        return this.y;
    }

    /**
     * @brief Gets the width of the hitbox.
     * @return The current width.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * @brief Gets the height of the hitbox.
     * @return The current height.
     */
    public float getHeight() {
        return this.height;
    }

    // --- Convenience Getters for Edges/Center ---

    /**
     * @brief Gets the x-coordinate of the right edge of the hitbox.
     * @return The x-coordinate of the right edge (x + width).
     */
    public float getRight() {
        return this.x + this.width;
    }

    /**
     * @brief Gets the y-coordinate of the bottom edge of the hitbox.
     * @return The y-coordinate of the bottom edge (y + height).
     */
    public float getBottom() {
        return this.y + this.height;
    }

    /**
     * @brief Gets the x-coordinate of the center of the hitbox.
     * @return The x-coordinate of the center (x + width / 2).
     */
    public float getCenterX() {
        return this.x + this.width / 2.0f;
    }

    /**
     * @brief Gets the y-coordinate of the center of the hitbox.
     * @return The y-coordinate of the center (y + height / 2).
     */
    public float getCenterY() {
        return this.y + this.height / 2.0f;
    }


    /**
     * @brief Sets the x-coordinate of the top-left corner of the hitbox.
     * @param x The new x-coordinate.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @brief Sets the y-coordinate of the top-left corner of the hitbox.
     * @param y The new y-coordinate.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @brief Sets the width of the hitbox.
     * If a negative width is provided, a warning is printed to System.err,
     * and the width is set to 0.
     * @param width The new width.
     */
    public void setWidth(float width) {
        if (width < 0) {
            System.err.println("Warning: Attempted to set negative hitbox width. Setting to 0 instead.");
            this.width = 0;
        } else {
            this.width = width;
        }
    }

    /**
     * @brief Sets the height of the hitbox.
     * If a negative height is provided, a warning is printed to System.err,
     * and the height is set to 0.
     * @param height The new height.
     */
    public void setHeight(float height) {
        if (height < 0) {
            System.err.println("Warning: Attempted to set negative hitbox height. Setting to 0 instead.");
            this.height = 0;
        } else {
            this.height = height;
        }
    }

    /**
     * @brief Returns a string representation of the Hitbox object.
     * @return A string in the format "Hitbox[x=value, y=value, width=value, height=value]".
     */
    @Override
    public String toString() {
        return "Hitbox[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
    }
}