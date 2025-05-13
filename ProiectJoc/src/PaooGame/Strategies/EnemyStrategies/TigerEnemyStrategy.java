package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.Animation;
import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

public class TigerEnemyStrategy extends EnemyStrategy {

    private static TigerEnemyStrategy instance = null;

    public static TigerEnemyStrategy getInstance(RefLinks reflink){
        if(TigerEnemyStrategy.instance == null ){
            TigerEnemyStrategy.instance = new TigerEnemyStrategy(reflink);
        }
        return instance;
    }



    public TigerEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = -0.4f;
        this.hitboxWidth = (int)( (64.0/100.0)*50.0 );
        this.hitboxHeight = (int)( (32.0/100)*50.0 );

        this.levelWidthInTiles = Constants.LEVEL1_WIDTH;
        this.levelHeightInTiles = Constants.LEVEL1_HEIGHT;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.damage = Constants.TIGER_DAMAGE;
        this.health = Constants.TIGER_HEALTH;


        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,4,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,4,15,this.getName()); //
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,1,10,this.getName()); //
        this.inFightIdleAnimation.loadAnimation();

        this.behaviorIDsToRespect = reflink.getGame().getLevel1().getBehaviorIDs();
    }

    @Override
    public String getName(){
        return Constants.TIGER_NAME;
    }

    @Override
    public String getSource(){
        return Constants.LEVEL_1;
    }


}
