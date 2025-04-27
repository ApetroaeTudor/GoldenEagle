
package PaooGame.States;

import PaooGame.Graphics.ImageLoader;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.*;

public class MenuState extends State  {
    private BufferedImage background;
    private Rectangle startButton, settingsButton, quitButton;

    public MenuState(RefLinks refLink) {
        super(refLink);

        // Încarcă imaginea de fundal
        background = ImageLoader.LoadImage("/textures/menu_background.png");
        int screenWidth = refLink.GetWidth();
        int screenHeight = refLink.GetHeight();
        System.out.println( screenHeight + " /// " + screenWidth);

        // Definim zonele butoanelor
        int centerX = refLink.GetWidth() / 2 -100 ;
        startButton = new Rectangle(centerX, 200, 200, 50);
        settingsButton = new Rectangle(centerX, 270, 200, 50);
        quitButton = new Rectangle(centerX, 340, 200, 50);
    }

    @Override
    public void Update() {
        MouseInput mouse = refLink.GetMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (startButton.contains(mx, my)) {
                State.SetState(refLink.GetGame().GetLevel1State());
            }
            else if (settingsButton.contains(mx, my)) {
            }
            else if (quitButton.contains(mx, my)) {
                System.exit(0);
            }
            // Resetăm mouse-ul după ce a fost tratat
            mouse.mouseReleased(null);
        }
    }

    @Override
    public void Draw(Graphics g) {
        // Desenează fundal

        if(background != null) {
            g.drawImage(background, 0, 0, refLink.GetWidth(), refLink.GetHeight(), null);
        } else {
            // Fallback dacă imaginea lipsește
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, refLink.GetWidth(), refLink.GetHeight());
        }


        // Coordonate mouse
        MouseInput mouse = refLink.GetMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        // Font frumos
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Funcție lambda pt. butoane
        drawButton(g2d, startButton, "Start Game", mx, my);
        drawButton(g2d, settingsButton, "Settings", mx, my);
        drawButton(g2d, quitButton, "Quit", mx, my);
    }

    private void drawButton(Graphics2D g2d, Rectangle button, String text, int mx, int my) {
        // Hover effect
        boolean hovered = button.contains(mx, my);

        // Culoare fundal buton
        if (hovered) {
            g2d.setColor(new Color(70, 130, 180)); // steel blue
        } else {
            g2d.setColor(new Color(40, 40, 40)); // dark grey
        }

        // Desenează butonul
        g2d.fillRoundRect(button.x, button.y, button.width, button.height, 20, 20);

        // Border
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(button.x, button.y, button.width, button.height, 20, 20);

        // Textul butonului centrat
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int textX = button.x + (button.width - textWidth) / 2;
        int textY = button.y + (button.height + textHeight) / 2 - 5;

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, textX, textY);
    }


//    // Adaugă mouse listener (poți să-l adaugi în Game sau JFrame)
//    public void mousePressed(MouseEvent e) {
//        int mx = e.getX();
//        int my = e.getY();
//
//        if (startButton.contains(mx, my)) {
//            // Start game
//            State.SetState(refLink.GetGame().GetLevel1State());
//        } else if (settingsButton.contains(mx, my)) {
//            // Settings - schimbă în SettingsState, dacă ai unul
//            System.out.println("Settings pressed");
//        } else if (quitButton.contains(mx, my)) {
//            System.exit(0);
//        }
//    }
}