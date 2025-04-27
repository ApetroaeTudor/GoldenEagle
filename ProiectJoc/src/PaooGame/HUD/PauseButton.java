package PaooGame.HUD;
import PaooGame.Hero.Hero;

import java.awt.*;
import java.awt.geom.Point2D;

public class PauseButton extends HUD {
    private Rectangle bounds;
    private boolean isHovered;

    public PauseButton(Hero hero, int x, int y) {
        super(hero);
        int size = 40;
        this.bounds = new Rectangle(x, y, size, size);
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Font și text
        String label = "Pause:";
        g2d.setFont(g2d.getFont().deriveFont(16f));
        FontMetrics fm = g2d.getFontMetrics();

        // Măsurăm textul pentru aliniere verticală
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();

        int textX = bounds.x - textWidth - 10; // 10px padding între text și buton
        int textY = bounds.y + (bounds.height + textHeight) / 2 - 4; // ajustare verticală fină

        // Desenează textul
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, textX, textY);

        // Fundal hover
        Color baseColor = isHovered ? new Color(220, 220, 220, 180) : new Color(255, 255, 255, 120);
        g2d.setColor(baseColor);
        g2d.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 12, 12);

        // Simbol pauză
        g2d.setColor(Color.BLACK);
        int lineWidth = 4;
        int lineHeight = 20;
        int lineY = bounds.y + (bounds.height - lineHeight) / 2;

        g2d.fillRect(bounds.x + bounds.width / 3 - lineWidth, lineY, lineWidth, lineHeight);
        g2d.fillRect(bounds.x + 2 * bounds.width / 3 - lineWidth, lineY, lineWidth, lineHeight);
    }


    public void updateHover(int mouseX, int mouseY) {
        isHovered = bounds.contains(mouseX, mouseY);
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return bounds.contains(mouseX, mouseY);
    }
}