package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.swing.*;


/**
 * @class DeathState
 * @brief Represents the game state displayed when the player dies.
 *
 * This state shows a death screen, typically with a background image. It implements
 * a fade-in effect when the state is entered and a fade-out effect after a short delay,
 * after which it transitions back to the menu state.
 */
public class DeathState extends State {

    private double blackIntensity = 1.0;    ///< Current intensity of the black overlay for fading, ranging from 0.0 (transparent) to 1.0 (opaque).
    private double fadeSpeed = 0.05;        ///< The rate at which the {@link #blackIntensity} changes per frame during fade effects.

    private Timer timer;                    ///< Swing Timer used to delay the fade-out effect.
    private int timeoutInMillis = 1000;     ///< The duration in milliseconds before the fade-out sequence begins after fade-in completes.

    private boolean isFadedIn = false;      ///< Flag indicating if the initial fade-in effect has completed.
    private boolean isFadingOut = false;    ///< Flag indicating if the fade-out effect is currently active.

    /**
     * @brief Constructs a DeathState object.
     *
     * Initializes the state with a reference to game links, sets its name,
     * and configures a timer that triggers the fade-out sequence.
     * @param reflink A {@link RefLinks} object providing access to game-wide resources and utilities.
     */
    public DeathState(RefLinks reflink){
        super(reflink);
        this.stateName = "DeathState"; // Sets the identifier for this state.
        this.timer = new Timer(this.timeoutInMillis,e->{
            this.isFadingOut = true; // When the timer elapses, start the fade-out process.
        });
        this.timer.setRepeats(false); // The timer should only fire once per activation.
    }

    /**
     * @brief Gets the name of this state.
     * @return The string identifier for this state.
     */
    @Override
    public String getStateName() {
        return this.stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is part of the {@link State} interface but is not
     * used or implemented in a DeathState, as it typically doesn't involve specific enemy interactions.
     * @param enemy The {@link Enemy} to be set (ignored in this state).
     */
    @Override
    public void setEnemy(Enemy enemy) {
        // Not applicable for DeathState.
    }

    /**
     * @brief Updates the logic of the DeathState.
     *
     * If the fade-in effect has completed ({@link #isFadedIn} is true),
     * this method starts the timer that will eventually trigger the fade-out.
     */
    @Override
    public void update() {
        if(this.isFadedIn){
            this.timer.start(); // Start the countdown to fade out.
        }
    }

    /**
     * @brief Draws the content of the DeathState on the screen.
     *
     * Renders the death screen background image and then applies the
     * black fade effect (either fade-in or fade-out).
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void draw(Graphics g) {
        BufferedImage backgroundImg = this.reflink.getTileCache().getBackground(Constants.DEATH_SCREEN_BG_PATH);
        g.drawImage(backgroundImg,0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);

        drawBlackFade(g); // Apply the fade effect.
    }

    /**
     * @brief Manages and draws the fade-in and fade-out black overlay.
     *
     * If the state is active and not yet faded in, it gradually reduces {@link #blackIntensity}
     * to create a fade-from-black effect. Once faded in, if {@link #isFadingOut} is true,
     * it gradually increases {@link #blackIntensity} to create a fade-to-black effect,
     * after which it transitions to the menu state.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawBlackFade(Graphics g){
        // Fade-in logic
        if(Objects.equals(State.getState().getStateName(), this.stateName) && !this.isFadedIn){
            Graphics2D g2d = (Graphics2D) g;
            Color originalColor = g2d.getColor(); // Preserve original color

            // Clamp blackIntensity to [0, 1] range
            if(this.blackIntensity>=1){
                this.blackIntensity = 1.0;
            }
            if(this.blackIntensity<0){
                this.blackIntensity = 0.0;
                this.isFadedIn = true; // Mark fade-in as complete
            }

            g2d.setColor(new Color(0,0,0,(int)(this.blackIntensity*255.0))); // Set color with current alpha
            g2d.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT); // Draw overlay
            this.blackIntensity-=this.fadeSpeed; // Decrease intensity for next frame

            g2d.setColor(originalColor); // Restore original color
        }

        // Fade-out logic
        if(Objects.equals(State.getState().getStateName(), this.stateName) && this.isFadingOut){
            Graphics2D g2d = (Graphics2D) g;
            Color originalColor = g2d.getColor();

            if(this.blackIntensity>1){ // If fully faded out
                this.reflink.setDataRefreshSignal(true); // Signal data refresh if needed
                State.setState(this.reflink.getGame().getMenuState()); // Transition to menu state
                // blackIntensity will be reset by restoreState when DeathState is re-entered
            }
            if(this.blackIntensity<0){
                this.blackIntensity = 0.0;
                 this.isFadedIn = true;
            }
            g2d.setColor(new Color(0,0,0,(int)(this.blackIntensity*255.0)));
            g2d.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            this.blackIntensity+=this.fadeSpeed; // Increase intensity for next frame

            g2d.setColor(originalColor);
        }
    }

    /**
     * @brief Restores the initial settings of this state.
     *
     * Called when this state is entered or re-entered. It resets the fade flags
     * and the {@link #blackIntensity} to ensure the fade-in starts correctly.
     * The timer is implicitly stopped or will be restarted by {@link #update()}.
     */
    @Override
    public void restoreState() {
        this.isFadedIn = false;
        this.isFadingOut = false;
        this.blackIntensity = 1.0; // Reset for fade-in
        if (this.timer != null && this.timer.isRunning()) {
            this.timer.stop(); // Ensure timer is stopped
        }
    }

    /**
     * @brief Loads state-specific data.
     *
     * This method is part of the {@link State} contract for state management.
     */
    @Override
    public void loadState(boolean access) {
        // Not implemented for DeathState.
    }

    /**
     * @brief Stores state-specific data.
     *
     * This method is part of the {@link State} contract for state management.
     */
    @Override
    public void storeState(boolean access) {
        // Not implemented for DeathState.
    }

}