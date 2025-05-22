package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Entities.Hero;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @class ShopState
 * @brief Represents the in-game shop state.
 *
 * This class extends the abstract {@link State} class and defines the
 * behavior and rendering for a shop menu. It allows the player to purchase
 * items, such as health potions, using in-game currency (gold). The shop
 * appears as an overlay on top of the Level 2 game screen.
 */
public class ShopState extends State {
    private Rectangle buyButton;                            ///< Rectangle defining the "BUY" button area.
    private Rectangle exitButton;                           ///< Rectangle defining the "EXIT" button area.
    private BufferedImage potionImage;                      ///< Image for the health potion item displayed in the shop.
    private Color panelColor = new Color(30, 30, 30, 220);  ///< Color for the semi-transparent background panel of the shop.
    private Color borderColor = new Color(70, 130, 180);    ///< Color for the border of the shop buttons and panel.
    private Font titleFont = new Font("Arial", Font.BOLD, 42); ///< Font used for the "SHOP" title.
    protected String stateName = "SHOP_STATE";              ///< The name identifier for this state.

    /**
     * @brief Constructs a ShopState object.
     *
     * Initializes the shop interface, including the "BUY" and "EXIT" buttons,
     * and loads the image for the potion item. The shop panel and buttons are
     * positioned centrally on the screen.
     *
     * @param refLink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
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
            e.printStackTrace(); // Consider more robust error handling or logging
        }
    }

    /**
     * @brief Updates the logic for the ShopState.
     *
     * This method is called repeatedly in the game loop. It handles:
     * - Checking for mouse clicks on the "BUY" and "EXIT" buttons.
     * - "BUY": If the hero has enough gold, purchases a potion (restores health and deducts gold).
     * - "EXIT": Closes the shop and returns the game to Level 2.
     * This method overrides the {@link State#update()} method.
     */
    @Override
    public void update() {
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (exitButton.contains(mx, my)) {
                State.setState(reflink.getGame().getLevel2State()); // Assumes shop is accessed from Level 2
                mouse.mouseReleased(null); // Reset mouse press state
            } else if (buyButton.contains(mx, my)) {
                Hero hero = reflink.getHero();
                if (hero.getGold() >= 50) { // Check if has enough money
                    hero.restoreHealth(25); // Restore 25% health
                    hero.setGold(hero.getGold() - 50); // Deduct 50 money
                }
                mouse.mouseReleased(null); // Reset mouse press state
            }
        }
    }

    /**
     * @brief Draws the ShopState to the screen.
     *
     * This method is responsible for rendering all visual elements of the shop:
     * - First, it draws the underlying game level (Level 2) that was active when the shop was opened.
     * - Then, it draws a semi-transparent dark overlay on top of the game.
     * - A central panel is drawn for the shop interface.
     * - The "SHOP" title and the hero's current gold amount are displayed.
     * - The image of the potion item is shown.
     * - The "BUY" and "EXIT" buttons are drawn with hover effects.
     * This method overrides the {@link State#draw(Graphics)} method.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    @Override
    public void draw(Graphics g) {

        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        // Draw the underlying game state
        if (reflink.getGame().getLevel2State() != null) {
            reflink.getGame().getLevel2State().draw(g);
        }

        Graphics2D g2d = (Graphics2D) g;

        // Dark overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, reflink.getWidth(), reflink.getHeight());


        // Main panel
        int panelWidth = 400;
        int panelHeight = 400;
        int panelX = reflink.getWidth()/2 - panelWidth/2;
        int panelY = 100;

        g2d.setColor(panelColor);
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        // Title
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "SHOP";
        FontMetrics fmTitle = g2d.getFontMetrics(titleFont);
        int titleWidth = fmTitle.stringWidth(title);
        g2d.drawString(title, panelX + (panelWidth - titleWidth)/2, panelY + 60);

        // Gold display
        int gold = reflink.getHero().getGold();
        Font goldFont = new Font("Arial", Font.PLAIN, 20);
        g2d.setFont(goldFont);
        String goldText = "Gold: " + gold + "G";
        FontMetrics fmGold = g2d.getFontMetrics(goldFont);
        int goldTextWidth = fmGold.stringWidth(goldText);
        g2d.drawString(goldText, panelX + (panelWidth - goldTextWidth)/2, panelY + 90);

        // Potion image
        if (potionImage != null) {
            g.drawImage(potionImage, panelX + 150, panelY + 100, 100, 100, null);
        }

        // Buttons
        drawModernButton(g2d, buyButton, "BUY (50G)", mx, my);
        drawModernButton(g2d, exitButton, "EXIT", mx, my);
    }

    /**
     * @brief Helper method to draw a shop button with text and hover effect.
     *
     * Draws a rounded rectangle for the button. The button's background color
     * changes if the mouse cursor (mx, my) is hovering over it. The specified
     * text is centered within the button.
     *
     * @param g2d The {@link Graphics2D} context used for drawing.
     * @param rect The {@link Rectangle} defining the button's bounds.
     * @param text The {@link String} text to display on the button.
     * @param mx The current x-coordinate of the mouse.
     * @param my The current y-coordinate of the mouse.
     */
    private void drawModernButton(Graphics2D g2d, Rectangle rect, String text, int mx, int my) {
        boolean hover = rect.contains(mx, my);
        g2d.setColor(hover ? borderColor.brighter() : borderColor);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2d.setColor(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(buttonFont);
        FontMetrics fm = g2d.getFontMetrics(buttonFont);
        int textWidth = fm.stringWidth(text);
        // Centering text
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, textX, textY);
    }


    /**
     * @brief Gets the name of this state.
     * This method overrides the {@link State#getStateName()} method.
     * @return The string name of the state.
     */
    @Override
    public String getStateName() { return stateName; }

    /**
     * @brief Sets an enemy for this state.
     * This method overrides the {@link State#setEnemy(Enemy)} method.
     * @param enemy The {@link Enemy} to be set.
     */
    @Override
    public void setEnemy(Enemy enemy) {
    }


    /**
     * @brief Restores the state of the shop.
     * This method overrides the {@link State#restoreState()} method.
     */
    @Override public void restoreState() {
    }

    /**
     * @brief Loads state specific to the shop.
     * This method overrides the {@link State#loadState(boolean)} method.
     * @param access Boolean indicating access permission.
     */
    @Override
    public void loadState(boolean access) {
    }

    /**
     * @brief Stores state specific to the shop.
     * This method overrides the {@link State#storeState(boolean)} method.
     * @param access Boolean indicating access permission.
     */
    @Override
    public void storeState(boolean access) {
    }
}