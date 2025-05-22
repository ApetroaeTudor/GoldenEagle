package PaooGame;

import PaooGame.DatabaseManaging.DataManager;
import PaooGame.Entities.Hero;
import PaooGame.Input.MouseInput;

import PaooGame.Input.KeyManager;
import PaooGame.States.State;
import PaooGame.Strategies.EnemyStrategies.EnemyStrategy;
import PaooGame.Tiles.TileCache;

/**
 * @class RefLinks
 * @brief Provides a centralized way to access shared game objects and utilities.
 *
 * This class acts as a "references" or "links" holder, allowing various game components
 * to easily access core systems like the main {@link Game} object, input managers (keyboard, mouse),
 * the player {@link Hero}, {@link TileCache}, enemy strategies, and data management signals.
 * It helps to reduce direct dependencies between components and simplifies passing around
 * commonly needed objects.
 */
public class RefLinks
{
    private Game game;                                  ///< Reference to the main Game object.
    private KeyManager keyManager;                      ///< Reference to the keyboard input manager.
    private MouseInput mouseInput;                      ///< Reference to the mouse input manager.

    private State currentRunningLevel;                  ///< Reference to the currently active game level state.

    // Enemy Strategies
    private EnemyStrategy tigerEnemyStrategy;           ///< Strategy for Tiger enemies.
    private EnemyStrategy basicSkeletonEnemyStrategy;   ///< Strategy for Basic Skeleton enemies.
    private EnemyStrategy wizardEnemyStrategy;          ///< Strategy for Wizard enemies.
    private EnemyStrategy minotaurEnemyStrategy;        ///< Strategy for Minotaur enemies.
    private EnemyStrategy ghostEnemyStrategy;           ///< Strategy for Ghost enemies.
    private EnemyStrategy strongSkeletonEnemyStategy;   ///< Strategy for Strong Skeleton enemies. (Typo: Strategy)
    private DataManager dataProxy;                      ///< Reference to the data manager (proxy).

    // Data Refresh Signals
    private boolean dataRefreshSignal;                  ///< Signal indicating that game data needs to be refreshed (loaded).
    private boolean level1RefreshDoneSignal;            ///< Signal indicating Level 1 has finished refreshing its data.
    private boolean level2RefreshDoneSignal;            ///< Signal indicating Level 2 has finished refreshing its data.
    private boolean level3RefreshDoneSignal;            ///< Signal indicating Level 3 has finished refreshing its data.
    private boolean heroRefreshDoneSignal;              ///< Signal indicating the Hero has finished refreshing its data.

    // Data Store Signals
    private boolean dataStoreSignal;                    ///< Signal indicating that game data needs to be stored.
    private boolean level1StoreDoneSignal;              ///< Signal indicating Level 1 has finished storing its data.
    private boolean level2StoreDoneSignal;              ///< Signal indicating Level 2 has finished storing its data.
    private boolean level3StoreDoneSignal;              ///< Signal indicating Level 3 has finished storing its data.
    private boolean heroStoreDoneSignal;                ///< Signal indicating the Hero has finished storing its data.

    /**
     * @brief Gets the first top score from the game.
     * @return The first top score.
     */
    public int getScore1(){return this.game.getScore1();}
    /**
     * @brief Gets the second top score from the game.
     * @return The second top score.
     */
    public int getScore2(){return this.game.getScore2();}
    /**
     * @brief Gets the third top score from the game.
     * @return The third top score.
     */
    public int getScore3(){return this.game.getScore3();}

    /**
     * @brief Sets the first top score in the game.
     * @param score1 The value for the first top score.
     */
    public void setScore1(int score1){this.game.setScore1(score1);}
    /**
     * @brief Sets the second top score in the game.
     * @param score2 The value for the second top score.
     */
    public void setScore2(int score2){this.game.setScore2(score2);}
    /**
     * @brief Sets the third top score in the game.
     * @param score3 The value for the third top score.
     */
    public void setScore3(int score3){this.game.setScore3(score3);}

