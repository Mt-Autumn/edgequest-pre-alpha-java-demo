package com.mtautumn.edgequest;

public class AnimationClock extends Thread {
	SceneManager sceneManager;
	public AnimationClock(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while (true) {
			try {
				if (!sceneManager.system.isGameOnLaunchScreen) {
					sceneManager.system.animationClock++;
				}
				Thread.sleep(sceneManager.settings.tickLength * 8);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
