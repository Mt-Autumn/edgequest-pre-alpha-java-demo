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
	
	public int[][] walk(int[][] map, int passes) {
		
		int xMax = map.length;
		int yMax = map[0].length;
		
		// Get starting pos
		int x = this.rng.nextInt(xMax);
		int y = this.rng.nextInt(yMax);
		
		for (int i = 0; i < passes; i++) {
			
			if (map[x][y] == 0) { map[x][y] = 1; }
			
			int[] dir = changeDirection();
			x += dir[0];
			y += dir[1];
			
			if (x > xMax-1 || y > yMax-1 || x < 0 || y < 0) {x -= dir[0]; y -= dir[1]; }
			
		}
		
		return map;
		
	}
	
	public int[][] shortWalk(int[][] map) {
		
		return walk(map, shortWalkPasses);
		
	}
	
	public int[][] longWalk(int[][] map) {
		return walk(map, longWalkPasses);
	}
	
	private int[] changeDirection() {
		
		int[] i = new int[2];
		int r = this.rng.nextInt(4);
		
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
		
		return i;
		
	}
	
}
