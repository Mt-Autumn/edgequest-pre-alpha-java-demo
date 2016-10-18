package com.mtautumn.edgequest;

import java.io.IOException;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.GameSaves;

public class ButtonActionManager extends Thread {
	DataManager dataManager;
	public ButtonActionManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		while (dataManager.system.running) {
			runButtonActions();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void runButtonActions() {
		int size = dataManager.system.buttonActionQueue.size();
		if (size > 0) {
			runButtonAction(dataManager.system.buttonActionQueue.get(size - 1), size - 1);
		}
	}
	
	private void runButtonAction(int id, int index) {
		dataManager.system.buttonActionQueue.remove(index);
		switch (id) {
		case 1: //New Game
			try {
				long seed = Long.parseLong(getInputText("Enter a Seed Number:"));
				dataManager.savable.seed = seed;
				dataManager.terrainManager.terrainGenerator.clearCache();
				dataManager.savable.playerStructuresMap.clear();
				dataManager.savable.map.clear();
				dataManager.savable.lightMap.clear();
				dataManager.savable.footPrints.clear();
				dataManager.system.blockGenerationLastTick = true;
				dataManager.system.isGameOnLaunchScreen = false;
				dataManager.system.isLaunchScreenLoaded = false;
			} catch (Exception e) {
				setNoticeText("Seeds should be whole numbers");
			}
			break;
		case 2: //load game
			try {
				GameSaves.loadGame(getInputText("Enter a File Name:"), dataManager);
				dataManager.system.isGameOnLaunchScreen = false;
				dataManager.system.isLaunchScreenLoaded = false;
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
					dataManager.settings.targetFPS = fps;
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
				GameSaves.saveGame(fileSaveName, dataManager);
			} catch (IOException e) {
				setNoticeText("Unable to save game");
			}
			break;
		case 5:
			String fileLoadName = getInputText("World Name:");
			try {
				GameSaves.loadGame(fileLoadName, dataManager);
			} catch (Exception e) {
				setNoticeText("Unable to load game");
				e.printStackTrace();
			}
			break;
		case 6:
			dataManager.settings.isFullScreen = !dataManager.settings.isFullScreen;
			if (dataManager.settings.isFullScreen) {
				dataManager.menuButtonManager.getButtonFromName("fullScreen").visible = false;
				dataManager.menuButtonManager.getButtonFromName("windowed").visible = true;
			} else {
				dataManager.menuButtonManager.getButtonFromName("fullScreen").visible = true;
				dataManager.menuButtonManager.getButtonFromName("windowed").visible = false;
			}
			break;
		case 7:
			dataManager.settings.vSyncOn = !dataManager.settings.vSyncOn;
			if (dataManager.settings.vSyncOn) {
				dataManager.menuButtonManager.getButtonFromName("vSyncOn").visible = false;
				dataManager.menuButtonManager.getButtonFromName("vSyncOff").visible = true;
			} else {
				dataManager.menuButtonManager.getButtonFromName("vSyncOn").visible = true;
				dataManager.menuButtonManager.getButtonFromName("vSyncOff").visible = false;
			}
			break;
		case 8:
			dataManager.system.running = false;
			break;
		default:
			break;
		}
	}
	private String getInputText(String text) {
		dataManager.system.inputText.add(text);
		int length = dataManager.system.inputText.size();
		while (dataManager.system.inputTextResponse.size() < dataManager.system.inputText.size()) {
			dataManager.system.inputTextResponse.add("");
		}
		dataManager.system.inputTextResponse.set(dataManager.system.inputTextResponse.size() - 1, "");
		while (dataManager.system.inputText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return dataManager.system.lastInputMessage;
	}
	
	private void setNoticeText(String text) {
		dataManager.system.noticeText.add(text);
		int length = dataManager.system.noticeText.size();
		while (dataManager.system.noticeText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
