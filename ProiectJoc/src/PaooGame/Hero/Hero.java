package PaooGame.Hero;

import PaooGame.Animations.Animation;
import PaooGame.Animations.PlayerAnimations.PlayerActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Hero {

    private float x;
    private float y;

    private float gravity; // Gravity should accelerate
    private float maxFallSpeed;
    private float jumpStrength;

    private int speed;
    private double health;

    private int jumpCap;
    private boolean isGrounded;
    private boolean headingLeft;

    private Hitbox hitbox;

    private Constants.HERO_STATES currentState;
    private RefLinks reflink;

    private float velocityX;
    private float velocityY;

    private float hitboxOffsetX = 16;
    private float hitboxOffsetY = 9;


    private Animation idleAnimation;
    private Animation fallingAnimation;
    private Animation runningAnimation;
    private Animation jumpingAnimation;
    private Animation attackingAnimation;
    private Animation crounchingAnimation;

    public Hero(RefLinks refLink, int startX, int startY) {
        this.reflink = refLink;

        this.gravity = 0.15f;        // Acceleration due to gravity (pixels/frame^2). Adjust!
        this.maxFallSpeed = 6.0f;    // Max downward speed (pixels/frame). Adjust!
        this.jumpStrength = -3.2f;   // Initial upward velocity (negative Y). Adjust! Needs to be > gravity per frame initially.

        this.speed = 2;             // Base horizontal speed
        this.health = 100;
        this.jumpCap = 10;          // Stamina/resource for jumps

        this.x = startX;
        this.y = startY;

        this.hitbox = new Hitbox(this.x + this.hitboxOffsetX, this.y + this.hitboxOffsetY, 16, 27);

        this.isGrounded = false;    // Assume starting in the air initially
        this.currentState = Constants.HERO_STATES.FALLING;
        this.velocityX = 0f;
        this.velocityY = 0f;        // Start with no initial vertical velocity
        this.headingLeft=false;


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

    public void Update() {
        handleInput(); // Sets velocityX, checks for jump press
        applyGravity();
        moveAndCollide(); // Updates hitbox position, handles collisions, sets isGrounded
        updateVisualPosition();
        updateAnimationState();

        System.out.println(this.headingLeft);


    }

    private void handleInput() {
        boolean jumpPressed = reflink.GetKeyManager().isKeyPressedOnce(KeyEvent.VK_SPACE);
        boolean rightPressed = reflink.GetKeyManager().getKeyState()[KeyEvent.VK_D];
        boolean leftPressed = reflink.GetKeyManager().getKeyState()[KeyEvent.VK_A];
        boolean crouchPressed = reflink.GetKeyManager().getKeyState()[KeyEvent.VK_C];

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


    private void applyGravity() {
        if (!this.isGrounded) {
            this.velocityY += this.gravity; //velocitatea accelereaza cu cat cad mai mult
            if (this.velocityY > this.maxFallSpeed) { //impiedic caderea exagerata
                this.velocityY = this.maxFallSpeed;
            }
        }
    }


    private void jump() {
        if (this.isGrounded) { //incep jump o singura data doar daca sunt grounded
            this.velocityY = this.jumpStrength;
            this.isGrounded = false;
            this.jumpCap -= 4;
        }
    }

    private void moveAndCollide() {
        float originalX = this.hitbox.getX(); //partea stanga a hitbox-ului
        float deltaX = this.velocityX; //cat ar trebui sa se deplaseze


        //presupun ca miscarea e valida si actualizez hitbox-ul
        this.hitbox.setX(originalX + deltaX); //incercare de movement

        //verific daca miscarea chiar e valida
        boolean horizontalCollision = false;
        //pentru pereti


        if(this.reflink.GetGame().GetLevel1State().getLevel1().checkCeilingCollision(hitbox)){
            this.velocityY=1;
        }



        if (this.velocityX > 0) { //coliziune perete dreapta
            if (reflink.GetGame().GetLevel1State().getLevel1().checkWallCollision(hitbox, true)) {
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
            if (reflink.GetGame().GetLevel1State().getLevel1().checkWallCollision(hitbox, false)) {
                horizontalCollision = true;

                float checkCoordOutsideLeft = hitbox.getX() - Constants.EPSILON;
                int wallTileX = (int)Math.floor(checkCoordOutsideLeft / Constants.TILE_SIZE);

                float wallRightEdgeX = (wallTileX + 1) * Constants.TILE_SIZE;

                this.hitbox.setX(wallRightEdgeX);

            }
        }

        if(this.velocityX<0){
            this.headingLeft=true;
        }
        else if(velocityX>0){
            this.headingLeft=false;
        }

        if (horizontalCollision) {
            this.velocityX = 0;
        }

        float originalY = this.hitbox.getY();
        float deltaY = this.velocityY;
        this.hitbox.setY(originalY + deltaY);

        int fallCheckResult = reflink.GetGame().GetLevel1State().getLevel1().checkFalling(hitbox);



        if (this.velocityY > 0) { // Moving Down
            if (fallCheckResult == 0) { // Hit ground
                this.isGrounded = true;
                this.velocityY = 0;
                reflink.GetGame().GetLevel1State().getLevel1().snapToGround(this.hitbox);
                this.jumpCap = 10; //resetare jump
            } else {
                this.isGrounded = false;
            }
        } else if (this.velocityY < 0) { // Moving Up
            this.isGrounded = false;

        } else { // velocityY == 0
            if (fallCheckResult == 0) { // Standing still on ground
                if (!this.isGrounded) { // Just landed precisely
                    reflink.GetGame().GetLevel1State().getLevel1().snapToGround(this.hitbox);
                    this.jumpCap = 10;
                }
                this.isGrounded = true;
                this.velocityY = 0; // Ensure velocity is 0 when grounded
            } else { // Standing still in air
                this.isGrounded = false;
            }
        }
    }


    private void updateVisualPosition() {
        this.x = this.hitbox.getX() - hitboxOffsetX;
        this.y = this.hitbox.getY() - hitboxOffsetY;
    }


    private Animation getAnimationByState(){
        switch (this.currentState){
            case IDLE:
                return this.idleAnimation;
            case RUNNING:
                return this.runningAnimation;
            case ATTACKING:
                return this.attackingAnimation;
            case FALLING:
                if(reflink.GetKeyManager().isKeyPressed(KeyEvent.VK_SPACE)){
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


    private void updateAnimationState() {

        boolean rightPressed = reflink.GetKeyManager().getKeyState()[KeyEvent.VK_D];
        boolean leftPressed = reflink.GetKeyManager().getKeyState()[KeyEvent.VK_A];
        boolean crouchPressed = reflink.GetKeyManager().getKeyState()[KeyEvent.VK_C];

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


        // Note: ATTACKING state would need separate logic based on attack input/cooldowns
    }



    public void Draw(Graphics g) {
//        BufferedImage imgToDraw = this.reflink.getTileCache().getHeroState(this.currentState);
//        if (imgToDraw != null && imgToDraw.getWidth() >= 48 && imgToDraw.getHeight() >= 48) {
//            imgToDraw = imgToDraw.getSubimage(0, 0, 48, 48);
//            g.drawImage(imgToDraw, (int) this.x, (int) this.y, 48, 48, null);
//        } else {
//            g.setColor(Color.MAGENTA);
//            g.fillRect((int) this.x, (int) this.y, 48, 48);
//            g.setColor(Color.BLACK);
//            g.drawString("?", (int) this.x + 20, (int) this.y + 30);
//        }

//        this.idleAnimation.paintAnimation(g,(int)this.x,(int)this.y);
        this.getAnimationByState().paintAnimation(g,(int)this.x,(int)this.y,this.headingLeft);

        this.hitbox.printHitbox(g);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return hitbox.getWidth();
    }

    public float getHeight() {
        return hitbox.getHeight();
    }

    // Hero.java
    public double getHealth() {
        return health;
    }

}