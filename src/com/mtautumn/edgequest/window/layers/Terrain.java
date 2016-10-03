package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.window.Renderer;

public class Terrain {
	public static void draw(Renderer r) {
		Color.white.bind();
		int blockSize = r.sceneManager.settings.blockSize;
		int minTileX = r.sceneManager.system.minTileX;
		int minTileY = r.sceneManager.system.minTileY;
		int maxTileX = r.sceneManager.system.maxTileX;
		int maxTileY = r.sceneManager.system.maxTileY;
		double charX = r.sceneManager.savable.charX;
		double charY = r.sceneManager.savable.charY;
		
		int xPos = (int) ((minTileX - charX) * blockSize + r.sceneManager.settings.screenWidth/2.0);
		for(int x = minTileX; x <= maxTileX; x++) {
			int yPos = (int)((minTileY - charY) * blockSize + r.sceneManager.settings.screenHeight/2.0);
			for (int y = minTileY; y <= maxTileY; y++) {
				r.drawTexture(getTerrainBlockTexture(r, x, y),xPos, yPos, blockSize, blockSize);
				if (r.sceneManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
					r.drawTexture(getStructureBlockTexture(r, x, y),xPos, yPos - r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.playerStructuresMap.get(x + "," + y)).blockHeight * blockSize, blockSize, blockSize + r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.playerStructuresMap.get(x + "," + y)).blockHeight * blockSize);
				}
				yPos += blockSize;
			}
			xPos += blockSize;
		}
	}
	
	private static Texture getTerrainBlockTexture(Renderer r, int x, int y) {
		short blockValue = getTerrainBlockValue(r, x, y);
		return r.sceneManager.system.blockIDMap.get(blockValue).getBlockImg(r.sceneManager.savable.time);
	}
	private static Texture getStructureBlockTexture(Renderer r, int x, int y) {
		short blockValue = getStructureBlockValue(r, x, y);
		return r.sceneManager.system.blockIDMap.get(blockValue).getBlockImg(r.sceneManager.savable.time);
	}
	private static short getStructureBlockValue(Renderer r, int x, int y) {
		if (r.sceneManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return r.sceneManager.savable.playerStructuresMap.get(x + "," + y);
		} else {
			return 0;
		}
	}
	private static short getTerrainBlockValue(Renderer r, int x, int y) {
		if (r.sceneManager.savable.map.containsKey(x + "," + y)) {
			return r.sceneManager.savable.map.get(x + "," + y);
		} else {
			return 0;
		}
	}
}
