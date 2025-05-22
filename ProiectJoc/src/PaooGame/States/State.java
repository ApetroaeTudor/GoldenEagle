package PaooGame.States;

import java.awt.*;

import PaooGame.Entities.Enemy;
import PaooGame.RefLinks;

/**
 * @class State
 * @brief Defines an abstract base class for all game states.
 *
 * This class provides a common interface and static methods for managing
 * different states within the game, such as the main menu, gameplay levels,
 * settings, etc. Each specific state (e.g., MenuState, Level1State) must
 * extend this class and implement its abstract methods.
 */
public abstract class State
{
    protected String stateName = "";                        ///< The name identifier for the specific state instance.

    private static State previousState  = null;             ///< Reference to the previous game state.
    private static State currentState   = null;             ///< Reference to the current game state (e.g., game, menu, settings).
    protected RefLinks reflink;                             ///< A reference to shared game objects and utilities.

    /**
     * @brief Constructs a State object.
     *
     * Initializes the state with a reference to the game's shared {@link RefLinks} object.
     * @param refLink A {@link RefLinks} object providing access to game-wide objects and utilities.
     */
    public State(RefLinks refLink)
    {
        this.reflink = refLink;
    }

    /**
     * @brief Sets the current game state.
     *
     * Updates the {@link #currentState} to the new state and stores the
     * previous current state in {@link #previousState}.
     * @param state The new {@link State} to be set as current.
     */
    public static void setState(State state)
    {
        previousState = currentState;
        currentState = state;
    }

    /**
     * @brief Gets the current game state.
     * @return The current active {@link State}.
     */
    public static State getState()
    {
        return currentState;
    }

    /**
     * @brief Gets the name of the current state.
     *
     * This method must be implemented by concrete subclasses to return their
     * specific state name.
     * @return A {@link String} representing the name of the state.
     */
    public abstract String getStateName();

    /**
     * @brief Sets an enemy for the current state.
     *
     * This method is intended for states that involve direct interaction with
     * a specific enemy, such as a fight state. Concrete subclasses must implement
     * this if applicable.
     * @param enemy The {@link Enemy} to be set for the state.
     */
    public abstract void setEnemy(Enemy enemy);

    /**
     * @brief Updates the logic of the current state.
     *
     * This method is called repeatedly in the game loop and should contain
     * all game logic updates specific to the state.
     */
    public abstract void update();

    /**
     * @brief Draws the current state to the screen.
     *
     * This method is called repeatedly in the game loop and should handle
     * all rendering specific to the state.
     * @param g The {@link Graphics} context used for drawing.
     */
    public abstract void draw(Graphics g);

    /**
     * @brief Restores the state to its initial or a default configuration.
     *
     * This method is typically used when re-entering a state after a reset
     * (e.g., after player death, starting a new game in a level).
     */
    public abstract void restoreState();

    /**
     * @brief Loads the state's data from a persistent source.
     *
     * This method is used to retrieve saved game progress or configuration
     * specific to this state.
     * @param access A boolean flag, potentially for controlling access permissions during loading.
     */
    public abstract void loadState(boolean access);

    /**
     * @brief Stores the state's current data to a persistent source.
     *
     * This method is used to save game progress or configuration
     * specific to this state.
     * @param access A boolean flag, potentially for controlling access permissions during storing.
     */
    public abstract void storeState(boolean access);


}