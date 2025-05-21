package PaooGame.Maps;

import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @class Level
 * @brief Abstract base class for game levels, managing tile data and providing collision detection utilities.
 *
 * This class handles the storage of visual and behavioral tile data for a game level.
 * It loads this data from CSV files. Additionally, it provides a suite of static utility methods
 * for performing tile-based collision checks (falling, wall, ceiling, ground properties) against
 * a given {@link Hitbox}.
 *
 * Tile behaviors are typically encoded as integers:
 * - 0: Lethal (e.g., water, lava) - Causes death or falling.
 * - 1: Lethal / Fall-through (e.g., pit) - Causes death or falling.
 * - 2: Solid (e.g., ground, wall) - Blocks movement.
 * - Other values: Air or custom behaviors.
 */
public abstract class Level {
    int[] visualIDs;                    ///< 1D array storing visual tile IDs, typically loaded from a CSV file.
    int[] behaviorIDs;                  ///< 1D array storing behavior tile IDs, loaded from a CSV file, used by static collision methods.

    /**
     * @brief Gets the 1D array of visual tile IDs.
     * @return The array of visual tile IDs.
     */
    public int[] getVisualIDs(){return this.visualIDs;}

    /**
     * @brief Gets the 1D array of behavior tile IDs.
     * @return The array of behavior tile IDs.
     */
    public int[] getBehaviorIDs(){return this.behaviorIDs;}



