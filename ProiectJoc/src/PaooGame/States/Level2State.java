package PaooGame.States;

import PaooGame.Camera.Camera;
import PaooGame.Config.Constants;
import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;
import PaooGame.Entities.Enemy;
import PaooGame.Entities.Entity;
import PaooGame.Entities.NPC;
import PaooGame.HUD.ContextHUD;
import PaooGame.HUD.MessageTriggerZone;
import PaooGame.HUD.PauseButton;
import PaooGame.Input.MouseInput;
import PaooGame.Items.BoosterItem;
import PaooGame.Items.FloppyItem;
import PaooGame.Items.BonfireItem;
import PaooGame.Maps.Level2;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Objects;

/**
 * @class Level2State
 * @brief Represents the game state for Level 2.
 *
 * This class manages all aspects of Level 2, building upon the {@link State} class.
 * It includes the level map, hero interactions, enemies, NPCs, items (save points, boosters, floppy disks),
 * camera control, UI elements (pause button, context HUD), and handles transitions
 * to other states (FightState, Level3State, DeathState, PauseMenuState, ShopState).
 * It also implements saving and loading of level-specific progress.
 */
public class Level2State extends State {
    protected String stateName = Constants.LEVEL2_STATE;    ///< The name identifier for this state.
    private Level2 level2;                                  ///< The Level2 map data object.
    private Camera camera;                                  ///< The game camera for this level.
    private PauseButton pauseButton;                        ///< The UI button to pause the game.
    private int levelWidth;                                 ///< The total width of the level in pixels.
    private int levelHeight;                                ///< The total height of the level in pixels.

    protected boolean transitioning = false;                ///< General flag indicating if a transition (fade) to another level or state is in progress.
    protected boolean transition_to_fight = false;          ///< Flag indicating a transition to FightState.
    private boolean isCameraSet = false;                    ///< Flag indicating if the camera has been initially set

    private Entity[] npcs;                                  ///< Array of NPC entities in the level.
    private int nrOfNpcs = 1;                               ///< The number of NPCs in this level.

    private ContextHUD contextHUD;                          ///< Heads-Up Display for contextual messages.

    private int nrOfEnemies = 3;                            ///< The number of enemies in this level.
    private Enemy[] enemies;                                ///< Array of enemy entities present in this level.
    private BonfireItem[] saves;                            ///< Array of save points (bonfires) in the level.
    private BoosterItem[] boosters;                         ///< Array of booster items in the level.
    private int nrOfBoosters = 3;                           ///< The number of booster items in this level.
    private int nrOfSaves = 1;                              ///< The number of save points in this level.
    private FloppyItem[] floppyDisks;                       ///< Array of floppy disk items, associated with save points.

    protected double targetBlackIntensity = 0.0;            ///< Current intensity of the black overlay for fade transitions (0.0 to 1.0).
    protected double blackFadeStep = 0.05;                  ///< Increment step for {@link #targetBlackIntensity} during fades.

