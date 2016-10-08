package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class AutoCharacterWalk extends Thread{
	DataManager dataManager;
	public AutoCharacterWalk(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		while (dataManager.system.running) {
			try {
				if (dataManager.system.autoWalk) {
					double charX = dataManager.savable.charX;
					double charY = dataManager.savable.charY;
					double targetX = dataManager.system.autoWalkX + 0.5;
					double targetY = dataManager.system.autoWalkY + 0.5;
					dataManager.system.isKeyboardLeft = false;
					dataManager.system.isKeyboardRight = false;
					dataManager.system.isKeyboardUp = false;
					dataManager.system.isKeyboardDown = false;
					dataManager.system.isKeyboardSprint = false;
					if (charX - targetX > 0.05) dataManager.system.isKeyboardLeft = true;
					if (charX - targetX < -0.05) dataManager.system.isKeyboardRight = true;
					if (charY - targetY > 0.05) dataManager.system.isKeyboardUp = true;
					if (charY - targetY < -0.05) dataManager.system.isKeyboardDown = true;
					if (!(dataManager.system.isKeyboardLeft || dataManager.system.isKeyboardRight || dataManager.system.isKeyboardUp || dataManager.system.isKeyboardDown)) {
						dataManager.system.autoWalk = false;
					}
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
