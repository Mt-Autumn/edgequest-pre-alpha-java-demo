package com.mtautumn.edgequest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSaves {
	public static void saveGame(String saveFile, SceneManager sceneManager) throws IOException {
		try {
		FileOutputStream fout = new FileOutputStream(saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(sceneManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadGame(String saveFile, SceneManager sceneManager) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(fin);
		SceneManager loadedSM = (SceneManager) ois.readObject();
		sceneManager.world.time = loadedSM.world.time;
		sceneManager.system.charX = loadedSM.system.charX;
		sceneManager.system.charY = loadedSM.system.charY;
		sceneManager.world.map = loadedSM.world.map;
		sceneManager.world.biomeMap = loadedSM.world.biomeMap;
		sceneManager.world.biomeMapFiltered = loadedSM.world.biomeMapFiltered;
		sceneManager.world.lightMap = loadedSM.world.lightMap;
		sceneManager.world.lightSourceMap = loadedSM.world.lightSourceMap;
		sceneManager.system.blockGenerationLastTick = true;
		sceneManager.system.charDir = loadedSM.system.charDir;
		sceneManager.world.playerStructuresMap = loadedSM.world.playerStructuresMap;
		sceneManager.world.seed = loadedSM.world.seed;
		sceneManager.world.footPrints = loadedSM.world.footPrints;
	}
}
