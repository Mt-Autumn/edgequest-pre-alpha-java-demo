package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.SceneManager;
import com.mtautumn.edgequest.window.Renderer;

public class OptionPane {
	public static void draw(Renderer r) {
		for (int i = 0; i < r.sceneManager.system.inputText.size(); i++) {
			drawInput(r, r.sceneManager.system.inputText.get(i), i);
		}
		for (int i = 0; i < r.sceneManager.system.noticeText.size(); i++) {
			drawNotice(r, r.sceneManager.system.noticeText.get(i), i);
		}
	}
	private static void drawInput(Renderer r, String text, int count) {
		if (r.sceneManager.settings.screenWidth > 1.6 * r.sceneManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.sceneManager.settings.screenHeight - r.sceneManager.settings.screenWidth / 1.6) / 2, r.sceneManager.settings.screenWidth,(int)(r.sceneManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.sceneManager.settings.screenWidth - r.sceneManager.settings.screenHeight * 1.6)/2, 0, (int)(r.sceneManager.settings.screenHeight * 1.6),r.sceneManager.settings.screenHeight);

		}
		int textX = (r.sceneManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (r.sceneManager.settings.screenHeight) / 2 - r.font2.getHeight(text) - 100;
		r.font2.drawString(textX, textY, text);
		int xPos = (r.sceneManager.settings.screenWidth - 400) / 2;
		int yPos = (r.sceneManager.settings.screenHeight - 60) / 2;
		r.drawTexture(r.textureManager.getTexture("inputField"), xPos, yPos, 400, 60);
		String inputFieldText = "";
		if (r.sceneManager.system.inputTextResponse.size() > count) {
			inputFieldText = r.sceneManager.system.inputTextResponse.get(count);
		} else {
			r.sceneManager.system.inputTextResponse.add("");
		}
		if (System.currentTimeMillis() / 500 % 2 == 0) {
			inputFieldText = inputFieldText + "|";
		}
		r.font2.drawString(xPos + 15, yPos + 15, inputFieldText);
		
	}
	private static void drawNotice(Renderer r, String text, int count) {
		if (r.sceneManager.settings.screenWidth > 1.6 * r.sceneManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.sceneManager.settings.screenHeight - r.sceneManager.settings.screenWidth / 1.6) / 2, r.sceneManager.settings.screenWidth,(int)(r.sceneManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.sceneManager.settings.screenWidth - r.sceneManager.settings.screenHeight * 1.6)/2, 0, (int)(r.sceneManager.settings.screenHeight * 1.6),r.sceneManager.settings.screenHeight);

		}
		int textX = (r.sceneManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (r.sceneManager.settings.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
	public static void closeOptionPane(SceneManager sceneManager) {
		if (sceneManager.system.noticeText.size() > 0) {
			int index = sceneManager.system.noticeText.size() - 1;
			sceneManager.system.noticeText.remove(index);
		} else {
			int index = sceneManager.system.inputText.size() - 1;
			sceneManager.system.inputText.remove(index);
			sceneManager.system.lastInputMessage = sceneManager.system.inputTextResponse.get(index);
			sceneManager.system.inputTextResponse.remove(index);
		}
	}
}
