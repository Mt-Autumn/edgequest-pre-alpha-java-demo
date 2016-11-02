package com.mtautumn.edgequest.generator;

public class Cave {
	
	/* 
	 * 
	 * Set of functions to apply to a special cave map that is a 2D array of floating point values
	 * 
	 * Just use makeAndApplyCave and you'll be set basically
	 * 
	 */
	
	// Easiest way to make a cave and apply
	public int[][] makeAndApplyCave(int[][] dunMap, long seed, float thresh) {
		
		// Do everything
		float[][] caveMap = applyThreshold(getNoise(initCaveMap(dunMap.length, dunMap[0].length), seed), thresh);
		return overlayCave(caveMap, dunMap);
		
	}
	
	// Create a basic 2D map of floats for the cave noise to be applied too
	private float[][] initCaveMap(int x, int y) {
		float[][] caveMap = new float[x][y];
		
		// Init to 0
		for (float[] row : caveMap) {
			for (float ele: row) {
				row[(int) ele] = 0.0f;
			}
		}
		
		return caveMap;
	}
	
	// Generate noise over the cave map
	private float[][] getNoise(float[][] caveMap, long seed) {
		SimplexNoise s = new SimplexNoise();
		return s.generateSimplexNoise((int) caveMap[0].length, (int) caveMap.length, seed);
	}

	// Apply a threshold to polarize the noise map
	private float[][] applyThreshold(float[][] caveMap, float thresh) {
		for (int i = 0; i < caveMap.length ; i++) {
			for (int j = 0; j < caveMap[0].length; j++) {
				if (caveMap[i][j] > thresh) {
					caveMap[i][j] = 1.0f;
				} else {
					caveMap[i][j] = 0.0f;
				}
			}
		}
		return caveMap;
	}
	
	// Overlay a cave map on top of a dungeon map.
	// NOTE: This function assumes that the 
	// cave map and dungeon map are of the same dimensions and that a 
	// threshold has been applied to the cave map
	private int[][] overlayCave(float[][] caveMap, int[][] dunMap) {
		for (int i = 0; i < caveMap.length ; i++) {
			for (int j = 0; j < caveMap[0].length; j++) {
				// Only knock down walls
				if (caveMap[i][j] == 1.0f && dunMap[i][j] == 0) {
					dunMap[i][j] = 1;
				}
			}
		}
		return dunMap;
	}
	
}