    /**
     * @brief Constructs a Level2State object.
     *
     * Initializes the level map, camera, items (save points, boosters, floppy disks),
     * NPCs, enemies, HUD elements, and calculates level dimensions.
     * Sets up message trigger zones for the context HUD.
     * @param reflink A {@link RefLinks} object providing access to game-wide resources and utilities.
     * @param level2 The {@link Level2} map data for this state.
     */
    public Level2State(RefLinks reflink, Level2 level2) {
        super(reflink);
        this.level2 = level2;
        this.saves = new BonfireItem[this.nrOfSaves];
        this.enemies = new Enemy[this.nrOfEnemies];
        this.floppyDisks = new FloppyItem[this.nrOfSaves];
        this.boosters = new BoosterItem[this.nrOfBoosters];
        this.npcs = new Entity[this.nrOfNpcs];

        this.boosters[0] = new BoosterItem(this.reflink, Constants.BOOSTER1_X, Constants.BOOSTER1_Y);
        this.boosters[1] = new BoosterItem(this.reflink, Constants.BOOSTER2_X, Constants.BOOSTER2_Y);
        this.boosters[2] = new BoosterItem(this.reflink, Constants.BOOSTER3_X, Constants.BOOSTER3_Y);

        this.npcs[0] = new NPC(this.reflink, Constants.GOBLIN_POS_X, Constants.GOBLIN_POS_Y);

        this.contextHUD = new ContextHUD(this.reflink.getHero());

        levelWidth = Constants.LEVEL2_WIDTH * Constants.TILE_SIZE;
        levelHeight = Constants.LEVEL2_HEIGHT * Constants.TILE_SIZE; // Corrected from LEVEL2_WIDTH to LEVEL2_HEIGHT
        camera = new Camera(0, 0);

        this.saves[0] = new BonfireItem(this.reflink, Constants.LEVEL2_SAVE1_X, Constants.LEVEL2_SAVE1_Y);

        this.enemies[0] = new Enemy(this.reflink, Constants.BASIC_SKELETON1_X, Constants.BASIC_SKELETON1_Y, Constants.BASIC_SKELETON_NAME);
        this.enemies[1] = new Enemy(this.reflink, Constants.STRONG_SKELETON1_X, Constants.STRONG_SKELETON1_Y, Constants.STRONG_SKELETON_NAME);
        this.enemies[2] = new Enemy(this.reflink, Constants.BASIC_SKELETON2_X, Constants.BASIC_SKELETON2_Y, Constants.BASIC_SKELETON_NAME);

        this.floppyDisks[0] = new FloppyItem(this.reflink, Constants.LEVEL2_SAVE1_X + 10, Constants.LEVEL2_SAVE1_Y - 10);

        pauseButton = new PauseButton(reflink.getHero(), 80, 50);

        addMessageTriggers();
    }

    /**
     * @brief Adds message trigger zones to the context HUD.
     *
     * These zones, when intersected by the hero, display predefined messages.
     */
    private void addMessageTriggers() {
        contextHUD.addTrigger(new MessageTriggerZone(
                415, 150, 80, 50,
                "Parcurge peste pentru a trece la nivelul următor!"
        ));
        contextHUD.addTrigger(new MessageTriggerZone(
                800, 220, 80, 50,
                "Ai grija, unde flori sunt otravitoare! "
        ));
        contextHUD.addTrigger(new MessageTriggerZone(
                1830, 380, 60, 50,
                "Apasa E pentru a deschide usa catre nivelul 3"
        ));
        contextHUD.addTrigger(new MessageTriggerZone(
                1500, 280, 60, 50,
                "Acest jump este foarte riscant, asigura te ca esti bine pozitionat"
        ));
    }

    /**
     * @brief Restores the state of Level 2, primarily resetting enemy positions.
     *
     * This method is called when returning to this state. It places enemies
     * at their initial coordinates. Other elements like hero position or item states
     * might be handled by loading or specific game logic.
     */
    @Override
    public void restoreState() {
        this.enemies[0].setX(Constants.BASIC_SKELETON1_X);
        this.enemies[0].setY(Constants.BASIC_SKELETON1_Y);
        this.enemies[1].setX(Constants.STRONG_SKELETON1_X);
        this.enemies[1].setY(Constants.STRONG_SKELETON1_Y);
        this.enemies[2].setX(Constants.BASIC_SKELETON2_X);
        this.enemies[2].setY(Constants.BASIC_SKELETON2_Y);

        // Reset transition flags and fade intensity
        this.transitioning = false;
        this.transition_to_fight = false;
        this.targetBlackIntensity = 0.0;
        this.isCameraSet = false; // Reset camera flag if needed
    }

