package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Time extends superObj{
	
	GamePanel gp;
	
	public OBJ_Time(GamePanel gp) {
		this.gp = gp;
		name = "Time";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/timebonus.png"));
			zBox.scaleImage(image, gp.TileSize, gp.TileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}

}