    /**
     * @brief Loads visual and behavior tile IDs from specified CSV files.
     *
     * This method reads integer IDs from comma-separated value files and populates
     * the {@link #visualIDs} and {@link #behaviorIDs} arrays.
     * @param TexturesCsv The file path to the CSV file containing visual tile IDs.
     * @param BehaviorCsv The file path to the CSV file containing behavior tile IDs.
     */
    protected void setIDs(String TexturesCsv, String BehaviorCsv ){
        String line;

        try{
            int i = 0;
            BufferedReader br = new BufferedReader(new FileReader(TexturesCsv));
            BufferedReader br2 = new BufferedReader(new FileReader(BehaviorCsv));

            while((line = br.readLine())!=null){
                String [] tokens;
                tokens = line.split(",");
                for(String token: tokens){
                    visualIDs[i++]=Integer.parseInt(token);
                }
            }
            i = 0;
            while( (line = br2.readLine())!=null ){
                String [] tokens;
                tokens = line.split(",");
                for(String token : tokens){
                    behaviorIDs[i++] = Integer.parseInt(token);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found in loading tiles in level.\n");
        } catch (IOException e) {
            System.out.println("IOException In loading tiles in level.\n");
        } catch (NumberFormatException e){
            System.out.println("NumberFormatException in loading tiles in level.\n");
        }
    }


    /**
     * @brief Checks if an entity with the given hitbox is falling or standing on solid ground.
     *
     * It examines the tiles directly beneath the hitbox.
     * @param hitbox The {@link Hitbox} of the entity to check.
     * @param LEVEL_WIDTH The width of the level in number of tiles.
     * @param LEVEL_HEIGHT The height of the level in number of tiles.
     * @param behaviorIDs A 1D array of tile behavior IDs for the level.
     * @return
     *         -  `1`: Entity is over a lethal or fall-through tile (behavior ID 0 or 1).
     *         -  `0`: Entity is over a solid tile (behavior ID 2), meaning it's standing.
     *         - `-1`: Entity is over air, off the map vertically, or an error occurred (e.g., invalid index).
     */
    public static int checkFalling(Hitbox hitbox,int LEVEL_WIDTH,int LEVEL_HEIGHT, int[] behaviorIDs) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();
        float hitboxHeight = hitbox.getHeight();

        int startX = (int) Math.floor(hitboxX / Constants.TILE_SIZE); // positions in tile-coordinates for the hitbox on the X axis
        int endX = (int) Math.floor((hitboxX + hitboxWidth - Constants.EPSILON) / Constants.TILE_SIZE);

        float checkY = hitboxY + hitboxHeight; // The coordinate defining the top of the tile row below
        int tileRowToCheck = (int) Math.floor(checkY / Constants.TILE_SIZE); // the tiles under the characters are the ones that must be checked

        if (tileRowToCheck < 0 || tileRowToCheck >= LEVEL_HEIGHT) {
            return -1; // Off map vertically (below or above), considered falling.
        }

        for (int tileX = startX; tileX <= endX; tileX++) { // check each collumn corresponding to the selected tile row
            if (tileX < 0 || tileX >= LEVEL_WIDTH) {
                continue; // Skip columns outside level bounds
            }

            int index = tileRowToCheck * LEVEL_WIDTH + tileX; // find the corresponding tile index, which will be used in the behavior IDs array

            if (index < 0 || index >= behaviorIDs.length) {
                System.err.println("Error: Calculated map index out of bounds in checkFalling: " + index);
                continue; // Treat invalid index as air and continue
            }

            int behavior = behaviorIDs[index];

            //death or fall-through
            if(behavior == 1 || behavior == 0){
                return 1; // Lethal or fall-through tile
            }
            //solid ground
            else if(behavior == 2 ){
                return 0; // Solid ground
            }
        }
        // All tiles checked are air or non-solid/non-lethal in the specific context of this check
        return -1; // Effectively falling or over non-solid tile
    }


    /**
     * @brief Adjusts the hitbox's y-coordinate so that its bottom edge aligns perfectly with the top surface of the tile row it's over.
     *
     * This is useful for ensuring an entity rests exactly on the ground after landing.
     * @param hitbox The {@link Hitbox} to adjust.
     */
    public static void snapToGround(Hitbox hitbox) {
        float bottomY = hitbox.getY() + hitbox.getHeight();
        float groundSurfaceY = (int) Math.floor(bottomY / Constants.TILE_SIZE) * Constants.TILE_SIZE; // used mostly because float coordinates don't always match with tile sizes
        hitbox.setY((int) (groundSurfaceY - hitbox.getHeight()));
    }


    /**
     * @brief Checks for collision with a solid wall tile (behavior ID 2) to the left or right of the hitbox.
     *
     * The check is performed along a vertical segment of the hitbox, slightly shrunk from the top and bottom.
     * @param hitbox The {@link Hitbox} of the entity.
     * @param checkRight True to check for collision on the right side, false to check on the left.
     * @param LEVEL_WIDTH The width of the level in number of tiles.
     * @param LEVEL_HEIGHT The height of the level in number of tiles.
     * @param behaviorIDs A 1D array of tile behavior IDs for the level.
     * @return True if a solid wall tile is detected in the checked direction, false otherwise.
     *         Also returns true if hitting the world boundary in the checked direction.
     */
    public static boolean checkWallCollision(Hitbox hitbox, boolean checkRight, int LEVEL_WIDTH,int LEVEL_HEIGHT, int[] behaviorIDs) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();
        float hitboxHeight = hitbox.getHeight();

        float shrinkTop = 0.0f;    // Pixels to ignore from the top
        float shrinkBottom = 2.0f; // Pixels to ignore from the bottom

        float checkStartY = hitboxY + shrinkTop;
        float checkEndY = hitboxY + hitboxHeight - shrinkBottom - Constants.EPSILON;
        // these 2 define the y range that should be checked

        if (checkEndY < checkStartY) { // Invalid vertical check range
            return false;
        }

        int startTileY = (int) Math.floor(checkStartY / Constants.TILE_SIZE); // the values calculated above are used in order to fin corresponding tile rows
        int endTileY = (int) Math.floor(checkEndY / Constants.TILE_SIZE);

        float checkXCoord;
        if (checkRight) {
            checkXCoord = hitboxX + hitboxWidth - Constants.EPSILON; // Check just inside the right edge
        } else { // Checking Left
            checkXCoord = hitboxX + Constants.EPSILON; // Check just inside the left edge
        }

        int checkTileX = (int) Math.floor(checkXCoord / Constants.TILE_SIZE); // find the corresponding tile column

        for (int tileY = startTileY; tileY <= endTileY; tileY++) { // check each row
            // Check for hitting world boundaries first if checkTileX is outside map
            if (checkTileX < 0 && !checkRight) return true; // Hit left world boundary
            if (checkTileX >= LEVEL_WIDTH && checkRight) return true; // Hit right world boundary


            // If horizontally within map but vertically out, or general out of bounds for the tile index
            if (checkTileX < 0 || checkTileX >= LEVEL_WIDTH || tileY < 0 || tileY >= LEVEL_HEIGHT) {
                continue;
            }

            int index = tileY * LEVEL_WIDTH + checkTileX; // calculate the exact index of the tile to verify

            if (index < 0 || index >= behaviorIDs.length) {
                continue;
            }

            int behavior = behaviorIDs[index];
            if (behavior == 2) { // Behavior 2 is solid wall
                return true;
            }
        }
        return false;
    }


    /**
     * @brief Checks for collision with a solid ceiling tile (behavior ID 2) directly above the hitbox.
     * @param hitbox The {@link Hitbox} of the entity.
     * @param LEVEL_WIDTH The width of the level in number of tiles.
     * @param LEVEL_HEIGHT The height of the level in number of tiles.
     * @param behaviorIDs A 1D array of tile behavior IDs for the level.
     * @return True if a solid ceiling tile is detected above the hitbox, false otherwise.
     */
    public static boolean checkCeilingCollision(Hitbox hitbox, int LEVEL_WIDTH, int LEVEL_HEIGHT, int[] behaviorIDs) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();

        int startTileX = (int) Math.floor(hitboxX / Constants.TILE_SIZE);
        int endTileX = (int) Math.floor((hitboxX + hitboxWidth - Constants.EPSILON) / Constants.TILE_SIZE);
        float checkY = hitboxY - Constants.EPSILON; // Check just above the hitbox's top

        int tileRowToCheck = (int) Math.floor(checkY / Constants.TILE_SIZE);

