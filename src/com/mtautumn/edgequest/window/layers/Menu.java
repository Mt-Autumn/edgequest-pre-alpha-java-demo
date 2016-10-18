package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;

public class Menu {
	public static void draw(Renderer r) {
		r.fillRect(0, 0, r.dataManager.settings.screenWidth, r.dataManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		r.dataManager.system.menuX = r.dataManager.settings.screenWidth / 2 - 375;
		r.dataManager.system.menuY = r.dataManager.settings.screenHeight / 2 - 250;
		r.drawTexture(r.textureManager.getTexture("menuBackground"), r.dataManager.system.menuX, r.dataManager.system.menuY, 750,500);
		drawButtons(r);
	}

	private static void drawButtons(Renderer r) {
		for (int i = 0; i<r.dataManager.menuButtonManager.buttonIDArray.size(); i++) {
			MenuButtonManager.MenuButton button = r.dataManager.menuButtonManager.buttonIDArray.get(i);
			if (button.visible) {
				r.drawTexture(button.buttonImage, button.getPosX(r.dataManager.system.menuX), button.getPosY(r.dataManager.system.menuY), button.width, button.height);
			}
		}
	}
}
