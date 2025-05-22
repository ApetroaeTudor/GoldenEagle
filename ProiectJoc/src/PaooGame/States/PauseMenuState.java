package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Config.Constants;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;
import java.awt.*;

/**
 * @class PauseMenuState
 * @brief Represents the pause menu state of the game.
 *
 * This class extends the abstract {@link State} class and defines the
 * behavior and rendering for the pause menu. It appears as an overlay
 * on top of the currently running game level, providing options to continue
 * the game, return to the main menu, or quit the application.
 */
public class PauseMenuState extends State {
    private Rectangle continueButton;                       ///< Rectangle defining the "CONTINUE" button area.
    private Rectangle exitToMenuButton;                     ///< Rectangle defining the "MAIN MENU" button area.
    private Rectangle exitButton;                           ///< Rectangle defining the "QUIT GAME" button area.
    private Color panelColor = new Color(30, 30, 30, 220);  ///< Color for the semi-transparent background panel.
    private Color borderColor = new Color(70, 130, 180);    ///< Color for the border of the background panel.
    private Font titleFont = new Font("Arial", Font.BOLD, 42); ///< Font used for the "PAUSED" title.

    protected String stateName = Constants.PAUSE_MENU_STATE; ///< The name identifier for this state.

    /**
     * @brief Constructs a PauseMenuState object.
     *
     * Initializes the pause menu buttons (Continue, Main Menu, Quit Game)
     * by defining their positions and dimensions within a central panel.
     *
     * @param refLink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public PauseMenuState(RefLinks refLink) {
        super(refLink);
        int panelWidth = 400;
        int centerX = refLink.getWidth()/2 - panelWidth/2;
        int startY = 150;

        // Centered buttons
        continueButton = new Rectangle(centerX + 50, startY + 100, 300, 50);
        exitToMenuButton = new Rectangle(centerX + 50, startY + 170, 300, 50);
        exitButton = new Rectangle(centerX + 50, startY + 240, 300, 50);
    }

    /**
     * @brief Gets the name of this state.
     * This method overrides the {@link State#getStateName()} method.
     * @return The string name of the state, typically defined in {@link PaooGame.Config.Constants#PAUSE_MENU_STATE}.
     */
    @Override
    public String getStateName(){
        return stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is part of the {@link State} interface.
     * @param enemy The {@link Enemy}
     */
    @Override
    public void setEnemy(Enemy enemy) {

    }

    /**
     * @brief Updates the logic for the PauseMenuState.
     *
     * This method is called repeatedly in the game loop. It handles:
     * - Checking for mouse clicks on the "CONTINUE", "MAIN MENU", and "QUIT GAME" buttons.
     * - "CONTINUE": Resumes the game by setting the state back to the currently running level.
     * - "MAIN MENU": Transitions the game to the {@link MenuState}.
     * - "QUIT GAME": Exits the application.
     * This method overrides the {@link State#update()} method.
     */
    @Override
    public void update() {
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (continueButton.contains(mx, my)) {
                State.setState(this.reflink.getCurrentRunningLevel());
                mouse.mouseReleased(null); // Reset mouse press state
            } else if (exitToMenuButton.contains(mx, my)) {
                State.setState(reflink.getGame().getMenuState());
                mouse.mouseReleased(null); // Reset mouse press state
            } else if (exitButton.contains(mx, my)) {
                System.exit(0);
            }
        }
    }

    /**
     * @brief Draws the PauseMenuState to the screen.
     *
     * This method is responsible for rendering all visual elements of the pause menu:
     * - First, it draws the underlying game level that was paused.
     * - Then, it draws a semi-transparent dark overlay on top of the game.
     * - A central panel with a border is drawn for the menu itself.
     * - The "PAUSED" title is displayed within this panel.
     * - The "CONTINUE", "MAIN MENU", and "QUIT GAME" buttons are drawn with hover effects.
     * This method overrides the {@link State#draw(Graphics)} method.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        // Draw the underlying paused game state first
        if (reflink.getCurrentRunningLevel() != null) {
            reflink.getCurrentRunningLevel().draw(g);
        }


        Graphics2D g2d = (Graphics2D) g;

        // Dark overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, reflink.getWidth(), reflink.getHeight());

        // Main panel
        int panelWidth = 400;
        int panelHeight = 350;
        int panelX = reflink.getWidth()/2 - panelWidth/2;
        int panelY = 100;

        // Panel Background
        g2d.setColor(panelColor);
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        // Panel Border
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        // Title
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "PAUSED";
        FontMetrics fmTitle = g2d.getFontMetrics(titleFont);
        int titleWidth = fmTitle.stringWidth(title);
        g2d.drawString(title, panelX + (panelWidth - titleWidth)/2, panelY + 60);

        // Buttons
        int mx = reflink.getMouseInput().getMouseX();
        int my = reflink.getMouseInput().getMouseY();
        drawModernButton(g2d, continueButton, "CONTINUE", mx, my);
        drawModernButton(g2d, exitToMenuButton, "MAIN MENU", mx, my);
        drawModernButton(g2d, exitButton, "QUIT GAME", mx, my);
    }

    /**
     * @brief Restores the state of the pause menu.
     *
     */
    @Override
    public void restoreState() {
    }

    /**
     * @brief Loads state specific to the pause menu.
     *
     * This method is part of the {@link State} interface. For the PauseMenuState,
     * there is typically no specific state to load.
     * @param access
     */
    @Override
    public void loadState(boolean access) {
    }

    /**
     * @brief Stores state specific to the pause menu.
     *
     * This method is part of the {@link State} interface. For the PauseMenuState,
     * there is typically no specific state to store.
     * @param access
     */
    @Override
    public void storeState(boolean access) {
    }

    /**
     * @brief Helper method to draw a modern-style button with text, gradient, and hover effect.
     *
     * Draws a rounded rectangle for the button with a gradient background.
     * The button's appearance (gradient and border) changes if the mouse
     * cursor (mx, my) is hovering over it. The specified text is centered
     * within the button.
     *
     * @param g2d The {@link Graphics2D} context used for drawing.
     * @param rect The {@link Rectangle} defining the button's bounds.
     * @param text The {@link String} text to display on the button.
     * @param mx The current x-coordinate of the mouse.
     * @param my The current y-coordinate of the mouse.
     */
    private void drawModernButton(Graphics2D g2d, Rectangle rect, String text, int mx, int my) {
        boolean hover = rect.contains(mx, my);

        // Gradient background
        GradientPaint gp = new GradientPaint(
                rect.x, rect.y,
                hover ? new Color(70, 130, 180) : new Color(60, 60, 60), // Top color
                rect.x, rect.y + rect.height,
                hover ? new Color(50, 100, 150) : new Color(40, 40, 40)  // Bottom color
        );

        g2d.setPaint(gp);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Border
        g2d.setColor(hover ? new Color(160, 200, 255) : new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Text
        Font buttonFont = new Font("Arial", Font.BOLD, 22); // Define button font
        g2d.setFont(buttonFont);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics(buttonFont); // Get metrics for the correct font
        int textX = rect.x + (rect.width - fm.stringWidth(text))/2;
        // Better vertical centering for text
        int textY = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, textX, textY);
    }
}