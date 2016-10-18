package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Lighting {
	public static void draw(Renderer r) {
		Color.white.bind();
		
		int xPos = xStartPos(r);
		for(int x = r.dataManager.system.minTileX; x <= r.dataManager.system.maxTileX; x++) {
			
			int yPos = yStartPos(r);
			for (int y = r.dataManager.system.minTileY; y <= r.dataManager.system.maxTileY; y++) {
				
				if (r.dataManager.savable.getBrightness() < 1) {
					drawBrightness(r, x, y, xPos, yPos);
				}
				yPos += r.dataManager.settings.blockSize;
				
			}
			xPos += r.dataManager.settings.blockSize;
			
		}
		
	}
	
	private static int xStartPos(Renderer r) {
		return (int) ((r.dataManager.system.minTileX - r.dataManager.savable.charX) * r.dataManager.settings.blockSize + r.dataManager.settings.screenWidth/2.0);
	}
	
	private static int yStartPos(Renderer r) {
		return (int)((r.dataManager.system.minTileY - r.dataManager.savable.charY) * r.dataManager.settings.blockSize + r.dataManager.settings.screenHeight/2.0);
	}
	
	private static double getBrightness(Renderer r, int x, int y) {
		if (r.dataManager.savable.lightMap.containsKey(x + "," + y)) {
			return Double.valueOf(((int) r.dataManager.savable.lightMap.get(x + "," + y) + 128)) / 255.0;
		}
		return 0.0;
	}
	
	private static void drawBrightness(Renderer r, int x, int y, int xPos, int yPos) {
		double brightness = getBrightness(r, x, y);
		double nightBrightness = (1 - r.dataManager.savable.getBrightness()) * brightness + r.dataManager.savable.getBrightness();
		
		r.fillRect(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize, 0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness));
		float blockBrightness = (float)(0.2 * (nightBrightness - r.dataManager.savable.getBrightness()));
		
		if (blockBrightness > 0) {
			r.fillRect(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize, 1.0f,0.6f,0.05f, blockBrightness);
		}
	}
}
