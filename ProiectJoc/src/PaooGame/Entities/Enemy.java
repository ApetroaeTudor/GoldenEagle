package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.Maps.Level;
import PaooGame.RefLinks;
import PaooGame.States.State;
import PaooGame.Strategies.EnemyStrategies.EnemyStrategy;

public class Enemy extends Entity {
    private Constants.ENEMY_STATES currentState;

    private Animation walkingAnimation;
    private Animation inFightIdleAnimation;
    private Animation inFightAttackingAnimation;

    private EnemyStrategy enemyStrategy;

    private int directionSwitchCounter = 5;

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



    @Override
    public void restoreEntity() {

    }

    @Override
    public void update(){
        if(this.currentState == Constants.ENEMY_STATES.FALLING || this.currentState == Constants.ENEMY_STATES.WALKING){
            this.hitbox.setWidth(this.enemyStrategy.getHitboxWidth()); //
            this.hitbox.setHeight(this.enemyStrategy.getHitboxHeight()); //
            applyGravity();
            moveAndCollide();
            updateVisualPosition();
        }
        updateAnimationState();
        this.getAnimationByState().updateAnimation();
    }

    @Override
    protected void updateVisualPosition() {
        this.x = this.hitbox.getX();
        this.y = this.hitbox.getY();
    }

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

    @Override
    protected void updateAnimationState() {
        if (this.currentState == Constants.ENEMY_STATES.IN_FIGHT_ATTACKING) {
            if (this.inFightAttackingAnimation.getIsFinished()) {
                this.currentState = Constants.ENEMY_STATES.IN_FIGHT_IDLE;
            } else {
                return;
            }
        }

        boolean isInFightState = State.getState() != null && State.getState().getStateName() == Constants.FIGHT_STATE;
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

    @Override
    protected void moveAndCollide() {
        this.velocityX = this.speed;
        float originalX = this.hitbox.getX(); //partea stanga a hitbox-ului
        float deltaX = this.velocityX; //cat ar trebui sa se deplaseze

        boolean changingDirection = !Level.isGroundAhead(this.hitbox,!this.flipped,this.enemyStrategy.getLevelWidthInTiles(),this.enemyStrategy.getLevelHeightInTiles(),this.behaviorIDsToRespect); //
        if(directionSwitchCounter == 5){
            if(changingDirection){
                speed=-speed;
                this.directionSwitchCounter=0;
            }
        }
        else{
            this.directionSwitchCounter++;
        }


        //presupun ca miscarea e valida si actualizez hitbox-ul
        this.hitbox.setX(originalX + deltaX); //incercare de movement


        if(this.velocityX<0){
            this.flipped=false;
        }
        else if(velocityX>0){
            this.flipped=true;
        }





        float originalY = this.hitbox.getY();
        float deltaY = this.velocityY;
        this.hitbox.setY(originalY + deltaY);

        int fallCheckResult = Level.checkFalling(hitbox,this.enemyStrategy.getLevelWidthInTiles(),this.enemyStrategy.getLevelHeightInTiles(),this.behaviorIDsToRespect); //


        if (this.velocityY > 0) { // Moving Down
            if (fallCheckResult == 0) { // Hit ground
                this.isGrounded = true;
                this.velocityY = 0;
                Level.snapToGround(this.hitbox);
            } else {
                this.isGrounded = false;
            }
        } else if (this.velocityY < 0) { // Moving Up
            this.isGrounded = false;

        } else { // velocityY == 0
            if (fallCheckResult == 0) { // Standing still on ground
                if (!this.isGrounded) { // Just landed precisely
                    Level.snapToGround(this.hitbox);
                }
                this.isGrounded = true;
                this.velocityY = 0; // Ensure velocity is 0 when grounded
            } else { // Standing still in air
                this.isGrounded = false;
            }
        }
    }

    public void setCurrentState(Constants.ENEMY_STATES state){
        this.currentState = state;
    }

    @Override
    public String getName(){
        return this.enemyStrategy.getName();
    } //



    @Override
    public void attack(){
        this.currentState = Constants.ENEMY_STATES.IN_FIGHT_ATTACKING;
        this.getAnimationByState().triggerOnce();
    }

    @Override
    public String getSource() {
        return this.enemyStrategy.getSource();
    } //

    public void setX(int x){this.x = x; this.getHitbox().setX(this.x);}
    public void setY(int y){this.y = y; this.getHitbox().setY(this.y);}

    public EnemyStrategy getEnemyStrategy() {
        return enemyStrategy;
    }
}
