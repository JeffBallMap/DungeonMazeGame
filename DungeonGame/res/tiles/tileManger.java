package tiles;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import mazer.Maze;

public class tileManger {
	GamePanel gp;
	public makeTile[] tile;
	public int mapTNum[][];
	Maze maze;
	
	public tileManger(GamePanel gp) {
        this.gp = gp;
        tile = new makeTile[10];
        mapTNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        maze = new Maze(gp);

        String file = maze.saveMazeToPackageFolder("maze_output.txt");

        getTileImage();
        loadMap(file);
    }
	
	public void getTileImage() {
		try {
			
			tile[0] = new makeTile();
			tile[0].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudbrick.png"));//bricks aka walls
			tile[0].collision = true;
			
			tile[1] = new makeTile();
			tile[1].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtilecracked.png"));//cracked
			
			tile[2] = new makeTile();
			tile[2].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtiledbrocke.png"));//broken
			
			tile[3] = new makeTile();
			tile[3].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtileejustsand.png"));//sand
			
			tile[4] = new makeTile();
			tile[4].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtileclean.png"));//clean
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void loadMap(String filePath) {
	    try (InputStream is = getClass().getResourceAsStream(filePath);
	         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

	        for (int row = 0; row < gp.maxScreenRow; row++) {
	            String line = br.readLine();
	            if (line == null) break;
	            String[] numbers = line.split(" ");
	            for (int col = 0; col < gp.maxScreenCol; col++) {
	                mapTNum[col][row] = Integer.parseInt(numbers[col]);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void draw(Graphics2D g2) {
		
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		
		while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
			
			int tileNum = mapTNum[col][row];
			
			g2.drawImage(tile[tileNum].imagee, x, y, gp.TileSize, gp.TileSize, null);
			col++;
			x+=gp.TileSize;
			
			if(col == gp.maxScreenCol) {
				col = 0;
				x = 0;
				row++;
				y+=gp.TileSize;
			}
			
		}
		
	}

}
