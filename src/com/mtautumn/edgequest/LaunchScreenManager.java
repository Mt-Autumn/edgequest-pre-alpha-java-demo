package com.mtautumn.edgequest;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class LaunchScreenManager {
	ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	SceneManager sceneManager;
	public LaunchScreenManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
		buttonIDArray.add(new MenuButton(1,-247,-36,197,73,"newGame"));
		buttonIDArray.add(new MenuButton(2,50,-36,197,73,"loadGame"));
	}
	public void renderScreen(Graphics2D g2,TextureManager textureManager) {
		if (sceneManager.settings.screenWidth > 1.6 * sceneManager.settings.screenHeight) {
			g2.drawImage(textureManager.getTexture("launchScreenBackground"), 0, (int)(sceneManager.settings.screenHeight - sceneManager.settings.screenWidth / 1.6) / 2, sceneManager.settings.screenWidth,(int)(sceneManager.settings.screenWidth / 1.6),null);
		} else {
			g2.drawImage(textureManager.getTexture("launchScreenBackground"), (int)(sceneManager.settings.screenWidth - sceneManager.settings.screenHeight * 1.6)/2, 0, (int)(sceneManager.settings.screenHeight * 1.6),sceneManager.settings.screenHeight,null);

		}
		g2.drawImage(textureManager.getTexture("launchScreenLogo"), (sceneManager.settings.screenWidth / 2 - 200), 80, 400, 48, null);
		for (int i = 0; i<buttonIDArray.size(); i++) {
			MenuButton button = buttonIDArray.get(i);
			g2.drawImage(button.buttonImage, button.getPosX(sceneManager.settings.screenWidth), button.getPosY(sceneManager.settings.screenHeight), button.width, button.height, null);
		}
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
				sceneManager.world.seed = seed;
				sceneManager.world.biomeMap.clear();
				sceneManager.world.biomeMapFiltered.clear();
				sceneManager.world.playerStructuresMap.clear();
				sceneManager.world.map.clear();
				sceneManager.world.lightMap.clear();
				sceneManager.world.footPrints.clear();
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
		public int getPosX(int screenWidth) {
			return screenWidth / 2 + posX;
		}
		public int getPosY(int screenHeight) {
			return screenHeight / 2 + posY;
		}
	}

}
