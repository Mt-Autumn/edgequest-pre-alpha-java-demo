package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class MiniMap {
	public static final int MAP_WIDTH = 125;
	public static final int MAP_HEIGHT = 125;
	public static void draw(Renderer r) {
		Color.white.bind();
		drawBackground(r);
	}
	
	private static void drawBackground(Renderer r) {
		int xPos = r.dataManager.settings.screenWidth - MAP_WIDTH;
		r.drawTexture(r.textureManager.getTexture("miniMap"), xPos, 0, MAP_WIDTH, MAP_HEIGHT);
	}
	
}
