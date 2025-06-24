package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.keyHandler;

public class createPlayer extends entity{
	
	GamePanel gp;
	keyHandler keyH;
	
	public createPlayer(GamePanel gp, keyHandler keyH) {
		this.gp=gp;
		this.keyH=keyH; 
		
		setDefaultValues();
		getPlayerImages();
	}
	
	public int spriteCount;
	
	public void getPlayerImages() {
		try {
			
			up = ImageIO.read(getClass().getResourceAsStream("/player/tiltedup.png"));
			down = ImageIO.read(getClass().getResourceAsStream("/player/tilteddown.png"));
			left = ImageIO.read(getClass().getResourceAsStream("/player/tiltedleft.png"));
			right = ImageIO.read(getClass().getResourceAsStream("/player/tiltedright.png"));
			still = ImageIO.read(getClass().getResourceAsStream("/player/tiltedstill.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDefaultValues() {
		x = 0; // Start at top-left corner
		y = 0;
		speed = gp.TileSize; // Move one tile at a time
		direction = "still";
	}
	
	public void update() {
		int newX = x;
		int newY = y;
		
		if(keyH.upPress == true) {
			direction = "up";
			newY = y - speed;
		}
		else if(keyH.downPress == true) {
			direction = "down";
			newY = y + speed;
		}
		else if(keyH.leftPress == true) {
			direction = "left";
			newX = x - speed;
		}
		else if(keyH.rightPress == true) {
			direction = "right";
			newX = x + speed;
		} else {
			direction = "still";
			return; // No movement, no need to check collision
		}
		
		// Check collision before moving
		if (!gp.tileM.isWall(newX, newY) && 
			!gp.tileM.isWall(newX + gp.TileSize - 1, newY) &&
			!gp.tileM.isWall(newX, newY + gp.TileSize - 1) &&
			!gp.tileM.isWall(newX + gp.TileSize - 1, newY + gp.TileSize - 1)) {
			
			x = newX;
			y = newY;
			
			// Check if player reached the exit (bottom-right area)
			int tileX = x / gp.TileSize;
			int tileY = y / gp.TileSize;
			
			if (tileX >= gp.maxScreenCol - 2 && tileY >= gp.maxScreenRow - 2) {
				// Player reached the exit, generate new maze
				gp.tileM.regenerateMaze();
				x = 0;
				y = 0;
				direction = "still";
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		switch(direction) {
		case "up":
			image = up;
			break;
		case "down":
			image = down;
			break;
		case "left":
			image = left;
			break;
		case "right":
			image = right;
			break;
			default:
				image = still;
		}
		
		g2.drawImage(image, x, y, gp.TileSize, gp.TileSize, null);
		
	} 

}