package Entities;

import PaooGame.Animations.Animation;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

public abstract class Entity {
    protected float x;
    protected float y;

    protected boolean isEngaged;

    protected float gravity;
    protected float maxFallSpeed;

    protected float speed;
    protected double health;

    protected boolean isGrounded;
    protected boolean flipped;

    protected Hitbox hitbox;

    protected RefLinks reflink;

    protected float velocityX;
    protected float velocityY;

    protected float hitboxOffsetX = 0;
    protected float hitboxOffsetY = 0;

    public Entity(RefLinks reflink, int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.reflink = reflink;
        this.gravity = 0.15f;
        this.maxFallSpeed = 6.0f;
        this.health = 100;

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

    public abstract void Update();

    protected abstract void updateVisualPosition();

    protected abstract Animation getAnimationByState();

    protected abstract void updateAnimationState();

    public void Draw(Graphics g){
        this.hitbox.printHitbox(g);
        this.getAnimationByState().paintAnimation(g,(int)this.x,(int)this.y,this.flipped);
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

    public double getHealth() {
        return health;
    }

    public Hitbox getHitbox() { return this.hitbox; }

    public void setHitbox(Hitbox hitbox) { this.hitbox = hitbox; }

    public boolean getIsEngaged(){
        return this.isEngaged;
    }
    public void setIsEngaged(boolean isEngaged){
        this.isEngaged = isEngaged;
    }

}
