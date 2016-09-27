package com.mtautumn.edgequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockInformation {
	public Map<Integer, String> blockIconHashMap = new HashMap<Integer, String>();
	public Map<Integer, String> blockHashMap = new HashMap<Integer, String>();
	public Map<String, Integer> reverseBlockHashMap = new HashMap<String, Integer>();
	public Map<Integer, Boolean> isPassableMap = new HashMap<Integer, Boolean>();
	public Map<Integer, Boolean> emitsLightMap = new HashMap<Integer, Boolean>();
	public Map<Integer, Boolean> isAnimatedMap = new HashMap<Integer, Boolean>();
	public Map<Integer, Integer[]> animationStepsMap = new HashMap<Integer, Integer[]>();

	public BlockInformation() {
		//--Define Blocks
		addBlock(0, "noTexture", false, false, "none");
		addBlock(1, "grass", false, false, "grassIcon");
		addBlock(2, "dirt", false, false, "dirtIcon");
		addBlock(3, "stone", false, false, "stoneIcon");
		addBlock(4, "sand", false, false, "sandIcon");
		addBlock(5, "snow", false ,false, "snowIcon");
		addBlock(6, "water", true, false, "waterIcon", new Integer[]{0,1,2,3,2,1});

		addBlock(100, "torch", true ,true, "torchIcon");
		addBlock(101, "lilyPad", true ,false, "lilyPadIcon");

		//200+ values are non-block textures
		addBlock(200, "characterUp", true, false, "none");
		addBlock(201, "characterUpRight", true, false, "none");
		addBlock(202, "characterRight", true, false, "none");
		addBlock(203, "characterDownRight", true, false, "none");
		addBlock(204, "characterDown", true, false, "none");
		addBlock(205, "characterDownLeft", true, false, "none");
		addBlock(206, "characterLeft", true, false, "none");
		addBlock(207, "characterUpLeft", true, false, "none");
		addBlock(208, "waterSplash", true ,false, "none", new Integer[]{0,0,1,1,2,2});
		addBlock(209, "footsteps", true, false, "none");
		addBlock(210, "footsteps2", true, false, "none");
		addBlock(211, "footsteps3", true, false, "none");
		addBlock(212, "select", true ,false, "none");
		addBlock(213, "selectFar", true ,false, "none");
		addBlock(214, "selectFlag", true ,false, "none");
		addBlock(215, "cursor", true ,false, "none");

		addBlock(300, "menuBackground", true, false, "none");
		addBlock(301, "launchScreenBackground", true, false, "none");
		addBlock(302, "launchScreenLogo", true, false, "none");
		addBlock(303, "backpack", true, false, "none");
	}

	private void addBlock(Integer blockID, String name, boolean isPassable, boolean emitsLight, String blockIconName) {
		blockHashMap.put(blockID, name);
		reverseBlockHashMap.put(name, blockID);
		isPassableMap.put(blockID, isPassable);
		emitsLightMap.put(blockID, emitsLight);
		isAnimatedMap.put(blockID, false);
		blockIconHashMap.put(blockID, blockIconName);
	}
	private void addBlock(Integer blockID, String name, boolean isPassable, boolean emitsLight, String blockIconName, Integer[] animationSteps) {
		addBlock(blockID, name, isPassable, emitsLight, blockIconName);
		isAnimatedMap.put(blockID, true);
		animationStepsMap.put(blockID, animationSteps);
	}
	public String getIconName(int id) {
		if (blockIconHashMap.containsKey(id)) {
			return blockIconHashMap.get(id);
		}
		return null;
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
	public String[] getBlockIconList() {
		ArrayList<String> blockIconArray = new ArrayList<String>();
		for(int i = 0; i < blockIconHashMap.size(); i++) {
			int iD = (int) blockIconHashMap.keySet().toArray()[i];
				blockIconArray.add(blockIconHashMap.get(iD));
		}
		String[] result = new String[blockIconArray.size()];
		for(int i = 0; i < blockIconArray.size(); i++) {
			result[i] = blockIconArray.get(i);
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
