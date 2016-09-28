package com.mtautumn.edgequest;

public class CharacterManager extends Thread{
	SceneManager sceneManager;
	BlockUpdateManager blockUpdateManager;
	double lastFootX = 0.0;
	double lastFootY = 0.0;
	public CharacterManager(SceneManager scnMgr, BlockUpdateManager bum) {
		sceneManager = scnMgr;
		blockUpdateManager = bum;
	}
	public void charPlaceTorch() {
		int charX = (int) Math.floor(sceneManager.system.charX);
		int charY = (int) Math.floor(sceneManager.system.charY);
		if (!sceneManager.world.playerStructuresMap.containsKey(charX + "," + charY)) {
			if ((short)getCharaterBlockInfo()[0] != sceneManager.system.blockNameMap.get("water").getID()) {
				sceneManager.world.playerStructuresMap.put(charX + "," + charY, sceneManager.system.blockNameMap.get("torch").getID());
				blockUpdateManager.updateLighting(charX, charY);
			}
		} else {
			sceneManager.world.playerStructuresMap.remove(charX + "," + charY);
			blockUpdateManager.updateLighting(charX, charY);
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(sceneManager.system.charX);
		int charY = (int) Math.floor(sceneManager.system.charY);
		if (sceneManager.world.map.containsKey(charX + "," + charY)) {
			blockInfo[0] = sceneManager.world.map.get(charX + "," + charY);
		}
		if (sceneManager.world.playerStructuresMap.containsKey(charX + "," + charY)) {
			blockInfo[1] = sceneManager.world.playerStructuresMap.get(charX + "," + charY);
		}
		if (sceneManager.world.biomeMapFiltered.containsKey(charX + "," + charY)) {
			blockInfo[2] = sceneManager.world.biomeMapFiltered.get(charX + "," + charY);
		}
		if (sceneManager.world.lightMap.containsKey(charX + "," + charY)) {
			blockInfo[3] = sceneManager.world.lightMap.get(charX + "," + charY);
		}
		return blockInfo;
	}
	public void run() {
		long lastUpdate = System.currentTimeMillis();

		while (true) {
			try {
				if (!sceneManager.system.isGameOnLaunchScreen) {
					updateFootprints();
					double moveInterval = Double.valueOf(System.currentTimeMillis() - lastUpdate) / 600.0;
					lastUpdate = System.currentTimeMillis();
					double charYOffset = 0.0;
					double charXOffset = 0.0;
					if (sceneManager.system.isKeyboardUp) {
						charYOffset -= moveInterval;
					}
					if (sceneManager.system.isKeyboardRight) {
						charXOffset += moveInterval;
					}
					if (sceneManager.system.isKeyboardDown) {
						charYOffset += moveInterval;
					}
					if (sceneManager.system.isKeyboardLeft) {
						charXOffset -= moveInterval;
					}
					if (charXOffset != 0 && charYOffset != 0) {
						charXOffset *= 0.70710678118;
						charYOffset *= 0.70710678118;
					}
					if (sceneManager.system.isKeyboardSprint) {
						charXOffset *= 2.0;
						charYOffset *= 2.0;
					}
					if (sceneManager.system.blockIDMap.get((short)getCharaterBlockInfo()[0]).isLiquid && getCharaterBlockInfo()[1] == 0.0) {
						charXOffset /= 1.7;
						charYOffset /= 1.7;
						
					}

					sceneManager.system.charX += charXOffset;
					sceneManager.system.charY += charYOffset;
					if(charYOffset < 0 && charXOffset == 0) {
						sceneManager.system.charDir = 0;
					} else if (charYOffset < 0 && charXOffset < 0) {
						sceneManager.system.charDir = 7;
					} else if (charYOffset < 0 && charXOffset > 0) {
						sceneManager.system.charDir = 1;
					} else if (charYOffset == 0 && charXOffset < 0) {
						sceneManager.system.charDir = 6;
					} else if (charYOffset == 0 && charXOffset > 0) {
						sceneManager.system.charDir = 2;
					} else if (charYOffset > 0 && charXOffset < 0) {
						sceneManager.system.charDir = 5;
					} else if (charYOffset > 0 && charXOffset == 0) {
						sceneManager.system.charDir = 4;
					} else if (charYOffset > 0 && charXOffset > 0) {
						sceneManager.system.charDir = 3;
					}
					sceneManager.system.characterMoving = (charXOffset != 0 || charYOffset != 0);
				}
				Thread.sleep(sceneManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void updateFootprints() {
		int charX = (int) Math.floor(sceneManager.system.charX);
		int charY = (int) Math.floor(sceneManager.system.charY);
		if (sceneManager.world.map.containsKey(charX + "," + charY)) {
			if (sceneManager.system.blockIDMap.get(sceneManager.world.map.get(charX + "," + charY)).canHavePrints) {
				if (Math.sqrt(Math.pow(sceneManager.system.charX - lastFootX, 2)+Math.pow(sceneManager.system.charY - lastFootY, 2)) > 0.7) {
					lastFootX = sceneManager.system.charX;
					lastFootY = sceneManager.system.charY;
					sceneManager.world.footPrints.add(new FootPrint(sceneManager.system.charX, sceneManager.system.charY, sceneManager.system.charDir));
				}
			}
		}
		for (int i = 0; i < sceneManager.world.footPrints.size(); i++) {
			sceneManager.world.footPrints.get(i).opacity -= 0.001;
			if (sceneManager.world.footPrints.get(i).opacity <= 0) {
				sceneManager.world.footPrints.remove(i);
			}

		}
	}
}
