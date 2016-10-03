package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Lighting {
	public static void draw(Renderer r) {
		Color.white.bind();
		
		int xPos = xStartPos(r);
		for(int x = r.sceneManager.system.minTileX; x <= r.sceneManager.system.maxTileX; x++) {
			
			int yPos = yStartPos(r);
			for (int y = r.sceneManager.system.minTileY; y <= r.sceneManager.system.maxTileY; y++) {
				
				if (r.sceneManager.savable.getBrightness() < 1) {
					drawBrightness(r, x, y, xPos, yPos);
				}
				yPos += r.sceneManager.settings.blockSize;
				
			}
			xPos += r.sceneManager.settings.blockSize;
			
		}
		
	}
	
	private static int xStartPos(Renderer r) {
		return (int) ((r.sceneManager.system.minTileX - r.sceneManager.savable.charX) * r.sceneManager.settings.blockSize + r.sceneManager.settings.screenWidth/2.0);
	}
	
	private static int yStartPos(Renderer r) {
		return (int)((r.sceneManager.system.minTileY - r.sceneManager.savable.charY) * r.sceneManager.settings.blockSize + r.sceneManager.settings.screenHeight/2.0);
	}
	
	private static double getBrightness(Renderer r, int x, int y) {
		if (r.sceneManager.savable.lightMap.containsKey(x + "," + y)) {
			return Double.valueOf(((int) r.sceneManager.savable.lightMap.get(x + "," + y) + 128)) / 255.0;
		} else {
			return 0.0;
		}
	}
	
	private static void drawBrightness(Renderer r, int x, int y, int xPos, int yPos) {
		double brightness = getBrightness(r, x, y);
		double nightBrightness = (1 - r.sceneManager.savable.getBrightness()) * brightness + r.sceneManager.savable.getBrightness();
		
		r.fillRect(xPos, yPos, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize, 0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness));
		float blockBrightness = (float)(0.2 * (nightBrightness - r.sceneManager.savable.getBrightness()));
		
		if (blockBrightness > 0) {
			r.fillRect(xPos, yPos, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize, 1.0f,0.6f,0.05f, blockBrightness);
		}
	}
}
