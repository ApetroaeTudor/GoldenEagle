package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class MinotaurEnemyStrategy
 * @brief Implements the specific strategy for the Minotaur enemy type.
 *
 * This class extends {@link EnemyStrategy} and defines the unique properties
 * and behaviors for Minotaur enemies, such as their speed, health, damage,
 * animations, and the level they primarily appear in. It follows a Singleton
 * pattern to ensure only one instance of this strategy configuration exists.
 */
public class MinotaurEnemyStrategy extends EnemyStrategy {

    private static MinotaurEnemyStrategy instance = null; ///< Singleton instance of the MinotaurEnemyStrategy.

    /**
     * @brief Gets the singleton instance of the MinotaurEnemyStrategy.
     *
     * If the instance does not exist, it creates a new one.
     * This method ensures that only one instance of the strategy is used throughout the game.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @return The singleton instance of {@link MinotaurEnemyStrategy}.
     */
    public static MinotaurEnemyStrategy getInstance(RefLinks reflink){
        if(MinotaurEnemyStrategy.instance == null){
            MinotaurEnemyStrategy.instance = new MinotaurEnemyStrategy(reflink);
        }
        return instance;
    }

    /**
     * @brief Constructs a MinotaurEnemyStrategy object.
     *
     * Initializes the specific properties for the Minotaur enemy, including
     * speed, hitbox dimensions, health, damage, and animations. It loads the
     * necessary animations (walking, attacking, idle) and sets up the behavior
     * tile IDs relevant to its movement and interaction within Level 3.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public MinotaurEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.MINOTAUR_SPEED;
        this.hitboxWidth = Constants.MINOTAUR_PASSIVE_TILE_WIDTH/2;
        this.hitboxHeight = Constants.MINOTAUR_PASSIVE_TILE_HEIGHT/2;

        this.levelHeightInTiles = Constants.LEVEL3_HEIGHT;
        this.levelWidthInTiles = Constants.LEVEL3_WIDTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.MINOTAUR_PASSIVE_TILE_NR,15,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.MINOTAUR_ATTACKING_TILE_NR,10,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.MINOTAUR_IN_FIGHT_IDLE_TILE_NR,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.MINOTAUR_DAMAGE;
        this.health = Constants.MINOTAUR_HEALTH;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        // Ensure Level 3 and its behavior IDs are loaded before accessing them
        if (reflink.getGame().getLevel3() != null) {
            this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
        } else {
            // Handle case where Level 3 might not be initialized yet
            System.err.println("Warning: Level3 behavior IDs accessed before Level3 was fully initialized in MinotaurEnemyStrategy.");
            this.behaviorIDsToRespect = new int[0]; // Default to empty array or handle appropriately
        }
    }


    /**
     * @brief Gets the name of the enemy type this strategy represents.
     * This method overrides the {@link EnemyStrategy#getName()} method.
     * @return A {@link String} representing the name of the enemy (e.g., "Minotaur").
     */
    @Override
    public String getName(){
        return Constants.MINOTAUR_NAME;
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
        g2d.setColor(Color.RED);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw the enemy's name at a fixed position (consider making this dynamic or configurable)
        g2d.drawString(this.getName(),470,60);

        // Restore original font, color, and rendering hints
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
        g2d.setRenderingHints(oldHints);
    }

}