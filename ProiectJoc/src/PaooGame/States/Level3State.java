package PaooGame.States;
import PaooGame.Animations.Animation;
import PaooGame.Animations.ItemsAnimations.FloatingItemAnimation;
import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Entity;
import PaooGame.HUD.PauseButton;
import PaooGame.Hitbox.Hitbox;
import PaooGame.Input.MouseInput;
import PaooGame.Items.SaveItem;
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
import java.util.LinkedList;


public class Level3State extends State{
    private Level3 level3;
    private Camera camera;
    private PauseButton pauseButton;
    private int levelWidth;
    private int levelHeight;

    private LinkedList<Point> MarkedHooks;
    private Timer unmarkHookTimer;
    private int hookTimeoutMillis = 100;


    private WhipItem whip;



    private boolean cameraIsSet = false;
    private boolean adjustingCameraForDepth = false;
    private boolean adjustingCameraForArena = false;

    protected boolean transitioning = false;
    protected boolean transition_to_fight = false;

    private Entity[] enemies; //TODO
    private SaveItem[] saves;
    private int nrOfSaves = 4;

    protected String stateName = Constants.LEVEL3_STATE;

    protected double targetBlackIntensity = 0.0;
    protected double blackFadeStep = 0.05;

    public Level3State(RefLinks reflink, Level3 level3){
        super(reflink);
        this.whip = new WhipItem(this.refLink,Constants.WHIP_POSITION_X,Constants.WHIP_POSITION_Y);
        this.saves = new SaveItem[this.nrOfSaves];

        this.level3 = level3;
        this.MarkedHooks = new LinkedList<Point>();
        levelWidth = Constants.LEVEL3_WIDTH*Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL3_HEIGHT*Constants.TILE_SIZE;
        camera = new Camera(0,0);
        this.unmarkHookTimer = new Timer(hookTimeoutMillis,null);
        this.unmarkHookTimer.setRepeats(false);

        pauseButton = new PauseButton(reflink.getHero(),80,50);

        this.saves[0] = new SaveItem(this.refLink,Constants.LEVEL3_SAVE1_X,Constants.LEVEL3_SAVE1_Y);
        this.saves[1] = new SaveItem(this.refLink,Constants.LEVEL3_SAVE2_X,Constants.LEVEL3_SAVE2_Y);
        this.saves[2] = new SaveItem(this.refLink,Constants.LEVEL3_SAVE3_X,Constants.LEVEL3_SAVE3_Y);
        this.saves[3] = new SaveItem(this.refLink,Constants.LEVEL3_SAVE4_X,Constants.LEVEL3_SAVE4_Y);


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

    }

