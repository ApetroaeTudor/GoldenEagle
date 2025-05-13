package PaooGame.HUD;

import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Hero;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;

public class ContextHUD extends HUD {
    private ArrayList<MessageTriggerZone> triggers = new ArrayList<>();
    private String currentMessage = "";
    private int messageDuration = 100; // 2 secunde la 60 FPS
    private int messageTimer = 0;
    private Point currentMessagePosition = new Point();

    public ContextHUD(Hero hero) {
        super(hero);
    }


    public void addTrigger(MessageTriggerZone trigger) {
        triggers.add(trigger);
    }

    public void update() {
        // Actualizează bounds-urile pentru toate trigger-ele
        for (MessageTriggerZone trigger : triggers) {
            if (trigger.hasEntity()) { // Adaugă o metodă în MessageTriggerZone
                trigger.updateBounds();
            }
        }

        // Verifică coliziunea cu jucătorul
        Rectangle heroBounds = new Rectangle(
                (int) entity.getHitbox().getX(),
                (int) entity.getHitbox().getY(),
                (int) entity.getHitbox().getWidth(),
                (int) entity.getHitbox().getHeight()
        );

        // Resetează mesajul
        currentMessage = "";
        for (MessageTriggerZone trigger : triggers) {
            if (trigger.checkTrigger(heroBounds)) {
                currentMessage = trigger.getMessage();
                Rectangle triggerBounds = trigger.getBounds();
                currentMessagePosition.setLocation(
                        triggerBounds.x + triggerBounds.width / 2,
                        triggerBounds.y - 20
                );
                messageTimer = messageDuration;
                break; // Afișează doar primul mesaj activ (opțional)
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!currentMessage.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics metrics = g2d.getFontMetrics();

            int padding = 10;
            int textWidth = metrics.stringWidth(currentMessage);
            int textHeight = metrics.getHeight();

            // Poziție fixă sus pe ecran
            int boxX = Constants.WINDOW_WIDTH / 2 - textWidth / 2 - padding;
            int boxY = 120;
            int boxWidth = textWidth + padding * 2;
            int boxHeight = textHeight + padding;

            // Fundal semitransparent
            g2d.setColor(new Color(0, 0, 0, 170));
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            // Contur
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            // Textul
            g2d.setColor(Color.YELLOW);
            int textX = boxX + padding;
            int textY = boxY + textHeight - 5;
            g2d.drawString(currentMessage, textX, textY);
        }
    }


}