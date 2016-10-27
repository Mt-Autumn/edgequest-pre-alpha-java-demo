/* Used by most of the game to access/set world block data
 * Automatically determines which block is being referenced based on dungeon
 * level.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class WorldUtils {
	private DataManager dm;
	public DungeonUtils du;
	public OverwordUtils ou;
	public WorldUtils(DataManager dm) {
		this.dm = dm;
		du = new DungeonUtils(dm);
		ou = new OverwordUtils(dm);
	}
	public void wipeMaps() {
		dm.savable.playerStructuresMap.clear();
		dm.savable.dungeonMap.clear();
		dm.savable.map.clear();
		dm.savable.lightMap.clear();
	}

	public void setStructBlock(int x, int y, short id) {
		if (dm.savable.isInDungeon)
			du.setStructBlock(x, y, id);
		else
			ou.setStructBlock(x, y, id);
			
	}
	public short getStructBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.getStructBlock(x, y);
		return ou.getStructBlock(x, y);

	}
	public boolean isStructBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.isStructBlock(x, y);
		return ou.isStructBlock(x, y);
	}
	public void removeStructBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			du.removeStructBlock(x, y);
		else
			ou.removeStructBlock(x, y);
			
	}

	public void setGroundBlock(int x, int y, short id) {
		if (dm.savable.isInDungeon)
			du.setGroundBlock(x, y, id);
		else
			ou.setGroundBlock(x, y, id);
	}
	public short getGroundBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.getGroundBlock(x, y);
		return ou.getGroundBlock(x, y);
	}
	public boolean isGroundBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.isGroundBlock(x, y);
		return ou.isGroundBlock(x, y);
	}
	public void removeGroundBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			du.removeGroundBlock(x, y);
		else
			ou.removeGroundBlock(x, y);
			
	}

	public void setLight(int x, int y, byte val) {
		if (dm.savable.isInDungeon)
			du.setLight(x, y, val);
		else
			ou.setLight(x, y, val);
	}
	public byte getLight(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.getLight(x, y);
		return ou.getLight(x, y);
	}
	public boolean isLight(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.isLight(x, y);
		return ou.isLight(x, y);
	}
	public void removeLight(int x, int y) {
		if (dm.savable.isInDungeon)
			du.removeLight(x, y);
		else
			ou.removeLight(x, y);
			
	}
	
	public double getBrightness() {
		if (dm.savable.isInDungeon)
			return 0.0;
		int tempTime = dm.savable.time - 200;
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
