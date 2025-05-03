package PaooGame.Maps;

import Entities.Entity;
import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;

public abstract class Level {
    protected int [][] VisualTiles;
    protected int [][] BehaviorTiles;
    int[] visualIDs;
    int[] behaviorIDs;


    public int[] getVisualIDs(){return this.visualIDs;}
    public int[] getBehaviorIDs(){return this.behaviorIDs;}
    public int[][] getVisualTiles(){return this.VisualTiles;}
    public int[][] getBehaviorTiles(){return this.BehaviorTiles;}



    public int checkFalling(Hitbox hitbox,int LEVEL_WIDTH,int LEVEL_HEIGHT) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();
        float hitboxHeight = hitbox.getHeight();

        int startX = (int) Math.floor(hitboxX / Constants.TILE_SIZE);
        int endX = (int) Math.floor((hitboxX + hitboxWidth - Constants.EPSILON) / Constants.TILE_SIZE);

        float checkY = hitboxY + hitboxHeight; // The coordinate defining the top of the tile row below
        int tileRowToCheck = (int) Math.floor(checkY / Constants.TILE_SIZE);

        if (tileRowToCheck < 0 || tileRowToCheck >= LEVEL_HEIGHT) {
            return -1; // Off map vertically (below or above), considered falling.
        }

        for (int tileX = startX; tileX <= endX; tileX++) {
            if (tileX < 0 || tileX >= LEVEL_WIDTH) {
                // Treat out-of-bounds as air, but continue checking other tiles
                continue;
            }

            int index = tileRowToCheck * LEVEL_WIDTH + tileX;

            if (index < 0 || index >= behaviorIDs.length) {
                System.err.println("Error: Calculated map index out of bounds: " + index);
                continue; // Treat invalid index as air and continue
            }

            int behavior = behaviorIDs[index];
            if (behavior != -1) {
                return 0; // Found a solid tile, not falling
            }
        }

