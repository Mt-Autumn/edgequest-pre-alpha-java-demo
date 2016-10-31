package com.mtautumn.edgequest.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.mtautumn.edgequest.Character;
import com.mtautumn.edgequest.Entity;

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
		
		// Set everything up
		Entity characterEntity = null;
		for (int i = 0; i < dataManager.savable.entities.size(); i++) {
			dataManager.savable.entities.get(i).initializeClass(dataManager);
			if (dataManager.savable.entities.get(i).getType() == Entity.EntityType.character) {
				characterEntity = dataManager.savable.entities.get(i);
			}
		}
		dataManager.characterManager.characterEntity = (Character) characterEntity;
		
	}
}
