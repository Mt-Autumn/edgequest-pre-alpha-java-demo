package com.mtautumn.edgequest.generator;

import java.util.Random;

public class DrunkardsWalk {

	/*
	 * 
	 * Cellular automata that goes through a 2D array of ints and starts 'eating' walls (0s)
	 * 
	 */
	
	// RNG needs to be used persistently
	Random rng;
	
	int shortWalkPasses = 600;
	int longWalkPasses = 2000;
	
	public DrunkardsWalk(long seed) {
		this.rng = new Random(seed);
	}
	
	// Walk types
	
	public int[][] shortRandomWalk(int[][] map) {
		return walk(map, shortWalkPasses, 0.0f);
	}
	
	public int[][] longRandomWalk(int[][] map) {
		return walk(map, longWalkPasses, 0.0f);
	}
	
	public int[][] shortSemiDrunkWalk(int[][] map) {
		return walk(map, shortWalkPasses, 0.5f);
	}
	
	public int[][] longSemiDrunkWalk(int[][] map) {
		return walk(map, longWalkPasses, 0.5f);
	}
	
	public int[][] shortAntWalk(int[][] map) {
		return walk(map, shortWalkPasses, 0.75f);
	}
	
	public int[][] longAntWalk(int[][] map) {
		return walk(map, longWalkPasses, 0.75f);
	}
	
	
	// Does most of the heavy lifting
	public int[][] walk(int[][] map, int passes, float chaosChance) {
		
		int xMax = map.length;
		int yMax = map[0].length;
		
		// Get starting pos
		int x = this.rng.nextInt(xMax);
		int y = this.rng.nextInt(yMax);
		
		int[] oldDir = {0, 0};
		
		for (int i = 0; i < passes; i++) {
			
			if (map[x][y] == 0) { map[x][y] = 1; }
			
			int[] dir = changeDirection(chaosChance);
			if (dir[0] == 0 && dir[1] == 0) {
				dir = oldDir;
			} else {
				oldDir = dir;
			}
			
			x += dir[0];
			y += dir[1];
			
			if (x > xMax-1 || y > yMax-1 || x < 0 || y < 0) {x -= dir[0]; y -= dir[1]; }
			
		}
		
		return map;
		
	}
	
	// Direction changer
	// I mean... it's technically a cellular autonoma
	private int[] changeDirection(float chaosChance) {
		
		int[] i = new int[2];
		int r = this.rng.nextInt(4);
		
		if (this.rng.nextFloat() < chaosChance) {
			i[0] = 0;
			i[1] = 0;
		} else {
			if (r == 0) {
				i[0] = 0;
				i[1] = 1;
			} else if (r == 1) {
				i[0] = 0;
				i[1] = -1;
			} else if (r == 2 ) {
				i[0] = -1;
				i[1] = 0;
			} else {
				i[0] = 1;
				i[1] = 0;
			}
		}
		
		return i;
		
	}
	
}
