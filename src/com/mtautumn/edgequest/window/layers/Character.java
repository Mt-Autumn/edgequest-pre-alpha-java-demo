package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Character {
	public static void draw(Renderer r) {
		Color.white.bind();
		int blockSize = r.dataManager.settings.blockSize;
		r.drawTexture(r.textureManager.getTexture("character"), (int) ((r.dataManager.settings.screenWidth - blockSize) / 2.0), (int) ((r.dataManager.settings.screenHeight - blockSize) / 2.0), blockSize, blockSize, Float.valueOf(r.dataManager.savable.charDir) * 45.0f);
	}
}
