package PaooGame.Input;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class MouseInput implements MouseInputListener, MouseMotionListener {

    private int numberOfMousePresses=0;
    private int mouseX=0;
    private int mouseY=0;

    private boolean isMousePressed = false;
    private boolean clickDetected = false;


    @Override
    public void mousePressed(MouseEvent e) {
        isMousePressed = true;
        if(numberOfMousePresses < Integer.MAX_VALUE)
            numberOfMousePresses += 1;

        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
        clickDetected = false;
        numberOfMousePresses=0;
        mouseX=0;
        mouseY=0;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public int getNumberOfMousePresses(){
        return this.numberOfMousePresses;
    }
    public int getMouseX(){
        return this.mouseX;
    }
    public int getMouseY(){
        return this.mouseY;
    }


    public boolean isOneClick() {
        if (isMousePressed && !clickDetected) {
            clickDetected = true;
            return true;
        }
        return false;
    }
}
