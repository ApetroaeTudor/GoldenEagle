package PaooGame.States;

import Entities.Entity;
import PaooGame.Animations.Animation;
import PaooGame.Animations.EffectsAnimations.EffectAnimation;
import PaooGame.Config.Constants;
import PaooGame.HUD.AttackButton;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Ref;

public class FightState extends State {
    private Entity enemy=null;
    private boolean isPlayerTurn;
    private double blackIntensity = 1.0;
    private double fadeSpeed = 0.05;

    private Animation attackAnimation;

    private AttackButton attackButton;

    private int scrollX = 0;
    private int scrollSpeed = 1;

    public FightState(RefLinks reflink){
        super(reflink);

        this.attackButton = new AttackButton(reflink.getHero(),300,650);

        this.isPlayerTurn = true;

        this.attackAnimation = new EffectAnimation(this.refLink,Constants.EFFECTS.ATTACK_EXPLOSION,8,5);
        this.attackAnimation.loadAnimation();
    }

    @Override
    public void Update() {
        if(this.enemy!=null){
            this.enemy.setX(480);
            this.enemy.setY(100);
            this.enemy.Update();
            this.enemy.setHealthBarX(460);
            this.enemy.setHealthBarY(80);




            this.refLink.getHero().setHealthBarX(20);
            this.refLink.getHero().setHealthBarY(650);
            this.refLink.getHero().setHealthBarWidth(200);
            this.refLink.getHero().setHealthBarHeight(40);

        }
        MouseInput mouse = refLink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        this.attackAnimation.updateAnimation();

        if(this.attackButton.isClicked(mx,my,mouse.isOneClick())){
            this.attackAnimation.triggerOnce();
            this.enemy.reduceHealth(this.refLink.getHero().getDamage());
        }


        this.scrollX -= this.scrollSpeed;

        if(this.scrollX <= -Constants.WINDOW_WIDTH){
            this.scrollX = 0;
        }

    }

    @Override
    public void Draw(Graphics g) {
        BufferedImage backgroundImg = this.refLink.getTileCache().getBackground(Constants.TIGER_FIGHT_BG_PATH);
//        g.drawImage(backgroundImg, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);

        g.drawImage(backgroundImg,scrollX,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        //scrollX ca offsett si acum ramane o gaura in background, trebuie acoperita cu o a doua img
        g.drawImage(backgroundImg,scrollX+ Constants.WINDOW_WIDTH,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        //a doua imagine e printata fix dupa prima pentru a parea o singura imagine

        Graphics2D g2d = (Graphics2D)g;
        Color originalColor = g2d.getColor();
        if(this.blackIntensity>=1){
            this.blackIntensity = 1;
        }
        if(this.blackIntensity<0){
            this.blackIntensity = 0;
        }
        g2d.setColor(new Color(0,0,0,(int)(blackIntensity*255.0) ));
        this.blackIntensity-=this.fadeSpeed;

        g.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        g2d.setColor(originalColor);

        if(this.enemy!=null){
            this.enemy.Draw(g);
        }

        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String enemyName = enemy.getName();
        switch (enemyName){
            case "Tiger":
                g2d.setColor(Color.RED);
                g2d.drawString(enemyName,520,60);
                break;
        }
        g2d.setFont(new Font("Arial",Font.BOLD,20));
        g2d.setColor(Color.WHITE);
        if(this.isPlayerTurn){
            g2d.drawString("Current Turn: Player",20,30);
        }
        else{
            g2d.setColor(Color.RED);
            String printString = "Current Turn: " + enemy.getName();
            g2d.drawString(printString,20,30);
        }
        g2d.drawString("Your health:",20,630);


        this.enemy.DrawHealthBar(g);
        refLink.getHero().DrawHealthBar(g);

        this.attackButton.draw(g2d);
        this.attackAnimation.paintAnimation(g,380,200,false);




    }

    @Override
    public void setEnemy(Entity enemy){
        this.enemy = enemy;
    }


    protected String stateName = Constants.FIGHT_STATE;
    @Override
    public String getStateName() {
        return this.stateName;
    }
}
