/* Manages the dungeons (duh). Contains an array of dungeon levels which define
 * each level of the dungeon. Also has methods for accessing info about the
 * dungeon and requesting a level of the dungeon be generated.
 */
package com.mtautumn.edgequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.generator.Generator;

public class Dungeon implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int MAX_LEVEL = 100;
	DungeonLevel[] levels = new DungeonLevel[MAX_LEVEL];
	private long seed;
	private long dungeonID;
	public Dungeon(DataManager dataManager, long dungeonID) { //dungeonID is the nth dungeon created in the map
		seed = dataManager.savable.seed;
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
	public void requestLevel(int level, Map<String, BlockItem> blockNameMap) {
		if (level >= 0) {
			if (levels[level] == null) {

				levels[level] =  new DungeonLevel(level, dungeonID, blockNameMap, seed);
			}
		}
	}

	public int[] getStairsDown(int level) {
		return levels[level].stairsDownLocation;
	}
	public int[] getStairsUp(int level) {
		return levels[level].stairsUpLocation;
	}
	private class DungeonLevel implements Serializable {
		private static final long serialVersionUID = 1L;
		public Map<String, Short> groundMap = new HashMap<String, Short>();
		public Map<String, Short> structureMap = new HashMap<String, Short>();
		public Map<String, Byte> lightingMap = new HashMap<String, Byte>();
		public int[] stairsUpLocation = new int[2];
		public int[] stairsDownLocation = new int[2];
		private int depth;
		private long seed;

		public DungeonLevel(int depth, long dungeonID, Map<String, BlockItem> blockNameMap, long seed) {
			this.depth = depth;
			this.seed = seed;
			generateLevel(dungeonID, blockNameMap);
		}
		private void generateLevel(long dungeonID, Map<String, BlockItem> blockNameMap) {
			int[][] dungeonMap = new Generator(100, 100, 10, seed * dungeonID * (depth + 1)).getNewDungeon();
			for (int x = -2; x < 102; x+=103) {
				for (int y = -2; y < 102; y++) {
					structureMap.put(x+","+y, blockNameMap.get("ground").getID());
					groundMap.put(x+","+y, blockNameMap.get("stone").getID());
				}
			}
			for (int y = -2; y < 102; y+=103) {
				for (int x = -2; x < 102; x++) {
					structureMap.put(x+","+y, blockNameMap.get("ground").getID());
					groundMap.put(x+","+y, blockNameMap.get("stone").getID());
				}
			}
			for (int x = -1; x < 101; x+=101) {
				for (int y = -1; y < 101; y++) {
					structureMap.put(x+","+y, blockNameMap.get("dirt").getID());
					groundMap.put(x+","+y, blockNameMap.get("stone").getID());
				}
			}
			for (int y = -1; y < 101; y+=101) {
				for (int x = -1; x < 101; x++) {
					structureMap.put(x+","+y, blockNameMap.get("dirt").getID());
					groundMap.put(x+","+y, blockNameMap.get("stone").getID());
				}
			}
			for (int x = 0; x < dungeonMap.length; x++) {
				for (int y = 0; y < dungeonMap[1].length; y++) {
					groundMap.put(x+","+y, blockNameMap.get("stone").getID());
					switch (dungeonMap[x][y]) {
					case 0:
						structureMap.put(x+","+y, blockNameMap.get("dirt").getID());
						break;
					case 2:
						setUpStairs(x, y, blockNameMap);
						break;
					case 3:
						setDownStairs(x, y, blockNameMap);
						break;
					default:
						break;
					}
				}
			}

		}
		private void setUpStairs(int x, int y, Map<String, BlockItem> blockNameMap) {
			stairsUpLocation[0] = x;
			stairsUpLocation[1] = y;
			structureMap.put(x+","+y, blockNameMap.get("dungeonUp").getID());
		}
		private void setDownStairs(int x, int y, Map<String, BlockItem> blockNameMap) {
			stairsDownLocation[0] = x;
			stairsDownLocation[1] = y;
			structureMap.put(x+","+y, blockNameMap.get("dungeon").getID());
		}
	}
}
