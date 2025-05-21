package PaooGame.Items;

import PaooGame.Animations.ItemsAnimations.FloatingItemAnimation;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;

import java.awt.*;

/**
 * @class FloppyItem
 * @brief Represents a "floppy disk" item in the game, used for saving progress.
 *
 * This class extends the abstract {@link Item} class. It features a floating animation
 * where the item moves up and down. It initializes its properties such as name,
 * sprite sheet path, hitbox, and a specific {@link FloatingItemAnimation} using constants
 * from {@link PaooGame.Config.Constants}.
 */
public class FloppyItem extends Item{
    private boolean floppyFloatingUp = true; ///< Flag indicating the current direction of the floating animation (true for up, false for down).
    protected int floatingTick;              ///< Counter used to time the floating movement steps.
    protected int floatingTickCap;           ///< The threshold for {@link #floatingTick} before a movement step occurs.

    private int initialY;                    ///< The initial y-coordinate where the item was placed, used as a reference for the floating motion.



    /**
     * @brief Constructs a FloppyItem object.
     *
     * Initializes the floppy disk item at a specified position. It sets the item's name,
     * sprite sheet path, creates its hitbox, and loads its floating animation.
     * The item will float vertically around its initial y-position.
     * @param reflink A {@link RefLinks} object providing access to game-wide resources and utilities.
     * @param x The initial x-coordinate of the floppy item in the game world.
     * @param y The initial y-coordinate of the floppy item in the game world, which also serves as the center of its floating motion.
     */
    public FloppyItem(RefLinks reflink, int x, int y){
        super();
        this.initialY = y;
        this.floatingTick = 0;
        this.floatingTickCap = 5; // Determines how many game ticks pass before the item moves one pixel vertically.
        this.x = x;
        this.y = y;
        this.itemName = Constants.SAVE_ITEM_NAME;
        this.itemSheetPath = Constants.SAVE_ITEM_PATH;
        this.hitbox = new Hitbox(this.x,this.y,Constants.SAVE_ITEM_TILE_SIZE,Constants.SAVE_ITEM_TILE_SIZE);
        this.animation = new FloatingItemAnimation(reflink,this.itemSheetPath,18,5,Constants.SAVE_ITEM_TILE_SIZE,Constants.SAVE_ITEM_TILE_SIZE);
        this.animation.loadAnimation();
    }

    /**
     * @brief Updates the state of the floppy item.
     *
     * This method primarily updates the item's animation frames. The floating
     * movement logic is handled within the {@link #drawItem(Graphics)} method in this implementation.
     */
    @Override
    public void updateItem(){
        this.animation.updateAnimation();
    }

    /**
     * @brief Draws the floppy item on the screen and executes its floating movement.
     *
     * This method uses the item's {@link FloatingItemAnimation} to render its current frame
     * at the item's current position. It also calls {@link #executeFloppyFloating()}
     * to update the item's y-coordinate for the floating effect.
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void drawItem(Graphics g){
        this.animation.paintAnimation(g,this.x,this.y,false,1);
        executeFloppyFloating();
    }

    /**
     * @brief Manages the vertical floating movement of the floppy item.
     *
     * The item moves up and down by a small amount (4 pixels from {@link #initialY})
     * periodically, controlled by {@link #floatingTick} and {@link #floatingTickCap}.
     * The direction of movement is toggled by {@link #floppyFloatingUp}.
     */
    private void executeFloppyFloating(){
        if(this.floppyFloatingUp){
            this.floatingTick++;
            if(this.floatingTick>=this.floatingTickCap){
                this.y+=1; // Move down when floppyFloatingUp is true
                this.floatingTick = 0;
            }
            if(this.y>=this.initialY+4){ // Reached bottom of float range
                this.floppyFloatingUp = false; // Change direction to up
            }
        }
        else{ // Moving up
            this.floatingTick++;
            if(this.floatingTick>=this.floatingTickCap){
                this.y-=1; // Move up
                this.floatingTick = 0;
            }
            if(this.y<=this.initialY-4){ // Reached top of float range
                this.floppyFloatingUp = true; // Change direction to down
            }
        }
    }


}