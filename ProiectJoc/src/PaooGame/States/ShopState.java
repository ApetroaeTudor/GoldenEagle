package PaooGame.States;

import PaooGame.Config.Constants;
import PaooGame.Entities.Enemy;
import PaooGame.Entities.Hero;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShopState extends State {
    private Rectangle buyButton, exitButton;
    private BufferedImage potionImage;
    private Color panelColor = new Color(30, 30, 30, 220);
    private Color borderColor = new Color(70, 130, 180);
    private Font titleFont = new Font("Arial", Font.BOLD, 42);
    protected String stateName = "SHOP_STATE";

    public ShopState(RefLinks refLink) {
        super(refLink);
        int panelWidth = 400;
        int centerX = refLink.getWidth()/2 - panelWidth/2;
        int startY = 150;

        buyButton = new Rectangle(centerX + 50, startY + 200, 300, 50);
        exitButton = new Rectangle(centerX + 50, startY + 270, 300, 50);

        try {
            potionImage = ImageIO.read(new File("res/Items/potion.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (exitButton.contains(mx, my)) {
                State.setState(reflink.getGame().getLevel2State());
                mouse.mouseReleased(null);
            } else if (buyButton.contains(mx, my)) {
                Hero hero = reflink.getHero();
                if (hero.getGold() >= 50) { // Verifică dacă are suficienți bani
                    hero.restoreHealth(25); // Restaurează 25% viață
                    hero.setGold(hero.getGold() - 50); // Scade 50 de bani
                }
                mouse.mouseReleased(null);
            }
        }
    }

    @Override
    public void draw(Graphics g) {

        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();


        if (reflink.getGame().getLevel2State() != null) {
            reflink.getGame().getLevel2State().draw(g);
        }

        Graphics2D g2d = (Graphics2D) g;

        // Overlay întunecat
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, reflink.getWidth(), reflink.getHeight());


        // Panou principal
        int panelWidth = 400;
        int panelHeight = 400;
        int panelX = reflink.getWidth()/2 - panelWidth/2;
        int panelY = 100;

        g2d.setColor(panelColor);
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        // Titlu
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "SHOP";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelX + (panelWidth - titleWidth)/2, panelY + 60);

        int gold = reflink.getHero().getGold(); // sau getMoney(), dupa caz
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        String goldText = "Gold: " + gold + "G";
        int goldTextWidth = g2d.getFontMetrics().stringWidth(goldText);
        g2d.drawString(goldText, panelX + (panelWidth - goldTextWidth)/2, panelY + 90);

        // Imagine potiune
        if (potionImage != null) {
            g.drawImage(potionImage, panelX + 150, panelY + 100, 100, 100, null);
        }

        // Butoane
        drawModernButton(g2d, buyButton, "BUY (50G)", mx, my);
        drawModernButton(g2d, exitButton, "EXIT", mx, my);
    }

    private void drawModernButton(Graphics2D g2d, Rectangle rect, String text, int mx, int my) {
        boolean hover = rect.contains(mx, my);
        g2d.setColor(hover ? borderColor.brighter() : borderColor);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2d.drawString(text, rect.x + (rect.width - textWidth) / 2, rect.y + (rect.height + textHeight) / 2 - 5);
    }


    @Override
    public String getStateName() { return stateName; }

    @Override
    public void setEnemy(Enemy enemy) {

    }



    @Override public void restoreState() {}

    @Override
    public void loadState(boolean access) {

    }

    @Override
    public void storeState(boolean access) {

    }
}