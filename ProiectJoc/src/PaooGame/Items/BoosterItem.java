package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.StaticItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class BoosterItem
 * @brief Represents a specific type of item in the game that acts as a "booster".
 *
 * This class extends the abstract {@link Item} class and defines a booster item.
 * It initializes its properties such as name, sprite sheet path, hitbox, and a
 * static animation, using constants defined in {@link PaooGame.Config.Constants}.
 */
public class BoosterItem extends Item {

    /**
     * @brief Constructs a BoosterItem object.
     *
     * Initializes the booster item at a specified position. It sets the item's name,
     * sprite sheet path, creates its hitbox, and loads its static animation.
     * The properties are derived from {@link PaooGame.Config.Constants}.
     * @param reflink A {@link RefLinks} object providing access to game-wide resources and utilities.
     * @param x The initial x-coordinate of the booster item in the game world.
     * @param y The initial y-coordinate of the booster item in the game world.
     */
    public BoosterItem(RefLinks reflink, int x, int y){
        super();
        this.x =x;
        this.y =y;
        this.itemName = Constants.BOOSTER_ITEM_NAME;
        this.itemSheetPath = Constants.BOOSTER_PATH;
        this.hitbox = new Hitbox(this.x, this.y,Constants.BOOSTER_IMG_WIDTH,Constants.BOOSTER_IMG_HEIGHT);
        this.animation = new StaticItemAnimation(reflink,this.itemSheetPath,1,1,Constants.BOOSTER_IMG_WIDTH,Constants.BOOSTER_IMG_HEIGHT);
        this.animation.loadAnimation();
    }

    /**
     * @brief Draws the booster item on the screen.
     *
     * This method uses the item's {@link StaticItemAnimation} to render its current frame
     * at the item's position.
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void drawItem(Graphics g) {
        this.animation.paintAnimation(g,this.x,this.y,false,1);
    }

    /**
     * @brief Updates the state of the booster item.
     */
    @Override
    public void updateItem() {
        this.animation.updateAnimation();
    }
}