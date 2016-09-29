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
			try {
				if (!sceneManager.system.isGameOnLaunchScreen) {
					if (sceneManager.system.blockGenerationLastTick || sceneManager.system.characterMoving) {
						blocksPerTick = 0;
						for(int i = sceneManager.system.minTileX - 2; i <= sceneManager.system.maxTileX + 1 && blocksPerTick < 1000; i++) {
							for (int j = sceneManager.system.minTileY - 2; j <= sceneManager.system.maxTileY + 1; j++) {
								if (!sceneManager.savable.map.containsKey(i + "," + j)) {
									terrainGenerator.generateBlock(i, j);
									blocksPerTick++;
								}
							}
						}
						sceneManager.system.blockGenerationLastTick = (blocksPerTick > 0);
					}
				}
				Thread.sleep(sceneManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
