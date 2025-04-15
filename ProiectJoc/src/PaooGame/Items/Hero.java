package PaooGame.Items;

import java.awt.*;
import java.awt.image.BufferedImage;

import PaooGame.RefLinks;
import PaooGame.Graphics.Assets;
import PaooGame.Animations.Animation;

public class Hero extends Character {

    private Animation animUp, animLeft, animDown, animRight;
    private Animation jumpUp, jumpRight, jumpLeft, jumpDown;
    private BufferedImage currentFrame;

    private boolean isJumping = false;
    private float jumpVelocity = 0;
    private final float JUMP_FORCE = -10f;
    private final float GRAVITY = 0.5f;

    private boolean jumpKeyPressed = false;

    public Hero(RefLinks refLink, float x, float y) {
        super(refLink, x, y, Character.DEFAULT_CREATURE_WIDTH, Character.DEFAULT_CREATURE_HEIGHT);

        animUp = new Animation(100, Assets.heroWalks[0]);
        animLeft = new Animation(100, Assets.heroWalks[1]);
        animDown = new Animation(100, Assets.heroWalks[2]);
        animRight = new Animation(100, Assets.heroWalks[3]);

        jumpUp = new Animation(100, Assets.heroJump[2]);
        jumpRight = new Animation(100, Assets.heroJump[3]);
        jumpDown = new Animation(100, Assets.heroJump[0]);
        jumpLeft = new Animation(100, Assets.heroJump[1]);

        normalBounds.x = 16;
        normalBounds.y = 16;
        normalBounds.width = 16;
        normalBounds.height = 32;

        attackBounds.x = 10;
        attackBounds.y = 10;
        attackBounds.width = 38;
        attackBounds.height = 38;
    }

    @Override
    public void Update() {
        animUp.Update();
        animDown.Update();
        animLeft.Update();
        animRight.Update();
        jumpUp.Update();
        jumpLeft.Update();
        jumpRight.Update();

        GetInput();

        // Permite mișcarea în aer
        x += xMove;
        ApplyJumpPhysics();

        // Selectează cadrul curent
        if (isJumping) {
            if (xMove < 0)
                currentFrame = jumpLeft.getCurrentFrame();
            else if (xMove > 0)
                currentFrame = jumpRight.getCurrentFrame();
            else
                currentFrame = jumpUp.getCurrentFrame();
        } else {
            if (xMove < 0)
                currentFrame = animLeft.getCurrentFrame();
            else if (xMove > 0)
                currentFrame = animRight.getCurrentFrame();
            else
                currentFrame = Assets.heroWalks[2][0]; // idle down
        }
    }

    private void GetInput() {
        xMove = 0;

        if (refLink.GetKeyManager().left)
            xMove = -speed;
        if (refLink.GetKeyManager().right)
            xMove = speed;

        // Săritura – permite doar când e pe sol
        if (refLink.GetKeyManager().space) {
            if (!jumpKeyPressed && !isJumping) {
                isJumping = true;
                jumpVelocity = JUMP_FORCE;
                jumpKeyPressed = true;
            }
        } else {
            jumpKeyPressed = false;
        }
    }

    private void ApplyJumpPhysics() {
        // Aplica gravitatia in orice moment daca nu e pe sol
        if (y < 470 || isJumping) {
            isJumping = true;
            y += jumpVelocity;
            jumpVelocity += GRAVITY;

            // Daca a atins solul
            if (y >= 470) {
                y = 470;
                isJumping = false;
                jumpVelocity = 0;
            }
        }
    }



    @Override
    public void Draw(Graphics g) {
        g.drawImage(currentFrame, (int) x, (int) y, width, height, null);

        // Debug coliziuni:
        // g.setColor(Color.BLUE);
        // g.fillRect((int)(x + bounds.x), (int)(y + bounds.y), bounds.width, bounds.height);
    }
}
