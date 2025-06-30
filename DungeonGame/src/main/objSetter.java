package main;

import objet.OBJ_DOor;
import objet.OBJ_GEM;
import objet.OBJ_Time;

public class objSetter {
	
	GamePanel gp;
	public objSetter(GamePanel gp) {
		this.gp=gp;
	}
	
	public void setObj() {
		
		gp.Obj[0] = new OBJ_GEM();
		gp.Obj[0].x=21 * gp.TileSize;
		gp.Obj[0].y=3 * gp.TileSize;
		
		gp.Obj[1] = new OBJ_GEM();
		gp.Obj[1].x=15 * gp.TileSize;
		gp.Obj[1].y=23 * gp.TileSize;
		
		gp.Obj[2] = new OBJ_DOor();
		gp.Obj[2].x=24 * gp.TileSize;
		gp.Obj[2].y=1 * gp.TileSize;		
		
		gp.Obj[3] = new OBJ_Time();
		gp.Obj[3].x=14 * gp.TileSize;
		gp.Obj[3].y=11 * gp.TileSize;
		
	}

}
