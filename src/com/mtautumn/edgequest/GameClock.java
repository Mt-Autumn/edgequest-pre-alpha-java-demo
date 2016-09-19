package com.mtautumn.edgequest;

public class GameClock extends Thread {
	SceneManager sceneManager;
	public GameClock(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while(true) {
		if (sceneManager.time < 2399) {
			sceneManager.time++;
		} else {
			sceneManager.time = 0;
		}
		int hours = (int) Math.floor(sceneManager.time / 100) % 12;
		int minutes = (int) Math.round(Double.valueOf(sceneManager.time % 100) * 0.05);
		if (hours == 0) hours = 12;

		if (sceneManager.time < 1200) {
			sceneManager.timeReadable = "" + hours + ":" + minutes + "0 AM";
		} else {
			sceneManager.timeReadable = "" + hours + ":" + minutes + "0 PM";
		}
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
}
