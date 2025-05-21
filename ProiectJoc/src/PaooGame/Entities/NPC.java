package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.NpcAnimations.StaticAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @class NPC
 * @brief Represents a Non-Player Character (NPC) in the game, specifically a Goblin.
 *
 * This NPC has two states: active (bound) and passive (freed). The player can interact
 * with the NPC to change its state. It uses static animations for its visual representation.
 */
public class NPC extends Entity {
    private StaticAnimation passiveAnimation; ///< Static animation for the NPC's passive state.
    private StaticAnimation activeAnimation;  ///< Static animation for the NPC's active state.
    private boolean isActive;                 ///< Flag indicating if the NPC is currently in its active state.

    private final double scale = 0.7; ///< Scaling factor for drawing the NPC sprite.

    /**
     * @brief Constructs an NPC object.
     *
     * Initializes the NPC at a given position, loads its sprites for active and passive states,
     * creates static animations, and sets up its initial hitbox.
     * @param refLinks A reference to shared game resources.
     * @param x The initial x-coordinate of the NPC.
     * @param y The initial y-coordinate of the NPC.
     */
    public NPC(RefLinks refLinks, int x, int y) {
        super(refLinks, x, y);
        this.health = 100;
        this.damage = 0;
        this.isActive = true;

        BufferedImage spriteSheet = loadImage(Constants.GOBLIN_SPRITE_SHEET_PATH);

        BufferedImage activeSprite = spriteSheet.getSubimage(
                0, 0,
                Constants.GOBLIN_BOUND_TILE_WIDTH,
                Constants.GOBLIN_BOUND_TILE_HEIGHT
        );

        BufferedImage passiveSprite = spriteSheet.getSubimage(
                Constants.GOBLIN_BOUND_TILE_WIDTH, 0,
                Constants.GOBLIN_PASSIVE_TILE_WIDTH,
                Constants.GOBLIN_PASSIVE_TILE_HEIGHT
        );

        activeAnimation = new StaticAnimation(activeSprite);
        passiveAnimation = new StaticAnimation(passiveSprite);

        this.hitbox = new Hitbox(
                (int)(x - 10),
                (int)y,
                (int)((Constants.GOBLIN_BOUND_TILE_WIDTH + 10) * scale),
                (int)(Constants.GOBLIN_BOUND_TILE_HEIGHT * scale)
        );
    }

    /**
     * @brief Loads an image from the specified path.
     * @param path The file path to the image.
     * @return The loaded BufferedImage, or null if an error occurs.
     */
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Error on loading the image: " + path);
            return null;
        }
    }

    /**
     * @brief Updates the NPC's state.
     *
     * Checks for player interaction (pressing 'E' key) when the NPC is active
     * and the hero is intersecting with the NPC's hitbox. If conditions are met,
     * the NPC transitions to the passive state and its hitbox is updated.
     */
    @Override
    public void update() {
        if (isActive && reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_E)) {
            if (this.reflink.getHero().getHitbox().intersects(this.hitbox)) {
                isActive = false;
                // activates the hitbox for the free state
                this.hitbox = new Hitbox(
                        (int)(x - 10),
                        (int)y,
                        (int)((Constants.GOBLIN_PASSIVE_TILE_WIDTH + 10) * scale),
                        (int)(Constants.GOBLIN_PASSIVE_TILE_HEIGHT * scale)
                );
            }
        }
    }

    /**
     * @brief Handles movement and collision for the NPC.
     *
     * This NPC is static, so this method is empty.
     */
    @Override
    protected void moveAndCollide() {
        // Static NPC
    }

    /**
     * @brief Updates the visual position of the NPC.
     *
     * This NPC is static, so this method is empty as its visual position
     * doesn't change dynamically based on a hitbox.
     */
    @Override
    protected void updateVisualPosition() {
        // Static
    }

    /**
     * @brief Gets the current animation based on the NPC's state.
     * @return The StaticAnimation corresponding to the active or passive state.
     */
    @Override
    protected Animation getAnimationByState() {
        return isActive ? activeAnimation : passiveAnimation;
    }

    /**
     * @brief Updates the animation state of the NPC.
     *
     * This NPC uses static animations, so this method is empty.
     */
    @Override
    protected void updateAnimationState() {
        // Static
    }

    /**
     * @brief Gets the name of the NPC.
     * @return The name of the Goblin NPC as defined in Constants.
     */
    @Override
    public String getName() {
        return Constants.GOBLIN_NAME;
    }

    /**
     * @brief Restores the NPC to a specific state.
     *
     * Sets the NPC to its passive (inactive) state.
     */
    @Override
    public void restoreEntity() {
        this.isActive = false;
    }

    /**
     * @brief Defines the NPC's attack behavior.
     *
     * This NPC does not attack, so this method is empty.
     */
    @Override
    public void attack() {
    }

    /**
     * @brief Gets the source path for the NPC's sprite sheet.
     * @return The file path to the Goblin sprite sheet.
     */
    @Override
    public String getSource() {
        return Constants.GOBLIN_SPRITE_SHEET_PATH;
    }

    /**
     * @brief Draws the NPC on the screen.
     *
     * Renders the current animation (active or passive) at the NPC's position,
     * applying the defined scaling factor.
     * @param g The Graphics context to draw on.
     */

    @Override
    public void draw(Graphics g) {
        int drawX = (int) x;
        int drawY = (int) y;
        getAnimationByState().paintAnimation(g, drawX, drawY, false, scale);
    }

    /**
     * @brief Checks if the NPC is currently in its active state.
     * @return True if the NPC is active (bound), false otherwise (passive/freed).
     */
    public boolean isActive() {
        return isActive;
    }
}