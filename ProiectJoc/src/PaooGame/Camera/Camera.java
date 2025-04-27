package PaooGame.Camera;

import PaooGame.Config.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static PaooGame.Config.Constants.*;

public class Camera {
    private double xOffset, yOffset;
    private int screenWidth, screenHeight;
    private double scale = 1.0;
    private int levelWidthInPixels;
    private int levelHeightInPixels;

    public Camera(double xOffset, double yOffset, int screenWidth, int screenHeight) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void apply(Graphics2D g2d) {
        g2d.translate(-xOffset, -yOffset);
    }


    public void smoothFollow(double targetX, double targetY, double smoothness) {
        xOffset += (targetX - xOffset) * smoothness;
        yOffset += (targetY - yOffset) * smoothness;

        xOffset = Math.max(0, Math.min(xOffset, levelWidthInPixels - screenWidth));
        yOffset = Math.max(0, Math.min(yOffset, levelHeightInPixels - screenHeight));

        System.out.println("Camera offset: " + xOffset + ", " + yOffset);
    }



    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setLevelBounds(int levelWidthInTiles, int levelHeightInTiles) {
        this.levelWidthInPixels = levelWidthInTiles * Constants.TILE_SIZE;
        this.levelHeightInPixels = levelHeightInTiles * Constants.TILE_SIZE;
    }


    public void setPosition(double x, double y) {

        this.xOffset = x;

        this.yOffset = y;

    }
}


