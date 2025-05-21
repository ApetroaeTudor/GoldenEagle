package PaooGame.GameWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @class GameWindow
 * @brief Manages the main game window (JFrame) and the drawing surface (Canvas).
 *
 * This class is responsible for creating, configuring, and providing access
 * to the game's window and the canvas where graphics are rendered.
 */
public class GameWindow
{
    private JFrame  wndFrame;    ///< The main window frame of the game.
private String  wndTitle;    ///< The title displayed in the window's title bar.
private int     wndWidth;    ///< The width of the game window in pixels.
private int     wndHeight;   ///< The height of the game window in pixels.

private Canvas  canvas;      ///< The canvas component used for drawing game graphics.

    /**
     * @brief Constructs a GameWindow object with specified properties.
     *
     * Initializes the window title, width, and height. The actual window frame
     * is not created until BuildGameWindow() is called.
     * @param title The title for the game window.
     * @param width The desired width of the game window.
     * @param height The desired height of the game window.
     */
    public GameWindow(String title, int width, int height){
        wndTitle    = title;
        wndWidth    = width;
        wndHeight   = height;
        wndFrame    = null; // Initialize wndFrame to null, indicating it's not yet built.
    }

    /**
     * @brief Builds and displays the game window and its canvas.
     *
     * If the window frame (wndFrame) has not been previously created, this method
     * initializes a new JFrame with the specified title, size, and properties.
     * It also creates and adds a Canvas for drawing. The window is then made visible.
     * If the window already exists, this method does nothing.
     */
    public void BuildGameWindow()
    {
        if(wndFrame != null)
        {
            return; // Window already built, do nothing.
        }
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits when the window is closed.
        wndFrame.setResizable(false); // Prevent resizing of the window.
        wndFrame.setLocationRelativeTo(null); // Center the window on the screen.
        wndFrame.setVisible(true); // Make the window visible.

        // Create and configure the canvas for drawing.
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight)); // Fix size to prevent layout issues.
        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight)); // Fix size to prevent layout issues.

        wndFrame.add(canvas); // Add the canvas to the frame.
        wndFrame.pack();      // Adjusts frame size to fit its components (the canvas).
    }

    /**
     * @brief Gets the width of the game window.
     * @return The width of the window in pixels.
     */
    public int GetWndWidth()
    {
        return wndWidth;
    }

    /**
     * @brief Gets the height of the game window.
     * @return The height of the window in pixels.
     */
    public int GetWndHeight()
    {
        return wndHeight;
    }

    /**
     * @brief Gets the canvas used for drawing in the game window.
     * @return The Canvas object associated with this game window.
     *         Returns null if BuildGameWindow() has not been called.
     */
    public Canvas GetCanvas()
    {
        return canvas;
    }

    /**
     * @brief Gets the JFrame object representing the game window.
     * @return The JFrame object.
     *         Returns null if BuildGameWindow() has not been called.
     */
    public JFrame GetWndFrame()
    {
        return wndFrame;
    }
}