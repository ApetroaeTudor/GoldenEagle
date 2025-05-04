package PaooGame.States;

import PaooGame.Entities.Entity;
import PaooGame.Entities.Tiger;
import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Hero;
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
    private PauseButton pauseButton;
    private int levelWidth;  // Lățimea totală a nivelului în pixeli
    private int levelHeight; // Înălțimea totală a nivelului în pixeli

    private int tiger1X =400;
    private int tiger1Y =450;
    private int tiger2X =720;
    private int tiger2Y =470;




    protected boolean transitioning = false;
    protected boolean transition_to_fight = false;

    private Entity [] enemies;

    protected String stateName = Constants.LEVEL1_STATE;

    protected double targetBlackIntensity = 0.0;
    protected double blackFadeStep = 0.05;

    public Level1State(RefLinks refLink) {
        super(refLink);
        level1 = new Level1();
        camera = new Camera(0, 0);

        enemies = new Entity[2];
        enemies[0] = new Tiger(this.refLink,this.tiger1X,this.tiger1Y);
        enemies[1] = new Tiger(this.refLink,this.tiger2X,this.tiger2Y);

        pauseButton = new PauseButton(refLink.getHero(), 80, 50);
        pauseButton = new PauseButton(refLink.getHero(), 80, 50);
        // Calculează dimensiunile totale ale nivelului
        levelWidth = Constants.LEVEL1_WIDTH * Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL1_HEIGHT * Constants.TILE_SIZE;
    }

    @Override
    public void restoreState() {
        this.enemies[0].setX(this.tiger1X);
        this.enemies[0].setY(this.tiger1Y);
        this.enemies[1].setX(this.tiger2X);
        this.enemies[1].setY(this.tiger2Y);
        for(Entity enemy: enemies){
            enemy.restoreEntity();
        }
    }


    @Override
    public void update() {
        this.refLink.getHero().Update();
        Hero hero = refLink.getHero();


        for(Entity enemy : enemies){
            if(enemy.getHealth()==0){
                enemy.nullifyHitbox();
            }
            else{
                if(refLink.getHero().getHitbox().intersects(enemy.getHitbox())){
                    enemy.setIsEngaged(true);
                    this.transitioning = true;
                    this.transition_to_fight = true;
                    refLink.getGame().getFightState().setEnemy(enemy);

                }
            }



            enemy.Update();
        }

        if (refLink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            State.setState(refLink.getGame().getPauseMenuState());
        }

        MouseInput mouse = refLink.getMouseInput();


        Point mousePos = new Point(mouse.getMouseX(), mouse.getMouseY());
        pauseButton.updateHover(mousePos.x, mousePos.y); // fără transformare

        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(refLink.getGame().getPauseMenuState()); // Acum trimite către meniul de pauză
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

        if(this.transition_to_fight && this.targetBlackIntensity==1) {
            FightState fightState = (FightState) refLink.getGame().getFightState();
            this.targetBlackIntensity = 0;
            this.transitioning = false;
            this.transition_to_fight = false;
            State.setState(fightState);


        }
        if(this.refLink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){
            this.targetBlackIntensity = 0;

            this.refLink.getGame().getDeathState().restoreState();
            State.setState(this.refLink.getGame().getDeathState());
//            this.transitioning = false;
        }
    }

    @Override
    public void draw(Graphics g) {
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



        for(Entity enemy : enemies){
            if(enemy.getHealth()>0){
                enemy.Draw(g);
            }
        }

        if(this.transitioning) {
            Color originalColor = g2d.getColor();
            this.targetBlackIntensity += this.blackFadeStep;
            if(this.targetBlackIntensity>=1){
                this.targetBlackIntensity = 1.0;
            }
            int alpha = (int)(this.targetBlackIntensity * 255.0);

            g2d.setColor(new Color(0, 0, 0, alpha));
            g.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

            g2d.setColor(originalColor);
        }

        // Restabilește transformarea
        this.refLink.getHero().Draw(g);
        g2d.setTransform(originalTransform);
        this.refLink.getHero().DrawHealthBar(g);
        pauseButton.draw(g2d);

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
    }



    public Level1 getLevel1() {
        return level1;
    }

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Entity enemy) {

    }
}