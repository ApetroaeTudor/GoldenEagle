package PaooGame;

import PaooGame.Config.Constants;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Input.KeyManager;
import PaooGame.Input.MouseInput;
import PaooGame.Maps.Level1;
import PaooGame.States.*;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileCache;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable
{
    private GameWindow wnd;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;

    private Graphics g;

    private PlayState playState;
    private State menuState;
    private State settingsState;
    private Level1State level1State;
    private State aboutState;
    private KeyManager keyManager;
    private RefLinks refLink;
    private MouseInput mouseInput;

    private TileCache tileCache;
    private Level1 level1;

    private Tile tile;

    public Game(String title, int width, int height)
    {
        wnd = new GameWindow(title, width, height);
        runState = false;
        keyManager = new KeyManager();
    }

    private void InitGame()
    {
        tileCache=new TileCache();
        level1=new Level1();


        wnd.BuildGameWindow();

        mouseInput = new MouseInput();
        keyManager = new KeyManager();
        wnd.GetCanvas().addKeyListener(keyManager);
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocusInWindow();
        wnd.GetCanvas().addMouseListener(mouseInput);
        wnd.GetCanvas().addMouseMotionListener(mouseInput);
        Assets.Init();


        refLink = new RefLinks(this);
        refLink.SetKeyManager(keyManager);
        refLink.SetMouseInput(mouseInput);
        refLink.setTileCache(tileCache);

        playState       = new PlayState(refLink);
        menuState       = new MenuState(refLink);
        settingsState   = new SettingsState(refLink);
        aboutState      = new AboutState(refLink);
        level1State     = new Level1State(refLink);
        State.SetState(menuState);
    }

    public void run()
    {
        InitGame();
        long oldTime = System.nanoTime();
        long curentTime;


        final int framesPerSecond   = 60;
        final double timeFrame      = 1000000000 / framesPerSecond;

        while (runState == true)
        {
            curentTime = System.nanoTime();
            if((curentTime - oldTime) > timeFrame)
            {
                Update();
                Draw();
                oldTime = curentTime;
            }
        }

    }

    public synchronized void StartGame()
    {
        if(runState == false)
        {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
        else
        {
            return;
        }
    }

    public synchronized void StopGame()
    {
        if(runState == true)
        {
            runState = false;
            try
            {
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            return;
        }
    }

    private void Update()
    {
        keyManager.Update();
        if(State.GetState() != null)
        {
            State.GetState().Update();
        }
    }

    private void Draw()
    {
        bs = wnd.GetCanvas().getBufferStrategy();
        if(bs == null)
        {
            try
            {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        if(State.GetState() != null)
        {
            State.GetState().Draw(g);
        }

        bs.show();

        g.dispose();
    }

    public int GetWidth()
    {
        return wnd.GetWndWidth();
    }

    public int GetHeight()
    {
        return wnd.GetWndHeight();
    }

    public KeyManager GetKeyManager()
    {
        return keyManager;
    }

    public Level1State GetLevel1State(){
        return level1State;
    }
    public PlayState GetPlayState() {
        return playState;
    }
    public MouseInput GetMouseInput() {
        return mouseInput;
    }

}

