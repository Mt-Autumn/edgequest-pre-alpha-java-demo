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
			double tileWidth = Double.valueOf(sceneManager.screenWidth) / sceneManager.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(sceneManager.screenHeight) / sceneManager.blockSize / 2.0 + 1;
			if (sceneManager.blockGenerationLastTick || sceneManager.characterMoving) {
			blocksPerTick = 0;
		sceneManager.minTileX = (int) (sceneManager.charX - tileWidth - 1);
		sceneManager.maxTileX = (int) (sceneManager.charX + tileWidth);
		sceneManager.minTileY = (int) (sceneManager.charY - tileHeight - 1);
		sceneManager.maxTileY = (int) (sceneManager.charY + tileHeight);
		
		for(int i = sceneManager.minTileX - 2; i <= sceneManager.maxTileX + 1; i++) {
			for (int j = sceneManager.minTileY - 2; j <= sceneManager.maxTileY + 1; j++) {
				if (!sceneManager.map.containsKey(i + "," + j) && blocksPerTick < 1000) {
					terrainGenerator.generateBlock(i, j);
					blocksPerTick++;
				}
			}
		}
		sceneManager.blockGenerationLastTick = (blocksPerTick > 0);
		}
		try {
			Thread.sleep(sceneManager.tickLength);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	}
}
