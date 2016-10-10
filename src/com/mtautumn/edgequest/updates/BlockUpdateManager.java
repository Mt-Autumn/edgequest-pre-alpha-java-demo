package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.data.DataManager;

public class BlockUpdateManager extends Thread {
	DataManager dataManager;
	public UpdateLighting lighting;
	public UpdateMining mining;
	public UpdateFootprints footprints;
	public BlockUpdateManager(DataManager dataManager) {
		this.dataManager = dataManager;
		lighting = new UpdateLighting(dataManager);
		mining = new UpdateMining(dataManager);
		footprints = new UpdateFootprints(dataManager);
	}
	public void run() {
		int i = 0;
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					i++;
					if (i % 30 == 0) melt();
					mining.update();
					footprints.update();
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
