package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class BlockDamage {
	public static void draw(Renderer r) {
		Color.white.bind();
		
		int posX = getMousePosX(r);
		int posY = getMousePosY(r);
		int blockSize = r.dataManager.settings.blockSize;
		
		if (!r.dataManager.system.isMouseFar) drawBlockHealth(r, posX, posY, blockSize);		
	}
	
	
	private static int getMousePosX(Renderer r) {
		double coordsOffsetX = offsetX(r);
		return (int)((r.dataManager.system.mouseX - coordsOffsetX)*r.dataManager.settings.blockSize);
	}
	private static int getMousePosY(Renderer r) {
		double coordsOffsetY = offsetY(r);
		return (int)((r.dataManager.system.mouseY - coordsOffsetY)*r.dataManager.settings.blockSize);
	}
	
	
	private static double offsetX(Renderer r) {
		return r.dataManager.savable.charX - Double.valueOf(r.dataManager.settings.screenWidth) / Double.valueOf(2 * r.dataManager.settings.blockSize);
	}
	private static double offsetY(Renderer r) {
		return r.dataManager.savable.charY - Double.valueOf(r.dataManager.settings.screenHeight) / Double.valueOf(2 * r.dataManager.settings.blockSize);
	}
	
	private static void drawBlockHealth(Renderer r, int posX, int posY, int blockSize) {
		if (r.dataManager.system.blockDamage != 0 ) {
			r.drawTexture(r.textureManager.getTexture("blockHealthBar"), posX, posY, blockSize, blockSize);
			r.drawTexture(r.textureManager.getTexture("blockHealth"), posX, posY, (float) (blockSize * (r.dataManager.system.blockDamage / 10.0)), blockSize);
		}
	}
}
