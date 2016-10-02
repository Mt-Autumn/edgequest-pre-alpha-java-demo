package com.mtautumn.edgequest;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class MenuButtonManager {
	ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	SceneManager sceneManager;
	public MenuButtonManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
		buttonIDArray.add(new MenuButton(1,50,100,197,73,"setFPS"));
		buttonIDArray.add(new MenuButton(2,503,100,197,73,"setSeed"));
		buttonIDArray.add(new MenuButton(3,50,230,197,73,"regenWorld"));
		buttonIDArray.add(new MenuButton(4,503,230,197,73,"saveGame"));
		buttonIDArray.add(new MenuButton(5,50,360,197,73,"loadGame"));
		buttonIDArray.add(new MenuButton(6,503,360,197,73,"fullScreen"));
	}

	public void buttonPressed(int posX, int posY) {
		int adjustedX = posX - sceneManager.system.menuX;
		int adjustedY = posY - sceneManager.system.menuY;
		for (int i = 0; i < buttonIDArray.size(); i++) {
			MenuButton button = buttonIDArray.get(i);
			if (adjustedX > button.posX && adjustedX < button.posX + button.width && adjustedY > button.posY && adjustedY < button.posY + button.height) {
				runButtonAction(button.id);
			}
		}
	}
	private void runButtonAction(int id) {
		switch (id) {
		case 1:
			String ans = JOptionPane.showInputDialog("Set FPS:");
			try {
				int fps = Integer.parseInt(ans);
				if (fps > 0) {
					sceneManager.settings.targetFPS = fps;
				} else {
					JOptionPane.showMessageDialog(null, "FPS too low");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "FPS not valid");
			}
			break;
		case 2:
			String ans2 = JOptionPane.showInputDialog("Type Seed Number:");
			try {
				long seed = Long.parseLong(ans2);
				sceneManager.savable.seed = seed;
				JOptionPane.showMessageDialog(null, "Seed updated to: " + sceneManager.savable.seed);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Seed needs to be an whole number");
			}
			break;
		case 3:
			sceneManager.system.biomeMap.clear();
			sceneManager.savable.biomeMapFiltered.clear();
			sceneManager.savable.playerStructuresMap.clear();
			sceneManager.savable.map.clear();
			sceneManager.savable.lightMap.clear();
			sceneManager.savable.footPrints.clear();
			sceneManager.system.blockGenerationLastTick = true;
			JOptionPane.showMessageDialog(null, "World reset");
			break;
		case 4:
			String fileSaveName = JOptionPane.showInputDialog("FileName:");
			try {
				GameSaves.saveGame(fileSaveName, sceneManager);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unable to save game");
			}
			break;
		case 5:
			String fileLoadName = JOptionPane.showInputDialog("FileName:");
			try {
				GameSaves.loadGame(fileLoadName, sceneManager);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Unable to load game");
				e.printStackTrace();
			}
			break;
		case 6:
			if (sceneManager.settings.isFullScreen) {
				sceneManager.system.setWindowed = true;
			} else {
				sceneManager.system.setFullScreen = true;
			}
			for (int i = 0; i < buttonIDArray.size(); i++) {
				if (buttonIDArray.get(i).name == "windowed") buttonIDArray.set(i, new MenuButton(6,503,360,197,73,"fullScreen"));
				else if (buttonIDArray.get(i).name == "fullScreen") buttonIDArray.set(i, new MenuButton(6,503,360,197,73,"windowed"));
			}
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
		public int getPosX(int menuStartX) {
			return posX+menuStartX;
		}
		public int getPosY(int menuStartY) {
			return posY+menuStartY;
		}
	}
}

