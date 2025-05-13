package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

public class WizardEnemyStrategy extends EnemyStrategy{

    private static WizardEnemyStrategy instance = null;

    public static WizardEnemyStrategy getInstance(RefLinks reflink){
        if(WizardEnemyStrategy.instance == null){
            WizardEnemyStrategy.instance = new WizardEnemyStrategy(reflink);
        }
        return instance;
    }


    public WizardEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.WIZARD_SPEED;
        this.hitboxWidth = Constants.WIZARD_PASSIVE_TILE_WIDTH;
        this.hitboxHeight = Constants.WIZARD_PASSIVE_TILE_HEIGHT;

        this.levelHeightInTiles = Constants.LEVEL3_HEIGHT;
        this.levelWidthInTiles = Constants.LEVEL3_WIDTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.WIZARD_PASSIVE_TILE_NR,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.WIZARD_ATTACKING_TILE_NR,10,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.WIZARD_IN_FIGHT_IDLE_TILE_NR,10,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.WIZARD_DAMAGE;
        this.health = Constants.WIZARD_HEALTH;

        this.healthBarColor1 = Constants.PURPLE_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.PURPLE_HEALTH_BAR_COLOR_2;

        this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
    }

    @Override
    public String getName(){
        return Constants.WIZARD_NAME;
    }

    @Override
    public String getSource(){
        return Constants.LEVEL_3;
    }

    @Override
    public void drawName(Graphics2D g2d){
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font("Arial",Font.BOLD,50));
        g2d.setColor(Color.RED);
        g2d.drawString(this.getName(),385,95);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }



}
