package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class EdgeQuest {
	public static DataManager dataManager = new DataManager();
	public static void main(String[] args) throws InterruptedException {
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
