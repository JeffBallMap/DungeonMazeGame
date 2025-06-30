package main;

import entity.entity;

public class CollisionCheck {
	
	GamePanel gp;
	
	public CollisionCheck(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTile(entity entity) {
		
		int entityLeftX = entity.x + entity.solidArea.x;
		int entityRightX = entity.x + entity.solidArea.width + entity.solidArea.x;
		int entityTopY = entity.y + entity.solidArea.y;
		int entityBottY = entity.y + entity.solidArea.height + entity.solidArea.y;
		
		int entityLeftCol = entityLeftX/gp.TileSize;
		int entityRightCol = entityRightX/gp.TileSize;
		int entityUpRow = entityTopY/gp.TileSize;
		int entityDownRow = entityBottY/gp.TileSize;
		int TileNo1, TileNo2;
		
		switch(entity.direction) {
		case "up":
			entityUpRow = (entityTopY - entity.speed)/gp.TileSize;
			TileNo1 = gp.tileM.mapTNum[entityLeftCol][entityUpRow];
			TileNo2 = gp.tileM.mapTNum[entityRightCol][entityUpRow];
			if(gp.tileM.tile[TileNo1].collision == true || gp.tileM.tile[TileNo2].collision == true ) {
				entity.collisionOn=true;
			}
			break;
		case "down":
			entityDownRow = (entityBottY + entity.speed)/gp.TileSize;
			TileNo1 = gp.tileM.mapTNum[entityLeftCol][entityDownRow];
			TileNo2 = gp.tileM.mapTNum[entityRightCol][entityDownRow];
			if(gp.tileM.tile[TileNo1].collision == true || gp.tileM.tile[TileNo2].collision == true ) {
				entity.collisionOn=true;
			}
			break;
		case "left":
			entityLeftCol = (entityLeftX - entity.speed)/gp.TileSize;
			TileNo1 = gp.tileM.mapTNum[entityLeftCol][entityUpRow];
			TileNo2 = gp.tileM.mapTNum[entityLeftCol][entityDownRow];
			if(gp.tileM.tile[TileNo1].collision == true || gp.tileM.tile[TileNo2].collision == true ) {
				entity.collisionOn=true;
			}
			break;
		case "right":
			entityRightCol = (entityRightX + entity.speed)/gp.TileSize;
			TileNo1 = gp.tileM.mapTNum[entityRightCol][entityUpRow];
			TileNo2 = gp.tileM.mapTNum[entityRightCol][entityDownRow];
			if(gp.tileM.tile[TileNo1].collision == true || gp.tileM.tile[TileNo2].collision == true ) {
				entity.collisionOn=true;
			}
			break;
		}
	}
	
	public int checkObj(entity ent, boolean player) {
		int index = 999;
		
		for(int i = 0;i < gp.Obj.length;i++) {
			if(gp.Obj[i]!=null) {
				
				//entity position getter
				ent.solidArea.x = ent.x + ent.solidArea.x;
				ent.solidArea.y = ent.y + ent.solidArea.y;
				//object position getter
				gp.Obj[i].solidArea.x = gp.Obj[i].x + gp.Obj[i].solidArea.x;
				gp.Obj[i].solidArea.y = gp.Obj[i].y + gp.Obj[i].solidArea.y;
				
				switch(ent.direction) {
				case "up":
					ent.solidArea.y -= ent.speed;
					if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
						if(gp.Obj[i].collision == true) {
							ent.collisionOn=true;
						}
						if(player == true) {
							index=i; 
						}
					}
					break;
				case "down":
					ent.solidArea.y += ent.speed;
					if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							if(gp.Obj[i].collision == true) {
								ent.collisionOn=true;
							}
							if(player == true) {
								index = i;
						}
					}}
					break;
				case "left":
					ent.solidArea.x -= ent.speed;
					if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							if(gp.Obj[i].collision == true) {
								ent.collisionOn=true;
							}
							if(player == true) {
								index = i;
							}
						}
					}
					break;
				case "right":
					ent.solidArea.x += ent.speed;
					if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							if(gp.Obj[i].collision == true) {
								ent.collisionOn=true;
							}
							if(player == true) {
								index = i;
							}
						}
					}
					break;
				}
				ent.solidArea.x=ent.solidAreaDefaultX;
				ent.solidArea.y=ent.solidAreaDefaultY;
				gp.Obj[i].solidArea.x=gp.Obj[i].solidAreaDefaultX;
				gp.Obj[i].solidArea.y=gp.Obj[i].solidAreaDefaultY;
			}
		}
		
		return index;
		
		}
	}


