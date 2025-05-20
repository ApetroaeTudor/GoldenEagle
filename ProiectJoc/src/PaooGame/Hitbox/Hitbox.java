package PaooGame.Hitbox;

import java.awt.*;

public class Hitbox {

    private float x, y, width, height;
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



    public boolean intersects(Hitbox other) {
        if (other == null) {
            return false;
        }
        return this.x < other.x + other.width &&   // This left edge is left of other right edge
                this.x + this.width > other.x &&    // This right edge is right of other left edge
                this.y < other.y + other.height &&  // This top edge is above other bottom edge
                this.y + this.height > other.y;     // This bottom edge is below other top edge
    }

    public static boolean checkIntersection(Hitbox hitbox1, Hitbox hitbox2) {
        if (hitbox1 == null || hitbox2 == null) {
            return false;
        }
        return hitbox1.intersects(hitbox2); // Delegate to the instance method
    }



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



    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        if (width < 0) {
            System.err.println("Warning: Attempted to set negative hitbox width. Setting to 0 instead.");
            this.width = 0;
        } else {
            this.width = width;
        }
    }

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