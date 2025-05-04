package PaooGame.HUD;

import Entities.Entity;
import Entities.Hero;
import PaooGame.Config.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar extends HUD {
    private double displayedHealth;

    private int width = 200;
    private int height = 20;
    private int x = 20;
    private int y = 20;

    private Color color1 = Constants.GREEN_HEALTH_BAR_COLOR_1;
    private Color color2 = Constants.GREEN_HEALTH_BAR_COLOR_2;

    public HealthBar(Entity entity) {
        super(entity);
        this.displayedHealth = entity.getHealth();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (entity == null) return;



        // Animația între viața reală și cea afișată
        double targetHealth = entity.getHealth();
        double speed = 1.5; // viteză de tranziție
        if (displayedHealth > targetHealth) {
            displayedHealth -= speed;
            if (displayedHealth < targetHealth) {
                displayedHealth = targetHealth;
            }
        } else if (displayedHealth < targetHealth) {
            displayedHealth += speed;
            if (displayedHealth > targetHealth) {
                displayedHealth = targetHealth;
            }
        }

        // Desenăm textul
//        g2d.setColor(Color.WHITE);
//        g2d.setFont(g2d.getFont().deriveFont(16f));
//        g2d.drawString("Health:", 20, y + 15);

        // Fundal roșu
        g2d.setColor(Color.RED);
        g2d.fillRoundRect(x, y, width, height, 10, 10);

        // Bară verde cu gradient
        int currentWidth = (int) (width * (displayedHealth / 100.0));
        if (currentWidth > 0) {
            g2d.setPaint(new java.awt.GradientPaint(x, y, this.color1, x + currentWidth, y, this.color2));
            g2d.fillRoundRect(x, y, currentWidth, height, 10, 10);
        }

        // Contur negru
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, width, height, 10, 10);
    }

    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
    public void setWidth(int width){this.width = width;}
    public void setHeight(int height){this.height = height;}

    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}

    public Color getColor1(){return this.color1;}
    public Color getColor2(){return this.color2;}

    public void setColor1(Color color1){this.color1 = color1;}
    public void setColor2(Color color2){this.color2 = color2;}
    public void resetPositionToDefault(){
        this.width = 200;
        this.height = 20;
        this.x = 80;
        this.y = 20;
    }


}