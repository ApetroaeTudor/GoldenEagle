package PaooGame.HUD;

import PaooGame.Entities.Entity;
import PaooGame.Entities.Hero;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Gold extends HUD {
    public Gold(Entity entity) {
        super(entity);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (entity == null || !(entity instanceof Hero)) return;
        Hero hero = (Hero) entity;
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.YELLOW);
        String goldText = "Gold: " + hero.getGold();
        g2d.drawString(goldText, 20, 110);
    }
}