    @Override
    public void update(){
        this.refLink.getHero().update();
        if(refLink.getKeyManager().isKeyPressed(KeyEvent.VK_ESCAPE)){
            State.setState(refLink.getGame().getMenuState());
        }

        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].updateItem();
        }
        if(this.refLink.getHero().getHitbox().intersects(this.saves[0].getHitbox())){
            System.out.println("Interaction");
        }

        MouseInput mouse = refLink.getMouseInput();
        Point mousePos = new Point(mouse.getMouseX(),mouse.getMouseY());
        pauseButton.updateHover(mousePos.x,mousePos.y);

        float heroCenterX = this.refLink.getHero().getX() + this.refLink.getHero().getWidth() / 2;
        float heroCenterY = this.refLink.getHero().getY() + this.refLink.getHero().getHeight() / 2;

        int heroTileX = (int)(heroCenterX / Constants.TILE_SIZE);
        int heroTileY = (int)(heroCenterY / Constants.TILE_SIZE);
        boolean isHeroFlipped = this.refLink.getHero().getFlipped();

        if(this.refLink.getHero().getHitbox().intersects(this.whip.getHitbox())){
            this.refLink.getHero().setHasWhip(true);
        }

        if(this.refLink.getHero().getHasWhip()){
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
//            System.out.printf("Closest grapple tile at (%d,%d), distance: %.1f tiles%n",
//                    closestX, closestY, closestDistance);
                Point pt = new Point(closestX,closestY);
                // You can return or use these coordinates here
                if(!this.MarkedHooks.contains(pt)){
                    if(closestDistance> 3){
                        this.refLink.getHero().setGrapplePoint(closestX,closestY);
                        this.MarkedHooks.add(pt);
                        startUnmarkHookTimerWithParameters(pt);
                        this.refLink.getHero().setGrappleInterrupt(false);
                    }
                    else{
                        this.refLink.getHero().setGrappleInterrupt(true);
                    }

                }
            } else {
                this.refLink.getHero().setGrapplePoint(0,0); //punctele sunt relative la heroCenter point
            }
        }
        else{
            this.whip.updateItem();
        }










        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(refLink.getGame().getPauseMenuState()); // Acum trimite către meniul de pauză
            mouse.mouseReleased(null);
            return;
        }

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

        if(!adjustingCameraForDepth && this.refLink.getHero().getY()>1500){
            cameraY+=500;
            cameraX+=200;
            camera.setPosition(cameraX,cameraY);
            adjustingCameraForDepth = true;
        }


        if(adjustingCameraForDepth && this.refLink.getHero().getY()<1500 && this.refLink.getHero().getX()>4100 && !this.adjustingCameraForArena){
            cameraY+=200;
            cameraX+=200;
            camera.setPosition(cameraX,cameraY);
            this.adjustingCameraForArena = true;
        }


        if(this.refLink.getHero().getX()<4820)
            camera.updatePosition(this.refLink.getHero().getVelocityX(),this.refLink.getHero().getVelocityY()*2);

        int currentCameraXProblem = (int)(this.refLink.getHero().getX() - camera.getxOffset());
        if(currentCameraXProblem<94){
            camera.updatePosition(-1,0);
        }
        else if(currentCameraXProblem>94){
            camera.updatePosition(1,0);
        }




        if(this.refLink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){
            this.targetBlackIntensity = 0;

            this.refLink.getGame().getDeathState().restoreState();
            State.setState(this.refLink.getGame().getDeathState());
        }



    }

    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();


        camera.apply(g2d);


        if(this.refLink.getHero().getY()>1300){
            g2d.setTransform(originalTransform);
            originalTransform = g2d.getTransform();
            camera.apply(g2d);
        }

        BufferedImage backgroundImage = this.refLink.getTileCache().getBackground(Constants.LEVEL3_BG_PATH);
        g.drawImage(backgroundImage,0,0,this.levelWidth,this.levelHeight,null);
        for (int i = 0; i < Constants.LEVEL3_TILE_NR; ++i) {
            int currentID = this.level3.getVisualIDs()[i];
            if (currentID != -1) {
                this.refLink.getTileCache()
                        .getTile(Constants.LEVEL3_TEXTURES_PATH, currentID)
                        .Draw(g, (i % Constants.LEVEL3_WIDTH) * Constants.TILE_SIZE,
                                (i / Constants.LEVEL3_WIDTH) * Constants.TILE_SIZE);
            }
        }

        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].drawItem(g);
        }

        if(refLink.getHero().getHealth() == 0 || this.transitioning){
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

        if(refLink.getHero().getHasWhip()){
            Color originalColor1 = g2d.getColor();
            g2d.setColor(new Color(255,0,0,(int)(255*0.2)));
            g2d.fillRect(this.refLink.getHero().getCurrentGrappleX()*16,this.refLink.getHero().getCurrentGrappleY()*16,16,16);
            g2d.setColor(originalColor1);
        }
        else{
            this.whip.drawItem(g);
        }

        if(!refLink.getHero().getHasWhip()){

        }


        this.refLink.getHero().Draw(g);
        g2d.setTransform(originalTransform);
        this.refLink.getHero().DrawHealthBar(g);
        pauseButton.draw(g2d);


    }

    public Level3 getLevel3(){return this.level3;}

    @Override
    public String getStateName(){
        return this.stateName;
    }

    @Override
    public void setEnemy(Entity enemy){

    }


}
