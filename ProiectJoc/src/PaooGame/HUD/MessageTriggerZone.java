package PaooGame.HUD;

import PaooGame.Entities.Entity;
import java.awt.Rectangle;

public class MessageTriggerZone {
    private Entity entity;
    private Rectangle bounds;
    private String message;
    private boolean isActive;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

    public MessageTriggerZone(Entity entity, int offsetX, int offsetY, int width, int height, String message) {
        this.entity = entity;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.message = message;
        this.isActive = true;
        updateBounds(); // Inițializează bounds
    }

    public MessageTriggerZone(int x, int y, int width, int height, String message) {
        this.bounds = new Rectangle(x, y, width, height); // Ignoră entitatea
        this.message = message;
        this.isActive = true;
    }


    public void updateBounds() {
        if (entity != null) { // Actualizează doar dacă există entitate
            int x = (int) entity.getHitbox().getX() + offsetX;
            int y = (int) entity.getHitbox().getY() + offsetY;
            this.bounds = new Rectangle(x, y, width, height);
        }
    }

    public boolean checkTrigger(Rectangle target) {
        boolean isTriggered = isActive && bounds.intersects(target);
        if (isTriggered) {
//            System.out.println("Trigger activat: " + message);
        }
        return isTriggered;
    }

    // Restul metodelor existente...
    public String getMessage() { return message; }
    public boolean isActive() { return isActive; }
    public Rectangle getBounds() { return bounds; }

    public boolean hasEntity() {
        return entity != null;
    }

}