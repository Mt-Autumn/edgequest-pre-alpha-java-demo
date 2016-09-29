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
