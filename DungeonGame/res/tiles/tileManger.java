package tiles;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class tileManger {
	
	GamePanel gp;
	makeTile[] tile;
	int mapTNum[][];
	
	public tileManger(GamePanel gp) {
		this.gp = gp;
		tile = new makeTile[10];
		mapTNum = new int[gp.maxScreenCol][gp.maxScreenRow];
		
		getTileImage();
		loadMap();
		
		
	}
	
	public void getTileImage() {
		try {
			
			tile[0] = new makeTile();
			tile[0].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtileclean.png"));//clean
			
			tile[1] = new makeTile();
			tile[1].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtilecracked.png"));//cracked
			
			tile[2] = new makeTile();
			tile[2].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtiledbrocke.png"));//broken
			
			tile[3] = new makeTile();
			tile[3].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudtileejustsand.png"));//sand
			
			tile[4] = new makeTile();
			tile[4].imagee = ImageIO.read(getClass().getResourceAsStream("/tiles/mudbrick.png"));//bricks
			
		}catch(IOException e) {
			e.printStackTrace();
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
