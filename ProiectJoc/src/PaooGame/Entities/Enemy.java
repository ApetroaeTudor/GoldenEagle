package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.Maps.Level;
import PaooGame.RefLinks;
import PaooGame.States.State;
import PaooGame.Strategies.EnemyStrategies.EnemyStrategy;

import java.util.Objects;

/**
 * @class Enemy
 * @brief Represents an enemy entity in the game.
 *
 * They have specific behaviors, animations, and attributes defined by an EnemyStrategy.
 */
public class Enemy extends Entity {
    private Constants.ENEMY_STATES currentState; ///< The current state of the enemy (for example walking, attacking).

    private Animation walkingAnimation; ///< Animation for when the enemy is walking.
    private Animation inFightIdleAnimation; ///< Animation for when the enemy is idle during a fight.
    private Animation inFightAttackingAnimation; ///< Animation for when the enemy is attacking during a fight.

    private EnemyStrategy enemyStrategy; ///< The strategy defining the enemy's behavior, attributes, and animations.

    private int directionSwitchCounter = 5; ///< Counter to manage a direction switch timeout

    /**
     * @brief Constructs an Enemy object.
     *
     * Initializes the enemy based on its type, setting up its strategy,
     * animations, hitbox, and other attributes.
     * @param reflink A reference to the game's RefLinks object for accessing shared resources.
     * @param startX The initial X-coordinate of the enemy.
     * @param startY The initial Y-coordinate of the enemy.
     * @param enemyType A string representing the type of the enemy (for example "Tiger", "Skeleton").
     */
    public Enemy(RefLinks reflink, int startX, int startY, String enemyType){
        super(reflink,startX,startY);

        switch (enemyType){
            case Constants.TIGER_NAME:
                this.enemyStrategy = this.reflink.getTigerEnemyStrategy();
                break;
            case Constants.BASIC_SKELETON_NAME:
                this.enemyStrategy = this.reflink.getBasicSkeletonEnemyStrategy();
                break;
            case Constants.WIZARD_NAME:
                this.enemyStrategy = this.reflink.getWizardEnemyStrategy();
                break;
            case Constants.MINOTAUR_NAME:
                this.enemyStrategy = this.reflink.getMinotaurEnemyStrategy();
                break;
            case Constants.GHOST_NAME:
                this.enemyStrategy = this.reflink.getGhostEnemyStrategy();
                break;
            case Constants.STRONG_SKELETON_NAME:
                this.enemyStrategy = this.reflink.getStrongSkeletonEnemyStrategy();
                break;
        }

        this.speed = this.enemyStrategy.getSpeed();
        this.hitbox = new Hitbox(this.x,this.y,this.enemyStrategy.getHitboxWidth(),this.enemyStrategy.getHitboxHeight()); //
        this.currentState = Constants.ENEMY_STATES.FALLING;

        this.setHealthBarColor1(this.enemyStrategy.getHealthBarColor1());
        this.setHealthBarColor2(this.enemyStrategy.getHealthBarColor2());

        this.walkingAnimation = this.enemyStrategy.getWalkingAnimation();
        this.inFightAttackingAnimation = this.enemyStrategy.getInFightAttackingAnimation();
        this.inFightIdleAnimation = this.enemyStrategy.getInFightIdleAnimation();

        this.damage = this.enemyStrategy.getDamage();
        this.health = this.enemyStrategy.getHealth();

        this.behaviorIDsToRespect = this.enemyStrategy.getBehaviorIDsToRespect();


    }


    /**
     * @brief Restores the entity to its initial state.
     *
     */
    @Override
    public void restoreEntity() {

    }

    /**
     * @brief Updates the enemy's state and behavior.
     *
     * Handles gravity, movement, collision detection, and animation updates based on the enemy's current state.
     */
    @Override
    public void update(){
        if(this.currentState == Constants.ENEMY_STATES.FALLING || this.currentState == Constants.ENEMY_STATES.WALKING){
            this.hitbox.setWidth(this.enemyStrategy.getHitboxWidth());
            this.hitbox.setHeight(this.enemyStrategy.getHitboxHeight());
            applyGravity();
            moveAndCollide();
            updateVisualPosition();
        }
        updateAnimationState();
        this.getAnimationByState().updateAnimation();

    }

    /**
     * @brief Updates the enemy's visual position based on its hitbox.
     *
     * Synchronizes the entity's x and y coordinates with its hitbox's position.
     */
    @Override
    protected void updateVisualPosition() {
        this.x = this.hitbox.getX();
        this.y = this.hitbox.getY();
    }

    /**
     * @brief Retrieves the appropriate animation based on the enemy's current state.
     * @return The Animation object corresponding to the current state.
     *         Defaults to walking animation if no specific fight animation is matched.
     */
    @Override
    protected Animation getAnimationByState() {
        Animation returnAni = this.walkingAnimation;
        switch (this.currentState){
            case IN_FIGHT_IDLE:
                returnAni = this.inFightIdleAnimation;
                break;
            case IN_FIGHT_ATTACKING:
                returnAni = this.inFightAttackingAnimation;
                break;
        }
        return returnAni;
    }

