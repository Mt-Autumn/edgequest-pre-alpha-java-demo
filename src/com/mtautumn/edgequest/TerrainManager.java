package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class TerrainManager extends Thread {
	DataManager dataManager;
	TerrainGenerator terrainGenerator;
	public TerrainManager(DataManager dataManager) {
		this.dataManager = dataManager;
		terrainGenerator = new TerrainGenerator(dataManager);
	}
	public void run() {
		int blocksPerTick = 0;
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					if (dataManager.system.blockGenerationLastTick || dataManager.system.characterMoving) {
						blocksPerTick = 0;
						for(int i = dataManager.system.minTileX - 2; i <= dataManager.system.maxTileX + 1 && blocksPerTick < 1000; i++) {
							for (int j = dataManager.system.minTileY - 2; j <= dataManager.system.maxTileY + 1; j++) {
								if (!dataManager.savable.map.containsKey(i + "," + j)) {
									terrainGenerator.generateBlock(i, j);
									blocksPerTick++;
								}
							}
						}
						dataManager.system.blockGenerationLastTick = (blocksPerTick > 0);
					}
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
