package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class EdgeQuest {
	public static DataManager dataManager = new DataManager();
	public static void main(String[] args) throws InterruptedException {
		byte os = (byte) System.getProperty("os.name").toLowerCase().charAt(0);
		switch (os) {
		case 108:
			System.out.println("Setting OS to GNU/Linux");
			dataManager.system.os = 0;
			break;
		case 109:
			System.out.println("Setting OS to macOS");
			dataManager.system.os = 1;
			break;
		case 119:
			System.out.println("Setting OS to Windows");
			dataManager.system.os = 2;
			break;
		default:
			break;
		}
		dataManager.characterManager.start();
		dataManager.terrainManager.start();
		dataManager.rendererManager.start();
		dataManager.gameClock.start();
		dataManager.animationClock.start();
		dataManager.blockUpdateManager.start();
		dataManager.autoCharacterWalk.start();
		dataManager.buttonActionManager.start();
	}
}
