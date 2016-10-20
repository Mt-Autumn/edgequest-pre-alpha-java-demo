package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mtautumn.edgequest.data.DataManager;

public class Dungeon {
	private static final int MAX_LEVEL = 100;
	DungeonLevel[] levels = new DungeonLevel[MAX_LEVEL];
	private DataManager dataManager;
	private long dungeonID;
	public Dungeon(DataManager dataManager, long dungeonID) { //dungeonID is the nth dungeon created in the map
		this.dataManager = dataManager;
		this.dungeonID = dungeonID;
	}

	public short getGroundBlock(int level, int x, int y) {
		if (levels[level] == null) {
			return 0;
		}
		if (levels[level].groundMap.containsKey(x+","+y))
			return levels[level].groundMap.get(x+","+y);
		return 0;
	}
	public void setGroundBlock(int level, int x, int y, short id) {
		levels[level].groundMap.put(x+","+y, id);
	}
	public boolean isGroundBlock(int level, int x, int y) {
		if (levels[level] == null) {
			return false;
		}
		return levels[level].groundMap.containsKey(x+","+y);
	}
	public void removeGroundBlock(int level, int x, int y) {
		if (levels[level] != null) {
			levels[level].groundMap.remove(x+","+y);
		}
	}
	public short getStructureBlock(int level, int x, int y) {
		if (levels[level] == null) {
			return 0;
		}
		if (levels[level].structureMap.containsKey(x+","+y))
			return levels[level].structureMap.get(x+","+y);
		return 0;
	}
	public void setStructureBlock(int level, int x, int y, short id) {
		levels[level].structureMap.put(x+","+y, id);
	}
	public boolean isStructureBlock(int level, int x, int y) {
		if (levels[level] == null) {
			return false;
		}
		return levels[level].structureMap.containsKey(x+","+y);
	}
	public void removeStructureBlock(int level, int x, int y) {
		if (levels[level] != null) {
			levels[level].structureMap.remove(x+","+y);
		}
	}
	public byte getLighting(int level, int x, int y) {
		if (levels[level] == null) {
			return 0;
		}
		if (levels[level].lightingMap.containsKey(x+","+y))
			return levels[level].lightingMap.get(x+","+y);
		return Byte.MIN_VALUE;
	}
	public void setLighting(int level, int x, int y, byte value) {
		levels[level].lightingMap.put(x+","+y, value);
	}
	public boolean isLighting(int level, int x, int y) {
		if (levels[level] == null) {
			return false;
		}
		return levels[level].lightingMap.containsKey(x+","+y);
	}
	public void removeLighting(int level, int x, int y) {
		if (levels[level] != null) {
			levels[level].lightingMap.remove(x+","+y);
		}
	}
	public void requestLevel(int level) {
		if (level >= 0) {
			if (levels[level] == null) {

				levels[level] =  new DungeonLevel(level, dataManager, dungeonID);
			}
		}
	}
	
	public int[] getStairsDown(int level) {
		return levels[level].stairsDownLocation;
	}
	public int[] getStairsUp(int level) {
		return levels[level].stairsUpLocation;
	}
	private class DungeonLevel {
		public Map<String, Short> groundMap = new HashMap<String, Short>();
		public Map<String, Short> structureMap = new HashMap<String, Short>();
		public Map<String, Byte> lightingMap = new HashMap<String, Byte>();
		public int[] stairsUpLocation = new int[2];
		public int[] stairsDownLocation = new int[2];
		private int depth;
		private DataManager dataManager;

		public DungeonLevel(int depth, DataManager dataManager, long dungeonID) {
			this.depth = depth;
			this.dataManager = dataManager;
			generateLevel(dungeonID);
		}
		private void generateLevel(long dungeonID) {
			//TODO: Create generation algorithm
			//USE THE SEED (found at dataManager.savable.seed;
			//Use "ground" block for indestructible terrain
			Random random = new Random(dataManager.savable.seed * dungeonID * (depth+1)); //make sure random numbers are based on seed, depth, and dungeonID
			for (int x = -100; x < 100; x++) {
				for (int y = -100; y < 100; y++) {
					if (random.nextDouble() > 0.5) {
						groundMap.put(x+","+y, dataManager.system.blockNameMap.get("dirt").getID());
					} else {
						groundMap.put(x+","+y, dataManager.system.blockNameMap.get("stone").getID());
					}
				}
			}
			setUpStairs(2, 5);
			setDownStairs(-2, 5);

		}
		private void setUpStairs(int x, int y) {
			stairsUpLocation[0] = x;
			stairsUpLocation[1] = y;
			structureMap.put(x+","+y, dataManager.system.blockNameMap.get("dungeonUp").getID());
		}
		private void setDownStairs(int x, int y) {
			stairsDownLocation[0] = x;
			stairsDownLocation[1] = y;
			structureMap.put(x+","+y, dataManager.system.blockNameMap.get("dungeon").getID());
		}
	}
}
