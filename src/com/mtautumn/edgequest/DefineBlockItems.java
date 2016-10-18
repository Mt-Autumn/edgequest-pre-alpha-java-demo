package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.data.DataManager;

public class DefineBlockItems {
	public static Map<Short, BlockItem> blockIDMap = new HashMap<Short, BlockItem>();
	public static Map<String, BlockItem> blockNameMap = new HashMap<String, BlockItem>();
	public static void setDefinitions(DataManager dataManager) {
		noneDefinition();
		noTextureDefinition();
		grassDefinition();
		dirtDefinition();
		stoneDefinition();
		sandDefinition();
		snowDefinition();
		waterDefinition();
		groundDefinition();
		iceDefinition();

		torchDefinition();
		lilyPadDefinition();
		treeDefinition();
		dataManager.system.blockIDMap = blockIDMap;
		dataManager.system.blockNameMap = blockNameMap;
	}
	private static void noneDefinition() {
		BlockItem none = new BlockItem(-1, true, true, "none", new int[]{0} , new int[]{0});
		addToMaps(none);
	}
	private static void noTextureDefinition() {
		BlockItem noTexture = new BlockItem(0, true, true, "noTexture", new int[]{0} , new int[]{0});
		addToMaps(noTexture);
	}
	private static void grassDefinition() {
		BlockItem grass = new BlockItem(1, true, true, "grass", new int[]{0} , new int[]{0});
		grass.replacedBy = "dirt";
		addToMaps(grass);
	}
	private static void dirtDefinition() {
		BlockItem dirt = new BlockItem(2, true, true, "dirt", new int[]{0} , new int[]{0});
		addToMaps(dirt);
	}
	private static void stoneDefinition() {
		BlockItem stone = new BlockItem(3, true, true, "stone", new int[]{0} , new int[]{0});
		addToMaps(stone);
	}
	private static void sandDefinition() {
		BlockItem sand = new BlockItem(4, true, true, "sand", new int[]{0} , new int[]{0});
		addToMaps(sand);
	}
	private static void snowDefinition() {
		BlockItem snow = new BlockItem(5, true, true, "snow", new int[]{0} , new int[]{0});
		snow.canHavePrints = true;
		snow.melts = true;
		snow.meltsInto = "grass";
		snow.replacedBy = "grass";
		addToMaps(snow);
	}
	private static void waterDefinition() {
		BlockItem water = new BlockItem(6, true, false, "water", new int[]{0,1,2,3,2,1} , null);
		water.isLiquid = true;
		water.isPassable = true;
		water.hardness = -1;
		addToMaps(water);
	}
	private static void groundDefinition() {
		BlockItem ground = new BlockItem(7, true, false, "ground", new int[]{0} , null);
		ground.hardness = -1;
		addToMaps(ground);
	}
	private static void iceDefinition() {
		BlockItem ice = new BlockItem(8, true, true, "ice", new int[]{0} , new int[]{0});
		ice.melts = true;
		ice.meltsInto = "water";
		ice.replacedBy = "water";
		addToMaps(ice);
	}
	private static void torchDefinition() {
		BlockItem torch = new BlockItem(100, true, true, "torch", new int[]{0} , new int[]{0});
		torch.isLightSource = true;
		torch.isHot = true;
		torch.isPassable = true;
		addToMaps(torch);
	}
	private static void lilyPadDefinition() {
		BlockItem lilyPad = new BlockItem(101, true, true, "lilyPad", new int[]{0} , new int[]{0});
		lilyPad.isPassable = true;
		addToMaps(lilyPad);
	}
	private static void treeDefinition() {
		BlockItem tree = new BlockItem(102, true, false, "tree", new int[]{0} , null);
		addToMaps(tree);
	}
	
	
	private static void addToMaps(BlockItem blockItem) {
		blockIDMap.put(blockItem.getID(), blockItem);
		blockNameMap.put(blockItem.getName(), blockItem);
	}
}

