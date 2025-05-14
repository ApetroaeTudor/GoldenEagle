package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.Animation;
import PaooGame.RefLinks;

import java.awt.*;

public abstract class EnemyStrategy {
    public int getHitboxWidth() {
        return hitboxWidth;
    }

    public float getSpeed() {
        return speed;
    }

    public int getHitboxHeight() {
        return hitboxHeight;
    }

    public int getLevelWidthInTiles() {
        return levelWidthInTiles;
    }

    public int getLevelHeightInTiles() {
        return levelHeightInTiles;
    }

    public Color getHealthBarColor1() {
        return healthBarColor1;
    }

    public Color getHealthBarColor2() {
        return healthBarColor2;
    }

    public Animation getWalkingAnimation() {
        return walkingAnimation;
    }

    public Animation getInFightAttackingAnimation() {
        return inFightAttackingAnimation;
    }

    public Animation getInFightIdleAnimation() {
        return inFightIdleAnimation;
    }

    public RefLinks getReflink() {
        return reflink;
    }

    public double getDamage() {
        return damage;
    }

    public double getHealth() {
        return health;
    }

    public int[] getBehaviorIDsToRespect() {
        return behaviorIDsToRespect;
    }

    protected float speed;
    protected int hitboxWidth;
    protected int hitboxHeight;

    protected int levelWidthInTiles;
    protected int levelHeightInTiles;

    protected Color healthBarColor1;
    protected Color healthBarColor2;

    protected Animation walkingAnimation;
    protected Animation inFightAttackingAnimation;
    protected Animation inFightIdleAnimation;

    protected RefLinks reflink;

    protected double damage;
    protected double health;

    protected int[] behaviorIDsToRespect;

    public EnemyStrategy(RefLinks reflink){
        this.reflink = reflink;
    }

    public abstract String getName();
    public abstract String getSource();

    public abstract void drawName(Graphics2D g2d);

}
