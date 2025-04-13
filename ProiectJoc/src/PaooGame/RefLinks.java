package PaooGame;

import PaooGame.Input.MouseInput;

import PaooGame.Input.KeyManager;
import PaooGame.Tiles.TileCache;

import java.awt.*;

public class RefLinks
{
    private Game game;
    private KeyManager keyManager;
    private MouseInput mouseInput;

    private TileCache tileCache;

    public RefLinks(Game game)
    {
        this.game = game;
        this.tileCache=new TileCache();
    }

    public KeyManager GetKeyManager()
    {
        return game.GetKeyManager();
    }

    public int GetWidth()
    {
        return game.GetWidth();
    }

    public int GetHeight()
    {
        return game.GetHeight();
    }

    public Game GetGame()
    {
        return game;
    }

    public void SetGame(Game game)
    {
        this.game = game;
    }

    public void SetKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }
    public MouseInput GetMouseInput() {
        return mouseInput;
    }

    public void SetMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }


    public void setTileCache(TileCache tileCache){
        this.tileCache=tileCache;
    }
    public TileCache getTileCache(){
        return this.tileCache;
    }
}
