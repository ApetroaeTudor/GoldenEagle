package PaooGame;

import PaooGame.Config.Constants;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Entities.Hero;
import PaooGame.Input.KeyManager;
import PaooGame.Input.MouseInput;
import PaooGame.Maps.Level1;
import PaooGame.Maps.Level2;
import PaooGame.States.*;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileCache;
import PaooGame.Maps.Level3;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {
    private GameWindow wnd;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;

    private Graphics g;
    private State menuState;
    private State settingsState;

    private State level1State;
    private Level1 level1;
    private State level2State;
    private Level2 level2;
    private State level3State;
    private Level3 level3;

    private State pauseMenuState;
    private State aboutState;
    private State fightState;
    private State deathState;
    private KeyManager keyManager;
    private RefLinks refLink;
    private MouseInput mouseInput;

    private TileCache tileCache;
    private Hero hero;

    private Tile tile;

    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

    private void InitGame() {


        tileCache = TileCache.getInstance();


        wnd.BuildGameWindow();

        mouseInput = new MouseInput();
        this.keyManager = new KeyManager();
        wnd.GetCanvas().addKeyListener(keyManager);
        wnd.GetCanvas().addMouseListener(mouseInput);
        wnd.GetCanvas().addMouseMotionListener(mouseInput);




        refLink = new RefLinks(this);
        refLink.setKeyManager(keyManager);
        refLink.setMouseInput(mouseInput);
        refLink.setTileCache(tileCache);

//        hero = new Hero(refLink,Constants.HERO_LEVEL1_STARTING_X,Constants.HERO_LEVEL1_STARTING_Y);
        hero = new Hero(refLink,Constants.HERO_LEVEL2_STARTING_X,Constants.HERO_LEVEL2_STARTING_Y);

        //hero = new Hero(refLink, 1770, 252); //100 - 420
//        hero = new Hero(refLink,284,1300);
        this.refLink.setHero(hero);

        level1 = new Level1();
        level2 = new Level2();
        level3 = new Level3();

        menuState = new MenuState(refLink);
        settingsState = new SettingsState(refLink);
        aboutState = new AboutState(refLink);
        fightState = new FightState(refLink);
        level1State = new Level1State(refLink,level1);
        level2State = new Level2State(refLink,level2);
        level3State = new Level3State(refLink,level3);
        deathState = new DeathState(refLink);
        pauseMenuState = new PauseMenuState(refLink);
        State.setState(menuState);
    }


    public void run() {
        InitGame();
        long oldTime = System.nanoTime();
        long curentTime;


        final int framesPerSecond = 60;
        final double timeFrame = 1000000000 / framesPerSecond;

        while (runState == true) {
            curentTime = System.nanoTime();
            if ((curentTime - oldTime) > timeFrame) {
                Update();
                Draw();
                oldTime = curentTime;
            }
        }

    }

    public synchronized void StartGame() {
        if (runState == false) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        } else {
            return;
        }
    }

    public synchronized void StopGame() {
        if (runState == true) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            return;
        }
    }

    private void Update() {
        if (State.getState() != null) {
            State.getState().update();
        }
    }

    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();
        if (bs == null) {
            try {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        if (State.getState() != null) {
            State.getState().draw(g);
        }
        bs.show();
        g.dispose();
    }

    public int getWidth() {
        return wnd.GetWndWidth();
    }

    public int getHeight() {
        return wnd.GetWndHeight();
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public State getLevel1State() {
        return level1State;
    }
    public State getLevel2State() {
        return level2State;
    }
    public State getLevel3State() {return level3State;}

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public State getPlayState() {
        return level1State;
    }

    public State getFightState(){ return fightState; }


    public State getPauseMenuState() {
        return pauseMenuState;
    }

    public State getMenuState() {
        return menuState;
    }

    public State getDeathState() {return deathState;}

    public Level1 getLevel1(){ return this.level1; }
    public Level2 getLevel2() {return this.level2; }
    public Level3 getLevel3() {return this.level3;}

    public void resetLevel1State() {
        level1State.restoreState();
    }
    public void resetLevel2State(){
        level2State.restoreState();
    }
    public void resetLevel3State() { level3State.restoreState(); }


}