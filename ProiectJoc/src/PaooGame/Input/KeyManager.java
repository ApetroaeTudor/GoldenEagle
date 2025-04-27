package PaooGame.Input; // Assuming this is your package

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean[] keyState;         // Stores the current state of keys (pressed or not)
    private boolean[] prevKeyState;     // Stores the state of keys from the previous frame
    private static final int NUM_KEYS = 256; // Standard number of VK codes, adjust if needed

    public KeyManager() {
        keyState = new boolean[NUM_KEYS];
        prevKeyState = new boolean[NUM_KEYS]; // Initialize previous state array
    }

    /**
     * Updates the previous key state array.
     * MUST be called exactly ONCE per game loop update cycle,
     * ideally at the VERY END of your main update method, after
     * all game logic for the current frame has processed input.
     */
    public void updatePreviousState() {
        // Copy the current state to the previous state array
        System.arraycopy(keyState, 0, prevKeyState, 0, NUM_KEYS);
        // Alternative loop implementation:
        // for (int i = 0; i < NUM_KEYS; i++) {
        //     prevKeyState[i] = keyState[i];
        // }
    }

    /**
     * Checks if a specific key is currently pressed down.
     * Use this for continuous actions (like moving).
     *
     * @param keyCode The KeyEvent.VK_ code of the key to check.
     * @return true if the key is currently held down, false otherwise.
     */
    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            return keyState[keyCode];
        }
        return false; // Invalid key code
    }

    /**
     * Checks if a specific key was just pressed down in this frame.
     * Returns true only on the single frame the key transitions from UP to DOWN.
     * Use this for single-trigger actions (like jumping, shooting, menu select).
     * Requires updatePreviousState() to be called correctly each frame.
     *
     * @param keyCode The KeyEvent.VK_ code of the key to check.
     * @return true if the key was pressed in this frame but not the previous one, false otherwise.
     */
    public boolean isKeyPressedOnce(int keyCode) {
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            // Check if the key is currently pressed AND was NOT pressed previously
            return keyState[keyCode] && !prevKeyState[keyCode];
        }
        return false; // Invalid key code
    }


    // --- KeyListener Implementation ---

    @Override
    public void keyTyped(KeyEvent e) {
        // Usually not needed for game controls
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            keyState[keyCode] = true; // Mark key as currently pressed
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            keyState[keyCode] = false; // Mark key as released
        }
    }

    /**
     * Gets the raw current key state array.
     * Generally prefer using isKeyPressed() or isKeyPressedOnce().
     * @return The boolean array representing the current state of all keys.
     */
    public boolean[] getKeyState() {
        return keyState;
    }
}