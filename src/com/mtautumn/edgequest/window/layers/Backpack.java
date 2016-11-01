package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Backpack {
	public static void draw(Renderer r) {
		r.fillRect(0, 0, r.dataManager.settings.screenWidth, r.dataManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		drawBackground(r);
		drawSpaces(r);	
	}
	
	private static void drawBackground(Renderer r) {
		r.dataManager.system.menuX = r.dataManager.settings.screenWidth / 2 - 375;
		r.dataManager.system.menuY = r.dataManager.settings.screenHeight/2 - 250;
		r.drawTexture(r.textureManager.getTexture("backpack"), r.dataManager.system.menuX, r.dataManager.system.menuY, 750,500);
	}
	
	private static void drawSpaces(Renderer r) {
		for (int i = 2; i < r.dataManager.savable.backpackItems.length; i++) {
			int posX = r.dataManager.system.menuX + (i - 2) * 64 + 37;
			for (int j = 0; j < r.dataManager.savable.backpackItems[i].length; j++) {
				int posY = r.dataManager.system.menuY + j * 65 + 94;
				Color.white.bind();
				try {
					r.drawTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.backpackItems[i][j].getItemID()).getItemImg(r.dataManager.savable.time), posX, posY, 48, 48);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (r.dataManager.savable.backpackItems[i][j].getItemCount() != 0) {
					r.backpackFont.drawString(posX, posY, "" + r.dataManager.savable.backpackItems[i][j].getItemCount(), Color.black);
				}
			}
		}
	}
	
}
