package PaooGame.HUD; // Or your preferred package

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;

public class VerticalGradientBar {

    private int width = 50;
    private int height = 400;
    private int x = 20;
    private int y = 50;
    private int cornerRadius = 20;

    private double currentValue;    // The actual target value set via updateValue
    private double displayedValue; // The value currently shown (animated)
    private final double maxValue; // The maximum value the bar represents (gradient endpoint)
    private final double capValue; // The value above which the bar turns grey

    private Color startColor = Color.RED;
    private Color midColor = Color.YELLOW;
    private Color endColor = Color.WHITE;
    private Color capColor = Color.GRAY;
    private Color backgroundColor = new Color(50, 50, 50, 180); // Semi-transparent dark background
    private Color outlineColor = Color.BLACK;

    // --- Animation ---
    private double animationSpeed = 2.5; // Speed of the displayed value catching up (adjust as needed)

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

    public void updateValue(double newValue) {
        this.currentValue = newValue;

    }
    public void draw(Graphics2D g2d) {

        if (displayedValue != currentValue) {
            double diff = currentValue - displayedValue;
            double speed = animationSpeed;
            double change = speed * Math.signum(diff);
            displayedValue = currentValue;

        }

        double clampedDisplayedValue = Math.max(0.0, Math.min(displayedValue, this.maxValue));

        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);

        if (currentValue > capValue) {
            g2d.setColor(capColor);
            g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        } else {
            double fillRatio = (maxValue == 0) ? 0 : (clampedDisplayedValue / maxValue);
            int currentFillHeight = (int) (height * fillRatio);

            if (currentFillHeight > 0) {
                currentFillHeight = Math.min(currentFillHeight, height);

                Point2D start = new Point2D.Float(x + width / 2.0f, y + height); // Bottom-center
                Point2D end = new Point2D.Float(x + width / 2.0f, y);           // Top-center
                float[] fractions = {0.0f, 0.5f, 1.0f};                          // Stops for Red, Yellow, White
                Color[] colors = {startColor, midColor, endColor};               // The colors at the stops
                LinearGradientPaint gradientPaint = new LinearGradientPaint(start, end, fractions, colors);

                g2d.setPaint(gradientPaint);

                int fillY = y + (height - currentFillHeight);

                g2d.fillRoundRect(x, fillY, width, currentFillHeight, cornerRadius, cornerRadius);

            }
        }

        g2d.setColor(outlineColor);
        g2d.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius);
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    public Color getStartColor() { return startColor; }
    public void setStartColor(Color startColor) { this.startColor = startColor; }
    public Color getMidColor() { return midColor; }
    public void setMidColor(Color midColor) { this.midColor = midColor; }
    public Color getEndColor() { return endColor; }
    public void setEndColor(Color endColor) { this.endColor = endColor; }
    public Color getCapColor() { return capColor; }
    public void setCapColor(Color capColor) { this.capColor = capColor; }
    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
    public Color getOutlineColor() { return outlineColor; }
    public void setOutlineColor(Color outlineColor) { this.outlineColor = outlineColor; }

    public double getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(double animationSpeed) { this.animationSpeed = animationSpeed; }

    public double getMaxValue() { return maxValue; }
    public double getCapValue() { return capValue; }
    public double getCurrentValue() { return currentValue; } // Getter for the target value


    public void resetPositionToDefault() {
        this.width = 40;
        this.height = 300;
        this.x = 20;
        this.y = 50;
    }
}