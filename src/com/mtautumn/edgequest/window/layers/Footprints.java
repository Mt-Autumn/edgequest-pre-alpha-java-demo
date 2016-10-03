package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.FootPrint;
import com.mtautumn.edgequest.window.Renderer;

public class Footprints {
	public static void draw(Renderer r) {
	    Color.white.bind();
	    
		for (int i = 0; i < r.sceneManager.savable.footPrints.size(); i++) {
			FootPrint fp = r.sceneManager.savable.footPrints.get(i);
			
			if (fp.opacity > 0.4) {
				drawPrint(r, fp, "footsteps");
			} else if (fp.opacity > 0.2) {
				drawPrint(r, fp, "footsteps2");
			} else {
				drawPrint(r, fp, "footsteps3");
			}
		}
	}
	
	private static int xPos(Renderer r, FootPrint fp) {
		return (int)((fp.posX - offsetX(r))*r.sceneManager.settings.blockSize);
	}
	
	private static int yPos(Renderer r, FootPrint fp) {
		return (int)((fp.posY - offsetY(r))*r.sceneManager.settings.blockSize);
	}
	
	private static double offsetX(Renderer r) {
		return r.sceneManager.savable.charX - Double.valueOf(r.sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(r.sceneManager.settings.blockSize);
	}
	
	private static double offsetY(Renderer r) {
		return r.sceneManager.savable.charY - Double.valueOf(r.sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(r.sceneManager.settings.blockSize);
	}
	
	
	private static void drawPrint(Renderer r, FootPrint fp, String name) {
		int posX = xPos(r, fp);
		int posY = yPos(r, fp);
		float width = (float)r.sceneManager.settings.blockSize / 6f;
		float length = (float)r.sceneManager.settings.blockSize / 3f;
		r.drawTexture(r.textureManager.getTexture(name), posX - width , posY - length, length, length * 2f, 45f * Float.valueOf(fp.direction));
	}
}
