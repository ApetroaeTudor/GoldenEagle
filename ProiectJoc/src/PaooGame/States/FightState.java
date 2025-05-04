package PaooGame.States;

import PaooGame.Entities.Entity;
import PaooGame.Animations.Animation;
import PaooGame.Animations.EffectsAnimations.EffectAnimation;
import PaooGame.Config.Constants;
import PaooGame.HUD.AttackButton;
import PaooGame.HUD.VerticalGradientBar;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

//la enemy turn progress valorile vor varia intre 0 si 50.
//un progressOnSpace == 50 inseamna 50% damage reduction

public class FightState extends State {
    private Entity enemy=null;
    private boolean isPlayerTurn;
    private double blackIntensity = 1.0;
    private double fadeSpeed = 0.05;

    private Timer timer;
    private Timer timer2;
    private Timer timer3;
    private Timer popupTimer;
    private Timer waitForEnemyDeathTimer;

    private int popupTimeInMillis = 700;
    private int timeoutInMillis = 1000;
    private int timeoutInMillisForDelayingPlayerTurn = 1500;
    private int timeoutInMillisWaitForEnemyDeath = 3000;
    private boolean isTimerStarted;
    private boolean isTimerFinished;
    private boolean isWaitingForPlayerTurn;
    private boolean didEnemyAttackAlready;

    private boolean printingDamageReceivedPopup = false;
    private boolean printingDamageDealtPopup = false;
    private double latestDamageReceived = 0.0;
    private double latestDamageDealt = 0.0;

    private boolean transitioningToDeath = false;
    private boolean transitioningToVictory = false;

    private double fadeToBlackProgress = 0.0;
    private double fadeToBlackStep = 0.05;


    private int enemyTurnProgressTick = 0;
    private int enemyTurnProgressCap = 2;
    private int enemyTurnProgress = 0;
    private VerticalGradientBar blockingBar;
    private int progressOnSpace = 0;

    private Animation attackAnimation;

    private AttackButton attackButton;

    private int scrollX = 0;
    private int scrollSpeed = 1;

    public FightState(RefLinks reflink){
        super(reflink);
        this.isTimerFinished = false;
        this.isTimerStarted = false;
        this.isWaitingForPlayerTurn = false;
        this.didEnemyAttackAlready = false;

        this.blockingBar = new VerticalGradientBar(50,50);
        this.blockingBar.setPosition(935,250);

        this.timer = new Timer(timeoutInMillis, e->{
            this.isTimerFinished = true;
            this.isTimerStarted = false;

        });
        this.timer2 = new Timer(timeoutInMillisForDelayingPlayerTurn,e->{
            this.isPlayerTurn = true;
            this.isWaitingForPlayerTurn = false;
            this.didEnemyAttackAlready = false;
            this.enemyTurnProgress = 0;
            this.blockingBar.updateValue(this.enemyTurnProgress);
            this.refLink.getHero().reduceHealth(this.enemy.getDamage()* (  (100.0-this.progressOnSpace)/100.0) );
            this.latestDamageReceived = this.enemy.getDamage()* (  (100.0-this.progressOnSpace)/100.0);
            System.out.println("Reduced damage: " + this.enemy.getDamage()* (  (100.0-this.progressOnSpace)/100.0) );
            System.out.println("Original damage: " + this.enemy.getDamage());
            this.progressOnSpace = 0;
            this.printingDamageReceivedPopup = true;
            this.popupTimer.start();

        });
        this.timer3 = new Timer(1000,e->{
            this.transitioningToDeath = true;
        });

        this.popupTimer = new Timer(this.popupTimeInMillis,e->{
            this.printingDamageReceivedPopup = false;
            this.latestDamageReceived = 0.0;
            this.printingDamageDealtPopup = false;
            this.latestDamageDealt = 0.0;
        });
        this.waitForEnemyDeathTimer = new Timer(this.timeoutInMillisWaitForEnemyDeath,e->{
            transitioningToVictory = true;

        });
        this.timer.setRepeats(false);
        this.timer2.setRepeats(false);
        this.timer3.setRepeats(false);
        this.popupTimer.setRepeats(false);
        this.waitForEnemyDeathTimer.setRepeats(false);

        this.attackButton = new AttackButton(reflink.getHero(),310,620);

        this.isPlayerTurn = true;

        this.attackAnimation = new EffectAnimation(this.refLink,Constants.EFFECTS.ATTACK_EXPLOSION,8,5);
        this.attackAnimation.loadAnimation();
    }

