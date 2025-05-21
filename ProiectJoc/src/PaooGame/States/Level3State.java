package PaooGame.States;
import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;
import PaooGame.Entities.Enemy;
import PaooGame.HUD.PauseButton;
import PaooGame.Input.MouseInput;
import PaooGame.Items.FloppyItem;
import PaooGame.Items.BonfireItem;
import PaooGame.Items.WhipItem;
import PaooGame.Maps.Level;
import PaooGame.Maps.Level3;
import PaooGame.RefLinks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Objects;


public class Level3State extends State{
    private Level3 level3;
    private Camera camera;
    private PauseButton pauseButton;
    private int levelWidth;
    private int levelHeight;

    private LinkedList<Point> MarkedHooks;
    private Timer unmarkHookTimer;
    private Timer bossDefeatedDelayTimer;
    private int hookTimeoutMillis = 100;
    private int bossDefeatedTimeoutMillis = 300;

    private boolean bossDefeated = false;


    private WhipItem whip;



    private boolean cameraIsSet = false;
    private boolean adjustingCameraForDepth = false;
    private boolean adjustingCameraForArena = false;

    protected boolean transitioning = false;
    protected boolean transition_to_fight = false;

    private Enemy[] enemies;
    private int nrOfEnemies = 5;


    private BonfireItem[] saves;
    private int nrOfSaves = 4;
    private FloppyItem[] floppyDisks;

    protected String stateName = Constants.LEVEL3_STATE;

    protected double targetBlackIntensity = 0.0;
    protected double blackFadeStep = 0.05;

    public Level3State(RefLinks reflink, Level3 level3){
        super(reflink);
        this.whip = new WhipItem(this.reflink,Constants.WHIP_POSITION_X,Constants.WHIP_POSITION_Y);
        this.saves = new BonfireItem[this.nrOfSaves];
        this.floppyDisks = new FloppyItem[this.nrOfSaves];

        this.level3 = level3;
        this.MarkedHooks = new LinkedList<Point>();
        levelWidth = Constants.LEVEL3_WIDTH*Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL3_HEIGHT*Constants.TILE_SIZE;
        camera = new Camera(0,0);
        this.bossDefeatedDelayTimer = new Timer(this.bossDefeatedTimeoutMillis, e->{
            this.bossDefeated = true;
        });
        this.bossDefeatedDelayTimer.setRepeats(false);
        this.unmarkHookTimer = new Timer(hookTimeoutMillis,null);
        this.unmarkHookTimer.setRepeats(false);

        pauseButton = new PauseButton(reflink.getHero(),80,50);

        this.enemies = new Enemy[this.nrOfEnemies];

        this.enemies[0] = new Enemy(this.reflink,Constants.WIZARD_X,Constants.WIZARD_Y,Constants.WIZARD_NAME); //wizard
        this.enemies[1] = new Enemy(this.reflink,Constants.MINOTAUR1_X,Constants.MINOTAUR1_Y,Constants.MINOTAUR_NAME); //minotaur0
        this.enemies[2] = new Enemy(this.reflink,Constants.MINOTAUR2_X,Constants.MINOTAUR2_Y,Constants.MINOTAUR_NAME); //minotaur1
        this.enemies[3] = new Enemy(this.reflink,Constants.GHOST1_X,Constants.GHOST1_Y,Constants.GHOST_NAME); //ghost0
        this.enemies[4] = new Enemy(this.reflink,Constants.GHOST1_X,Constants.GHOST2_Y,Constants.GHOST_NAME); //ghost1


        this.saves[0] = new BonfireItem(this.reflink,Constants.LEVEL3_SAVE1_X,Constants.LEVEL3_SAVE1_Y);
        this.saves[1] = new BonfireItem(this.reflink,Constants.LEVEL3_SAVE2_X,Constants.LEVEL3_SAVE2_Y);
        this.saves[2] = new BonfireItem(this.reflink,Constants.LEVEL3_SAVE3_X,Constants.LEVEL3_SAVE3_Y);
        this.saves[3] = new BonfireItem(this.reflink,Constants.LEVEL3_SAVE4_X,Constants.LEVEL3_SAVE4_Y);
        this.floppyDisks[0] = new FloppyItem(this.reflink,Constants.LEVEL3_SAVE1_X+10,Constants.LEVEL3_SAVE1_Y-10);
        this.floppyDisks[1] = new FloppyItem(this.reflink,Constants.LEVEL3_SAVE2_X+10,Constants.LEVEL3_SAVE2_Y-10);
        this.floppyDisks[2] = new FloppyItem(this.reflink,Constants.LEVEL3_SAVE3_X+10,Constants.LEVEL3_SAVE3_Y-10);
        this.floppyDisks[3] = new FloppyItem(this.reflink,Constants.LEVEL3_SAVE4_X+10,Constants.LEVEL3_SAVE4_Y-10);
    }

