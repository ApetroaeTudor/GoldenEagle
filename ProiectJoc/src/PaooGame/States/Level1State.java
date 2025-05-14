package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Hero;
import PaooGame.HUD.ContextHUD;
import PaooGame.HUD.MessageTriggerZone;
import PaooGame.Input.MouseInput;
import PaooGame.Items.FloppyItem;
import PaooGame.Items.SaveItem;
import PaooGame.Maps.Level1;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.file.AccessDeniedException;
import java.time.Instant;

import PaooGame.HUD.PauseButton;

import javax.swing.*;


public class Level1State extends State {
    private Level1 level1;
    private Camera camera;
    private PauseButton pauseButton;
    private int levelWidth;  // Lățimea totală a nivelului în pixeli
    private int levelHeight; // Înălțimea totală a nivelului în pixeli

    private int tiger1X =Constants.TIGER1_X;
    private int tiger1Y =Constants.TIGER1_Y;
    private int tiger2X =Constants.TIGER2_X;
    private int tiger2Y =Constants.TIGER2_Y;

    private SaveItem[] saves;
    private int nrOfSaves = 1;
    private FloppyItem[] floppyDisks;

    private ContextHUD contextHUD;

    private Timer saveTimer;
    private int saveDelayMillis = 100;





    protected boolean transitioning = false;
    protected boolean transition_to_fight = false;

    private Enemy [] enemies;

    protected String stateName = Constants.LEVEL1_STATE;

    protected double targetBlackIntensity = 0.0;
    protected double blackFadeStep = 0.05;

    private boolean isSwitchingToLevel2 = false;

    public Level1State(RefLinks refLink, Level1 level1) {
        super(refLink);
        this.level1 = level1;
        camera = new Camera(0, 0);
        this.saves = new SaveItem[this.nrOfSaves];

        this.contextHUD = new ContextHUD(refLink.getHero());


        enemies = new Enemy[2];
        floppyDisks = new FloppyItem[this.nrOfSaves];

        enemies[0] = new Enemy(this.reflink,this.tiger1X,this.tiger1Y,Constants.TIGER_NAME); //tiger0
        enemies[1] = new Enemy(this.reflink,this.tiger2X,this.tiger2Y,Constants.TIGER_NAME); //tiger1

        this.saves[0] = new SaveItem(this.reflink,Constants.LEVEL1_SAVE1_X,Constants.LEVEL1_SAVE1_Y);
        this.floppyDisks[0] = new FloppyItem(this.reflink,Constants.LEVEL1_SAVE1_X+10,Constants.LEVEL1_SAVE1_Y-30);

        pauseButton = new PauseButton(refLink.getHero(), 80, 50);
        // Calculează dimensiunile totale ale nivelului
        levelWidth = Constants.LEVEL1_WIDTH * Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL1_HEIGHT * Constants.TILE_SIZE;


        for (Enemy enemy : enemies) {
            contextHUD.addTrigger(new MessageTriggerZone(
                    enemy, -30, -100, 120, 150,
                    "Învinge tigrul!"
            ));
        }
        contextHUD.addTrigger(new MessageTriggerZone(
                850, 600, 100, 50, // x, y, width, height
                "Sari în peșteră!"
        ));


        this.saveTimer = new Timer(this.saveDelayMillis,e->{
            this.reflink.setDataStoreSignal(true);
            try{
                this.reflink.getDataProxy().store(Constants.CURRENT_STATE,2,true);
            } catch (AccessDeniedException | IllegalArgumentException exc){
                System.err.println(exc.getMessage());
            }
        });
        this.saveTimer.setRepeats(false);
    }

    @Override
    public void restoreState() {
        this.enemies[0].setX(Constants.TIGER1_X);
        this.enemies[0].setY(Constants.TIGER1_Y);

        this.enemies[1].setX(Constants.TIGER2_X);
        this.enemies[1].setY(Constants.TIGER2_Y);
    }


