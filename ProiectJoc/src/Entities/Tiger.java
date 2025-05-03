package Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.EnemyAnimations.TigerActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

public class Tiger extends Entity {
    private Constants.ENEMY_STATES currentState;

    private Animation walkingAnimation;
    private Animation inFightIdleAnimation;
    private Animation inFightAttackingAnimation;

    private int directionSwitchCounter = 5;

    public Tiger(RefLinks reflink, int startX, int startY){
        super(reflink,startX,startY);
        this.speed = -0.4f;
        this.hitbox = new Hitbox(this.x,this.y,(int)( (64.0/100.0)*50.0 ),(int)( (32.0/100)*50.0 ));
        this.currentState = Constants.ENEMY_STATES.FALLING;

        this.walkingAnimation = new TigerActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,4,10);
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new TigerActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,4,10);
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new TigerActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,1,10);
        this.inFightIdleAnimation.loadAnimation();

    }

    @Override
    public void Update(){
        applyGravity();
        moveAndCollide();
        updateVisualPosition();
        updateAnimationState();
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
        if(this.isGrounded){
            if(this.isEngaged){
                this.currentState = Constants.ENEMY_STATES.IN_FIGHT_IDLE;
            }
            else{
                this.currentState = Constants.ENEMY_STATES.WALKING;
            }

        }
        else{
            this.currentState = Constants.ENEMY_STATES.FALLING;
        }

        this.getAnimationByState().updateAnimation();
    }

    @Override
    protected void moveAndCollide() {
        this.velocityX = this.speed;
        float originalX = this.hitbox.getX(); //partea stanga a hitbox-ului
        float deltaX = this.velocityX; //cat ar trebui sa se deplaseze

        boolean changingDirection = !this.reflink.getGame().getLevel1State().getLevel1().isGroundAhead(this.hitbox,!this.flipped,Constants.LEVEL1_WIDTH,Constants.LEVEL1_HEIGHT);
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

        int fallCheckResult = reflink.getGame().getLevel1State().getLevel1().checkFalling(hitbox,Constants.LEVEL1_WIDTH,Constants.LEVEL1_HEIGHT);


        if (this.velocityY > 0) { // Moving Down
            if (fallCheckResult == 0) { // Hit ground
                this.isGrounded = true;
                this.velocityY = 0;
                reflink.getGame().getLevel1State().getLevel1().snapToGround(this.hitbox);
            } else {
                this.isGrounded = false;
            }
        } else if (this.velocityY < 0) { // Moving Up
            this.isGrounded = false;

        } else { // velocityY == 0
            if (fallCheckResult == 0) { // Standing still on ground
                if (!this.isGrounded) { // Just landed precisely
                    reflink.getGame().getLevel1State().getLevel1().snapToGround(this.hitbox);
                }
                this.isGrounded = true;
                this.velocityY = 0; // Ensure velocity is 0 when grounded
            } else { // Standing still in air
                this.isGrounded = false;
            }
        }
    }





}
