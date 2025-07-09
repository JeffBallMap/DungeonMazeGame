package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ZToolBox {
	
	public BufferedImage scaleImage(BufferedImage og, int wid, int hig) {
		BufferedImage scaledImage = new BufferedImage(wid,hig,og.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(og,0,0,wid,hig,null);
		
		g2.dispose();
		
		return scaledImage;
	}

}