    @Override
    public void update() {
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
        this.attackAnimation.updateAnimation();


        MouseInput mouse = refLink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();


        if(this.isPlayerTurn){

            this.attackButton.updateHover(mx,my);
            if(this.attackButton.isClicked(mx,my,mouse.isOneClick())){
                this.attackAnimation.triggerOnce();
                this.enemy.reduceHealth(this.refLink.getHero().getDamage());
                this.printingDamageDealtPopup = true;
                this.latestDamageDealt = this.refLink.getHero().getDamage();
                this.attackButton.setIsHovered(false);
                this.isPlayerTurn = false;
            }
        }
        else{
            if(this.refLink.getKeyManager().isKeyPressed(KeyEvent.VK_SPACE) && this.enemyTurnProgress<=50){
                this.progressOnSpace = this.enemyTurnProgress;
                this.enemyTurnProgress = 51;
            }

            this.blockingBar.updateValue(this.enemyTurnProgress);
            this.enemyTurnProgressTick++;
            if(this.enemyTurnProgressTick>this.enemyTurnProgressCap){
                this.enemyTurnProgress+=6;
                this.enemyTurnProgressTick = 0;
            }

            if(!this.isTimerStarted && !isTimerFinished && !didEnemyAttackAlready){
                System.out.println("timer started");
                this.timer.start();
                this.isTimerStarted = true;
            }
            if(this.isTimerFinished && !this.isTimerStarted && !this.didEnemyAttackAlready){
                System.out.println("tiger attacks now");
                this.enemy.attack();
                this.isTimerFinished = false;
                this.isWaitingForPlayerTurn = true;
                this.didEnemyAttackAlready = true;
            }
            if(this.isWaitingForPlayerTurn){
                this.timer2.start();
            }
        }



        if(this.enemy.getHealth() == 0){
            this.waitForEnemyDeathTimer.start();

        }
        if(this.refLink.getHero().getHealth()==0){
            this.timer3.start();
        }
        if(this.transitioningToVictory && this.fadeToBlackProgress==1){
            this.transitioningToVictory = false;
            this.fadeToBlackProgress = 0.0;
            this.attackAnimation.setIsFinished(true);
            State.setState(refLink.getGame().getLevel1State());
            this.refLink.getHero().resetHealthBarDefaultValues();
        }
        if(this.transitioningToDeath && this.fadeToBlackProgress==1){
            this.transitioningToDeath = false;
            this.fadeToBlackProgress = 0.0;
            this.attackAnimation.setIsFinished(true);
            refLink.getGame().getDeathState().restoreState();
            State.setState(refLink.getGame().getDeathState());
        }


        this.scrollX -= this.scrollSpeed;

        if(this.scrollX <= -Constants.WINDOW_WIDTH){
            this.scrollX = 0;
        }

    }

    @Override
    public void draw(Graphics g) {
        BufferedImage backgroundImg = this.refLink.getTileCache().getBackground(Constants.TIGER_FIGHT_BG_PATH);
//        g.drawImage(backgroundImg, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);

        g.drawImage(backgroundImg,scrollX,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        //scrollX ca offsett si acum ramane o gaura in background, trebuie acoperita cu o a doua img
        g.drawImage(backgroundImg,scrollX+ Constants.WINDOW_WIDTH,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        //a doua imagine e printata fix dupa prima pentru a parea o singura imagine

        Graphics2D g2d = (Graphics2D)g;
        this.blockingBar.draw(g2d);
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
            g2d.setColor(Color.WHITE);
        }
        g2d.drawString("Your health:",20,630);
        g2d.drawString("Press space",900,687);
        g2d.setFont(new Font("Arial",Font.BOLD,10));
        g2d.drawString("(When the bar is fuller you block more damage)",840,710);

        if(this.printingDamageReceivedPopup){
            g2d.setFont(new Font("Arial",Font.BOLD,30));
            originalColor = g2d.getColor();
            g2d.setColor(Color.RED);
            String damageToPrint = String.format("-%.2f",this.latestDamageReceived);
            g2d.drawString(damageToPrint,80,600);

            g2d.setColor(originalColor);
        }

        if(this.printingDamageDealtPopup){
            g2d.setFont(new Font("Arial",Font.BOLD,30));
            originalColor = g2d.getColor();
            g2d.setColor(Color.GREEN);
            String damageToPrint = String.format("-%.2f",this.latestDamageDealt);
            g2d.drawString(damageToPrint,350,170);

            g2d.setColor(originalColor);
        }


        this.enemy.DrawHealthBar(g);
        refLink.getHero().DrawHealthBar(g);

        this.attackButton.draw(g2d);
        this.attackAnimation.paintAnimation(g,380,200,false);


        if(transitioningToVictory || transitioningToDeath){
            originalColor = g2d.getColor();
            this.fadeToBlackProgress +=this.fadeToBlackStep;
            if(this.fadeToBlackProgress >=1 ){
                this.fadeToBlackProgress = 1;
            }
            int alpha = (int)(this.fadeToBlackProgress*255.0);
            g2d.setColor(new Color(0,0,0,alpha));
            g.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            g2d.setColor(originalColor);
        }

    }

    @Override
    public void restoreState() {
        blackIntensity = 1.0;
        fadeSpeed = 0.05;
        fadeToBlackProgress = 0.0;
        fadeToBlackStep = 0.05;
        this.transitioningToDeath = false;
        this.transitioningToVictory = false;

        this.printingDamageDealtPopup = false;
        this.printingDamageReceivedPopup = false;

        this.latestDamageDealt = 0.0;
        this.latestDamageReceived = 0.0;
        this.progressOnSpace = 0;
        this.enemyTurnProgress = 0;
        this.blockingBar.updateValue(this.enemyTurnProgress);
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
