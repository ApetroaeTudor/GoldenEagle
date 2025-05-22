package PaooGame.Strategies.Fight;

import PaooGame.Entities.Enemy;
import PaooGame.States.State;

/**
 * @class FightStrategy
 * @brief Represents the configuration and state for an enemy during a fight sequence.
 *
 * This class encapsulates properties related to an enemy's appearance and behavior
 * within the fight state, such as its position, size, health bar details, background image,
 * and defense. It uses a Builder pattern for construction.
 */
public class FightStrategy {
    protected float x;                              ///< The x-coordinate of the enemy in the fight scene.
    protected float y;                              ///< The y-coordinate of the enemy in the fight scene.
    protected float width;                          ///< The width of the enemy in the fight scene.
    protected float height;                         ///< The height of the enemy in the fight scene.
    protected int healthBarX;                       ///< The x-coordinate of the enemy's health bar.
    protected int healthBarY;                       ///< The y-coordinate of the enemy's health bar.
    protected int healthBarWidth;                   ///< The width of the enemy's health bar.
    protected int healthBarHeight;                  ///< The height of the enemy's health bar.
    protected String backgroundImgPath;            ///< Path to the background image for the fight scene.
    protected Enemy enemy;                          ///< The enemy entity associated with this fight strategy.
    protected float defence;                        ///< The defense value of the enemy, reducing incoming damage.
    protected State ownerState;                     ///< The state that owns or initiated this fight (e.g., a specific level state).

    /**
     * @brief Constructs a FightStrategy object using a {@link FightStrategyBuilder}.
     * @param builder The builder object containing the configuration for the fight strategy.
     */
    public FightStrategy(FightStrategyBuilder builder){
        this.enemy = builder.enemy;
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.healthBarX = builder.healthBarX;
        this.healthBarY = builder.healthBarY;
        this.healthBarWidth = builder.healthBarWidth;
        this.healthBarHeight = builder.healthBarHeight;
        this.backgroundImgPath = builder.backgroundImgPath;
        this.defence = builder.defence;
        this.ownerState = builder.ownerState;
    }

    /**
     * @brief Updates the associated enemy's properties based on this strategy.
     *
     * Sets the enemy's position and health bar dimensions according to the values
     * stored in this fight strategy.
     */
    public void update(){
        this.enemy.setX(this.x);
        this.enemy.setY(this.y);
        this.enemy.setHealthBarX(this.healthBarX);
        this.enemy.setHealthBarY(this.healthBarY);
        this.enemy.setHealthBarWidth(this.healthBarWidth);
        this.enemy.setHealthBarHeight(this.healthBarHeight);
    }

    /**
     * @brief Applies damage to the enemy, considering its defense.
     * @param damage The raw damage amount to be applied.
     */
    public void takeDamage(float damage){
        this.enemy.reduceHealth((1f-this.defence)*damage);
    }

    /**
     * @brief Calculates the actual damage taken by the enemy after considering its defense.
     * @param damage The raw damage amount.
     * @return The calculated damage after applying the defense reduction.
     */
    public float calculateDamage(float damage){
        return (1f-this.defence)*damage;
    }


    /**
     * @class FightStrategyBuilder
     * @brief Builder class for fluently constructing {@link FightStrategy} objects.
     *
     * This class implements the Builder pattern, allowing for a more readable and flexible
     * way to create instances of {@link FightStrategy} by chaining configuration methods.
     */
    public static class FightStrategyBuilder {
        private float x = 0f;                       ///< The x-coordinate for the enemy in the fight.
        private float y = 0f;                       ///< The y-coordinate for the enemy in the fight.
        private float width = 0f;                   ///< The width for the enemy in the fight.
        private float height = 0f;                  ///< The height for the enemy in the fight.
        private int healthBarX = 0;                 ///< The x-coordinate for the enemy's health bar.
        private int healthBarY = 0;                 ///< The y-coordinate for the enemy's health bar.
        private int healthBarWidth = 0;             ///< The width for the enemy's health bar.
        private int healthBarHeight = 0;            ///< The height for the enemy's health bar.
        private String backgroundImgPath;           ///< Path to the background image for the fight.
        private Enemy enemy = null;                 ///< The enemy entity for the fight.
        private float defence = 0f;                 ///< The defense value for the enemy.
        private State ownerState = null;            ///< The state that owns this fight strategy.

        /**
         * @brief Constructs a FightStrategyBuilder.
         * @param enemy The {@link Enemy} entity this fight strategy will be associated with. This is a mandatory parameter.
         */
        public FightStrategyBuilder(Enemy enemy){
            this.enemy = enemy;
        }

