package PaooGame;

import PaooGame.Config.Constants;


/**
 * @class Main
 * @brief The main entry point for the PaooGame application.
 *
 * This class contains the {@code main} method which initializes and starts the game.
 */
public class Main
{
    /**
     * @brief The main method that serves as the application's entry point.
     *
     * It creates an instance of the {@link Game} class with a specific title and dimensions
     * defined in {@link PaooGame.Config.Constants}, and then starts the game loop
     * by calling the {@link Game#StartGame()} method.
     *
     */
    public static void main(String[] args)
    {

        int a[] = new int[10];
        a[1]= 10;
        System.out.println(a[1]);

        // Create a new Game instance with the title "GoldenEagle" and dimensions from Constants
        Game paooGame = new Game("GoldenEagle", Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        // Start the game
        paooGame.StartGame();
    }
}