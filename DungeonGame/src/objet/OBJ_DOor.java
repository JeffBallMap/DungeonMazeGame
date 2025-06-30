package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_DOor extends superObj{
	
	public OBJ_DOor() {
		name = "Door";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/doorbroken.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}

}
