package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class BackpackManager {
	public BackpackManager(DataManager dataManager) {
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				dataManager.savable.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
}
