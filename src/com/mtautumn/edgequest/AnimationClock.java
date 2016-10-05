package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class AnimationClock extends Thread {
	DataManager dataManager;
	public AnimationClock(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		while (true) {
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
