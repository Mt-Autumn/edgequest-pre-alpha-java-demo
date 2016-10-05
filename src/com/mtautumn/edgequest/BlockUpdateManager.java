package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class BlockUpdateManager extends Thread {
	DataManager dataManager;
	private int lightDiffuseDistance = 8;
	public BlockUpdateManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void updateLighting(int x, int y) {
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				double closestLightSource = lightDiffuseDistance + 1;
				for (int k = i - lightDiffuseDistance; k <= i + lightDiffuseDistance; k++) {
					for (int l = j - lightDiffuseDistance; l <= j + lightDiffuseDistance; l++) {
						if (doesContainLightSource(k,l)) {
							double distance = Math.sqrt(Math.pow(k-i, 2)+Math.pow(l-j, 2));
							if (distance < closestLightSource) {
								closestLightSource = distance;
							}
						}
					}
				}
				updateLighting(i,j,1.0 - closestLightSource/Double.valueOf(lightDiffuseDistance));
			}
		}
	}
	public void run() {
		int i = 0;
		while (true) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					i++;
					if (i % 30 == 0) melt();
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void updateLighting(int x, int y, double brightness) {
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		dataManager.savable.lightMap.put(x + "," + y, (byte)(brightness*255.0-128.0));
	}
	private boolean doesContainLightSource(int x, int y) {
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y)).isLightSource;
		}
		return false;
	}
	private void melt() {
		for(int x = dataManager.system.minTileX; x <= dataManager.system.maxTileX; x++) {
			for(int y = dataManager.system.minTileY; y <= dataManager.system.maxTileY; y++) {
				if (dataManager.savable.map.containsKey(x+","+y)) {
					if (dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y)).melts) {
						double brightness = 0;
						if (dataManager.savable.lightMap.containsKey(x+","+y)) {
							brightness = Double.valueOf(((int) dataManager.savable.lightMap.get(x + "," + y) + 128)) / 255.0;
						}
						if (brightness > 0.7) {
							if (1 - Math.random() < (brightness - 0.7) / 50.0) {
								dataManager.savable.map.put(x+","+y, dataManager.system.blockNameMap.get(dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y)).meltsInto).getID());
							}
						}
					}
				}
			}	
		}
	}
}