    public void startUnmarkHookTimerWithParameters(Point pt){
        ActionListener lambdaWithParameters = e->{
            if(this.MarkedHooks.contains(pt)){
                this.MarkedHooks.remove(pt);
            }
        };
        this.unmarkHookTimer.addActionListener(lambdaWithParameters);
        this.unmarkHookTimer.start();
    }

    @Override
    public void restoreState(){
        this.enemies[0].setX(Constants.WIZARD_X);
        this.enemies[0].setY(Constants.WIZARD_Y);

        this.enemies[1].setX(Constants.MINOTAUR1_X);
        this.enemies[1].setY(Constants.MINOTAUR1_Y);

        this.enemies[2].setX(Constants.MINOTAUR2_X);
        this.enemies[2].setY(Constants.MINOTAUR2_Y);

        this.enemies[3].setX(Constants.GHOST1_X);
        this.enemies[3].setY(Constants.GHOST1_Y);

        this.enemies[4].setX(Constants.GHOST2_X);
        this.enemies[4].setY(Constants.GHOST2_Y);

    }

    @Override
    public void update(){
        if(Objects.equals(State.getState().getStateName(), this.getStateName())){
            this.reflink.setCurrentRunningLevel(this.reflink.getGame().getLevel3State());
        }

        for(FloppyItem disk : this.floppyDisks){
            disk.updateItem();
        }

        onEnemyDefeat();
        handleSaveInteraction();
        handleEnemyInteraction();
        handleTransitions();
        handlePauseButton();
        handleGrappleLogic();
        this.reflink.getHero().update();
        if(reflink.getKeyManager().isKeyPressed(KeyEvent.VK_ESCAPE)){
            State.setState(reflink.getGame().getMenuState());
        }

        handleCameraLogic();

    }

    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        camera.apply(g2d);

        if(this.reflink.getHero().getY()>1300){
            g2d.setTransform(originalTransform);
            originalTransform = g2d.getTransform();
            camera.apply(g2d);
        }

        BufferedImage backgroundImage = this.reflink.getTileCache().getBackground(Constants.LEVEL3_BG_PATH);
        g.drawImage(backgroundImage,0,0,this.levelWidth,this.levelHeight,null);


