package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.Animation;
import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.sql.Ref;

public class StrongSkeletonEnemyStrategy extends EnemyStrategy {
    private static StrongSkeletonEnemyStrategy instance = null;

    public static StrongSkeletonEnemyStrategy getInstance(RefLinks reflink){
        if(StrongSkeletonEnemyStrategy.instance == null ){
            StrongSkeletonEnemyStrategy.instance = new StrongSkeletonEnemyStrategy(reflink);
        }
        return instance;
    }


    @Override
    public String getName(){
        return Constants.STRONG_SKELETON_NAME;
    }

    @Override
    public String getSource(){
        return Constants.LEVEL_2;
    }

    public StrongSkeletonEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.STRONG_SKELETON_SPEED;
        this.hitboxWidth = 32;
        this.hitboxHeight = 32;

        this.levelWidthInTiles = Constants.LEVEL2_WIDTH;
        this.levelHeightInTiles = Constants.LEVEL2_HEIGHT;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.damage = Constants.STRONG_SKELETON_DAMAGE;
        this.health = Constants.STRONG_SKELETON_HEALTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.STRONG_SKELETON_PASSIVE_TILE_NR,15,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.STRONG_SKELETON_ATTACKING_TILE_NR,7,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.STRONG_SKELETON_IN_FIGHT_IDLE_TILE_NR,20,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.behaviorIDsToRespect = reflink.getGame().getLevel2().getBehaviorIDs();
    }

    @Override
    public void drawName(Graphics2D g2d){
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font("Arial",Font.BOLD,25));
        g2d.setColor(Color.RED);
        g2d.drawString(this.getName(),480,60);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }

}
