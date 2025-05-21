package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.PlayerAnimations.PlayerActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;
import PaooGame.Hitbox.Hitbox;
import PaooGame.Maps.Level;
import PaooGame.RefLinks;
import PaooGame.States.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @class Hero
 * @brief Represents the main player character in the game.
 *
 * The Hero class extends Entity and implements specific behaviors for the player,
 * including input handling, movement (running, jumping, grappling), animations,
 * health management, and interactions with the game world and data management systems.
 */
public class Hero extends Entity {
    private float jumpStrength; ///< The initial upward velocity applied when the hero jumps.

    private Timer deathAnimationTimer; ///< Timer to manage the duration of the death animation/state.
    private int timeoutInMillisForDeathAnimation = 600; ///< Duration of the death animation state in milliseconds.
    private boolean isDying = false; ///< Flag indicating if the hero is currently in the dying state.
    private int currentGrappleX = 0; ///< The X-coordinate (tile index) of the current grapple point.
    private int currentGrappleY = 0; ///< The Y-coordinate (tile index) of the current grapple point.
    private boolean isGrappling = false; ///< Flag indicating if the hero is currently grappling.

    int gold = 0; ///< The amount of gold collected by the hero.

    private boolean hasWhip = false; ///< Flag indicating if the hero has acquired the whip.

    private Timer grappleExpiredTimer; ///< Timer to manage the duration of a grapple attempt.
    private int grappleTimeoutMillis = 120; ///< Timeout for a grapple attempt in milliseconds.
    private boolean isGrapplingTimerExpired = false; ///< Flag indicating if the grapple timer has expired.
    private boolean isGrappleInterrupted = false; ///< Flag indicating if the grapple action was interrupted (e.g., by jumping).
    private boolean didJumpAfterGrapple = false; ///< Flag indicating if the hero jumped immediately after a grapple.
    private boolean isEngageReady = true; ///< Flag indicating if the hero can engage in combat or other interactions.

    private int nrOfEscapes = 2; ///< The current number of escapes (from fights) the hero has.
    private int maxNrOfEscapes = 2; ///< The maximum number of escapes the hero can have.
    private int nrOfCompletedLevels = 0; ///< The number of levels the hero has successfully completed.

    private int nrOfCollectedSaves = 0; ///< The number of save points or collectibles the hero has gathered.

    private int score = 0; ///< The hero's current score.

    private int jumpCap; ///< A resource that limits jumping.

    private Constants.HERO_STATES currentState; ///< The current state of the hero (e.g., idle, running, jumping).


    private float hitboxOffsetX = 16; ///< Offset for the hero's hitbox X-position relative to the hero's X-position.
    private float hitboxOffsetY = 9; ///< Offset for the hero's hitbox Y-position relative to the hero's Y-position.

    private int LEVEL_WIDTH; ///< The width of the current level in tiles.
    private int LEVEL_HEIGHT; ///< The height of the current level in tiles.


    private Animation idleAnimation; ///< Animation for when the hero is idle.
    private Animation fallingAnimation; ///< Animation for when the hero is falling.
    private Animation runningAnimation; ///< Animation for when the hero is running.
    private Animation jumpingAnimation; ///< Animation for when the hero is jumping.
    private Animation attackingAnimation; ///< Animation for when the hero is attacking.
    private Animation crounchingAnimation; ///< Animation for when the hero is crouching.

