/*This class is in charge of directing the character's movements when the
*setAutoWalk method is called. It controls character movements by directly
*setting the keyboard key states to the appropriate value for moving in a
*given direction. Until dataManager.system.autoWalk is set to false, this
*continues.
*/
package com.mtautumn.edgequest;

import java.util.ArrayList;

import com.mtautumn.edgequest.PathFinder.IntCoord;
import com.mtautumn.edgequest.data.DataManager;

public class AutoCharacterWalk extends Thread{
	DataManager dataManager;
	PathFinder aStar;
	boolean moveXPositive = true;
	boolean moveYPositive = true;
	ArrayList<IntCoord> path = new ArrayList<IntCoord>();
	public AutoCharacterWalk(DataManager dataManager) {
		this.dataManager = dataManager;
		aStar = new PathFinder(dataManager);
	}
	public void setAutoWalk(int x, int y) {
		int charX = (int) Math.floor(dataManager.savable.charX);
		int charY = (int) Math.floor(dataManager.savable.charY);
		path = aStar.findPath(charX, charY, x, y, dataManager);
		moveXPositive = dataManager.savable.charX < path.get(path.size() - 1).x;
		moveYPositive = dataManager.savable.charY < path.get(path.size() - 1).y;
		dataManager.system.autoWalk = true;
	}
	public void run() {
		while (dataManager.system.running) {
			try {
				if (dataManager.system.autoWalk) {
					double charX = dataManager.savable.charX;
					double charY = dataManager.savable.charY;
					if (path.size() > 0) {
						double targetX = path.get(path.size() - 1).x + 0.5;
						double targetY = path.get(path.size() - 1).y + 0.5;
						dataManager.system.isKeyboardRight = (charX < targetX && moveXPositive);
						dataManager.system.isKeyboardLeft = (charX > targetX && !moveXPositive);
						dataManager.system.isKeyboardDown =  (charY < targetY && moveYPositive);
						dataManager.system.isKeyboardUp = (charY > targetY && !moveYPositive);
						if (charX > targetX == moveXPositive && charY>targetY == moveYPositive) {
							path.remove(path.size() - 1);
							if (path.size() > 0) {
								moveXPositive = charX < path.get(path.size() - 1).x;
								moveYPositive = charY < path.get(path.size() - 1).y;
								targetX = path.get(path.size() - 1).x + 0.5;
								targetY = path.get(path.size() - 1).y + 0.5;
								dataManager.system.isKeyboardLeft = false;
								dataManager.system.isKeyboardRight = false;
								dataManager.system.isKeyboardUp = false;
								dataManager.system.isKeyboardDown = false;
								dataManager.system.isKeyboardSprint = false;
								if (charX < targetX && moveXPositive) dataManager.system.isKeyboardRight = true;
								if (charX > targetX && !moveXPositive) dataManager.system.isKeyboardLeft = true;
								if (charY < targetY && moveYPositive) dataManager.system.isKeyboardDown = true;
								if (charY > targetY && !moveYPositive) dataManager.system.isKeyboardUp = true;
							}
						}
					} else {
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
