package PaooGame.States;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Entities.Enemy;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @class WinState
 * @brief Represents the game state displayed when the player wins the game.
 *
 * This class extends the abstract {@link State} class and defines the
 * behavior and rendering for the win screen. It features a fade-in effect,
 * background animations, displays the player's score and top scores,
 * and provides a button to return to the main menu.
 */
public class WinState extends State {

    private double blackIntensity = 1.0;                    ///< Current intensity of the black fade-in effect (1.0 is fully black, 0.0 is clear).
    private double fadeSpeed = 0.05;                        ///< Speed at which the black overlay fades out.

    private StaticItemAnimation bgAnimation;                ///< Animation for the win screen background.
    private StaticItemAnimation youWinAnimation;            ///< "You Win" text animation.

    private Timer showReturnButtonTimer;                    ///< Timer to delay the appearance of the "Return to Menu" button.
    private int showReturnButtonTimeoutMillis = 2000;       ///< Timeout in milliseconds before the "Return to Menu" button is shown.
    private boolean showButton = false;                     ///< Flag indicating whether the "Return to Menu" button should be visible.
    private boolean isTimerStarted = false;                 ///< Flag indicating if the showReturnButtonTimer has been started.


    private Rectangle exitToMenuButton;                     ///< Rectangle defining the "MAIN MENU" button area.

