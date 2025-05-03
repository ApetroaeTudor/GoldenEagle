package PaooGame.States;

import Entities.Entity;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Ref;

public class FightState extends State {
    private Entity enemy;
    private boolean isPlayerTurn;
    private double blackIntensity = 1.0;
    private double fadeSpeed = 0.05;

    private int scrollX = 0;
    private int scrollSpeed = 1;

    public FightState(RefLinks reflink){
        super(reflink);

        this.isPlayerTurn = true;


    }


    @Override
    public void Update() {
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
    }


    protected String stateName = Constants.FIGHT_STATE;
    @Override
    public String getStateName() {
        return this.stateName;
    }
}
