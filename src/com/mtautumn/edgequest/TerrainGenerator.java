package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mtautumn.edgequest.data.DataManager;

public class TerrainGenerator {
	DataManager dataManager;
	Map<String,Integer> altitudeMap = new HashMap<String, Integer>();
	Map<String,Integer> temperatureMap = new HashMap<String, Integer>();
	
	Map<String,Integer> altitudeMapFiltered = new HashMap<String, Integer>();
	Map<String,Integer> temperatureMapFiltered = new HashMap<String, Integer>();
	public TerrainGenerator(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void clearCache() {
		altitudeMap.clear();
		temperatureMap.clear();
		altitudeMapFiltered.clear();
		temperatureMapFiltered.clear();
	}
	private double getRNG(int x, int y) {
		return Math.sqrt((new Random(dataManager.savable.seed * x * 2 + x / 2).doubles().skip(Math.abs(y)%65535).findFirst().getAsDouble()) * (new Random(dataManager.savable.seed * 3 * y + 5 * y).doubles().skip(Math.abs(x)%65535).findFirst().getAsDouble()));
	}
	private Double getAltNoise(long x, long y) {
		return Math.sqrt((new Random(dataManager.savable.seed * x + x).doubles().skip(Math.abs(y)%65535).findFirst().getAsDouble()) * (new Random(dataManager.savable.seed * 2 * y + 2 * y).doubles().skip(Math.abs(x)%65535).findFirst().getAsDouble()));
	}
	private Double getTempNoise(long x, long y) {
		return Math.sqrt((new Random(dataManager.savable.seed * x * 3 - x - 1).doubles().skip(Math.abs(y)%65535).findFirst().getAsDouble()) * (new Random(dataManager.savable.seed * 2 * y / 4 + 2 * y - 12).doubles().skip(Math.abs(x)%65535).findFirst().getAsDouble()));
	}
	public int[] getBlockStats(int x, int y) {
		int[] stats = new int[2];
		if (altitudeMap.containsKey(x + "," + y)) {
			stats[0] = altitudeMap.get(x + "," + y);
		}
		if (temperatureMap.containsKey(x + "," + y))
			stats[1] = temperatureMap.get(x + "," + y);
		if (stats[0] == 0) {
			int chunkX = (int) Math.floor(x / dataManager.settings.chunkSize);
			int chunkY = (int) Math.floor(y / dataManager.settings.chunkSize);
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
			int chunkX = (int) Math.floor(x / dataManager.settings.chunkSize);
			int chunkY = (int) Math.floor(y / dataManager.settings.chunkSize);
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
					dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("water").getID());
				} else {
					dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("ice").getID());
				}
			} else {
				dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("water").getID());
				if (getRNG(x, y) < 0.02) {
					dataManager.savable.playerStructuresMap.put(x+","+y, dataManager.system.blockNameMap.get("lilyPad").getID());
				}
			}
		} else if (alt < 64000000 && temp > 62200000){
			dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("sand").getID());
		} else {
			if (temp < 62200000) {
				dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("snow").getID());
			} else if (temp > 70000000){
				dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("sand").getID());
			} else  if (alt < 80000000 ){
				dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("grass").getID());
				if (getRNG(x, y) < 0.03) {
					dataManager.savable.playerStructuresMap.put(x+","+y, dataManager.system.blockNameMap.get("tree").getID());
				}
			} else {
				if (getRNG(x, y) < 0.75) {
					dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("stone").getID());
				} else { 
					dataManager.savable.map.put(x + "," + y, dataManager.system.blockNameMap.get("dirt").getID());
				}
			}
		}
	}
}
