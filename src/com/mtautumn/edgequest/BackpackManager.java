package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class BackpackManager {
	DataManager dataManager;
	public BackpackManager(DataManager dataManager) {
		this.dataManager = dataManager;
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				dataManager.savable.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
	public void addItem(BlockItem item) {
		boolean foundSpot = false;
		for(int i = 0; i < dataManager.savable.backpackItems.length && !foundSpot; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length && !foundSpot; j++) {
				ItemSlot slot = dataManager.savable.backpackItems[i][j];
				if (slot.getItemCount() == 0) {
					slot.setItem(item.getID());
					slot.setItemCount(1);
					foundSpot = true;
				} else if (slot.getItemID() == item.getID() && !slot.isSlotFull()) {
					slot.addOne();
					foundSpot = true;
				}
			}
		}
		if (!foundSpot) {
			System.out.println("No space for item");
		}
	}
}
