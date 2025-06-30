package objet;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class superObj {
	
	public BufferedImage image;
	public String name;
	public boolean collision;
	public int x,y;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public Rectangle solidArea = new Rectangle(0,0,48,48);
	
	public void draw(Graphics2D g2, GamePanel gp) {
		
		g2.drawImage(image, x, y, gp.TileSize, gp.TileSize, null);
		
	}

}
