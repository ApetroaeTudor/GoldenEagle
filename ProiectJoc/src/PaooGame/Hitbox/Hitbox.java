package PaooGame.Hitbox;

import java.awt.*;

/**
 * Represents a rectangular hitbox using floating-point coordinates for position and dimensions.
 * Useful for precise collision detection and physics calculations.
 */
public class Hitbox {

    private float x, y, width, height;
    // x, y are the coordinates of the top-left corner of the hitbox.
    // Increasing y moves the hitbox downwards on the screen.

    /**
     * Creates a new Hitbox.
     *
     * @param x      The initial x-coordinate (top-left corner).
     * @param y      The initial y-coordinate (top-left corner).
     * @param width  The initial width of the hitbox. Must be non-negative.
     * @param height The initial height of the hitbox. Must be non-negative.
     */
    public Hitbox(float x, float y, float width, float height) {
        // Basic validation to prevent negative dimensions
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Hitbox dimensions cannot be negative.");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves the hitbox by the specified amounts and optionally resizes it.
     *
     * @param xToAdd    The amount to add to the current x-coordinate.
     * @param yToAdd    The amount to add to the current y-coordinate.
     * @param newWidth  The new width. If less than or equal to 0, the width is not changed.
     * @param newHeight The new height. If less than or equal to 0, the height is not changed.
     */
    public void updateHitbox(float xToAdd, float yToAdd, float newWidth, float newHeight) {
        // Resize first if valid dimensions are provided
        if (newWidth > 0.0f) {
            setWidth(newWidth);
        }
        if (newHeight > 0.0f) {
            setHeight(newHeight);
        }
        // Then move
        this.x += xToAdd;
        this.y += yToAdd;
    }

    /**
     * Moves the hitbox by the specified delta values.
     * @param dx Amount to move horizontally.
     * @param dy Amount to move vertically.
     */
    public void moveBy(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Sets the absolute position of the hitbox's top-left corner.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws an outline of the hitbox for debugging purposes.
     * Note: Graphics coordinates are integers, so float values are cast.
     *
     * @param g The Graphics context to draw on.
     */
    public void printHitbox(Graphics g) {
        Color originalColor = g.getColor();
        g.setColor(Color.red);
        // Cast float coordinates/dimensions to int for drawing on screen
        g.drawRect((int) this.x, (int) this.y, (int) this.width, (int) this.height);
        g.setColor(originalColor);
    }

    /**
     * Checks if this hitbox intersects with another hitbox.
     * Uses the Axis-Aligned Bounding Box (AABB) intersection test.
     *
     * @param other The other Hitbox to check against.
     * @return true if the hitboxes intersect, false otherwise.
     */
    public boolean intersects(Hitbox other) {
        if (other == null) {
            return false;
        }
        // Check for overlap on the X axis and Y axis
        // No overlap if one rectangle is entirely to the left/right/above/below the other
        return this.x < other.x + other.width &&   // This left edge is left of other right edge
                this.x + this.width > other.x &&    // This right edge is right of other left edge
                this.y < other.y + other.height &&  // This top edge is above other bottom edge
                this.y + this.height > other.y;     // This bottom edge is below other top edge
    }

    /**
     * Static method to check for intersection between two Hitboxes.
     * Equivalent to calling hitbox1.intersects(hitbox2).
     *
     * @param hitbox1 The first hitbox.
     * @param hitbox2 The second hitbox.
     * @return true if the hitboxes intersect, false otherwise.
     */
    public static boolean checkIntersection(Hitbox hitbox1, Hitbox hitbox2) {
        if (hitbox1 == null || hitbox2 == null) {
            return false;
        }
        return hitbox1.intersects(hitbox2); // Delegate to the instance method
    }


    // --- Getters ---

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    // --- Convenience Getters for Edges/Center ---

    public float getRight() {
        return this.x + this.width;
    }

    public float getBottom() {
        return this.y + this.height;
    }

    public float getCenterX() {
        return this.x + this.width / 2.0f;
    }

    public float getCenterY() {
        return this.y + this.height / 2.0f;
    }


    // --- Setters ---

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the width of the hitbox. Must be non-negative.
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
     * Sets the height of the hitbox. Must be non-negative.
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

    @Override
    public String toString() {
        return "Hitbox[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
    }
}