    /**
     * @brief Constructs a WinState object.
     *
     * Initializes the win screen elements, including animations, timers,
     * and the "MAIN MENU" button. It sets up the fade-in effect and prepares
     * the animations for display.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public WinState(RefLinks reflink){
        super(reflink);
        this.stateName = "WinState";

        this.showReturnButtonTimer = new Timer(this.showReturnButtonTimeoutMillis, e-> {
            this.showButton = true;
        });
        this.showReturnButtonTimer.setRepeats(false);
        int centerX = 350;
        int startY = 650;

        this.exitToMenuButton = new Rectangle(centerX + 50, startY,300,50);

        this.bgAnimation = new StaticItemAnimation(this.reflink,Constants.WINNING_BG_PATH,Constants.WINNING_BG_FRAME_NR,5,Constants.WINNING_BG_WIDTH,Constants.WINNING_BG_HEIGHT);
        this.youWinAnimation = new StaticItemAnimation(this.reflink,Constants.YOU_WIN_PATH,Constants.YOU_WIN_FRAME_NR,12,Constants.YOU_WIN_IMG_WIDTH,Constants.YOU_WIN_IMG_HEIGHT);
        this.bgAnimation.loadAnimation();

        this.youWinAnimation.loadAnimation();
        this.youWinAnimation.triggerOnce();
        this.youWinAnimation.setPlayOnce(true);


    }

    /**
     * @brief Gets the name of this state.
     * This method overrides the {@link State#getStateName()} method.
     * @return The string name of the state.
     */
    @Override
    public String getStateName(){
        return this.stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     * This method overrides the {@link State#setEnemy(Enemy)} method.
     * @param enemy The {@link Enemy} to be set.
     */
    @Override
    public void setEnemy(Enemy enemy){
    }

    /**
     * @brief Updates the logic for the WinState.
     *
     * This method is called repeatedly in the game loop. It handles:
     * - Restoring the state if it's not the active state (e.g., on re-entry).
     * - Updating background and "You Win" animations.
     * - Checking for mouse clicks on the "MAIN MENU" button.
     * - Starting the timer to show the "MAIN MENU" button if this state is active and the timer hasn't started.
     * This method overrides the {@link State#update()} method.
     */
    @Override
    public void update(){
        if(!Objects.equals(State.getState().getStateName(), this.stateName)){
            this.restoreState(); // Reset state variables if this state becomes inactive then active again
        }
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();
        this.bgAnimation.updateAnimation();
        this.youWinAnimation.updateAnimation();


        if(mouse.getNumberOfMousePresses()>0){
            if(this.showButton && exitToMenuButton.contains(mx,my)){ // Ensure button is visible before interaction
                State.setState(this.reflink.getGame().getMenuState());
                mouse.mouseReleased(null); // Reset mouse press
                this.restoreState(); // Restore state after exiting
            }
        }

        if(Objects.equals(State.getState().getStateName(), this.stateName) && !this.isTimerStarted){
            this.isTimerStarted = true;
            this.showReturnButtonTimer.start();
        }
    }


    /**
     * @brief Draws the WinState to the screen.
     *
     * This method is responsible for rendering all visual elements of the win screen:
     * - Background and "You Win" animations.
     * - Player's score and top scores.
     * - A fade-out effect for the black overlay (revealing the win screen).
     * - The "MAIN MENU" button (after a delay).
     * This method overrides the {@link State#draw(Graphics)} method.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        this.bgAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        this.youWinAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);

        // Store original font and color to restore later
        Font oldFont = g2d.getFont();
        Color oldColor = g2d.getColor();
        RenderingHints oldHints = g2d.getRenderingHints(); // Store old rendering hints

        // Enable text antialiasing for smoother text
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Display player's score
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Your score: " + reflink.getHero().getScore(),20,40);

        // Display top scores
        g2d.setFont(new Font("Arial",Font.BOLD,50));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("TOP SCORES",380,50);
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.drawString("1. "+this.reflink.getScore1(),380,80);
        g2d.drawString("2. " +this.reflink.getScore2(),380,110);
        g2d.drawString("3. "+ this.reflink.getScore3(),380,140);


        // Restore original font, color, and rendering hints
        g2d.setFont(oldFont);
        g2d.setColor(oldColor);
        g2d.setRenderingHints(oldHints);

        // Fade-in effect (actually a fade-out of a black overlay)
        if(Objects.equals(State.getState().getStateName(),this.getStateName())){ // Ensure effect only runs when state is active
            if (this.blackIntensity > 0) { // Only draw and update if still fading
                g2d.setColor(new Color(0,0,0,(int)(this.blackIntensity*255.0)));
                g2d.fillRect(0,0, Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
                this.blackIntensity-=this.fadeSpeed;
                if (this.blackIntensity < 0) { // Clamp to 0
                    this.blackIntensity = 0;
                }
            }
        }

        // Draw "MAIN MENU" button if visible
        if(this.showButton){
            int mx = reflink.getMouseInput().getMouseX();
            int my = reflink.getMouseInput().getMouseY();
            drawModernButton(g2d,exitToMenuButton,"MAIN MENU",mx,my);
        }
    }

    /**
     * @brief Restores the WinState to its initial settings.
     *
     * This method is called to reset the state's variables, such as the
     * fade effect intensity, timer flags, and button visibility. It also
     * resets the displayed top scores in the RefLinks object.
     * This method overrides the {@link State#restoreState()} method.
     */
    @Override
    public void restoreState(){
        this.blackIntensity = 1.0;
        this.isTimerStarted = false;
        this.showButton = false;
        if (this.showReturnButtonTimer.isRunning()) { // Stop timer if it's running
            this.showReturnButtonTimer.stop();
        }
        this.reflink.setScore1(0);
        this.reflink.setScore2(0);
        this.reflink.setScore3(0);
    }

    /**
     * @brief Loads state specific to the win screen.
     * This method overrides the {@link State#loadState(boolean)} method.
     * @param access Boolean indicating access permission.
     */
    @Override
    public void loadState(boolean access){
    }

    /**
     * @brief Stores state specific to the win screen.
     * This method overrides the {@link State#storeState(boolean)} method.
     * @param access Boolean indicating access permission.
     */
    @Override
    public void storeState(boolean access){
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
                hover ? new Color(70, 130, 180) : new Color(60, 60, 60),
                rect.x, rect.y + rect.height,
                hover ? new Color(50, 100, 150) : new Color(40, 40, 40)
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