        // All tiles checked are air or out-of-bounds
        return -1;
    }

    public void snapToGround(Hitbox hitbox) {
        // 1. Calculate the Y coordinate of the bottom of the hitbox.
        float bottomY = hitbox.getY() + hitbox.getHeight();

        // 2. Calculate the Y coordinate of the top surface of the tile row containing/below the bottomY.
        //    Floor division finds the tile index, multiplying by TILE_SIZE gets the top edge Y.
        float groundSurfaceY = (int) Math.floor(bottomY / Constants.TILE_SIZE) * Constants.TILE_SIZE;

        // 3. Set the hitbox's top Y so its bottom rests exactly on the calculated ground surface.
        hitbox.setY((int) (groundSurfaceY - hitbox.getHeight()));
    }


    public boolean checkWallCollision(Hitbox hitbox, boolean checkRight, int LEVEL_WIDTH,int LEVEL_HEIGHT) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();
        float hitboxHeight = hitbox.getHeight();

        float shrinkTop = 0.0f;    // Pixels to ignore from the top
        float shrinkBottom = 2.0f; // Pixels to ignore from the bottom (e.g., feet area)

        float checkStartY = hitboxY + shrinkTop;

        float checkEndY = hitboxY + hitboxHeight - shrinkBottom - Constants.EPSILON;

        if (checkEndY < checkStartY) {
            return false;
        }

        int startTileY = (int) Math.floor(checkStartY / Constants.TILE_SIZE);
        int endTileY = (int) Math.floor(checkEndY / Constants.TILE_SIZE);


        float checkXCoord; // The precise X coordinate we will check tiles at

        if (checkRight) {
            checkXCoord = hitboxX + hitboxWidth -Constants.EPSILON;
        } else { // Checking Left
            checkXCoord = hitboxX - Constants.EPSILON;
        }

        int checkTileX = (int) Math.floor(checkXCoord / Constants.TILE_SIZE);

        for (int tileY = startTileY; tileY <= endTileY; tileY++) {
            if (checkTileX < 0 || checkTileX >= LEVEL_WIDTH || tileY < 0 || tileY >= LEVEL_WIDTH) {
                if (checkRight && checkTileX >= LEVEL_WIDTH) return true; // Hit right world boundary
                if (!checkRight && checkTileX < 0) return true;             // Hit left world boundary
                // If only vertically out of bounds, might not be a wall (e.g., above map),
                // but let's treat it as non-colliding for wall check purposes within this loop.
                continue; // Skip processing for tiles outside vertical map bounds
            }

            int index = tileY * LEVEL_WIDTH + checkTileX;

            if (index < 0 || index >= behaviorIDs.length) {
                System.err.println("Warning: checkWallCollision calculated invalid map index: " + index + " for tile (" + checkTileX + ", " + tileY + ")");
                continue; // Skip this invalid index
            }

            // Get the behavior ID of the tile at the checked location
            int behavior = behaviorIDs[index];

            if (behavior == 2) {
                // System.out.println("Wall Collision detected at tile (" + checkTileX + ", " + tileY + ")");
                return true; // Collision detected
            }
        }

        return false; // No collision
    }


    public boolean checkCeilingCollision(Hitbox hitbox, int LEVEL_WIDTH, int LEVEL_HEIGHT) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();

        int startTileX = (int) Math.floor(hitboxX / Constants.TILE_SIZE);
        int endTileX = (int) Math.floor((hitboxX + hitboxWidth - Constants.EPSILON) / Constants.TILE_SIZE);
        float checkY = hitboxY - Constants.EPSILON;

        int tileRowToCheck = (int) Math.floor(checkY / Constants.TILE_SIZE);

        if (tileRowToCheck < 0) {
            return false;
        }
        if (tileRowToCheck >= LEVEL_HEIGHT) {
            return false;
        }

        for (int tileX = startTileX; tileX <= endTileX; tileX++) {
            if (tileX < 0 || tileX >= LEVEL_WIDTH) {
                continue; // Skip this column, check the next one within the hitbox's span
            }

            int index = tileRowToCheck * LEVEL_WIDTH + tileX;

            if (index < 0 || index >= behaviorIDs.length) {
                System.err.println("Warning: checkCeilingCollision calculated invalid map index: " + index + " for tile (" + tileX + ", " + tileRowToCheck + ")");
                continue; // Skip this potentially invalid index
            }

            int behavior = behaviorIDs[index];

            if (behavior == 2) {
                return true; // Collision detected with a solid tile above
            }
        }
        return false;
    }

    public boolean isTileSolid(int tileX, int tileY, int LEVEL_WIDTH, int LEVEL_HEIGHT){
        if (tileX < 0 || tileX >= LEVEL_WIDTH || tileY < 0 || tileY >= LEVEL_HEIGHT) {
            return false;
        }

        int index = tileY * LEVEL_WIDTH + tileX;
        int behavior = behaviorIDs[index];
        return behavior == 2;
    }


    public boolean isGroundAhead(Hitbox hitbox, boolean headingLeft,int LEVEL_WIDTH,int LEVEL_HEIGHT) {
        int checkTileX;
        // Determine the x-coordinate of the tile to check, just outside the hitbox edge
        if (headingLeft) {
            // Check tile just to the left of the bottom-left corner
            checkTileX = (int) Math.floor((hitbox.getX() - Constants.EPSILON) / Constants.TILE_SIZE);
        } else {
            // Check tile just to the right of the bottom-right corner
            checkTileX = (int) Math.floor((hitbox.getX() + hitbox.getWidth() + Constants.EPSILON) / Constants.TILE_SIZE);
        }

        // Check the tile directly below the leading edge
        int checkTileY = (int) Math.floor((hitbox.getY() + hitbox.getHeight() + Constants.EPSILON) / Constants.TILE_SIZE);

        // Check bounds and solidity (Implement this based on your level data)
        return isTileSolid(checkTileX, checkTileY,LEVEL_WIDTH,LEVEL_HEIGHT);
    }
}
