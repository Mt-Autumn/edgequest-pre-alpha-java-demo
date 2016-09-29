package com.mtautumn.edgequest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSaves {
	public static void saveGame(String saveFile, SceneManager sceneManager) throws IOException {
		try {
			Savable saveClass = sceneManager.savable;
			FileOutputStream fout = new FileOutputStream(saveFile + ".egqst");
			@SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadGame(String saveFile, SceneManager sceneManager) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(fin);
		Savable loadedSM = (Savable) ois.readObject();
		sceneManager.savable = loadedSM;
	}
}
