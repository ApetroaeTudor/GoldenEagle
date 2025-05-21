package PaooGame.HUD;

import PaooGame.Entities.Entity;
import PaooGame.Config.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @class HealthBar
 * @brief Represents a visual health bar for an entity in the game's Heads-Up Display (HUD).
 *
 * This class extends {@link HUD} and is responsible for drawing a health bar that
 * reflects the current health of an associated {@link Entity}. It features a smooth
 * animation for health changes and customizable appearance (position, size, colors).
 */
public class HealthBar extends HUD {
    private double displayedHealth; ///< The health value currently rendered, used for smooth animation.

    private int width = 200;        ///< The width of the health bar in pixels.
    private int height = 20;        ///< The height of the health bar in pixels.
    private int x = 20;             ///< The x-coordinate of the top-left corner of the health bar.
    private int y = 20;             ///< The y-coordinate of the top-left corner of the health bar.

    private Color color1 = Constants.GREEN_HEALTH_BAR_COLOR_1; ///< The primary color for the health bar gradient (typically the full health color).
    private Color color2 = Constants.GREEN_HEALTH_BAR_COLOR_2; ///< The secondary color for the health bar gradient.

    /**
     * @brief Constructs a HealthBar object associated with a specific entity.
     *
     * Initializes the health bar with the entity's current health and default appearance settings.
     * @param entity The entity whose health this bar will represent.
     */
    public HealthBar(Entity entity) {
        super(entity);
        this.displayedHealth = entity.getHealth();
    }

    /**
     * @brief Draws the health bar on the screen.
     *
     * Renders the health bar with a background, a foreground bar representing current health (with gradient),
     * and an outline. The displayed health value is animated to smoothly transition towards the entity's actual health.
     * @param g2d The Graphics2D context to draw on.
     */
    @Override
    public void draw(Graphics2D g2d) {
        if (entity == null) return; // Do not draw if no entity is associated.


        // Smooth transition animation between actual health and displayed health
        double targetHealth = entity.getHealth();
        double speed = 1.5; // Transition speed
        if (displayedHealth > targetHealth) {
            displayedHealth -= speed;
            if (displayedHealth < targetHealth) {
                displayedHealth = targetHealth; // Clamp to target
            }
        } else if (displayedHealth < targetHealth) {
            displayedHealth += speed;
            if (displayedHealth > targetHealth) {
                displayedHealth = targetHealth; // Clamp to target
            }
        }

        // Background (represents lost health, typically red)
        g2d.setColor(Color.RED);
        g2d.fillRoundRect(x, y, width, height, 10, 10); // Rounded corners

        // Foreground bar (represents current health, typically green gradient)
        int currentWidth = (int) (width * (displayedHealth / 100.0)); // Assuming max health is 100
        if (currentWidth > 0) {
            // Apply gradient paint for the health fill
            g2d.setPaint(new java.awt.GradientPaint(x, y, this.color1, x + currentWidth, y, this.color2));
            g2d.fillRoundRect(x, y, currentWidth, height, 10, 10);
        }

        // Outline
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, width, height, 10, 10);
    }

    /**
     * @brief Sets the x-coordinate of the health bar's top-left corner.
     * @param x The new x-coordinate.
     */
    public void setX(int x){this.x = x;}
    /**
     * @brief Sets the y-coordinate of the health bar's top-left corner.
     * @param y The new y-coordinate.
     */
    public void setY(int y){this.y = y;}
    /**
     * @brief Sets the width of the health bar.
     * @param width The new width in pixels.
     */
    public void setWidth(int width){this.width = width;}
    /**
     * @brief Sets the height of the health bar.
     * @param height The new height in pixels.
     */
    public void setHeight(int height){this.height = height;}

    /**
     * @brief Gets the x-coordinate of the health bar's top-left corner.
     * @return The current x-coordinate.
     */
    public int getX(){return this.x;}
    /**
     * @brief Gets the y-coordinate of the health bar's top-left corner.
     * @return The current y-coordinate.
     */
    public int getY(){return this.y;}
    /**
     * @brief Gets the width of the health bar.
     * @return The current width in pixels.
     */
    public int getWidth(){return this.width;}
    /**
     * @brief Gets the height of the health bar.
     * @return The current height in pixels.
     */
    public int getHeight(){return this.height;}

    /**
     * @brief Gets the primary color used for the health bar's gradient.
     * @return The primary {@link Color} object.
     */
    public Color getColor1(){return this.color1;}
    /**
     * @brief Gets the secondary color used for the health bar's gradient.
     * @return The secondary {@link Color} object.
     */
    public Color getColor2(){return this.color2;}

    /**
     * @brief Sets the primary color for the health bar's gradient.
     * @param color1 The new primary {@link Color}.
     */
    public void setColor1(Color color1){this.color1 = color1;}
    /**
     * @brief Sets the secondary color for the health bar's gradient.
     * @param color2 The new secondary {@link Color}.
     */
    public void setColor2(Color color2){this.color2 = color2;}

    /**
     * @brief Resets the health bar's position and dimensions to default values.
     * Default values are: width=200, height=20, x=80, y=20.
     */
    public void resetPositionToDefault(){
        this.width = 200;
        this.height = 20;
        this.x = 80;
        this.y = 20;
    }


}