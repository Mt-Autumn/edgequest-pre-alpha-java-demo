package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.updates.BlockUpdateManager;

public class CharacterManager extends Thread{
	DataManager dataManager;
	BlockUpdateManager blockUpdateManager;
	public CharacterManager(DataManager dataManager) {
		this.dataManager = dataManager;
		blockUpdateManager = dataManager.blockUpdateManager;
	}
	public void charPlaceTorch() {
		int charX = (int) Math.floor(dataManager.savable.charX);
		int charY = (int) Math.floor(dataManager.savable.charY);
		if (!dataManager.savable.playerStructuresMap.containsKey(charX + "," + charY)) {
			if ((short)getCharaterBlockInfo()[0] != dataManager.system.blockNameMap.get("water").getID()) {
				dataManager.savable.playerStructuresMap.put(charX + "," + charY, dataManager.system.blockNameMap.get("torch").getID());
				blockUpdateManager.lighting.update(charX, charY);
			}
		} else {
			dataManager.savable.playerStructuresMap.remove(charX + "," + charY);
			blockUpdateManager.lighting.update(charX, charY);
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(dataManager.savable.charX);
		int charY = (int) Math.floor(dataManager.savable.charY);
		if (dataManager.savable.map.containsKey(charX + "," + charY)) {
			blockInfo[0] = dataManager.savable.map.get(charX + "," + charY);
		}
		if (dataManager.savable.playerStructuresMap.containsKey(charX + "," + charY)) {
			blockInfo[1] = dataManager.savable.playerStructuresMap.get(charX + "," + charY);
		}
		if (dataManager.savable.lightMap.containsKey(charX + "," + charY)) {
			blockInfo[3] = dataManager.savable.lightMap.get(charX + "," + charY);
		}
		return blockInfo;
	}
	public void run() {
		long lastUpdate = System.currentTimeMillis();

		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					double moveInterval = Double.valueOf(System.currentTimeMillis() - lastUpdate) / 1000.0 * dataManager.settings.moveSpeed;
					lastUpdate = System.currentTimeMillis();
					double charYOffset = 0.0;
					double charXOffset = 0.0;
					if (dataManager.system.isKeyboardUp) {
						charYOffset -= moveInterval;
					}
					if (dataManager.system.isKeyboardRight) {
						charXOffset += moveInterval;
					}
					if (dataManager.system.isKeyboardDown) {
						charYOffset += moveInterval;
					}
					if (dataManager.system.isKeyboardLeft) {
						charXOffset -= moveInterval;
					}
					if (charXOffset != 0 && charYOffset != 0) {
						charXOffset *= 0.70710678118;
						charYOffset *= 0.70710678118;
					}
					if (dataManager.system.isKeyboardSprint) {
						charXOffset *= 2.0;
						charYOffset *= 2.0;
					}
					if (dataManager.system.blockIDMap.get((short)getCharaterBlockInfo()[0]).isLiquid && getCharaterBlockInfo()[1] == 0.0) {
						charXOffset /= 1.7;
						charYOffset /= 1.7;

					}
					if (checkMoveProposal(charXOffset, true)) {
						dataManager.savable.charX += charXOffset;
					}
					if (checkMoveProposal(charYOffset, false)) {
						dataManager.savable.charY += charYOffset;
					}
					if(charYOffset < 0 && charXOffset == 0) {
						dataManager.savable.charDir = 0;
					} else if (charYOffset < 0 && charXOffset < 0) {
						dataManager.savable.charDir = 7;
					} else if (charYOffset < 0 && charXOffset > 0) {
						dataManager.savable.charDir = 1;
					} else if (charYOffset == 0 && charXOffset < 0) {
						dataManager.savable.charDir = 6;
					} else if (charYOffset == 0 && charXOffset > 0) {
						dataManager.savable.charDir = 2;
					} else if (charYOffset > 0 && charXOffset < 0) {
						dataManager.savable.charDir = 5;
					} else if (charYOffset > 0 && charXOffset == 0) {
						dataManager.savable.charDir = 4;
					} else if (charYOffset > 0 && charXOffset > 0) {
						dataManager.savable.charDir = 3;
					}
					dataManager.system.characterMoving = (charXOffset != 0 || charYOffset != 0);
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private boolean checkMoveProposal(double charOffset, boolean isX) {
		int charX;
		int charY;
		if (isX) {
			charX = (int) Math.floor(charOffset + dataManager.savable.charX);
			charY = (int) Math.floor(dataManager.savable.charY);
		} else {
			charY = (int) Math.floor(charOffset + dataManager.savable.charY);
			charX = (int) Math.floor(dataManager.savable.charX);
		}
		if (dataManager.savable.playerStructuresMap.containsKey(charX + "," + charY)) {
			return (dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(charX + "," + charY)).isPassable);
		}
		return true;
	}
}
