package PaooGame.States;

import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;
import PaooGame.Entities.Enemy;
import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.Entities.Hero;
import PaooGame.HUD.ContextHUD;
import PaooGame.HUD.Gold;
import PaooGame.HUD.MessageTriggerZone;
import PaooGame.Input.MouseInput;
import PaooGame.Items.FloppyItem;
import PaooGame.Items.BonfireItem;
import PaooGame.Maps.Level1;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Objects;

import PaooGame.HUD.PauseButton;


/**
 * @class Level1State
 * @brief Represents the game state for Level 1.
 *
 * This class manages all aspects of Level 1, including the level map,
 * hero, enemies, items (save points, floppy disks), camera, UI elements (pause button, context HUD),
 * and transitions to other states (FightState, Level2State, DeathState, PauseMenuState).
 * It handles game logic such as player movement, collision detection with items and enemies,
 * pausing the game, and saving/loading level-specific progress.
 */
public class Level1State extends State {
    protected String stateName = Constants.LEVEL1_STATE; ///< The name identifier for this state.
    private Level1 level1;                              ///< The Level1 map data object.
    private Camera camera;                              ///< The game camera for this level.
    private PauseButton pauseButton;                    ///< The UI button to pause the game.
    private Gold gold;
    private int levelWidth;                             ///< The total width of the level in pixels.
    private int levelHeight;                            ///< The total height of the level in pixels.
    private int tiger1X =Constants.TIGER1_X;            ///< Initial x-coordinate for the first tiger enemy.
    private int tiger1Y =Constants.TIGER1_Y;            ///< Initial y-coordinate for the first tiger enemy.
    private int tiger2X =Constants.TIGER2_X;            ///< Initial x-coordinate for the second tiger enemy.
    private int tiger2Y =Constants.TIGER2_Y;            ///< Initial y-coordinate for the second tiger enemy.

    private BonfireItem[] saves;                        ///< Array of save points (bonfires) in the level.
    private int nrOfSaves = 1;                          ///< The number of save points in this level.
    private FloppyItem[] floppyDisks;                   ///< Array of floppy disk items, associated with save points.

    private ContextHUD contextHUD;                      ///< Heads-Up Display for contextual messages.

    protected boolean isTransitioning = false;          ///< General flag indicating if a transition (fade) is in progress.
    protected boolean isTransitioning_to_fight = false; ///< Flag indicating a transition to FightState.
    private boolean isSwitchingToLevel2 = false;        ///< Flag indicating a transition to Level2State.

    private Enemy [] enemies;                           ///< Array of enemies present in this level.

    protected double targetBlackIntensity = 0.0;        ///< Current intensity of the black overlay for fade transitions (0.0 to 1.0).
    protected double blackFadeStep = 0.05;              ///< Increment step for {@link #targetBlackIntensity} during fades.


    /**
     * @brief Constructs a Level1State object.
     *
     * Initializes the level map, camera, hero, enemies, items, HUD elements,
     * and calculates level dimensions. Sets up message trigger zones for context HUD.
     * @param refLink A {@link RefLinks} object providing access to game-wide resources and utilities.
     * @param level1 The {@link Level1} map data for this state.
     */
    public Level1State(RefLinks refLink, Level1 level1) {
        super(refLink);
        this.level1 = level1;
        camera = new Camera(0, 0);
        this.contextHUD = new ContextHUD(refLink.getHero());
        this.gold= new Gold(refLink.getHero());
        // Initialize save points (bonfires)
        this.saves = new BonfireItem[this.nrOfSaves];
        this.saves[0] = new BonfireItem(this.reflink,Constants.LEVEL1_SAVE1_X,Constants.LEVEL1_SAVE1_Y);

        // Initialize enemies
        enemies = new Enemy[2];
        enemies[0] = new Enemy(this.reflink,this.tiger1X,this.tiger1Y,Constants.TIGER_NAME); //tiger0

        enemies[1] = new Enemy(this.reflink,this.tiger2X,this.tiger2Y,Constants.TIGER_NAME); //tiger1

        // Initialize floppy disks associated with save points
        floppyDisks = new FloppyItem[this.nrOfSaves];
        this.floppyDisks[0] = new FloppyItem(this.reflink,Constants.LEVEL1_SAVE1_X+10,Constants.LEVEL1_SAVE1_Y-10);

        pauseButton = new PauseButton(refLink.getHero(), 80, 50);
        // Calculate total level dimensions in pixels
        levelWidth = Constants.LEVEL1_WIDTH * Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL1_HEIGHT * Constants.TILE_SIZE;

        // Add message trigger zones for enemies
        for (Enemy enemy : enemies) {
            contextHUD.addTrigger(new MessageTriggerZone(
                    enemy, -30, -100, 120, 150, // offsetX, offsetY, width, height relative to enemy
                    "Învinge tigrul!" // Message to display
            ));
        }
        // Add message trigger zone for level exit
        contextHUD.addTrigger(new MessageTriggerZone(
                850, 600, 100, 50, // x, y, width, height (absolute coordinates)
                "Sari în peșteră!" // Message to display
        ));
    }

