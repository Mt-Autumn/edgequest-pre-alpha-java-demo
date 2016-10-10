package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.data.DataManager;

public class AnimationClock extends Thread {
	DataManager dataManager;
	public AnimationClock(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					dataManager.system.animationClock++;
				}
				Thread.sleep(dataManager.settings.tickLength * 8);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
