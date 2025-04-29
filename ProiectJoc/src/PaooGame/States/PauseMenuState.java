package PaooGame.States;

import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;
import java.awt.*;

public class PauseMenuState extends State {
    private Rectangle continueButton, exitToMenuButton, exitButton;
    private Color panelColor = new Color(30, 30, 30, 220); // Chenar semi-transparent
    private Color borderColor = new Color(70, 130, 180);
    private Font titleFont = new Font("Arial", Font.BOLD, 42);

    public PauseMenuState(RefLinks refLink) {
        super(refLink);
        int panelWidth = 400;
        int centerX = refLink.GetWidth()/2 - panelWidth/2;
        int startY = 150;

        // Butoane centrate în chenar
        continueButton = new Rectangle(centerX + 50, startY + 100, 300, 50);
        exitToMenuButton = new Rectangle(centerX + 50, startY + 170, 300, 50);
        exitButton = new Rectangle(centerX + 50, startY + 240, 300, 50);
    }

    @Override
    public void Update() {
        MouseInput mouse = refLink.GetMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (continueButton.contains(mx, my)) {
                State.SetState(refLink.GetGame().GetLevel1State());
                mouse.mouseReleased(null);
            } else if (exitToMenuButton.contains(mx, my)) {
                // Resetează Level1State și revino la meniul principal
                refLink.GetGame().ResetLevel1State(); // Aici se apelează resetarea
                State.SetState(refLink.GetGame().GetMenuState());
                mouse.mouseReleased(null);
            } else if (exitButton.contains(mx, my)) {
                System.exit(0);
            }
        }
    }

    @Override
    public void Draw(Graphics g) {
        // Fundalul nivelului
        if (refLink.GetGame().GetLevel1State() != null) {
            refLink.GetGame().GetLevel1State().Draw(g);
        }

        Graphics2D g2d = (Graphics2D) g;

        // Overlay întunecat
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, refLink.GetWidth(), refLink.GetHeight());

        // Chenar principal
        int panelWidth = 400;
        int panelHeight = 350;
        int panelX = refLink.GetWidth()/2 - panelWidth/2;
        int panelY = 100;

        // Fundal chenar
        g2d.setColor(panelColor);
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        // Border chenar
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        // Titlu
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "PAUSED";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelX + (panelWidth - titleWidth)/2, panelY + 60);

        // Butoane
        int mx = refLink.GetMouseInput().getMouseX();
        int my = refLink.GetMouseInput().getMouseY();
        drawModernButton(g2d, continueButton, "CONTINUE", mx, my);
        drawModernButton(g2d, exitToMenuButton, "MAIN MENU", mx, my);
        drawModernButton(g2d, exitButton, "QUIT GAME", mx, my);
    }

    private void drawModernButton(Graphics2D g2d, Rectangle rect, String text, int mx, int my) {
        boolean hover = rect.contains(mx, my);

        // Fundal gradient
        GradientPaint gp = new GradientPaint(
                rect.x, rect.y,
                hover ? new Color(70, 130, 180) : new Color(60, 60, 60),
                rect.x, rect.y + rect.height,
                hover ? new Color(50, 100, 150) : new Color(40, 40, 40)
        );

        g2d.setPaint(gp);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Border
        g2d.setColor(hover ? new Color(160, 200, 255) : new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text))/2;
        int textY = rect.y + (rect.height + fm.getAscent())/2 - 4;
        g2d.drawString(text, textX, textY);
    }
}