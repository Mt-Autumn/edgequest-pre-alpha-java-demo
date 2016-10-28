package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.window.Renderer;

public class Entities {
	public static void draw(Renderer r) {
		Color.white.bind();
		for( int i = 0; i < r.dataManager.savable.entities.size(); i++) {
			Entity entity = r.dataManager.savable.entities.get(i);
			drawEntity(entity.getTextureName(), entity.getX(), entity.getY(), entity.getRot(), r);
		}
	}
	private static void drawEntity(String texture, double posX, double posY, byte rotation, Renderer r) {
		double blockSize = r.dataManager.settings.blockSize;
		int pixelsX = (int) ((posX - (r.dataManager.system.screenX - (Double.valueOf(r.dataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
		int pixelsY = (int) ((posY - (r.dataManager.system.screenY - (Double.valueOf(r.dataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(r.textureManager.getTexture(texture), (int) (pixelsX - blockSize / 2.0), (int) (pixelsY - blockSize / 2.0), (int) blockSize, (int) blockSize, Float.valueOf(rotation) * 45.0f);

	}
}
