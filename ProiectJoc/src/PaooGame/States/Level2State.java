package PaooGame.States;

import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Enemy;
import PaooGame.Entities.Entity;
import PaooGame.Entities.NPC;
import PaooGame.HUD.ContextHUD;
import PaooGame.HUD.MessageTriggerZone;
import PaooGame.HUD.PauseButton;
import PaooGame.Input.MouseInput;
import PaooGame.Items.BoosterItem;
import PaooGame.Items.FloppyItem;
import PaooGame.Items.SaveItem;
import PaooGame.Maps.Level2;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Objects;

public class Level2State extends State{
    private Level2 level2;
    private Camera camera;
    private PauseButton pauseButton;
    private int levelWidth;  // Lățimea totală a nivelului în pixeli
    private int levelHeight; // Înălțimea totală a nivelului în pixeli

    protected boolean transitioning = false;
    protected boolean transition_to_fight = false;
    private boolean isCameraSet = false;

    private Entity[] npcs;
    private int nrOfNpcs = 1;

    private ContextHUD contextHUD;





    private int nrOfEnemies = 3;
    private Enemy[] enemies;
    private SaveItem[] saves;
    private BoosterItem[] boosters;
    private int nrOfBoosters = 3;
    private int nrOfSaves = 1;
    private FloppyItem[] floppyDisks;


    protected String stateName = Constants.LEVEL2_STATE;

    protected double targetBlackIntensity = 0.0;
    protected double blackFadeStep = 0.05;

    public Level2State(RefLinks reflink,Level2 level2){
        super(reflink);
        this.level2 = level2;
        this.saves = new SaveItem[this.nrOfSaves];
        this.enemies = new Enemy[this.nrOfEnemies];
        this.floppyDisks = new FloppyItem[this.nrOfSaves];
        this.boosters = new BoosterItem[this.nrOfBoosters];
        this.npcs = new Entity[this.nrOfNpcs];

        this.boosters[0] = new BoosterItem(this.reflink,Constants.BOOSTER1_X,Constants.BOOSTER1_Y);
        this.boosters[1] = new BoosterItem(this.reflink,Constants.BOOSTER2_X,Constants.BOOSTER2_Y);
        this.boosters[2] = new BoosterItem(this.reflink,Constants.BOOSTER3_X,Constants.BOOSTER3_Y);

        this.npcs[0] = new NPC(this.reflink,Constants.GOBLIN_POS_X,Constants.GOBLIN_POS_Y);

        this.contextHUD = new ContextHUD(this.reflink.getHero());


        levelWidth = Constants.LEVEL2_WIDTH*Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL2_WIDTH*Constants.TILE_SIZE;
        camera = new Camera(0,0);

        this.saves[0] = new SaveItem(this.reflink,Constants.LEVEL2_SAVE1_X,Constants.LEVEL2_SAVE1_Y);

        this.enemies[0] = new Enemy(this.reflink,Constants.BASIC_SKELETON1_X,Constants.BASIC_SKELETON1_Y,Constants.BASIC_SKELETON_NAME); //basicSkeleton0
        this.enemies[1] = new Enemy(this.reflink,Constants.STRONG_SKELETON1_X,Constants.STRONG_SKELETON1_Y,Constants.STRONG_SKELETON_NAME); //strongSkeleton0
        this.enemies[2] = new Enemy(this.reflink,Constants.BASIC_SKELETON2_X,Constants.BASIC_SKELETON2_Y,Constants.BASIC_SKELETON_NAME); //basicSkeleton1

        this.floppyDisks[0] = new FloppyItem(this.reflink,Constants.LEVEL2_SAVE1_X+10,Constants.LEVEL2_SAVE1_Y-10);

        pauseButton = new PauseButton(reflink.getHero(),80,50);


        int platformX = 415; // Coordonata X a platformei (ajustați în funcție de nivel)
        int platformY = 150; // Coordonata Y a platformei
        int triggerWidth = 80;
        int triggerHeight = 50;
        contextHUD.addTrigger(new MessageTriggerZone(
                platformX, platformY, triggerWidth, triggerHeight,
                "Parcurge peste pentru a trece la nivelul următor!"
        ));

        int platformX2 = 800; // Coordonata X a platformei (ajustați în funcție de nivel)
        int platformY2 = 220; // Coordonata Y a platformei
        int triggerWidth2 = 80;
        int triggerHeight2 = 50;
        contextHUD.addTrigger(new MessageTriggerZone(
                platformX2, platformY2, triggerWidth2, triggerHeight2,
                "Ai grija, unde flori sunt otravitoare! "
        ));

        int platformX3 = 1830; // Coordonata X a platformei (ajustați în funcție de nivel)
        int platformY3 = 380; // Coordonata Y a platformei
        int triggerWidth3 = 60;
        int triggerHeight3 = 50;
        contextHUD.addTrigger(new MessageTriggerZone(
                platformX3, platformY3, triggerWidth3, triggerHeight3,
                "Apasa E pentru a deschide usa catre nivelul 3"
        ));

        int platformX4 = 1500; // Coordonata X a platformei (ajustați în funcție de nivel)
        int platformY4 = 280; // Coordonata Y a platformei
        int triggerWidth4 = 60;
        int triggerHeight4 = 50;
        contextHUD.addTrigger(new MessageTriggerZone(
                platformX4, platformY4, triggerWidth4, triggerHeight4,
                "Acest jump este foarte riscant, asigura te ca esti bine pozitionat"
        ));

    }

