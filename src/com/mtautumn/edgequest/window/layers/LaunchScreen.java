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
		if (r.sceneManager.settings.screenWidth > 1.6 * r.sceneManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.sceneManager.settings.screenHeight - r.sceneManager.settings.screenWidth / 1.6) / 2, r.sceneManager.settings.screenWidth,(int)(r.sceneManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.sceneManager.settings.screenWidth - r.sceneManager.settings.screenHeight * 1.6)/2, 0, (int)(r.sceneManager.settings.screenHeight * 1.6),r.sceneManager.settings.screenHeight);

		}
	}

	private static void drawLogo(Renderer r) {
		r.drawTexture(r.textureManager.getTexture("launchScreenLogo"), (r.sceneManager.settings.screenWidth / 2 - 200), 80, 400, 48);
	}

	private static void drawButtons(Renderer r) {
		for (int i = 0; i<r.launchScreenManager.buttonIDArray.size(); i++) {
			MenuButton button = r.launchScreenManager.buttonIDArray.get(i);
			r.drawTexture(button.buttonImage, button.getPosX(r.sceneManager.settings.screenWidth), button.getPosY(r.sceneManager.settings.screenHeight), button.width, button.height);
		}
	}
}
