package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Config.Constants;

public class Main
{
    public static void main(String[] args)
    {
        Game paooGame = new Game("PaooGame", Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        paooGame.StartGame();
    }
}