    @Override
    public void restoreState(){
            this.enemies[0].setX(Constants.BASIC_SKELETON1_X);
            this.enemies[0].setY(Constants.BASIC_SKELETON1_Y);

            this.enemies[1].setX(Constants.STRONG_SKELETON1_X);
            this.enemies[1].setY(Constants.STRONG_SKELETON1_Y);

            this.enemies[2].setX(Constants.BASIC_SKELETON2_X);
            this.enemies[2].setY(Constants.BASIC_SKELETON2_Y);
    }

    @Override
    public void update(){

        for (Entity npc : npcs) {
            if (npc != null) {
                npc.update();
            }
        }


        for (Entity npc : npcs) {
            if (npc != null && npc instanceof NPC) {
                NPC currentNPC = (NPC) npc;
                if (!currentNPC.isActive() &&
                        reflink.getHero().getHitbox().intersects(currentNPC.getHitbox()) &&
                        reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_T)) {
                    State.setState(reflink.getGame().getShopState());
                }
            }
        }


        for(BoosterItem item : this.boosters){
            item.updateItem();
        }

        if(Objects.equals(State.getState().getStateName(), this.getStateName())){
            this.reflink.setCurrentRunningLevel(this.reflink.getGame().getLevel2State());
        }

        int nrOfTouchedBoosters = 0;
        for(BoosterItem item : this.boosters){
            if(item.getHitbox().intersects(this.reflink.getHero().getHitbox())){
                nrOfTouchedBoosters++;
                this.reflink.getHero().setJumpStrength(Constants.HERO_BOOSTED_JUMP_STRENGTH);
            }
        }
        if(nrOfTouchedBoosters>0){
            this.reflink.getHero().setJumpStrength(Constants.HERO_BOOSTED_JUMP_STRENGTH);
        }
        else{
            this.reflink.getHero().setJumpStrength(Constants.HERO_BASE_JUMP_STRENGTH);
        }
        nrOfTouchedBoosters = 0;



