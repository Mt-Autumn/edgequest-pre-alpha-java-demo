package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.BlockItem;
import com.mtautumn.edgequest.data.DataManager;

public class UpdateMining {
	DataManager dataManager;
	public UpdateMining(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	private boolean wasMouseDown = false;
	public void update() {
		if (!dataManager.system.isKeyboardBackpack && !dataManager.system.isKeyboardMenu) {
			if (dataManager.system.leftMouseDown && wasMouseDown && !dataManager.system.isMouseFar) {
				if (dataManager.system.miningX != dataManager.system.mouseX || dataManager.system.miningY != dataManager.system.mouseY) {
					dataManager.system.miningX = dataManager.system.mouseX;
					dataManager.system.miningY = dataManager.system.mouseY;
					dataManager.system.blockDamage = 0;
				}
				dataManager.system.blockDamage += 1.0/getBlockAt(dataManager.system.mouseX, dataManager.system.mouseY).hardness/dataManager.settings.tickLength;
				if (dataManager.system.blockDamage < 0) dataManager.system.blockDamage = 0;
				if (dataManager.system.blockDamage >= 10) {
					dataManager.system.blockDamage = 0;
					breakBlock(dataManager.system.mouseX, dataManager.system.mouseY);
					dataManager.blockUpdateManager.lighting.update(dataManager.system.mouseX, dataManager.system.mouseY);
				}
			} else {
				dataManager.system.blockDamage = 0;
			}
		}
		wasMouseDown = dataManager.system.leftMouseDown;
	}
	private BlockItem getBlockAt(int x, int y) {
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y));
		} else if (dataManager.savable.map.containsKey(x + "," + y)) {
			return dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y));
		} else {
			return null;
		}

	}
	private void breakBlock(int x, int y) {
		BlockItem item = null;
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			item = dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y));
			dataManager.savable.playerStructuresMap.remove(x + "," + y);

		} else if (dataManager.savable.map.containsKey(x + "," + y)) {
			item = dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y));
			String replacement = dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y)).replacedBy;
			dataManager.savable.map.put(x + "," + y,dataManager.system.blockNameMap.get(replacement).getID());
		}
		if (item != null) {
			BlockItem result = dataManager.system.blockNameMap.get(item.breaksInto);
			if (result.getIsItem()) {
				dataManager.backpackManager.addItem(result);
			}
		}
	}
}
