package PaooGame.HUD;

import Entities.Hero;
import java.awt.Graphics2D;

public abstract class HUD {
    protected Hero hero;

    public HUD(Hero hero) {
        this.hero = hero;
    }

    public abstract void draw(Graphics2D g2d);
}