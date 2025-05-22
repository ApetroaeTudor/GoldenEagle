package PaooGame.States;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;
import PaooGame.Entities.Enemy;
import PaooGame.Config.Constants;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class MenuState
 * @brief Represents the main menu state of the game.
 *
 * This class extends the abstract {@link State} class and defines the
 * behavior and rendering for the main menu. It includes buttons for starting
 * a new game, continuing a saved game, and quitting the application. It also
 * features an animated background.
 */
public class MenuState extends State  {
    private Rectangle startButton;                          ///< Rectangle defining the "New Game" button area.
    private Rectangle continueButton;                       ///< Rectangle defining the "Continue" button area.
    private Rectangle quitButton;                           ///< Rectangle defining the "Quit" button area.
    private StaticItemAnimation bgAnimation;                ///< Animation for the main menu background.

    private int buttonHeight = 70;                          ///< Default height for the menu buttons.
    private int topOffset = 300;                            ///< Vertical offset from the top of the screen for the first button.

    protected String stateName = Constants.MENU_STATE;      ///< The name identifier for this state.

    /**
     * @brief Constructs a MenuState object.
     *
     * Initializes the menu buttons (Start, Continue, Quit) by defining their
     * positions and dimensions based on the screen size. It also loads and
     * prepares the background animation using {@link StaticItemAnimation}.
     *
     * @param refLink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public MenuState(RefLinks refLink) {
        super(refLink);

        int centerX = refLink.getWidth() / 2 -100 ;
        startButton = new Rectangle(centerX, topOffset, 200, 50);
        continueButton = new Rectangle(centerX,topOffset+buttonHeight,200,50);
        quitButton = new Rectangle(centerX, topOffset+buttonHeight*2, 200, 50);

        this.bgAnimation = new StaticItemAnimation(this.reflink,Constants.MAIN_MENU_BG_PATH,Constants.MAIN_MENU_BG_FRAME_NR,5,Constants.MAIN_MENU_BG_IMG_WIDTH,Constants.MAIN_MENU_BG_IMG_HEIGHT);
        this.bgAnimation.loadAnimation();
    }

    /**
     * @brief Gets the name of this state.
     * This method overrides the {@link State#getStateName()} method.
     * @return The string name of the state, defined in {@link PaooGame.Config.Constants#MENU_STATE}.
     */
    @Override
    public String getStateName(){
        return stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is part of the {@link State} interface but is not used
     * in the MenuState as menus do not involve enemies.
     * This method overrides the {@link State#setEnemy(Enemy)} method.
     * @param enemy The {@link Enemy}
     */
    @Override
    public void setEnemy(Enemy enemy) {

    }

    /**
     * @brief Updates the logic for the MenuState.
     *
     * This method is called repeatedly in the game loop. It handles:
     * - Updating the background animation.
     * - Checking for mouse clicks on the "New Game", "Continue", and "Quit" buttons.
     * - "New Game": Resets the game buffer and loads the appropriate level based on saved state (defaulting to level 1 if no state).
     * - "Continue": Loads the game state from the buffer and transitions to the saved level.
     * - "Quit": Exits the application.
     * This method overrides the {@link State#update()} method.
     */
    @Override
    public void update() {
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();
        this.bgAnimation.updateAnimation();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (startButton.contains(mx, my)) {
                try{
                    this.reflink.getDataProxy().resetBuffer(true);
                    // The following load is to determine the starting level for a "New Game"
                    // which might be configurable or always level 1.
                    switch (this.reflink.getDataProxy().load(Constants.CURRENT_STATE,true)){
                        case 1:
                            this.reflink.setDataRefreshSignal(true);
                            State.setState(this.reflink.getGame().getLevel1State());
                            break;
                        case 2:
                            this.reflink.setDataRefreshSignal(true);
                            State.setState(this.reflink.getGame().getLevel2State());
                            break;
                        case 3:
                            this.reflink.setDataRefreshSignal(true);
                            State.setState(this.reflink.getGame().getLevel3State());
                            break;
                        default: // Default to level 1 for a truly new game if current_state is invalid
                            this.reflink.setDataRefreshSignal(true);
                            this.reflink.getGame().getLevel1State().restoreState();
                            State.setState(this.reflink.getGame().getLevel1State());
                            break;
                    }
                } catch (AccessNotPermittedException | ValueStoreException | DataBufferNotReadyException e) {
                    System.err.println(e.getMessage());
                    // Fallback for new game if data access fails, go to Level 1
                    this.reflink.setDataRefreshSignal(true);
                    this.reflink.getGame().getLevel1State().restoreState();
                    State.setState(this.reflink.getGame().getLevel1State());
                }
            }
            else if (quitButton.contains(mx, my)) {
                System.exit(0);
            }
            else if(continueButton.contains(mx,my)) {
                try{
                    this.reflink.getDataProxy().resetBuffer(true); // Clears current runtime buffer
                    this.reflink.getDataProxy().loadBuffer(true);  // Loads persisted data into runtime buffer
                    this.reflink.setDataRefreshSignal(true);       // Signals levels to reload their state from buffer


                    switch (this.reflink.getDataProxy().load(Constants.CURRENT_STATE,true)){
                        case 1:
                            State.setState(this.reflink.getGame().getLevel1State());
                            break;
                        case 2:
                            State.setState(this.reflink.getGame().getLevel2State());
                            break;
                        case 3:
                            State.setState(this.reflink.getGame().getLevel3State());
                            break;
                        default:
                            break;
                    }
                } catch (AccessNotPermittedException | ValueStoreException | DataBufferNotReadyException e) {
                    System.err.println(e.getMessage());
                }
            }
            mouse.mouseReleased(null);
        }
    }

    /**
     * @brief Draws the MenuState to the screen.
     *
     * This method is responsible for rendering all visual elements of the main menu,
     * including:
     * - Drawing the animated background.
     * - Drawing the "New Game", "Continue", and "Quit" buttons, with hover effects.
     * This method overrides the {@link State#draw(Graphics)} method.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        this.bgAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);

        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        g.setFont(new Font("Arial", Font.BOLD, 26));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawButton(g2d, startButton, "New Game", mx, my);
        drawButton(g2d,continueButton,"Continue",mx,my);
        drawButton(g2d, quitButton, "Quit", mx, my);
    }

    /**
     * @brief Restores the state of the menu.
     *
     * This method is part of the {@link State} interface. For the MenuState,
     * there is typically no complex state to restore, so this method is empty.
     * This method overrides the {@link State#restoreState()} method.
     */
    @Override
    public void restoreState() {
    }

    /**
     * @brief Loads state specific to the menu.
     *
     * This method is part of the {@link State} interface.
     * @param access Boolean indicating access permission (unused).
     */
    @Override
    public void loadState(boolean access) {
    }

    /**
     * @brief Stores state specific to the menu.
     *
     * This method is part of the {@link State} interface.
     * @param access
     */
    @Override
    public void storeState(boolean access) {
    }

    /**
     * @brief Helper method to draw a menu button with text and hover effect.
     *
     * Draws a rounded rectangle for the button. The button's appearance changes
     * if the mouse cursor (mx, my) is hovering over it. The specified text is
     * centered within the button.
     *
     * @param g2d The {@link Graphics2D} context used for drawing.
     * @param button The {@link Rectangle} defining the button's bounds.
     * @param text The {@link String} text to display on the button.
     * @param mx The current x-coordinate of the mouse.
     * @param my The current y-coordinate of the mouse.
     */
    private void drawButton(Graphics2D g2d, Rectangle button, String text, int mx, int my) {
        boolean hovered = button.contains(mx, my);

        if (hovered) {
            g2d.setColor(new Color(70, 130, 180)); // blue
        } else {
            g2d.setColor(new Color(40, 40, 40)); // dark grey
        }

        g2d.fillRoundRect(button.x, button.y, button.width, button.height, 20, 20);

        g2d.setColor(Color.WHITE); // Border color
        g2d.drawRoundRect(button.x, button.y, button.width, button.height, 20, 20);

        // FontMetrics for centering text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = button.x + (button.width - textWidth) / 2;
        // Adjust textY to better center text vertically in the button
        int textY = button.y + (button.height - fm.getHeight()) / 2 + fm.getAscent();


        g2d.setColor(Color.WHITE); // Text color
        g2d.drawString(text, textX, textY);
    }

}