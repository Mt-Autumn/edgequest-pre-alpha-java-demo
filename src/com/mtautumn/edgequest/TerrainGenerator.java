package com.mtautumn.edgequest;

import java.util.Random;

public class TerrainGenerator {
	SceneManager sceneManager;
	public TerrainGenerator(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	private double getChunkRNG(int x, int y) {
		return Math.sqrt((new Random(sceneManager.seed * x).doubles().skip(Math.abs(y)).findFirst().getAsDouble()) * (new Random(sceneManager.seed * 2 * y).doubles().skip(Math.abs(x)).findFirst().getAsDouble()));
	}
	public int getBlockBiome(int x, int y) {
		if (sceneManager.biomeMap.containsKey(x + "," + y)) {
			return sceneManager.biomeMap.get(x + "," + y);
		} else {
		int chunkX = (int) Math.floor(x / sceneManager.chunkSize);
		int chunkY = (int) Math.floor(y / sceneManager.chunkSize);
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
			sceneManager.biomeMap.put(x + "," + y, 4); //sand
			return 4;
		} else if (chunkRNGAverage < 0.43) {
			sceneManager.biomeMap.put(x + "," + y, 1); //grass
			return 1;
		} else if (chunkRNGAverage < 0.455) {
			sceneManager.biomeMap.put(x + "," + y, 5); //water
			return 5;
		} else if (chunkRNGAverage < 0.47) {
			sceneManager.biomeMap.put(x + "," + y, 1); //grass
			return 1;
		} else if (chunkRNGAverage < 0.51) {
			sceneManager.biomeMap.put(x + "," + y, 2); //snow
			return 2;
		} else {
			sceneManager.biomeMap.put(x + "," + y, 3); //stone
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
			sceneManager.biomeMapFiltered.put(x + "," + y, 1);
			createBlockForBiome(x, y, 1);
		} else if (isLocationGreatest(biomeCount, 2)) {
			sceneManager.biomeMapFiltered.put(x + "," + y, 2);
			createBlockForBiome(x, y, 2);
		} else if (isLocationGreatest(biomeCount, 3)){
			sceneManager.biomeMapFiltered.put(x + "," + y, 3);
			createBlockForBiome(x, y, 3);
		} else if (isLocationGreatest(biomeCount, 4)){
			sceneManager.biomeMapFiltered.put(x + "," + y, 4);
			createBlockForBiome(x, y, 4);
		} else {
			sceneManager.biomeMapFiltered.put(x + "," + y, 5);
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
		sceneManager.lightMap.put(x + "," + y, 0.0);
		switch (biome) {
		case 1:
			sceneManager.map.put(x + "," + y, 1);
			break;
		case 2:
			sceneManager.map.put(x + "," + y, 7);
			break;
		case 3:
			if (getChunkRNG(x, y) < 0.75) {
			sceneManager.map.put(x + "," + y, 3);
			} else {
				sceneManager.map.put(x + "," + y, 2);
			}
			break;
		case 4:
			sceneManager.map.put(x + "," + y, 8);
			break;
		case 5:
			sceneManager.map.put(x + "," + y, 4);
			if (getChunkRNG(x, y) < 0.03) {
				sceneManager.playerStructuresMap.put(x + "," + y, 9);
			}
			break;
		default:
			sceneManager.map.put(x + "," + y, 0);
			break;
		}
	}
}
