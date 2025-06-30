package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.keyHandler;

public class createPlayer extends entity{
	
	GamePanel gp;
	keyHandler keyH;
	int hasGem = 0;
	boolean reset = false;
	
	public createPlayer(GamePanel gp, keyHandler keyH) {
		this.gp=gp;
		this.keyH=keyH; 
		
		solidArea = new Rectangle(8,8,16,16);
		solidAreaDefaultX = solidArea.x; 
		solidAreaDefaultY = solidArea.y;
		
		speed=4;		
		setDefaultValues();
		getPlayerImages();
	}
	
	//public int spriteCount;
	
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
		x=1*gp.TileSize;
		y=1*gp.TileSize;
		direction="still";
	}
	
	public void update() {
		// If reset flag is true, return player to starting position
		if (reset) {
			setDefaultValues(); 
			reset = false;      
			return;             
		}
		
		if(keyH.upPress == true) {
			direction = "up";
		}
		else if(keyH.downPress == true) {
			direction = "down";
		}
		else if(keyH.leftPress == true) {
			direction = "left";
		}
		else if(keyH.rightPress == true) {
			direction = "right";
		}else {
			direction = "still";
		}
		
		
		//Collision check
		collisionOn = false;
		gp.collcheck.checkTile(this);
		int objIndex=gp.collcheck.checkObj(this, true);
		pickUpObj(objIndex);
		
		//if collision will happen
		if(collisionOn==false) {
			switch(direction) {
			case "up": y -= speed;
				break;
			case "down": y += speed;
				break;
			case "left": x -= speed;
				break;
			case "right": x += speed;
				break;
			}
		}		
	}
	
	public void pickUpObj(int index) {
		
		if(index != 999) {
			String objName = gp.Obj[index].name;
			
			switch(objName) {
			case "Gem":
				gp.Obj[index] = null;
				hasGem++;
				System.out.println("You found a gem! You have "+hasGem +" gem(s).");
				break;
			case "Door":
				reset = true;
				break;
			case "Time":
				speed += 2;
				gp.Obj[index] = null;
				break;
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
