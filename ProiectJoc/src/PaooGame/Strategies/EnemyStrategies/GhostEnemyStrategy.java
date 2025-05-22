package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class GhostEnemyStrategy
 * @brief Implements the specific strategy for the Ghost enemy type.
 *
 * This class extends {@link EnemyStrategy} and defines the unique properties
 * and behaviors for Ghost enemies, such as their speed, health, damage,
 * animations, and the level they primarily appear in. It follows a Singleton
 * pattern to ensure only one instance of this strategy configuration exists.
 */
public class GhostEnemyStrategy extends EnemyStrategy{

    private static GhostEnemyStrategy instance = null; ///< Singleton instance of the GhostEnemyStrategy.

    /**
     * @brief Gets the singleton instance of the GhostEnemyStrategy.
     *
     * If the instance does not exist, it creates a new one.
     * This method ensures that only one instance of the strategy is used throughout the game.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @return The singleton instance of {@link GhostEnemyStrategy}.
     */
    public static GhostEnemyStrategy getInstance(RefLinks reflink){
        if(GhostEnemyStrategy.instance == null){
            GhostEnemyStrategy.instance = new GhostEnemyStrategy(reflink);
        }
        return instance;
    }

    /**
     * @brief Constructs a GhostEnemyStrategy object.
     *
     * Initializes the specific properties for the Ghost enemy, including
     * speed, hitbox dimensions, health, damage, and animations. It loads the
     * necessary animations (walking, attacking, idle) and sets up the behavior
     * tile IDs relevant to its movement and interaction within Level 3.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public GhostEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.GHOST_SPEED;
        this.hitboxWidth = Constants.GHOST_PASSIVE_TILE_WIDTH;
        this.hitboxHeight = Constants.GHOST_PASSIVE_TILE_HEIGHT;

        this.levelHeightInTiles = Constants.LEVEL3_HEIGHT;
        this.levelWidthInTiles = Constants.LEVEL3_WIDTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.GHOST_PASSIVE_TILE_NR,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.GHOST_ATTACKING_TILE_NR,20,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.GHOST_IN_FIGHT_IDLE_TILE_NR,20,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.GHOST_DAMAGE;
        this.health = Constants.GHOST_HEALTH;

        this.healthBarColor1 = Constants.PURPLE_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.PURPLE_HEALTH_BAR_COLOR_2;

        // Ensure Level 3 and its behavior IDs are loaded before accessing them
        if (reflink.getGame().getLevel3() != null) {
            this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
        } else {
            // Handle case where Level 3 might not be initialized yet
            System.err.println("Warning: Level3 behavior IDs accessed before Level3 was fully initialized in GhostEnemyStrategy.");
            this.behaviorIDsToRespect = new int[0]; // Default to empty array or handle appropriately
        }
    }


    /**
     * @brief Gets the name of the enemy type this strategy represents.
     * This method overrides the {@link EnemyStrategy#getName()} method.
     * @return A {@link String} representing the name of the enemy (e.g., "Ghost").
     */
    @Override
    public String getName(){
        return Constants.GHOST_NAME;
    }

    /**
     * @brief Gets the source or primary level where this enemy type is found.
     * This method overrides the {@link EnemyStrategy#getSource()} method.
     * @return A {@link String} representing the source level (e.g., "Level3").
     */
    @Override
    public String getSource(){
        return Constants.LEVEL_3;
    }

    /**
     * @brief Draws the name of the enemy on the screen, typically during a fight.
     * This method overrides the {@link EnemyStrategy#drawName(Graphics2D)} method.
     * @param g2d The {@link Graphics2D} context used for drawing.
     */
    @Override
    public void drawName(Graphics2D g2d){
        // Store original color, font, and rendering hints to restore them later
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        RenderingHints oldHints = g2d.getRenderingHints();

        // Set font, color, and rendering hint for drawing the name
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setColor(Color.ORANGE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw the enemy's name at a fixed position (consider making this dynamic or configurable)
        g2d.drawString(this.getName(),515,60);

        // Restore original font, color, and rendering hints
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
        g2d.setRenderingHints(oldHints);
    }
}