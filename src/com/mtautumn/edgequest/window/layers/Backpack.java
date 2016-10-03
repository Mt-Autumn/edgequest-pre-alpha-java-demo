package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Backpack {
	public static void draw(Renderer r) {
		r.fillRect(0, 0, r.sceneManager.settings.screenWidth, r.sceneManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		drawBackground(r);
		drawSpaces(r);	
	}
	
	private static void drawBackground(Renderer r) {
		r.sceneManager.system.menuX = r.sceneManager.settings.screenWidth / 2 - 375;
		r.sceneManager.system.menuY = r.sceneManager.settings.screenHeight/2 - 250;
		r.drawTexture(r.textureManager.getTexture("backpack"), r.sceneManager.system.menuX, r.sceneManager.system.menuY, 750,500);
	}
	
	private static void drawSpaces(Renderer r) {
		for (int i = 0; i < r.sceneManager.savable.backpackItems.length; i++) {
			int posX = r.sceneManager.system.menuX + i * 64 + 37;
			for (int j = 0; j < r.sceneManager.savable.backpackItems[i].length; j++) {
				int posY = r.sceneManager.system.menuY + j * 65 + 94;
				try {
					r.drawTexture(r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.backpackItems[i][j].getItemID()).getItemImg(r.sceneManager.savable.time), posX, posY, 48, 48);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
