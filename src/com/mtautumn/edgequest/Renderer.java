package com.mtautumn.edgequest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Renderer extends JComponent {
	private static SceneManager sceneManager;
	private static TextureManager textureManager;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1075263557773488547L;
	
	public Renderer(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.DARK_GRAY);
		textureManager = new TextureManager();
		for(int i = sceneManager.minTileX; i <= sceneManager.maxTileX; i++) {
			int xPos = (int) ((i - sceneManager.charX) * sceneManager.blockSize + sceneManager.screenWidth/2.0);
			for (int j = sceneManager.minTileY; j <= sceneManager.maxTileY; j++) {
				int blockValue;
				double nightBrightness;
				if (sceneManager.map.containsKey(i + "," + j)) {
					blockValue = sceneManager.map.get(i + "," + j);
					nightBrightness = (1 - sceneManager.getBrightness()) * sceneManager.lightMap.get(i + "," + j) + sceneManager.getBrightness();
				} else {
					blockValue = 0;
					nightBrightness = 1;
				}
				g2.drawImage(textureManager.getTexture(blockValue, sceneManager.animationClock6Step),xPos, (int)((j - sceneManager.charY) * sceneManager.blockSize + sceneManager.screenHeight/2.0), sceneManager.blockSize + 1, sceneManager.blockSize + 1, null);
				if (sceneManager.playerStructuresMap.containsKey(i + "," + j)) {
					g2.drawImage(textureManager.getTexture(sceneManager.playerStructuresMap.get(i + "," + j), sceneManager.animationClock6Step),xPos, (int)((j - sceneManager.charY) * sceneManager.blockSize + sceneManager.screenHeight/2.0), sceneManager.blockSize + 1, sceneManager.blockSize + 1, null);
				}
				if (sceneManager.getBrightness() < 1) {
				g2.setColor(new Color(0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness)));
				g2.fillRect(xPos, (int)((j - sceneManager.charY) * sceneManager.blockSize + sceneManager.screenHeight/2.0), sceneManager.blockSize + 1, sceneManager.blockSize + 1);
				g2.setColor(new Color(1.0f,0.6f,0.05f, (float)(0.2 * (nightBrightness - sceneManager.getBrightness()))));
				g2.fillRect(xPos, (int)((j - sceneManager.charY) * sceneManager.blockSize + sceneManager.screenHeight/2.0), sceneManager.blockSize + 1, sceneManager.blockSize + 1);
				}
			}
		}
		
		
		
		g2.drawImage(textureManager.getCharacter(sceneManager.charDir), (int) ((sceneManager.screenWidth- sceneManager.blockSize) / 2.0), (int) ((sceneManager.screenHeight - sceneManager.blockSize) / 2.0), sceneManager.blockSize, sceneManager.blockSize, null);
		g2.setColor(new Color(0.01f,0.0f,0.2f,(float) (1.0 - sceneManager.getBrightness())));
		if (sceneManager.showDiag) {
			g2.setColor(new Color(0.7f,0.7f,0.7f, 0.7f));
			g2.fillRoundRect(10, 10, 250, 190, 8, 8);
			g2.setColor(new Color(0.0f,0.0f,0.0f, 0.7f));
			g2.drawString("Time: " + sceneManager.time, 20, 30);
			g2.drawString("Time Human: " + sceneManager.timeReadable, 20, 50);
			g2.drawString("Brightness: " + sceneManager.getBrightness(), 20, 70);
			g2.drawString("CharX: " + sceneManager.charX, 20, 90);
			g2.drawString("CharY: " + sceneManager.charY, 20, 110);
			g2.drawString("CharDir: " + sceneManager.charDir, 20, 130);
			g2.drawString("CharMove: " + sceneManager.characterMoving, 20, 150);
			g2.drawString("TerrGen: " + sceneManager.blockGenerationLastTick, 20, 170);
			g2.drawString("Zoom: " + sceneManager.blockSize, 20, 190);
		}
	}
	

}
