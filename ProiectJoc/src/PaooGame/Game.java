package PaooGame;

import PaooGame.Config.Constants;
import PaooGame.DatabaseManaging.DataManager;
import PaooGame.DatabaseManaging.ProxyDataManager;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Entities.Hero;
import PaooGame.Input.KeyManager;
import PaooGame.Input.MouseInput;
import PaooGame.Maps.Level1;
import PaooGame.Maps.Level2;
import PaooGame.States.*;
import PaooGame.Strategies.EnemyStrategies.*;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileCache;
import PaooGame.Maps.Level3;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * @class Game
 * @brief Main class for the game, responsible for initialization, game loop, and state management.
 *
 * This class implements the {@link Runnable} interface to handle the game loop in a separate thread.
 * It initializes all game components, including the game window, input managers, game states,
 * entities, levels, and data management systems. It also manages the update and rendering cycles.
 */
public class Game implements Runnable {
    private GameWindow wnd;                             ///< The main game window.
    private boolean runState;                           ///< Flag indicating if the game loop is running.
    private Thread gameThread;                          ///< The thread where the game loop runs.
    private BufferStrategy bs;                          ///< BufferStrategy for smooth rendering.

    private Graphics g;                                 ///< Graphics context for drawing.
    private State menuState;                            ///< The main menu game state.

    // Enemy Strategies
    private EnemyStrategy tigerEnemyStrategy;           ///< Strategy for Tiger enemies.
    private EnemyStrategy basicSkeletonStrategy;        ///< Strategy for Basic Skeleton enemies.
    private EnemyStrategy wizardStrategy;               ///< Strategy for Wizard enemies.
    private EnemyStrategy minotaurStrategy;             ///< Strategy for Minotaur enemies.
    private EnemyStrategy ghostStrategy;                ///< Strategy for Ghost enemies.
    private EnemyStrategy strongSkeletonStrategy;       ///< Strategy for Strong Skeleton enemies.

    private DataManager dataProxy;                      ///< Data manager for saving and loading game data.

    // Level States and Data
    private State level1State;                          ///< The game state for Level 1.
    private Level1 level1;                              ///< The map data for Level 1.
    private State level2State;                          ///< The game state for Level 2.
    private Level2 level2;                              ///< The map data for Level 2.
    private State level3State;                          ///< The game state for Level 3.
    private Level3 level3;                              ///< The map data for Level 3.

    // High Scores
    private int score1;                                 ///< First top score.
    private int score2;                                 ///< Second top score.
    private int score3;                                 ///< Third top score.

    // Other Game States
    private State shopState;                            ///< The in-game shop state.
    private State winState;                             ///< The game win state.
    private State pauseMenuState;                       ///< The pause menu state.
    private State aboutState;                           ///< The about/credits state (currently placeholder).
    private State fightState;                           ///< The combat/fight state.
    private State deathState;                           ///< The game over/death state.

    // Input and References
    private KeyManager keyManager;                      ///< Manages keyboard input.
    private RefLinks refLink;                           ///< Provides shared references to game components.
    private MouseInput mouseInput;                      ///< Manages mouse input.

    // Game Assets and Entities
    private TileCache tileCache;                        ///< Cache for managing tile images.
    private Hero hero;                                  ///< The player character.

    private Tile tile;                                  ///< A generic tile instance (purpose here might be for testing or as a placeholder).

