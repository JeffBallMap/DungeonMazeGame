package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class entity {
	
	public int x,y;
	public int speed;
	
	public BufferedImage up, left, right, down, still;
	public String direction;
	
	public Rectangle solidArea;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;

}
