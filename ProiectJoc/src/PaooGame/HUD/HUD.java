package PaooGame.HUD;

import PaooGame.Entities.Entity;

import java.awt.Graphics2D;

/**
 * @class HUD
 * @brief An abstract base class for Heads-Up Display (HUD) elements.
 *
 * This class provides a common structure for various HUD components that might be
 * associated with a game {@link Entity}. Subclasses must implement the "draw" method
 * to define how they are rendered on the screen.
 */
public abstract class HUD {
    protected Entity entity; ///< The game entity this HUD element is associated with or relates to.

    /**
     * @brief Constructs a HUD object.
     *
     * Initializes the HUD element with a reference to an entity.
     * This entity can be used by subclasses to display information
     * relevant to it
     * @param entity The entity this HUD element is associated with.
     */
    public HUD(Entity entity) {
        this.entity = entity;
    }

    /**
     * @brief Abstract method for drawing the HUD element.
     *
     * Subclasses must implement this method to define how the HUD element
     * is rendered on the screen.
     * @param g2d The Graphics2D context to draw on.
     */
    public abstract void draw(Graphics2D g2d);
}