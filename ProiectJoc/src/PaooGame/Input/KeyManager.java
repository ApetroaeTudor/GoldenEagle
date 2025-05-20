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

    public void updatePreviousState() {
        // Copy the current state to the previous state array
        System.arraycopy(keyState, 0, prevKeyState, 0, NUM_KEYS);
        // Alternative loop implementation:
        // for (int i = 0; i < NUM_KEYS; i++) {
        //     prevKeyState[i] = keyState[i];
        // }
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < NUM_KEYS) {
            return keyState[keyCode];
        }
        return false; // Invalid key code
    }

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

    public boolean[] getKeyState() {
        return keyState;
    }
}