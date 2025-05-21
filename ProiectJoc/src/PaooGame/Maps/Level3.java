package PaooGame.Maps;

import PaooGame.Config.Constants;


/**
 * @class Level3
 * @brief Represents the third level of the game.
 *
 * This class extends the abstract {@link Level} class and specifically defines
 * the properties and data for Level 3. It initializes its tile data (both visual
 * and behavioral) by loading from CSV files specified in {@link PaooGame.Config.Constants}.
 */
public class Level3 extends Level{

    /**
     * @brief Constructs a Level3 object.
     *
     * Initializes the dimensions for the visual tile array based on
     * constants defined for Level 3 in {@link PaooGame.Config.Constants}.
     * It then calls the {@link #setIDs(String, String)} method (inherited from {@link Level})
     * to populate the {@link #visualIDs} and {@link #behaviorIDs} arrays by reading
     * data from the CSV files specified by {@link Constants#LEVEL3_TEXTURES_CSV} and
     * {@link Constants#LEVEL3_BEHAVIOR_CSV}.
     * The `behaviorTiles` 2D array is not initialized in this constructor.
     */
    public Level3(){

        // Initialize 1D arrays that will be populated by setIDs
        this.visualIDs = new int[Constants.LEVEL3_TILE_NR];
        this.behaviorIDs = new int[Constants.LEVEL3_TILE_NR];

        // Load tile IDs from CSV files
        setIDs(Constants.LEVEL3_TEXTURES_CSV,Constants.LEVEL3_BEHAVIOR_CSV);
    }
}