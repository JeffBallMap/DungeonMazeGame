package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_GEM extends superObj{
	
	GamePanel gp;
	
	public OBJ_GEM(GamePanel gp) {
		this.gp = gp;
		name = "Gem";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/gem1.png"));
			zBox.scaleImage(image, gp.TileSize, gp.TileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}

}
