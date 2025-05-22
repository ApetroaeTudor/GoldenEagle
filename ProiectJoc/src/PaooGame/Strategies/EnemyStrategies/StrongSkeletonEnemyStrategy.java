package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class StrongSkeletonEnemyStrategy
 * @brief Implements the specific strategy for the Strong Skeleton enemy type.
 *
 * This class extends {@link EnemyStrategy} and defines the unique properties
 * and behaviors for Strong Skeleton enemies, such as their speed, health, damage,
 * animations, and the level they primarily appear in. It follows a Singleton
 * pattern to ensure only one instance of this strategy configuration exists.
 */
public class StrongSkeletonEnemyStrategy extends EnemyStrategy {
    private static StrongSkeletonEnemyStrategy instance = null; ///< Singleton instance of the StrongSkeletonEnemyStrategy.

    /**
     * @brief Gets the singleton instance of the StrongSkeletonEnemyStrategy.
     *
     * If the instance does not exist, it creates a new one.
     * This method ensures that only one instance of the strategy is used throughout the game.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @return The singleton instance of {@link StrongSkeletonEnemyStrategy}.
     */
    public static StrongSkeletonEnemyStrategy getInstance(RefLinks reflink){
        if(StrongSkeletonEnemyStrategy.instance == null ){
            StrongSkeletonEnemyStrategy.instance = new StrongSkeletonEnemyStrategy(reflink);
        }
        return instance;
    }


    /**
     * @brief Gets the name of the enemy type this strategy represents.
     * This method overrides the {@link EnemyStrategy#getName()} method.
     * @return A {@link String} representing the name of the enemy (e.g., "StrongSkeleton").
     */
    @Override
    public String getName(){
        return Constants.STRONG_SKELETON_NAME;
    }

    /**
     * @brief Gets the source or primary level where this enemy type is found.
     * This method overrides the {@link EnemyStrategy#getSource()} method.
     * @return A {@link String} representing the source level (e.g., "Level2").
     */
    @Override
    public String getSource(){
        return Constants.LEVEL_2;
    }

    /**
     * @brief Constructs a StrongSkeletonEnemyStrategy object.
     *
     * Initializes the specific properties for the Strong Skeleton enemy, including
     * speed, hitbox dimensions, health, damage, and animations. It loads the
     * necessary animations (walking, attacking, idle) and sets up the behavior
     * tile IDs relevant to its movement and interaction within Level 2.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public StrongSkeletonEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.STRONG_SKELETON_SPEED;
        this.hitboxWidth = 32;
        this.hitboxHeight = 32;

        this.levelWidthInTiles = Constants.LEVEL2_WIDTH;
        this.levelHeightInTiles = Constants.LEVEL2_HEIGHT;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.damage = Constants.STRONG_SKELETON_DAMAGE;
        this.health = Constants.STRONG_SKELETON_HEALTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.STRONG_SKELETON_PASSIVE_TILE_NR,15,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.STRONG_SKELETON_ATTACKING_TILE_NR,7,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.STRONG_SKELETON_IN_FIGHT_IDLE_TILE_NR,20,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        // Ensure Level 2 and its behavior IDs are loaded before accessing them
        if (reflink.getGame().getLevel2() != null) {
            this.behaviorIDsToRespect = reflink.getGame().getLevel2().getBehaviorIDs();
        } else {
            // Handle case where Level 2 might not be initialized yet
            System.err.println("Warning: Level2 behavior IDs accessed before Level2 was fully initialized in StrongSkeletonEnemyStrategy.");
            this.behaviorIDsToRespect = new int[0]; // Default to empty array or handle appropriately
        }
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
        g2d.setFont(new Font("Arial",Font.BOLD,25)); // Font size is 25 for Strong Skeleton
        g2d.setColor(Color.RED);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw the enemy's name at a fixed position
        g2d.drawString(this.getName(),480,60);

        // Restore original font, color, and rendering hints
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
        g2d.setRenderingHints(oldHints);
    }

}