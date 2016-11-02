/* Defines each space in the backpack (eventually other storage areas too)
 * Keeps track of the item and how much of that item is there.
 */
package com.mtautumn.edgequest;

import java.io.Serializable;

public class ItemSlot implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int maxItemCount = 99;
	
	private Short itemID = -1;
	private int itemCount = 0;
	public Short getItemID() {
		return itemID;
	}
	public void setItem(Short itemID) {
		this.itemID = itemID;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int count) {
		itemCount = count;
		correctItemCount();
	}
	public int addItems(int count) {
		int itemsAdded = 0;
		if (count + itemCount > maxItemCount) {
			itemsAdded = maxItemCount - itemCount;
			itemCount = maxItemCount;
		} else {
			itemsAdded = count;
			itemCount += count;
		}
		return itemsAdded;
	}
	public int removeItems(int count) {
		int itemsRemoved = 0;
		if (itemCount - count < 0) {
			itemsRemoved = itemCount;
			itemCount = 0;
		} else {
			itemsRemoved = count;
			itemCount -= count;
		}
		return itemsRemoved;
	}
	public boolean isSlotFull() {
		return itemCount >= maxItemCount;
	}
	public void addOne() {
		itemCount++;
		correctItemCount();
	}
	public void subtractOne() {
		itemCount--;
		correctItemCount();
	}
	private void correctItemCount() {
		if (itemCount > maxItemCount) itemCount = maxItemCount;
		if (itemCount < 0) itemCount = 0;
	}
}
