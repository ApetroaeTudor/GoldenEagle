package PaooGame.Maps;

import PaooGame.Config.Constants;

/**
 * @class Level2
 * @brief Represents the second level of the game.
 *
 * This class extends the abstract {@link Level} class and specifically defines
 * the properties and data for Level 2. It initializes its tile data (both visual
 * and behavioral) by loading from CSV files specified in {@link PaooGame.Config.Constants}.
 */
public class Level2 extends Level{

    /**
     * @brief Constructs a Level2 object.
     *
     * Initializes the dimensions for the visual and behavioral tile arrays based on
     * constants defined for Level 2 in {@link PaooGame.Config.Constants}.
     * It then calls the {@link #setIDs(String, String)} method (inherited from {@link Level})
     * to populate the {@link #visualIDs} and {@link #behaviorIDs} arrays by reading
     * data from the CSV files specified by {@link Constants#LEVEL2_TEXTURES_CSV} and
     * {@link Constants#LEVEL2_BEHAVIOR_CSV}.
     */
    public Level2(){

        // Initialize 1D arrays that will be populated by setIDs
        this.visualIDs = new int[Constants.LEVEL2_TILE_NR];
        this.behaviorIDs = new int[Constants.LEVEL2_TILE_NR];

        // Load tile IDs from CSV files
        setIDs(Constants.LEVEL2_TEXTURES_CSV,Constants.LEVEL2_BEHAVIOR_CSV);
    }
}