    /**
     * @brief Constructs a Hero object.
     *
     * Initializes the hero's properties, timers, animations, hitbox, and default values.
     * @param refLink A reference to shared game resources.
     * @param startX The initial X-coordinate of the hero.
     * @param startY The initial Y-coordinate of the hero.
     */
    public Hero(RefLinks refLink, int startX, int startY) {
        super(refLink,startX,startY);

        this.deathAnimationTimer = new Timer(this.timeoutInMillisForDeathAnimation,e->{
            this.health = 0;
            this.isDying = false;
        });
        this.deathAnimationTimer.setRepeats(false);

        this.grappleExpiredTimer = new Timer(this.grappleTimeoutMillis,e -> {
            this.isGrappling = false;
            this.isGrapplingTimerExpired = true;
            if(!this.didJumpAfterGrapple){
                this.velocityY = this.jumpStrength*1.2f;
                this.isGrounded = false;
                this.jumpCap -= 4;
            }
            this.didJumpAfterGrapple = false;

        });
        this.grappleExpiredTimer.setRepeats(false);


        this.jumpStrength = Constants.HERO_BASE_JUMP_STRENGTH;  // Initial upward velocity (negative Y).

        this.speed = Constants.HERO_BASE_SPEED;             // Base horizontal speed
        this.jumpCap = 10;          // Stamina/resource for jumps


        this.hitbox = new Hitbox(this.x + this.hitboxOffsetX, this.y + this.hitboxOffsetY, 16, 27);

        this.currentState = Constants.HERO_STATES.FALLING;

        this.idleAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.IDLE,4,10);
        this.idleAnimation.loadAnimation();
        this.runningAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.RUNNING,3,7);
        this.runningAnimation.loadAnimation();
        this.fallingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.FALLING,1,1);
        this.fallingAnimation.loadAnimation();
        this.jumpingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.JUMPING,6,10);
        this.jumpingAnimation.loadAnimation();
        this.attackingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.ATTACKING,6,10);
        this.attackingAnimation.loadAnimation();
        this.crounchingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.CROUCHING,1,1);


        this.damage = Constants.HERO_BASE_DAMAGE;
        this.health = Constants.HERO_BASE_HEALTH;

    }

    /**
     * @brief Restores the hero to a default state.
     *
     * Resets position, speed, hitbox, health bar, and health.
     */
    @Override
    public void restoreEntity() {
        this.x = 100;
        this.y = 420;
        this.speed = Constants.HERO_BASE_SPEED;
        this.hitbox.setX(this.x);
        this.hitbox.setY(this.y);
        this.resetHealthBarDefaultValues();
        this.setHealth(100);
    }

    /**
     * @brief Updates the hero's state and behavior.
     *
     * Handles behavior IDs, checks for falling, processes input (if alive),
     * applies gravity, handles movement and collisions, updates visual position,
     * updates animation state, and gets information about the current level.
     */
    @Override
    public void update() {
        System.out.println(this.isEngageReady);
        handleBehaviorIDs();

        if( Level.checkFalling(this.getHitbox(),this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect) ==1){
            this.deathAnimationTimer.start();
            this.isDying = true;
        }

        if(this.isDying){
            this.velocityX = 0;
            this.velocityY = 1;
        }
        else{
            if(this.health>0){
                handleInput();
            }
        }

        applyGravity();
        moveAndCollide(); // Updates hitbox position, handles collisions, sets isGrounded
        updateVisualPosition();
        updateAnimationState();
//        getInfoAboutCurrentLevel();

    }

    /**
     * @brief Draws the hero on the screen.
     *
     * Renders the hero's current animation. If the hero is dying,
     * it also draws a red overlay to indicate this state.
     * @param g The Graphics context to draw on.
     */
    @Override
    public void draw(Graphics g){

        this.getAnimationByState().paintAnimation(g,(int)this.x,(int)this.y,this.flipped,1);

        if(this.isDying){
            Graphics2D g2d = (Graphics2D) g;
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.1f));
            Color originalColor = g2d.getColor();
            g2d.setColor(Color.RED);
            g2d.fillRect((int)this.x-Constants.WINDOW_WIDTH/2,0,Constants.WINDOW_WIDTH*2,Constants.WINDOW_HEIGHT*2);
            g2d.setColor(originalColor);
            g2d.setComposite(originalComposite);
        }
    }

    /**
     * @brief Handles player input for movement and actions.
     *
     * Reads key presses for jumping, moving left/right, and grappling.
     * Updates hero's velocity and state based on input.
     */
    private void handleInput() {
        boolean jumpPressed = reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_SPACE);
        boolean rightPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_D];
        boolean leftPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_A];
        boolean grapplePressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_G];

        if(this.currentGrappleX!=0 && this.currentGrappleY!=0 && grapplePressed && !this.isGrapplingTimerExpired && this.hasWhip){
            this.isGrappling = true;
            this.grappleExpiredTimer.start();
        }
        if(!grapplePressed || (this.currentGrappleX==0 && this.currentGrappleY ==0 && this.hasWhip) ){
            this.isGrappling = false;
            this.isGrapplingTimerExpired = false;
            this.didJumpAfterGrapple = false;
        }
        if(this.isGrappling && this.isGrappleInterrupted && this.hasWhip){
            this.velocityY = this.jumpStrength*1.2f;
            this.isGrounded = false;
            this.jumpCap -= 4;
            this.didJumpAfterGrapple = true;
        }

        this.velocityX = 0;

        if(this.isGrappling && this.hasWhip){
            if(this.currentGrappleX*Constants.TILE_SIZE>this.getHitbox().getX()){
                this.velocityX = this.speed*2; // if the detected hook is in front of the hero, the velocity applied pulls the hero forward
            }
            else{
                this.velocityX = -this.speed*2; // the same but backwards
            }
            if(this.currentGrappleY*Constants.TILE_SIZE>this.getHitbox().getY()){
                this.velocityY = this.speed*2; // the same but for vertical velocity
            }
            else{
                this.velocityY = -this.speed*2;
            }
        }
        else{
            if (rightPressed && !leftPressed) { // here the regular velocity on the X axis is set
                this.velocityX = this.speed;
            } else if (leftPressed && !rightPressed) {
                this.velocityX = -this.speed;
            }


            if (jumpPressed && this.isGrounded && this.jumpCap > 0) { // here the jump starts.
                // The hero has to be grounded, needs enough stamina and the space key needs to be pressed
                jump();
            }
        }


    }

    /**
     * @brief Initiates a jump if the hero is grounded.
     *
     * Sets the vertical velocity for an upward jump and consumes jump capability.
     */
    private void jump() {
        if (this.isGrounded) { // jump starts only if grounded
            this.velocityY = this.jumpStrength;
            this.isGrounded = false;
            this.jumpCap -= 4;
        }
    }

    /**
     * @brief Handles hero movement and collision detection with the level.
     *
     * Updates the hero's hitbox position based on velocity, checks for collisions
     * with walls and ceilings, and handles ground detection and snapping.
     */
    @Override
    protected void moveAndCollide() {
        float originalX = this.hitbox.getX(); // the left side of the hitbox
        float deltaX = this.velocityX; // how much the hero should move on the X axis

        this.hitbox.setX(originalX + deltaX);

        boolean horizontalCollision = false;

        if(Level.checkCeilingCollision(hitbox,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect)){
            this.velocityY=1;
            // if the hero touches the ceiling, it is sent back down
        }

        if (this.velocityX > 0) { // right side collision
            if (Level.checkWallCollision(hitbox, true,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect)) {
                horizontalCollision = true;

                // this check is made in order to make sure the collision happens INSIDE the hitbox
                // the epsilon is used in order to make sure the inside is checked, not just the edge
                float checkCoordInsideRight = hitbox.getX() + hitbox.getWidth() - Constants.EPSILON;
                // this is just the coordinate, now the corresponding tile must be found

                int wallTileX = (int)Math.floor(checkCoordInsideRight / Constants.TILE_SIZE);
                // divided by tile size in order to find the index of the tile

                float wallLeftEdgeX = wallTileX * Constants.TILE_SIZE;
                // multiplied again in order to "snap" to a certain tile

                this.hitbox.setX(wallLeftEdgeX - hitbox.getWidth());
                // the hero is moved to the left of this edge

            }
        } else if (this.velocityX < 0) { // left side collision
            if (Level.checkWallCollision(hitbox, false,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect)) {
                horizontalCollision = true;

                float checkCoordOutsideLeft = hitbox.getX() - Constants.EPSILON;
                int wallTileX = (int)Math.floor(checkCoordOutsideLeft / Constants.TILE_SIZE);

                float wallRightEdgeX = (wallTileX + 1) * Constants.TILE_SIZE;

                this.hitbox.setX(wallRightEdgeX);

            }
        }

        if(this.velocityX<0){ // used for animations and drawing
            this.flipped=true; // a flip boolean is required because the hero in the sprite sheet faces right
            // so if the hero is facing left, the sprite must also be flipped
        }
        else if(velocityX>0){
            this.flipped=false;
        }

        if (horizontalCollision) { // if the character is colliding, the velocity is nullified
            this.velocityX = 0;
        }

        float originalY = this.hitbox.getY(); // now the check for the Y axis is performed
        float deltaY = this.velocityY; // the movement is first attempted
        this.hitbox.setY(originalY + deltaY);

        int fallCheckResult = Level.checkFalling(hitbox,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect);


        if (this.velocityY > 0) { // moving Down
            if (fallCheckResult == 0) { // if the ground is hit
                this.isGrounded = true;
                this.velocityY = 0;
                Level.snapToGround(this.hitbox);
                this.jumpCap = 10; // jump reset
            } else {
                this.isGrounded = false;
            }
        } else if (this.velocityY < 0) { // moving Up
            this.isGrounded = false;

        } else { // velocityY == 0
            if (fallCheckResult == 0) { // standing still on ground
                if (!this.isGrounded) { // sust landed precisely
                    Level.snapToGround(this.hitbox);
                    this.jumpCap = 10;
                }
                this.isGrounded = true;
                this.velocityY = 0;
            } else { // standing still in air
                this.isGrounded = false;
            }
        }

    }

    /**
     * @brief Updates the hero's visual position based on its hitbox and offsets.
     */
    @Override
    protected void updateVisualPosition() {
        this.x = this.hitbox.getX() - hitboxOffsetX;
        this.y = this.hitbox.getY() - hitboxOffsetY;
    }

    /**
     * @brief Retrieves the appropriate animation based on the hero's current state.
     * @return The Animation object corresponding to the current hero state.
     */
    @Override
    protected Animation getAnimationByState(){
        switch (this.currentState){
            case IDLE:
                return this.idleAnimation;
            case RUNNING:
                return this.runningAnimation;
            case ATTACKING:
                return this.attackingAnimation;
            case FALLING:
            case JUMPING:
                return this.jumpingAnimation;
            case CROUCHING:
        }


        return this.idleAnimation;
    }

    /**
     * @brief Updates the hero's animation state based on current actions and conditions.
     *
     * Transitions between states like idle, running, jumping, falling
     */
    @Override
    protected void updateAnimationState() {
        if (this.isGrounded) { // only apply running and idle when on ground
            if (this.velocityX != 0) {
                this.currentState = Constants.HERO_STATES.RUNNING;
            } else {
                this.currentState = Constants.HERO_STATES.IDLE;
            }
        } else { // in the air
            if (this.velocityY < 0) {
                this.currentState = Constants.HERO_STATES.JUMPING;
            } else { // velocityY > 0
                this.currentState = Constants.HERO_STATES.FALLING;
            }
        }
        // the function sets the state, and then the corresponding animation is updated here
        this.getAnimationByState().updateAnimation();
    }

    /**
     * @brief Checks if the hero is currently in the dying state.
     * @return True if dying, false otherwise.
     */
    public boolean getIsDying(){
        return this.isDying;
    }

    /**
     * @brief Gets the name of the hero.
     * @return The string "Player".
     */
    @Override
    public String getName(){
        return "Player";
    }

    /**
     * @brief Defines the hero's attack action.
     *
     * Currently, this method is empty and does not implement any attack logic.
     */
    @Override
    public void attack() {

    }

    /**
     * @brief Gets the hero's jump strength.
     * @return The jump strength value.
     */
    public float getJumpStrength(){
        return this.jumpStrength;
    }
    /**
     * @brief Sets the hero's jump strength.
     * @param jumpStrength The new jump strength value.
     */
    public void setJumpStrength(float jumpStrength){
        this.jumpStrength = jumpStrength;
    }

    /**
     * @brief Gets the source identifier for hero-related assets or data.
     * @return The string "GAME".
     */
    @Override
    public String getSource() {
        return "GAME";
    }

    /**
     * @brief Sets the current grapple point coordinates.
     * @param x The X-coordinate (tile index) of the grapple point.
     * @param y The Y-coordinate (tile index) of the grapple point.
     */
    public void setGrapplePoint(int x,int y){
        this.currentGrappleX = x;
        this.currentGrappleY = y;
    }


    /**
     * @brief Handles setting level-specific properties based on the current game state.
     *
     * Updates level dimensions, behavior IDs to respect, and potentially hero attributes
     * (like jump strength) based on the active level.
     */
    private void handleBehaviorIDs(){
        switch (State.getState().getStateName()){
            case Constants.LEVEL1_STATE:
                this.LEVEL_WIDTH = Constants.LEVEL1_WIDTH;
                this.LEVEL_HEIGHT = Constants.LEVEL1_HEIGHT;
                this.behaviorIDsToRespect = reflink.getGame().getLevel1().getBehaviorIDs();
                break;
            case Constants.LEVEL2_STATE:
                this.LEVEL_WIDTH = Constants.LEVEL2_WIDTH;
                this.LEVEL_HEIGHT = Constants.LEVEL2_HEIGHT;
                this.behaviorIDsToRespect = reflink.getGame().getLevel2().getBehaviorIDs();
                break;
            case Constants.LEVEL3_STATE:
                this.LEVEL_WIDTH = Constants.LEVEL3_WIDTH;
                this.LEVEL_HEIGHT = Constants.LEVEL3_HEIGHT;
                this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
                if(this.x > 3500){ // increased jump in order to make the final grapple platforming easier
                    this.jumpStrength= Constants.HERO_BASE_JUMP_STRENGTH*1.2f;
                }
                else{
                    this.jumpStrength = Constants.HERO_BASE_JUMP_STRENGTH;
                }
        }

    }


    /**
     * @brief Loads the hero's state from the data management system.
     *
     * Retrieves health, position, whip status, escapes, collected saves,
     * completed levels, and gold. Uses an access flag and a signal to prevent redundant loading.
     * @param access Boolean flag indicating if access to load data is permitted.
     */
    public void loadHeroState(boolean access){
        if(this.reflink.getHeroRefreshDoneSignal()){ // acts like a lock on the loading process. it only happens if the signal is set on false
            return;
        }

        try{
            this.health = this.reflink.getDataProxy().load(Constants.HERO_HEALTH,access);
            this.x = this.reflink.getDataProxy().load(Constants.HERO_X,access);
            this.getHitbox().setX(this.x);
            this.y = this.reflink.getDataProxy().load(Constants.HERO_Y,access);
            this.getHitbox().setY(this.y);
            this.hasWhip = this.reflink.getDataProxy().load(Constants.HERO_HAS_WHIP, access) == 1;
            this.nrOfEscapes = this.reflink.getDataProxy().load(Constants.HERO_NR_OF_FLEES,access);
            this.nrOfCollectedSaves = this.reflink.getDataProxy().load(Constants.HERO_NR_OF_COLLECTED_SAVES,access);
            this.nrOfCompletedLevels = this.reflink.getDataProxy().load(Constants.HERO_NR_OF_FINISHED_LEVELS,access);
            this.gold = this.reflink.getDataProxy().load(Constants.HERO_GOLD,access);
            this.reflink.setHeroRefreshDoneSignal(true);
        } catch (AccessNotPermittedException | ValueStoreException | DataBufferNotReadyException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @brief Stores the hero's current state to the data management system.
     *
     * Saves health, position, whip status, escapes, collected saves,
     * completed levels, and gold. Uses an access flag and a signal to prevent redundant storing.
     * @param access Boolean flag indicating if access to store data is permitted.
     */
    public void storeHeroState(boolean access){
        if(this.reflink.getHeroStoreDoneSignal()){
            return;
        }

        try{
            this.reflink.getDataProxy().store(Constants.HERO_HEALTH,(int)this.health,access);
            this.reflink.getDataProxy().store(Constants.HERO_X,(int)this.getHitbox().getX(),access);
            this.reflink.getDataProxy().store(Constants.HERO_Y,(int)this.getHitbox().getY(), access);
            int storeWhip = this.hasWhip ? 1:0;
            this.reflink.getDataProxy().store(Constants.HERO_HAS_WHIP,storeWhip, access);
            this.reflink.getDataProxy().store(Constants.HERO_NR_OF_FLEES,this.nrOfEscapes,access);
            this.reflink.getDataProxy().store(Constants.HERO_NR_OF_COLLECTED_SAVES,this.nrOfCollectedSaves,access);
            this.reflink.getDataProxy().store(Constants.HERO_NR_OF_FINISHED_LEVELS,this.nrOfCompletedLevels,access);
            this.reflink.getDataProxy().store(Constants.HERO_GOLD,this.gold,access);
            this.reflink.setHeroStoreDoneSignal(true);
        } catch (AccessNotPermittedException | ValueStoreException e) {
            System.err.println(e.getMessage());
        }



    }

    /**
     * @brief Gets the hero's current score.
     * @return The score value.
     */
    public int getScore(){return this.score;}
    /**
     * @brief Sets the hero's score.
     * @param score The new score value.
     */
    public void setScore(int score){this.score = score;}

    /**
     * @brief Gets the number of collected save items.
     * @return The number of collected saves.
     */
    public int getNrOfCollectedSaves() { return this.nrOfCollectedSaves;}
    /**
     * @brief Sets the number of collected save items.
     * @param nr The new number of collected saves.
     */
    public void setNrOfCollectedSaves(int nr) { this.nrOfCollectedSaves = nr;}

    /**
     * @brief Gets the number of completed levels.
     * @return The number of completed levels.
     */
    public int getNrOfCompletedLevels() { return this.nrOfCompletedLevels;}
    /**
     * @brief Sets the number of completed levels.
     * @param nrOfCompletedLevels The new number of completed levels.
     */
    public void setNrOfCompletedLevels(int nrOfCompletedLevels) { this.nrOfCompletedLevels = nrOfCompletedLevels;}

    /**
     * @brief Gets the amount of gold the hero has.
     * @return The amount of gold.
     */
    public int getGold(){return this.gold;}
    /**
     * @brief Sets the amount of gold the hero has.
     * @param gold The new amount of gold.
     */
    public void setGold(int gold){this.gold = gold;}

    /**
     * @brief Checks if the hero's sprite is flipped.
     * @return True if flipped, false otherwise.
     */
    public boolean getFlipped() {return this.flipped;}
    /**
     * @brief Sets the grapple interrupt flag.
     * @param isInterrupted True if grapple is interrupted, false otherwise.
     */
    public void setGrappleInterrupted(boolean isInterrupted) { this .isGrappleInterrupted = isInterrupted;}

    /**
     * @brief Gets the X-coordinate of the current grapple point.
     * @return The current grapple X-coordinate.
     */
    public int getCurrentGrappleX() {return this.currentGrappleX;}
    /**
     * @brief Gets the Y-coordinate of the current grapple point.
     * @return The current grapple Y-coordinate.
     */
    public int getCurrentGrappleY() {return this.currentGrappleY;}

    /**
     * @brief Checks if the hero has the whip.
     * @return True if the hero has the whip, false otherwise.
     */
    public boolean getHasWhip(){return this.hasWhip;}
    /**
     * @brief Sets whether the hero has the whip.
     * @param hasWhip True if the hero has the whip, false otherwise.
     */
    public void setHasWhip(boolean hasWhip){this.hasWhip = hasWhip;}

    /**
     * @brief Gets the current number of escapes the hero has.
     * @return The number of escapes.
     */
    public int getNrOfEscapes(){return this.nrOfEscapes;}
    /**
     * @brief Gets the maximum number of escapes the hero can have.
     * @return The maximum number of escapes.
     */
    public int getMaxNrOfEscapes(){return this.maxNrOfEscapes;}
    /**
     * @brief Sets the number of escapes the hero has.
     *
     * Ensures the number of escapes stays within the valid range [0, maxNrOfEscapes].
     * @param nrOfEscapes The new number of escapes.
     */
    public void setNrOfEscapes(int nrOfEscapes) { this.nrOfEscapes = nrOfEscapes; if(this.nrOfEscapes>this.getMaxNrOfEscapes()){this.nrOfEscapes = 3; } else if(this.nrOfEscapes<0){this.nrOfEscapes = 0;}} // Assumes max is 3 if it goes over.

    /**
     * @brief Checks if the hero can currently engage in interactions.
     * @return True if can engage, false otherwise.
     */
    public boolean getEngageReady(){return this.isEngageReady;}
    /**
     * @brief Sets whether the hero can engage in interactions.
     * @param engageReady True if can engage, false otherwise.
     */
    public void setEngageReady(boolean engageReady) { this.isEngageReady = engageReady;}
}