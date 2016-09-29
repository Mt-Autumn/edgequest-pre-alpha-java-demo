package com.mtautumn.edgequest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Renderer extends JComponent {
	private static SceneManager sceneManager;
	private static MenuButtonManager menuButtonManager;
	private static TextureManager textureManager = new TextureManager();
	public static LaunchScreenManager launchScreenManager;

	private static final long serialVersionUID = -1075263557773488547L;

	public Renderer(SceneManager scnMgr, MenuButtonManager mbm, LaunchScreenManager lsm) {
		sceneManager = scnMgr;
		menuButtonManager = mbm;
		launchScreenManager = lsm;
	}
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.DARK_GRAY);
		if (sceneManager.system.isGameOnLaunchScreen) {
			launchScreenManager.renderScreen(g2,textureManager);
		} else {
			drawTerrain(g2);
			drawFootprints(g2);
			drawCharacterEffects(g2);
			drawCharacter(g2);
			drawLighting(g2);
			if (!sceneManager.system.hideMouse) drawMouseSelection(g2);
			if (sceneManager.settings.showDiag) drawDiagnostics(g2);
			if (sceneManager.system.isKeyboardBackpack) drawBackpack(g2);
			if (sceneManager.system.isKeyboardMenu) drawMenu(g2);
		}
	}
	public void drawTerrain(Graphics2D g2) {
		int minTileX = sceneManager.system.minTileX;
		int minTileY = sceneManager.system.minTileY;
		int maxTileX = sceneManager.system.maxTileX;
		int maxTileY = sceneManager.system.maxTileY;
		double charX = sceneManager.savable.charX;
		double charY = sceneManager.savable.charY;
		int xPos = (int) ((minTileX - charX) * sceneManager.settings.blockSize + sceneManager.settings.screenWidth/2.0);
		for(int i = minTileX; i <= maxTileX; i++) {
			int yPos = (int)((minTileY - charY) * sceneManager.settings.blockSize + sceneManager.settings.screenHeight/2.0);
			for (int j = minTileY; j <= maxTileY; j++) {
				short blockValue;
				if (sceneManager.savable.map.containsKey(i + "," + j)) {
					blockValue = sceneManager.savable.map.get(i + "," + j);
				} else {
					blockValue = 0;
				}
				g2.drawImage(sceneManager.system.blockIDMap.get(blockValue).getBlockImg(sceneManager.savable.time),xPos, yPos, sceneManager.settings.blockSize, sceneManager.settings.blockSize, null);
				if (sceneManager.savable.playerStructuresMap.containsKey(i + "," + j)) {
					
					g2.drawImage(sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(i + "," + j)).getBlockImg(sceneManager.savable.time),xPos, yPos - sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(i + "," + j)).blockHeight * sceneManager.settings.blockSize, sceneManager.settings.blockSize, sceneManager.settings.blockSize + sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(i + "," + j)).blockHeight * sceneManager.settings.blockSize, null);
				}
				yPos += sceneManager.settings.blockSize;
			}
			xPos += sceneManager.settings.blockSize;
		}
	}
	public void drawFootprints(Graphics2D g2) {
		for (int i = 0; i < sceneManager.savable.footPrints.size(); i++) {
			FootPrint fp = sceneManager.savable.footPrints.get(i);
			double coordsOffsetX = sceneManager.savable.charX - Double.valueOf(sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
			double coordsOffsetY = sceneManager.savable.charY - Double.valueOf(sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
			int posX = (int)((fp.posX - coordsOffsetX)*sceneManager.settings.blockSize);
			int posY = (int)((fp.posY - coordsOffsetY)*sceneManager.settings.blockSize);
			g2.rotate(Math.PI / 4.0 * Double.valueOf(fp.direction) + Math.PI, posX, posY);
			if (fp.opacity > 0.4) {
				g2.drawImage(textureManager.getTexture("footsteps"), posX, posY, sceneManager.settings.blockSize / 3, (int)(sceneManager.settings.blockSize / 1.5), null);
			} else if (fp.opacity > 0.2) {
				g2.drawImage(textureManager.getTexture("footsteps2"), posX, posY, sceneManager.settings.blockSize / 3, (int)(sceneManager.settings.blockSize / 1.5), null);
			} else {
				g2.drawImage(textureManager.getTexture("footsteps3"), posX, posY, sceneManager.settings.blockSize / 3, (int)(sceneManager.settings.blockSize / 1.5), null);
			}
			g2.rotate(-Math.PI / 4.0 * Double.valueOf(fp.direction) - Math.PI, posX, posY);
		}
	}
	public void drawMouseSelection(Graphics2D g2) {
		double coordsOffsetX = sceneManager.savable.charX - Double.valueOf(sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
		double coordsOffsetY = sceneManager.savable.charY - Double.valueOf(sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
		int posX = (int)((sceneManager.system.mouseX - coordsOffsetX)*sceneManager.settings.blockSize);
		int posY = (int)((sceneManager.system.mouseY - coordsOffsetY)*sceneManager.settings.blockSize);
		if (sceneManager.system.isMouseFar) {
			g2.drawImage(textureManager.getTexture("selectFar"), posX, posY, sceneManager.settings.blockSize, sceneManager.settings.blockSize, null);
		} else {
			g2.drawImage(textureManager.getTexture("select"), posX, posY, sceneManager.settings.blockSize, sceneManager.settings.blockSize, null);
		}
		if (sceneManager.system.isKeyboardSprint) {
			g2.drawImage(textureManager.getTexture("selectFlag"), posX, posY - (int)(0.4375 * sceneManager.settings.blockSize), (int)(sceneManager.settings.blockSize * 1.25), (int)(sceneManager.settings.blockSize*1.4375), null);
		}
	}
	public void drawCharacterEffects(Graphics2D g2) {
		if (sceneManager.system.blockIDMap.get((short)getCharaterBlockInfo()[0]).isLiquid && getCharaterBlockInfo()[1] == 0.0) {
			g2.drawImage(textureManager.getAnimatedTexture("waterSplash", sceneManager), (int) ((sceneManager.settings.screenWidth- sceneManager.settings.blockSize) / 2.0), (int) ((sceneManager.settings.screenHeight - sceneManager.settings.blockSize) / 2.0), sceneManager.settings.blockSize, sceneManager.settings.blockSize, null);
		}
	}
	public void drawCharacter(Graphics2D g2) {
		g2.drawImage(textureManager.getCharacter(sceneManager.savable.charDir), (int) ((sceneManager.settings.screenWidth- sceneManager.settings.blockSize) / 2.0), (int) ((sceneManager.settings.screenHeight - sceneManager.settings.blockSize) / 2.0), sceneManager.settings.blockSize, sceneManager.settings.blockSize, null);

	}
	public void drawLighting(Graphics2D g2) {
		int minTileX = sceneManager.system.minTileX;
		int minTileY = sceneManager.system.minTileY;
		int maxTileX = sceneManager.system.maxTileX;
		int maxTileY = sceneManager.system.maxTileY;
		double charX = sceneManager.savable.charX;
		double charY = sceneManager.savable.charY;
		int xPos = (int) ((minTileX - charX) * sceneManager.settings.blockSize + sceneManager.settings.screenWidth/2.0);
		for(int i = minTileX; i <= maxTileX; i++) {
			int yPos = (int)((minTileY - charY) * sceneManager.settings.blockSize + sceneManager.settings.screenHeight/2.0);
			for (int j = minTileY; j <= maxTileY; j++) {
				double nightBrightness;
				double brightness;
				if (sceneManager.savable.lightMap.containsKey(i + "," + j)) {
					brightness = Double.valueOf(((int) sceneManager.savable.lightMap.get(i + "," + j) + 128)) / 255.0;
				} else {
					brightness = 0.0;
				}
				nightBrightness = (1 - sceneManager.savable.getBrightness()) * brightness + sceneManager.savable.getBrightness();
				if (sceneManager.savable.getBrightness() < 1) {
					g2.setColor(new Color(0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness)));
					g2.fillRect(xPos, yPos, sceneManager.settings.blockSize, sceneManager.settings.blockSize);
					g2.setColor(new Color(1.0f,0.6f,0.05f, (float)(0.2 * (nightBrightness - sceneManager.savable.getBrightness()))));
					g2.fillRect(xPos, yPos, sceneManager.settings.blockSize, sceneManager.settings.blockSize);
				}
				yPos += sceneManager.settings.blockSize;
			}
			xPos += sceneManager.settings.blockSize;
		}
	}
	public void drawDiagnostics(Graphics2D g2) {
		g2.setColor(new Color(0.7f,0.7f,0.7f, 0.7f));
		g2.fillRoundRect(10, 10, 250, 210, 8, 8);
		g2.setColor(new Color(0.0f,0.0f,0.0f, 0.7f));
		g2.drawString("FPS: " + sceneManager.system.averagedFPS, 20, 30);
		g2.drawString("Time: " + sceneManager.savable.time, 20, 50);
		g2.drawString("Time Human: " + sceneManager.system.timeReadable, 20, 70);
		g2.drawString("Brightness: " + sceneManager.savable.getBrightness(), 20, 90);
		g2.drawString("CharX: " + sceneManager.savable.charX, 20, 110);
		g2.drawString("CharY: " + sceneManager.savable.charY, 20, 130);
		g2.drawString("CharDir: " + sceneManager.savable.charDir, 20, 150);
		g2.drawString("CharMove: " + sceneManager.system.characterMoving, 20, 170);
		g2.drawString("TerrGen: " + sceneManager.system.blockGenerationLastTick, 20, 190);
		g2.drawString("Zoom: " + sceneManager.settings.blockSize, 20, 210);
	}
	public void drawMenu(Graphics2D g2) {
		g2.setColor(new Color(0.2f,0.2f,0.2f, 0.7f));
		g2.fillRect(0, 0, sceneManager.settings.screenWidth, sceneManager.settings.screenHeight);
		sceneManager.system.menuX = sceneManager.settings.screenWidth / 2 - 375;
		sceneManager.system.menuY = sceneManager.settings.screenHeight/2 - 250;
		g2.drawImage(textureManager.getTexture("menuBackground"), sceneManager.system.menuX, sceneManager.system.menuY, 750,500,null);
		for (int i = 0; i<menuButtonManager.buttonIDArray.size(); i++) {
			MenuButtonManager.MenuButton button = menuButtonManager.buttonIDArray.get(i);repaint();
			g2.drawImage(button.buttonImage, button.getPosX(sceneManager.system.menuX), button.getPosY(sceneManager.system.menuY), button.width, button.height, null);
		}
	}
	public void drawBackpack(Graphics2D g2) {
		g2.setColor(new Color(0.2f,0.2f,0.2f, 0.7f));
		g2.fillRect(0, 0, sceneManager.settings.screenWidth, sceneManager.settings.screenHeight);
		sceneManager.system.menuX = sceneManager.settings.screenWidth / 2 - 375;
		sceneManager.system.menuY = sceneManager.settings.screenHeight/2 - 250;
		g2.drawImage(textureManager.getTexture("backpack"), sceneManager.system.menuX, sceneManager.system.menuY, 750,500,null);
		for (int i = 0; i < sceneManager.savable.backpackItems.length; i++) {
			int posX = sceneManager.system.menuX + i * 64 + 37;
			for (int j = 0; j < sceneManager.savable.backpackItems[i].length; j++) {
				int posY = sceneManager.system.menuY + j * 65 + 94;
				try {
				g2.drawImage(sceneManager.system.blockIDMap.get(sceneManager.savable.backpackItems[i][j].getItemID()).getItemImg(sceneManager.savable.time), posX, posY, 48, 48, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(sceneManager.savable.charX);
		int charY = (int) Math.floor(sceneManager.savable.charY);
		if (sceneManager.savable.map.containsKey(charX + "," + charY)) {
			blockInfo[0] = sceneManager.savable.map.get(charX + "," + charY);
		}
		if (sceneManager.savable.playerStructuresMap.containsKey(charX + "," + charY)) {
			blockInfo[1] = sceneManager.savable.playerStructuresMap.get(charX + "," + charY);
		}
		if (sceneManager.savable.biomeMapFiltered.containsKey(charX + "," + charY)) {
			blockInfo[2] = sceneManager.savable.biomeMapFiltered.get(charX + "," + charY);
		}
		if (sceneManager.savable.lightMap.containsKey(charX + "," + charY)) {
			blockInfo[3] = sceneManager.savable.lightMap.get(charX + "," + charY);
		}
		return blockInfo;
	}
}