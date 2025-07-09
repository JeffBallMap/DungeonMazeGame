package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Mark extends superObj{
	
	GamePanel gp;
	
	public OBJ_Mark(GamePanel gp) {
		this.gp = gp;
		name = "Markers";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/solnMarker.png.png"));
			zBox.scaleImage(image, gp.TileSize, gp.TileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}
}
