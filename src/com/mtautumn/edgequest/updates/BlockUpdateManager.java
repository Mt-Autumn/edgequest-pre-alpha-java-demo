/* Checks to see what blocks need to be updated each tick. This includes
 * lighting, block breaks, footprints, etc.
 */
package com.mtautumn.edgequest.updates;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class BlockUpdateManager extends Thread {
	DataManager dataManager;
	public UpdateLighting lighting;
	public UpdateMining mining;
	public UpdateFootprints footprints;
	public UpdateBlockPlace blockPlace;
	ArrayList<Point2D> lightingQueue = new ArrayList<Point2D>();
	public BlockUpdateManager(DataManager dataManager) {
		this.dataManager = dataManager;
		lighting = new UpdateLighting(dataManager);
		mining = new UpdateMining(dataManager);
		footprints = new UpdateFootprints(dataManager);
		blockPlace = new UpdateBlockPlace(dataManager);
	}
	public void updateLighting(int x, int y) {
		lightingQueue.add(new Point(x, y));
	}
	public void updateBlock(int x, int y) {
		if (dataManager.world.isStructBlock(x, y)) {
			if (dataManager.world.getStructBlock(x, y) == dataManager.system.blockNameMap.get("torch").getID()) {
				if (dataManager.world.getGroundBlock(x, y) == dataManager.system.blockNameMap.get("water").getID()) {
					dataManager.world.removeStructBlock(x, y);
				}
			}
		}
		updateLighting(x, y);
	}
	private void executeLighting() {
		for (int i = 0; i < lightingQueue.size(); i++) {
			lighting.update((int)lightingQueue.get(i).getX(), (int)lightingQueue.get(i).getY());
		}
		lightingQueue.clear();
	}
	public void run() {
		int i = 0;
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					i++;
					if (i % 30 == 0) melt();
					mining.update();
					blockPlace.update();
					footprints.update();
					executeLighting();
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
				if (dataManager.world.isGroundBlock(x, y)) {
					if (dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(x, y)).melts) {
						double brightness = 0;
						if (dataManager.world.isLight(x, y)) {
							brightness = Double.valueOf((dataManager.world.getLight(x, y) + 128)) / 255.0;
						}
						if (brightness > 0.7) {
							if (1 - Math.random() < (brightness - 0.7) / 50.0) {
								dataManager.world.setGroundBlock(x, y, dataManager.system.blockNameMap.get(dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(x, y)).meltsInto).getID());
								updateBlock(x, y);
							}
						}
					}
				}
			}	
		}
	}
}
