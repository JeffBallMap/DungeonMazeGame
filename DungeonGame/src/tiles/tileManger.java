package tiles;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import maze.MazeGenerator;

public class tileManger {
	
	GamePanel gp;
	public makeTile[] tile;
	public int mapTNum[][];
	private Random random;
	
	public tileManger(GamePanel gp) {
		this.gp = gp;
		this.random = new Random();
		tile = new makeTile[10];
		mapTNum = new int[gp.maxScreenCol][gp.maxScreenRow];
		
		getTileImage();
		generateRandomMaze();
	}
	
	public void getTileImage() {
		try {
			
			tile[0] = new makeTile();
			tile[0].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtileclean.png"));//clean path
			tile[0].collision = false;
			
			tile[1] = new makeTile();
			tile[1].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtilecracked.png"));//cracked path
			tile[1].collision = false;
			
			tile[2] = new makeTile();
			tile[2].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtiledbrocke.png"));//broken path
			tile[2].collision = false;
			
			tile[3] = new makeTile();
			tile[3].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtileejustsand.png"));//sand path
			tile[3].collision = false;
			
			tile[4] = new makeTile();
			tile[4].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudbrick.png"));//wall
			tile[4].collision = true;
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateRandomMaze() {
		MazeGenerator generator = new MazeGenerator(gp.maxScreenCol, gp.maxScreenRow);
		int[][] mazeLayout = generator.generateMaze();
		
		// Convert maze layout to tile numbers
		for (int x = 0; x < gp.maxScreenCol; x++) {
			for (int y = 0; y < gp.maxScreenRow; y++) {
				if (mazeLayout[x][y] == 1) {
					// Wall
					mapTNum[x][y] = 4;
				} else {
					// Path - randomize between different floor tiles
					mapTNum[x][y] = random.nextInt(4); // 0-3 for different floor types
				}
			}
		}
	}
	
	public void loadMap() {
		try {
			
			InputStream is = getClass().getResourceAsStream("/maps/testmap1.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col<gp.maxScreenCol && row<gp.maxScreenRow) {
				
				String line = br.readLine();
				
				while(col<gp.maxScreenCol) {
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTNum[col][row] = num;
					col++;
				}
				
				if(col == gp.maxScreenCol) {
					col=0;
					row++;
				}
				
			}
			br.close();
			
		}catch(Exception e) {
			
		}
	}
	
	public boolean isWall(int x, int y) {
		int tileX = x / gp.TileSize;
		int tileY = y / gp.TileSize;
		
		if (tileX < 0 || tileX >= gp.maxScreenCol || tileY < 0 || tileY >= gp.maxScreenRow) {
			return true; // Treat out of bounds as walls
		}
		
		return tile[mapTNum[tileX][tileY]].collision;
	}
	
	public void regenerateMaze() {
		generateRandomMaze();
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