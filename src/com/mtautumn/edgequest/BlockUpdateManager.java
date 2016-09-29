package com.mtautumn.edgequest;

public class BlockUpdateManager extends Thread {
	SceneManager sceneManager;
	private int lightDiffuseDistance = 8;
	public BlockUpdateManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
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
				if (!sceneManager.system.isGameOnLaunchScreen) {
					i++;
					if (i % 30 == 0) melt();
				}
				Thread.sleep(sceneManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void updateLighting(int x, int y, double brightness) {
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		sceneManager.savable.lightMap.put(x + "," + y, (byte)(brightness*255.0-128.0));
	}
	private boolean doesContainLightSource(int x, int y) {
		if (sceneManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(x + "," + y)).isLightSource;
		}
		return false;
	}
	private void melt() {
		for(int x = sceneManager.system.minTileX; x <= sceneManager.system.maxTileX; x++) {
			for(int y = sceneManager.system.minTileY; y <= sceneManager.system.maxTileY; y++) {
				if (sceneManager.savable.map.containsKey(x+","+y)) {
					if (sceneManager.system.blockIDMap.get(sceneManager.savable.map.get(x + "," + y)).melts) {
						double brightness = 0;
						if (sceneManager.savable.lightMap.containsKey(x+","+y)) {
							brightness = Double.valueOf(((int) sceneManager.savable.lightMap.get(x + "," + y) + 128)) / 255.0;
						}
						if (brightness > 0.7) {
							if (1 - Math.random() < (brightness - 0.7) / 50.0) {
								sceneManager.savable.map.put(x+","+y, sceneManager.system.blockNameMap.get(sceneManager.system.blockIDMap.get(sceneManager.savable.map.get(x + "," + y)).meltsInto).getID());
							}
						}
					}
				}
			}	
		}
	}
}