        /**
         * @brief Sets the x-coordinate for the enemy in the fight.
         * @param x The x-coordinate.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder x(float x){
            this.x = x;
            return this;
        }

        /**
         * @brief Sets the y-coordinate for the enemy in the fight.
         * @param y The y-coordinate.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder y(float y){
            this.y = y;
            return this;
        }

        /**
         * @brief Sets the width for the enemy in the fight.
         * @param width The width.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder width(float width){
            this.width = width;
            return this;
        }

        /**
         * @brief Sets the height for the enemy in the fight.
         * @param height The height.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder height(float height){
            this.height = height;
            return this;
        }

        /**
         * @brief Sets the x-coordinate for the enemy's health bar.
         * @param healthBarX The health bar x-coordinate.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder healthBarX(int healthBarX){
            this.healthBarX = healthBarX;
            return this;
        }

        /**
         * @brief Sets the y-coordinate for the enemy's health bar.
         * @param healthBarY The health bar y-coordinate.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder healthBarY(int healthBarY){
            this.healthBarY = healthBarY;
            return this;
        }

        /**
         * @brief Sets the width for the enemy's health bar.
         * @param healthBarWidth The health bar width.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder healthBarWidth(int healthBarWidth){
            this.healthBarWidth = healthBarWidth;
            return this;
        }

        /**
         * @brief Sets the height for the enemy's health bar.
         * @param healthBarHeight The health bar height.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder healthBarHeight(int healthBarHeight){
            this.healthBarHeight = healthBarHeight;
            return this;
        }

        /**
         * @brief Sets the path to the background image for the fight.
         * @param backgroundImgPath The file path of the background image.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder backgroundImgPath(String backgroundImgPath){
            this.backgroundImgPath = backgroundImgPath;
            return this;
        }

        /**
         * @brief Sets the defense value for the enemy.
         * @param defence The defense value (e.g., 0.1 for 10% damage reduction).
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder defence(float defence){
            this.defence = defence;
            return this;
        }

        /**
         * @brief Sets the owner state for this fight strategy.
         * @param ownerState The {@link State} that owns or initiates this fight.
         * @return This builder instance for method chaining.
         */
        public FightStrategyBuilder ownerState(State ownerState){
            this.ownerState = ownerState;
            return this;
        }

        /**
         * @brief Builds and returns a {@link FightStrategy} instance with the configured properties.
         * @return A new {@link FightStrategy} object.
         */
        public FightStrategy build(){
            return new FightStrategy(this);
        }
    }

    /**
     * @brief Sets the enemy entity for this fight strategy.
     * @param enemy The {@link Enemy} to associate with this strategy.
     */
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * @brief Sets the defense value for the enemy.
     * @param defence The new defense value.
     */
    public void setDefence(float defence) {
        this.defence = defence;
    }

    /**
     * @brief Sets the x-coordinate of the enemy in the fight scene.
     * @param x The new x-coordinate.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @brief Sets the y-coordinate of the enemy in the fight scene.
     * @param y The new y-coordinate.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @brief Sets the width of the enemy in the fight scene.
     * @param width The new width.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * @brief Sets the height of the enemy in the fight scene.
     * @param height The new height.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * @brief Sets the x-coordinate of the enemy's health bar.
     * @param healthBarX The new health bar x-coordinate.
     */
    public void setHealthBarX(int healthBarX) {
        this.healthBarX = healthBarX;
    }

    /**
     * @brief Sets the y-coordinate of the enemy's health bar.
     * @param healthBarY The new health bar y-coordinate.
     */
    public void setHealthBarY(int healthBarY) {
        this.healthBarY = healthBarY;
    }

    /**
     * @brief Sets the width of the enemy's health bar.
     * @param healthBarWidth The new health bar width.
     */
    public void setHealthBarWidth(int healthBarWidth) {
        this.healthBarWidth = healthBarWidth;
    }

    /**
     * @brief Sets the height of the enemy's health bar.
     * @param healthBarHeight The new health bar height.
     */
    public void setHealthBarHeight(int healthBarHeight) {
        this.healthBarHeight = healthBarHeight;
    }

    /**
     * @brief Sets the path to the background image for the fight scene.
     * @param backgroundImgPath The new file path for the background image.
     */
    public void setBackgroundImgPath(String backgroundImgPath) {
        this.backgroundImgPath = backgroundImgPath;
    }


    /**
     * @brief Gets the defense value of the enemy.
     * @return The defense value.
     */
    public float getDefence() {
        return defence;
    }

    /**
     * @brief Gets the enemy entity associated with this strategy.
     * @return The {@link Enemy} object.
     */
    public Enemy getEnemy() {
        return enemy;
    }

    /**
     * @brief Gets the x-coordinate of the enemy in the fight scene.
     * @return The x-coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * @brief Gets the y-coordinate of the enemy in the fight scene.
     * @return The y-coordinate.
     */
    public float getY() {
        return y;
    }

    /**
     * @brief Gets the width of the enemy in the fight scene.
     * @return The width.
     */
    public float getWidth() {
        return width;
    }

    /**
     * @brief Gets the height of the enemy in the fight scene.
     * @return The height.
     */
    public float getHeight() {
        return height;
    }

    /**
     * @brief Gets the x-coordinate of the enemy's health bar.
     * @return The health bar x-coordinate.
     */
    public int getHealthBarX() {
        return healthBarX;
    }

    /**
     * @brief Gets the y-coordinate of the enemy's health bar.
     * @return The health bar y-coordinate.
     */
    public int getHealthBarY() {
        return healthBarY;
    }

    /**
     * @brief Gets the width of the enemy's health bar.
     * @return The health bar width.
     */
    public int getHealthBarWidth() {
        return healthBarWidth;
    }

    /**
     * @brief Gets the height of the enemy's health bar.
     * @return The health bar height.
     */
    public int getHealthBarHeight() {
        return healthBarHeight;
    }

    /**
     * @brief Gets the path to the background image for the fight scene.
     * @return The file path of the background image.
     */
    public String getBackgroundImgPath() {
        return this.backgroundImgPath;
    }

    /**
     * @brief Gets the owner state of this fight strategy.
     * @return The {@link State} that owns or initiated this fight.
     */
    public State getOwnerState(){
        return this.ownerState;
    }

}