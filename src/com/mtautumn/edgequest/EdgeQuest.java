/* The class with the main method that gets everything going.
 * It sets the OS, creates the dataManager and starts all the threads.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

/*
 * 
 * EdgeQuest class that contains the main method that runs the game
 * 
 */

public class EdgeQuest {
	public static DataManager dataManager = new DataManager();
	public static void main(String[] args) throws InterruptedException {
		
		// Detect the OS
		byte os = (byte) System.getProperty("os.name").toLowerCase().charAt(0);
		
		switch (os) {
		case 108:
			System.out.println("Setting OS to GNU/Linux");
			dataManager.system.os = 0;
			break;
		case 109:
			System.out.println("Setting OS to macOS");
			dataManager.system.os = 1;
			break;
		case 119:
			System.out.println("Setting OS to Windows");
			dataManager.system.os = 2;
			break;
		default:
			break;
		}
		
		// Start the data manager (the main game loop)
		dataManager.start();
		
		//Waits for the game to load
		while(!dataManager.system.gameLoaded) {
			Thread.sleep(100);
		}
		dataManager.system.buttonActionQueue.add("fullScreen"); //Sets the game to full screen
	}
}
