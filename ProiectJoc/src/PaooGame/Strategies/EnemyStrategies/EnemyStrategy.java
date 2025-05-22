package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.Animation;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class EnemyStrategy
 * @brief Defines an abstract base class for different enemy AI and property strategies.
 *
 * This class provides a common structure for defining the characteristics
 * and behaviors of various enemy types in the game. Concrete enemy strategies
 * (e.g., BasicSkeletonEnemyStrategy) will extend this class to implement
 * specific attributes like speed, health, damage, animations, and how they
 * interact with the game world.
 */
public abstract class EnemyStrategy {
    /**
     * @brief Gets the width of the enemy's hitbox.
     * @return The hitbox width in pixels.
     */
    public int getHitboxWidth() {
        return hitboxWidth;
    }

    /**
     * @brief Gets the movement speed of the enemy.
     * @return The enemy's speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @brief Gets the height of the enemy's hitbox.
     * @return The hitbox height in pixels.
     */
    public int getHitboxHeight() {
        return hitboxHeight;
    }

    /**
     * @brief Gets the width of the level (in tiles) this enemy operates in.
     * @return The level width in number of tiles.
     */
    public int getLevelWidthInTiles() {
        return levelWidthInTiles;
    }

    /**
     * @brief Gets the height of the level (in tiles) this enemy operates in.
     * @return The level height in number of tiles.
     */
    public int getLevelHeightInTiles() {
        return levelHeightInTiles;
    }

    /**
     * @brief Gets the primary color for the enemy's health bar.
     * @return The primary {@link Color} of the health bar.
     */
    public Color getHealthBarColor1() {
        return healthBarColor1;
    }

    /**
     * @brief Gets the secondary color (e.g., background or depleted part) for the enemy's health bar.
     * @return The secondary {@link Color} of the health bar.
     */
    public Color getHealthBarColor2() {
        return healthBarColor2;
    }

    /**
     * @brief Gets the walking animation for the enemy.
     * @return The {@link Animation} used when the enemy is walking.
     */
    public Animation getWalkingAnimation() {
        return walkingAnimation;
    }

    /**
     * @brief Gets the attacking animation for the enemy when in a fight.
     * @return The {@link Animation} used when the enemy is attacking during a fight.
     */
    public Animation getInFightAttackingAnimation() {
        return inFightAttackingAnimation;
    }

    /**
     * @brief Gets the idle animation for the enemy when in a fight.
     * @return The {@link Animation} used when the enemy is idle during a fight.
     */
    public Animation getInFightIdleAnimation() {
        return inFightIdleAnimation;
    }

    /**
     * @brief Gets the reference links object.
     * @return The {@link RefLinks} object providing access to game-wide utilities.
     */
    public RefLinks getReflink() {
        return reflink;
    }

    /**
     * @brief Gets the damage value dealt by the enemy.
     * @return The amount of damage the enemy inflicts.
     */
    public double getDamage() {
        return damage;
    }

    /**
     * @brief Gets the total health points of the enemy.
     * @return The maximum health of the enemy.
     */
    public double getHealth() {
        return health;
    }

    /**
     * @brief Gets an array of behavior tile IDs that this enemy should respect or interact with.
     * These IDs typically define collision or special interaction tiles in the level.
     * @return An array of integer IDs representing behavior tiles.
     */
    public int[] getBehaviorIDsToRespect() {
        return behaviorIDsToRespect;
    }

    protected float speed;                              ///< Movement speed of the enemy.
    protected int hitboxWidth;                          ///< Width of the enemy's hitbox in pixels.
    protected int hitboxHeight;                         ///< Height of the enemy's hitbox in pixels.

    protected int levelWidthInTiles;                    ///< Width of the current level in tiles, relevant for enemy navigation.
    protected int levelHeightInTiles;                   ///< Height of the current level in tiles, relevant for enemy navigation.

    protected Color healthBarColor1;                    ///< Primary color for the enemy's health bar.
    protected Color healthBarColor2;                    ///< Secondary color for the enemy's health bar.

    protected Animation walkingAnimation;               ///< Animation for when the enemy is walking.
    protected Animation inFightAttackingAnimation;      ///< Animation for when the enemy is attacking during a fight.
    protected Animation inFightIdleAnimation;           ///< Animation for when the enemy is idle during a fight.

    protected RefLinks reflink;                         ///< Reference to shared game objects and utilities.

    protected double damage;                            ///< Amount of damage the enemy inflicts.
    protected double health;                            ///< Total health points of the enemy.

    protected int[] behaviorIDsToRespect;               ///< Array of tile behavior IDs the enemy interacts with (e.g., collision).

    /**
     * @brief Constructs an EnemyStrategy object.
     *
     * Initializes the strategy with a reference to the game's shared {@link RefLinks} object.
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public EnemyStrategy(RefLinks reflink){
        this.reflink = reflink;
    }

    /**
     * @brief Gets the name of the enemy type this strategy represents.
     *
     * This method must be implemented by concrete subclasses to return the
     * specific name of the enemy (e.g., "Skeleton", "Goblin").
     * @return A {@link String} representing the name of the enemy.
     */
    public abstract String getName();

    /**
     * @brief Gets the source or primary level where this enemy type is typically found.
     *
     * This method must be implemented by concrete subclasses.
     * @return A {@link String} representing the source level (e.g., "Level1")
     */
    public abstract String getSource();

    /**
     * @brief Draws the name of the enemy on the screen.
     *
     * This method is typically used during fight sequences to display the enemy's name.
     * Concrete subclasses must implement how the name is drawn.
     * @param g2d The {@link Graphics2D} context used for drawing.
     */
    public abstract void drawName(Graphics2D g2d);

}