    /**
     * @brief Updates the logic of the Level2State.
     *
     * This method is called every game tick. It orchestrates updates for all game elements
     * within Level 2: NPCs, items, hero, enemies. It handles interactions, transitions,
     * pausing, camera, and the context HUD.
     */
    @Override
    public void update() {
        if (Objects.equals(State.getState().getStateName(), this.getStateName())) {
            this.reflink.setCurrentRunningLevel(this.reflink.getGame().getLevel2State()); // Set current running level reference
        }

        updateNPCs();
        handleNPCInteraction();
        updateBoosterItems();
        handleBoosterInteraction();

        this.reflink.getHero().update();
        this.floppyDisks[0].updateItem(); // Assuming one floppy disk tied to the save logic

        updateBonfireItems();
        handleSaveInteraction();
        handleEnemyInteractions();
        handleFightTransition();
        handleLevelBoundaryAndTransitionTrigger(); // Handles hero reaching level exit area

        if (handlePauseInput()) { // If game is paused, further updates might be skipped
            return;
        }

        updateCamera();
        handleDeathTransition();
        handleLevel3Transition(); // Handles transition to Level 3

        this.contextHUD.update();
    }


    /**
     * @brief Draws the content of the Level2State on the screen.
     *
     * This method is called every frame to render all visual elements of Level 2.
     * It applies camera transformations and draws the background, tiles, items,
     * enemies, NPCs, hero, and UI elements like health bars and pause button.
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform(); // Save original transform

        camera.apply(g2d); // Apply camera transformation

        drawBackground(g);
        drawTiles(g);
        drawBonfireItems(g);
        drawEnemies(g);
        drawBoosterItems(g);
        drawFadeEffect(g2d); // Draw fade overlay if transitioning
        drawFloppyDisks(g);
        drawNPCs(g);

        this.reflink.getHero().draw(g); // Draw hero

        g2d.setTransform(originalTransform); // Reset transform for HUD elements

        this.reflink.getHero().DrawHealthBar(g); // Draw hero health bar
        pauseButton.draw(g2d); // Draw pause button
        this.contextHUD.draw(g2d); // Draw contextual messages
    }


    /**
     * @brief Updates all NPC entities in the level.
     */
    private void updateNPCs() {
        for (Entity npc : npcs) {
            if (npc != null) {
                npc.update();
            }
        }
    }

    /**
     * @brief Handles hero interaction with NPCs, specifically for opening the shop.
     *
     * If the hero is near an NPC and presses the 'T' key, the game transitions
     * to the {@link ShopState}.
     */
    private void handleNPCInteraction() {
        for (Entity npc : npcs) {
            if (npc != null && npc instanceof NPC) {
                NPC currentNPC = (NPC) npc;
                if (!currentNPC.isActive() &&
                        reflink.getHero().getHitbox().intersects(currentNPC.getHitbox()) &&
                        reflink.getKeyManager().isKeyPressedOnce(KeyEvent.VK_T)) {
                    State.setState(reflink.getGame().getShopState()); // Transition to ShopState
                }
            }
        }
    }

    /**
     * @brief Updates all booster items in the level.
     */
    private void updateBoosterItems() {
        for (BoosterItem item : this.boosters) {
            item.updateItem();
        }
    }

    /**
     * @brief Handles hero interaction with booster items.
     *
     * If the hero touches any booster item, their jump strength is increased.
     * Otherwise, it's set to the base jump strength.
     */
    private void handleBoosterInteraction() {
        int nrOfTouchedBoosters = 0;
        for (BoosterItem item : this.boosters) {
            if (item.getHitbox().intersects(this.reflink.getHero().getHitbox())) {
                nrOfTouchedBoosters++;
            }
        }
        if (nrOfTouchedBoosters > 0) {
            this.reflink.getHero().setJumpStrength(Constants.HERO_BOOSTED_JUMP_STRENGTH); // Apply boosted jump
        } else {
            this.reflink.getHero().setJumpStrength(Constants.HERO_BASE_JUMP_STRENGTH); // Revert to base jump
        }
    }

    /**
     * @brief Updates all bonfire (save point) items in the level.
     */
    private void updateBonfireItems() {
        for (int i = 0; i < this.nrOfSaves; ++i) {
            this.saves[i].updateItem();
        }
    }