        this.reflink.getHero().update();
        this.floppyDisks[0].updateItem();



        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].updateItem();
        }
        if(this.reflink.getHero().getHitbox().intersects(this.saves[0].getHitbox())){
            if(this.reflink.getHero().getNrOfCollectedSaves() == 1){
                if(this.reflink.getHero().getHitbox().intersects(this.floppyDisks[0].getHitbox())){
                    this.reflink.getHero().setNrOfCollectedSaves(this.reflink.getHero().getNrOfCollectedSaves()+1);
                    this.reflink.getHero().setNrOfEscapes(this.reflink.getHero().getMaxNrOfEscapes());
                    this.reflink.setHeroStoreDoneSignal(false);
                    this.reflink.setLevel2StoreDoneSignal(false);
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
            if(enemy!=null){
                if(enemy.getHealth()==0){
                    enemy.nullifyHitbox();
                }
                else{
                    if(reflink.getHero().getHitbox().intersects(enemy.getHitbox())&& reflink.getHero().getCanEngage()){
                        enemy.setIsEngaged(true);
                        this.transitioning = true;
                        this.transition_to_fight = true;
                        reflink.getGame().getFightState().setEnemy(enemy);

                    }
                }
                enemy.update();
            }

        }
        if(this.transition_to_fight && this.targetBlackIntensity==1) {
            this.targetBlackIntensity = 0;
            this.transitioning = false;
            this.transition_to_fight = false;
//            refLink.getGame().getFightState().restoreState();

            State.setState(reflink.getGame().getFightState());
        }

        if(this.reflink.getHero().getX()>1880){
            this.reflink.getHero().setX(1870);
            this.reflink.getHero().getHitbox().setX(1870);
        }
        if(this.reflink.getHero().getX()>1855 && this.reflink.getHero().getY()<400){
            this.transitioning = true;
        }

        if(reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_ESCAPE)){
            State.setState(reflink.getGame().getPauseMenuState());
        }

        MouseInput mouse = reflink.getMouseInput();
        Point mousePos = new Point(mouse.getMouseX(),mouse.getMouseY());
        pauseButton.updateHover(mousePos.x,mousePos.y);

        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(reflink.getGame().getPauseMenuState()); // Acum trimite către meniul de pauză
            mouse.mouseReleased(null);
            return;
        }

        if(State.getState().getStateName() == this.stateName && !this.isCameraSet){
            float heroCenterX = this.reflink.getHero().getX() + this.reflink.getHero().getWidth() / 2;
            float heroCenterY = this.reflink.getHero().getY() + this.reflink.getHero().getHeight() / 2;
            double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
            double cameraY = heroCenterY - (Constants.WINDOW_HEIGHT / 2) / camera.getScale()-1000;

            double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
            double maxCameraY = Constants.WINDOW_HEIGHT;

            cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
            cameraY = Math.max(0, Math.min(cameraY, maxCameraY))+350;

            camera.setPosition(cameraX, cameraY);
        }
        camera.updatePosition(this.reflink.getHero().getVelocityX(),0);




        if(this.reflink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){
            this.targetBlackIntensity = 0;

            this.reflink.getGame().getDeathState().restoreState();
            State.setState(this.reflink.getGame().getDeathState());
//            this.transitioning = false;
        }
        if(this.transitioning && this.targetBlackIntensity == 1 &&this.reflink.getHero().getX()>1850){
            this.targetBlackIntensity = 0;
            this.reflink.getHero().setJumpStrength(Constants.HERO_BASE_JUMP_STRENGTH);
            this.transitioning = false;


//            this.reflink.setDataStoreSignal(true);

            if(this.reflink.getHero().getNrOfCompletedLevels() == 1){
                this.reflink.getHero().setNrOfCompletedLevels(2);
                this.reflink.setLevel2StoreDoneSignal(false);
                this.storeState(true);

                this.reflink.setHeroStoreDoneSignal(false);
                this.reflink.getHero().storeHeroState(true);
                try{
                    this.reflink.getDataProxy().storeBuffer(true);
                } catch (AccessDeniedException e){
                    System.err.println(e.getMessage());
                }
            }

            this.reflink.getHero().setX(Constants.HERO_LEVEL3_STARTING_X);
            this.reflink.getHero().getHitbox().setX(Constants.HERO_LEVEL3_STARTING_X);
            this.reflink.getHero().setY(Constants.HERO_LEVEL3_STARTING_Y);
            this.reflink.getHero().getHitbox().setY(Constants.HERO_LEVEL3_STARTING_Y);
            this.reflink.getHero().setJumpStrength(-3.5f);
            this.reflink.getHero().setNrOfCompletedLevels(2);
            State.setState(this.reflink.getGame().getLevel3State());





        }



        this.contextHUD.update();
    }

    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        camera.apply(g2d);

        BufferedImage backgroundImage = this.reflink.getTileCache().getBackground(Constants.LEVEL2_BG_PATH);
        g.drawImage(backgroundImage, 0, -210, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage,Constants.WINDOW_WIDTH,-210,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);

        for (int i = 0; i < Constants.LEVEL2_TILE_NR; ++i) {
            int currentID = this.level2.getVisualIDs()[i];
            if (currentID != -1) {
                this.reflink.getTileCache()
                        .getTile(Constants.LEVEL2_TEXTURES_PATH, currentID)
                        .Draw(g, (i % Constants.LEVEL2_WIDTH) * Constants.TILE_SIZE,
                                (i / Constants.LEVEL2_WIDTH) * Constants.TILE_SIZE);
            }
        }

        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].drawItem(g);
        }

        for(Enemy enemy : enemies){
            if(enemy!=null && enemy.getHealth()>0){
                enemy.draw(g);
            }
        }

        for(BoosterItem item : this.boosters){
            item.updateItem();
            item.drawItem(g);
        }

        if(reflink.getHero().getHealth() == 0 || (this.transitioning&&this.reflink.getHero().getX()>1850) || this.transition_to_fight){
            this.targetBlackIntensity+=this.blackFadeStep;
            Color originalColor = g2d.getColor();
            if(this.targetBlackIntensity>=1.0){
                this.targetBlackIntensity = 1.0;

            }
            int alpha = (int)(this.targetBlackIntensity*255.0);
            g2d.setColor(new Color(0,0,0,alpha));
            g.fillRect((int)this.reflink.getHero().getX()-Constants.WINDOW_WIDTH/2,0,Constants.WINDOW_WIDTH*2,Constants.WINDOW_HEIGHT*2);
            g2d.setColor(originalColor);
        }

        if(this.reflink.getHero().getNrOfCollectedSaves()==1 /* && this.floppyDisks[0].getDrawable()*/){
            this.floppyDisks[0].drawItem(g);
        }

        for (Entity npc : npcs) {
            if (npc != null) {
                npc.draw(g);
            }
        }


        this.reflink.getHero().draw(g);
        g2d.setTransform(originalTransform);
        this.reflink.getHero().DrawHealthBar(g);
        pauseButton.draw(g2d);



        this.contextHUD.draw(g2d);
    }

    public Level2 getLevel2(){return level2;}

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Enemy enemy) {

    }

    @Override
    public void loadState(boolean access){
        if(this.reflink.getLevel2RefreshDoneSignal()) {
            return;
        }
        try{
            this.enemies[0].setHealth(this.reflink.getDataProxy().load(Constants.BASIC_SKELETON0_HEALTH,access));
            this.enemies[1].setHealth(this.reflink.getDataProxy().load(Constants.STRONG_SKELETON0_HEALTH,access));
            this.enemies[2].setHealth(this.reflink.getDataProxy().load(Constants.BASIC_SKELETON1_HEALTH,access));
            this.reflink.setLevel2RefreshDoneSignal(true);
        }catch (AccessDeniedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void storeState(boolean access) {
        if(this.reflink.getLevel2StoreDoneSignal()){
            return;
        }
        try{
            if(State.getState().getStateName().compareTo(this.stateName) == 0){
                this.reflink.getDataProxy().store(Constants.CURRENT_STATE,2,access);
            }
            this.reflink.getDataProxy().store(Constants.TIMESTAMP,(int) Instant.now().getEpochSecond(),access);
            this.reflink.getDataProxy().store(Constants.BASIC_SKELETON0_HEALTH,(int)this.enemies[0].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.STRONG_SKELETON0_HEALTH,(int)this.enemies[1].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.BASIC_SKELETON1_HEALTH,(int)this.enemies[2].getHealth(),access);
            this.reflink.setLevel2StoreDoneSignal(true);
        }catch (AccessDeniedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }


}
