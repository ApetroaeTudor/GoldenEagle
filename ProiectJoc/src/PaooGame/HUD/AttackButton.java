package PaooGame.HUD;

import PaooGame.Entities.Entity;

import java.awt.*;

public class AttackButton extends HUD {
    private boolean isHovered;
    private int x;
    private int y;
    private int buttonWidth = 500;
    private int buttonHeight = 70;

    private Rectangle bounds;

    public AttackButton(Entity entity, int x, int y) {
        super(entity);
        this.isHovered = false;
        this.x = x;
        this.y = y;
        bounds = new Rectangle(this.x,this.y,this.buttonWidth,this.buttonHeight);
    }

    @Override
    public void draw(Graphics2D g2d) {
        // --- Button Background ---
        Color baseColor;
        if (isHovered) {
            baseColor = new Color(190, 50, 50, 220);
        } else {
            baseColor = new Color(220, 80, 80, 180);
        }

        g2d.setColor(baseColor);
        // Use fillRoundRect for rounded corners
        g2d.fillRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 10, 10); // 10px corner radius

        // --- Button Text ---
        String label = "Attack";
        g2d.setFont(new Font("Arial", Font.BOLD, 35)); // Choose font
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);


        int textX = this.x + (this.buttonWidth - textWidth) / 2;
        // Center vertically: start y + half button height + half text height (approx) - adjust baseline offset
        int textY = this.y + (this.buttonHeight - fm.getHeight()) / 2 + fm.getAscent();

        // Draw the text shadow (optional, for better visibility)
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(label, textX + 1, textY + 1);

        // Draw the main text
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, textX, textY);

        // --- Button Border ---
        g2d.setColor(isHovered ? Color.WHITE : Color.BLACK); // Border changes on hover
        g2d.setStroke(new BasicStroke(isHovered ? 2 : 1)); // Thicker border on hover
        g2d.drawRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 10, 10);
        g2d.setStroke(new BasicStroke(1)); // Reset stroke
    }

    public void updateHover(int mouseX, int mouseY) {
        isHovered = bounds.contains(mouseX, mouseY);
    }

    public boolean isClicked(int mouseX, int mouseY, boolean mousePressed) {
        return mousePressed && bounds.contains(mouseX, mouseY);
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isHovered() {
        return isHovered;
    }
    public void setIsHovered(boolean isHovered) {this.isHovered = isHovered;}
}