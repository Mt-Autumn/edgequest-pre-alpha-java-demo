package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class OptionPane {
	public static void draw(Renderer r) {
		for (int i = 0; i < r.dataManager.system.inputText.size(); i++) {
			drawInput(r, r.dataManager.system.inputText.get(i), i);
		}
		for (int i = 0; i < r.dataManager.system.noticeText.size(); i++) {
			drawNotice(r, r.dataManager.system.noticeText.get(i), i);
		}
	}
	private static void drawInput(Renderer r, String text, int count) {
		if (r.dataManager.settings.screenWidth > 1.6 * r.dataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.dataManager.settings.screenHeight - r.dataManager.settings.screenWidth / 1.6) / 2, r.dataManager.settings.screenWidth,(int)(r.dataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.dataManager.settings.screenWidth - r.dataManager.settings.screenHeight * 1.6)/2, 0, (int)(r.dataManager.settings.screenHeight * 1.6),r.dataManager.settings.screenHeight);

		}
		int textX = (r.dataManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (r.dataManager.settings.screenHeight) / 2 - r.font2.getHeight(text) - 100;
		r.font2.drawString(textX, textY, text);
		int xPos = (r.dataManager.settings.screenWidth - 400) / 2;
		int yPos = (r.dataManager.settings.screenHeight - 60) / 2;
		r.drawTexture(r.textureManager.getTexture("inputField"), xPos, yPos, 400, 60);
		String inputFieldText = "";
		if (r.dataManager.system.inputTextResponse.size() > count) {
			inputFieldText = r.dataManager.system.inputTextResponse.get(count);
		} else {
			r.dataManager.system.inputTextResponse.add("");
		}
		if (System.currentTimeMillis() / 500 % 2 == 0) {
			inputFieldText = inputFieldText + "|";
		}
		if (r.dataManager.system.os == 2) { //adjusts font location for windows
			r.font2.drawString(xPos + 15, yPos + 5, inputFieldText);
		} else {
			r.font2.drawString(xPos + 15, yPos + 15, inputFieldText);
		}
		
	}
	private static void drawNotice(Renderer r, String text, int count) {
		if (r.dataManager.settings.screenWidth > 1.6 * r.dataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.dataManager.settings.screenHeight - r.dataManager.settings.screenWidth / 1.6) / 2, r.dataManager.settings.screenWidth,(int)(r.dataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.dataManager.settings.screenWidth - r.dataManager.settings.screenHeight * 1.6)/2, 0, (int)(r.dataManager.settings.screenHeight * 1.6),r.dataManager.settings.screenHeight);

		}
		int textX = (r.dataManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (r.dataManager.settings.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
	public static void closeOptionPane(DataManager sceneManager) {
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