    /**
     * @brief Constructs a Game object.
     *
     * Initializes the game window with the specified title, width, and height.
     * @param title The title of the game window.
     * @param width The width of the game window in pixels.
     * @param height The height of the game window in pixels.
     */
    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false; // Game loop is not running initially
    }

    /**
     * @brief Initializes all game components and systems.
     *
     * This method sets up the game window, input managers, tile cache, hero,
     * levels, data manager, enemy strategies, and all game states.
     * It also initializes various signal flags for data refresh and storage.
     * The initial game state is set to the main menu.
     */
    private void InitGame() {

        // Initialize tile cache
        tileCache = TileCache.getInstance();

        // Build game window and set up input listeners
        wnd.BuildGameWindow();
        mouseInput = new MouseInput();
        this.keyManager = new KeyManager();
        wnd.GetCanvas().addKeyListener(keyManager);
        wnd.GetCanvas().addMouseListener(mouseInput);
        wnd.GetCanvas().addMouseMotionListener(mouseInput);


        // Initialize reference links and pass shared components
        refLink = new RefLinks(this);
        refLink.setKeyManager(keyManager);
        refLink.setMouseInput(mouseInput);
        refLink.setTileCache(tileCache);

        // Initialize hero character with starting position for Level 1
        hero = new Hero(refLink,Constants.HERO_LEVEL1_STARTING_X,Constants.HERO_LEVEL1_STARTING_Y);
        this.refLink.setHero(hero);

        // Initialize level data
        level1 = new Level1();
        level2 = new Level2();
        level3 = new Level3();

        // Initialize data refresh/store signals
        this.refLink.setDataRefreshSignal(false);
        this.refLink.setLevel1RefreshDoneSignal(false);
        this.refLink.setLevel2RefreshDoneSignal(false);
        this.refLink.setLevel3RefreshDoneSignal(false);
        this.refLink.setHeroRefreshDoneSignal(false);

        // Initialize data manager (proxy)
        this.dataProxy = new ProxyDataManager(this.refLink);
        this.refLink.setDataProxy(this.dataProxy);

        // Initialize enemy strategies
        strongSkeletonStrategy = StrongSkeletonEnemyStrategy.getInstance(refLink);
        this.refLink.setStrongSkeletonEnemyStrategy(this.strongSkeletonStrategy);

        ghostStrategy = GhostEnemyStrategy.getInstance(refLink);
        this.refLink.setGhostEnemyStrategy(this.ghostStrategy);

        minotaurStrategy = MinotaurEnemyStrategy.getInstance(refLink);
        this.refLink.setMinotaurEnemyStrategy(this.minotaurStrategy);

        wizardStrategy = WizardEnemyStrategy.getInstance(refLink);
        this.refLink.setWizardEnemyStrategy(this.wizardStrategy);

        tigerEnemyStrategy = TigerEnemyStrategy.getInstance(refLink);
        this.refLink.setTigerEnemyStrategy(this.tigerEnemyStrategy);

        basicSkeletonStrategy = BasicSkeletonEnemyStrategy.getInstance(refLink);
        this.refLink.setBasicSkeletonStrategy(this.basicSkeletonStrategy);

        // Initialize all game states
        winState = new WinState(refLink);
        menuState = new MenuState(refLink);
        fightState = new FightState(refLink);
        level1State = new Level1State(refLink,level1);
        level2State = new Level2State(refLink,level2);
        level3State = new Level3State(refLink,level3);
        deathState = new DeathState(refLink);
        pauseMenuState = new PauseMenuState(refLink);
        shopState = new ShopState(refLink);

        // Set the initial game state to the main menu
        State.setState(menuState);
    }


    /**
     * @brief The main game loop.
     *
     * Initializes the game and then enters a loop that updates and draws the game
     * at a fixed rate (target 60 frames per second).
     */
    public void run() {
        InitGame();
        long oldTime = System.nanoTime(); // Time of the last frame
        long curentTime; // Current time

        final int framesPerSecond = 60; // Target FPS
        final double timeFrame = 1000000000.0 / framesPerSecond; // Time per frame in nanoseconds

        // Game loop
        while (runState == true) {
            curentTime = System.nanoTime();
            if ((curentTime - oldTime) > timeFrame) { // If enough time has passed for a new frame
                Update(); // Update game logic
                Draw();   // Render the game
                oldTime = curentTime; // Update oldTime for the next iteration
            }
        }
    }

    /**
     * @brief Starts the game thread if it's not already running.
     *
     * Sets the {@link #runState} to true and creates and starts a new {@link Thread}
     * for the game loop.
     */
    public synchronized void StartGame() {
        if (runState == false) { // If game is not already running
            runState = true;
            gameThread = new Thread(this); // Create a new thread for this Game instance
            gameThread.start(); // Start the game loop
        } else {
            return; // Game is already running
        }
    }

    /**
     * @brief Stops the game thread if it's running.
     *
     * Sets the {@link #runState} to false and waits for the game thread to join (terminate).
     */
    public synchronized void StopGame() {
        if (runState == true) { // If game is running
            runState = false; // Signal the game loop to stop
            try {
                gameThread.join(); // Wait for the game thread to finish
            } catch (InterruptedException ex) {
                ex.printStackTrace(); // Handle interruption exception
            }
        } else {
            return; // Game is not running
        }
    }

    /**
     * @brief Updates the game logic.
     *
     * This method handles data refresh and storage signals, ensuring that game states
     * (levels, hero) load or store their data as needed. It then calls the update
     * method of the current active game state.
     */
    private void Update() {

        // Handle data refresh signal: load data for all relevant states
        if(this.refLink.getDataRefreshSignal()){
            // Reset done signals for individual components
            this.refLink.setHeroRefreshDoneSignal(false);
            this.refLink.setLevel1RefreshDoneSignal(false);
            this.refLink.setLevel2RefreshDoneSignal(false);
            this.refLink.setLevel3RefreshDoneSignal(false);
            this.refLink.setDataRefreshSignal(false); // Reset the main refresh signal

            // Load state for levels and hero
            this.level1State.loadState(true);
            this.level2State.loadState(true);
            this.level3State.loadState(true);
            this.refLink.getHero().loadHeroState(true);
        }


        // Handle data store signal: store data for all relevant states
        if(this.refLink.getDataStoreSignal()){
            this.refLink.setHeroStoreDoneSignal(false);
            this.refLink.setLevel1StoreDoneSignal(false);
            this.refLink.setLevel2StoreDoneSignal(false);
            this.refLink.setLevel3StoreDoneSignal(false);
            this.refLink.setDataStoreSignal(false); // Reset the main store signal

            // Store state for levels and hero
            this.level1State.storeState(true);
            this.level2State.storeState(true);
            this.level3State.storeState(true);
            this.refLink.getHero().storeHeroState(true);
        }

        // Update the current active game state
        if (State.getState() != null) {
            State.getState().update();
        }
    }

    /**
     * @brief Draws the game to the screen.
     *
     * Retrieves the buffer strategy from the game window's canvas. If it doesn't exist,
     * it attempts to create one. It then gets the graphics context, clears the screen,
     * and calls the draw method of the current active game state. Finally, it shows
     * the buffer and disposes of the graphics context.
     */
    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy(); // Get the buffer strategy
        if (bs == null) { // If buffer strategy doesn't exist, create it
            try {
                wnd.GetCanvas().createBufferStrategy(3); // Triple buffering
                return; // Exit draw method for this frame, will be available next frame
            } catch (Exception e) {
                e.printStackTrace(); // Handle exception during buffer strategy creation
            }
        }
        g = bs.getDrawGraphics(); // Get graphics context from buffer strategy
        // Clear the screen
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        // Draw the current active game state
        if (State.getState() != null) {
            State.getState().draw(g);
        }
        // Show the contents of the back buffer and dispose graphics context
        bs.show();
        g.dispose();
    }

    /**
     * @brief Gets the width of the game window.
     * @return The width of the window in pixels.
     */
    public int getWidth() {
        return wnd.GetWndWidth();
    }

    /**
     * @brief Gets the height of the game window.
     * @return The height of the window in pixels.
     */
    public int getHeight() {
        return wnd.GetWndHeight();
    }

    /**
     * @brief Gets the keyboard input manager.
     * @return The {@link KeyManager} instance.
     */
    public KeyManager getKeyManager() {
        return keyManager;
    }

    /**
     * @brief Gets the game state for Level 1.
     * @return The {@link Level1State} instance.
     */
    public State getLevel1State() {
        return level1State;
    }
    /**
     * @brief Gets the game state for Level 2.
     * @return The {@link Level2State} instance.
     */
    public State getLevel2State() {
        return level2State;
    }
    /**
     * @brief Gets the game state for Level 3.
     * @return The {@link Level3State} instance.
     */
    public State getLevel3State() {return level3State;}

    /**
     * @brief Gets the mouse input manager.
     * @return The {@link MouseInput} instance.
     */
    public MouseInput getMouseInput() {
        return mouseInput;
    }

    /**
     * @brief Gets the primary play state (currently defaults to Level 1 State).
     * @return The {@link Level1State} instance.
     */
    public State getPlayState() {
        return level1State; // This might need to be more dynamic if "PlayState" can change
    }

    /**
     * @brief Gets the fight/combat game state.
     * @return The {@link FightState} instance.
     */
    public State getFightState(){ return fightState; }

    /**
     * @brief Gets the game win state.
     * @return The {@link WinState} instance.
     */
    public State getWinState() {return winState;}

    /**
     * @brief Gets the pause menu game state.
     * @return The {@link PauseMenuState} instance.
     */
    public State getPauseMenuState() {
        return pauseMenuState;
    }

    /**
     * @brief Gets the in-game shop state.
     * @return The {@link ShopState} instance.
     */
    public State getShopState(){return shopState;}

    /**
     * @brief Gets the main menu game state.
     * @return The {@link MenuState} instance.
     */
    public State getMenuState() {
        return menuState;
    }

    /**
     * @brief Gets the game over/death state.
     * @return The {@link DeathState} instance.
     */
    public State getDeathState() {return deathState;}

    /**
     * @brief Gets the map data for Level 1.
     * @return The {@link Level1} map data object.
     */
    public Level1 getLevel1(){ return this.level1; }
    /**
     * @brief Gets the map data for Level 2.
     * @return The {@link Level2} map data object.
     */
    public Level2 getLevel2() {return this.level2; }
    /**
     * @brief Gets the map data for Level 3.
     * @return The {@link Level3} map data object.
     */
    public Level3 getLevel3() {return this.level3;}

    /**
     * @brief Resets Level 1 to its initial state.
     * Calls the {@link State#restoreState()} method of the Level 1 state.
     */
    public void resetLevel1State() {
        level1State.restoreState();
    }
    /**
     * @brief Resets Level 2 to its initial state.
     * Calls the {@link State#restoreState()} method of the Level 2 state.
     */
    public void resetLevel2State(){
        level2State.restoreState();
    }
    /**
     * @brief Resets Level 3 to its initial state.
     * Calls the {@link State#restoreState()} method of the Level 3 state.
     */
    public void resetLevel3State() { level3State.restoreState(); }

    /**
     * @brief Gets the first top score.
     * @return The first top score.
     */
    public int getScore1(){return score1;}
    /**
     * @brief Gets the second top score.
     * @return The second top score.
     */
    public int getScore2(){return score2;}
    /**
     * @brief Gets the third top score.
     * @return The third top score.
     */
    public int getScore3(){return score3;}

    /**
     * @brief Sets the first top score.
     * @param score1 The value for the first top score.
     */
    public void setScore1(int score1) {this.score1 = score1;}
    /**
     * @brief Sets the second top score.
     * @param score2 The value for the second top score.
     */
    public void setScore2(int score2) {this.score2 = score2;}
    /**
     * @brief Sets the third top score.
     * @param score3 The value for the third top score.
     */
    public void setScore3(int score3) {this.score3 = score3;}


}