package com.mtautumn.edgequest;

public class AnimationClock extends Thread {
	SceneManager sceneManager;
	public AnimationClock(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while (true) {
			if (!sceneManager.system.isGameOnLaunchScreen) {
				sceneManager.system.animationClock++;
			}
			try {
				Thread.sleep(sceneManager.settings.tickLength * 8);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