        if (tileRowToCheck < 0) { // Above the map
            return false; // Or true if top boundary is solid, depends on game rules
        }
        if (tileRowToCheck >= LEVEL_HEIGHT) { // This case should ideally not happen if checkY is above hitbox
            return false;
        }

        for (int tileX = startTileX; tileX <= endTileX; tileX++) { // check each column
            if (tileX < 0 || tileX >= LEVEL_WIDTH) {
                continue;
            }

            int index = tileRowToCheck * LEVEL_WIDTH + tileX;

            if (index < 0 || index >= behaviorIDs.length) {
                System.err.println("Warning: checkCeilingCollision calculated invalid map index: " + index + " for tile (" + tileX + ", " + tileRowToCheck + ")");
                continue;
            }

            int behavior = behaviorIDs[index];
            if (behavior == 2) { // Behavior 2 is solid
                return true;
            }
        }
        return false;
    }

    /**
     * @brief Checks if a specific tile in the level is solid (behavior ID 2).
     * @param tileX The x-coordinate of the tile in tile units.
     * @param tileY The y-coordinate of the tile in tile units.
     * @param LEVEL_WIDTH_IN_NR_TILES The width of the level in number of tiles.
     * @param LEVEL_HEIGHT_IN_NR_TILES The height of the level in number of tiles.
     * @param behaviorIDs A 1D array of tile behavior IDs for the level.
     * @return True if the tile at (tileX, tileY) is solid, false if it's out of bounds or not solid.
     */
    public static boolean isTileSolid(int tileX, int tileY, int LEVEL_WIDTH_IN_NR_TILES, int LEVEL_HEIGHT_IN_NR_TILES,int[] behaviorIDs){
        if (tileX < 0 || tileX >= LEVEL_WIDTH_IN_NR_TILES || tileY < 0 || tileY >= LEVEL_HEIGHT_IN_NR_TILES) {
            return false; // Out of bounds is not solid
        }

        int index = tileY * LEVEL_WIDTH_IN_NR_TILES + tileX;
        if (index < 0 || index >= behaviorIDs.length) { // Additional safety check for index
            return false;
        }
        int behavior = behaviorIDs[index];
        return behavior == 2; // Behavior 2 is solid
    }

    /**
     * @brief Gets the behavior ID of a specific tile in the level.
     * @param tileX The x-coordinate of the tile in tile units.
     * @param tileY The y-coordinate of the tile in tile units.
     * @param LEVEL_WIDTH_IN_NR_TILES The width of the level in number of tiles.
     * @param LEVEL_HEIGHT_IN_NR_TILES The height of the level in number of tiles.
     * @param behaviorIDs A 1D array of tile behavior IDs for the level.
     * @return The behavior ID of the tile at (tileX, tileY), or -2 if the coordinates are out of bounds or index is invalid.
     */
    public static int getTileBehavior(int tileX, int tileY, int LEVEL_WIDTH_IN_NR_TILES, int LEVEL_HEIGHT_IN_NR_TILES, int[] behaviorIDs){
        if (tileX < 0 || tileX >= LEVEL_WIDTH_IN_NR_TILES || tileY < 0 || tileY >= LEVEL_HEIGHT_IN_NR_TILES) {
            return -2; // Special value indicating out of bounds
        }

        int index = tileY * LEVEL_WIDTH_IN_NR_TILES + tileX;
        if (index < 0 || index >= behaviorIDs.length) { // Additional safety check for index
            return -2; // Special value indicating invalid index
        }
        int behavior = behaviorIDs[index];
        return behavior;
    }


    /**
     * @brief Checks if there is solid ground (behavior ID 2) just ahead and below an entity's leading edge.
     *
     * This is useful for enemies to detect edges or cliffs.
     * @param hitbox The {@link Hitbox} of the entity.
     * @param headingLeft True if the entity is heading left, false if heading right.
     * @param LEVEL_WIDTH The width of the level in number of tiles.
     * @param LEVEL_HEIGHT The height of the level in number of tiles.
     * @param behaviorIDs A 1D array of tile behavior IDs for the level.
     * @return True if solid ground is detected in the specified direction just below the entity's leading edge, false otherwise.
     */
    public static boolean isGroundAhead(Hitbox hitbox, boolean headingLeft,int LEVEL_WIDTH,int LEVEL_HEIGHT, int[] behaviorIDs) {
        int checkTileX;
        if (headingLeft) {
            // Check tile just to the left of the bottom-left corner
            checkTileX = (int) Math.floor((hitbox.getX() - Constants.EPSILON) / Constants.TILE_SIZE);
        } else {
            // Check tile just to the right of the bottom-right corner
            checkTileX = (int) Math.floor((hitbox.getX() + hitbox.getWidth() + Constants.EPSILON) / Constants.TILE_SIZE);
        }

        // Check the tile directly below the leading edge
        int checkTileY = (int) Math.floor((hitbox.getY() + hitbox.getHeight() + Constants.EPSILON) / Constants.TILE_SIZE);

        return isTileSolid(checkTileX, checkTileY,LEVEL_WIDTH,LEVEL_HEIGHT,behaviorIDs);
    }
}