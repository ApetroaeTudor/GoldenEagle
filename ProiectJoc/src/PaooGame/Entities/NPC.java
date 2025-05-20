package PaooGame.Entities;

import PaooGame.Animations.Animation;
import PaooGame.Animations.NpcAnimations.StaticAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NPC extends Entity {
    private StaticAnimation passiveAnimation;
    private StaticAnimation activeAnimation;
    private boolean isActive;

    private final double scale = 0.7;

    public NPC(RefLinks refLinks, int x, int y) {
        super(refLinks, x, y);
        this.health = 100;
        this.damage = 0;
        this.isActive = true;

        BufferedImage spriteSheet = loadImage(Constants.GOBLIN_SPRITE_SHEET_PATH);

        // Taie cele două stări din spriteSheet
        BufferedImage activeSprite = spriteSheet.getSubimage(
                0, 0,
                Constants.GOBLIN_BOUND_TILE_WIDTH,
                Constants.GOBLIN_BOUND_TILE_HEIGHT
        );

        BufferedImage passiveSprite = spriteSheet.getSubimage(
                Constants.GOBLIN_BOUND_TILE_WIDTH, 0,
                Constants.GOBLIN_PASSIVE_TILE_WIDTH,
                Constants.GOBLIN_PASSIVE_TILE_HEIGHT
        );

        // Creează animațiile statice
        activeAnimation = new StaticAnimation(activeSprite);
        passiveAnimation = new StaticAnimation(passiveSprite);

        // Setează hitbox-ul inițial (activ)
        this.hitbox = new Hitbox(
                (int)(x - 10),
                (int)y,
                (int)((Constants.GOBLIN_BOUND_TILE_WIDTH + 10) * scale),
                (int)(Constants.GOBLIN_BOUND_TILE_HEIGHT * scale)
        );
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Eroare la încărcarea imaginii: " + path);
            return null;
        }
    }

    @Override
    public void update() {
        if (isActive && reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_E)) {
            if (this.reflink.getHero().getHitbox().intersects(this.hitbox)) {
                isActive = false;
                // Actualizează hitbox-ul pentru starea "passive"
                this.hitbox = new Hitbox(
                        (int)(x - 10),
                        (int)y,
                        (int)((Constants.GOBLIN_PASSIVE_TILE_WIDTH + 10) * scale),
                        (int)(Constants.GOBLIN_PASSIVE_TILE_HEIGHT * scale)
                );
            }
        }
    }

    @Override
    protected void moveAndCollide() {
        // Static NPC
    }

    @Override
    protected void updateVisualPosition() {
        // Static
    }

    @Override
    protected Animation getAnimationByState() {
        return isActive ? activeAnimation : passiveAnimation;
    }

    @Override
    protected void updateAnimationState() {
        // Static
    }

    @Override
    public String getName() {
        return Constants.GOBLIN_NAME;
    }

    @Override
    public void restoreEntity() {
        this.isActive = false;
    }

    @Override
    public void attack() {
        // Nu atacă
    }

    @Override
    public String getSource() {
        return Constants.GOBLIN_SPRITE_SHEET_PATH;
    }

    // ✅ Desenare cu SCALARE
    @Override
    public void draw(Graphics g) {
        int drawX = (int) x;
        int drawY = (int) y;
        getAnimationByState().paintAnimation(g, drawX, drawY, false, scale);
    }

    public boolean isActive() {
        return isActive;
    }
}