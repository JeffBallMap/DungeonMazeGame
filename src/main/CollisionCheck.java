package main;

import entity.entity;

public class CollisionCheck {
	
	GamePanel gp;  // Reference to main game panel
	
	public CollisionCheck(GamePanel gp) {
		this.gp = gp;
	}
	
	// Check collision between an entity and tiles
	public void checkTile(entity entity) {
		
		// Calculate entity's bounding box edges in world coordinates
		int entityLeftX = entity.x + entity.solidArea.x;
		int entityRightX = entity.x + entity.solidArea.width + entity.solidArea.x;
		int entityTopY = entity.y + entity.solidArea.y;
		int entityBottY = entity.y + entity.solidArea.height + entity.solidArea.y;
		
		// Calculate which tile columns and rows the entity's bounding box edges fall into
		int entityLeftCol = entityLeftX / gp.TileSize;
		int entityRightCol = entityRightX / gp.TileSize;
		int entityUpRow = entityTopY / gp.TileSize;
		int entityDownRow = entityBottY / gp.TileSize;
		int TileNo1, TileNo2;
		
		switch(entity.direction) {
			case "up":
				// Predict the tile row entity will move into when moving up
				entityUpRow = (entityTopY - entity.speed) / gp.TileSize;
				// Get tile numbers at entity's left and right edges in that row
				TileNo1 = gp.tileM.mapTNum[entityLeftCol][entityUpRow];
				TileNo2 = gp.tileM.mapTNum[entityRightCol][entityUpRow];
				// If either tile has collision enabled, mark collision true
				if(gp.tileM.tile[TileNo1].collision || gp.tileM.tile[TileNo2].collision) {
					entity.collisionOn = true;
				}
				break;
				
			case "down":
				// Predict the tile row entity will move into when moving down
				entityDownRow = (entityBottY + entity.speed) / gp.TileSize;
				// Get tile numbers at entity's left and right edges in that row
				TileNo1 = gp.tileM.mapTNum[entityLeftCol][entityDownRow];
				TileNo2 = gp.tileM.mapTNum[entityRightCol][entityDownRow];
				if(gp.tileM.tile[TileNo1].collision || gp.tileM.tile[TileNo2].collision) {
					entity.collisionOn = true;
				}
				break;
				
			case "left":
				// Predict the tile column entity will move into when moving left
				entityLeftCol = (entityLeftX - entity.speed) / gp.TileSize;
				// Get tile numbers at entity's top and bottom edges in that column
				TileNo1 = gp.tileM.mapTNum[entityLeftCol][entityUpRow];
				TileNo2 = gp.tileM.mapTNum[entityLeftCol][entityDownRow];
				if(gp.tileM.tile[TileNo1].collision || gp.tileM.tile[TileNo2].collision) {
					entity.collisionOn = true;
				}
				break;
				
			case "right":
				// Predict the tile column entity will move into when moving right
				entityRightCol = (entityRightX + entity.speed) / gp.TileSize;
				// Get tile numbers at entity's top and bottom edges in that column
				TileNo1 = gp.tileM.mapTNum[entityRightCol][entityUpRow];
				TileNo2 = gp.tileM.mapTNum[entityRightCol][entityDownRow];
				if(gp.tileM.tile[TileNo1].collision || gp.tileM.tile[TileNo2].collision) {
					entity.collisionOn = true;
				}
				break;
		}
	}
	
	// Check collision between an entity and objects; returns index of collided object if player
	public int checkObj(entity ent, boolean player) {
		int index = 999;  // Default: no collision found
		
		// Loop through all objects in the game
		for(int i = 0; i < gp.Obj.length; i++) {
			if(gp.Obj[i] != null) {
				
				// Save original solid area offsets (relative to entity/object position)
				int entSolidX = ent.solidArea.x;
				int entSolidY = ent.solidArea.y;
				int objSolidX = gp.Obj[i].solidArea.x;
				int objSolidY = gp.Obj[i].solidArea.y;
				
				// Update solid area to absolute world coordinates for collision check
				ent.solidArea.x = ent.x + ent.solidArea.x;
				ent.solidArea.y = ent.y + ent.solidArea.y;
				gp.Obj[i].solidArea.x = gp.Obj[i].x + gp.Obj[i].solidArea.x;
				gp.Obj[i].solidArea.y = gp.Obj[i].y + gp.Obj[i].solidArea.y;
				
				// Move the entity solid area according to its direction and speed
				switch(ent.direction) {
					case "up":
						ent.solidArea.y -= ent.speed;
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							// If object has collision, set collisionOn true
							if(gp.Obj[i].collision) ent.collisionOn = true;
							// If checking for player, record index of object collided with
							if(player) index = i;
						}
						break;
					case "down":
						ent.solidArea.y += ent.speed;
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							if(gp.Obj[i].collision) ent.collisionOn = true;
							if(player) index = i;
						}
						break;
					case "left":
						ent.solidArea.x -= ent.speed;
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							if(gp.Obj[i].collision) ent.collisionOn = true;
							if(player) index = i;
						}
						break;
					case "right":
						ent.solidArea.x += ent.speed;
						if(ent.solidArea.intersects(gp.Obj[i].solidArea)) {
							if(gp.Obj[i].collision) ent.collisionOn = true;
							if(player) index = i;
						}
						break;
				}
				
				// Restore original solid area offsets
				ent.solidArea.x = entSolidX;
				ent.solidArea.y = entSolidY;
				gp.Obj[i].solidArea.x = objSolidX;
				gp.Obj[i].solidArea.y = objSolidY;
			}
		}
		return index;  // Return index of collided object or 999 if none
	}
}
