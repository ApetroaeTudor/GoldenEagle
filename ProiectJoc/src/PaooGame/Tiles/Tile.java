package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

import PaooGame.Config.Constants;

/**
 * @class Tile
 * @brief Represents a single tile in the game world.
 *
 * This class encapsulates the image and identifier for a game tile.
 * It provides methods for loading tiles from a spritesheet and drawing them
 * onto the screen.
 */
public class Tile
{
    public static final int TILE_WIDTH  = 48;       ///< Default width of a tile in pixels. Note: This might conflict with Constants.TILE_SIZE if they are different.
    public static final int TILE_HEIGHT = 48;       ///< Default height of a tile in pixels. Note: This might conflict with Constants.TILE_SIZE if they are different.

    protected BufferedImage img;                    ///< The image representing this tile.
    protected final int id;                         ///< The unique identifier for this tile type.

    /**
     * @brief Constructs a Tile object.
     * @param image The {@link BufferedImage} for this tile.
     * @param id The unique identifier for this tile type.
     */
    public Tile(BufferedImage image, int id)
    {
        img = image;
        this.id = id;
    }

    /**
     * @brief Updates the tile's state.
     *
     * Currently, this method is empty as static tiles typically do not require
     * per-frame updates. It can be overridden by subclasses for animated tiles.
     */
    public void Update() {
    }

    /**
     * @brief Loads a single tile image from a larger spritesheet.
     *
     * Calculates the row and column of the tile within the spritesheet based on its ID
     * and the assumption that the spritesheet is 32 tiles wide. It then extracts
     * the sub-image corresponding to the tile.
     *
     * @param sheet The {@link BufferedImage} representing the entire spritesheet.
     * @param id The unique identifier of the tile to load.
     * @return A new {@link Tile} object containing the loaded image and ID.
     */
    public static Tile loadTile(BufferedImage sheet, int id){
        // Calculate row and column based on ID, spritesheet width of 32 tiles
        int row = id / 32; // 32 tiles per row in the spritesheet
        int col = id % 32;
        // Extract the tile image from the spritesheet using dimensions from Constants
        BufferedImage tileImage = sheet.getSubimage(col * Constants.TILE_SIZE, row * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

        return new Tile(tileImage, id);
    }

    /**
     * @brief Draws the tile at the specified screen coordinates.
     *
     * The tile is drawn using the dimensions specified in {@link PaooGame.Config.Constants#TILE_SIZE}.
     *
     * @param g The {@link Graphics} context used for drawing.
     * @param x The x-coordinate on the screen where the tile should be drawn.
     * @param y The y-coordinate on the screen where the tile should be drawn.
     */
    public void Draw(Graphics g, int x, int y)
    {
        // Draw the tile image at the given x, y coordinates, scaled to Constants.TILE_SIZE
        g.drawImage(img, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
    }

    /**
     * @brief Gets the unique identifier of this tile.
     * @return The integer ID of the tile.
     */
    public int GetId()
    {
        return id;
    }
}