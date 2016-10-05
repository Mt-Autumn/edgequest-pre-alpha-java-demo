package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class CharacterEffects {
	public static void draw(Renderer r) {
		Color.white.bind();
		drawWaterSplash(r);
	}

	private static void drawWaterSplash(Renderer r) {
		int blockSize =  r.dataManager.settings.blockSize;
		if (r.dataManager.system.blockIDMap.get((short)r.getCharaterBlockInfo()[0]).isLiquid && r.getCharaterBlockInfo()[1] == 0.0) {
			r.drawTexture(r.textureManager.getAnimatedTexture("waterSplash", r.dataManager), (int) ((r.dataManager.settings.screenWidth - blockSize) / 2.0), (int) ((r.dataManager.settings.screenHeight - blockSize) / 2.0), blockSize, blockSize);
		}
	}

}
