package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.Animation;
import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.sql.Ref;

public class BasicSkeletonEnemyStrategy extends EnemyStrategy {
    private static BasicSkeletonEnemyStrategy instance = null;

    public static BasicSkeletonEnemyStrategy getInstance(RefLinks reflink){
        if(BasicSkeletonEnemyStrategy.instance == null ){
            BasicSkeletonEnemyStrategy.instance = new BasicSkeletonEnemyStrategy(reflink);
        }
        return instance;
    }


    @Override
    public String getName(){
        return Constants.BASIC_SKELETON_NAME;
    }

    @Override
    public String getSource(){
        return Constants.LEVEL_2;
    }

    public BasicSkeletonEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.BASIC_SKELETON_SPEED;
        this.hitboxWidth = 32;
        this.hitboxHeight = 32;

        this.levelWidthInTiles = Constants.LEVEL2_WIDTH;
        this.levelHeightInTiles = Constants.LEVEL2_HEIGHT;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.damage = Constants.BASIC_SKELETON_DAMAGE;
        this.health = Constants.BASIC_SKELETON_HEALTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,13,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,18,5,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,11,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.behaviorIDsToRespect = reflink.getGame().getLevel2().getBehaviorIDs();
    }

    @Override
    public void drawName(Graphics2D g2d){
        g2d.setColor(Color.RED);
        g2d.drawString(this.getName(),520,60);
    }

}
