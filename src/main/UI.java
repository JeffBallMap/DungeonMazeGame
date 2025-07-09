package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import objet.OBJ_GEM;

public class UI {
	GamePanel gp;
	Font arial_20, calibri_80;
	BufferedImage gemImage;
	public boolean messageOn = false;
	public String message = " ";
	int messageTime = 0;
	public boolean gameFin = false;
	
	double playTime;
	DecimalFormat dF = new DecimalFormat("#0.00");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		arial_20 = new Font("Calibri", Font.BOLD, 20);
		calibri_80 = new Font("Calibri", Font.BOLD, 80);
		OBJ_GEM gem = new OBJ_GEM(gp);
		gemImage = gem.image;
		
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	public void draw(Graphics2D g2) {
		if(gameFin == true) {
			
			String text;
			int textlength;
			int x;
			int y;
			g2.setFont(calibri_80);
			g2.setColor(Color.black);
			
			text="Fin";
			textlength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		
			x=gp.screenWidth/2 - textlength/2;
			y=gp.screenHeight/2 - (gp.TileSize*3);

			g2.drawString(text, x, y);
			
			text="Your Time is: "+dF.format(playTime);
			textlength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		
			x=gp.screenWidth/2 - textlength/2;
			y=20+gp.screenHeight/2 + (gp.TileSize*1);

			g2.drawString(text, x, y);
			
			gp.gameThread=null;
			
		} else {
			
			g2.setFont(arial_20);
			g2.setColor(Color.black);
			g2.drawImage(gemImage, 1,1, gp.TileSize, gp.TileSize, null);
			g2.drawString("x"+gp.player.hasGem , gp.TileSize, 10+gp.TileSize/2);
			
			//Timer
			playTime += (double)1/60;
			g2.drawString("Time:"+dF.format(playTime), gp.TileSize*10, 10+gp.TileSize/2);
			
			if(messageOn==true) {
				g2.setFont(g2.getFont().deriveFont(Font.ITALIC ,21));
				g2.drawString(message, gp.TileSize*2-6, gp.TileSize-6);
				messageTime++;
				
				if(messageTime >= 120) {
					messageTime=0;
					messageOn=false;
				}
				
			}
			
		}
		
	}

}
