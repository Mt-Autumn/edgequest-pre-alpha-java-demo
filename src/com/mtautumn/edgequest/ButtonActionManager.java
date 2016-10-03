package com.mtautumn.edgequest;

import java.io.IOException;

public class ButtonActionManager extends Thread {
	SceneManager sceneManager;
	public ButtonActionManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}
	public void run() {
		while (true) {
			runButtonActions();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void runButtonActions() {
		int size = sceneManager.system.buttonActionQueue.size();
		if (size > 0) {
			runButtonAction(sceneManager.system.buttonActionQueue.get(size - 1), size - 1);
		}
	}
	
	private void runButtonAction(int id, int index) {
		sceneManager.system.buttonActionQueue.remove(index);
		switch (id) {
		case 1: //New Game
			try {
				long seed = Long.parseLong(getInputText("Enter a Seed Number:"));
				sceneManager.savable.seed = seed;
				sceneManager.system.biomeMap.clear();
				sceneManager.savable.biomeMapFiltered.clear();
				sceneManager.savable.playerStructuresMap.clear();
				sceneManager.savable.map.clear();
				sceneManager.savable.lightMap.clear();
				sceneManager.savable.footPrints.clear();
				sceneManager.system.blockGenerationLastTick = true;
				sceneManager.system.isGameOnLaunchScreen = false;
				sceneManager.system.isLaunchScreenLoaded = false;
			} catch (Exception e) {
				setNoticeText("Seeds should be whole numbers");
			}
			break;
		case 2: //load game
			try {
				GameSaves.loadGame(getInputText("Enter a File Name:"), sceneManager);
				sceneManager.system.isGameOnLaunchScreen = false;
				sceneManager.system.isLaunchScreenLoaded = false;
			} catch (Exception e) {
				setNoticeText("Could not load game");
				e.printStackTrace();
			}
			break;
		case 3:
			String ans = getInputText("Enter FPS Target:");
			try {
				int fps = Integer.parseInt(ans);
				if (fps > 0) {
					sceneManager.settings.targetFPS = fps;
				} else {
					setNoticeText("FPS too low");
				}
			} catch (Exception e) {
				setNoticeText("FPS not valid");
			}
			break;
		case 4:
			String fileSaveName = getInputText("World Name:");
			try {
				GameSaves.saveGame(fileSaveName, sceneManager);
			} catch (IOException e) {
				setNoticeText("Unable to save game");
			}
			break;
		case 5:
			String fileLoadName = getInputText("World Name:");
			try {
				GameSaves.loadGame(fileLoadName, sceneManager);
			} catch (Exception e) {
				setNoticeText("Unable to load game");
				e.printStackTrace();
			}
			break;
		case 6:
			if (sceneManager.settings.isFullScreen) {
				sceneManager.system.setWindowed = true;
			} else {
				sceneManager.system.setFullScreen = true;
			}
		default:
			break;
		}
	}
	private String getInputText(String text) {
		sceneManager.system.inputText.add(text);
		int length = sceneManager.system.inputText.size();
		while (sceneManager.system.inputTextResponse.size() < sceneManager.system.inputText.size()) {
			sceneManager.system.inputTextResponse.add("");
		}
		sceneManager.system.inputTextResponse.set(sceneManager.system.inputTextResponse.size() - 1, "");
		while (sceneManager.system.inputText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return sceneManager.system.lastInputMessage;
	}
	
	private void setNoticeText(String text) {
		sceneManager.system.noticeText.add(text);
		int length = sceneManager.system.noticeText.size();
		while (sceneManager.system.noticeText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
