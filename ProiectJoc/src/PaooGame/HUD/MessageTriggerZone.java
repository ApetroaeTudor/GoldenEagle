package PaooGame.HUD;

import PaooGame.Entities.Entity;
import java.awt.Rectangle;

/**
 * @class MessageTriggerZone
 * @brief Represents a zone that can trigger a message when an entity or a specified area interacts with it.
 *
 * This class defines a rectangular area in the game world. When a target
 * intersects with this zone, it can trigger an associated message. The zone can be static or
 * dynamically linked to an entity's position.
 */
public class MessageTriggerZone {
    private Entity entity;          ///< The entity this trigger zone is associated with, if any. Its position can be used to update the zone's bounds.
    private Rectangle bounds;       ///< The rectangular bounds of the trigger zone.
    private String message;         ///< The message to be displayed or processed when the zone is triggered.
    private boolean isActive;       ///< Flag indicating if the trigger zone is currently active and can be triggered.
    private int offsetX;            ///< The x-offset from the associated entity's position, if an entity is present.
    private int offsetY;            ///< The y-offset from the associated entity's position, if an entity is present.
    private int width;              ///< The width of the trigger zone.
    private int height;             ///< The height of the trigger zone.

    /**
     * @brief Constructs a MessageTriggerZone associated with a specific entity.
     *
     * The zone's position will be relative to the entity's hitbox, adjusted by the provided offsets.
     * @param entity The {@link Entity} this zone is attached to.
     * @param offsetX The horizontal offset from the entity's hitbox top-left corner.
     * @param offsetY The vertical offset from the entity's hitbox top-left corner.
     * @param width The width of the trigger zone.
     * @param height The height of the trigger zone.
     * @param message The message to be triggered.
     */
    public MessageTriggerZone(Entity entity, int offsetX, int offsetY, int width, int height, String message) {
        this.entity = entity;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.message = message;
        this.isActive = true;
        updateBounds();
    }

    /**
     * @brief Constructs a MessageTriggerZone at a fixed position.
     *
     * This constructor creates a static trigger zone defined by absolute coordinates and dimensions,
     * not associated with any entity.
     * @param x The x-coordinate of the top-left corner of the trigger zone.
     * @param y The y-coordinate of the top-left corner of the trigger zone.
     * @param width The width of the trigger zone.
     * @param height The height of the trigger zone.
     * @param message The message to be triggered.
     */
    public MessageTriggerZone(int x, int y, int width, int height, String message) {
        this.bounds = new Rectangle(x, y, width, height);
        this.message = message;
        this.isActive = true;
    }


    /**
     * @brief Updates the bounds of the trigger zone if it is associated with an entity.
     *
     * If an entity is linked to this zone, its bounds are recalculated based on the
     * entity's current hitbox position and the predefined offsets.
     * If no entity is associated, this method does nothing.
     */
    public void updateBounds() {
        if (entity != null) { // Only if an entity exists
            int x = (int) entity.getHitbox().getX() + offsetX;
            int y = (int) entity.getHitbox().getY() + offsetY;
            this.bounds = new Rectangle(x, y, width, height);
        }
    }

    /**
     * @brief Checks if a given target rectangle intersects with this trigger zone.
     *
     * The zone must be active for a trigger to occur.
     * @param target The {@link Rectangle} to check for intersection with this zone's bounds.
     * @return True if the zone is active and the target intersects with its bounds, false otherwise.
     */
    public boolean checkTrigger(Rectangle target) {
        return isActive && bounds.intersects(target);
    }

    /**
     * @brief Gets the message associated with this trigger zone.
     * @return The message string.
     */
    public String getMessage() { return message; }

    /**
     * @brief Checks if the trigger zone is currently active.
     * @return True if the zone is active, false otherwise.
     */
    public boolean isActive() { return isActive; }

    /**
     * @brief Gets the rectangular bounds of this trigger zone.
     * @return A {@link Rectangle} object representing the zone's area.
     */
    public Rectangle getBounds() { return bounds; }

    /**
     * @brief Checks if this trigger zone is associated with an entity.
     * @return True if an entity is linked to this zone, false otherwise.
     */
    public boolean hasEntity() {
        return entity != null;
    }

}