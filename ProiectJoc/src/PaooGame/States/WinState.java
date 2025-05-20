package PaooGame.States;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Entities.Enemy;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import javax.swing.*;
import java.awt.*;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class WinState extends State {

    private double blackIntensity = 1.0;
    private double fadeSpeed = 0.05;

    private StaticItemAnimation bgAnimation;
    private StaticItemAnimation youWinAnimation;

    private Timer showReturnButtonTimer;
    private int showReturnButtonTimeoutMillis = 2000;
    private boolean showButton = false;
    private boolean isTimerStarted = false;


    private Rectangle exitToMenuButton;

    public WinState(RefLinks reflink){
        super(reflink);
        this.stateName = "WinState";

        this.showReturnButtonTimer = new Timer(this.showReturnButtonTimeoutMillis, e-> {
            this.showButton = true;
        });
        this.showReturnButtonTimer.setRepeats(false);
        int centerX = 350;
        int startY = 650;

        this.exitToMenuButton = new Rectangle(centerX + 50, startY,300,50);

        this.bgAnimation = new StaticItemAnimation(this.reflink,Constants.WINNING_BG_PATH,Constants.WINNING_BG_FRAME_NR,5,Constants.WINNING_BG_WIDTH,Constants.WINNING_BG_HEIGHT);
        this.youWinAnimation = new StaticItemAnimation(this.reflink,Constants.YOU_WIN_PATH,Constants.YOU_WIN_FRAME_NR,12,Constants.YOU_WIN_IMG_WIDTH,Constants.YOU_WIN_IMG_HEIGHT);
        this.bgAnimation.loadAnimation();

        this.youWinAnimation.loadAnimation();
        this.youWinAnimation.triggerOnce();
        this.youWinAnimation.setPlayOnce(true);


    }

    @Override
    public String getStateName(){
        return this.stateName;
    }

    @Override
    public void setEnemy(Enemy enemy){

    }

    @Override
    public void update(){
        if(!Objects.equals(State.getState().getStateName(), this.stateName)){
            this.restoreState();
        }
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();
        this.bgAnimation.updateAnimation();
        this.youWinAnimation.updateAnimation();





        if(mouse.getNumberOfMousePresses()>0){
            if(exitToMenuButton.contains(mx,my)){
                State.setState(this.reflink.getGame().getMenuState());
                mouse.mouseReleased(null);
                this.restoreState();
            }
        }

        if(Objects.equals(State.getState().getStateName(), this.stateName) && !this.isTimerStarted){
            this.isTimerStarted = true;
            this.showReturnButtonTimer.start();
        }
    }


    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        this.bgAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        this.youWinAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);

        Font oldFont = g2d.getFont();
        Color oldColor = g2d.getColor();
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString("Your score: " + reflink.getHero().getScore(),20,40);

        g2d.setFont(new Font("Arial",Font.BOLD,50));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("TOP SCORES",380,50);
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.drawString("1. "+this.reflink.getScore1(),380,80);
        g2d.drawString("2. " +this.reflink.getScore2(),380,110);
        g2d.drawString("3. "+ this.reflink.getScore3(),380,140);


        g2d.setFont(oldFont);
        g2d.setColor(oldColor);


        if(Objects.equals(State.getState().getStateName(),this.getStateName())){
            Color originalColor = g2d.getColor();
            if (this.blackIntensity <= 0) {
                this.blackIntensity = 0;
            }
            g2d.setColor(new Color(0,0,0,(int)(this.blackIntensity*255.0)));
            g2d.fillRect(0,0, Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            this.blackIntensity-=this.fadeSpeed;
            g2d.setColor(originalColor);
        }

        if(this.showButton){
            int mx = reflink.getMouseInput().getMouseX();
            int my = reflink.getMouseInput().getMouseY();
            drawModernButton(g2d,exitToMenuButton,"MAIN MENU",mx,my);
        }
    }

    @Override
    public void restoreState(){
        this.blackIntensity = 1.0;
        this.fadeSpeed = 0.05;
        this.isTimerStarted = false;
        this.showButton = false;
        this.reflink.setScore1(0);
        this.reflink.setScore2(0);
        this.reflink.setScore3(0);
    }

    @Override
    public void loadState(boolean access){

    }

    @Override
    public void storeState(boolean access){

    }


    private void drawModernButton(Graphics2D g2d, Rectangle rect, String text, int mx, int my) {
        boolean hover = rect.contains(mx, my);
        // Fundal gradient
        GradientPaint gp = new GradientPaint(
                rect.x, rect.y,
                hover ? new Color(70, 130, 180) : new Color(60, 60, 60),
                rect.x, rect.y + rect.height,
                hover ? new Color(50, 100, 150) : new Color(40, 40, 40)
        );

        g2d.setPaint(gp);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Border
        g2d.setColor(hover ? new Color(160, 200, 255) : new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text))/2;
        int textY = rect.y + (rect.height + fm.getAscent())/2 - 4;
        g2d.drawString(text, textX, textY);
    }



}

