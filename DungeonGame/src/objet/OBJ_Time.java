package objet;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_Time extends superObj{
	
	public OBJ_Time() {
		name = "Time";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objet/timebonus.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision=false;
	}

}
