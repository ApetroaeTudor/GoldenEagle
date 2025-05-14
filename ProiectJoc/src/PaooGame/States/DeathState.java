package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.swing.*;


public class DeathState extends State {

    private double blackIntensity = 1.0;
    private double fadeSpeed = 0.05;

    private Timer timer;
    private int timeoutInMillis = 1000;

    private boolean isFadedIn = false;

    private boolean isFadingOut = false;

    public DeathState(RefLinks reflink){
        super(reflink);
        this.stateName = "DeathState";
        this.timer = new Timer(this.timeoutInMillis,e->{
            this.isFadingOut = true;
        });
        this.timer.setRepeats(false);

    }
    @Override
    public String getStateName() {
        return this.stateName;
    }

    @Override
    public void setEnemy(Enemy enemy) {

    }

    @Override
    public void update() {

        if(this.isFadedIn){
            this.timer.start();
        }
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage backgroundImg = this.reflink.getTileCache().getBackground(Constants.DEATH_SCREEN_BG_PATH);
        g.drawImage(backgroundImg,0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);

        if(Objects.equals(State.getState().getStateName(), this.stateName) && !this.isFadedIn){
            Graphics2D g2d = (Graphics2D) g;
            Color originalColor = g2d.getColor();
            if(this.blackIntensity>=1){
                this.blackIntensity = 1.0;
            }
            if(this.blackIntensity<0){
                this.blackIntensity = 0.0;
                this.isFadedIn = true;
            }
            g2d.setColor(new Color(0,0,0,(int)(this.blackIntensity*255.0)));
            g2d.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            this.blackIntensity-=this.fadeSpeed;
            g2d.setColor(originalColor);
        }

        if(Objects.equals(State.getState().getStateName(), this.stateName) && this.isFadingOut){
            Graphics2D g2d = (Graphics2D) g;
            Color originalColor = g2d.getColor();
            if(this.blackIntensity>1){
                this.reflink.setDataRefreshSignal(true);
//                this.restoreState();
//                this.blackIntensity = 1.0;
//                this.reflink.getHero().restoreEntity();
//                this.reflink.getGame().getLevel1State().restoreState();
//                this.reflink.getGame().getFightState().restoreState();
                State.setState(this.reflink.getGame().getMenuState());

            }
            if(this.blackIntensity<0){
                this.blackIntensity = 0.0;
                this.isFadedIn = true;
            }
            g2d.setColor(new Color(0,0,0,(int)(this.blackIntensity*255.0)));
            g2d.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            this.blackIntensity+=this.fadeSpeed;
            g2d.setColor(originalColor);
        }

    }

    @Override
    public void restoreState() {
        this.isFadedIn = false;
        this.isFadingOut = false;

    }

    @Override
    public void loadState(boolean access) {

    }

    @Override
    public void storeState(boolean access) {

    }

    public void setIsFadedIn(boolean isFadedIn){ this.isFadedIn = isFadedIn;}
    public void setIsFadingOut(boolean isFadingOut){this.isFadingOut = isFadingOut;}
}
