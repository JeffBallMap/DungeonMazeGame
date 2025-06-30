package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_GEM extends superObj{
	
	public OBJ_GEM() {
		name = "Gem";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/gem1.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}

}
