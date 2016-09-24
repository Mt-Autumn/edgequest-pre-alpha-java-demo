package com.mtautumn.edgequest;

public class TerrainManager extends Thread {
	SceneManager sceneManager;
	TerrainGenerator terrainGenerator;
	public TerrainManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
		terrainGenerator = new TerrainGenerator(scnMgr);
	}
	public void run() {
		int blocksPerTick = 0;
		while (true) {
			double tileWidth = Double.valueOf(sceneManager.settings.screenWidth) / sceneManager.settings.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(sceneManager.settings.screenHeight) / sceneManager.settings.blockSize / 2.0 + 1;
			if (sceneManager.system.blockGenerationLastTick || sceneManager.system.characterMoving) {
			blocksPerTick = 0;
		sceneManager.system.minTileX = (int) (sceneManager.system.charX - tileWidth - 1);
		sceneManager.system.maxTileX = (int) (sceneManager.system.charX + tileWidth);
		sceneManager.system.minTileY = (int) (sceneManager.system.charY - tileHeight - 1);
		sceneManager.system.maxTileY = (int) (sceneManager.system.charY + tileHeight);
		
		for(int i = sceneManager.system.minTileX - 2; i <= sceneManager.system.maxTileX + 1; i++) {
			for (int j = sceneManager.system.minTileY - 2; j <= sceneManager.system.maxTileY + 1; j++) {
				if (!sceneManager.world.map.containsKey(i + "," + j) && blocksPerTick < 1000) {
					terrainGenerator.generateBlock(i, j);
					blocksPerTick++;
				}
			}
		}
		sceneManager.system.blockGenerationLastTick = (blocksPerTick > 0);
		}
		try {
			Thread.sleep(sceneManager.settings.tickLength);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	}
}
