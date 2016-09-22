package com.mtautumn.edgequest;

public class BlockUpdateManager {
	SceneManager sceneManager;
	BlockInformation blockInfo = new BlockInformation();
	private int lightDiffuseDistance = 8;
	public BlockUpdateManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void addLightSource(int x, int y) {
		sceneManager.lightSourceMap.put(x + "," + y, true);
		sceneManager.playerStructuresMap.put(x + "," + y, blockInfo.getBlockID("torch"));
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
				if (closestLightSource <= lightDiffuseDistance) {
					updateLighting(i,j,1.0 - closestLightSource/Double.valueOf(lightDiffuseDistance));
				}
			}
		}
	}
	private void updateLighting(int x, int y, double brightness) {
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		sceneManager.lightMap.put(x + "," + y, brightness);
	}
	private boolean doesContainLightSource(int x, int y) {
		if (sceneManager.lightSourceMap.containsKey(x + "," + y)) {
			return sceneManager.lightSourceMap.get(x + "," + y);
		}
		return false;
	}
}
