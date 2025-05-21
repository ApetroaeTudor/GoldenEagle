package PaooGame.HUD;

import PaooGame.Entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @class ImageButton
 * @brief Represents a clickable button that displays an image.
 *
 * This class extends {@link HUD} and provides functionality for drawing a button
 * with a specified image, detecting mouse hover, and handling click events.
 * The button's appearance (background color, border) changes when hovered over.
 */
public class ImageButton extends HUD{
    private boolean isHovered;      ///< Flag indicating if the mouse cursor is currently over the button.
    private int x;                  ///< The x-coordinate of the top-left corner of the button.
    private int y;                  ///< The y-coordinate of the top-left corner of the button.
    private int buttonWidth = 50;   ///< The width of the button in pixels.
    private int buttonHeight = 50;  ///< The height of the button in pixels.
    private BufferedImage imgToDraw;///< The image to be displayed on the button.

    private Rectangle bounds;       ///< The rectangular bounds of the button, used for click and hover detection.

    /**
     * @brief Constructs an ImageButton object.
     *
     * Initializes the button's position, associated entity, and the image to display.
     * It also creates its bounding box for interaction detection.
     * @param entity The entity this HUD element is associated with (inherited from HUD).
     * @param x The x-coordinate for the button's top-left corner.
     * @param y The y-coordinate for the button's top-left corner.
     * @param imgToDraw The {@link BufferedImage} to be displayed on the button.
     */
    public ImageButton(Entity entity, int x, int y, BufferedImage imgToDraw){
        super(entity);

        this.isHovered = false;
        this.x = x;
        this.y = y;
        this.imgToDraw = imgToDraw;

        bounds = new Rectangle(this.x,this.y,this.buttonWidth,this.buttonHeight);
    }

    /**
     * @brief Draws the image button on the screen.
     *
     * Renders the button with a rounded background, a border, and the specified image.
     * The background color and border thickness change if the button is hovered.
     * @param g2d The Graphics2D context to draw on.
     */
    @Override
    public void draw(Graphics2D g2d){
        Color baseColor;
        if (isHovered) {
            // Gray color with transparency when hovered
            baseColor = new Color(150, 150, 150, 220);  // Medium gray
        } else {
            // Yellow-ish color with transparency by default
            baseColor = new Color(220, 200, 100, 180);  // Light golden yellow
        }

        g2d.setColor(baseColor);
        // Fill a rounded rectangle for the button background
        g2d.fillRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 30, 30); // 30px corner radius for a circular look if width=height

        // Draw the border, changing color and thickness on hover
        g2d.setColor(isHovered ? Color.WHITE : Color.BLACK);
        g2d.setStroke(new BasicStroke(isHovered ? 2 : 1)); // Thicker border on hover
        g2d.drawRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 30, 30);
        g2d.setStroke(new BasicStroke(1)); // Reset stroke to default

        // Draw the image if it's available
        if(imgToDraw!=null){
            // Calculate position to center the image (assuming image is smaller than button or has padding)
            // Current implementation places image with a fixed offset
            int imgX = this.x +10; // Image X position with 10px padding from left
            int imgY = this.y +10; // Image Y position with 10px padding from top
            g2d.drawImage(this.imgToDraw,imgX,imgY,null);
        }

    }

    /**
     * @brief Updates the hover state of the button based on the mouse cursor's position.
     * @param mouseX The current x-coordinate of the mouse cursor.
     * @param mouseY The current y-coordinate of the mouse cursor.
     */
    public void updateHover(int mouseX, int mouseY) {
        isHovered = bounds.contains(mouseX, mouseY);
    }

    /**
     * @brief Checks if the button was clicked given the mouse position and press state.
     * @param mouseX The x-coordinate of the mouse event.
     * @param mouseY The y-coordinate of the mouse event.
     * @param mousePressed True if a mouse button is currently pressed, false otherwise.
     * @return True if the mouse coordinates are within the button's bounds and the mouse button is pressed, false otherwise.
     */
    public boolean isClicked(int mouseX, int mouseY, boolean mousePressed) {
        return mousePressed && bounds.contains(mouseX, mouseY);
    }

    /**
     * @brief Gets the rectangular bounds of the button.
     * @return A Rectangle object representing the button's area on the screen.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * @brief Checks if the mouse cursor is currently hovering over the button.
     * @return True if the button is hovered, false otherwise.
     */
    public boolean isHovered() {
        return isHovered;
    }

    /**
     * @brief Sets the hover state of the button.
     * @param isHovered The new hover state to set (true for hovered, false otherwise).
     */
    public void setIsHovered(boolean isHovered) {this.isHovered = isHovered;}

}