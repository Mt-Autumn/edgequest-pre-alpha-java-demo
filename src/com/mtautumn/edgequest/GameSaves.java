package com.mtautumn.edgequest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSaves {
	public static void saveGame(String saveFile, SceneManager sceneManager) throws IOException {
		FileOutputStream fout = new FileOutputStream(saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(sceneManager);
	}
	public static void loadGame(String saveFile, SceneManager sceneManager) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(fin);
		SceneManager loadedSM = (SceneManager) ois.readObject();
		sceneManager.time = loadedSM.time;
		sceneManager.charX = loadedSM.charX;
		sceneManager.charY = loadedSM.charY;
		sceneManager.map = loadedSM.map;
		sceneManager.biomeMap = loadedSM.biomeMap;
		sceneManager.biomeMapFiltered = loadedSM.biomeMapFiltered;
		sceneManager.lightMap = loadedSM.lightMap;
		sceneManager.lightSourceMap = loadedSM.lightSourceMap;
		sceneManager.blockGenerationLastTick = true;
		sceneManager.charDir = loadedSM.charDir;
		sceneManager.playerStructuresMap = loadedSM.playerStructuresMap;
		sceneManager.seed = loadedSM.seed;
	}
}
