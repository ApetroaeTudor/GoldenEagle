package PaooGame.Camera;

import PaooGame.Config.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static PaooGame.Config.Constants.*;

public class Camera {
    private double xOffset, yOffset;
    private double scale = 2.3;

    public Camera(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void apply(Graphics2D g2d) {
        AffineTransform transform = new AffineTransform();
        transform.translate(-xOffset* Constants.MAGIC_NUMBER, -yOffset);
        transform.scale(scale, scale);
        g2d.transform(transform);
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


    public void setPosition(double x, double y) {

        this.xOffset = x;

        this.yOffset = y;

    }
}


