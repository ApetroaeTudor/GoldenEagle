package PaooGame.GameWindow;

import javax.swing.*;
import java.awt.*;

public class GameWindow
{
    private JFrame  wndFrame;
    private String  wndTitle;
    private int     wndWidth;
    private int     wndHeight;

    private Canvas  canvas;

    public GameWindow(String title, int width, int height){
        wndTitle    = title;
        wndWidth    = width;
        wndHeight   = height;
        wndFrame    = null;
    }

    public void BuildGameWindow()
    {
        if(wndFrame != null)
        {
            return;
        }
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndFrame.setResizable(false);
        wndFrame.setLocationRelativeTo(null);
        wndFrame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight));
        wndFrame.add(canvas);
        wndFrame.pack();
    }

    public int GetWndWidth()
    {
        return wndWidth;
    }

    public int GetWndHeight()
    {
        return wndHeight;
    }

    public Canvas GetCanvas()
    {
        return canvas;
    }

    public JFrame GetWndFrame()
    {
        return wndFrame;
    }
}
