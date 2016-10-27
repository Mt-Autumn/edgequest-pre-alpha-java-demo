//Basically just sets the in-game time and updates itself periodically
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class GameClock extends Thread {
	DataManager dataManager;
	public GameClock(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		while(dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					if (dataManager.savable.time < 2399) {
						dataManager.savable.time++;
					} else {
						dataManager.savable.time = 0;
					}
					int hours = (int) Math.floor(dataManager.savable.time / 100) % 12;
					int minutes = (int) Math.round(Double.valueOf(dataManager.savable.time % 100) * 0.05);
					if (hours == 0) hours = 12;

					if (dataManager.savable.time < 1200) {
						dataManager.system.timeReadable = "" + hours + ":" + minutes + "0 AM";
					} else {
						dataManager.system.timeReadable = "" + hours + ":" + minutes + "0 PM";
					}
				}
				Thread.sleep(250);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
