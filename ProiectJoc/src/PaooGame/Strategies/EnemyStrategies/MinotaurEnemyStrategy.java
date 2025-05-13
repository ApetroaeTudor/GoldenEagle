package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

public class MinotaurEnemyStrategy extends EnemyStrategy {

    private static MinotaurEnemyStrategy instance = null;

    public static MinotaurEnemyStrategy getInstance(RefLinks reflink){
        if(MinotaurEnemyStrategy.instance == null){
            MinotaurEnemyStrategy.instance = new MinotaurEnemyStrategy(reflink);
        }
        return instance;
    }

    public MinotaurEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.MINOTAUR_SPEED;
        this.hitboxWidth = Constants.MINOTAUR_PASSIVE_TILE_WIDTH/2;
        this.hitboxHeight = Constants.MINOTAUR_PASSIVE_TILE_HEIGHT/2;

        this.levelHeightInTiles = Constants.LEVEL3_HEIGHT;
        this.levelWidthInTiles = Constants.LEVEL3_WIDTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.MINOTAUR_PASSIVE_TILE_NR,15,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.MINOTAUR_ATTACKING_TILE_NR,10,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.MINOTAUR_IN_FIGHT_IDLE_TILE_NR,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.MINOTAUR_DAMAGE;
        this.health = Constants.MINOTAUR_HEALTH;

        this.healthBarColor1 = Constants.YELLOW_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.YELLOW_HEALTH_BAR_COLOR_2;

        this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
    }


    @Override
    public String getName(){
        return Constants.MINOTAUR_NAME;
    }

    @Override
    public String getSource(){
        return Constants.LEVEL_3;
    }

    @Override
    public void drawName(Graphics2D g2d){
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setColor(Color.RED);
        g2d.drawString(this.getName(),470,60);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }

}
