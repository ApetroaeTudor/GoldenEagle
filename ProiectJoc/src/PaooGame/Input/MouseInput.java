package PaooGame.Input;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


/**
 * @class MouseInput
 * @brief Implements {@link MouseInputListener} and {@link MouseMotionListener} to handle mouse input events.
 *
 * This class tracks mouse button presses, releases, and movement. It provides methods
 * to get the current mouse coordinates, the number of times the mouse has been pressed
 * since the last release, and a mechanism to detect a single click event.
 */
public class MouseInput implements MouseInputListener, MouseMotionListener {

    private int numberOfMousePresses=0; ///< Counts the number of times the mouse button has been pressed since the last release. Increments on press, resets on release.
    private int mouseX=0;               ///< The current x-coordinate of the mouse cursor.
    private int mouseY=0;               ///< The current y-coordinate of the mouse cursor.

    private boolean isMousePressed = false; ///< Flag indicating if a mouse button is currently pressed down.
    private boolean clickDetected = false;  ///< Flag used by {@link #isOneClick()} to ensure a click is reported only once per press.

    /**
     * @brief Invoked when a mouse button has been pressed on a component.
     *
     * Sets the {@link #isMousePressed} flag to true, increments {@link #numberOfMousePresses}
     * (capped at {@link Integer#MAX_VALUE}), and updates {@link #mouseX} and {@link #mouseY}
     * with the event coordinates.
     * @param e The {@link MouseEvent} associated with the mouse press.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        isMousePressed = true;
        if(numberOfMousePresses < Integer.MAX_VALUE)
            numberOfMousePresses += 1;

        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * @brief Invoked when a mouse button has been released on a component.
     *
     * Sets the {@link #isMousePressed} flag to false, resets {@link #clickDetected} to false,
     * resets {@link #numberOfMousePresses} to 0, and clears {@link #mouseX} and {@link #mouseY} to 0.
     * @param e The {@link MouseEvent} associated with the mouse release.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
        clickDetected = false;
        numberOfMousePresses=0;
        mouseX=0;
        mouseY=0;
    }

    /**
     * @brief Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * Updates {@link #mouseX} and {@link #mouseY} with the event coordinates.
     * @param e The {@link MouseEvent} associated with the mouse movement.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * @brief Invoked when the mouse button has been clicked (pressed and released) on a component.
     *
     * This method is part of the {@link MouseInputListener} interface but is not
     * actively used for state tracking in this implementation.
     * @param e The {@link MouseEvent} associated with the mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * @brief Invoked when the mouse enters a component.
     *
     * This method is part of the {@link MouseInputListener} interface but is not
     * used in this implementation.
     * @param e The {@link MouseEvent} associated with the mouse enter event.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @brief Invoked when the mouse exits a component.
     *
     * This method is part of the {@link MouseInputListener} interface but is not
     * used in this implementation.
     * @param e The {@link MouseEvent} associated with the mouse exit event.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @brief Invoked when a mouse button is pressed on a component and then dragged.
     *
     * This method is part of the {@link MouseMotionListener} interface but is not
     * actively used for state tracking in this implementation.
     * The current {@link #mouseMoved(MouseEvent)} handles general coordinate updates.
     * @param e The {@link MouseEvent} associated with the mouse drag.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * @brief Gets the number of times a mouse button has been pressed since the last release.
     * @return The current value of {@link #numberOfMousePresses}.
     */
    public int getNumberOfMousePresses(){
        return this.numberOfMousePresses;
    }

    /**
     * @brief Gets the current x-coordinate of the mouse cursor.
     * @return The current value of {@link #mouseX}.
     */
    public int getMouseX(){
        return this.mouseX;
    }

    /**
     * @brief Gets the current y-coordinate of the mouse cursor.
     * @return The current value of {@link #mouseY}.
     */
    public int getMouseY(){
        return this.mouseY;
    }


    /**
     * @brief Detects if a mouse click has occurred and ensures it's reported only once per press.
     *
     * It returns true if a mouse button is currently pressed
     * and this specific press has not yet been reported as a click by this method.
     * After reporting true once, it will return false for subsequent calls during the same
     * press, until the mouse button is released and pressed again.
     * @return True if a new click is detected (mouse is pressed and not yet reported), false otherwise.
     */
    public boolean isOneClick() {
        if (isMousePressed && !clickDetected) {
            clickDetected = true; // Mark this press as detected
            return true;
        }
        return false;
    }
}