package com.mtautumn.edgequest;

import java.util.Random;

public class TerrainGenerator {
	SceneManager sceneManager;
	public TerrainGenerator(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	private double getChunkRNG(int x, int y) {
		return Math.sqrt((new Random(sceneManager.savable.seed * x + x).doubles().skip(Math.abs(y)%65535).findFirst().getAsDouble()) * (new Random(sceneManager.savable.seed * 2 * y + 2 * y).doubles().skip(Math.abs(x)%65535).findFirst().getAsDouble()));
	}
	public int getBlockBiome(int x, int y) {
		if (sceneManager.system.biomeMap.containsKey(x + "," + y)) {
			return sceneManager.system.biomeMap.get(x + "," + y);
		} else {
			int chunkX = (int) Math.floor(x / sceneManager.settings.chunkSize);
			int chunkY = (int) Math.floor(y / sceneManager.settings.chunkSize);
			double chunkRNGSum = 0;
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (i != 0 || j != 0) {
						chunkRNGSum += getChunkRNG(chunkX + i, chunkY + j);
					}
				}
			}
			double chunkRNGAverage = chunkRNGSum / 24.0;
			if (chunkRNGAverage < 0.40) {
				sceneManager.system.biomeMap.put(x + "," + y, (byte) 4); //desert
				return 4;
			} else if (chunkRNGAverage < 0.43) {
				sceneManager.system.biomeMap.put(x + "," + y, (byte) 1); //grass
				return 1;
			} else if (chunkRNGAverage < 0.455) {
				sceneManager.system.biomeMap.put(x + "," + y, (byte) 5); //water
				return 5;
			} else if (chunkRNGAverage < 0.47) {
				sceneManager.system.biomeMap.put(x + "," + y, (byte) 1); //grass
				return 1;
			} else if (chunkRNGAverage < 0.51) {
				sceneManager.system.biomeMap.put(x + "," + y, (byte) 2); //snow
				return 2;
			} else {
				sceneManager.system.biomeMap.put(x + "," + y, (byte) 3); //stone
				return 3;
			}
		}
	}
	public void generateBlock(int x, int y) {
		int[] biomeCount = new int[6];
		for (int i = -24; i<=24; i++) {
			for (int j = -24; j <= 24; j++) {
				biomeCount[getBlockBiome(x + i, y + j)] += 1;
			}
		}
		if (isLocationGreatest(biomeCount, 1)) {
			sceneManager.savable.biomeMapFiltered.put(x + "," + y, (byte) 1);
			createBlockForBiome(x, y, 1);
		} else if (isLocationGreatest(biomeCount, 2)) {
			sceneManager.savable.biomeMapFiltered.put(x + "," + y, (byte) 2);
			createBlockForBiome(x, y, 2);
		} else if (isLocationGreatest(biomeCount, 3)){
			sceneManager.savable.biomeMapFiltered.put(x + "," + y, (byte) 3);
			createBlockForBiome(x, y, 3);
		} else if (isLocationGreatest(biomeCount, 4)){
			sceneManager.savable.biomeMapFiltered.put(x + "," + y, (byte) 4);
			createBlockForBiome(x, y, 4);
		} else {
			sceneManager.savable.biomeMapFiltered.put(x + "," + y, (byte) 5);
			createBlockForBiome(x, y, 5);
		}
	}
	private boolean isLocationGreatest(int[] biomeCount, int biome) {
		boolean isGreatest = true;
		for (int i = 0; i < biomeCount.length; i++) {
			if (biomeCount[biome] < biomeCount[i]) {
				isGreatest = false;
			}
		}
		return isGreatest;
	}
	public void createBlockForBiome(int x, int y, int biome) {
		switch (biome) {
		case 1: //grass
			sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("grass").getID());
			break;
		case 2: //snow
			sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("snow").getID());
			break;
		case 3: //stone
			if (getChunkRNG(x, y) < 0.75) {
				sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("stone").getID());
			} else {
				sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("dirt").getID());
			}
			break;
		case 4: //desert
			sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("sand").getID());
			break;
		case 5: //water
			sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("water").getID());
			if (getChunkRNG(x, y) < 0.03) {
				sceneManager.savable.playerStructuresMap.put(x + "," + y, sceneManager.system.blockNameMap.get("lilyPad").getID());
			}
			break;
		default:
			sceneManager.savable.map.put(x + "," + y, sceneManager.system.blockNameMap.get("noTexture").getID());
			break;
		}
	}
}
