package com.mtautumn.edgequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockInformation {
	public Map<Integer, String> blockHashMap = new HashMap<Integer, String>();
	public Map<String, Integer> reverseBlockHashMap = new HashMap<String, Integer>();
	public Map<Integer, Boolean> isPassableMap = new HashMap<Integer, Boolean>();
	public Map<Integer, Boolean> emitsLightMap = new HashMap<Integer, Boolean>();
	public Map<Integer, Boolean> isAnimatedMap = new HashMap<Integer, Boolean>();
	public Map<Integer, Integer[]> animationStepsMap = new HashMap<Integer, Integer[]>();
	
	public BlockInformation() {
		//--Define Blocks
		addBlock(0, "noTexture", false, false);
		addBlock(1, "grass", false, false);
		addBlock(2, "dirt", false, false);
		addBlock(3, "stone", false, false);
		addBlock(4, "sand", false, false);
		addBlock(5, "snow", false ,false);
		addBlock(6, "water", true, false, new Integer[]{0,1,2,3,2,1});
		
		addBlock(100, "torch", true ,true);
		addBlock(101, "lilyPad", true ,false);
		
		//200+ values are non-block textures
		addBlock(200, "characterUp", true, false);
		addBlock(201, "characterUpRight", true, false);
		addBlock(202, "characterRight", true, false);
		addBlock(203, "characterDownRight", true, false);
		addBlock(204, "characterDown", true, false);
		addBlock(205, "characterDownLeft", true, false);
		addBlock(206, "characterLeft", true, false);
		addBlock(207, "characterUpLeft", true, false);
		addBlock(208, "waterSplash", true ,false, new Integer[]{0,0,1,1,2,2});
		addBlock(209, "footsteps", true, false);
		addBlock(210, "footsteps2", true, false);
		addBlock(211, "footsteps3", true, false);
		
		addBlock(300, "menuBackground", true, false);
	}
	
	private void addBlock(Integer blockID, String name, boolean isPassable, boolean emitsLight) {
		blockHashMap.put(blockID, name);
		reverseBlockHashMap.put(name, blockID);
		isPassableMap.put(blockID, isPassable);
		emitsLightMap.put(blockID, emitsLight);
		isAnimatedMap.put(blockID, false);
	}
	private void addBlock(Integer blockID, String name, boolean isPassable, boolean emitsLight, Integer[] animationSteps) {
		addBlock(blockID, name, isPassable, emitsLight);
		isAnimatedMap.put(blockID, true);
		animationStepsMap.put(blockID, animationSteps);
	}
	public int getBlockID(String name) {
		if (reverseBlockHashMap.containsKey(name)) {
			return reverseBlockHashMap.get(name);
		}
		return 0;
	}
	public String[] getBlockList() {
		ArrayList<String> blockArray = new ArrayList<String>();
		for(int i = 0; i < blockHashMap.size(); i++) {
			int iD = (int) blockHashMap.keySet().toArray()[i];
			if (isBlockAnimated(iD)) {
				for(int j = 0; j < getBlockAnimation(iD).length; j++) {
					if (!blockArray.contains("" + blockHashMap.get(iD) + getBlockAnimation(iD)[j])) {
						blockArray.add("" + blockHashMap.get(iD) + getBlockAnimation(iD)[j]);
					}
				}
			} else {
				blockArray.add(blockHashMap.get(iD));
			}
		}
		String[] result = new String[blockArray.size()];
		for(int i = 0; i < blockArray.size(); i++) {
			result[i] = blockArray.get(i);
		}
		return result;
	}
	public String getBlockName(int iD) {
		if (blockHashMap.containsKey(iD)) {
			return blockHashMap.get(iD);
		}
		return null;
	}
	public Integer[] getBlockAnimation(int iD) {
		if (animationStepsMap.containsKey(iD)) {
			return animationStepsMap.get(iD);
		}
		return null;
	}
	public boolean isBlockPassable(int iD) {
		if (isPassableMap.containsKey(iD)) {
			return isPassableMap.get(iD);
		}
		return false;
	}
	public boolean isBlockLightSource(int iD) {
		if (emitsLightMap.containsKey(iD)) {
			return emitsLightMap.get(iD);
		}
		return false;
	}
	public boolean isBlockAnimated(int iD) {
		if (isAnimatedMap.containsKey(iD)) {
			return isAnimatedMap.get(iD);
		}
		return false;
	}
}
