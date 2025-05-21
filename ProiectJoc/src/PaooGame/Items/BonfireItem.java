package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class SaveItem
 * @brief Represents a "bonfire" or save point in the game.
 *
 * This class extends the abstract {@link Item} class and defines a save item,
 * visually represented as a bonfire. It uses a static animation
 * Properties like name, sprite sheet path, and dimensions are derived from
 * {@link PaooGame.Config.Constants}.
 */
public class BonfireItem extends Item{

    /**
     * @brief Constructs a SaveItem object.
     *
     * Initializes the bonfire at a
     * specified position. It sets the item's name,
     * sprite sheet path, creates its hitbox, and loads its animation.
     * The properties are derived from {@link PaooGame.Config.Constants}.
     * @param reflink A {@link RefLinks} object providing access to game-wide resources and utilities.
     * @param x The initial x-coordinate of the save item in the game world.
     * @param y The initial y-coordinate of the save item in the game world.
     */
    public BonfireItem(RefLinks reflink, int x, int y){
        super();
        this.x = x;
        this.y = y;
        this.itemName = Constants.BONFIRE_NAME;
        this.itemSheetPath = Constants.BONFIRE_SHEET_PATH;

        this.hitbox = new Hitbox(this.x,this.y,Constants.BONFIRE_TILE_SIZE,Constants.BONFIRE_TILE_SIZE);
        this.animation = new StaticItemAnimation(reflink,this.itemSheetPath,4,5,Constants.BONFIRE_TILE_SIZE,Constants.BONFIRE_TILE_SIZE);
        this.animation.loadAnimation();
    }

    /**
     * @brief Draws the bonfire on the screen.
     *
     * This method uses the item's animation (managed by {@link StaticItemAnimation})
     * to render its current frame at the item's position.
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void drawItem(Graphics g) {
        this.animation.paintAnimation(g,this.x,this.y,false,1);
    }

    /**
     * @brief Updates the state of the save item.
     */
    @Override
    public void updateItem() {
        this.animation.updateAnimation();
    }


}