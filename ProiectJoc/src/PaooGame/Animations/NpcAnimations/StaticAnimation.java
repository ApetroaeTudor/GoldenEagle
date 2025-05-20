package PaooGame.Animations.NpcAnimations;

import PaooGame.Animations.Animation;
import java.awt.*;
import java.awt.image.BufferedImage;

public class StaticAnimation extends Animation {
    private BufferedImage frame;

    public StaticAnimation(BufferedImage frame) {
        this.frame = frame; // Stochează un singur cadru
        this.imgWidth = frame.getWidth();
        this.imgHeight = frame.getHeight();
    }

    @Override
    public void loadAnimation() {} // Nu este necesară încărcarea

    @Override
    public void updateAnimation() {} // Nu există actualizări (e static)

    @Override
    public void triggerOnce() {} // Nu se aplică

    @Override
    public void paintAnimation(Graphics g, int x, int y, boolean flipped, double scale) {
        int drawWidth = (int)(imgWidth * scale);
        int drawHeight = (int)(imgHeight * scale);

        if (flipped) {
            // Desenează inversat orizontal
            g.drawImage(frame, x + drawWidth, y, -drawWidth, drawHeight, null);
        } else {
            g.drawImage(frame, x, y, drawWidth, drawHeight, null);
        }
    }
}