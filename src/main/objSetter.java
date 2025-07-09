package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import objet.OBJ_DOor;
import objet.OBJ_GEM;
import objet.OBJ_Mark;
import objet.OBJ_Time;

public class objSetter {

    GamePanel gp;  // Reference to GamePanel

    public objSetter(GamePanel gp) {
        this.gp = gp;
    }

    // Check if tile is walkable (tiles 1,2,3,4)
    public boolean tileCheck(int col, int row) {
        int tileNum = gp.tileM.mapTNum[col][row];
        return tileNum == 1 || tileNum == 2 || tileNum == 3 || tileNum == 4;
    }

    // Get random walkable tile coordinates
    public int[] getValidRandomTile() {
        Random rand = new Random();
        int col, row;

        while (true) {
            col = rand.nextInt(gp.maxScreenCol);  // Random column
            row = rand.nextInt(gp.maxScreenRow);  // Random row
            if (tileCheck(col, row)) {             // Check if walkable
                return new int[] { col, row };     // Return valid coords
            }
        }
    }

    // Place objects on map
    public void setObj() {
        int[] coords;

        // GEM 1 - random location
        coords = getValidRandomTile();
        gp.Obj[0] = new OBJ_GEM(gp);
        gp.Obj[0].x = coords[0] * gp.TileSize;
        gp.Obj[0].y = coords[1] * gp.TileSize;

        // GEM 2 - random location
        coords = getValidRandomTile();
        gp.Obj[1] = new OBJ_GEM(gp);
        gp.Obj[1].x = coords[0] * gp.TileSize;
        gp.Obj[1].y = coords[1] * gp.TileSize;

        // DOOR - fixed location
        gp.Obj[2] = new OBJ_DOor(gp);
        gp.Obj[2].x = (gp.maxScreenCol - 1) * gp.TileSize;
        gp.Obj[2].y = 1 * gp.TileSize;

        // TIME BOOST - random location
        coords = getValidRandomTile();
        gp.Obj[3] = new OBJ_Time(gp);
        gp.Obj[3].x = coords[0] * gp.TileSize;
        gp.Obj[3].y = coords[1] * gp.TileSize;
    }
    
    // Load solution markers from solution file
    public void setSolutionMarkers(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < gp.maxScreenRow) {
                String[] values = line.trim().split(" ");  // Split line into tiles
                for (int col = 0; col < values.length && col < gp.maxScreenCol; col++) {
                    if (values[col].equals("1")) {          // If solution marker present
                        OBJ_Mark marker = new OBJ_Mark(gp); 
                        marker.x = col * gp.TileSize;       // Set x position
                        marker.y = row * gp.TileSize;       // Set y position
                        gp.solnMarkers.add(marker);          // Add marker to list
                    }
                }
                row++;  // Next row
            }
        } catch (IOException e) {
            System.out.println("Error reading solution file: " + e.getMessage());
        }
    }
}
