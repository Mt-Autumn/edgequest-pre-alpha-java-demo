package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class MouseSelection {
	public static void draw(Renderer r) {
		Color.white.bind();
		
		int posX = getMousePosX(r);
		int posY = getMousePosY(r);
		int blockSize = r.sceneManager.settings.blockSize;
		
		if (r.sceneManager.system.isMouseFar) drawFarSelection(r, posX, posY, blockSize);
		else drawNearSelection(r, posX, posY, blockSize);
		
		if (r.sceneManager.system.isKeyboardSprint) drawFlag(r, posX, posY, blockSize);
	}
	
	
	private static int getMousePosX(Renderer r) {
		double coordsOffsetX = offsetX(r);
		return (int)((r.sceneManager.system.mouseX - coordsOffsetX)*r.sceneManager.settings.blockSize);
	}
	private static int getMousePosY(Renderer r) {
		double coordsOffsetY = offsetY(r);
		return (int)((r.sceneManager.system.mouseY - coordsOffsetY)*r.sceneManager.settings.blockSize);
	}
	
	
	private static double offsetX(Renderer r) {
		return r.sceneManager.savable.charX - Double.valueOf(r.sceneManager.settings.screenWidth) / Double.valueOf(2 * r.sceneManager.settings.blockSize);
	}
	private static double offsetY(Renderer r) {
		return r.sceneManager.savable.charY - Double.valueOf(r.sceneManager.settings.screenHeight) / Double.valueOf(2 * r.sceneManager.settings.blockSize);
	}
	
	private static void drawFarSelection(Renderer r, int posX, int posY, int blockSize) {
		r.drawTexture(r.textureManager.getTexture("selectFar"), posX, posY, blockSize, blockSize);

	}
	private static void drawNearSelection(Renderer r, int posX, int posY, int blockSize) {
		r.drawTexture(r.textureManager.getTexture("select"), posX, posY, blockSize, blockSize);

	}
	private static void drawFlag(Renderer r, int posX, int posY, int blockSize) {
		r.drawTexture(r.textureManager.getTexture("selectFlag"), posX, posY - (int)(0.4375 * blockSize), (int)(blockSize * 1.25), (int)(blockSize*1.4375));
	}

}
