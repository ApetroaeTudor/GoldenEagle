package PaooGame.HUD;

import PaooGame.Hero.Hero;
import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar extends HUD {
    private double displayedHealth;

    public HealthBar(Hero hero) {
        super(hero);
        this.displayedHealth = hero.getHealth();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (hero == null) return;

        int width = 200;
        int height = 20;
        int x = 80;
        int y = 20;

        // Animația între viața reală și cea afișată
        double targetHealth = hero.getHealth();
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
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(16f));
        g2d.drawString("Health:", 20, y + 15);

        // Fundal roșu
        g2d.setColor(Color.RED);
        g2d.fillRoundRect(x, y, width, height, 10, 10);

        // Bară verde cu gradient
        int currentWidth = (int) (width * (displayedHealth / 100.0));
        if (currentWidth > 0) {
            Color greenStart = new Color(0, 255, 0);
            Color greenEnd = new Color(0, 150, 0);
            g2d.setPaint(new java.awt.GradientPaint(x, y, greenStart, x + currentWidth, y, greenEnd));
            g2d.fillRoundRect(x, y, currentWidth, height, 10, 10);
        }

        // Contur negru
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, width, height, 10, 10);
    }
}