package PaooGame.HUD;
import PaooGame.Entities.Hero;

import java.awt.*;

/**
 * @class PauseButton
 * @brief Represents a clickable button used to pause the game.
 *
 * This class extends {@link HUD} and provides functionality for drawing a pause button,
 * detecting mouse hover, and handling click events. The button displays "Pause:" text
 * next to a visual pause symbol (two vertical bars). Its appearance changes when hovered over.
 */
public class PauseButton extends HUD {
    private Rectangle bounds;       ///< The rectangular bounds of the pause button, used for click and hover detection.
    private boolean isHovered;      ///< Flag indicating if the mouse cursor is currently over the button.

    /**
     * @brief Constructs a PauseButton object.
     *
     * Initializes the button's position and associates it with a hero (inherited from HUD).
     * It also creates its bounding box for interaction detection.
     * @param hero The {@link Hero} entity this HUD element is associated with (inherited from HUD).
     * @param x The x-coordinate for the button's top-left corner.
     * @param y The y-coordinate for the button's top-left corner.
     */
    public PauseButton(Hero hero, int x, int y) {
        super(hero);
        int size = 40; ///< The size (width and height) of the pause button.
        this.bounds = new Rectangle(x, y, size, size);
    }

    /**
     * @brief Draws the pause button on the screen.
     *
     * Renders the "Pause:" label, a rounded background, and the pause symbol (two vertical bars).
     * The background color changes if the button is hovered.
     * @param g2d The Graphics2D context to draw on.
     */
    @Override
    public void draw(Graphics2D g2d) {
        // Font and text
        String label = "Pause:";
        g2d.setFont(g2d.getFont().deriveFont(16f));
        FontMetrics fm = g2d.getFontMetrics();

        // Measuring the text for vertical allignment
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();

        int textX = bounds.x - textWidth - 10; // 10px padding between text and buton
        int textY = bounds.y + (bounds.height + textHeight) / 2 - 4;

        // Drawing text
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, textX, textY);

        // Hover Background
        Color baseColor = isHovered ? new Color(220, 220, 220, 180) : new Color(255, 255, 255, 120);
        g2d.setColor(baseColor);
        g2d.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 12, 12);

        // Pause symbol
        g2d.setColor(Color.BLACK);
        int lineWidth = 4;
        int lineHeight = 20;
        int lineY = bounds.y + (bounds.height - lineHeight) / 2;

        g2d.fillRect(bounds.x + bounds.width / 3 - lineWidth, lineY, lineWidth, lineHeight);
        g2d.fillRect(bounds.x + 2 * bounds.width / 3 - lineWidth, lineY, lineWidth, lineHeight);
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
     * @brief Checks if the button was clicked given the mouse position.
     * @param mouseX The x-coordinate of the mouse event.
     * @param mouseY The y-coordinate of the mouse event.
     * @return True if the mouse coordinates are within the button's bounds, false otherwise.
     */
    public boolean isClicked(int mouseX, int mouseY) {
        return bounds.contains(mouseX, mouseY);
    }
}