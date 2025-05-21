package PaooGame.HUD;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;

/**
 * @class VerticalGradientBar
 * @brief Represents a customizable vertical bar that displays a value with a gradient fill.
 *
 * This class provides a visual representation of a value
 * as a vertical bar. The bar fills from bottom to top with a configurable color gradient.
 * It supports animation for value changes, a cap value beyond which the bar changes color (for example to gray),
 * and customizable dimensions, position, and colors.
 */
public class VerticalGradientBar {

    private int width = 50;             ///< The width of the bar in pixels.
    private int height = 400;           ///< The height of the bar in pixels.
    private int x = 20;                 ///< The x-coordinate of the top-left corner of the bar.
    private int y = 50;                 ///< The y-coordinate of the top-left corner of the bar.
    private int cornerRadius = 20;      ///< The radius for the rounded corners of the bar.

    private double currentValue;        ///< The actual target value that the bar should represent. Set via {@link #updateValue(double)}.
    private double displayedValue;      ///< The value currently being displayed, which animates towards {@link #currentValue}.
    private final double maxValue;      ///< The maximum value that the bar can represent (corresponds to a full bar).
    private final double capValue;      ///< A threshold value; if {@link #currentValue} exceeds this, the bar is drawn with {@link #capColor}.

    private Color startColor = Color.RED;       ///< The color at the bottom of the gradient fill.
    private Color midColor = Color.YELLOW;      ///< The color at the middle of the gradient fill.
    private Color endColor = Color.WHITE;       ///< The color at the top of the gradient fill.
    private Color capColor = Color.GRAY;        ///< The color used to fill the bar if {@link #currentValue} exceeds {@link #capValue}.
    private Color backgroundColor = new Color(50, 50, 50, 180); ///< The background color of the bar's container.
    private Color outlineColor = Color.BLACK;   ///< The color of the bar's outline.

    private double animationSpeed = 2.5; ///< The speed at which the {@link #displayedValue} animates towards the {@link #currentValue}.

    /**
     * @brief Constructs a VerticalGradientBar.
     *
     * Initializes the bar with a maximum value and a cap value.
     * The current and displayed values are initialized to 0.
     * @param maxValue The maximum value the bar can represent. Must be positive.
     * @param capValue The value above which the bar's appearance changes (e.g., to a 'capped' state color).
     * @throws IllegalArgumentException if maxValue is not positive.
     */
    public VerticalGradientBar(double maxValue, double capValue) {
        if (maxValue <= 0) {
            throw new IllegalArgumentException("maxValue must be positive.");
        }
        this.maxValue = maxValue;
        this.capValue = capValue;
        // Initialize values
        this.currentValue = 0;
        this.displayedValue = 0;
    }

    /**
     * @brief Updates the target value that the bar should represent.
     *
     * @param newValue The new target value for the bar.
     */
    public void updateValue(double newValue) {
        this.currentValue = newValue;
    }

    /**
     * @brief Draws the vertical gradient bar on the screen.
     *
     * This method handles the animation of the displayed value, draws the background,
     * the gradient fill (or cap color if applicable), and the outline of the bar.
     * The fill is a vertical linear gradient from {@link #startColor} (bottom) through
     * {@link #midColor} to {@link #endColor} (top).
     * @param g2d The Graphics2D context to draw on.
     */
    public void draw(Graphics2D g2d) {

        // Animate displayedValue towards currentValue
        if (displayedValue != currentValue) {
            displayedValue = currentValue;
        }

        double clampedDisplayedValue = Math.max(0.0, Math.min(displayedValue, this.maxValue));

        // Draw background
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);

        // Draw fill or cap color
        if (currentValue > capValue) {
            g2d.setColor(capColor);
            g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        } else {
            double fillRatio = (maxValue == 0) ? 0 : (clampedDisplayedValue / maxValue);
            int currentFillHeight = (int) (height * fillRatio);

            if (currentFillHeight > 0) {
                currentFillHeight = Math.min(currentFillHeight, height); // Ensure fill doesn't exceed bar height

                // Define gradient points and colors
                Point2D start = new Point2D.Float(x + width / 2.0f, y + height); // Bottom-center
                Point2D end = new Point2D.Float(x + width / 2.0f, y);           // Top-center
                float[] fractions = {0.0f, 0.5f, 1.0f};                          // Stops for Red, Yellow, White
                Color[] colors = {startColor, midColor, endColor};               // The colors at the stops
                LinearGradientPaint gradientPaint = new LinearGradientPaint(start, end, fractions, colors);

                g2d.setPaint(gradientPaint);

                // Calculate the y-coordinate for the top of the filled portion
                int fillY = y + (height - currentFillHeight);

                // Draw the filled portion with rounded corners
                g2d.fillRoundRect(x, fillY, width, currentFillHeight, cornerRadius, cornerRadius);
            }
        }

