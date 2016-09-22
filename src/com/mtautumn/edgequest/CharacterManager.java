package com.mtautumn.edgequest;

public class CharacterManager extends Thread{
	SceneManager sceneManager;
	BlockUpdateManager blockUpdateManager;
	BlockInformation blockInfo = new BlockInformation();
	public CharacterManager(SceneManager scnMgr, BlockUpdateManager bum) {
		sceneManager = scnMgr;
		blockUpdateManager = bum;
	}
	public void charPlaceTorch() {
		int charX = (int) Math.floor(sceneManager.charX);
		int charY = (int) Math.floor(sceneManager.charY);
		if (sceneManager.map.containsKey(charX + "," + charY)) {
			if (getCharaterBlockInfo()[0] != blockInfo.getBlockID("water")) {
				blockUpdateManager.addLightSource((int) Math.floor(sceneManager.charX), (int) Math.floor(sceneManager.charY));
			}
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(sceneManager.charX);
		int charY = (int) Math.floor(sceneManager.charY);
		if (sceneManager.map.containsKey(charX + "," + charY)) {
			blockInfo[0] = sceneManager.map.get(charX + "," + charY);
		}
		if (sceneManager.playerStructuresMap.containsKey(charX + "," + charY)) {
			blockInfo[1] = sceneManager.playerStructuresMap.get(charX + "," + charY);
		}
		if (sceneManager.biomeMapFiltered.containsKey(charX + "," + charY)) {
			blockInfo[2] = sceneManager.biomeMapFiltered.get(charX + "," + charY);
		}
		if (sceneManager.lightMap.containsKey(charX + "," + charY)) {
			blockInfo[3] = sceneManager.lightMap.get(charX + "," + charY);
		}
		return blockInfo;
	}
	public void run() {
		long lastUpdate = System.currentTimeMillis();
		while (true) {
			double moveInterval = Double.valueOf(System.currentTimeMillis() - lastUpdate) / 600.0;
			lastUpdate = System.currentTimeMillis();
			double charYOffset = 0.0;
			double charXOffset = 0.0;
			if (sceneManager.isWPressed) {
				charYOffset -= moveInterval;
			}
			if (sceneManager.isDPressed) {
				charXOffset += moveInterval;
			}
			if (sceneManager.isSPressed) {
				charYOffset += moveInterval;
			}
			if (sceneManager.isAPressed) {
				charXOffset -= moveInterval;
			}
				if (charXOffset != 0 && charYOffset != 0) {
					charXOffset *= 0.70710678118;
					charYOffset *= 0.70710678118;
				}
				if (sceneManager.isShiftPressed) {
					charXOffset *= 2.0;
					charYOffset *= 2.0;
				}
				if (getCharaterBlockInfo()[0] == blockInfo.getBlockID("water") && getCharaterBlockInfo()[1] == 0.0) {
					charXOffset /= 1.7;
					charYOffset /= 1.7;
				}
				
				sceneManager.charX += charXOffset;
				sceneManager.charY += charYOffset;
				if(charYOffset < 0 && charXOffset == 0) {
					sceneManager.charDir = 0;
				} else if (charYOffset < 0 && charXOffset < 0) {
					sceneManager.charDir = 7;
				} else if (charYOffset < 0 && charXOffset > 0) {
					sceneManager.charDir = 1;
				} else if (charYOffset == 0 && charXOffset < 0) {
					sceneManager.charDir = 6;
				} else if (charYOffset == 0 && charXOffset > 0) {
					sceneManager.charDir = 2;
				} else if (charYOffset > 0 && charXOffset < 0) {
					sceneManager.charDir = 5;
				} else if (charYOffset > 0 && charXOffset == 0) {
					sceneManager.charDir = 4;
				} else if (charYOffset > 0 && charXOffset > 0) {
					sceneManager.charDir = 3;
				}
				sceneManager.characterMoving = (charXOffset != 0 || charYOffset != 0);
			try {
				Thread.sleep(sceneManager.tickLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
