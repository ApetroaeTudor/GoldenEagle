package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class WizardEnemyStrategy
 * @brief Implements the specific strategy for the Wizard enemy type.
 *
 * This class extends {@link EnemyStrategy} and defines the unique properties
 * and behaviors for Wizard enemies, such as their speed, health, damage,
 * animations, and the level they primarily appear in. It follows a Singleton
 * pattern to ensure only one instance of this strategy configuration exists.
 */
public class WizardEnemyStrategy extends EnemyStrategy{

    private static WizardEnemyStrategy instance = null; ///< Singleton instance of the WizardEnemyStrategy.

    /**
     * @brief Gets the singleton instance of the WizardEnemyStrategy.
     *
     * If the instance does not exist, it creates a new one.
     * This method ensures that only one instance of the strategy is used throughout the game.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @return The singleton instance of {@link WizardEnemyStrategy}.
     */
    public static WizardEnemyStrategy getInstance(RefLinks reflink){
        if(WizardEnemyStrategy.instance == null){
            WizardEnemyStrategy.instance = new WizardEnemyStrategy(reflink);
        }
        return instance;
    }


    /**
     * @brief Constructs a WizardEnemyStrategy object.
     *
     * Initializes the specific properties for the Wizard enemy, including
     * speed, hitbox dimensions, health, damage, and animations. It loads the
     * necessary animations (walking, attacking, idle) and sets up the behavior
     * tile IDs relevant to its movement and interaction within Level 3.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public WizardEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.WIZARD_SPEED;
        this.hitboxWidth = Constants.WIZARD_PASSIVE_TILE_WIDTH;
        this.hitboxHeight = Constants.WIZARD_PASSIVE_TILE_HEIGHT;

        this.levelHeightInTiles = Constants.LEVEL3_HEIGHT;
        this.levelWidthInTiles = Constants.LEVEL3_WIDTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.WIZARD_PASSIVE_TILE_NR,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.WIZARD_ATTACKING_TILE_NR,10,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.WIZARD_IN_FIGHT_IDLE_TILE_NR,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.WIZARD_DAMAGE;
        this.health = Constants.WIZARD_HEALTH;

        this.healthBarColor1 = Constants.PURPLE_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.PURPLE_HEALTH_BAR_COLOR_2;

        this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
    }

    /**
     * @brief Gets the name of the enemy type this strategy represents.
     * This method overrides the {@link EnemyStrategy#getName()} method.
     * @return A {@link String} representing the name of the enemy (e.g., "Wizard").
     */
    @Override
    public String getName(){
        return Constants.WIZARD_NAME;
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
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font("Arial",Font.BOLD,50));
        g2d.setColor(Color.RED);
        g2d.drawString(this.getName(),390,95);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }



}