    @Override
    public void update() {
        this.reflink.getHero().update();

        this.floppyDisks[0].updateItem();

        Hero hero = reflink.getHero();
        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].updateItem();
        }
        if(this.reflink.getHero().getHitbox().intersects(this.saves[0].getHitbox())){
            if(this.reflink.getHero().getNrOfCollectedSaves() == 0){
                if(this.reflink.getHero().getHitbox().intersects(this.floppyDisks[0].getHitbox())){
                    this.reflink.getHero().setNrOfCollectedSaves(this.reflink.getHero().getNrOfCollectedSaves()+1);
                    this.reflink.getHero().setNrOfEscapes(this.reflink.getHero().getMaxNrOfEscapes());
                    this.reflink.setHeroStoreDoneSignal(false);
                    this.reflink.setLevel1StoreDoneSignal(false);
                    this.reflink.getHero().storeHeroState(true);
                    this.storeState(true);
                    try{
                        this.reflink.getDataProxy().storeBuffer(true);
                    } catch (AccessDeniedException e){
                        System.err.println(e.getMessage());
                    }
                }
            }
        }


        for(Enemy enemy : enemies){
            if(enemy.getHealth()==0){
                enemy.nullifyHitbox();
            }
            else{
                if(reflink.getHero().getHitbox().intersects(enemy.getHitbox()) && reflink.getHero().getCanEngage()){
                    enemy.setIsEngaged(true);
                    this.transitioning = true;
                    this.transition_to_fight = true;
                    reflink.getGame().getFightState().setEnemy(enemy);

                }
            }

            enemy.update();
        }

        if (reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            State.setState(reflink.getGame().getPauseMenuState());
        }

        MouseInput mouse = reflink.getMouseInput();


        Point mousePos = new Point(mouse.getMouseX(), mouse.getMouseY());
        pauseButton.updateHover(mousePos.x, mousePos.y); // fără transformare

        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(reflink.getGame().getPauseMenuState()); // Acum trimite către meniul de pauză
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


        if(!reflink.getHero().getIsDying()){
            camera.setPosition(cameraX, cameraY);
        }

        if(this.reflink.getHero().getX() > 735 && this.reflink.getHero().getX() < 930 && this.reflink.getHero().getY() > 650){
            this.isSwitchingToLevel2 = true;
        }

        if(this.isSwitchingToLevel2 && this.targetBlackIntensity ==1){
            this.targetBlackIntensity = 0;
            this.isSwitchingToLevel2 = false;


            if(this.reflink.getHero().getNrOfCompletedLevels() == 0){
                this.reflink.getHero().setNrOfCompletedLevels(1);
                this.reflink.getHero().getHitbox().setY(650);
                this.reflink.setLevel1StoreDoneSignal(false);
                this.storeState(true);
                this.reflink.setHeroStoreDoneSignal(false);
                this.reflink.getHero().storeHeroState(true);
                try{
                    this.reflink.getDataProxy().storeBuffer(true);
                } catch (AccessDeniedException e){
                    System.err.println(e.getMessage());
                }
            }


//            this.reflink.setDataStoreSignal(true);


            this.reflink.getHero().setX(Constants.HERO_LEVEL2_STARTING_X);
            this.reflink.getHero().setY(Constants.HERO_LEVEL2_STARTING_Y);
            this.reflink.getHero().getHitbox().setX(Constants.HERO_LEVEL2_STARTING_X);
            this.reflink.getHero().getHitbox().setY((Constants.HERO_LEVEL2_STARTING_Y));
            this.reflink.getHero().setJumpStrength(Constants.HERO_LEVEL2_JUMP_STRENGTH);
            this.reflink.getHero().setNrOfCollectedSaves(1);

            State.setState(reflink.getGame().getLevel2State());
//            this.saveTimer.start();


        }

        if(this.transition_to_fight && this.targetBlackIntensity==1 && !this.isSwitchingToLevel2) {
            this.targetBlackIntensity = 0;
            this.transitioning = false;
            this.transition_to_fight = false;
//            refLink.getGame().getFightState().restoreState();

            State.setState(reflink.getGame().getFightState());


        }
        if(this.reflink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){
            this.targetBlackIntensity = 0;

            this.reflink.getGame().getDeathState().restoreState();
            State.setState(this.reflink.getGame().getDeathState());
//            this.transitioning = false;
        }

        contextHUD.update();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        // Aplică transformarea camerei
        camera.apply(g2d);





        BufferedImage backgroundImage = this.reflink.getTileCache().getBackground(Constants.LEVEL1_BG_PATH);
        g.drawImage(backgroundImage, 0, 0, levelWidth, levelHeight, null); // Folosește levelWidth și levelHeight


//         Desenează tile-urile nivelului
        for (int i = 0; i < Constants.LEVEL1_TILE_NR; ++i) {
            int currentID = this.level1.getVisualIDs()[i];
            if (currentID != -1) {
                this.reflink.getTileCache()
                        .getTile(Constants.LEVEL1_TEXTURES_PATH, currentID)
                        .Draw(g, (i % Constants.LEVEL1_WIDTH) * Constants.TILE_SIZE,
                                (i / Constants.LEVEL1_WIDTH) * Constants.TILE_SIZE);
            }
        }

        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].drawItem(g);
        }



        for(Enemy enemy : enemies){
            if(enemy.getHealth()>0){
                enemy.draw(g);
            }
        }

        if(this.reflink.getHero().getNrOfCollectedSaves()==0 /* && this.floppyDisks[0].getDrawable()*/){
            this.floppyDisks[0].drawItem(g);
        }

        if(this.transitioning || this.isSwitchingToLevel2) {
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
        this.reflink.getHero().draw(g);
        g2d.setTransform(originalTransform);
        this.reflink.getHero().DrawHealthBar(g);
        pauseButton.draw(g2d);

        if(reflink.getHero().getHealth() == 0){
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

        contextHUD.draw(g2d);

    }



    public Level1 getLevel1() {
        return level1;
    }

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Enemy enemy) {

    }

    @Override
    public void loadState(boolean access){
        if(this.reflink.getLevel1RefreshDoneSignal()) {
            return;
        }
        try{
            this.enemies[0].setHealth(this.reflink.getDataProxy().load(Constants.TIGER0_HEALTH,access));
            this.enemies[1].setHealth(this.reflink.getDataProxy().load(Constants.TIGER1_HEALTH,access));
            this.reflink.setLevel1RefreshDoneSignal(true);

        }catch (AccessDeniedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void storeState(boolean access) {
        if(this.reflink.getLevel1StoreDoneSignal()){
            return;
        }
        try{
            if(State.getState().getStateName().compareTo(this.stateName) == 0){
                this.reflink.getDataProxy().store(Constants.CURRENT_STATE,1,access);
            }
            this.reflink.getDataProxy().store(Constants.TIMESTAMP,(int) Instant.now().getEpochSecond(),access);
            this.reflink.getDataProxy().store(Constants.TIGER0_HEALTH,(int)this.enemies[0].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.TIGER1_HEALTH,(int)this.enemies[1].getHealth(),access);
            this.reflink.setLevel1StoreDoneSignal(true);
        }catch (AccessDeniedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }


}