package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.HUD.HealthBar;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class Entity
 * @brief An abstract base class for all game entities (e.g., Player, Enemy).
 *
 * This class provides common properties and functionalities for game entities,
 * such as position, health, hitbox, and rendering.
 * It defines abstract methods that subclasses must implement to define specific
 * behaviors and appearances.
 */
public abstract class Entity {
    protected float x; ///< The x-coordinate of the entity's position.
    protected float y; ///< The y-coordinate of the entity's position.
    private HealthBar healthBar; ///< The health bar associated with this entity.
    protected int[] behaviorIDsToRespect; ///< Array of tile behavior IDs that this entity should respect or interact with.


    protected boolean isEngaged; ///< Flag indicating if the entity is currently engaged in combat or a specific interaction.

    protected float gravity; ///< The gravitational force applied to the entity.
    protected float maxFallSpeed; ///< The maximum speed at which the entity can fall.

    protected float speed; ///< The movement speed of the entity.
    protected double health; ///< The current health of the entity.
    protected double damage; ///< The amount of damage this entity can inflict.

    protected boolean isGrounded; ///< Flag indicating if the entity is currently on the ground.
    protected boolean flipped; ///< Flag indicating if the entity's sprite should be flipped horizontally.

    protected Hitbox hitbox; ///< The hitbox used for collision detection.

    protected RefLinks reflink; ///< A reference to shared game resources and utilities.

    protected float velocityX; ///< The entity's current velocity on the x-axis.
    protected float velocityY; ///< The entity's current velocity on the y-axis.

    /**
     * @brief Constructs an Entity object.
     *
     * Initializes the entity's position, health bar, references, and default physics properties.
     * @param reflink A reference to shared game resources.
     * @param startX The initial x-coordinate of the entity.
     * @param startY The initial y-coordinate of the entity.
     */
    public Entity(RefLinks reflink, int startX, int startY){
        this.healthBar = new HealthBar(this);

        this.x = startX;
        this.y = startY;
        this.reflink = reflink;
        this.gravity = Constants.BASE_ENTITY_GRAVITY;
        this.maxFallSpeed = Constants.BASE_MAX_ENTITY_FALL_SPEED;

        this.velocityX = 0f;
        this.velocityY = 0f;

        this.flipped = false;
        this.isGrounded = false;

    }

    /**
     * @brief Applies gravitational force to the entity if it is not grounded.
     *
     * Increases the vertical velocity (velocityY) by the gravity amount, up to the maximum fall speed.
     */
    protected void applyGravity(){
        if (!this.isGrounded) {
            this.velocityY += this.gravity; // the velocity accelerates
            if (this.velocityY > this.maxFallSpeed) {
                this.velocityY = this.maxFallSpeed;
            }
        }
    }

    /**
     * @brief Abstract method for handling entity movement and collision detection.
     *
     * Subclasses must implement this to define how the entity moves and interacts with its environment.
     */
    protected abstract void moveAndCollide();

    /**
     * @brief Abstract method for updating the entity's state.
     *
     * Subclasses must implement this to define the entity's logic per game tick.
     */
    public abstract void update();

    /**
     * @brief Abstract method for updating the entity's visual position.
     *
     * Typically used to synchronize the drawing position with the hitbox or logical position.
     */
    protected abstract void updateVisualPosition();

    /**
     * @brief Abstract method for retrieving the current animation based on the entity's state.
     * @return The Animation object corresponding to the current state.
     */
    protected abstract Animation getAnimationByState();

    /**
     * @brief Abstract method for updating the entity's animation state.
     *
     * Subclasses must implement this to manage transitions between different animations.
     */
    protected abstract void updateAnimationState();


    /**
     * @brief Gets the x-coordinate of the entity.
     * @return The current x-coordinate.
     */
    public float getX() {
        return x;
    }
    /**
     * @brief Sets the x-coordinate of the entity.
     * @param x The new x-coordinate.
     */
    public void setX(float x){this.x = x;}

    /**
     * @brief Gets the y-coordinate of the entity.
     * @return The current y-coordinate.
     */
    public float getY() {
        return y;
    }
    /**
     * @brief Sets the y-coordinate of the entity.
     * @param y The new y-coordinate.
     */
    public void setY(float y){this.y = y;}

    /**
     * @brief Gets the width of the entity's hitbox.
     * @return The width of the hitbox.
     */
    public float getWidth() {
        return hitbox.getWidth();
    }

    /**
     * @brief Gets the height of the entity's hitbox.
     * @return The height of the hitbox.
     */
    public float getHeight() {
        return hitbox.getHeight();
    }

    /**
     * @brief Gets the current health of the entity.
     * @return The current health value.
     */
    public double getHealth() {
        return health;
    }
    /**
     * @brief Sets the health of the entity.
     * @param health The new health value.
     */
    public void setHealth(double health) {this.health = health;}

    /**
     * @brief Gets the hitbox of the entity.
     * @return The Hitbox object.
     */
    public Hitbox getHitbox() { return this.hitbox; }

    /**
     * @brief Sets the hitbox for the entity.
     * @param hitbox The new Hitbox object.
     */
    public void setHitbox(Hitbox hitbox) { this.hitbox = hitbox; }

    /**
     * @brief Gets the health bar associated with this entity.
     * @return The HealthBar object.
     */
    public HealthBar getHealthBar(){return this.healthBar; }

