package PaooGame.Maps;

import PaooGame.Config.Constants;
import PaooGame.Hitbox.Hitbox;
import PaooGame.RefLinks;
import PaooGame.Tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Level1 {
    private int [][] VisualTiles;
    private int [][] BehaviorTiles;
    int[] visualIDs;
    int[] behaviorIDs;

    public int[] getVisualIDs(){return this.visualIDs;}
    public int[] getBehaviorIDs(){return this.behaviorIDs;}
    public int[][] getVisualTiles(){return this.VisualTiles;}
    public int[][] getBehaviorTiles(){return this.BehaviorTiles;}

    public Level1(){
        this.VisualTiles=new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.BehaviorTiles=new int[Constants.LEVEL1_WIDTH][Constants.LEVEL1_HEIGHT];
        this.visualIDs=new int[Constants.LEVEL1_TILE_NR];
        this.behaviorIDs=new int[Constants.LEVEL1_TILE_NR];


        String line;
        try
        {
            int i=0;
            BufferedReader br=new BufferedReader(new FileReader(Constants.LEVEL1_TEXTURES_CSV));
            BufferedReader br2=new BufferedReader(new FileReader(Constants.LEVEL1_BEHAVIOR_CSV));
            while((line = br.readLine())!=null){
                String[] tokens;
                tokens=line.split(",");
                for(String token: tokens){
                    visualIDs[i++]=Integer.parseInt(token);
                }
            }
            i=0;
            while((line=br2.readLine())!=null){
                String[] tokens;
                tokens=line.split(",");
                for(String token: tokens){
                    behaviorIDs[i++]=Integer.parseInt(token);
//                    this.BehaviorTiles[i/70][i%70]=Integer.parseInt(token);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("file not found");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("IoException");
        }
        catch(NumberFormatException e){
            e.printStackTrace();
            System.err.println("NumberFormatException");
        }

    }

    public int checkFalling(Hitbox hitbox) {
        float hitboxX = hitbox.getX();
        float hitboxY = hitbox.getY();
        float hitboxWidth = hitbox.getWidth();
        float hitboxHeight = hitbox.getHeight();

        int startX = (int) Math.floor(hitboxX / Constants.TILE_SIZE);
        int endX = (int) Math.floor((hitboxX + hitboxWidth - Constants.EPSILON) / Constants.TILE_SIZE);

        float checkY = hitboxY + hitboxHeight; // The coordinate defining the top of the tile row below

        int tileRowToCheck = (int) Math.floor(checkY / Constants.TILE_SIZE);

        if (tileRowToCheck < 0 || tileRowToCheck >= Constants.LEVEL1_HEIGHT) {
            return -1; // Off map vertically (below or above), considered falling.
        }


        for (int tileX = startX; tileX <= endX; tileX++) {
            if (tileX < 0 || tileX >= Constants.LEVEL1_WIDTH) {
                return -1;
            }

            int index = tileRowToCheck * Constants.LEVEL1_WIDTH + tileX;

            if (index < 0 || index >= behaviorIDs.length) {
                System.err.println("Error: Calculated map index out of bounds: " + index);
                return -1; // Treat invalid index as falling
            }


            int behavior = behaviorIDs[index];
            if (behavior == -1) { // Is this specific tile below the hitbox air?
                return -1; // Yes -> falling
            }
        }
        return 0; // Standing
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


    public boolean checkWallCollision(Hitbox hitbox, boolean checkRight) {
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
            if (checkTileX < 0 || checkTileX >= Constants.LEVEL1_WIDTH || tileY < 0 || tileY >= Constants.LEVEL1_HEIGHT) {
                if (checkRight && checkTileX >= Constants.LEVEL1_WIDTH) return true; // Hit right world boundary
                if (!checkRight && checkTileX < 0) return true;             // Hit left world boundary
                // If only vertically out of bounds, might not be a wall (e.g., above map),
                // but let's treat it as non-colliding for wall check purposes within this loop.
                continue; // Skip processing for tiles outside vertical map bounds
            }

            int index = tileY * Constants.LEVEL1_WIDTH + checkTileX;

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


    public boolean checkCeilingCollision(Hitbox hitbox) {
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
        if (tileRowToCheck >= Constants.LEVEL1_HEIGHT) {
            return false;
        }

        for (int tileX = startTileX; tileX <= endTileX; tileX++) {
            if (tileX < 0 || tileX >= Constants.LEVEL1_WIDTH) {
                continue; // Skip this column, check the next one within the hitbox's span
            }

            int index = tileRowToCheck * Constants.LEVEL1_WIDTH + tileX;

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







    void update(){
    }


}