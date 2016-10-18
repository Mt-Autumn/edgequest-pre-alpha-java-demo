package com.mtautumn.edgequest.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.FootPrint;
import com.mtautumn.edgequest.ItemSlot;

public class SavableData implements Serializable {
	private static final long serialVersionUID = 1L;
	public int time = 800;
	public Map<String, Short> map = new HashMap<String, Short>();
	public Map<String, Byte> lightMap = new HashMap<String, Byte>();
	public Map<String, Short> playerStructuresMap = new HashMap<String, Short>();
	public ArrayList<FootPrint> footPrints = new ArrayList<FootPrint>();
	public long seed = 7;
	public double charX = 5;
	public double charY = 5;
	public int charDir = 0;
	public ItemSlot[][] backpackItems = new ItemSlot[6][6];
	public double getBrightness() {
		int tempTime = time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) tempTime += 2400;
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		return brightness;
	}
}