package PaooGame.Items;

import PaooGame.Animations.Animation;
import PaooGame.Hitbox.Hitbox;

import java.awt.*;

/**
 * @class Item
 * @brief Represents an abstract base class for all items in the game.
 *
 * This class defines the common properties and behaviors that all game items
 * should possess, such as position, name, associated sprite sheet path,
 * hitbox for collision detection, and an animation.
 * Concrete item types must extend this class and implement the abstract methods
 * for drawing and updating their specific logic.
 */
public abstract class Item {
    protected int x;                ///< The x-coordinate of the item's position in the game world.
    protected int y;                ///< The y-coordinate of the item's position in the game world.
    protected String itemName;      ///< The name of the item
    protected String itemSheetPath; ///< The file path to the sprite sheet used for the item's animation.
    protected Hitbox hitbox;        ///< The hitbox associated with the item, used for collision detection.
    protected Animation animation;  ///< The animation object responsible for rendering the item's sprites.

    /**
     * @brief Default constructor for the Item class.
     *
     * Initializes a new item. Concrete subclasses are responsible for
     * setting the specific properties like position, name, sprite sheet, hitbox, and animation.
     */
    public Item(){}

    /**
     * @brief Gets the hitbox of the item.
     * @return The {@link Hitbox} object associated with this item.
     */
    public Hitbox getHitbox() {return this.hitbox;}

    /**
     * @brief Gets the name of the item.
     * @return A string representing the item's name.
     */
    public String getItemName() {return this.itemName;}

    /**
     * @brief Gets the file path to the item's sprite sheet.
     * @return A string representing the path to the sprite sheet.
     */
    public String getItemSheetPath() {return this.itemSheetPath;}

    /**
     * @brief Abstract method to draw the item on the screen.
     *
     * Concrete subclasses must implement this method to define how the item
     * is rendered, typically using its animation.
     * @param g The {@link Graphics} context to draw on.
     */
    public abstract void drawItem(Graphics g);

    /**
     * @brief Abstract method to update the item's state.
     *
     * Concrete subclasses must implement this method to define any logic
     * that needs to be executed per game update cycle, such as updating
     * its animation or position.
     */
    public abstract void updateItem();


}