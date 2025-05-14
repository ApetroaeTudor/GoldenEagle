
package PaooGame.States;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Entities.Enemy;
import PaooGame.Config.Constants;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;

import java.awt.*;
import java.nio.file.AccessDeniedException;

public class MenuState extends State  {
    private Rectangle startButton, continueButton,settingsButton, quitButton;
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
        continueButton = new Rectangle(centerX,topOffset+buttonHeight,200,50);
        settingsButton = new Rectangle(centerX, topOffset+buttonHeight*2, 200, 50);
        quitButton = new Rectangle(centerX, topOffset+buttonHeight*3, 200, 50);

        this.bgAnimation = new StaticItemAnimation(this.reflink,Constants.MAIN_MENU_BG_PATH,Constants.MAIN_MENU_BG_FRAME_NR,5,Constants.MAIN_MENU_BG_IMG_WIDTH,Constants.MAIN_MENU_BG_IMG_HEIGHT);
        this.bgAnimation.loadAnimation();
    }

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Enemy enemy) {

    }

    @Override
    public void update() {
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();
        this.bgAnimation.updateAnimation();

        if (mouse.getNumberOfMousePresses() > 0) {
            if (startButton.contains(mx, my)) {
                System.out.println("Se salveaza un STARTING SAVE in baza de date");
                try{
                    this.reflink.getDataProxy().resetBuffer(true);
                    this.reflink.setDataRefreshSignal(true);
                    switch (this.reflink.getDataProxy().load(Constants.CURRENT_STATE,true)){
                        case 1:
                            State.setState(this.reflink.getGame().getLevel1State());
                            break;
                        case 2:
                            State.setState(this.reflink.getGame().getLevel2State());
                            break;
                        case 3:
                            State.setState(this.reflink.getGame().getLevel3State());
                            break;
                    }
                } catch (AccessDeniedException e) {
                    System.err.println(e.getMessage());
                }
            }
            else if (settingsButton.contains(mx, my)) {
            }
            else if (quitButton.contains(mx, my)) {
                System.exit(0);
            }
            else if(continueButton.contains(mx,my)) {
                System.out.println("Loading data// adica continui normal");
                try{
                    this.reflink.getDataProxy().resetBuffer(true);
                    this.reflink.getDataProxy().loadBuffer(true);
                    this.reflink.setDataRefreshSignal(true);


                    switch (this.reflink.getDataProxy().load(Constants.CURRENT_STATE,true)){
                        case 1:
                            State.setState(this.reflink.getGame().getLevel1State());
                            break;
                        case 2:
                            State.setState(this.reflink.getGame().getLevel2State());
                            break;
                        case 3:
                            State.setState(this.reflink.getGame().getLevel3State());
                            break;
                    }
                } catch (AccessDeniedException e) {
                    System.err.println(e.getMessage());
                }
            }
            // Resetăm mouse-ul după ce a fost tratat
            mouse.mouseReleased(null);
        }
    }

    @Override
    public void draw(Graphics g) {


        this.bgAnimation.paintFullScreen(g,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);


        // Coordonate mouse
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        // Font frumos
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Funcție lambda pt. butoane
        drawButton(g2d, startButton, "New Game", mx, my);
        drawButton(g2d,continueButton,"Continue",mx,my);
        drawButton(g2d, settingsButton, "Settings", mx, my);
        drawButton(g2d, quitButton, "Quit", mx, my);
    }

    @Override
    public void restoreState() {

    }

    @Override
    public void loadState(boolean access) {

    }

    @Override
    public void storeState(boolean access) {

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