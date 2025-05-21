package PaooGame.Maps;

import PaooGame.Config.Constants;


/**
 * @class Level1
 * @brief Represents the first level of the game.
 *
 * This class extends the abstract {@link Level} class and specifically defines
 * the properties and data for Level 1. It initializes its tile data (both visual
 * and behavioral) by loading from CSV files specified in {@link PaooGame.Config.Constants}.
 */
public class Level1 extends Level {


    /**
     * @brief Constructs a Level1 object.
     *
     * Initializes the dimensions for the visual and behavioral tile arrays based on
     * constants defined for Level 1 in {@link PaooGame.Config.Constants}.
     * It then calls the {@link #setIDs(String, String)} method (inherited from {@link Level})
     * to populate the {@link #visualIDs} and {@link #behaviorIDs} arrays by reading
     * data from the CSV files specified by {@link Constants#LEVEL1_TEXTURES_CSV} and
     * {@link Constants#LEVEL1_BEHAVIOR_CSV}.
     */
    public Level1(){

        // Initialize 1D arrays that will be populated by setIDs
        this.visualIDs = new int[Constants.LEVEL1_TILE_NR];
        this.behaviorIDs = new int[Constants.LEVEL1_TILE_NR];

        // Load tile IDs from CSV files
        setIDs(Constants.LEVEL1_TEXTURES_CSV,Constants.LEVEL1_BEHAVIOR_CSV);
    }

}