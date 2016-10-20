package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.Dungeon;
import com.mtautumn.edgequest.window.Renderer;

public class Terrain {
	public static void draw(Renderer r) {
		Color.white.bind();
		int blockSize = r.dataManager.settings.blockSize;
		int minTileX = r.dataManager.system.minTileX;
		int minTileY = r.dataManager.system.minTileY;
		int maxTileX = r.dataManager.system.maxTileX;
		int maxTileY = r.dataManager.system.maxTileY;
		double charX = r.dataManager.savable.charX;
		double charY = r.dataManager.savable.charY;

		int xPos = (int) ((minTileX - charX) * blockSize + r.dataManager.settings.screenWidth/2.0);

			for(int x = minTileX; x <= maxTileX; x++) {
				int yPos = (int)((minTileY - charY) * blockSize + r.dataManager.settings.screenHeight/2.0);
				for (int y = minTileY; y <= maxTileY; y++) {
					r.drawTexture(getTerrainBlockTexture(r, x, y),xPos, yPos, blockSize, blockSize);
					if (r.dataManager.world.isStructBlock(x, y)) {
						r.drawTexture(getStructureBlockTexture(r, x, y),xPos, yPos - r.dataManager.system.blockIDMap.get(r.dataManager.world.getStructBlock(x, y)).blockHeight * blockSize, blockSize, blockSize + r.dataManager.system.blockIDMap.get(r.dataManager.world.getStructBlock(x, y)).blockHeight * blockSize);
					}
					yPos += blockSize;
				}
				xPos += blockSize;
			}
		}

	private static Texture getTerrainBlockTexture(Renderer r, int x, int y) {
		short blockValue = getTerrainBlockValue(r, x, y);
		return r.dataManager.system.blockIDMap.get(blockValue).getBlockImg(r.dataManager.savable.time);
	}
	private static Texture getStructureBlockTexture(Renderer r, int x, int y) {
		short blockValue = getStructureBlockValue(r, x, y);
		return r.dataManager.system.blockIDMap.get(blockValue).getBlockImg(r.dataManager.savable.time);
	}
	private static short getStructureBlockValue(Renderer r, int x, int y) {
		if (r.dataManager.world.isStructBlock(x, y)) {
			return r.dataManager.world.getStructBlock(x, y);
		}
		return 0;
	}
	private static short getTerrainBlockValue(Renderer r, int x, int y) {
		if (r.dataManager.world.isGroundBlock(x, y)) {
			return r.dataManager.world.getGroundBlock(x, y);
		}
		return 0;
	}
	private static short getDungeonStructureBlockValue(Renderer r, int x, int y, Dungeon dungeon) {
		return dungeon.getStructureBlock(r.dataManager.savable.dungeonLevel, x, y);
	}
	private static short getDungeonTerrainBlockValue(Renderer r, int x, int y, Dungeon dungeon) {
		return dungeon.getGroundBlock(r.dataManager.savable.dungeonLevel, x, y);
	}



}
