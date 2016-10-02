package com.mtautumn.edgequest;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class LaunchScreenManager {
	ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	SceneManager sceneManager;
	public LaunchScreenManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
		buttonIDArray.add(new MenuButton(1,-247,-36,197,73,"newGame"));
		buttonIDArray.add(new MenuButton(2,50,-36,197,73,"loadGame"));
	}
	public void buttonPressed(int posX, int posY) {
		for (int i = 0; i < buttonIDArray.size(); i++) {
			MenuButton button = buttonIDArray.get(i);
			if (posX > button.getPosX(sceneManager.settings.screenWidth) && posX < button.getPosX(sceneManager.settings.screenWidth) + button.width && posY > button.getPosY(sceneManager.settings.screenHeight) && posY < button.getPosY(sceneManager.settings.screenHeight) + button.height) {
				runButtonAction(button.id);
			}
		}
	}
	private void runButtonAction(int id) {
		switch (id) {
		case 1: //New Game
			String ans2 = JOptionPane.showInputDialog("Type Seed Number:");
			try {
				long seed = Long.parseLong(ans2);
				sceneManager.savable.seed = seed;
				sceneManager.system.biomeMap.clear();
				sceneManager.savable.biomeMapFiltered.clear();
				sceneManager.savable.playerStructuresMap.clear();
				sceneManager.savable.map.clear();
				sceneManager.savable.lightMap.clear();
				sceneManager.savable.footPrints.clear();
				sceneManager.system.blockGenerationLastTick = true;
				sceneManager.system.isGameOnLaunchScreen = false;
				sceneManager.system.isLaunchScreenLoaded = false;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Seed needs to be an whole number");
			}
			break;
		case 2: //load game
			String fileLoadName = JOptionPane.showInputDialog("FileName:");
			try {
				GameSaves.loadGame(fileLoadName, sceneManager);
				sceneManager.system.isGameOnLaunchScreen = false;
				sceneManager.system.isLaunchScreenLoaded = false;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Unable to load game");
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	public class MenuButton {
		public int posX = 0;
		public int posY = 0;
		public int width = 0;
		public int height = 0;
		public int id = -1;
		public Texture buttonImage;
		public String name = "";
		public MenuButton(int id, int posX, int posY, int width, int height, String name) {
			this.posX = posX;
			this.posY = posY;
			this.width = width;
			this.height = height;
			this.name = name;
			this.id = id;
			try {
				this.buttonImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/" + name + ".png"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		public int getPosX(int screenWidth) {
			return screenWidth / 2 + posX;
		}
		public int getPosY(int screenHeight) {
			return screenHeight / 2 + posY;
		}
	}

}
