package com.mtautumn.edgequest;

public class AnimationClock extends Thread {
	SceneManager sceneManager;
	public AnimationClock(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while (true) {
			sceneManager.animationClock60Step++;
			if (sceneManager.animationClock60Step > 59) {
				sceneManager.animationClock60Step = 0;
			}
			try {
				Thread.sleep(sceneManager.tickLength * 8);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
