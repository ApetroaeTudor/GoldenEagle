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

/**
 * @class Level3State
 * @brief Represents the game state for Level 3.
 *
 * This class extends the abstract {@link State} class and defines the specific
 * behavior, entities, and rendering for the third level of the game. It manages
 * the Level 3 map, camera, enemies, items, UI elements like the pause button,
 * and game logic such as grappling, transitions, and save/load functionality.
 */
public class Level3State extends State{
    private Level3 level3;                                  ///< The Level3 map data object.
    private Camera camera;                                  ///< The game camera for this level.
    private PauseButton pauseButton;                        ///< The UI button to pause the game.
    private int levelWidth;                                 ///< The total width of the level in pixels.
    private int levelHeight;                                ///< The total height of the level in pixels.

    private LinkedList<Point> MarkedHooks;                  ///< List of active grapple hook locations.
    private Timer unmarkHookTimer;                          ///< Timer to clear a marked grapple hook.
    private Timer bossDefeatedDelayTimer;                   ///< Timer for delay after boss defeat.
    private int hookTimeoutMillis = 100;                    ///< Duration in ms before a hook is unmarked.
    private int bossDefeatedTimeoutMillis = 300;            ///< Duration in ms for boss defeat delay.

    private boolean bossDefeated = false;                   ///< True if the level boss has been defeated.

    private WhipItem whip;                                  ///< The whip item for grappling mechanics.

    private boolean cameraIsSet = false;                    ///< True if the camera's initial setup is done.
    private boolean adjustingCameraForDepth = false;        ///< True if camera is adjusting for vertical depth.
    private boolean adjustingCameraForArena = false;        ///< True if camera is adjusting for the boss arena.

    protected boolean transitioning = false;                ///< True if a screen transition is active.
    protected boolean transition_to_fight = false;          ///< True if transitioning to the fight state.

    private Enemy[] enemies;                                ///< Array of enemies in this level.
    private int nrOfEnemies = 5;                            ///< Count of enemies in this level.

    private BonfireItem[] saves;                            ///< Array of bonfire save points.
    private int nrOfSaves = 4;                              ///< Count of bonfire save points.
    private FloppyItem[] floppyDisks;                       ///< Array of floppy disk items for saving.

    protected String stateName = Constants.LEVEL3_STATE;    ///< The name identifier for this state.

    protected double targetBlackIntensity = 0.0;            ///< Target alpha for black fade effect.
    protected double blackFadeStep = 0.05;                  ///< Step size for black fade intensity change.

    /**
     * @brief Constructs a Level3State object.
     *
     * Initializes the Level 3 environment, including the map, camera, enemies, items,
     * UI elements, and timers. It sets up the initial positions and properties for
     * various game elements based on constants defined in {@link PaooGame.Config.Constants}.
     *
     * @param reflink A {@link RefLinks} object providing access to game-wide objects and utilities.
     * @param level3 The {@link Level3} map data to be used for this state.
     */
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

    /**
     * @brief Starts the timer to unmark a specific grapple hook point.
     *
     * This method configures and starts {@link #unmarkHookTimer} with an action
     * that removes the specified {@link Point} from the {@link #MarkedHooks} list
     * when the timer expires.
     *
     * @param pt The {@link Point} representing the grapple hook location to be unmarked.
     */
    public void startUnmarkHookTimerWithParameters(Point pt){
        ActionListener lambdaWithParameters = e->{
            if(this.MarkedHooks.contains(pt)){
                this.MarkedHooks.remove(pt);
            }
        };
        this.unmarkHookTimer.addActionListener(lambdaWithParameters);
        this.unmarkHookTimer.start();
    }

    /**
     * @brief Restores the initial state of Level 3.
     *
     * It resets the positions of all enemies in Level 3 to their predefined
     * starting locations specified in {@link PaooGame.Config.Constants}.
     * This method overrides the {@link State#restoreState()} method.
     */
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

    /**
     * @brief Updates the game logic for Level 3 State.
     *
     * This method is called repeatedly in the game loop. It handles:
     * - Setting the current running level if this state is active.
     * - Updating floppy disk items.
     * - Checking for boss defeat conditions.
     * - Handling interactions with save points and enemies.
     * - Managing screen transitions.
     * - Processing pause button input.
     * - Updating grapple logic.
     * - Updating the hero character.
     * - Checking for the ESC key press to return to the menu.
     * - Updating camera logic.
     * This method overrides the {@link State#update()} method.
     */
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

