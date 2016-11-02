/* Algorithm for generating the overworld terrain. An instance is created
 * followed by calling generate block for each block that needs to be made
 */
package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mtautumn.edgequest.data.DataManager;

public class TerrainGenerator {
	DataManager dataManager;
	Map<String,Double> altNoiseMap = new HashMap<String, Double>();
	Map<String,Double> tempNoiseMap = new HashMap<String, Double>();

	Map<String,Integer> altitudeMap = new HashMap<String, Integer>();
	Map<String,Integer> temperatureMap = new HashMap<String, Integer>();

	Map<String,Integer> altitudeMapFiltered = new HashMap<String, Integer>();
	Map<String,Integer> temperatureMapFiltered = new HashMap<String, Integer>();
	public TerrainGenerator(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public static long generateSeed(long... vals) {
		long newSeed = vals[0];
		for (int i = 1; i < vals.length; i++) {
			newSeed = new Random(newSeed + vals[i]).nextLong();
		}
		return newSeed;
	}
	public void clearCache() {
		altitudeMap.clear();
		temperatureMap.clear();
		altitudeMapFiltered.clear();
		temperatureMapFiltered.clear();
		altNoiseMap.clear();
		tempNoiseMap.clear();
	}
	private double getRNG(int x, int y) {
		return Math.sqrt(new Random(generateSeed(dataManager.savable.seed,x,y,12)).nextDouble() * new Random(generateSeed(dataManager.savable.seed,x,y,11)).nextDouble());
	}
	private Double getAltNoise(long x, long y) {
		if (!altNoiseMap.containsKey(x + "," + y))
			altNoiseMap.put(x+","+y, Math.sqrt(new Random(generateSeed(dataManager.savable.seed,x,y,3)).nextDouble() * new Random(generateSeed(dataManager.savable.seed,x,y,4)).nextDouble()));
		return altNoiseMap.get(x + "," + y);
	}
	private Double getTempNoise(long x, long y) {
		if (!tempNoiseMap.containsKey(x + "," + y))
			tempNoiseMap.put(x+","+y, Math.sqrt(new Random(generateSeed(dataManager.savable.seed,x,y,7)).nextDouble() * new Random(generateSeed(dataManager.savable.seed,x,y,8)).nextDouble()));
		return tempNoiseMap.get(x+","+y);
	}
	public int[] getBlockStats(int x, int y) {
		int[] stats = new int[2];
		if (altitudeMap.containsKey(x + "," + y)) {
			stats[0] = altitudeMap.get(x + "," + y);
		}
		if (temperatureMap.containsKey(x + "," + y))
			stats[1] = temperatureMap.get(x + "," + y);

		int chunkX = (int) Math.floor(x / dataManager.settings.chunkSize);
		int chunkY = (int) Math.floor(y / dataManager.settings.chunkSize);
		if (stats[0] == 0) {
			double chunkRNGSum = 0;
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (i != 0 || j != 0)
						chunkRNGSum += getAltNoise(chunkX + i, chunkY + j);
				}
			}
			stats[0] = (int) (chunkRNGSum * 10000) + 1;
			altitudeMap.put(x+","+y, stats[0]);
		}
		if (stats[1] == 0) {
			double chunkRNGSum = 0;
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (i != 0 || j != 0)
						chunkRNGSum += getTempNoise(chunkX + i, chunkY + j);
				}
			}
			stats[1] = (int) (chunkRNGSum * 10000) + 1;
			temperatureMap.put(x+","+y, stats[1]);
		}
		return stats;
	}
	public void generateBlock(int x, int y) {
		int averageAltitude = 0;
		int averageTemperature = 0;
		for (int i = -8; i<=16; i++) {
			for (int j = -8; j <= 16; j++) {
				averageAltitude += getBlockStats(x + i, y + j)[0];
				averageTemperature += getBlockStats(x + i, y + j)[1];
			}
		}
		altitudeMapFiltered.put(x + "," + y, averageAltitude);
		temperatureMapFiltered.put(x + "," + y, averageTemperature);
		createBlockForStats(x, y);
	}
	public void createBlockForStats(int x, int y) {
		int alt = altitudeMapFiltered.get(x+","+y);
		int temp = temperatureMapFiltered.get(x+","+y);
		if (alt < 63000000) {
			if (temp < 62200000) {
				if (alt < 61000000) {
					dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("water").getID());
				} else {
					dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("ice").getID());
				}
			} else {
				dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("water").getID());
				if (getRNG(x, y) < 0.02) {
					dataManager.world.ou.setStructBlock(x, y, dataManager.system.blockNameMap.get("lilyPad").getID());
				}
			}
		} else if (alt < 64000000 && temp > 62200000){
			dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("sand").getID());
		} else {
			if (temp < 62200000) {
				dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("snow").getID());
			} else if (temp > 70000000){
				dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("sand").getID());
			} else  if (alt < 80000000 ){
				dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("grass").getID());
				if (getRNG(x, y) < 0.03) {
					dataManager.world.ou.setStructBlock(x, y, dataManager.system.blockNameMap.get("tree").getID());
				}
			} else {
				if (getRNG(x, y) < 0.75) {
					dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("stone").getID());
				} else { 
					dataManager.world.ou.setGroundBlock(x, y, dataManager.system.blockNameMap.get("dirt").getID());
				}
			}

			if (getRNG(x, y) > 0.98) {
				dataManager.world.ou.setStructBlock(x, y, dataManager.system.blockNameMap.get("dungeon").getID());
				dataManager.savable.dungeonMap.put(x + "," + y, new Dungeon(dataManager, ++dataManager.savable.dungeonCount));
			}
		}
	}
}
