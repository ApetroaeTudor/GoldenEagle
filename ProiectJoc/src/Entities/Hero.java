package Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.PlayerAnimations.PlayerActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.HUD.HealthBar;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;
import PaooGame.States.State;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Hero extends Entity {
    private float jumpStrength;



    private int jumpCap;

    private Constants.HERO_STATES currentState;


    private float hitboxOffsetX = 16;
    private float hitboxOffsetY = 9;

    private int LEVEL_WIDTH;
    private int LEVEL_HEIGHT;


    private Animation idleAnimation;
    private Animation fallingAnimation;
    private Animation runningAnimation;
    private Animation jumpingAnimation;
    private Animation attackingAnimation;
    private Animation crounchingAnimation;

    public Hero(RefLinks refLink, int startX, int startY) {
        super(refLink,startX,startY);

        this.damage = 30;




        this.jumpStrength = -3.5f;//-3.2f;   // Initial upward velocity (negative Y). Adjust! Needs to be > gravity per frame initially.

        this.speed = 2;             // Base horizontal speed
        this.jumpCap = 10;          // Stamina/resource for jumps


        this.hitbox = new Hitbox(this.x + this.hitboxOffsetX, this.y + this.hitboxOffsetY, 16, 27);

        this.currentState = Constants.HERO_STATES.FALLING;

        this.idleAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.IDLE,4,10);
        this.idleAnimation.loadAnimation();
        this.runningAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.RUNNING,3,10);
        this.runningAnimation.loadAnimation();
        this.fallingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.FALLING,1,1);
        this.fallingAnimation.loadAnimation();
        this.jumpingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.JUMPING,6,10);
        this.jumpingAnimation.loadAnimation();
        this.attackingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.ATTACKING,6,10);
        this.attackingAnimation.loadAnimation();
        this.crounchingAnimation=new PlayerActionAnimation(this.reflink,Constants.HERO_STATES.CROUCHING,1,1);

    }

    @Override
    public void Update() {
        handleInput(); // Sets velocityX, checks for jump press
        applyGravity();
        moveAndCollide(); // Updates hitbox position, handles collisions, sets isGrounded
        updateVisualPosition();
        updateAnimationState();

        getContext();


    }

    private void handleInput() {
        boolean jumpPressed = reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_SPACE);
        boolean rightPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_D];
        boolean leftPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_A];
        boolean crouchPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_C];

        this.velocityX = 0;
        if (rightPressed && !leftPressed) {
            this.velocityX = this.speed;
        } else if (leftPressed && !rightPressed) {
            this.velocityX = -this.speed;
        }
        if (jumpPressed && this.isGrounded && this.jumpCap > 0) {
            jump();
        }
        if (crouchPressed && isGrounded) {
            // Placeholder: Set state if needed, but full implementation is complex
        }
    }


    private void jump() {
        if (this.isGrounded) { //incep jump o singura data doar daca sunt grounded
            this.velocityY = this.jumpStrength;
            this.isGrounded = false;
            this.jumpCap -= 4;
        }
    }

    @Override
    protected void moveAndCollide() {
        float originalX = this.hitbox.getX(); //partea stanga a hitbox-ului
        float deltaX = this.velocityX; //cat ar trebui sa se deplaseze


        //presupun ca miscarea e valida si actualizez hitbox-ul
        this.hitbox.setX(originalX + deltaX); //incercare de movement

        //verific daca miscarea chiar e valida
        boolean horizontalCollision = false;
        //pentru pereti


        if(this.reflink.getGame().getLevel1State().getLevel1().checkCeilingCollision(hitbox,this.LEVEL_WIDTH,this.LEVEL_HEIGHT)){
            this.velocityY=1;
        }



        if (this.velocityX > 0) { //coliziune perete dreapta
            if (reflink.getGame().getLevel1State().getLevel1().checkWallCollision(hitbox, true,this.LEVEL_WIDTH,this.LEVEL_HEIGHT)) {
                horizontalCollision = true;

                //trebuie sa verific daca aceasta coliziune se intampla IN hitbox sau nu
                //cu acel epsilon mic vad daca sunt inauntru sau doar la margine
                float checkCoordInsideRight = hitbox.getX() + hitbox.getWidth() - Constants.EPSILON;
                //asta e doar coordonata, acum trebuie calculat tile-ul corespunzator


                int wallTileX = (int)Math.floor(checkCoordInsideRight / Constants.TILE_SIZE);
                //index de coloana pentru coliziune

                float wallLeftEdgeX = wallTileX * Constants.TILE_SIZE;
                //coordonata x pentru coloana respectiva

                this.hitbox.setX(wallLeftEdgeX - hitbox.getWidth());

            }
        } else if (this.velocityX < 0) { // Moving Left
            if (reflink.getGame().getLevel1State().getLevel1().checkWallCollision(hitbox, false,this.LEVEL_WIDTH,this.LEVEL_HEIGHT)) {
                horizontalCollision = true;

                float checkCoordOutsideLeft = hitbox.getX() - Constants.EPSILON;
                int wallTileX = (int)Math.floor(checkCoordOutsideLeft / Constants.TILE_SIZE);

                float wallRightEdgeX = (wallTileX + 1) * Constants.TILE_SIZE;

                this.hitbox.setX(wallRightEdgeX);

            }
        }

        if(this.velocityX<0){
            this.flipped=true;
        }
        else if(velocityX>0){
            this.flipped=false;
        }

        if (horizontalCollision) {
            this.velocityX = 0;
        }

        float originalY = this.hitbox.getY();
        float deltaY = this.velocityY;
        this.hitbox.setY(originalY + deltaY);

        int fallCheckResult = reflink.getGame().getLevel1State().getLevel1().checkFalling(hitbox,this.LEVEL_WIDTH,this.LEVEL_HEIGHT);



        if (this.velocityY > 0) { // Moving Down
            if (fallCheckResult == 0) { // Hit ground
                this.isGrounded = true;
                this.velocityY = 0;
                reflink.getGame().getLevel1State().getLevel1().snapToGround(this.hitbox);
                this.jumpCap = 10; //resetare jump
            } else {
                this.isGrounded = false;
            }
        } else if (this.velocityY < 0) { // Moving Up
            this.isGrounded = false;

        } else { // velocityY == 0
            if (fallCheckResult == 0) { // Standing still on ground
                if (!this.isGrounded) { // Just landed precisely
                    reflink.getGame().getLevel1State().getLevel1().snapToGround(this.hitbox);
                    //functia tine de clasa parinte abstracta Level deci nu conteaza ca depinde de Level1, va merge si pt Level2,3
                    this.jumpCap = 10;
                }
                this.isGrounded = true;
                this.velocityY = 0; // Ensure velocity is 0 when grounded
            } else { // Standing still in air
                this.isGrounded = false;
            }
        }
    }

    @Override
    protected void updateVisualPosition() {
        this.x = this.hitbox.getX() - hitboxOffsetX;
        this.y = this.hitbox.getY() - hitboxOffsetY;
    }

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
                if(reflink.getKeyManager().isKeyPressed(KeyEvent.VK_SPACE)){
                    return this.jumpingAnimation;

                }
                else{
                    return this.fallingAnimation;
                }
            case JUMPING:
                return this.jumpingAnimation;
            case CROUCHING:
                return this.crounchingAnimation;
        }

        return this.idleAnimation;
    }

    @Override
    protected void updateAnimationState() {

        boolean rightPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_D];
        boolean leftPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_A];
        boolean crouchPressed = reflink.getKeyManager().getKeyState()[KeyEvent.VK_C];

        if (this.isGrounded) {
            if (crouchPressed) {
                this.currentState = Constants.HERO_STATES.CROUCHING;
            } else if (this.velocityX != 0) { // Use actual velocity, not just key press
                this.currentState = Constants.HERO_STATES.RUNNING;
            } else {
                this.currentState = Constants.HERO_STATES.IDLE;
            }
        } else { // In the air
            if (this.velocityY < 0) {
                this.currentState = Constants.HERO_STATES.JUMPING;
            } else { // velocityY >= 0 (falling or at peak)
                this.currentState = Constants.HERO_STATES.FALLING;
            }
        }


        this.getAnimationByState().updateAnimation();

    }


    private void getContext(){
        switch (State.GetState().getStateName()){
            case Constants.LEVEL1_STATE:
                this.LEVEL_HEIGHT = Constants.LEVEL1_HEIGHT;
                this.LEVEL_WIDTH = Constants.LEVEL1_WIDTH;
                break;
            default:
                this.LEVEL_HEIGHT = Constants.LEVEL1_HEIGHT;
                this.LEVEL_WIDTH = Constants.LEVEL1_WIDTH; //placeholder, se va modifica la introducerea niv2 si niv3
        }
    }


    @Override
    public String getName(){
        return "Player";
    }

//    @Override
//    public void Draw(Graphics g){
//        super.Draw(g);
//        super.DrawHealthBar(g);
//    }

}