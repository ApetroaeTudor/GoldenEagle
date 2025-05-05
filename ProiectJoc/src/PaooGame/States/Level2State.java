package PaooGame.States;

import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Entity;
import PaooGame.HUD.PauseButton;
import PaooGame.Input.MouseInput;
import PaooGame.Maps.Level2;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Level2State extends State{
    private Level2 level2;
    private Camera camera;
    private PauseButton pauseButton;
    private int levelWidth;  // Lățimea totală a nivelului în pixeli
    private int levelHeight; // Înălțimea totală a nivelului în pixeli

    protected boolean transitioning = false;
    protected boolean transition_to_fight = false;

    private Entity[] enemies;

    protected String stateName = Constants.LEVEL2_STATE;

    protected double targetBlackIntensity = 0.0;
    protected double blackFadeStep = 0.05;

    public Level2State(RefLinks reflink,Level2 level2){
        super(reflink);
        this.level2 = level2;
        camera = new Camera(0,0);

        //TODO enemies

        pauseButton = new PauseButton(reflink.getHero(),80,50);

        levelWidth = Constants.LEVEL2_WIDTH*Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL2_WIDTH*Constants.TILE_SIZE;
    }

    @Override
    public void restoreState(){

    }

    @Override
    public void update(){
        this.refLink.getHero().update();

        if(refLink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_ESCAPE)){
            State.setState(refLink.getGame().getPauseMenuState());
        }

        MouseInput mouse = refLink.getMouseInput();
        Point mousePos = new Point(mouse.getMouseX(),mouse.getMouseY());
        pauseButton.updateHover(mousePos.x,mousePos.y);

        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(refLink.getGame().getPauseMenuState()); // Acum trimite către meniul de pauză
            mouse.mouseReleased(null);
            return;
        }

        float heroCenterX = this.refLink.getHero().getX() + this.refLink.getHero().getWidth() / 2;
        float heroCenterY = this.refLink.getHero().getY() + this.refLink.getHero().getHeight() / 2;
        double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
        double cameraY = heroCenterY - (Constants.WINDOW_HEIGHT / 2) / camera.getScale()-1000;

        double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
        double maxCameraY = Constants.WINDOW_HEIGHT;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY))+350;

        camera.setPosition(cameraX, cameraY);

        if(this.refLink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){
            this.targetBlackIntensity = 0;

            this.refLink.getGame().getDeathState().restoreState();
            State.setState(this.refLink.getGame().getDeathState());
//            this.transitioning = false;
        }

    }

    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        camera.apply(g2d);

        BufferedImage backgroundImage = this.refLink.getTileCache().getBackground(Constants.LEVEL2_BG_PATH);
        g.drawImage(backgroundImage, 0, -210, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage,Constants.WINDOW_WIDTH,-210,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);

        for (int i = 0; i < Constants.LEVEL2_TILE_NR; ++i) {
            int currentID = this.level2.getVisualIDs()[i];
            if (currentID != -1) {
                this.refLink.getTileCache()
                        .getTile(Constants.LEVEL2_TEXTURES_PATH, currentID)
                        .Draw(g, (i % Constants.LEVEL2_WIDTH) * Constants.TILE_SIZE,
                                (i / Constants.LEVEL2_WIDTH) * Constants.TILE_SIZE);
            }
        }


        if(refLink.getHero().getHealth() == 0){
            this.targetBlackIntensity+=this.blackFadeStep;
            Color originalColor = g2d.getColor();
            if(this.targetBlackIntensity>=1.0){
                this.targetBlackIntensity = 1.0;

            }
            int alpha = (int)(this.targetBlackIntensity*255.0);
            g2d.setColor(new Color(0,0,0,alpha));
            g.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            g2d.setColor(originalColor);
        }

        this.refLink.getHero().Draw(g);
        g2d.setTransform(originalTransform);
        this.refLink.getHero().DrawHealthBar(g);
        pauseButton.draw(g2d);

    }

    public Level2 getLevel2(){return level2;}

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Entity enemy) {

    }



}
