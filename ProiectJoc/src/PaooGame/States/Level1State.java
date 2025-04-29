package PaooGame.States;

import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.HUD.HealthBar;
import PaooGame.Hero.Hero;
import PaooGame.Input.MouseInput;
import PaooGame.Maps.Level1;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import PaooGame.HUD.PauseButton;


public class Level1State extends State {
    private Level1 level1;
    private Camera camera;
    private HealthBar healthBar;
    private PauseButton pauseButton;
    private int levelWidth;  // Lățimea totală a nivelului în pixeli
    private int levelHeight; // Înălțimea totală a nivelului în pixeli

    public Level1State(RefLinks refLink) {
        super(refLink);
        level1 = new Level1();
        camera = new Camera(0, 0);

        healthBar = new HealthBar(refLink.GetHero());
        pauseButton = new PauseButton(refLink.GetHero(), 80, 50);
        // Calculează dimensiunile totale ale nivelului
        levelWidth = Constants.LEVEL1_WIDTH * Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL1_HEIGHT * Constants.TILE_SIZE;
    }

    @Override
    public void Update() {
        this.refLink.GetHero().Update();
        Hero hero = refLink.GetHero();

        if (refLink.GetKeyManager().isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            State.SetState(refLink.GetGame().GetPauseMenuState());
        }

        MouseInput mouse = refLink.GetMouseInput();


        Point mousePos = new Point(mouse.getMouseX(), mouse.getMouseY());
        pauseButton.updateHover(mousePos.x, mousePos.y); // fără transformare

        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.SetState(refLink.GetGame().GetPauseMenuState()); // Acum trimite către meniul de pauză
            mouse.mouseReleased(null);
            return;
        }


        // Calculează centrul eroului
        float heroCenterX = hero.getX() + hero.getWidth() / 2;
        float heroCenterY = hero.getY() + hero.getHeight() / 2;

        // Calculează poziția camerei
        double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
        double cameraY = heroCenterY - (Constants.WINDOW_HEIGHT / 2) / camera.getScale();

        double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
        double maxCameraY = Constants.WINDOW_HEIGHT;// levelHeight - (Constants.WINDOW_HEIGHT / camera.getScale());

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY))+350;


        camera.setPosition(cameraX, cameraY);
    }

    @Override
    public void Draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        // Aplică transformarea camerei
        camera.apply(g2d);



        BufferedImage backgroundImage = this.refLink.getTileCache().getBackground(Constants.LEVEL1_BG_PATH);
        g.drawImage(backgroundImage, 0, 0, levelWidth, levelHeight, null); // Folosește levelWidth și levelHeight


//         Desenează tile-urile nivelului
        for (int i = 0; i < Constants.LEVEL1_TILE_NR; ++i) {
            int currentID = this.level1.getVisualIDs()[i];
            if (currentID != -1) {
                this.refLink.getTileCache()
                        .getTile(Constants.LEVEL1_TEXTURES_PATH, currentID)
                        .Draw(g, (i % Constants.LEVEL1_WIDTH) * Constants.TILE_SIZE,
                                (i / Constants.LEVEL1_WIDTH) * Constants.TILE_SIZE);
            }
        }


        this.refLink.GetHero().Draw(g);

        // Restabilește transformarea
        g2d.setTransform(originalTransform);
        healthBar.draw(g2d);
        pauseButton.draw(g2d);
    }

    public Level1 getLevel1() {
        return level1;
    }
}