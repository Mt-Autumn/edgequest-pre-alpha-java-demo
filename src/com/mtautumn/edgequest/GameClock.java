package com.mtautumn.edgequest;

public class GameClock extends Thread {
	SceneManager sceneManager;
	public GameClock(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while(true) {
			try {
				if (!sceneManager.system.isGameOnLaunchScreen) {
					if (sceneManager.savable.time < 2399) {
						sceneManager.savable.time++;
					} else {
						sceneManager.savable.time = 0;
					}
					int hours = (int) Math.floor(sceneManager.savable.time / 100) % 12;
					int minutes = (int) Math.round(Double.valueOf(sceneManager.savable.time % 100) * 0.05);
					if (hours == 0) hours = 12;

					if (sceneManager.savable.time < 1200) {
						sceneManager.system.timeReadable = "" + hours + ":" + minutes + "0 AM";
					} else {
						sceneManager.system.timeReadable = "" + hours + ":" + minutes + "0 PM";
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
