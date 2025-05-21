package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class AboutState
 * @brief Represents the "About" or "Credits" screen of the game.
 *
 * This state is typically used to display information about the game. It extends the abstract {@link State} class.
 */
public class AboutState extends State
{
    protected String stateName = Constants.ABOUT_STATE; ///< The name identifier for this state, typically "AboutState".

    /**
     * @brief Constructs an AboutState object.
     * @param refLink A {@link RefLinks} object providing access to game-wide resources and utilities.
     */
    public AboutState(RefLinks refLink)
    {
        super(refLink);
    }

    /**
     * @brief Gets the name of this state.
     * @return The string identifier for this state, as defined in {@link #stateName}.
     */
    @Override
    public String getStateName(){
        return stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is part of the {@link State} interface but is not typically
     * used or implemented in an AboutState, as it usually doesn't involve enemies.
     * @param enemy The {@link Enemy} to be set (ignored in this state).
     */
    @Override
    public void setEnemy(Enemy enemy) {
        // Not applicable for AboutState
    }

    /**
     * @brief Updates the logic of the AboutState.
     */
    @Override
    public void update() {
        // Not needed for AboutState.
    }


    /**
     * @brief Draws the content of the AboutState on the screen.
     *
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void draw(Graphics g) {
        // Not needed for AboutState.
    }

    /**
     * @brief Restores any specific settings or data when returning to this state.
     *
     * This method is part of the {@link State} contract for state management.
     * For an AboutState, this might not be necessary if it's stateless.
     */
    @Override
    public void restoreState() {
        // Not needed for AboutState.
    }

    /**
     * @brief Loads state-specific data.
     *
     * This method is part of the {@link State} contract for state management.
     */
    @Override
    public void loadState(boolean access) {
        // Not needed for AboutState.
    }

    /**
     * @brief Stores state-specific data.
     *
     * This method is part of the {@link State} contract for state management.
     */
    @Override
    public void storeState(boolean access) {
        // Not needed for AboutState.
    }
}