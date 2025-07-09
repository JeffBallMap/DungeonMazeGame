package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_DOor extends superObj{
	
	GamePanel gp;
	
	public OBJ_DOor(GamePanel gp) {
		this.gp = gp;
		name = "Door";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/doorbroken.png"));
			zBox.scaleImage(image, gp.TileSize, gp.TileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}

}
