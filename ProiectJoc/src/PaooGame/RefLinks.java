package PaooGame;

import PaooGame.DatabaseManaging.DataManager;
import PaooGame.Entities.Hero;
import PaooGame.Input.MouseInput;

import PaooGame.Input.KeyManager;
import PaooGame.States.State;
import PaooGame.Strategies.EnemyStrategies.EnemyStrategy;
import PaooGame.Strategies.EnemyStrategies.TigerEnemyStrategy;
import PaooGame.Tiles.TileCache;

import javax.xml.crypto.Data;

public class RefLinks
{
    private Game game;
    private KeyManager keyManager;
    private MouseInput mouseInput;

    private int score1=0;
    private int score2=0;
    private int score3=0;

    private State currentRunningLevel;

    private EnemyStrategy tigerEnemyStrategy;
    private EnemyStrategy basicSkeletonEnemyStrategy;
    private EnemyStrategy wizardEnemyStrategy;
    private EnemyStrategy minotaurEnemyStrategy;
    private EnemyStrategy ghostEnemyStrategy;
    private EnemyStrategy strongSkeletonEnemyStategy;
    private DataManager dataProxy;

    private boolean dataRefreshSignal;
    private boolean level1RefreshDoneSignal;
    private boolean level2RefreshDoneSignal;
    private boolean level3RefreshDoneSignal;
    private boolean heroRefreshDoneSignal;


    private boolean dataStoreSignal;
    private boolean level1StoreDoneSignal;
    private boolean level2StoreDoneSignal;
    private boolean level3StoreDoneSignal;
    private boolean heroStoreDoneSignal;

    public int getScore1(){return this.score1;}
    public int getScore2(){return this.score2;}
    public int getScore3(){return this.score3;}

    public void setScore1(int score1){this.score1 = score1;}
    public void setScore2(int score2){this.score2 = score2;}
    public void setScore3(int score3){this.score3 = score3;}

    public void setCurrentRunningLevel(State currentRunningLevel) { this.currentRunningLevel = currentRunningLevel;}
    public State getCurrentRunningLevel() { return this.currentRunningLevel;}

    public void setDataStoreSignal(boolean dataStoreSignal) {
        this.dataStoreSignal = dataStoreSignal;
    }

    public void setLevel1StoreDoneSignal(boolean level1StoreDoneSignal) {
        this.level1StoreDoneSignal = level1StoreDoneSignal;
    }

    public void setLevel2StoreDoneSignal(boolean level2StoreDoneSignal) {
        this.level2StoreDoneSignal = level2StoreDoneSignal;
    }

    public void setLevel3StoreDoneSignal(boolean level3StoreDoneSignal) {
        this.level3StoreDoneSignal = level3StoreDoneSignal;
    }

    public void setHeroStoreDoneSignal(boolean heroStoreDoneSignal) {
        this.heroStoreDoneSignal = heroStoreDoneSignal;
    }



    public boolean getDataStoreSignal() {
        return dataStoreSignal;
    }

    public boolean getLevel1StoreDoneSignal() {
        return level1StoreDoneSignal;
    }

    public boolean getLevel2StoreDoneSignal() {
        return level2StoreDoneSignal;
    }

    public boolean getLevel3StoreDoneSignal() {
        return level3StoreDoneSignal;
    }

    public boolean getHeroStoreDoneSignal() {
        return heroStoreDoneSignal;
    }







    public boolean getHeroRefreshDoneSignal(){
        return heroRefreshDoneSignal;
    }

    public boolean getLevel1RefreshDoneSignal() {
        return level1RefreshDoneSignal;
    }

    public boolean getLevel2RefreshDoneSignal() {
        return level2RefreshDoneSignal;
    }

    public boolean getLevel3RefreshDoneSignal() {
        return level3RefreshDoneSignal;
    }

    public boolean getDataRefreshSignal() {
        return dataRefreshSignal;
    }

    public void setHeroRefreshDoneSignal(boolean heroRefreshDoneSignal){
        this.heroRefreshDoneSignal = heroRefreshDoneSignal;
    }

    public void setLevel3RefreshDoneSignal(boolean level3RefreshDoneSignal) {
        this.level3RefreshDoneSignal = level3RefreshDoneSignal;
    }

    public void setLevel2RefreshDoneSignal(boolean level2RefreshDoneSignal) {
        this.level2RefreshDoneSignal = level2RefreshDoneSignal;
    }

    public void setLevel1RefreshDoneSignal(boolean level1RefreshDoneSignal) {
        this.level1RefreshDoneSignal = level1RefreshDoneSignal;
    }

    public void setDataRefreshSignal(boolean dataRefreshSignal) {
        this.dataRefreshSignal = dataRefreshSignal;
    }






    private Hero hero;

    private TileCache tileCache;

    public RefLinks(Game game)
    {
        this.game = game;
    }


    public void setDataProxy(DataManager dataProxy) { this.dataProxy = dataProxy; }
    public DataManager getDataProxy() { return this.dataProxy; }

    public void setStrongSkeletonEnemyStrategy(EnemyStrategy strongSkeletonEnemyStategy){
        this.strongSkeletonEnemyStategy = strongSkeletonEnemyStategy;
    }
    public EnemyStrategy getStrongSkeletonEnemyStrategy(){return this.strongSkeletonEnemyStategy;}

    public void setGhostEnemyStrategy(EnemyStrategy ghostEnemyStrategy){
        this.ghostEnemyStrategy = ghostEnemyStrategy;
    }
    public EnemyStrategy getGhostEnemyStrategy(){
        return this.ghostEnemyStrategy;
    }

    public void setMinotaurEnemyStrategy(EnemyStrategy minotaurEnemyStrategy){
        this.minotaurEnemyStrategy = minotaurEnemyStrategy;
    }
    public EnemyStrategy getMinotaurEnemyStrategy(){
        return this.minotaurEnemyStrategy;
    }

    public void setTigerEnemyStrategy(EnemyStrategy tigerEnemyStrategy){
        this.tigerEnemyStrategy = tigerEnemyStrategy;
    }
    public EnemyStrategy getTigerEnemyStrategy(){
        return this.tigerEnemyStrategy;
    }

    public void setBasicSkeletonStrategy(EnemyStrategy basicSkeletonStrategy){
        this.basicSkeletonEnemyStrategy = basicSkeletonStrategy;
    }
    public EnemyStrategy getBasicSkeletonEnemyStrategy(){
        return this.basicSkeletonEnemyStrategy;
    }


    public void setWizardEnemyStrategy(EnemyStrategy wizardStrategy){
        this.wizardEnemyStrategy = wizardStrategy;
    }
    public EnemyStrategy getWizardEnemyStrategy(){
        return this.wizardEnemyStrategy;
    }



    public KeyManager getKeyManager()
    {
        return game.getKeyManager();
    }

    public int getWidth()
    {
        return game.getWidth();
    }

    public int getHeight()
    {
        return game.getHeight();
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }
    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public void setMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }

    public void setHero(Hero hero){
        this.hero=hero;
    }
    public Hero getHero(){
        return this.hero;
    }


    public void setTileCache(TileCache tileCache){
        this.tileCache=tileCache;
    }
    public TileCache getTileCache(){
        return this.tileCache;
    }


}