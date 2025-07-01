package main;

import java.util.Random;

import objet.OBJ_DOor;
import objet.OBJ_GEM;
import objet.OBJ_Time;

public class objSetter {

    GamePanel gp;

    public objSetter(GamePanel gp) {
        this.gp = gp;
    }

    // Checks if the tile at (col, row) is walkable (e.g., tile number 1,2,3,4)
    public boolean tileCheck(int col, int row) {
        int tileNum = gp.tileM.mapTNum[col][row];
        return tileNum == 1||tileNum == 2||tileNum == 3||tileNum == 4;
    }

    // Returns a valid random (col, row) where the tile is walkable
    public int[] getValidRandomTile() {
        Random rand = new Random();
        int col, row;

        while (true) {
            col = rand.nextInt(gp.maxScreenCol);
            row = rand.nextInt(gp.maxScreenRow);
            if (tileCheck(col, row)) {
                return new int[]{col, row};
            }
        }
    }

    // Sets the objects on the map
    public void setObj() {
        int[] coords;

        // GEM 1
        coords = getValidRandomTile();
        gp.Obj[0] = new OBJ_GEM();
        gp.Obj[0].x = coords[0] * gp.TileSize;
        gp.Obj[0].y = coords[1] * gp.TileSize;

        // GEM 2
        coords = getValidRandomTile();
        gp.Obj[1] = new OBJ_GEM();
        gp.Obj[1].x = coords[0] * gp.TileSize;
        gp.Obj[1].y = coords[1] * gp.TileSize;

        // DOOR (fixed location)
        gp.Obj[2] = new OBJ_DOor();
        gp.Obj[2].x = 24 * gp.TileSize;
        gp.Obj[2].y = 1 * gp.TileSize;

        // TIME BOOST
        coords = getValidRandomTile();
        gp.Obj[3] = new OBJ_Time();
        gp.Obj[3].x = coords[0] * gp.TileSize;
        gp.Obj[3].y = coords[1] * gp.TileSize;
    }
}
