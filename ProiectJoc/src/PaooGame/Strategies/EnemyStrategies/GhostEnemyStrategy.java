package PaooGame.Strategies.EnemyStrategies;

import PaooGame.Animations.EnemyAnimations.enemyActionAnimation;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

public class GhostEnemyStrategy extends EnemyStrategy{

    private static GhostEnemyStrategy instance = null;

    public static GhostEnemyStrategy getInstance(RefLinks reflink){
        if(GhostEnemyStrategy.instance == null){
            GhostEnemyStrategy.instance = new GhostEnemyStrategy(reflink);
        }
        return instance;
    }

    public GhostEnemyStrategy(RefLinks reflink){
        super(reflink);
        this.speed = Constants.GHOST_SPEED;
        this.hitboxWidth = Constants.GHOST_PASSIVE_TILE_WIDTH;
        this.hitboxHeight = Constants.GHOST_PASSIVE_TILE_HEIGHT;

        this.levelHeightInTiles = Constants.LEVEL3_HEIGHT;
        this.levelWidthInTiles = Constants.LEVEL3_WIDTH;

        this.walkingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.WALKING,Constants.GHOST_PASSIVE_TILE_NR,10,this.getName());
        this.walkingAnimation.loadAnimation();
        this.inFightAttackingAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_ATTACKING,Constants.GHOST_ATTACKING_TILE_NR,20,this.getName());
        this.inFightAttackingAnimation.loadAnimation();
        this.inFightIdleAnimation = new enemyActionAnimation(this.reflink,Constants.ENEMY_STATES.IN_FIGHT_IDLE,Constants.GHOST_IN_FIGHT_IDLE_TILE_NR,20,this.getName());
        this.inFightIdleAnimation.loadAnimation();

        this.damage = Constants.GHOST_DAMAGE;
        this.health = Constants.GHOST_HEALTH;

        this.healthBarColor1 = Constants.PURPLE_HEALTH_BAR_COLOR_1;
        this.healthBarColor2 = Constants.PURPLE_HEALTH_BAR_COLOR_2;

        this.behaviorIDsToRespect = reflink.getGame().getLevel3().getBehaviorIDs();
    }


    @Override
    public String getName(){
        return Constants.GHOST_NAME;
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
        g2d.setColor(Color.ORANGE);
        g2d.drawString(this.getName(),515,60);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }
}