        // Draw outline
        g2d.setColor(outlineColor);
        g2d.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius);
    }


    /**
     * @brief Sets the position of the top-left corner of the bar.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @brief Sets the dimensions of the bar.
     * @param width The new width in pixels.
     * @param height The new height in pixels.
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @brief Gets the x-coordinate of the bar's top-left corner.
     * @return The x-coordinate.
     */
    public int getX() { return this.x; }

    /**
     * @brief Gets the y-coordinate of the bar's top-left corner.
     * @return The y-coordinate.
     */
    public int getY() { return this.y; }

    /**
     * @brief Gets the width of the bar.
     * @return The width in pixels.
     */
    public int getWidth() { return this.width; }

    /**
     * @brief Gets the height of the bar.
     * @return The height in pixels.
     */
    public int getHeight() { return this.height; }

    /**
     * @brief Gets the start color of the gradient.
     * @return The start color (bottom of the gradient).
     */
    public Color getStartColor() { return startColor; }

    /**
     * @brief Sets the start color of the gradient.
     * @param startColor The new start color (bottom of the gradient).
     */
    public void setStartColor(Color startColor) { this.startColor = startColor; }

    /**
     * @brief Gets the middle color of the gradient.
     * @return The middle color of the gradient.
     */
    public Color getMidColor() { return midColor; }

    /**
     * @brief Sets the middle color of the gradient.
     * @param midColor The new middle color of the gradient.
     */
    public void setMidColor(Color midColor) { this.midColor = midColor; }

    /**
     * @brief Gets the end color of the gradient.
     * @return The end color (top of the gradient).
     */
    public Color getEndColor() { return endColor; }

    /**
     * @brief Sets the end color of the gradient.
     * @param endColor The new end color (top of the gradient).
     */
    public void setEndColor(Color endColor) { this.endColor = endColor; }

    /**
     * @brief Gets the color used when the value exceeds the cap value.
     * @return The cap color.
     */
    public Color getCapColor() { return capColor; }

    /**
     * @brief Sets the color used when the value exceeds the cap value.
     * @param capColor The new cap color.
     */
    public void setCapColor(Color capColor) { this.capColor = capColor; }

    /**
     * @brief Gets the background color of the bar.
     * @return The background color.
     */
    public Color getBackgroundColor() { return backgroundColor; }

    /**
     * @brief Sets the background color of the bar.
     * @param backgroundColor The new background color.
     */
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }

    /**
     * @brief Gets the outline color of the bar.
     * @return The outline color.
     */
    public Color getOutlineColor() { return outlineColor; }

    /**
     * @brief Sets the outline color of the bar.
     * @param outlineColor The new outline color.
     */
    public void setOutlineColor(Color outlineColor) { this.outlineColor = outlineColor; }

    /**
     * @brief Gets the animation speed for the displayed value.
     * @return The animation speed.
     */
    public double getAnimationSpeed() { return animationSpeed; }

    /**
     * @brief Sets the animation speed for the displayed value.
     * @param animationSpeed The new animation speed.
     */
    public void setAnimationSpeed(double animationSpeed) { this.animationSpeed = animationSpeed; }

    /**
     * @brief Gets the maximum value the bar can represent.
     * @return The maximum value.
     */
    public double getMaxValue() { return maxValue; }

    /**
     * @brief Gets the cap value, above which the bar's appearance changes.
     * @return The cap value.
     */
    public double getCapValue() { return capValue; }

    /**
     * @brief Gets the current target value of the bar.
     * @return The current target value.
     */
    public double getCurrentValue() { return currentValue; }


    /**
     * @brief Resets the bar's dimensions and position to default values.
     * Default width: 40, height: 300, x: 20, y: 50.
     */
    public void resetPositionToDefault() {
        this.width = 40;
        this.height = 300;
        this.x = 20;
        this.y = 50;
    }
}