/* Updates damage incurred on a block by mining and removes the block
 * if the damage is full.
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.BlockItem;
import com.mtautumn.edgequest.data.DataManager;

public class UpdateMining {
	DataManager dataManager;
	public UpdateMining(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	private boolean wasMouseDown = false;
	private boolean wasRightMouseDown = false;
	public void update() {
		if (!dataManager.system.isKeyboardBackpack && !dataManager.system.isKeyboardMenu) {
			if (dataManager.system.leftMouseDown && wasMouseDown && !dataManager.system.isMouseFar && (!dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[0].getItemID()).getIsBlock() || dataManager.backpackManager.getCurrentSelection()[0].getItemCount() <= 0)) {
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
			} else if (dataManager.system.rightMouseDown && wasRightMouseDown && !dataManager.system.isMouseFar && (!dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[1].getItemID()).getIsBlock() || dataManager.backpackManager.getCurrentSelection()[1].getItemCount() <= 0)) {
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
		wasRightMouseDown = dataManager.system.rightMouseDown;
	}
	private BlockItem getBlockAt(int x, int y) {
		if (dataManager.world.isStructBlock(x, y)) {
			return dataManager.system.blockIDMap.get(dataManager.world.getStructBlock(x, y));
		} else if (dataManager.world.isGroundBlock(x, y)) {
			return dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(x, y));
		} else {
			return null;
		}

	}
	private void breakBlock(int x, int y) {
		BlockItem item = null;
		if (dataManager.world.isStructBlock(x, y)) {
			item = dataManager.system.blockIDMap.get(dataManager.world.getStructBlock(x, y));
			dataManager.world.removeStructBlock(x, y);

		} else if (dataManager.world.isGroundBlock(x, y)) {
			item = dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(x, y));
			String replacement = dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(x, y)).replacedBy;
			dataManager.world.setGroundBlock(x, y,dataManager.system.blockNameMap.get(replacement).getID());
		}
		if (item != null) {
			BlockItem result = dataManager.system.blockNameMap.get(item.breaksInto);
			if (result.getIsItem()) {
				dataManager.backpackManager.addItem(result);
			}
		}
	}
}
