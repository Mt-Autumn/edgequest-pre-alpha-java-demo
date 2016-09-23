package com.mtautumn.edgequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class MenuButtonManager {
	ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	SceneManager sceneManager;
	public MenuButtonManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
		buttonIDArray.add(new MenuButton(1,50,100,200,50,"setFPS"));
		buttonIDArray.add(new MenuButton(2,500,100,200,50,"setSeed"));
		buttonIDArray.add(new MenuButton(3,50,200,200,50,"regenWorld"));
		buttonIDArray.add(new MenuButton(4,500,200,200,50,"saveGame"));
		buttonIDArray.add(new MenuButton(5,50,300,200,50,"loadGame"));
	}
	
	public void buttonPressed(int posX, int posY) {
		int adjustedX = posX - sceneManager.menuX;
		int adjustedY = posY - sceneManager.menuY;
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
					sceneManager.targetFPS = fps;
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
					sceneManager.seed = seed;
					JOptionPane.showMessageDialog(null, "Seed updated to: " + sceneManager.seed);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Seed needs to be an whole number");
			}
			break;
		case 3:
			sceneManager.biomeMap.clear();
			sceneManager.biomeMapFiltered.clear();
			sceneManager.playerStructuresMap.clear();
			sceneManager.map.clear();
			sceneManager.lightMap.clear();
			sceneManager.lightSourceMap.clear();
			sceneManager.blockGenerationLastTick = true;
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
		public BufferedImage buttonImage;
		public String name = "";
		public MenuButton(int id, int posX, int posY, int width, int height, String name) {
			this.posX = posX;
			this.posY = posY;
			this.width = width;
			this.height = height;
			this.name = name;
			this.id = id;
			try {
				this.buttonImage = ImageIO.read(new File("textures/" + name + ".png"));
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