    /**
     * @brief Updates the enemy's animation state based on its current conditions.
     *
     * Transitions between states like attacking, idle, walking, and falling.
     */
    @Override
    protected void updateAnimationState() {
        if (this.currentState == Constants.ENEMY_STATES.IN_FIGHT_ATTACKING) {
            if (this.inFightAttackingAnimation.getIsFinished()) {
                this.currentState = Constants.ENEMY_STATES.IN_FIGHT_IDLE;
            } else {
                return;
            }
        }

        boolean isInFightState = State.getState() != null && Objects.equals(State.getState().getStateName(), Constants.FIGHT_STATE);
        if (this.isEngaged && isInFightState) {
            this.currentState = Constants.ENEMY_STATES.IN_FIGHT_IDLE;
        }
        if (this.currentState != Constants.ENEMY_STATES.IN_FIGHT_IDLE && this.currentState != Constants.ENEMY_STATES.IN_FIGHT_ATTACKING)
        {
            if (this.isGrounded) {
                this.currentState = Constants.ENEMY_STATES.WALKING;
            } else {
                if (this.currentState != Constants.ENEMY_STATES.FALLING){
                    this.currentState = Constants.ENEMY_STATES.FALLING;
                }
                else if (this.currentState == Constants.ENEMY_STATES.WALKING && !this.isGrounded){
                    this.currentState = Constants.ENEMY_STATES.FALLING;
                }
            }
        }
    }

    /**
     * @brief Handles the enemy's movement and collision detection.
     *
     * Applies horizontal movement, checks for ground ahead to change direction,
     * and handles vertical movement (falling) with ground collision.
     */
    @Override
    protected void moveAndCollide() {
        this.velocityX = this.speed;
        float originalX = this.hitbox.getX(); // the left side of the hitbox
        float deltaX = this.velocityX; // how much the enemy should move in a direction on the X axis

        boolean changingDirection = !Level.isGroundAhead(this.hitbox,!this.flipped,this.enemyStrategy.getLevelWidthInTiles(),this.enemyStrategy.getLevelHeightInTiles(),this.behaviorIDsToRespect);
        if(directionSwitchCounter == 5){
            if(changingDirection){
                speed=-speed;
                this.directionSwitchCounter=0;
            }
        }
        else{
            this.directionSwitchCounter++;
        }


        // if the movement is valid the hitbox is also updated
        this.hitbox.setX(originalX + deltaX);


        if(this.velocityX<0){
            this.flipped=false;
        }
        else if(velocityX>0){
            this.flipped=true;
        }


        float originalY = this.hitbox.getY();
        float deltaY = this.velocityY;
        this.hitbox.setY(originalY + deltaY);

        int fallCheckResult = Level.checkFalling(hitbox,this.enemyStrategy.getLevelWidthInTiles(),this.enemyStrategy.getLevelHeightInTiles(),this.behaviorIDsToRespect);


        if (this.velocityY > 0) { // Moving Down
            if (fallCheckResult == 0) { // Hit ground
                this.isGrounded = true;
                this.velocityY = 0;
                Level.snapToGround(this.hitbox);
            } else {
                this.isGrounded = false;
            }
        } else if (this.velocityY < 0) { // Moving
            this.isGrounded = false;

        } else { // velocityY == 0
            if (fallCheckResult == 0) { // Standing still on ground
                if (!this.isGrounded) { // Just landed
                    Level.snapToGround(this.hitbox);
                }
                this.isGrounded = true;
                this.velocityY = 0; // Ensure velocity is 0 when grounded
            } else { // Standing still in air
                this.isGrounded = false;
            }
        }
    }

    /**
     * @brief Sets the current state of the enemy.
     * @param state The new state for the enemy, from Constants.ENEMY_STATES.
     */
    public void setCurrentState(Constants.ENEMY_STATES state){
        this.currentState = state;
    }

    /**
     * @brief Gets the name of the enemy.
     *
     * Retrieves the name from the enemy's strategy.
     * @return The name of the enemy.
     */
    @Override
    public String getName(){
        return this.enemyStrategy.getName();
    }


    /**
     * @brief Initiates an attack by the enemy.
     *
     * Sets the enemy's state to IN_FIGHT_ATTACKING and triggers the attack animation.
     */
    @Override
    public void attack(){
        this.currentState = Constants.ENEMY_STATES.IN_FIGHT_ATTACKING;
        this.getAnimationByState().triggerOnce();
    }

    /**
     * @brief Gets the source path for the enemy's owner level.
     *
     */
    @Override
    public String getSource() {
        return this.enemyStrategy.getSource();
    } //

    /**
     * @brief Sets the X-coordinate of the enemy.
     *
     * Also updates the X-coordinate of the enemy's hitbox.
     * @param x The new X-coordinate.
     */
    public void setX(int x){this.x = x; this.getHitbox().setX(this.x);}

    /**
     * @brief Sets the Y-coordinate of the enemy.
     *
     * Also updates the Y-coordinate of the enemy's hitbox.
     * @param y The new Y-coordinate.
     */
    public void setY(int y){this.y = y; this.getHitbox().setY(this.y);}

    /**
     * @brief Gets the enemy's strategy object.
     * @return The EnemyStrategy associated with this enemy.
     */
    public EnemyStrategy getEnemyStrategy() {
        return enemyStrategy;
    }
}