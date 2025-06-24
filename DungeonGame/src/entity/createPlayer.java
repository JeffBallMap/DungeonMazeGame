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
		x=100;
		y=100;
		speed=10;
		direction="still";
	}
	public void update() {
		if(keyH.upPress == true) {
			direction = "up";
			y -= speed;
		}
		else if(keyH.downPress == true) {
			direction = "down";
			y += speed;
		}
		else if(keyH.leftPress == true) {
			direction = "left";
			x -= speed;
		}
		else if(keyH.rightPress == true) {
			direction = "right";
			x += speed;
		}else {
			direction = "still";
		}
		
		//spriteCount++;
		//if(s)
		
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