    /**
     * @brief Restores the state of Level 1, typically enemy positions.
     *
     * This method is called when returning to this state, for example, after a fight
     * or loading a game. It resets the positions of enemies to their initial locations.
     * Hero position is usually restored by a separate mechanism or loaded from save data.
     */
    @Override
    public void restoreState() {
        // Reset enemy positions if they are not defeated
        this.enemies[0].setX(Constants.TIGER1_X);
        this.enemies[0].setY(Constants.TIGER1_Y);

        this.enemies[1].setX(Constants.TIGER2_X);
        this.enemies[1].setY(Constants.TIGER2_Y);

        // Reset transition flags and fade intensity
        this.isTransitioning = false;
        this.isTransitioning_to_fight = false;
        this.isSwitchingToLevel2 = false;
        this.targetBlackIntensity = 0.0;
    }


    /**
     * @brief Updates the logic of the Level1State.
     *
     * This method is called every game tick. It updates the hero, items, enemies,
     * checks for interactions (save points, enemy encounters, level exit),
     * handles pausing, camera movement, and manages transitions to other states.
     */
    @Override
    public void update() {
        this.reflink.getHero().update(); // Update hero logic
        if(Objects.equals(State.getState().getStateName(), this.getStateName())){
            this.reflink.setCurrentRunningLevel(this.reflink.getGame().getLevel1State()); // Set current running level reference
        }
        this.floppyDisks[0].updateItem(); // Update floppy disk item


        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].updateItem(); // Update save point items
        }

        // Check if hero reaches the area to switch to Level 2
        if(this.reflink.getHero().getX() > 735 && this.reflink.getHero().getX() < 930 && this.reflink.getHero().getY() > 650){
            this.isSwitchingToLevel2 = true;
        }

        heroTouchesSave(); // Handle hero interaction with save points
        heroTouchesEnemy(); // Handle hero interaction with enemies
        doPause(); // Handle pause input
        doCamera(); // Update camera position
        switchToLevel2(); // Handle transition to Level 2
        switchToFight(); // Handle transition to FightState
        switchToDeath(); // Handle transition to DeathState

        contextHUD.update(); // Update contextual HUD messages
    }

    /**
     * @brief Draws the content of the Level1State on the screen.
     *
     * This method is called every frame to render all visual elements of Level 1,
     * including the background, tiles, hero, enemies, items, and UI elements.
     * It applies camera transformations for scrolling.
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform(); // Save original transform
        // Apply camera transformation
        camera.apply(g2d);

        // Draw background image
        BufferedImage backgroundImage = this.reflink.getTileCache().getBackground(Constants.LEVEL1_BG_PATH);
        g.drawImage(backgroundImage, 0, 0, levelWidth, levelHeight, null);

        drawTiles(g); // Draw level tiles

        // Draw save points
        for(int i =0;i<this.nrOfSaves;++i){
            this.saves[i].drawItem(g);
        }

        // Draw enemies if they are alive
        for(Enemy enemy : enemies){
            if(enemy.getHealth()>0){
                enemy.draw(g);
            }
        }

        drawBlackFade(g); // Apply fade effect if transitioning

        // Draw floppy disk if not collected
        if(this.reflink.getHero().getNrOfCollectedSaves()==0){
            this.floppyDisks[0].drawItem(g);
        }

        this.reflink.getHero().draw(g); // Draw hero

        // Restore original transformation for UI elements
        g2d.setTransform(originalTransform);
        this.reflink.getHero().DrawHealthBar(g); // Draw hero health bar (HUD)
        pauseButton.draw(g2d); // Draw pause button (HUD)
        gold.draw(g2d);
        contextHUD.draw(g2d); // Draw contextual messages (HUD)
    }


    /**
     * @brief Loads level-specific state data, primarily enemy health.
     *
     * This method attempts to load the health values for enemies in Level 1
     * from the data proxy. It uses a flag ({@link RefLinks#getLevel1RefreshDoneSignal()})
     * to ensure data is loaded only once per appropriate context.
     * @param access A boolean flag, typically true, indicating permission to access stored data.
     */
    @Override
    public void loadState(boolean access){
        if(this.reflink.getLevel1RefreshDoneSignal()) { // Prevent redundant loading
            return;
        }
        try{
            // Load health for each enemy
            this.enemies[0].setHealth(this.reflink.getDataProxy().load(Constants.TIGER0_HEALTH,access));
            this.enemies[1].setHealth(this.reflink.getDataProxy().load(Constants.TIGER1_HEALTH,access));
            this.reflink.setLevel1RefreshDoneSignal(true); // Mark loading as done for this cycle

        }catch (AccessNotPermittedException | ValueStoreException | DataBufferNotReadyException e) {
            System.err.println("Error loading Level1State data: " + e.getMessage());
        }
    }


    /**
     * @brief Stores level-specific state data, primarily enemy health and current state identifier.
     *
     * This method saves the current health of enemies in Level 1 and identifies
     * this level as the current state to the data proxy. It uses a flag
     * ({@link RefLinks#getLevel1StoreDoneSignal()}) to prevent redundant saving.
     * @param access A boolean flag, typically true, indicating permission to store data.
     */
    @Override
    public void storeState(boolean access) {
        if(this.reflink.getLevel1StoreDoneSignal()){ // Prevent redundant saving
            return;
        }
        try{
            // If Level1State is the active state, mark it in the save data.
            if(State.getState().getStateName().compareTo(this.stateName) == 0){
                this.reflink.getDataProxy().store(Constants.CURRENT_STATE,1,access); // 1 represents Level1
            }
            this.reflink.getDataProxy().store(Constants.TIMESTAMP,(int) Instant.now().getEpochSecond(),access); // Save timestamp
            // Save health for each enemy
            this.reflink.getDataProxy().store(Constants.TIGER0_HEALTH,(int)this.enemies[0].getHealth(),access);
            this.reflink.getDataProxy().store(Constants.TIGER1_HEALTH,(int)this.enemies[1].getHealth(),access);
            this.reflink.setLevel1StoreDoneSignal(true); // Mark saving as done for this cycle
        }catch (AccessNotPermittedException | ValueStoreException e) {
            System.err.println("Error storing Level1State data: " + e.getMessage());
        }
    }

    /**
     * @brief Handles the logic when the hero interacts with a save point.
     *
     * If the hero touches a save point and also collects the associated floppy disk
     * (if not already collected), the game state is saved. This includes hero state,
     * level state (enemy health), and then triggers a buffer store.
     */
    private void heroTouchesSave(){
        if(this.reflink.getHero().getHitbox().intersects(this.saves[0].getHitbox())){
            if(this.reflink.getHero().getNrOfCollectedSaves() == 0){ // If save hasn't been "collected" yet
                if(this.reflink.getHero().getHitbox().intersects(this.floppyDisks[0].getHitbox())){ // And hero touches floppy
                    this.reflink.getHero().setNrOfCollectedSaves(this.reflink.getHero().getNrOfCollectedSaves()+1);
                    this.reflink.getHero().setNrOfEscapes(this.reflink.getHero().getMaxNrOfEscapes()); // Replenish escapes
                    // Reset flags to allow storing data
                    this.reflink.setHeroStoreDoneSignal(false);
                    this.reflink.setLevel1StoreDoneSignal(false);
                    this.reflink.getHero().storeHeroState(true); // Store hero's general state
                    this.storeState(true); // Store Level1 specific state (like enemy health)
                    try{
                        this.reflink.getDataProxy().storeBuffer(true); // Commit stored data
                    } catch (AccessNotPermittedException | ValueStoreException e){
                        System.err.println("Error storing buffer after save: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * @brief Handles logic when the hero interacts with an enemy.
     *
     * If an enemy is alive and the hero collides with it (and is ready to engage),
     * a transition to the {@link FightState} is initiated. Defeated enemies have their hitboxes nullified.
     * All enemies are updated.
     */
    private void heroTouchesEnemy(){
        for(Enemy enemy : enemies){
            if(enemy.getHealth()==0){
                enemy.nullifyHitbox(); // Make defeated enemies non-interactive
            }
            else{
                // Check for collision and if hero is ready to engage
                if(reflink.getHero().getHitbox().intersects(enemy.getHitbox()) && reflink.getHero().getEngageReady()){
                    enemy.setIsEngaged(true); // Mark enemy as engaged
                    this.isTransitioning = true; // Start general fade transition
                    this.isTransitioning_to_fight = true; // Specifically for fight state
                    reflink.getGame().getFightState().setEnemy(enemy); // Set the enemy for FightState
                }
            }
            enemy.update();
        }
    }

    /**
     * @brief Handles pausing the game.
     *
     * Checks for Escape key press or click on the pause button to transition
     * to the {@link PauseMenuState}.
     */
    private void doPause(){
        // Pause via Escape key
        if (reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            State.setState(reflink.getGame().getPauseMenuState());
        }

        MouseInput mouse = reflink.getMouseInput();
        Point mousePos = new Point(mouse.getMouseX(), mouse.getMouseY()); // Get mouse position (HUD space)
        pauseButton.updateHover(mousePos.x, mousePos.y); // Update pause button hover state

        // Pause via button click
        if (mouse.getNumberOfMousePresses() > 0 && pauseButton.isClicked(mousePos.x, mousePos.y)) {
            State.setState(reflink.getGame().getPauseMenuState());
            mouse.mouseReleased(null); // Consume the click
            return;
        }
    }

    /**
     * @brief Updates the camera position to follow the hero.
     *
     * The camera centers on the hero, with constraints to keep it within
     * the level boundaries.
     */
    private void doCamera(){
        Hero hero = reflink.getHero();

        // Calculate hero's center
        float heroCenterX = hero.getX() + hero.getWidth() / 2;
        float heroCenterY = hero.getY() + hero.getHeight() / 2;

        // Desired camera position to center hero (considering camera scale for zoom)
        double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
        double cameraY = heroCenterY - (Constants.WINDOW_HEIGHT / 2) / camera.getScale();

        // Maximum camera coordinates to stay within level bounds
        double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
        double maxCameraY = Constants.WINDOW_HEIGHT;

        // Clamp camera position to level boundaries
        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY))+350;

        // Update camera position unless hero is in dying animation
        if(!reflink.getHero().getIsDying()){
            camera.setPosition(cameraX, cameraY);
        }
    }

    /**
     * @brief Handles the transition to Level 2.
     *
     * If {@link #isSwitchingToLevel2} is true and the fade-to-black effect is complete,
     * it resets flags, potentially saves progress, updates hero's properties for Level 2,
     * and changes the game state to {@link Level2State}.
     */
    private void switchToLevel2(){
        if(this.isSwitchingToLevel2 && this.targetBlackIntensity ==1){ // If transitioning and fade is complete
            this.targetBlackIntensity = 0; // Reset fade for next screen
            this.isSwitchingToLevel2 = false;

            // If Level 1 is completed for the first time
            if(this.reflink.getHero().getNrOfCompletedLevels() == 0){
                this.reflink.getHero().setNrOfCompletedLevels(1);
                 this.reflink.getHero().getHitbox().setY(650);
                // Save progress before switching
                this.reflink.setLevel1StoreDoneSignal(false);
                this.storeState(true);
                this.reflink.setHeroStoreDoneSignal(false);
                this.reflink.getHero().storeHeroState(true);
                try{
                    this.reflink.getDataProxy().storeBuffer(true);
                } catch (AccessNotPermittedException | ValueStoreException e){
                    System.err.println("Error storing buffer on level switch: " + e.getMessage());
                }
            }

            // Set hero's starting position and properties for Level 2
            this.reflink.getHero().setX(Constants.HERO_LEVEL2_STARTING_X);
            this.reflink.getHero().setY(Constants.HERO_LEVEL2_STARTING_Y);
            this.reflink.getHero().getHitbox().setX(Constants.HERO_LEVEL2_STARTING_X);
            this.reflink.getHero().getHitbox().setY((Constants.HERO_LEVEL2_STARTING_Y));
            this.reflink.getHero().setJumpStrength(Constants.HERO_LEVEL2_JUMP_STRENGTH);
            this.reflink.getHero().setNrOfCollectedSaves(1);

            State.setState(reflink.getGame().getLevel2State()); // Change state to Level 2
        }
    }

    /**
     * @brief Handles the transition to the FightState.
     *
     * If {@link #isTransitioning_to_fight} is true and the fade-to-black effect is complete
     * (and not simultaneously switching to Level 2), it resets flags and changes
     * the game state to {@link FightState}.
     */
    private void switchToFight(){
        if(this.isTransitioning_to_fight && this.targetBlackIntensity==1 && !this.isSwitchingToLevel2) {
            this.targetBlackIntensity = 0; // Reset fade
            this.isTransitioning = false;
            this.isTransitioning_to_fight = false;

            State.setState(reflink.getGame().getFightState()); // Change state to Fight
        }
    }

    /**
     * @brief Handles the transition to the DeathState.
     *
     * If the hero's health is zero and the fade-to-black effect is complete,
     * it resets the fade, restores the {@link DeathState} (to ensure its effects run correctly),
     * and changes the game state to {@link DeathState}.
     */
    private void switchToDeath(){
        if(this.reflink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1){ // Hero defeated and fade complete
            this.targetBlackIntensity = 0; // Reset fade

            this.reflink.getGame().getDeathState().restoreState(); // Prepare DeathState
            State.setState(this.reflink.getGame().getDeathState()); // Change state to Death
        }
    }

    /**
     * @brief Draws the visual tiles of the level.
     *
     * Iterates through the visualIDs array of the {@link #level1} map data
     * and draws each tile at its calculated position using the {@link PaooGame.Tiles.TileCache}.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawTiles(Graphics g){
        for (int i = 0; i < Constants.LEVEL1_TILE_NR; ++i) { // Iterate through all tiles in 1D representation
            int currentID = this.level1.getVisualIDs()[i]; // Get visual ID of current tile
            if (currentID != -1) {
                this.reflink.getTileCache()
                        .getTile(Constants.LEVEL1_TEXTURES_PATH, currentID) // Get tile image from cache
                        .Draw(g, (i % Constants.LEVEL1_WIDTH) * Constants.TILE_SIZE, // Calculate X position
                                (i / Constants.LEVEL1_WIDTH) * Constants.TILE_SIZE); // Calculate Y position
            }
        }
    }

    /**
     * @brief Draws the black fade overlay during transitions.
     *
     * If a transition is active ({@link #isTransitioning}, {@link #isSwitchingToLevel2}) or
     * if the hero's health is zero, this method draws a black rectangle over the screen
     * with an alpha value determined by {@link #targetBlackIntensity}, creating a fade effect.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawBlackFade(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        // Handle fade for general transitions or switching to Level 2
        if(this.isTransitioning || this.isSwitchingToLevel2) {
            Color originalColor = g2d.getColor(); // Save original color
            this.targetBlackIntensity += this.blackFadeStep; // Increment fade progress
            if(this.targetBlackIntensity>=1){
                this.targetBlackIntensity = 1.0; // Cap at fully opaque
            }
            int alpha = (int)(this.targetBlackIntensity * 255.0); // Calculate alpha

            g2d.setColor(new Color(0, 0, 0, alpha)); // Set color with alpha
            g.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

            g2d.setColor(originalColor); // Restore original color
        }

        // Handle fade for hero death
        if(reflink.getHero().getHealth() == 0){
            this.targetBlackIntensity+=this.blackFadeStep;
            Color originalColor = g2d.getColor();
            if(this.targetBlackIntensity>=1.0){
                this.targetBlackIntensity = 1.0;
            }
            int alpha = (int)(this.targetBlackIntensity*255.0);
            g2d.setColor(new Color(0,0,0,alpha));
            // Same consideration for screen space vs camera space as above
            g.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            g2d.setColor(originalColor);
        }
    }

    /**
     * @brief Gets the {@link Level1} map data object associated with this state.
     * @return The {@link Level1} object.
     */
    public Level1 getLevel1() {
        return level1;
    }

    /**
     * @brief Gets the name of this state.
     * @return The string identifier for this state.
     */
    @Override
    public String getStateName(){
        return stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is part of the {@link State} interface. For a PlayState like Level1State,
     * enemies are typically managed as a collection within the state rather than a single
     * 'current enemy' set externally, so this method is not used directly.
     * @param enemy The {@link Enemy}
     */
    @Override
    public void setEnemy(Enemy enemy) {
        // Not typically used in PlayState where enemies are part of the level's composition.
        // FightState uses this to know which enemy is being fought.
    }
}