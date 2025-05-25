package PaooGame.HUD;

import PaooGame.Config.Constants;
import PaooGame.Entities.Hero;
import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;

/**
 * @class ContextHUD
 * @brief Manages and displays contextual messages to the player based on their proximity to predefined trigger zones.
 *
 * This HUD element is associated with a {@link Hero} and contains a list of {@link MessageTriggerZone} objects.
 * When the hero enters a trigger zone, a corresponding message is displayed on the screen for a set duration.
 * The message is typically shown at a fixed position near the top of the game window.
 */
public class ContextHUD extends HUD {
    private ArrayList<MessageTriggerZone> triggers = new ArrayList<>(); ///< List of trigger zones that can display contextual messages.
    private String currentMessage = "";                                 ///< The message currently being displayed. Empty if no message is active.
    private int messageDuration = 100;                                  ///< The duration (in game ticks or frames) for which a message stays on screen.
    private int messageTimer = 0;                                       ///< Timer to track how long the current message has been displayed.
    private Point currentMessagePosition = new Point();                 ///< Stores the intended position for the message, derived from the trigger zone.

    /**
     * @brief Constructs a ContextHUD object associated with a specific hero.
     * @param hero The hero entity whose interactions will trigger contextual messages.
     */
    public ContextHUD(Hero hero) {
        super(hero);
    }


    /**
     * @brief Adds a new message trigger zone to this ContextHUD.
     * @param trigger The {@link MessageTriggerZone} to be added.
     */
    public void addTrigger(MessageTriggerZone trigger) {
        triggers.add(trigger);
    }

    /**
     * @brief Updates the state of the ContextHUD.
     *
     * This method performs the following actions:
     * 1. Updates the bounding boxes of all associated trigger zones, especially if they are linked to moving entities.
     * 2. Checks if the hero's hitbox intersects with any of the trigger zones.
     * 3. If an intersection occurs, it sets the "currentMessage" to the message from the triggered zone,
     *    calculates a conceptual message position (though the drawing uses a fixed screen position),
     *    and resets the "messageTimer".
     * 4. If no trigger is active or the timer for the current message has expired (implicitly, as timer reset happens on new trigger),
     *    the "currentMessage" might be cleared (or remain from the last trigger until a new one is found).
     *    The current implementation prioritizes the first active trigger found.
     */
    public void update() {
        // Update the bounds for all triggers
        for (MessageTriggerZone trigger : triggers) {
            if (trigger.hasEntity()) {
                trigger.updateBounds();
            }
        }

        // Check collision with the player
        Rectangle heroBounds = new Rectangle(
                (int) entity.getHitbox().getX(),
                (int) entity.getHitbox().getY(),
                (int) entity.getHitbox().getWidth(),
                (int) entity.getHitbox().getHeight()
        );

        // Reset the message
        currentMessage = ""; // Clear previous message, will be set if a new trigger is active
        boolean triggerFound = false;
        for (MessageTriggerZone trigger : triggers) {
            if (trigger.checkTrigger(heroBounds)) {
                currentMessage = trigger.getMessage();
                Rectangle triggerBounds = trigger.getBounds();
                // The currentMessagePosition is calculated but not directly used by the draw method for positioning the box.
                currentMessagePosition.setLocation(
                        triggerBounds.x + triggerBounds.width / 2,
                        triggerBounds.y - 20
                );
                messageTimer = messageDuration; // Reset timer for the new/active message
                triggerFound = true;
                break;
            }
        }
        if (!triggerFound && messageTimer > 0) {
            messageTimer--;
            if (messageTimer <= 0) {
                currentMessage = ""; // Explicitly clear if timer runs out and no new trigger
            }
        } else if (!triggerFound) {
            currentMessage = ""; // Ensure message is clear if no trigger and timer was already 0
        }
    }

    /**
     * @brief Draws the current contextual message on the screen if one is active.
     *
     * The message is displayed in a rounded rectangle with a semi-transparent background,
     * centered horizontally near the top of the game window.
     * @param g2d The Graphics2D context to draw on.
     */
    @Override
    public void draw(Graphics2D g2d) {
        if (!currentMessage.isEmpty()) { // Only draw if there's an active message
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics metrics = g2d.getFontMetrics();

            int padding = 10;
            int textWidth = metrics.stringWidth(currentMessage);
            int textHeight = metrics.getHeight();

            // Fixed position
            int boxX = Constants.WINDOW_WIDTH / 2 - textWidth / 2 - padding;
            int boxY = 50; // Fixed Y position for the message box
            int boxWidth = textWidth + padding * 2;
            int boxHeight = textHeight + padding;

            // Semi-transparent background
            g2d.setColor(new Color(0, 0, 0, 170));
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            // Text
            g2d.setColor(Color.YELLOW);
            int textX = boxX + padding;
            // Adjust textY for proper vertical alignment within the box
            int textY = boxY + metrics.getAscent() + (padding / 2);
            if(textHeight < boxHeight){ // a small adjustment to center if padding makes box taller
                textY = boxY + metrics.getAscent() + (boxHeight - textHeight)/2 + (padding - metrics.getDescent())/2 -2;
            }

            g2d.drawString(currentMessage, textX, textY);
        }
    }


}