package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.managers.MenuButtonManager.MenuButton;
import com.mtautumn.edgequest.window.managers.MenuButtonManager.MenuPane;

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
		MenuPane menu = r.dataManager.menuButtonManager.getCurrentMenu();
		for (int i = 0; i < menu.getCount(); i++) {
			MenuButton button = menu.getButtons().get(i);
			if (button.visible) {
				r.drawTexture(button.buttonImage, button.getPosX(r.dataManager.system.menuX), button.getPosY(r.dataManager.system.menuY), button.width, button.height);
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(button.getPosX(r.dataManager.system.menuX) + (button.width - width) / 2, button.getPosY(r.dataManager.system.menuY) + (button.height - height) / 2, button.displayName);
			}
			if (menu.parent == null) {
				r.drawTexture(r.textureManager.getTexture("exit"), r.dataManager.system.menuX + r.dataManager.settings.BACK_BUTTON_PADDING, r.dataManager.system.menuY + r.dataManager.settings.BACK_BUTTON_PADDING, r.dataManager.settings.BACK_BUTTON_SIZE, r.dataManager.settings.BACK_BUTTON_SIZE);
			} else {
				r.drawTexture(r.textureManager.getTexture("back"), r.dataManager.system.menuX + r.dataManager.settings.BACK_BUTTON_PADDING, r.dataManager.system.menuY + r.dataManager.settings.BACK_BUTTON_PADDING, r.dataManager.settings.BACK_BUTTON_SIZE, r.dataManager.settings.BACK_BUTTON_SIZE);
			}
		}
	}
}
