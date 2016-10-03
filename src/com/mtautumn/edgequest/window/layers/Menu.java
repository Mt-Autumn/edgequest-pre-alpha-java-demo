package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;

public class Menu {
	public static void draw(Renderer r) {
		r.fillRect(0, 0, r.sceneManager.settings.screenWidth, r.sceneManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		r.sceneManager.system.menuX = r.sceneManager.settings.screenWidth / 2 - 375;
		r.sceneManager.system.menuY = r.sceneManager.settings.screenHeight / 2 - 250;
		r.drawTexture(r.textureManager.getTexture("menuBackground"), r.sceneManager.system.menuX, r.sceneManager.system.menuY, 750,500);
		drawButtons(r);
	}
	
	private static void drawButtons(Renderer r) {
		for (int i = 0; i<r.menuButtonManager.buttonIDArray.size(); i++) {
			MenuButtonManager.MenuButton button = r.menuButtonManager.buttonIDArray.get(i);
			r.drawTexture(button.buttonImage, button.getPosX(r.sceneManager.system.menuX), button.getPosY(r.sceneManager.system.menuY), button.width, button.height);
		}
	}
}
