package PaooGame;

import PaooGame.Entities.Hero;
import PaooGame.Input.MouseInput;

import PaooGame.Input.KeyManager;
import PaooGame.Strategies.EnemyStrategies.EnemyStrategy;
import PaooGame.Strategies.EnemyStrategies.TigerEnemyStrategy;
import PaooGame.Tiles.TileCache;

public class RefLinks
{
    private Game game;
    private KeyManager keyManager;
    private MouseInput mouseInput;

    private EnemyStrategy tigerEnemyStrategy;
    private EnemyStrategy basicSkeletonEnemyStrategy;
    private EnemyStrategy wizardEnemyStrategy;
    private EnemyStrategy minotaurEnemyStrategy;
    private EnemyStrategy ghostEnemyStrategy;


    private Hero hero;

    private TileCache tileCache;

    public RefLinks(Game game)
    {
        this.game = game;
//        this.tileCache=new TileCache();
    }


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