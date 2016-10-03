package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Character {
	public static void draw(Renderer r) {
		Color.white.bind();
		int blockSize = r.sceneManager.settings.blockSize;
		r.drawTexture(r.textureManager.getCharacter(r.sceneManager.savable.charDir), (int) ((r.sceneManager.settings.screenWidth - blockSize) / 2.0), (int) ((r.sceneManager.settings.screenHeight - blockSize) / 2.0), blockSize, blockSize);

	}
}