    /**
     * @brief Checks if the entity is currently engaged.
     * @return True if engaged, false otherwise.
     */
    public boolean getIsEngaged(){
        return this.isEngaged;
    }
    /**
     * @brief Sets the engagement status of the entity.
     * @param isEngaged True to set as engaged, false otherwise.
     */
    public void setIsEngaged(boolean isEngaged){
        this.isEngaged = isEngaged;
    }

    /**
     * @brief Abstract method to get the name of the entity.
     * @return A string representing the entity's name.
     */
    public abstract String getName();



    /**
     * @brief Draws the entity's health bar.
     * @param g The Graphics context to draw on.
     */
    public void DrawHealthBar(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        healthBar.draw(g2d);
    }

    /**
     * @brief Sets the x-coordinate of the health bar.
     * @param x The new x-coordinate for the health bar.
     */
    public void setHealthBarX(int x){
        this.healthBar.setX(x);
    }
    /**
     * @brief Sets the y-coordinate of the health bar.
     * @param y The new y-coordinate for the health bar.
     */
    public void setHealthBarY(int y){
        this.healthBar.setY(y);
    }
    /**
     * @brief Sets the width of the health bar.
     * @param width The new width for the health bar.
     */
    public void setHealthBarWidth(int width){
        this.healthBar.setWidth(width);
    }
    /**
     * @brief Sets the height of the health bar.
     * @param height The new height for the health bar.
     */
    public void setHealthBarHeight(int height){
        this.healthBar.setHeight(height);
    }

    /**
     * @brief Gets the x-coordinate of the health bar.
     * @return The x-coordinate of the health bar.
     */
    public int getHealthBarX(){return this.healthBar.getX();}
    /**
     * @brief Gets the y-coordinate of the health bar.
     * @return The y-coordinate of the health bar.
     */
    public int getHealthBarY(){return this.healthBar.getY();}
    /**
     * @brief Gets the width of the health bar.
     * @return The width of the health bar.
     */
    public int getHealthBarWidth(){return this.healthBar.getWidth();}
    /**
     * @brief Gets the height of the health bar.
     * @return The height of the health bar.
     */
    public int getHealthBarHeight(){return this.healthBar.getHeight();}

    /**
     * @brief Gets the primary color of the health bar.
     * @return The primary Color object.
     */
    public Color getHealthBarColor1(){return this.healthBar.getColor1();}
    /**
     * @brief Gets the secondary color of the health bar.
     * @return The secondary Color object.
     */
    public Color getHealthBarColor2(){return this.healthBar.getColor2();}

    /**
     * @brief Sets the primary color of the health bar.
     * @param color1 The new primary Color.
     */
    public void setHealthBarColor1(Color color1){this.healthBar.setColor1(color1);}
    /**
     * @brief Sets the secondary color of the health bar.
     * @param color2 The new secondary Color.
     */
    public void setHealthBarColor2(Color color2){this.healthBar.setColor2(color2);}

    /**
     * @brief Resets the health bar's position to its default values.
     */
    public void resetHealthBarDefaultValues(){this.healthBar.resetPositionToDefault();}

    /**
     * @brief Reduces the entity's health by a specified amount.
     * Health will not go below 0.
     * @param health The amount of health to reduce.
     */
    public void reduceHealth(double health) { this.health-=health; if(this.health < 0){this.health = 0;}}

    /**
     * @brief Restores the entity's health by a specified amount.
     * Health will not exceed 100 (or a predefined maximum).
     * @param health The amount of health to restore.
     */
    public void restoreHealth(double health) { this.health+=health; if(this.health > 100){this.health = 100;}} // Assuming 100 is max health

    /**
     * @brief Gets the damage value of the entity.
     * @return The amount of damage the entity inflicts.
     */
    public double getDamage(){ return this.damage; }
    /**
     * @brief Sets the damage value for the entity.
     * @param damage The new damage value.
     */
    public void setDamage(double damage){this.damage = damage;}

    /**
     * @brief Nullifies the entity's hitbox by setting its position and dimensions to zero.
     * This effectively makes the entity non-collidable.
     */
    public void nullifyHitbox(){
        this.hitbox.setX(0);
        this.hitbox.setY(0);
        this.hitbox.setWidth(0);
        this.hitbox.setHeight(0);
    }

    /**
     * @brief Abstract method to restore the entity to its initial or a saved state.
     * Subclasses must implement this to define how the entity is reset.
     */
    public abstract void restoreEntity();

    /**
     * @brief Abstract method for the entity to perform an attack.
     * Subclasses must implement this to define attack behavior.
     */
    public abstract void attack();

    /**
     * @brief Gets the entity's current velocity on the x-axis.
     * @return The x-axis velocity.
     */
    public float getVelocityX(){return this.velocityX;}
    /**
     * @brief Gets the entity's current velocity on the y-axis.
     * @return The y-axis velocity.
     */
    public float getVelocityY(){return this.velocityY;}

    /**
     * @brief Abstract method to get the source path for the entity's sprites or assets.
     * @return A string representing the asset source path.
     */
    public abstract String getSource();

    /**
     * @brief Draws the entity on the screen using its current animation.
     * @param g The Graphics context to draw on.
     */
    public void draw(Graphics g){
        this.getAnimationByState().paintAnimation(g,(int)this.x,(int)this.y,this.flipped,1);
    }

    /**
     * @brief Sets whether the entity's sprite should be flipped horizontally.
     * @param flipped True to flip, false otherwise.
     */
    public void setIsFlipped(boolean flipped )  {this.flipped = flipped;}
    /**
     * @brief Checks if the entity's sprite is currently flipped.
     * @return True if flipped, false otherwise.
     */
    public boolean getIsFlipped() { return this.flipped;}

}