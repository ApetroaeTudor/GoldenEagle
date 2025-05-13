package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Config.Constants;
import PaooGame.HUD.HealthBar;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

public abstract class Entity {
    protected float x;
    protected float y;
    private HealthBar healthBar;
    protected int[] behaviorIDsToRespect;




    protected boolean isEngaged;

    protected float gravity;
    protected float maxFallSpeed;

    protected float speed;
    protected double health;
    protected double damage;

    protected boolean isGrounded;
    protected boolean flipped;

    protected Hitbox hitbox;

    protected RefLinks reflink;

    protected float velocityX;
    protected float velocityY;

    protected float hitboxOffsetX = 0;
    protected float hitboxOffsetY = 0;

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

    protected void applyGravity(){
        if (!this.isGrounded) {
            this.velocityY += this.gravity; //velocitatea accelereaza cu cat cad mai mult
            if (this.velocityY > this.maxFallSpeed) { //impiedic caderea exagerata
                this.velocityY = this.maxFallSpeed;
            }
        }
    }

    protected abstract void moveAndCollide();

    public abstract void update();

    protected abstract void updateVisualPosition();

    protected abstract Animation getAnimationByState();

    protected abstract void updateAnimationState();



    public float getX() {
        return x;
    }
    public void setX(float x){this.x = x;}

    public float getY() {
        return y;
    }
    public void setY(float y){this.y = y;}

    public float getWidth() {
        return hitbox.getWidth();
    }

    public float getHeight() {
        return hitbox.getHeight();
    }

    public double getHealth() {
        return health;
    }
    public void setHealth(double health) {this.health = health;}

    public Hitbox getHitbox() { return this.hitbox; }

    public void setHitbox(Hitbox hitbox) { this.hitbox = hitbox; }

    public HealthBar getHealthBar(){return this.healthBar; }

    public boolean getIsEngaged(){
        return this.isEngaged;
    }
    public void setIsEngaged(boolean isEngaged){
        this.isEngaged = isEngaged;
    }

    public abstract String getName();




    public void DrawHealthBar(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        healthBar.draw(g2d);
    }

    public void setHealthBarX(int x){
        this.healthBar.setX(x);
    }
    public void setHealthBarY(int y){
        this.healthBar.setY(y);
    }
    public void setHealthBarWidth(int width){
        this.healthBar.setWidth(width);
    }
    public void setHealthBarHeight(int height){
        this.healthBar.setHeight(height);
    }

    public int getHealthBarX(){return this.healthBar.getX();}
    public int getHealthBarY(){return this.healthBar.getY();}
    public int getHealthBarWidth(){return this.healthBar.getWidth();}
    public int getHealthBarHeight(){return this.healthBar.getHeight();}

    public Color getHealthBarColor1(){return this.healthBar.getColor1();}
    public Color getHealthBarColor2(){return this.healthBar.getColor2();}

    public void setHealthBarColor1(Color color1){this.healthBar.setColor1(color1);}
    public void setHealthBarColor2(Color color2){this.healthBar.setColor2(color2);}

    public void resetHealthBarDefaultValues(){this.healthBar.resetPositionToDefault();}

    public void reduceHealth(double health) { this.health-=health; if(this.health < 0){this.health = 0;}}

    public void restoreHealth(double health) { this.health+=health; if(this.health > 100){this.health = 100;}}

    public double getDamage(){ return this.damage; }
    public void setDamage(double damage){this.damage = damage;}

    public void nullifyHitbox(){
        this.hitbox.setX(0);
        this.hitbox.setY(0);
        this.hitbox.setWidth(0);
        this.hitbox.setHeight(0);
    }

    public abstract void restoreEntity();


    public abstract void attack();

    public float getVelocityX(){return this.velocityX;}
    public float getVelocityY(){return this.velocityY;}

    public abstract String getSource();

    public void draw(Graphics g){

        this.hitbox.printHitbox(g);
        this.getAnimationByState().paintAnimation(g,(int)this.x,(int)this.y,this.flipped,1);
    }

    public void setIsFlipped(boolean flipped )  {this.flipped = flipped;}
    public boolean getIsFlipped() { return this.flipped;}






}
