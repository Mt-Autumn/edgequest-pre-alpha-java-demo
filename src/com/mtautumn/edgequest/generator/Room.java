package com.mtautumn.edgequest.generator;

// Rooms for the dungeon generator
public class Room {
	
	int width;
	int height;
	
	int xLoc;
	int yLoc;
	
	int[][] room;
	
	Center center;
	
	public Room(int width, int height, int xLoc, int yLoc) {
		// Dimensions of the room
		this.width = width;
		this.height = height;
		
		// Location of top left corner on map
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
		// 2D Array
		this.room = new int[width][height];
		
		// Set room array to all 1s, as 1s signify open floor tiles
		// NOTE: Maybe it's possible to remove this in the future, this is mostly wasted cpu cycles
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				this.room[w][h] = 1;
			}
		}
		
		// Center of the new room
		this.center = new Center((int) Math.floor(width / 2) + xLoc, (int) Math.floor(height / 2) + yLoc);
		
	}
	
	// Return the room array. Not really necessary for most uses
	public int[][] getRoom() {
		return this.room;
	}
	
	// Get the center of the room. Useful for making corridors
	public Center getCenter() {
		return this.center;
	}

}
