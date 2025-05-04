package PaooGame.HUD;

import Entities.Entity;
import Entities.Hero;
import java.awt.Graphics2D;

public abstract class HUD {
    protected Entity entity;

    public HUD(Entity entity) {
        this.entity = entity;
    }

    public abstract void draw(Graphics2D g2d);
}