    /**
     * @brief Handles hero interaction with save points (bonfires).
     *
     * If the hero touches a save point and the associated floppy disk (if required by game logic,
     * e.g., `nrOfCollectedSaves == 1`), the game state is saved.
     */
    private void handleSaveInteraction() {
        // Assuming interaction with the first save point for this logic
        if (this.reflink.getHero().getHitbox().intersects(this.saves[0].getHitbox())) {
            // Condition for saving: hero has collected 1 save previously (e.g., from Level 1)
            // and now touches the floppy disk for Level 2's save.
            if (this.reflink.getHero().getNrOfCollectedSaves() == 1) {
                if (this.reflink.getHero().getHitbox().intersects(this.floppyDisks[0].getHitbox())) {
                    this.reflink.getHero().setNrOfCollectedSaves(this.reflink.getHero().getNrOfCollectedSaves() + 1);
                    this.reflink.getHero().setNrOfEscapes(this.reflink.getHero().getMaxNrOfEscapes()); // Replenish escapes
                    // Reset flags to allow storing data
                    this.reflink.setHeroStoreDoneSignal(false);
                    this.reflink.setLevel2StoreDoneSignal(false);
                    this.reflink.getHero().storeHeroState(true); // Store hero's general state
                    this.storeState(true); // Store Level2 specific state
                    try {
                        this.reflink.getDataProxy().storeBuffer(true); // Commit stored data
                    } catch (AccessNotPermittedException | ValueStoreException e) {
                        System.err.println("Error storing buffer after save in Level2: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * @brief Handles hero interactions with enemies.
     *
     * If an enemy is alive and the hero collides with it (and is ready to engage),
     * a transition to the {@link FightState} is initiated. Defeated enemies have their hitboxes nullified.
     * All enemies are updated.
     */
    private void handleEnemyInteractions() {
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                if (enemy.getHealth() == 0) {
                    enemy.nullifyHitbox(); // Make defeated enemies non-interactive
                } else {
                    // Check for collision and if hero is ready to engage
                    if (reflink.getHero().getHitbox().intersects(enemy.getHitbox()) && reflink.getHero().getEngageReady()) {
                        enemy.setIsEngaged(true); // Mark enemy as engaged
                        this.transitioning = true; // Start general fade transition
                        this.transition_to_fight = true; // Specifically for fight state
                        reflink.getGame().getFightState().setEnemy(enemy); // Set the enemy for FightState
                    }
                }
                enemy.update(); // Update enemy logic
            }
        }
    }

    /**
     * @brief Handles the transition to the FightState.
     *
     * If {@link #transition_to_fight} is true and the fade-to-black effect is complete,
     * it resets flags and changes the game state to {@link FightState}.
     */
    private void handleFightTransition() {
        if (this.transition_to_fight && this.targetBlackIntensity == 1) { // If transitioning to fight and fade is complete
            this.targetBlackIntensity = 0; // Reset fade for next screen
            this.transitioning = false;
            this.transition_to_fight = false;
            State.setState(reflink.getGame().getFightState()); // Change state to Fight
        }
    }

    /**
     * @brief Handles hero reaching level boundaries and triggers for transitioning to the next level.
     *
     * Prevents the hero from moving beyond the rightmost boundary.
     * Sets the {@link #transitioning} flag if the hero is near the exit area for Level 3.
     */
    private void handleLevelBoundaryAndTransitionTrigger() {
        // Prevent hero from going past a certain x-coordinate (right boundary)
        if (this.reflink.getHero().getX() > 1880) {
            this.reflink.getHero().setX(1870);
            this.reflink.getHero().getHitbox().setX(1870);
        }
        // Trigger transition to Level 3 if hero is in the exit zone
        if (this.reflink.getHero().getX() > 1855 && this.reflink.getHero().getY() < 400) {
            this.transitioning = true; // Start general fade transition for level switch
        }
    }

    /**
     * @brief Handles pausing the game via keyboard (Escape) or mouse click on the pause button.
     * @return True if the game was paused (and state changed), false otherwise.
     */
    private boolean handlePauseInput() {
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
            return true; // Game was paused
        }
        return false; // Game was not paused by this interaction
    }

    /**
     * @brief Updates the camera position to follow the hero.
     *
     * The camera attempts to center on the hero, with constraints to keep it within
     * level boundaries. There's also an initial setup condition (`isCameraSet`).
     * The camera also pans horizontally with the hero's velocity.
     */
    private void updateCamera() {
        // Initial camera setup logic
        if (State.getState().getStateName().equals(this.stateName) && !this.isCameraSet) { // Used .equals for string comparison
            float heroCenterX = this.reflink.getHero().getX() + this.reflink.getHero().getWidth() / 2;
            float heroCenterY = this.reflink.getHero().getY() + this.reflink.getHero().getHeight() / 2;
            double cameraX = heroCenterX - (Constants.WINDOW_WIDTH / 2) / camera.getScale();
            double cameraY = heroCenterY - (Constants.WINDOW_HEIGHT / 2) / camera.getScale() - 1000;

            double maxCameraX = levelWidth - (Constants.WINDOW_WIDTH / camera.getScale());
            double maxCameraY = Constants.WINDOW_HEIGHT;

            cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
            cameraY = Math.max(0, Math.min(cameraY, maxCameraY)) + 350; // Additional offset

            camera.setPosition(cameraX, cameraY);

        }
        // Smooth camera panning based on hero's X velocity
        camera.updatePosition(this.reflink.getHero().getVelocityX(), 0);
    }


    /**
     * @brief Handles the transition to the DeathState if the hero's health reaches zero.
     *
     * If the hero is defeated and the fade-to-black effect is complete,
     * it resets the fade, restores {@link DeathState}, and changes the game state.
     */
    private void handleDeathTransition() {
        if (this.reflink.getHero().getHealth() == 0 && this.targetBlackIntensity == 1) { // Hero defeated and fade complete
            this.targetBlackIntensity = 0; // Reset fade for next screen
            this.reflink.getGame().getDeathState().restoreState(); // Prepare DeathState
            State.setState(this.reflink.getGame().getDeathState()); // Change state to Death
        }
    }

    /**
     * @brief Handles the transition to Level 3.
     *
     * If {@link #transitioning} is true (triggered by reaching the exit area),
     * the fade-to-black effect is complete, and the hero is at the far right of the level,
     * it resets flags, potentially saves progress, updates hero's properties for Level 3,
     * and changes the game state to {@link Level3State}.
     */
    private void handleLevel3Transition() {
        // Condition: general transition is active, fade is complete, and hero is at Level 2 exit area
        if (this.transitioning && this.targetBlackIntensity == 1 && this.reflink.getHero().getX() > 1850) {
            this.targetBlackIntensity = 0; // Reset fade
            this.reflink.getHero().setJumpStrength(Constants.HERO_BASE_JUMP_STRENGTH); // Reset jump strength
            this.transitioning = false;

            // If Level 2 is completed for the first time
            if (this.reflink.getHero().getNrOfCompletedLevels() == 1) {
                this.reflink.getHero().setNrOfCompletedLevels(2);
                // Save progress before switching
                this.reflink.setLevel2StoreDoneSignal(false);
                this.storeState(true);
                this.reflink.setHeroStoreDoneSignal(false);
                this.reflink.getHero().storeHeroState(true);
                try {
                    this.reflink.getDataProxy().storeBuffer(true);
                } catch (AccessNotPermittedException | ValueStoreException e) {
                    System.err.println("Error storing buffer on L2->L3 transition: " + e.getMessage());
                }
            }

            // Set hero's starting position and properties for Level 3
            this.reflink.getHero().setX(Constants.HERO_LEVEL3_STARTING_X);
            this.reflink.getHero().getHitbox().setX(Constants.HERO_LEVEL3_STARTING_X);
            this.reflink.getHero().setY(Constants.HERO_LEVEL3_STARTING_Y);
            this.reflink.getHero().getHitbox().setY(Constants.HERO_LEVEL3_STARTING_Y);
            this.reflink.getHero().setJumpStrength(-3.5f); // Specific jump strength for L3 start
            State.setState(this.reflink.getGame().getLevel3State()); // Change state to Level 3
        }
    }


    /**
     * @brief Draws the background of Level 2.
     *
     * Two copies of the background image are drawn side-by-side to allow for
     * potential scrolling effects
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawBackground(Graphics g) {
        BufferedImage backgroundImage = this.reflink.getTileCache().getBackground(Constants.LEVEL2_BG_PATH);
        g.drawImage(backgroundImage, 0, -210, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, Constants.WINDOW_WIDTH, -210, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, null);
    }

    /**
     * @brief Draws the visual tiles of Level 2.
     *
     * Iterates through the visual tile IDs of the level map data and renders each tile
     * using the {@link PaooGame.Tiles.TileCache}.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawTiles(Graphics g) {
        for (int i = 0; i < Constants.LEVEL2_TILE_NR; ++i) { // Iterate through all tiles in 1D representation
            int currentID = this.level2.getVisualIDs()[i]; // Get visual ID of current tile
            if (currentID != -1) {
                this.reflink.getTileCache()
                        .getTile(Constants.LEVEL2_TEXTURES_PATH, currentID) // Get tile image from cache
                        .Draw(g, (i % Constants.LEVEL2_WIDTH) * Constants.TILE_SIZE, // Calculate X position
                                (i / Constants.LEVEL2_WIDTH) * Constants.TILE_SIZE); // Calculate Y position
            }
        }
    }

    /**
     * @brief Draws all bonfire (save point) items in the level.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawBonfireItems(Graphics g) {
        for (int i = 0; i < this.nrOfSaves; ++i) {
            this.saves[i].drawItem(g);
        }
    }

    /**
     * @brief Draws all living enemies in the level.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawEnemies(Graphics g) {
        for (Enemy enemy : enemies) {
            if (enemy != null && enemy.getHealth() > 0) { // Only draw if enemy exists and is alive
                enemy.draw(g);
            }
        }
    }

    /**
     * @brief Draws all booster items in the level.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawBoosterItems(Graphics g) {
        for (BoosterItem item : this.boosters) {
            item.drawItem(g);
        }
    }

    /**
     * @brief Draws the black fade overlay effect during transitions or on hero death.
     *
     * If a relevant transition flag is active or hero's health is zero,
     * this method draws a black rectangle with an alpha value determined by
     * {@link #targetBlackIntensity}. The rectangle's position seems to be
     * relative to the hero, which might create an unusual fade effect.
     * @param g2d The {@link Graphics2D} context to draw on.
     */
    private void drawFadeEffect(Graphics2D g2d) {
        // Conditions for applying fade: hero death, transitioning to L3, or transitioning to fight
        if (reflink.getHero().getHealth() == 0 || (this.transitioning && this.reflink.getHero().getX() > 1850) || this.transition_to_fight) {
            this.targetBlackIntensity += this.blackFadeStep; // Increment fade progress
            Color originalColor = g2d.getColor(); // Save original color
            if (this.targetBlackIntensity >= 1.0) {
                this.targetBlackIntensity = 1.0; // Cap at fully opaque
            }
            int alpha = (int) (this.targetBlackIntensity * 255.0); // Calculate alpha
            g2d.setColor(new Color(0, 0, 0, alpha)); // Set color with alpha
            g2d.fillRect((int) this.reflink.getHero().getX() - Constants.WINDOW_WIDTH / 2, 0, Constants.WINDOW_WIDTH * 2, Constants.WINDOW_HEIGHT * 2);
            g2d.setColor(originalColor); // Restore original color
        }
    }

    /**
     * @brief Draws floppy disk items, typically if associated save point requirement is met.
     *
     * In this case, it draws the first floppy disk if the hero has collected exactly one save previously.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawFloppyDisks(Graphics g) {
        // Draw floppy disk if hero has collected 1 save
        if (this.reflink.getHero().getNrOfCollectedSaves() == 1) {
            this.floppyDisks[0].drawItem(g);
        }
    }

    /**
     * @brief Draws all NPC entities in the level.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawNPCs(Graphics g) {
        for (Entity npc : npcs) {
            if (npc != null) {
                npc.draw(g);
            }
        }
    }


    /**
     * @brief Gets the {@link Level2} map data object associated with this state.
     * @return The {@link Level2} object.
     */
    public Level2 getLevel2() {
        return level2;
    }

    /**
     * @brief Gets the name of this state.
     * @return The string identifier for this state.
     */
    @Override
    public String getStateName() {
        return stateName;
    }

    /**
     * @brief Sets an enemy for this state.
     *
     * This method is part of the {@link State} interface. For a PlayState like Level2State,
     * enemies are managed as a collection within the state. This method has no implementation here.
     * @param enemy The {@link Enemy}
     */
    @Override
    public void setEnemy(Enemy enemy) {
        // No implementation in original. Enemies are managed internally.
    }

    /**
     * @brief Loads level-specific state data, primarily enemy health, for Level 2.
     *
     * Attempts to load health values for all enemies in Level 2 from the data proxy.
     * Uses a flag ({@link RefLinks#getLevel2RefreshDoneSignal()}) to prevent redundant loading.
     * @param access A boolean flag, typically true, indicating permission to access stored data.
     */
    @Override
    public void loadState(boolean access) {
        if (this.reflink.getLevel2RefreshDoneSignal()) { // Prevent redundant loading
            return;
        }
        try {
            // Load health for each enemy in Level 2
            this.enemies[0].setHealth(this.reflink.getDataProxy().load(Constants.BASIC_SKELETON0_HEALTH, access));
            this.enemies[1].setHealth(this.reflink.getDataProxy().load(Constants.STRONG_SKELETON0_HEALTH, access));
            this.enemies[2].setHealth(this.reflink.getDataProxy().load(Constants.BASIC_SKELETON1_HEALTH, access));
            this.reflink.setLevel2RefreshDoneSignal(true); // Mark loading as done for this cycle
        } catch (AccessNotPermittedException | DataBufferNotReadyException | ValueStoreException e) {
            System.err.println("Error loading Level2State data: " + e.getMessage());
        }
    }

    /**
     * @brief Stores level-specific state data, primarily enemy health and current state identifier, for Level 2.
     *
     * Saves the current health of all enemies in Level 2 and identifies this level
     * as the current state (with ID 2) to the data proxy.
     * Uses a flag ({@link RefLinks#getLevel2StoreDoneSignal()}) to prevent redundant saving.
     * @param access A boolean flag, typically true, indicating permission to store data.
     */
    @Override
    public void storeState(boolean access) {
        if (this.reflink.getLevel2StoreDoneSignal()) { // Prevent redundant saving
            return;
        }
        try {
            // If Level2State is the active state, mark it in the save data.
            if (State.getState().getStateName().compareTo(this.stateName) == 0) {
                this.reflink.getDataProxy().store(Constants.CURRENT_STATE, 2, access);
            }
            this.reflink.getDataProxy().store(Constants.TIMESTAMP, (int) Instant.now().getEpochSecond(), access); // Save timestamp
            // Save health for each enemy in Level 2
            this.reflink.getDataProxy().store(Constants.BASIC_SKELETON0_HEALTH, (int) this.enemies[0].getHealth(), access);
            this.reflink.getDataProxy().store(Constants.STRONG_SKELETON0_HEALTH, (int) this.enemies[1].getHealth(), access);
            this.reflink.getDataProxy().store(Constants.BASIC_SKELETON1_HEALTH, (int) this.enemies[2].getHealth(), access);
            this.reflink.setLevel2StoreDoneSignal(true); // Mark saving as done for this cycle
        } catch (AccessNotPermittedException | ValueStoreException e) {
            System.err.println("Error storing Level2State data: " + e.getMessage());
        }
    }
}