    /**
     * @brief Draws the Level 3 State to the screen.
     *
     * This method is responsible for rendering all visual elements of Level 3,
     * including:
     * - Applying camera transformations.
     * - Drawing the background image.
     * - Drawing the level tiles.
     * - Drawing save points and floppy disk items.
     * - Drawing fade-to-black transition effects.
     * - Drawing the whip or grapple indicators.
     * - Drawing enemies.
     * - Drawing the hero character.
     * - Drawing UI elements like the hero's health bar and the pause button.
     * This method overrides the {@link State#draw(Graphics)} method.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
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


    /**
     * @brief Loads the state of Level 3 from persistent storage.
     *
     * This method retrieves saved data for Level 3, such as enemy health,
     * using the DataProxy. It ensures that data is loaded
     * only once per session or until a refresh is signaled.
     * This method overrides the {@link State#loadState(boolean)} method.
     *
     * @param access Boolean indicating access permission for data operations.
     * @throws AccessNotPermittedException If data access is not permitted.
     * @throws ValueStoreException If there's an issue with storing or retrieving values.
     * @throws DataBufferNotReadyException If the data buffer is not ready for operations.
     */
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
            this.adjustingCameraForDepth = false;
            this.cameraIsSet = false;


            this.reflink.setLevel3RefreshDoneSignal(true);


        }catch (AccessNotPermittedException | ValueStoreException  | DataBufferNotReadyException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @brief Stores the current state of Level 3 to persistent storage.
     *
     * This method saves data relevant to Level 3, such as the current timestamp and
     * enemy health, using the DataProxy. It ensures that data
     * is stored only once or until a store operation is signaled.
     * This method overrides the {@link State#storeState(boolean)} method.
     *
     * @param access Boolean indicating access permission for data operations.
     * @throws AccessNotPermittedException If data access is not permitted.
     * @throws ValueStoreException If there's an issue with storing values.
     */
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


    /**
     * @brief Handles logic upon the defeat of the main boss enemy.
     *
     * When the boss (enemies[0]) health reaches zero and `bossDefeated` is false:
     * - Calculates and sets the player's score.
     * - Updates and stores the top three high scores.
     * - Starts a delay timer ({@link #bossDefeatedDelayTimer}) before setting `bossDefeated` to true.
     * - Sets the hero's completed levels to 3.
     * - Stores the hero and level state.
     * - Stores the data buffer.
     * If `bossDefeated` is true and the screen fade targetBlackIntensity is complete,
     * it transitions to the {@link WinState}.
     */
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

    /**
     * @brief Calculates and returns the top three scores.
     *
     * Given the current player's score and the existing top three scores, this method
     * determines the new ranking and returns an array containing the updated top three scores.
     *
     * @param currentScore The player's current score.
     * @param score1 The highest score.
     * @param score2 The second highest score.
     * @param score3 The third highest score.
     * @return An integer array of size 3, where element 0 is the new highest score,
     *         element 1 is the new second highest, and element 2 is the new third highest.
     */
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

    /**
     * @brief Calculates the player's score.
     *
     * The score is calculated based on a weighted combination of the hero's remaining health,
     * number of escapes, and collected gold.
     *
     * @return The calculated integer score.
     */
    private int calculateScore(){
        float healthProportion = 0.5f;
        float remainingSavesProportion = 0.2f;
        float goldProportion = 0.3f;
        int health = (int)this.reflink.getHero().getHealth();
        int nrOfEscapes = this.reflink.getHero().getNrOfEscapes();
        int gold = this.reflink.getHero().getGold();

        return (int)(health*healthProportion + nrOfEscapes*remainingSavesProportion + gold*goldProportion);
    }

    /**
     * @brief Handles hero interaction with save points (bonfires) and floppy disk items.
     *
     * Updates each save point item. If the hero has collected a certain number of saves
     * and intersects with a corresponding floppy disk, the hero's collected saves count
     * is incremented, escapes are refilled, and the game state (hero and level) is saved.
     */
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

    /**
     * @brief Handles interactions between the hero and enemies.
     *
     * For each enemy:
     * - If an enemy's health is zero, its hitbox is nullified.
     * - If the hero's hitbox intersects with an active enemy's hitbox and the hero is ready to engage,
     *   the enemy is marked as engaged, and a transition to the {@link FightState} is initiated.
     * - Updates each enemy.
     */
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

    /**
     * @brief Manages screen transitions to other game states.
     *
     * - If the hero's health is zero and the fade-to-black transition is complete,
     *   resets the fade, restores the {@link DeathState}, and transitions to it.
     * - If a transition to the fight state is active and the fade-to-black is complete,
     *   resets fade and transition flags, and transitions to the {@link FightState}.
     */
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

    /**
     * @brief Handles input and actions for the pause button.
     *
     * Updates the hover state of the pause button based on mouse position.
     * If the pause button is clicked, it transitions the game to the {@link PauseMenuState}.
     */
    private void handlePauseButton(){
        MouseInput mouse = reflink.getMouseInput();
        Point mousePos = new Point(mouse.getMouseX(),mouse.getMouseY());
        pauseButton.updateHover(mousePos.x,mousePos.y);
        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(reflink.getGame().getPauseMenuState());
            mouse.mouseReleased(null);
            return;
        }
    }

    /**
     * @brief Manages the hero's grappling hook logic.
     *
     * - Checks if the hero collects the whip item.
     * - If the hero has the whip:
     *   - Searches for the nearest grapple-able tile within {@link Constants#HERO_GRAPPLE_RANGE}.
     *   - The search prioritizes tiles in the direction the hero is facing.
     *   - If a valid grapple point is found and not recently used (not in {@link #MarkedHooks}),
     *     it sets the hero's grapple point and marks the hook, starting {@link #unmarkHookTimer}.
     *   - If the grapple point is too close, it sets grapple as interrupted.
     *   - If no grapple point is found, clears the hero's grapple point.
     * - If the hero does not have the whip, updates the whip item itself.
     */
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
                        if (Math.abs(x - heroTileX) == radius || Math.abs(y - heroTileY) == radius) { // check to see if currently checking the exterior border
                            if (x >= 0 && x < Constants.LEVEL3_WIDTH &&
                                    y >= 0 && y < Constants.LEVEL3_HEIGHT) { // check if currently in level bounds
                                if (Level.getTileBehavior(x, y, Constants.LEVEL3_WIDTH, Constants.LEVEL3_HEIGHT, this.getLevel3().getBehaviorIDs()) == GRAPPLE_TILE_ID) {
                                    double distance = Math.sqrt(Math.pow(x - heroTileX, 2) + Math.pow(y - heroTileY, 2)); // calculating distance
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
                if (foundInThisRadius) { // if found in a certain radius, then exit, because other hooks will only be further away
                    break;
                }
            }

            if (closestX != -1) {
                Point pt = new Point(closestX,closestY);
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
                this.reflink.getHero().setGrapplePoint(0,0); // the points are relative to the hero center
            }
        }
        else{
            this.whip.updateItem();
        }
    }

    /**
     * @brief Manages the camera position and movement.
     *
     * - Calculates the target camera position based on the hero's center.
     * - Clamps the camera position within the level boundaries.
     * - Sets the initial camera position if not already set for this state.
     * - Implements specific camera adjustments based on hero's Y-position (depth)
     *   and X-position (approaching arena).
     * - Updates camera position smoothly based on hero's velocity.
     * - Fine-tunes camera X-position to keep the hero within a certain screen region.
     */
    private void handleCameraLogic(){
        float heroCenterX = this.reflink.getHero().getX() + this.reflink.getHero().getWidth() / 2;
        float heroCenterY = this.reflink.getHero().getY() + this.reflink.getHero().getHeight() / 2;

        double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
        double cameraY = heroCenterY*1.6;

        double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
        double maxCameraY = Constants.LEVEL3_HEIGHT*Constants.TILE_SIZE*2;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY))+350;


        if(Objects.equals(State.getState().getStateName(), this.stateName) && !cameraIsSet){
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

    /**
     * @brief Draws the tiles of Level 3.
     *
     * Iterates through the visual tile IDs of {@link #level3} and draws each
     * valid tile at its corresponding position using the {@link PaooGame.Tiles.TileCache}.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
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

    /**
     * @brief Draws a black fade effect on the screen.
     *
     * This effect is used during transitions (hero death, entering a fight, boss defeated).
     * The {@link #targetBlackIntensity} is gradually increased, and a semi-transparent
     * black rectangle is drawn over the entire level area with an alpha value
     * corresponding to this intensity.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
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

    /**
     * @brief Draws the whip item or grapple point indicators.
     *
     * If the hero has the whip, it draws a semi-transparent red square at the
     * current grapple target tile. Otherwise, it draws the whip item itself if it's
     * still present in the level.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
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

    /**
     * @brief Gets the {@link Level3} map data associated with this state.
     * @return The {@link Level3} object.
     */
    public Level3 getLevel3(){return this.level3;}

    /**
     * @brief Gets the name of this state.
     * This method overrides the {@link State#getStateName()} method.
     * @return The string name of the state, typically defined in {@link PaooGame.Config.Constants}.
     */
    @Override
    public String getStateName(){
        return this.stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is intended to be overridden by states that need to set a specific
     * enemy, for example, the {@link FightState}. In Level3State, it currently has no implementation.
     * This method overrides the {@link State#setEnemy(Enemy)} method.
     * @param enemy The {@link Enemy} to be set.
     */
    @Override
    public void setEnemy(Enemy enemy){

    }


}