package PaooGame.HUD;

import PaooGame.Entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageButton extends HUD{
    private boolean isHovered;
    private int x;
    private int y;
    private int buttonWidth = 50;
    private int buttonHeight = 50;
    private BufferedImage imgToDraw;

    private Rectangle bounds;

    public ImageButton(Entity entity, int x, int y, BufferedImage imgToDraw){
        super(entity);

        this.isHovered = false;
        this.x = x;
        this.y = y;
        this.imgToDraw = imgToDraw;

        bounds = new Rectangle(this.x,this.y,this.buttonWidth,this.buttonHeight);
    }

    @Override
    public void draw(Graphics2D g2d){
        Color baseColor;
        if (isHovered) {
            // Gray color with transparency (220 alpha = ~86% opaque)
            baseColor = new Color(150, 150, 150, 220);  // Medium gray
        } else {
            // Yellow-ish color with transparency (180 alpha = ~70% opaque)
            baseColor = new Color(220, 200, 100, 180);  // Light golden yellow
        }

        g2d.setColor(baseColor);
        g2d.fillRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 30, 30); // 10px corner radius

        g2d.setColor(isHovered ? Color.WHITE : Color.BLACK);
        g2d.setStroke(new BasicStroke(isHovered ? 2 : 1));
        g2d.drawRoundRect(this.x, this.y, this.buttonWidth, this.buttonHeight, 30, 30);
        g2d.setStroke(new BasicStroke(1)); // Reset stroke

        if(imgToDraw!=null){
            int imgX = this.x +10;
            int imgY = this.y +10;
            g2d.drawImage(this.imgToDraw,imgX,imgY,null);
        }

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
