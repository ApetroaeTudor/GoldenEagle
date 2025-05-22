package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class TigerEnemyStrategy
 * @brief Implements the specific strategy for the Tiger enemy type.
 *
 * This class extends {@link EnemyStrategy} and defines the unique properties
 * and behaviors for Tiger enemies, such as their speed, health, damage, animations,
 * and the level they primarily appear in. It follows a Singleton pattern to ensure
 * only one instance of this strategy configuration exists.
 */
public class TigerEnemyStrategy extends EnemyStrategy {

    private static TigerEnemyStrategy instance = null; ///< Singleton instance of the TigerEnemyStrategy.

    /**
     * @brief Gets the singleton instance of the TigerEnemyStrategy.
     *
     * If the instance does not exist, it creates a new one.
     * This method ensures that only one instance of the strategy is used throughout the game.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @return The singleton instance of {@link TigerEnemyStrategy}.
     */
    public static TigerEnemyStrategy getInstance(RefLinks reflink){
        if(TigerEnemyStrategy.instance == null ){
            TigerEnemyStrategy.instance = new TigerEnemyStrategy(reflink);
        }
        return instance;
    }


    /**
     * @brief Constructs a TigerEnemyStrategy object.
     *
     * Initializes the specific properties for the Tiger enemy, including
     * speed, hitbox dimensions, damage, and animations. It loads the
     * necessary animations (walking, attacking, idle) and sets up the behavior
     * tile IDs relevant to its movement and interaction within Level 1.
     * Note: Tiger health is not explicitly set here and might rely on a default
     * value in the base class or be set by the {@code Enemy} class itself using this strategy.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public TigerEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.TIGER_SPEED;
        this.hitboxWidth = (int)( (64.0/100.0)*50.0 ); // Scaled hitbox width, might benefit from being a constant
        this.hitboxHeight = (int)( (32.0/100)*50.0 ); // Scaled hitbox height, might benefit from being a constant

        this.levelWidthInTiles = Constants.LEVEL1_WIDTH;
        this.levelHeightInTiles = Constants.LEVEL1_HEIGHT;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.damage = Constants.TIGER_DAMAGE;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,4,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,4,15,this.getName()); //
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,1,10,this.getName()); //
        this.inFightIdleAnimation.loadAnimation();

        if (reflink.getGame().getLevel1() != null) {
            this.behaviorIDsToRespect = reflink.getGame().getLevel1().getBehaviorIDs();
        } else {
            // Handle case where Level 1 might not be initialized yet
            System.err.println("Warning: Level1 behavior IDs accessed before Level1 was fully initialized in TigerEnemyStrategy.");
            this.behaviorIDsToRespect = new int[0]; // Default to empty array or handle appropriately
        }
    }

    /**
     * @brief Gets the name of the enemy type this strategy represents.
     * This method overrides the {@link EnemyStrategy#getName()} method.
     * @return A {@link String} representing the name of the enemy (e.g., "Tiger").
     */
    @Override
    public String getName(){
        return Constants.TIGER_NAME;
    }

    /**
     * @brief Gets the source or primary level where this enemy type is found.
     * This method overrides the {@link EnemyStrategy#getSource()} method.
     * @return A {@link String} representing the source level (e.g., "Level1").
     */
    @Override
    public String getSource(){
        return Constants.LEVEL_1;
    }

    /**
     * @brief Draws the name of the enemy on the screen, typically during a fight.
     * This method overrides the {@link EnemyStrategy#drawName(Graphics2D)} method.
     * @param g2d The {@link Graphics2D} context used for drawing.
     */
    @Override
    public void drawName(Graphics2D g2d){
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        RenderingHints oldHints = g2d.getRenderingHints();

        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setColor(Color.RED);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.drawString(this.getName(),520,60); // Fixed position

        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
        g2d.setRenderingHints(oldHints);
    }


}