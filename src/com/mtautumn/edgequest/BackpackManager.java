/*A simple class that contains an array of ItemSlot classes.
 * Essentially this is a datatype used to keep track of what's
 * in the player's backpack.
 * 
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class BackpackManager extends Thread {
	DataManager dataManager;
	public BackpackManager(DataManager dataManager) {
		this.dataManager = dataManager;
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				dataManager.savable.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
	public void run() {
		while(dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					checkMouseSelection();
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public ItemSlot[] getCurrentSelection() {
		return new ItemSlot[] {dataManager.savable.backpackItems[0][dataManager.savable.hotBarSelection],dataManager.savable.backpackItems[1][dataManager.savable.hotBarSelection]};
	}
	private boolean wasMouseDown = false;
	private boolean isItemGrabbed = false;
	private int[] mouseItemLocation = {-1,-1};
	private void checkMouseSelection() {
		if (!wasMouseDown && dataManager.system.leftMouseDown) {
			if (!isItemGrabbed) {
				if (!(getMouseLocation()[0] == -1)) {
					mouseItemLocation = getMouseLocation();
					dataManager.savable.mouseItem = dataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]];
					dataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]] = new ItemSlot();
					isItemGrabbed = true;
				}
			} else {
				int[] mouseLocation = getMouseLocation();
				if (!(mouseLocation[0] == -1)) {
					if (dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemCount() == 0) {
						dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]] = dataManager.savable.mouseItem;
						dataManager.savable.mouseItem = new ItemSlot();
						isItemGrabbed = false;
					} else {
						if (dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemID().equals(dataManager.savable.mouseItem.getItemID())) {
							if (dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].isSlotFull()) {
								int slotCount = dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemCount();
								dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].setItemCount(dataManager.savable.mouseItem.getItemCount());
								dataManager.savable.mouseItem.setItemCount(slotCount);
							} else {
								int added = dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].addItems(dataManager.savable.mouseItem.getItemCount());
								dataManager.savable.mouseItem.removeItems(added);
								if (dataManager.savable.mouseItem.getItemCount() <= 0) {
									isItemGrabbed = false;
								}
							}
						} else {
							dataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]] = dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]];
							dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]] = dataManager.savable.mouseItem;
							dataManager.savable.mouseItem = new ItemSlot();
							isItemGrabbed = false;
						}

					}

				}
			}
			wasMouseDown = true;
		} else if (wasMouseDown && !dataManager.system.leftMouseDown) {
			wasMouseDown = false;
		}
	}
	private int[] getMouseLocation() {
		int maxX = 2;
		if (dataManager.system.isKeyboardBackpack) {
			maxX = dataManager.savable.backpackItems.length;
		}
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < dataManager.savable.backpackItems[x].length; y++) {
				int[] itemCoords = getItemSlotCoords(x, y);
				if (itemCoords[0] <= dataManager.system.mousePosition.getX() && itemCoords[1] <= dataManager.system.mousePosition.getY() && itemCoords[2] >= dataManager.system.mousePosition.getX() && itemCoords[3] >= dataManager.system.mousePosition.getY()) {
					return new int[] {x,y};
				}
			}
		}
		return new int[] {-1,-1};
	}
	private int[] getItemSlotCoords(int x, int y) {
		int[] coords = {-1,-1,0,0};
		if (x < 2) {
			int xPosMin = (dataManager.settings.screenWidth - 125) + x * 53 + 20;
			int yPosMin = ((dataManager.settings.screenHeight - 403) - 24) + y * 53 + 66;
			int xPosMax = xPosMin + 38;
			int yPosMax = yPosMin + 38;
			coords = new int[] {xPosMin, yPosMin, xPosMax, yPosMax};
		} else {
			int xPosMin = dataManager.system.menuX + (x - 2) * 64 + 37;
			int yPosMin = dataManager.system.menuY + (y) * 65 + 94;
			int xPosMax = xPosMin + 48;
			int yPosMax = yPosMin + 48;
			coords = new int[] {xPosMin, yPosMin, xPosMax, yPosMax};
		}
		return coords;
	}
	public void addItem(BlockItem item) {
		if (isItemInBackpack(item)) {
			boolean foundSpot = false;
			for(int i = 0; i < dataManager.savable.backpackItems.length && !foundSpot; i++) {
				for(int j = 0; j < dataManager.savable.backpackItems[i].length && !foundSpot; j++) {
					if (dataManager.savable.backpackItems[i][j].getItemID().equals(item.getID()) && !dataManager.savable.backpackItems[i][j].isSlotFull()) {
						ItemSlot slot = dataManager.savable.backpackItems[i][j];
						slot.addOne();
						foundSpot = true;
					}
				}
			}
		} else {
			boolean foundSpot = false;
			for(int i = 0; i < dataManager.savable.backpackItems.length && !foundSpot; i++) {
				for(int j = 0; j < dataManager.savable.backpackItems[i].length && !foundSpot; j++) {
					if (dataManager.savable.hotBarSelection != j || i >= 2) {
						ItemSlot slot = dataManager.savable.backpackItems[i][j];
						if (slot.getItemCount() == 0) {
							slot.setItem(item.getID());
							slot.setItemCount(1);
							foundSpot = true;
						} else if (slot.getItemID().equals(item.getID()) && !slot.isSlotFull()) {
							slot.addOne();
							foundSpot = true;
						}
					}
				}
			}
			if (!foundSpot) {
				for (int i = 0; i < 2 && !foundSpot; i++) {
					ItemSlot slot = dataManager.savable.backpackItems[0][dataManager.savable.hotBarSelection];
					if (slot.getItemCount() == 0) {
						slot.setItem(item.getID());
						slot.setItemCount(1);
						foundSpot = true;
					} else if (slot.getItemID().equals(item.getID()) && !slot.isSlotFull()) {
						slot.addOne();
						foundSpot = true;
					}
				}
			}
		}
	}
	private boolean isItemInBackpack(BlockItem item) {
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				if (dataManager.savable.backpackItems[i][j].getItemID().equals(item.getID()) && !dataManager.savable.backpackItems[i][j].isSlotFull()) {
					return true;
				}
			}
		}
		return false;
	}
}
