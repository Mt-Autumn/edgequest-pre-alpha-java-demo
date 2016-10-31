package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.managers.LaunchScreenManager.MenuButton;

public class LaunchScreen {
	public static void draw(Renderer r) {
		Color.white.bind();

		drawBackground(r);
		drawLogo(r);
		drawButtons(r);
	}

	private static void drawBackground(Renderer r) {
		if (r.dataManager.settings.screenWidth > 1.6 * r.dataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.dataManager.settings.screenHeight - r.dataManager.settings.screenWidth / 1.6) / 2, r.dataManager.settings.screenWidth,(int)(r.dataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.dataManager.settings.screenWidth - r.dataManager.settings.screenHeight * 1.6)/2, 0, (int)(r.dataManager.settings.screenHeight * 1.6),r.dataManager.settings.screenHeight);

		}
	}

	private static void drawLogo(Renderer r) {
		r.drawTexture(r.textureManager.getTexture("launchScreenLogo"), (r.dataManager.settings.screenWidth / 2 - 200), 80, 400, 48);
	}

	private static void drawButtons(Renderer r) {
		for (int i = 0; i<r.launchScreenManager.buttonIDArray.size(); i++) {
			MenuButton button = r.launchScreenManager.buttonIDArray.get(i);
				r.drawTexture(button.buttonImage, button.getPosX(r.dataManager.settings.screenWidth), button.getPosY(r.dataManager.settings.screenHeight), button.width, button.height);
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(button.getPosX(r.dataManager.settings.screenWidth) + (button.width - width) / 2, button.getPosY(r.dataManager.settings.screenHeight) + (button.height - height) / 2, button.displayName);
		}
	}
}
