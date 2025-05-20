package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.PlayerAnimations.PlayerActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.Maps.Level;
import PaooGame.RefLinks;
import PaooGame.States.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.AccessDeniedException;

public class Hero extends Entity {
    private float jumpStrength;

    private Timer deathAnimationTimer;
    private int timeoutInMillisForDeathAnimation = 600;
    private boolean isDying = false;
    private int currentGrappleX = 0;
    private int currentGrappleY = 0;
    private boolean isGrappling = false;

    private boolean hasWhip = false;

    private Timer grappleExpiredTimer;
    private int grappleTimeoutMillis = 120;
    private boolean isGrapplingTimerExpired = false;
    private boolean grappleInterrupt = false;
    private boolean didJumpAfterGrapple = false;
    private boolean canEngage = true;

    private int nrOfEscapes = 2;
    private int maxNrOfEscapes = 2;
    private int nrOfCompletedLevels = 0;

    private int nrOfCollectedSaves = 0;

    private int score = 0;





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


        this.jumpStrength = Constants.HERO_BASE_JUMP_STRENGTH;//-3.2f;   // Initial upward velocity (negative Y). Adjust! Needs to be > gravity per frame initially.

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

    @Override
    public void update() {







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
                if(this.x > 3500){
                    this.jumpStrength= Constants.HERO_BASE_JUMP_STRENGTH*1.2f;
                }
                else{
                    this.jumpStrength = Constants.HERO_BASE_JUMP_STRENGTH;
                }
        }

        if( Level.checkFalling(this.getHitbox(),this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect) ==1){
            this.deathAnimationTimer.start();
            this.isDying = true;
        }

        if(this.isDying){
//            this.gravity = Constants.DYING_ENTITY_GRAVITY;
//            this.maxFallSpeed = Constants.DYING_MAX_ENTITY_FALL_SPEED;
            this.velocityX = 0;
            this.velocityY = 1;
        }
        else{
            if(this.health>0){
//                this.gravity = Constants.BASE_ENTITY_GRAVITY;
//                this.maxFallSpeed = Constants.BASE_MAX_ENTITY_FALL_SPEED;
                handleInput();

            }

        }
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
        if(this.isGrappling && this.grappleInterrupt && this.hasWhip){
            this.velocityY = this.jumpStrength*1.2f;
            this.isGrounded = false;
            this.jumpCap -= 4;
            this.didJumpAfterGrapple = true;
        }

        this.velocityX = 0;

        if(this.isGrappling && this.hasWhip){
            if(this.currentGrappleX*Constants.TILE_SIZE>this.getHitbox().getX()){
                this.velocityX = this.speed*2;
            }
            else{
                this.velocityX = -this.speed*2;
            }
            if(this.currentGrappleY*Constants.TILE_SIZE>this.getHitbox().getY()){
                this.velocityY = this.speed*2;
            }
            else{
                this.velocityY = -this.speed*2;
            }
        }
        else{
            if (rightPressed && !leftPressed) {
                this.velocityX = this.speed;
            } else if (leftPressed && !rightPressed) {
                this.velocityX = -this.speed;
            }
            if (jumpPressed && this.isGrounded && this.jumpCap > 0) {
                jump();
            }
        }

//        if (crouchPressed && isGrounded) {
//            // Placeholder: Set state if needed, but full implementation is complex
//        }
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


        if(Level.checkCeilingCollision(hitbox,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect)){
            this.velocityY=1;
        }



