package com.mtautumn.edgequest;

public class BackpackManager {
	public BackpackManager(SceneManager sceneManager) {
		for(int i = 0; i < sceneManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < sceneManager.savable.backpackItems[i].length; j++) {
				sceneManager.savable.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
}
