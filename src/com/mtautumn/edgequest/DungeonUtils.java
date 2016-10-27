/* A series of methods for setting, getting, or checking blocks, structures, or
 * lighting for each tile on each level of the dungeon.(Normally accessed using
 * world utils @ dataManager.world)
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class DungeonUtils {
	DataManager dm;
	public DungeonUtils(DataManager dm) {
		this.dm = dm;
	}
	
	
	public void setStructBlock(int x, int y, short id) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).setStructureBlock(dm.savable.dungeonLevel, x, y, id);
	}
	public short getStructBlock(int x, int y) {
		if (isStructBlock(x, y)) {
			return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).getStructureBlock(dm.savable.dungeonLevel, x, y);
		}
		return 0;
	}
	public boolean isStructBlock(int x, int y) {
		return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).isStructureBlock(dm.savable.dungeonLevel, x, y);
	}
	public void removeStructBlock(int x, int y) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).removeStructureBlock(dm.savable.dungeonLevel, x, y);
	}
	
	public void setGroundBlock(int x, int y, short id) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).setGroundBlock(dm.savable.dungeonLevel, x, y, id);
	}
	public short getGroundBlock(int x, int y) {
		if (isGroundBlock(x, y)) {
			return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).getGroundBlock(dm.savable.dungeonLevel, x, y);
		}
		return 0;
	}
	public boolean isGroundBlock(int x, int y) {
		return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).isGroundBlock(dm.savable.dungeonLevel, x, y);
	}
	public void removeGroundBlock(int x, int y) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).removeGroundBlock(dm.savable.dungeonLevel, x, y);
	}
	
	public void setLight(int x, int y, byte val) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).setLighting(dm.savable.dungeonLevel, x, y, val);
	}
	public byte getLight(int x, int y) {
		if (isLight(x, y)) {
			return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).getLighting(dm.savable.dungeonLevel, x, y);
		}
		return Byte.MIN_VALUE;
	}
	public boolean isLight(int x, int y) {
		return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).isLighting(dm.savable.dungeonLevel, x, y);
	}
	public void removeLight(int x, int y) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).removeLighting(dm.savable.dungeonLevel, x, y);
	}
}
