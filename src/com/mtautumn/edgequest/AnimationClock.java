package com.mtautumn.edgequest;

public class AnimationClock extends Thread {
	SceneManager sceneManager;
	public AnimationClock(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while (true) {
			sceneManager.animationClock++;
			try {
				Thread.sleep(sceneManager.tickLength * 8);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