    /**
     * @brief Sets the currently running game level state.
     * @param currentRunningLevel The {@link State} representing the active level.
     */
    public void setCurrentRunningLevel(State currentRunningLevel) { this.currentRunningLevel = currentRunningLevel;}
    /**
     * @brief Gets the currently running game level state.
     * @return The {@link State} representing the active level.
     */
    public State getCurrentRunningLevel() { return this.currentRunningLevel;}

    /**
     * @brief Sets the signal for data storage.
     * @param dataStoreSignal True to indicate data should be stored, false otherwise.
     */
    public void setDataStoreSignal(boolean dataStoreSignal) {
        this.dataStoreSignal = dataStoreSignal;
    }

    /**
     * @brief Sets the signal indicating Level 1 has finished storing data.
     * @param level1StoreDoneSignal True if Level 1 data storage is complete, false otherwise.
     */
    public void setLevel1StoreDoneSignal(boolean level1StoreDoneSignal) {
        this.level1StoreDoneSignal = level1StoreDoneSignal;
    }

    /**
     * @brief Sets the signal indicating Level 2 has finished storing data.
     * @param level2StoreDoneSignal True if Level 2 data storage is complete, false otherwise.
     */
    public void setLevel2StoreDoneSignal(boolean level2StoreDoneSignal) {
        this.level2StoreDoneSignal = level2StoreDoneSignal;
    }

    /**
     * @brief Sets the signal indicating Level 3 has finished storing data.
     * @param level3StoreDoneSignal True if Level 3 data storage is complete, false otherwise.
     */
    public void setLevel3StoreDoneSignal(boolean level3StoreDoneSignal) {
        this.level3StoreDoneSignal = level3StoreDoneSignal;
    }

    /**
     * @brief Sets the signal indicating the Hero has finished storing data.
     * @param heroStoreDoneSignal True if Hero data storage is complete, false otherwise.
     */
    public void setHeroStoreDoneSignal(boolean heroStoreDoneSignal) {
        this.heroStoreDoneSignal = heroStoreDoneSignal;
    }


    /**
     * @brief Gets the data store signal status.
     * @return True if data is currently signaled to be stored, false otherwise.
     */
    public boolean getDataStoreSignal() {
        return dataStoreSignal;
    }

    /**
     * @brief Gets the status of Level 1 data storage completion.
     * @return True if Level 1 data storage is done, false otherwise.
     */
    public boolean getLevel1StoreDoneSignal() {
        return level1StoreDoneSignal;
    }

    /**
     * @brief Gets the status of Level 2 data storage completion.
     * @return True if Level 2 data storage is done, false otherwise.
     */
    public boolean getLevel2StoreDoneSignal() {
        return level2StoreDoneSignal;
    }

    /**
     * @brief Gets the status of Level 3 data storage completion.
     * @return True if Level 3 data storage is done, false otherwise.
     */
    public boolean getLevel3StoreDoneSignal() {
        return level3StoreDoneSignal;
    }

    /**
     * @brief Gets the status of Hero data storage completion.
     * @return True if Hero data storage is done, false otherwise.
     */
    public boolean getHeroStoreDoneSignal() {
        return heroStoreDoneSignal;
    }


    /**
     * @brief Gets the status of Hero data refresh completion.
     * @return True if Hero data refresh is done, false otherwise.
     */
    public boolean getHeroRefreshDoneSignal(){
        return heroRefreshDoneSignal;
    }

    /**
     * @brief Gets the status of Level 1 data refresh completion.
     * @return True if Level 1 data refresh is done, false otherwise.
     */
    public boolean getLevel1RefreshDoneSignal() {
        return level1RefreshDoneSignal;
    }

    /**
     * @brief Gets the status of Level 2 data refresh completion.
     * @return True if Level 2 data refresh is done, false otherwise.
     */
    public boolean getLevel2RefreshDoneSignal() {
        return level2RefreshDoneSignal;
    }

    /**
     * @brief Gets the status of Level 3 data refresh completion.
     * @return True if Level 3 data refresh is done, false otherwise.
     */
    public boolean getLevel3RefreshDoneSignal() {
        return level3RefreshDoneSignal;
    }