        if (this.velocityX > 0) { //coliziune perete dreapta
            if (Level.checkWallCollision(hitbox, true,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect)) {
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
            if (Level.checkWallCollision(hitbox, false,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect)) {
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

        int fallCheckResult = Level.checkFalling(hitbox,this.LEVEL_WIDTH,this.LEVEL_HEIGHT,this.behaviorIDsToRespect);


            if (this.velocityY > 0) { // Moving Down
                if (fallCheckResult == 0) { // Hit ground
                    this.isGrounded = true;
                    this.velocityY = 0;
                    Level.snapToGround(this.hitbox);
                    this.jumpCap = 10; //resetare jump
                } else {
                    this.isGrounded = false;
                }
            } else if (this.velocityY < 0) { // Moving Up
                this.isGrounded = false;

            } else { // velocityY == 0
                if (fallCheckResult == 0) { // Standing still on ground
                    if (!this.isGrounded) { // Just landed precisely
                        Level.snapToGround(this.hitbox);
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
//                if(reflink.getKeyManager().isKeyPressed(KeyEvent.VK_SPACE)){
//                    return this.jumpingAnimation;
//
//                }
//                else{
//                    return this.fallingAnimation;
//                }
            case JUMPING:
                return this.jumpingAnimation;
            case CROUCHING:
//                return this.crounchingAnimation;
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

    public boolean getIsDying(){
        return this.isDying;
    }


    private void getContext(){
        switch (State.getState().getStateName()){
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



    @Override
    public void attack() {

    }

    public float getJumpStrength(){
        return this.jumpStrength;
    }
    public void setJumpStrength(float jumpStrength){
        this.jumpStrength = jumpStrength;
    }

    @Override
    public String getSource() {
        return "GAME";
    }

    public void setGrapplePoint(int x,int y){
        this.currentGrappleX = x;
        this.currentGrappleY = y;
    }


    @Override
    public void draw(Graphics g){

        this.hitbox.printHitbox(g);
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

    public boolean getFlipped() {return this.flipped;}
    public void setGrappleInterrupt(boolean isInterrupted) { this .grappleInterrupt = isInterrupted;}

    public int getCurrentGrappleX() {return this.currentGrappleX;}
    public int getCurrentGrappleY() {return this.currentGrappleY;}

    public boolean getHasWhip(){return this.hasWhip;}
    public void setHasWhip(boolean hasWhip){this.hasWhip = hasWhip;}

    public int getNrOfEscapes(){return this.nrOfEscapes;}
    public int getMaxNrOfEscapes(){return this.maxNrOfEscapes;}
    public void setNrOfEscapes(int nrOfEscapes) { this.nrOfEscapes = nrOfEscapes; if(this.nrOfEscapes>this.getMaxNrOfEscapes()){this.nrOfEscapes = 3; } else if(this.nrOfEscapes<0){this.nrOfEscapes = 0;}}

    public boolean getCanEngage(){return this.canEngage;}
    public void setCanEngage(boolean canEngage) { this.canEngage = canEngage;}


    public void loadHeroState(boolean access){
        if(this.reflink.getHeroRefreshDoneSignal()){
           return;
        }

        try{
            System.out.println("Loading Character");
            this.health = this.reflink.getDataProxy().load(Constants.HERO_HEALTH,access);
            this.x = this.reflink.getDataProxy().load(Constants.HERO_X,access);
            this.getHitbox().setX(this.x);
            this.y = this.reflink.getDataProxy().load(Constants.HERO_Y,access);
            this.getHitbox().setY(this.y);
            this.hasWhip = this.reflink.getDataProxy().load(Constants.HERO_HAS_WHIP, access) == 1;
            this.nrOfEscapes = this.reflink.getDataProxy().load(Constants.HERO_NR_OF_FLEES,access);
            this.nrOfCollectedSaves = this.reflink.getDataProxy().load(Constants.HERO_NR_OF_COLLECTED_SAVES,access);
            this.nrOfCompletedLevels = this.reflink.getDataProxy().load(Constants.HERO_NR_OF_FINISHED_LEVELS,access);
            this.reflink.setHeroRefreshDoneSignal(true);
        } catch (AccessDeniedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

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
            this.reflink.setHeroStoreDoneSignal(true);
        } catch (AccessDeniedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }



    }

    public int getScore(){return this.score;}
    public void setScore(int score){this.score = score;}


    public int getNrOfCollectedSaves() { return this.nrOfCollectedSaves;}
    public void setNrOfCollectedSaves(int nr) { this.nrOfCollectedSaves = nr;}

    public int getNrOfCompletedLevels() { return this.nrOfCompletedLevels;}
    public void setNrOfCompletedLevels(int nrOfCompletedLevels) { this.nrOfCompletedLevels = nrOfCompletedLevels;}
}