package com.mtautumn.edgequest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Renderer extends JComponent {
	private static SceneManager sceneManager;
	private static MenuButtonManager menuButtonManager;
	private static TextureManager textureManager = new TextureManager();;
	private static BlockInformation blockInfo = new BlockInformation();

	private static final long serialVersionUID = -1075263557773488547L;

	public Renderer(SceneManager scnMgr, MenuButtonManager mbm) {
		sceneManager = scnMgr;
		menuButtonManager = mbm;
	}
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.DARK_GRAY);
		drawTerrain(g2);
		drawFootprints(g2);
		drawCharacterEffects(g2);
		drawCharacter(g2);
		drawLighting(g2);
		if (sceneManager.showDiag) drawDiagnostics(g2);
		if (sceneManager.isEscToggled) drawMenu(g2);
	}
	public void drawTerrain(Graphics2D g2) {
		int minTileX = sceneManager.minTileX;
		int minTileY = sceneManager.minTileY;
		int maxTileX = sceneManager.maxTileX;
		int maxTileY = sceneManager.maxTileY;
		double charX = sceneManager.charX;
		double charY = sceneManager.charY;
		int xPos = (int) ((minTileX - charX) * sceneManager.blockSize + sceneManager.screenWidth/2.0);
		for(int i = minTileX; i <= maxTileX; i++) {
			int yPos = (int)((minTileY - charY) * sceneManager.blockSize + sceneManager.screenHeight/2.0);
			for (int j = minTileY; j <= maxTileY; j++) {
				int blockValue;
				if (sceneManager.map.containsKey(i + "," + j)) {
					blockValue = sceneManager.map.get(i + "," + j);
				} else {
					blockValue = 0;
				}
				g2.drawImage(textureManager.getTexture(blockValue, sceneManager),xPos, yPos, sceneManager.blockSize, sceneManager.blockSize, null);
				if (sceneManager.playerStructuresMap.containsKey(i + "," + j)) {
					g2.drawImage(textureManager.getTexture(sceneManager.playerStructuresMap.get(i + "," + j), sceneManager),xPos, yPos, sceneManager.blockSize, sceneManager.blockSize, null);
				}
				yPos += sceneManager.blockSize;
			}
			xPos += sceneManager.blockSize;
		}
	}
	public void drawFootprints(Graphics2D g2) {
		for (int i = 0; i < sceneManager.footPrints.size(); i++) {
			FootPrint fp = sceneManager.footPrints.get(i);
			double coordsOffsetX = sceneManager.charX - Double.valueOf(sceneManager.screenWidth) / 2.0 / Double.valueOf(sceneManager.blockSize);
			double coordsOffsetY = sceneManager.charY - Double.valueOf(sceneManager.screenHeight) / 2.0 / Double.valueOf(sceneManager.blockSize);
			int posX = (int)((fp.posX - coordsOffsetX)*sceneManager.blockSize);
			int posY = (int)((fp.posY - coordsOffsetY)*sceneManager.blockSize);
			g2.rotate(Math.PI / 4.0 * Double.valueOf(fp.direction) + Math.PI, posX, posY);
			if (fp.opacity > 0.4) {
				g2.drawImage(textureManager.getTexture("footsteps", sceneManager), posX, posY, sceneManager.blockSize / 3, (int)(sceneManager.blockSize / 1.5), null);
			} else if (fp.opacity > 0.2) {
				g2.drawImage(textureManager.getTexture("footsteps2", sceneManager), posX, posY, sceneManager.blockSize / 3, (int)(sceneManager.blockSize / 1.5), null);
			} else {
				g2.drawImage(textureManager.getTexture("footsteps3", sceneManager), posX, posY, sceneManager.blockSize / 3, (int)(sceneManager.blockSize / 1.5), null);
			}
			g2.rotate(-Math.PI / 4.0 * Double.valueOf(fp.direction) - Math.PI, posX, posY);
		}
	}
	public void drawCharacterEffects(Graphics2D g2) {
		if (getCharaterBlockInfo()[0] == blockInfo.getBlockID("water") && getCharaterBlockInfo()[1] == 0.0) {
			g2.drawImage(textureManager.getTexture("waterSplash", sceneManager), (int) ((sceneManager.screenWidth- sceneManager.blockSize) / 2.0), (int) ((sceneManager.screenHeight - sceneManager.blockSize) / 2.0), sceneManager.blockSize, sceneManager.blockSize, null);
		}
	}
	public void drawCharacter(Graphics2D g2) {
		g2.drawImage(textureManager.getCharacter(sceneManager.charDir), (int) ((sceneManager.screenWidth- sceneManager.blockSize) / 2.0), (int) ((sceneManager.screenHeight - sceneManager.blockSize) / 2.0), sceneManager.blockSize, sceneManager.blockSize, null);

	}
	public void drawLighting(Graphics2D g2) {
		int minTileX = sceneManager.minTileX;
		int minTileY = sceneManager.minTileY;
		int maxTileX = sceneManager.maxTileX;
		int maxTileY = sceneManager.maxTileY;
		double charX = sceneManager.charX;
		double charY = sceneManager.charY;
		int xPos = (int) ((minTileX - charX) * sceneManager.blockSize + sceneManager.screenWidth/2.0);
		for(int i = minTileX; i <= maxTileX; i++) {
			int yPos = (int)((minTileY - charY) * sceneManager.blockSize + sceneManager.screenHeight/2.0);
			for (int j = minTileY; j <= maxTileY; j++) {
				double nightBrightness;
				if (sceneManager.lightMap.containsKey(i + "," + j)) {
					nightBrightness = (1 - sceneManager.getBrightness()) * sceneManager.lightMap.get(i + "," + j) + sceneManager.getBrightness();
				} else {
					nightBrightness = 1;
				}
				if (sceneManager.getBrightness() < 1) {
					g2.setColor(new Color(0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness)));
					g2.fillRect(xPos, yPos, sceneManager.blockSize, sceneManager.blockSize);
					g2.setColor(new Color(1.0f,0.6f,0.05f, (float)(0.2 * (nightBrightness - sceneManager.getBrightness()))));
					g2.fillRect(xPos, yPos, sceneManager.blockSize, sceneManager.blockSize);
				}
				yPos += sceneManager.blockSize;
			}
			xPos += sceneManager.blockSize;
		}
	}
	public void drawDiagnostics(Graphics2D g2) {
		g2.setColor(new Color(0.7f,0.7f,0.7f, 0.7f));
		g2.fillRoundRect(10, 10, 250, 210, 8, 8);
		g2.setColor(new Color(0.0f,0.0f,0.0f, 0.7f));
		g2.drawString("FPS: " + sceneManager.averagedFPS, 20, 30);
		g2.drawString("Time: " + sceneManager.time, 20, 50);
		g2.drawString("Time Human: " + sceneManager.timeReadable, 20, 70);
		g2.drawString("Brightness: " + sceneManager.getBrightness(), 20, 90);
		g2.drawString("CharX: " + sceneManager.charX, 20, 110);
		g2.drawString("CharY: " + sceneManager.charY, 20, 130);
		g2.drawString("CharDir: " + sceneManager.charDir, 20, 150);
		g2.drawString("CharMove: " + sceneManager.characterMoving, 20, 170);
		g2.drawString("TerrGen: " + sceneManager.blockGenerationLastTick, 20, 190);
		g2.drawString("Zoom: " + sceneManager.blockSize, 20, 210);
	}
	public void drawMenu(Graphics2D g2) {
		g2.setColor(new Color(0.3f,0.3f,0.3f, 0.5f));
		g2.fillRect(0, 0, sceneManager.screenWidth, sceneManager.screenHeight);
		sceneManager.menuX = sceneManager.screenWidth / 2 - 375;
		sceneManager.menuY = sceneManager.screenHeight/2 - 250;
		g2.drawImage(textureManager.getTexture("menuBackground", sceneManager), sceneManager.menuX, sceneManager.menuY, 750,500,null);
		for (int i = 0; i<menuButtonManager.buttonIDArray.size(); i++) {
			MenuButtonManager.MenuButton button = menuButtonManager.buttonIDArray.get(i);repaint();
			g2.setColor(Color.WHITE);
			g2.fillRect(button.getPosX(sceneManager.menuX), button.getPosY(sceneManager.menuY), button.width, button.height);
			g2.drawImage(button.buttonImage, button.getPosX(sceneManager.menuX), button.getPosY(sceneManager.menuY), button.width, button.height, null);
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(sceneManager.charX);
		int charY = (int) Math.floor(sceneManager.charY);
		if (sceneManager.map.containsKey(charX + "," + charY)) {
			blockInfo[0] = sceneManager.map.get(charX + "," + charY);
		}
		if (sceneManager.playerStructuresMap.containsKey(charX + "," + charY)) {
			blockInfo[1] = sceneManager.playerStructuresMap.get(charX + "," + charY);
		}
		if (sceneManager.biomeMapFiltered.containsKey(charX + "," + charY)) {
			blockInfo[2] = sceneManager.biomeMapFiltered.get(charX + "," + charY);
		}
		if (sceneManager.lightMap.containsKey(charX + "," + charY)) {
			blockInfo[3] = sceneManager.lightMap.get(charX + "," + charY);
		}
		return blockInfo;
	}
}