        drawTiles(g);

        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].drawItem(g);
        }

        for(int i = 0;i<this.nrOfSaves;++i){
            if(this.reflink.getHero().getNrOfCollectedSaves() == i+2){
                this.floppyDisks[i].drawItem(g);
            }
        }
        drawBlackFade(g);
        drawWhip(g);

        for(Enemy enemy : enemies){
            if(enemy!=null && enemy.getHealth()>0){
                enemy.draw(g);
            }
        }

        this.reflink.getHero().draw(g);
        g2d.setTransform(originalTransform);
        this.reflink.getHero().DrawHealthBar(g);
        pauseButton.draw(g2d);


    }



    @Override
    public void loadState(boolean access){
        if(this.reflink.getLevel3RefreshDoneSignal()) {
            return;
        }
        try{
            this.enemies[0].setHealth(this.reflink.getDataProxy().load(Constants.BOSS_HEALTH,access));
            this.enemies[1].setHealth(this.reflink.getDataProxy().load(Constants.MINOTAUR0_HEALTH,access));
            this.enemies[2].setHealth(this.reflink.getDataProxy().load(Constants.MINOTAUR1_HEALTH,access));
            this.enemies[3].setHealth(this.reflink.getDataProxy().load(Constants.GHOST0_HEALTH,access));
            this.enemies[4].setHealth(this.reflink.getDataProxy().load(Constants.GHOST1_HEALTH,access));
            this.cameraIsSet = false;

            this.reflink.setLevel3RefreshDoneSignal(true);


        }catch (AccessNotPermittedException | ValueStoreException  | DataBufferNotReadyException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void storeState(boolean access) {
        if(this.reflink.getLevel3StoreDoneSignal()){
            return;
        }
        try{
            if(State.getState().getStateName().compareTo(this.stateName) == 0){
                this.reflink.getDataProxy().store(Constants.CURRENT_STATE,3,access);
            }
            this.reflink.getDataProxy().store(Constants.TIMESTAMP,(int) Instant.now().getEpochSecond(),access);
            this.reflink.getDataProxy().store(Constants.BOSS_HEALTH,(int)this.enemies[0].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.MINOTAUR0_HEALTH,(int)this.enemies[1].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.MINOTAUR1_HEALTH,(int)this.enemies[2].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.GHOST0_HEALTH,(int)this.enemies[3].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.GHOST1_HEALTH,(int)this.enemies[4].getHealth(),access);
            this.reflink.setLevel3StoreDoneSignal(true);
        }catch (AccessNotPermittedException | ValueStoreException e) {
            System.err.println(e.getMessage());
        }
    }



    private void onEnemyDefeat(){
        if(this.enemies[0].getHealth() == 0 && !bossDefeated){
            this.reflink.getHero().setScore(this.calculateScore());
            int currentScore = this.reflink.getHero().getScore();

            int[] storedScores = new int[3]; storedScores[0]=0; storedScores[1]=0; storedScores[2]=0;
            try {
                storedScores = this.reflink.getDataProxy().loadScore(true);

            } catch (AccessNotPermittedException e){
                System.err.println(e.getMessage());
            }
            storedScores = calculateTopThreeScores(currentScore,storedScores[0],storedScores[1],storedScores[2]);
            this.reflink.setScore1(storedScores[0]);
            this.reflink.setScore2(storedScores[1]);
            this.reflink.setScore3(storedScores[2]);

            try{
                this.reflink.getDataProxy().storeScore(true,storedScores[0],storedScores[1],storedScores[2]);
            }
            catch (AccessNotPermittedException | ValueStoreException e){
                System.err.println(e.getMessage());
            }
            this.bossDefeatedDelayTimer.start();
            this.bossDefeated = true;



            this.reflink.getHero().setNrOfCompletedLevels(3);
            this.reflink.setLevel3StoreDoneSignal(false);
            this.storeState(true);
            this.reflink.setHeroStoreDoneSignal(false);
            this.reflink.getHero().storeHeroState(true);

            try{
                this.reflink.getDataProxy().storeBuffer(true);
            } catch(AccessNotPermittedException | ValueStoreException e){
                System.err.println(e.getMessage());
            }



        }
        else if(bossDefeated && this.targetBlackIntensity==1){
            State.setState(this.reflink.getGame().getWinState());
            this.targetBlackIntensity = 0;
            this.bossDefeated = false;
        }
    }

    private int[] calculateTopThreeScores(int currentScore,int score1,int score2,int score3){
        //score1 e cel mai mare, score2 e mai mic si score3 e al3lea
        int newScore1,newScore2,newScore3;
        newScore1=score1;
        newScore2 =score2;
        newScore3=score3;
        if(currentScore>score3 && currentScore!= score2 && currentScore!=score1){
            newScore3=currentScore;
        }
        if(currentScore>score2 && currentScore!= score1 && currentScore!=score3){
            newScore3=score2;
            newScore2 = currentScore;
        }
        if(currentScore>score1 && currentScore!=score2 && currentScore!=score3){
            newScore3=score2;
            newScore2=score1;
            newScore1=currentScore;
        }
        int[] returnScoreArr = new int[3];
        returnScoreArr[0]=newScore1;
        returnScoreArr[1]=newScore2;
        returnScoreArr[2]=newScore3;

        return returnScoreArr;
    }

    private int calculateScore(){
        float healthProportion = 0.5f;
        float remainingSavesProportion = 0.2f;
        float goldProportion = 0.3f;
        int health = (int)this.reflink.getHero().getHealth();
        int nrOfEscapes = this.reflink.getHero().getNrOfEscapes();
        int gold = this.reflink.getHero().getGold();

        return (int)(health*healthProportion + nrOfEscapes*remainingSavesProportion + gold*goldProportion);
    }

    private void handleSaveInteraction(){
        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].updateItem();
            if(i+2 == this.reflink.getHero().getNrOfCollectedSaves()){
                if(this.reflink.getHero().getHitbox().intersects(this.floppyDisks[i].getHitbox())){
                    this.reflink.getHero().setNrOfCollectedSaves(this.reflink.getHero().getNrOfCollectedSaves()+1);
                    this.reflink.getHero().setNrOfEscapes(this.reflink.getHero().getMaxNrOfEscapes());
                    this.reflink.setHeroStoreDoneSignal(false);
                    this.reflink.setLevel3StoreDoneSignal(false);
                    this.reflink.getHero().storeHeroState(true);
                    this.storeState(true);
                    try{
                        this.reflink.getDataProxy().storeBuffer(true);
                    } catch (AccessNotPermittedException | ValueStoreException e){
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }

    private void handleEnemyInteraction(){
        for(Enemy enemy : enemies){
            if(enemy!=null){
                if(enemy.getHealth()==0){
                    enemy.nullifyHitbox();
                }
                else{
                    if(reflink.getHero().getHitbox().intersects(enemy.getHitbox()) && reflink.getHero().getEngageReady()){
                        enemy.setIsEngaged(true);
                        this.transitioning = true;
                        this.transition_to_fight = true;
                        reflink.getGame().getFightState().setEnemy(enemy);

                    }
                }
                enemy.update();
            }

        }
    }

    private void handleTransitions(){
        if(this.reflink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){
            this.targetBlackIntensity = 0;

            this.reflink.getGame().getDeathState().restoreState();
            State.setState(this.reflink.getGame().getDeathState());
        }

        if(this.transition_to_fight && this.targetBlackIntensity==1) {
            this.targetBlackIntensity = 0;
            this.transitioning = false;
            this.transition_to_fight = false;

            State.setState(reflink.getGame().getFightState());
        }
    }

    private void handlePauseButton(){
        MouseInput mouse = reflink.getMouseInput();
        Point mousePos = new Point(mouse.getMouseX(),mouse.getMouseY());
        pauseButton.updateHover(mousePos.x,mousePos.y);
        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(reflink.getGame().getPauseMenuState()); // Acum trimite către meniul de pauză
            mouse.mouseReleased(null);
            return;
        }
    }

    private void handleGrappleLogic(){

        float heroCenterX = this.reflink.getHero().getX() + this.reflink.getHero().getWidth() / 2;
        float heroCenterY = this.reflink.getHero().getY() + this.reflink.getHero().getHeight() / 2;

        int heroTileX = (int)(heroCenterX / Constants.TILE_SIZE);
        int heroTileY = (int)(heroCenterY / Constants.TILE_SIZE);
        boolean isHeroFlipped = this.reflink.getHero().getFlipped();
        if(this.reflink.getHero().getHitbox().intersects(this.whip.getHitbox())){
            this.reflink.getHero().setHasWhip(true);
        }

        if(this.reflink.getHero().getHasWhip()){
            int closestX = -1;
            int closestY = -1;
            double closestDistance = 100;
            final int GRAPPLE_TILE_ID = 3;


            for (int radius = 0; radius <= Constants.HERO_GRAPPLE_RANGE; radius++) {
                boolean foundInThisRadius = false;

                for (int x = (isHeroFlipped? (heroTileX - radius): heroTileX); x<= (isHeroFlipped? heroTileX:(heroTileX + radius)); x++) {
                    for (int y = heroTileY - radius; y <= heroTileY + radius; y++) {
                        if (Math.abs(x - heroTileX) == radius || Math.abs(y - heroTileY) == radius) { //verific daca sunt pe marginile exterioare
                            if (x >= 0 && x < Constants.LEVEL3_WIDTH &&
                                    y >= 0 && y < Constants.LEVEL3_HEIGHT) {//verific daca sunt in raza nivelului
                                if (Level.getTileBehavior(x, y, Constants.LEVEL3_WIDTH, Constants.LEVEL3_HEIGHT, this.getLevel3().getBehaviorIDs()) == GRAPPLE_TILE_ID) {
                                    double distance = Math.sqrt(Math.pow(x - heroTileX, 2) + Math.pow(y - heroTileY, 2)); //calculez distanta cu formula distantei
                                    if (distance < closestDistance) {
                                        closestDistance = distance;
                                        closestX = x;
                                        closestY = y;
                                        foundInThisRadius = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (foundInThisRadius) { //daca am gasit pe o raza, inseamna ca pot sa ies, pentru ca pe razele urmatoare nu se poate decat sa fie tile-uri mai departate
                    break;
                }
            }

            if (closestX != -1) {
                Point pt = new Point(closestX,closestY);
                // You can return or use these coordinates here
                if(!this.MarkedHooks.contains(pt)){
                    if(closestDistance> 3){
                        this.reflink.getHero().setGrapplePoint(closestX,closestY);
                        this.MarkedHooks.add(pt);
                        startUnmarkHookTimerWithParameters(pt);
                        this.reflink.getHero().setGrappleInterrupted(false);
                    }
                    else{
                        this.reflink.getHero().setGrappleInterrupted(true);
                    }

                }
            } else {
                this.reflink.getHero().setGrapplePoint(0,0); //punctele sunt relative la heroCenter point
            }
        }
        else{
            this.whip.updateItem();
        }
    }

    private void handleCameraLogic(){
        float heroCenterX = this.reflink.getHero().getX() + this.reflink.getHero().getWidth() / 2;
        float heroCenterY = this.reflink.getHero().getY() + this.reflink.getHero().getHeight() / 2;

        int heroTileX = (int)(heroCenterX / Constants.TILE_SIZE);
        int heroTileY = (int)(heroCenterY / Constants.TILE_SIZE);
        boolean isHeroFlipped = this.reflink.getHero().getFlipped();






        double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
        double cameraY = heroCenterY*1.6; //- (Constants.WINDOW_HEIGHT / 2) / camera.getScale();

        double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
        double maxCameraY = Constants.LEVEL3_HEIGHT*Constants.TILE_SIZE*2;//Constants.WINDOW_HEIGHT;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY))+350;


        if(State.getState().getStateName() == this.stateName && !cameraIsSet){
            camera.setPosition(cameraX, cameraY);
            this.cameraIsSet = true;
        }

        if(!adjustingCameraForDepth && this.reflink.getHero().getY()>1500){
            cameraY+=500;
            cameraX+=200;
            camera.setPosition(cameraX,cameraY);
            adjustingCameraForDepth = true;
        }


        if(adjustingCameraForDepth && this.reflink.getHero().getY()<1500 && this.reflink.getHero().getX()>4100 && !this.adjustingCameraForArena){
            cameraY+=200;
            cameraX+=200;
            camera.setPosition(cameraX,cameraY);
            this.adjustingCameraForArena = true;
        }


        if(this.reflink.getHero().getX()<4820)
            camera.updatePosition(this.reflink.getHero().getVelocityX(),this.reflink.getHero().getVelocityY()*2);

        int currentCameraXProblem = (int)(this.reflink.getHero().getX() - camera.getxOffset());
        if(currentCameraXProblem<94){
            camera.updatePosition(-1,0);
        }
        else if(currentCameraXProblem>94){
            camera.updatePosition(1,0);
        }
    }

    private void drawTiles(Graphics g){
        for (int i = 0; i < Constants.LEVEL3_TILE_NR; ++i) {
            int currentID = this.level3.getVisualIDs()[i];
            if (currentID != -1) {
                this.reflink.getTileCache()
                        .getTile(Constants.LEVEL3_TEXTURES_PATH, currentID)
                        .Draw(g, (i % Constants.LEVEL3_WIDTH) * Constants.TILE_SIZE,
                                (i / Constants.LEVEL3_WIDTH) * Constants.TILE_SIZE);
            }
        }
    }

    private void drawBlackFade(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(reflink.getHero().getHealth() == 0 || this.transitioning || this.transition_to_fight || bossDefeated){
            this.targetBlackIntensity+=this.blackFadeStep;
            Color originalColor = g2d.getColor();
            if(this.targetBlackIntensity>=1.0){
                this.targetBlackIntensity = 1.0;

            }
            int alpha = (int)(this.targetBlackIntensity*255.0);
            g2d.setColor(new Color(0,0,0,alpha));
            g.fillRect(0,0,this.levelWidth,this.levelHeight);
            g2d.setColor(originalColor);
        }
    }

    private void drawWhip(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(reflink.getHero().getHasWhip()){
            Color originalColor1 = g2d.getColor();
            g2d.setColor(new Color(255,0,0,(int)(255*0.2)));
            g2d.fillRect(this.reflink.getHero().getCurrentGrappleX()*16,this.reflink.getHero().getCurrentGrappleY()*16,16,16);
            g2d.setColor(originalColor1);
        }
        else{
            this.whip.drawItem(g);
        }
    }


    public Level3 getLevel3(){return this.level3;}

    @Override
    public String getStateName(){
        return this.stateName;
    }

    @Override
    public void setEnemy(Enemy enemy){

    }


}
