package com.mtautumn.edgequest.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSaves {
	public static void saveGame(String saveFile, DataManager dataManager) throws IOException {
		try {
			SavableData saveClass = dataManager.savable;
			FileOutputStream fout = new FileOutputStream(saveFile + ".egqst");
			@SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadGame(String saveFile, DataManager dataManager) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(fin);
		SavableData loadedSM = (SavableData) ois.readObject();
		dataManager.savable = loadedSM;
	}
}