    /**
     * @brief Gets the data refresh signal status.
     * @return True if data is currently signaled to be refreshed, false otherwise.
     */
    public boolean getDataRefreshSignal() {
        return dataRefreshSignal;
    }

    /**
     * @brief Sets the signal indicating the Hero has finished refreshing data.
     * @param heroRefreshDoneSignal True if Hero data refresh is complete, false otherwise.
     */
    public void setHeroRefreshDoneSignal(boolean heroRefreshDoneSignal){
        this.heroRefreshDoneSignal = heroRefreshDoneSignal;
    }

    /**
     * @brief Sets the signal indicating Level 3 has finished refreshing data.
     * @param level3RefreshDoneSignal True if Level 3 data refresh is complete, false otherwise.
     */
    public void setLevel3RefreshDoneSignal(boolean level3RefreshDoneSignal) {
        this.level3RefreshDoneSignal = level3RefreshDoneSignal;
    }

    /**
     * @brief Sets the signal indicating Level 2 has finished refreshing data.
     * @param level2RefreshDoneSignal True if Level 2 data refresh is complete, false otherwise.
     */
    public void setLevel2RefreshDoneSignal(boolean level2RefreshDoneSignal) {
        this.level2RefreshDoneSignal = level2RefreshDoneSignal;
    }

    /**
     * @brief Sets the signal indicating Level 1 has finished refreshing data.
     * @param level1RefreshDoneSignal True if Level 1 data refresh is complete, false otherwise.
     */
    public void setLevel1RefreshDoneSignal(boolean level1RefreshDoneSignal) {
        this.level1RefreshDoneSignal = level1RefreshDoneSignal;
    }

    /**
     * @brief Sets the signal for data refresh.
     * @param dataRefreshSignal True to indicate data should be refreshed, false otherwise.
     */
    public void setDataRefreshSignal(boolean dataRefreshSignal) {
        this.dataRefreshSignal = dataRefreshSignal;
    }


    private Hero hero;                                  ///< Reference to the player's Hero object.

    private TileCache tileCache;                        ///< Reference to the TileCache for managing tile images.

    /**
     * @brief Constructs a RefLinks object.
     * @param game The main {@link Game} object.
     */
    public RefLinks(Game game)
    {
        this.game = game;
    }


    /**
     * @brief Sets the data manager (proxy).
     * @param dataProxy The {@link DataManager} instance to use.
     */
    public void setDataProxy(DataManager dataProxy) { this.dataProxy = dataProxy; }
    /**
     * @brief Gets the data manager (proxy).
     * @return The {@link DataManager} instance.
     */
    public DataManager getDataProxy() { return this.dataProxy; }

    /**
     * @brief Sets the strategy for Strong Skeleton enemies.
     * @param strongSkeletonEnemyStrategy The {@link EnemyStrategy} for Strong Skeletons.
     */
    public void setStrongSkeletonEnemyStrategy(EnemyStrategy strongSkeletonEnemyStrategy){
        this.strongSkeletonEnemyStategy = strongSkeletonEnemyStrategy;
    }
    /**
     * @brief Gets the strategy for Strong Skeleton enemies.
     * @return The {@link EnemyStrategy} for Strong Skeletons.
     */
    public EnemyStrategy getStrongSkeletonEnemyStrategy(){return this.strongSkeletonEnemyStategy;}

    /**
     * @brief Sets the strategy for Ghost enemies.
     * @param ghostEnemyStrategy The {@link EnemyStrategy} for Ghosts.
     */
    public void setGhostEnemyStrategy(EnemyStrategy ghostEnemyStrategy){
        this.ghostEnemyStrategy = ghostEnemyStrategy;
    }
    /**
     * @brief Gets the strategy for Ghost enemies.
     * @return The {@link EnemyStrategy} for Ghosts.
     */
    public EnemyStrategy getGhostEnemyStrategy(){
        return this.ghostEnemyStrategy;
    }

