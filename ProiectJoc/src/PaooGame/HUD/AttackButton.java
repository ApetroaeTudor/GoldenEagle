package PaooGame.HUD;

import PaooGame.Entities.Entity;

import java.awt.*;

/**
 * @class AttackButton
 * @brief Represents a clickable "Attack" button
 *
 * This class extends {@link HUD} and provides functionality for drawing an attack button,
 * detecting mouse hover, and handling click events. The button's appearance
 * changes when hovered over.
 */
public class AttackButton extends HUD {
    private boolean isHovered;  ///< Flag indicating if the mouse cursor is currently over the button.
    private int x;              ///< The x-coordinate of the top-left corner of the button.
    private int y;              ///< The y-coordinate of the top-left corner of the button.
    private int buttonWidth = 500; ///< The width of the button in pixels.
    private int buttonHeight = 70; ///< The height of the button in pixels.

    private Rectangle bounds;   ///< The rectangular bounds of the button, used for click and hover detection.

    /**
     * @brief Constructs an AttackButton object.
     *
     * Initializes the button's position, associated entity, and creates its bounding box
     * for interaction detection.
     * @param entity The entity this HUD element is associated with (inherited from HUD).
     * @param x The x-coordinate for the button's top-left corner.
     * @param y The y-coordinate for the button's top-left corner.
     */
    public AttackButton(Entity entity, int x, int y) {
        super(entity);
        this.isHovered = false;
        this.x = x;
        this.y = y;
        bounds = new Rectangle(this.x,this.y,this.buttonWidth,this.buttonHeight);
    }

    /**
     * @brief Draws the attack button on the screen.
     *
     * Renders the button with a background, "Attack" text, and a border.
     * The appearance (colors, border thickness) changes if the button is hovered.
     * @param g2d The Graphics2D context to draw on.
     */
    @Override
    public void draw(Graphics2D g2d) {
        // --- Button Background ---
        Color baseColor;
        if (isHovered) {
            baseColor = new Color(190, 50, 50, 220); // Darker red when hovered
        } else {
            baseColor = new Color(220, 80, 80, 180); // Lighter red by default
        }

        g2d.setColor(baseColor);
        // Use fillRoundRect for rounded corners
        g2d.fillRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 10, 10); // 10px corner radius

        // --- Button Text ---
        String label = "Attack";
        g2d.setFont(new Font("Arial", Font.BOLD, 35));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label); // used for centering the text


        // Calculate text position to center it within the button
        int textX = this.x + (this.buttonWidth - textWidth) / 2;
        int textY = this.y + (this.buttonHeight - fm.getHeight()) / 2 + fm.getAscent();

        // Draw the text shadow (optional, for better visibility)
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(label, textX + 1, textY + 1);

        // Draw the main text
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, textX, textY);

        // --- Button Border ---
        g2d.setColor(isHovered ? Color.WHITE : Color.BLACK); // Border changes on hover
        g2d.setStroke(new BasicStroke(isHovered ? 2 : 1)); // Thicker border on hover
        g2d.drawRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 10, 10);
        g2d.setStroke(new BasicStroke(1)); // Reset stroke to default
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