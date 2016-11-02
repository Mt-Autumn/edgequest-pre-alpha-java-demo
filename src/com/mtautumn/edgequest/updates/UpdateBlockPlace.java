package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.BlockItem;
import com.mtautumn.edgequest.ItemSlot;
import com.mtautumn.edgequest.data.DataManager;

public class UpdateBlockPlace {
	DataManager dataManager;
	public UpdateBlockPlace(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	private boolean wasMouseDown = false;
	private boolean wasRightMouseDown = false;
	public void update() {
		if (!dataManager.system.isKeyboardBackpack && !dataManager.system.isKeyboardMenu) {
			if (dataManager.system.leftMouseDown && !wasMouseDown && !dataManager.system.isMouseFar && dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[0].getItemID()).getIsBlock()) {
				placeBlock(dataManager.system.mouseX, dataManager.system.mouseY, 0);
			} else if (dataManager.system.rightMouseDown && !wasRightMouseDown && !dataManager.system.isMouseFar && dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[1].getItemID()).getIsBlock()) {
				placeBlock(dataManager.system.mouseX, dataManager.system.mouseY, 1);
			}
		}
		wasMouseDown = dataManager.system.leftMouseDown;
		wasRightMouseDown = dataManager.system.rightMouseDown;
	}
	private void placeBlock(int x, int y, int click) {
		if (!dataManager.world.isStructBlock(x, y) && dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].getItemCount() > 0) {
			BlockItem item = dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(x, y));
			BlockItem slotItem = dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[click].getItemID());
			if ((item.isName("water") || item.isName("ground")) && slotItem.isSolid) {
				dataManager.world.setGroundBlock(x, y, slotItem.getID());
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].subtractOne();
			} else if ((item.isName("grass") || item.isName("dirt")) && slotItem.isName("snow")) {
				dataManager.world.setGroundBlock(x, y, slotItem.getID());
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].subtractOne();
			} else {
				dataManager.world.setStructBlock(x, y, slotItem.getID());
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].subtractOne();
			}
			dataManager.blockUpdateManager.updateBlock(x, y);
			if (dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].getItemCount() <= 0) {
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection] = new ItemSlot();
			}
		}
	}
}


