package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.Maps.Level;
import PaooGame.RefLinks;
import PaooGame.States.State;

public class BasicSkeleton extends Entity{

    private Constants.ENEMY_STATES currentState;

    private Animation walkingAnimation;
    private Animation inFightIdleAnimation;
    private Animation inFightAttackingAnimation;

    private int directionSwitchCounter = 5;

    public BasicSkeleton(RefLinks reflink, int startX, int startY){
        super(reflink,startX,startY);
        this.speed = -0.4f;
        this.hitbox = new Hitbox(this.x,this.y,32,32);
        this.currentState = Constants.ENEMY_STATES.FALLING;

        this.setHealthBarColor1(Constants.YELLOW_HEALTH_BAR_COLOR_1);
        this.setHealthBarColor2(Constants.YELLOW_HEALTH_BAR_COLOR_2);

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,13,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,18,5,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,11,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.BASIC_SKELETON_DAMAGE;
        this.health = Constants.BASIC_SKELETON_HEALTH;

        this.behaviorIDsToRespect = reflink.getGame().getLevel2().getBehaviorIDs();


    }

    @Override
    public void restoreEntity() {
        this.health = 100.0;
        this.speed = Constants.BASIC_SKELETON_SPEED;
        this.hitbox.setX(this.x);
        this.hitbox.setY(this.y);
        this.hitbox.setWidth(32);
        this.hitbox.setHeight(32);
        this.currentState = Constants.ENEMY_STATES.FALLING;
        this.isEngaged = false;
    }

    @Override
    public void update(){
        if(this.currentState == Constants.ENEMY_STATES.FALLING || this.currentState == Constants.ENEMY_STATES.WALKING){
            this.hitbox.setWidth(32);
            this.hitbox.setHeight(32);
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

        boolean changingDirection = !Level.isGroundAhead(this.hitbox,!this.flipped,Constants.LEVEL2_WIDTH,Constants.LEVEL2_HEIGHT,this.behaviorIDsToRespect);
        if(directionSwitchCounter == 5){
            if(changingDirection){
//            this.flipped = !this.flipped;
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

        int fallCheckResult = Level.checkFalling(hitbox,Constants.LEVEL2_WIDTH,Constants.LEVEL2_HEIGHT,this.behaviorIDsToRespect);


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
    public void attack(){
        this.currentState = Constants.ENEMY_STATES.IN_FIGHT_ATTACKING;
        this.getAnimationByState().triggerOnce();
    }

    @Override
    public String getSource() {
        return "LEVEL_2";
    }

    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}

    @Override
    public String getName(){
        return "BasicSkeleton";
    }
}
