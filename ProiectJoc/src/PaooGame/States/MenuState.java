
package PaooGame.States;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Entities.Entity;
import PaooGame.Config.Constants;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;
import java.awt.image.BufferedImage;
import java.awt.*;

public class MenuState extends State  {
    private Rectangle startButton, settingsButton, quitButton;
    private StaticItemAnimation bgAnimation;

    private int buttonHeight = 70;
    private int topOffset = 300;

    protected String stateName = Constants.MENU_STATE;
    public MenuState(RefLinks refLink) {
        super(refLink);

        // Încarcă imaginea de fundal
        int screenWidth = refLink.getWidth();
        int screenHeight = refLink.getHeight();
        System.out.println( screenHeight + " /// " + screenWidth);

        // Definim zonele butoanelor
        int centerX = refLink.getWidth() / 2 -100 ;
        startButton = new Rectangle(centerX, topOffset, 200, 50);
        settingsButton = new Rectangle(centerX, topOffset+buttonHeight, 200, 50);
        quitButton = new Rectangle(centerX, topOffset+buttonHeight*2, 200, 50);

        this.bgAnimation = new StaticItemAnimation(this.refLink,Constants.MAIN_MENU_BG_PATH,Constants.MAIN_MENU_BG_FRAME_NR,5,Constants.MAIN_MENU_BG_IMG_WIDTH,Constants.MAIN_MENU_BG_IMG_HEIGHT);
        this.bgAnimation.loadAnimation();
    }

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Entity enemy) {

    }

    @Override
    public void update() {
        MouseInput mouse = refLink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();
        this.bgAnimation.updateAnimation();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (startButton.contains(mx, my)) {
                State.setState(refLink.getGame().getLevel1State());
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
    public void draw(Graphics g) {


        this.bgAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);


        // Coordonate mouse
        MouseInput mouse = refLink.getMouseInput();
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

    @Override
    public void restoreState() {

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

}