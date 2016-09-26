package com.mtautumn.edgequest;

import java.util.Random;

public class TerrainGenerator {
	SceneManager sceneManager;
	BlockInformation blockInfo = new BlockInformation();
	public TerrainGenerator(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	private double getChunkRNG(int x, int y) {
		return Math.sqrt((new Random(sceneManager.world.seed * x + x).doubles().skip(Math.abs(y)).findFirst().getAsDouble()) * (new Random(sceneManager.world.seed * 2 * y + 2 * y).doubles().skip(Math.abs(x)).findFirst().getAsDouble()));
	}
	public int getBlockBiome(int x, int y) {
		if (sceneManager.world.biomeMap.containsKey(x + "," + y)) {
			return sceneManager.world.biomeMap.get(x + "," + y);
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
				sceneManager.world.biomeMap.put(x + "," + y, 4); //desert
				return 4;
			} else if (chunkRNGAverage < 0.43) {
				sceneManager.world.biomeMap.put(x + "," + y, 1); //grass
				return 1;
			} else if (chunkRNGAverage < 0.455) {
				sceneManager.world.biomeMap.put(x + "," + y, 5); //water
				return 5;
			} else if (chunkRNGAverage < 0.47) {
				sceneManager.world.biomeMap.put(x + "," + y, 1); //grass
				return 1;
			} else if (chunkRNGAverage < 0.51) {
				sceneManager.world.biomeMap.put(x + "," + y, 2); //snow
				return 2;
			} else {
				sceneManager.world.biomeMap.put(x + "," + y, 3); //stone
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
			sceneManager.world.biomeMapFiltered.put(x + "," + y, 1);
			createBlockForBiome(x, y, 1);
		} else if (isLocationGreatest(biomeCount, 2)) {
			sceneManager.world.biomeMapFiltered.put(x + "," + y, 2);
			createBlockForBiome(x, y, 2);
		} else if (isLocationGreatest(biomeCount, 3)){
			sceneManager.world.biomeMapFiltered.put(x + "," + y, 3);
			createBlockForBiome(x, y, 3);
		} else if (isLocationGreatest(biomeCount, 4)){
			sceneManager.world.biomeMapFiltered.put(x + "," + y, 4);
			createBlockForBiome(x, y, 4);
		} else {
			sceneManager.world.biomeMapFiltered.put(x + "," + y, 5);
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
		sceneManager.world.lightMap.put(x + "," + y, 0.0);
		switch (biome) {
		case 1: //grass
			sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("grass"));
			break;
		case 2: //snow
			sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("snow"));
			break;
		case 3: //stone
			if (getChunkRNG(x, y) < 0.75) {
				sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("stone"));
			} else {
				sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("dirt"));
			}
			break;
		case 4: //desert
			sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("sand"));
			break;
		case 5: //water
			sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("water"));
			if (getChunkRNG(x, y) < 0.03) {
				sceneManager.world.playerStructuresMap.put(x + "," + y, blockInfo.getBlockID("lilyPad"));
			}
			break;
		default:
			sceneManager.world.map.put(x + "," + y, blockInfo.getBlockID("noTexture"));
			break;
		}
	}
}
