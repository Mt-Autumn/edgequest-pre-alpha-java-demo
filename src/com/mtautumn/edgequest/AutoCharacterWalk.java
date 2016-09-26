package com.mtautumn.edgequest;

public class AutoCharacterWalk extends Thread{
	SceneManager sceneManager;
	public AutoCharacterWalk(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void run() {
		while (true) {
			try {
				if (sceneManager.system.autoWalk) {
					double charX = sceneManager.system.charX;
					double charY = sceneManager.system.charY;
					double targetX = sceneManager.system.autoWalkX + 0.5;
					double targetY = sceneManager.system.autoWalkY + 0.5;
					sceneManager.system.isKeyboardLeft = false;
					sceneManager.system.isKeyboardRight = false;
					sceneManager.system.isKeyboardUp = false;
					sceneManager.system.isKeyboardDown = false;
					sceneManager.system.isKeyboardSprint = false;
					if (charX - targetX > 0.05) sceneManager.system.isKeyboardLeft = true;
					if (charX - targetX < -0.05) sceneManager.system.isKeyboardRight = true;
					if (charY - targetY > 0.05) sceneManager.system.isKeyboardUp = true;
					if (charY - targetY < -0.05) sceneManager.system.isKeyboardDown = true;
					if (!(sceneManager.system.isKeyboardLeft || sceneManager.system.isKeyboardRight || sceneManager.system.isKeyboardUp || sceneManager.system.isKeyboardDown)) {
						sceneManager.system.autoWalk = false;
					}
				}
				Thread.sleep(sceneManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
