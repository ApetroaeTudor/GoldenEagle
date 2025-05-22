package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class BasicSkeletonEnemyStrategy
 * @brief Implements the specific strategy for a basic skeleton enemy.
 *
 * This class defines the attributes and behaviors unique to a basic skeleton enemy,
 * including its speed, hitbox dimensions, health, damage, and animations.
 * It follows a singleton pattern to ensure only one instance exists.
 */
public class BasicSkeletonEnemyStrategy extends EnemyStrategy {
    private static BasicSkeletonEnemyStrategy instance = null; ///< The singleton instance of BasicSkeletonEnemyStrategy.

    /**
     * @brief Gets the singleton instance of BasicSkeletonEnemyStrategy.
     *
     * If an instance does not already exist, it creates a new one.
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @return The singleton instance of BasicSkeletonEnemyStrategy.
     */
    public static BasicSkeletonEnemyStrategy getInstance(RefLinks reflink){
        if(BasicSkeletonEnemyStrategy.instance == null ){
            BasicSkeletonEnemyStrategy.instance = new BasicSkeletonEnemyStrategy(reflink);
        }
        return instance;
    }

    /**
     * @brief Gets the name of this enemy strategy.
     * This method overrides the {@link EnemyStrategy#getName()} method.
     * @return A {@link String} representing the name of the basic skeleton enemy (e.g., "BasicSkeleton").
     */
    @Override
    public String getName(){
        return Constants.BASIC_SKELETON_NAME;
    }

    /**
     * @brief Gets the source level associated with this enemy strategy.
     * This method overrides the {@link EnemyStrategy#getSource()} method.
     * @return A {@link String} representing the level where this enemy typically appears.
     */
    @Override
    public String getSource(){
        return Constants.LEVEL_2; // Indicates this enemy is primarily found in Level 2.
    }

    /**
     * @brief Constructs a BasicSkeletonEnemyStrategy object.
     *
     * Initializes the specific attributes (speed, hitbox, health, damage, health bar colors)
     * and loads the animations for the basic skeleton enemy. It also sets the
     * behavior IDs to respect based on Level 2's tile data.
     * This constructor is private to enforce the singleton pattern.
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public BasicSkeletonEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.BASIC_SKELETON_SPEED;
        this.hitboxWidth = 32;
        this.hitboxHeight = 32;

        this.levelWidthInTiles = Constants.LEVEL2_WIDTH;
        this.levelHeightInTiles = Constants.LEVEL2_HEIGHT;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.damage = Constants.BASIC_SKELETON_DAMAGE;
        this.health = Constants.BASIC_SKELETON_HEALTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,13,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,18,5,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,11,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.behaviorIDsToRespect = reflink.getGame().getLevel2().getBehaviorIDs();
    }

    /**
     * @brief Draws the name of the enemy on the screen.
     *
     * This method is typically used within the fight state to display the enemy's name
     * at a specific position with a particular font and color.
     * This method overrides the {@link EnemyStrategy#drawName(Graphics2D)} method.
     * @param g2d The {@link Graphics2D} context used for drawing.
     */
    @Override
    public void drawName(Graphics2D g2d){
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setColor(Color.RED);
        g2d.drawString(this.getName(),494,60);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }

}