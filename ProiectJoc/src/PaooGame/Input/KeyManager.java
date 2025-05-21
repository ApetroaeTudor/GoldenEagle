package PaooGame.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @class KeyManager
 * @brief Manages keyboard input by tracking the current and previous state of keys.
 *
 * This class implements {@link KeyListener} to receive keyboard events. It stores the
 * current state (pressed or not pressed) of all keys and the state from the previous
 * update cycle. This allows for checking if a key is currently held down or if it
 * has just been pressed in the current cycle.
 */
public class KeyManager implements KeyListener {

    private boolean[] keyState;      ///< Array representing the current state of each key (true if pressed, false otherwise). Index corresponds to {@link KeyEvent#getKeyCode()}.
    private boolean[] prevKeyState;  ///< Array representing the state of each key in the previous update cycle. Used to detect single key presses.
    private static final int NUM_KEYS = 256; ///< The total number of key codes to track, covering standard keyboard keys.

    /**
     * @brief Constructs a KeyManager object.
     *
     * Initializes the {@link #keyState} and {@link #prevKeyState} arrays.
     * All keys are initially marked as not pressed.
     */
    public KeyManager() {
        keyState = new boolean[NUM_KEYS];
        prevKeyState = new boolean[NUM_KEYS];
    }

    /**
     * @brief Checks if a specific key is currently pressed.
     * @param keyCode The {@link KeyEvent#getKeyCode()} of the key to check.
     * @return True if the key is currently pressed, false otherwise or if the keyCode is invalid.
     */
    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            return keyState[keyCode];
        }
        return false;
    }

    /**
     * @brief Checks if a specific key was just pressed in the current update cycle.
     *
     * This method determines if the key is currently pressed and was not pressed in the
     * previous update cycle.
     * @param keyCode The {@link KeyEvent#getKeyCode()} of the key to check.
     * @return True if the key is currently pressed and was not pressed in the previous state,
     *         false otherwise or if the keyCode is invalid.
     */
    public boolean isKeyPressedOnce(int keyCode) {
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            return keyState[keyCode] && !prevKeyState[keyCode];
        }
        return false;
    }


    /**
     * @brief Invoked when a key has been typed (pressed and released).
     *
     * This method is part of the {@link KeyListener} interface but is not
     * used in this implementation for game input logic.
     * @param e The {@link KeyEvent} associated with the key type event.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @brief Invoked when a key has been pressed.
     *
     * Sets the state of the pressed key to true in the {@link #keyState} array.
     * @param e The {@link KeyEvent} associated with the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            keyState[keyCode] = true;
        }
    }

    /**
     * @brief Invoked when a key has been released.
     *
     * Sets the state of the released key to false in the {@link #keyState} array.
     * @param e The {@link KeyEvent} associated with the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            keyState[keyCode] = false;
        }
    }

    /**
     * @brief Gets the array representing the current state of all keys.
     * @return The {@link #keyState} array.
     */
    public boolean[] getKeyState() {
        return keyState;
    }
}