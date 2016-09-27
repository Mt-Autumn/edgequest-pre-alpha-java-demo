package com.mtautumn.edgequest;

public class BackpackManager {
	public BackpackManager(SceneManager sceneManager) {
		for(int i = 0; i < sceneManager.system.backpackItems.length; i++) {
			for(int j = 0; j < sceneManager.system.backpackItems[i].length; j++) {
				sceneManager.system.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
}