    /**
     * @brief Sets the strategy for Minotaur enemies.
     * @param minotaurEnemyStrategy The {@link EnemyStrategy} for Minotaurs.
     */
    public void setMinotaurEnemyStrategy(EnemyStrategy minotaurEnemyStrategy){
        this.minotaurEnemyStrategy = minotaurEnemyStrategy;
    }
    /**
     * @brief Gets the strategy for Minotaur enemies.
     * @return The {@link EnemyStrategy} for Minotaurs.
     */
    public EnemyStrategy getMinotaurEnemyStrategy(){
        return this.minotaurEnemyStrategy;
    }

    /**
     * @brief Sets the strategy for Tiger enemies.
     * @param tigerEnemyStrategy The {@link EnemyStrategy} for Tigers.
     */
    public void setTigerEnemyStrategy(EnemyStrategy tigerEnemyStrategy){
        this.tigerEnemyStrategy = tigerEnemyStrategy;
    }
    /**
     * @brief Gets the strategy for Tiger enemies.
     * @return The {@link EnemyStrategy} for Tigers.
     */
    public EnemyStrategy getTigerEnemyStrategy(){
        return this.tigerEnemyStrategy;
    }

    /**
     * @brief Sets the strategy for Basic Skeleton enemies.
     * @param basicSkeletonStrategy The {@link EnemyStrategy} for Basic Skeletons.
     */
    public void setBasicSkeletonStrategy(EnemyStrategy basicSkeletonStrategy){
        this.basicSkeletonEnemyStrategy = basicSkeletonStrategy;
    }
    /**
     * @brief Gets the strategy for Basic Skeleton enemies.
     * @return The {@link EnemyStrategy} for Basic Skeletons.
     */
    public EnemyStrategy getBasicSkeletonEnemyStrategy(){
        return this.basicSkeletonEnemyStrategy;
    }


    /**
     * @brief Sets the strategy for Wizard enemies.
     * @param wizardStrategy The {@link EnemyStrategy} for Wizards.
     */
    public void setWizardEnemyStrategy(EnemyStrategy wizardStrategy){
        this.wizardEnemyStrategy = wizardStrategy;
    }
    /**
     * @brief Gets the strategy for Wizard enemies.
     * @return The {@link EnemyStrategy} for Wizards.
     */
    public EnemyStrategy getWizardEnemyStrategy(){
        return this.wizardEnemyStrategy;
    }


    /**
     * @brief Gets the keyboard input manager from the game.
     * @return The {@link KeyManager} instance.
     */
    public KeyManager getKeyManager()
    {
        return game.getKeyManager();
    }

    /**
     * @brief Gets the width of the game window from the game.
     * @return The width of the window in pixels.
     */
    public int getWidth()
    {
        return game.getWidth();
    }

    /**
     * @brief Gets the height of the game window from the game.
     * @return The height of the window in pixels.
     */
    public int getHeight()
    {
        return game.getHeight();
    }

    /**
     * @brief Gets the main Game object.
     * @return The {@link Game} instance.
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * @brief Sets the main Game object.
     * @param game The {@link Game} instance to set.
     */
    public void setGame(Game game)
    {
        this.game = game;
    }

    /**
     * @brief Sets the keyboard input manager.
     * @param keyManager The {@link KeyManager} instance.
     */
    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }
    /**
     * @brief Gets the mouse input manager.
     * @return The {@link MouseInput} instance.
     */
    public MouseInput getMouseInput() {
        return mouseInput;
    }

    /**
     * @brief Sets the mouse input manager.
     * @param mouseInput The {@link MouseInput} instance.
     */
    public void setMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }

    /**
     * @brief Sets the player's Hero object.
     * @param hero The {@link Hero} instance.
     */
    public void setHero(Hero hero){
        this.hero=hero;
    }
    /**
     * @brief Gets the player's Hero object.
     * @return The {@link Hero} instance.
     */
    public Hero getHero(){
        return this.hero;
    }


    /**
     * @brief Sets the TileCache object.
     * @param tileCache The {@link TileCache} instance.
     */
    public void setTileCache(TileCache tileCache){
        this.tileCache=tileCache;
    }
    /**
     * @brief Gets the TileCache object.
     * @return The {@link TileCache} instance.
     */
    public TileCache getTileCache(){
        return this.tileCache;
    }


}