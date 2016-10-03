package com.mtautumn.edgequest.window;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.FootPrint;
import com.mtautumn.edgequest.MenuButtonManager;
import com.mtautumn.edgequest.LaunchScreenManager.MenuButton;

public class Layers {
	
	static void drawBackpack(Renderer r) {
		r.fillRect(0, 0, r.sceneManager.settings.screenWidth, r.sceneManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		r.sceneManager.system.menuX = r.sceneManager.settings.screenWidth / 2 - 375;
		r.sceneManager.system.menuY = r.sceneManager.settings.screenHeight/2 - 250;
		r.drawTexture(r.textureManager.getTexture("backpack"), r.sceneManager.system.menuX, r.sceneManager.system.menuY, 750,500);
		for (int i = 0; i < r.sceneManager.savable.backpackItems.length; i++) {
			int posX = r.sceneManager.system.menuX + i * 64 + 37;
			for (int j = 0; j < r.sceneManager.savable.backpackItems[i].length; j++) {
				int posY = r.sceneManager.system.menuY + j * 65 + 94;
				try {
					r.drawTexture(r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.backpackItems[i][j].getItemID()).getItemImg(r.sceneManager.savable.time), posX, posY, 48, 48);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	static void drawLaunchScreen(Renderer r) {
		if (r.sceneManager.settings.screenWidth > 1.6 * r.sceneManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.sceneManager.settings.screenHeight - r.sceneManager.settings.screenWidth / 1.6) / 2, r.sceneManager.settings.screenWidth,(int)(r.sceneManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.sceneManager.settings.screenWidth - r.sceneManager.settings.screenHeight * 1.6)/2, 0, (int)(r.sceneManager.settings.screenHeight * 1.6),r.sceneManager.settings.screenHeight);

		}
		r.drawTexture(r.textureManager.getTexture("launchScreenLogo"), (r.sceneManager.settings.screenWidth / 2 - 200), 80, 400, 48);
		for (int i = 0; i<r.launchScreenManager.buttonIDArray.size(); i++) {
			MenuButton button = r.launchScreenManager.buttonIDArray.get(i);
			r.drawTexture(button.buttonImage, button.getPosX(r.sceneManager.settings.screenWidth), button.getPosY(r.sceneManager.settings.screenHeight), button.width, button.height);
		}
	}
	
	
	
	static void drawTerrain(Renderer r) {
		Color.white.bind();
		int minTileX = r.sceneManager.system.minTileX;
		int minTileY = r.sceneManager.system.minTileY;
		int maxTileX = r.sceneManager.system.maxTileX;
		int maxTileY = r.sceneManager.system.maxTileY;
		double charX = r.sceneManager.savable.charX;
		double charY = r.sceneManager.savable.charY;
		int xPos = (int) ((minTileX - charX) * r.sceneManager.settings.blockSize + r.sceneManager.settings.screenWidth/2.0);
		for(int i = minTileX; i <= maxTileX; i++) {
			int yPos = (int)((minTileY - charY) * r.sceneManager.settings.blockSize + r.sceneManager.settings.screenHeight/2.0);
			for (int j = minTileY; j <= maxTileY; j++) {
				short blockValue;
				if (r.sceneManager.savable.map.containsKey(i + "," + j)) {
					blockValue = r.sceneManager.savable.map.get(i + "," + j);
				} else {
					blockValue = 0;
				}
				r.drawTexture(r.sceneManager.system.blockIDMap.get(blockValue).getBlockImg(r.sceneManager.savable.time),xPos, yPos, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize);
				if (r.sceneManager.savable.playerStructuresMap.containsKey(i + "," + j)) {
					r.drawTexture(r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.playerStructuresMap.get(i + "," + j)).getBlockImg(r.sceneManager.savable.time),xPos, yPos - r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.playerStructuresMap.get(i + "," + j)).blockHeight * r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize + r.sceneManager.system.blockIDMap.get(r.sceneManager.savable.playerStructuresMap.get(i + "," + j)).blockHeight * r.sceneManager.settings.blockSize);
				}
				yPos += r.sceneManager.settings.blockSize;
			}
			xPos += r.sceneManager.settings.blockSize;
		}
	}
	
	
	
	static void drawFootprints(Renderer r) {
	    Color.white.bind();
		for (int i = 0; i < r.sceneManager.savable.footPrints.size(); i++) {
			FootPrint fp = r.sceneManager.savable.footPrints.get(i);
			double coordsOffsetX = r.sceneManager.savable.charX - Double.valueOf(r.sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(r.sceneManager.settings.blockSize);
			double coordsOffsetY = r.sceneManager.savable.charY - Double.valueOf(r.sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(r.sceneManager.settings.blockSize);
			int posX = (int)((fp.posX - coordsOffsetX)*r.sceneManager.settings.blockSize);
			int posY = (int)((fp.posY - coordsOffsetY)*r.sceneManager.settings.blockSize);
			if (fp.opacity > 0.4) {
				r.drawTexture(r.textureManager.getTexture("footsteps"), posX - (float)r.sceneManager.settings.blockSize / 6f, posY - (float)r.sceneManager.settings.blockSize / 3f, (float)r.sceneManager.settings.blockSize / 3f, (float)r.sceneManager.settings.blockSize / 1.5f, 45f * Float.valueOf(fp.direction));
			} else if (fp.opacity > 0.2) {
				r.drawTexture(r.textureManager.getTexture("footsteps2"), posX - (float)r.sceneManager.settings.blockSize / 6f, posY - (float)r.sceneManager.settings.blockSize / 3f, (float)r.sceneManager.settings.blockSize / 3f, (float)r.sceneManager.settings.blockSize / 1.5f, 45f * Float.valueOf(fp.direction));

			} else {
				r.drawTexture(r.textureManager.getTexture("footsteps3"), posX - (float)r.sceneManager.settings.blockSize / 6f, posY - (float)r.sceneManager.settings.blockSize / 3f, (float)r.sceneManager.settings.blockSize / 3f, (float)r.sceneManager.settings.blockSize / 1.5f, 45f * Float.valueOf(fp.direction));
			}
		}
	}
	
	
	
	static void drawCharacterEffects(Renderer r) {
		Color.white.bind();
		if (r.sceneManager.system.blockIDMap.get((short)r.getCharaterBlockInfo()[0]).isLiquid && r.getCharaterBlockInfo()[1] == 0.0) {
			r.drawTexture(r.textureManager.getAnimatedTexture("waterSplash", r.sceneManager), (int) ((r.sceneManager.settings.screenWidth- r.sceneManager.settings.blockSize) / 2.0), (int) ((r.sceneManager.settings.screenHeight - r.sceneManager.settings.blockSize) / 2.0), r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize);
		}
	}
	
	
	
	static void drawMenu(Renderer r) {
		r.fillRect(0, 0, r.sceneManager.settings.screenWidth, r.sceneManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		r.sceneManager.system.menuX = r.sceneManager.settings.screenWidth / 2 - 375;
		r.sceneManager.system.menuY = r.sceneManager.settings.screenHeight/2 - 250;
		r.drawTexture(r.textureManager.getTexture("menuBackground"), r.sceneManager.system.menuX, r.sceneManager.system.menuY, 750,500);
		for (int i = 0; i<r.menuButtonManager.buttonIDArray.size(); i++) {
			MenuButtonManager.MenuButton button = r.menuButtonManager.buttonIDArray.get(i);
			r.drawTexture(button.buttonImage, button.getPosX(r.sceneManager.system.menuX), button.getPosY(r.sceneManager.system.menuY), button.width, button.height);
		}
	}
	
	
	
	static void drawDiagnostics(Renderer r) {
		Color.blue.bind();
		//TODO: Create class for generating texture from string
		r.fillRect(10,10, 215, 220, 0.7f, 0.7f, 0.7f, 0.7f);
		int i = 0;
		r.font.drawString(20, i+=20, "FPS: " + r.sceneManager.system.averagedFPS);
		r.font.drawString(20, i+=20, "Time: " + r.sceneManager.savable.time);
		r.font.drawString(20, i+=20, "Time Human: " + r.sceneManager.system.timeReadable);
		r.font.drawString(20, i+=20, "Brightness: " + r.sceneManager.savable.getBrightness());
		r.font.drawString(20, i+=20, "CharX: " + r.sceneManager.savable.charX);
		r.font.drawString(20, i+=20, "CharY: " + r.sceneManager.savable.charY);
		r.font.drawString(20, i+=20, "CharDir: " + r.sceneManager.savable.charDir);
		r.font.drawString(20, i+=20, "CharMove: " + r.sceneManager.system.characterMoving);
		r.font.drawString(20, i+=20, "Terrain Gen: " + r.sceneManager.system.blockGenerationLastTick);
		r.font.drawString(20, i+=20, "Block Size: " + r.sceneManager.settings.blockSize);
	}
	
	
	
	static void drawLighting(Renderer r) {
		Color.white.bind();
		int minTileX = r.sceneManager.system.minTileX;
		int minTileY = r.sceneManager.system.minTileY;
		int maxTileX = r.sceneManager.system.maxTileX;
		int maxTileY = r.sceneManager.system.maxTileY;
		double charX = r.sceneManager.savable.charX;
		double charY = r.sceneManager.savable.charY;
		int xPos = (int) ((minTileX - charX) * r.sceneManager.settings.blockSize + r.sceneManager.settings.screenWidth/2.0);
		for(int i = minTileX; i <= maxTileX; i++) {
			int yPos = (int)((minTileY - charY) * r.sceneManager.settings.blockSize + r.sceneManager.settings.screenHeight/2.0);
			for (int j = minTileY; j <= maxTileY; j++) {
				double nightBrightness;
				double brightness;
				if (r.sceneManager.savable.lightMap.containsKey(i + "," + j)) {
					brightness = Double.valueOf(((int) r.sceneManager.savable.lightMap.get(i + "," + j) + 128)) / 255.0;
				} else {
					brightness = 0.0;
				}
				nightBrightness = (1 - r.sceneManager.savable.getBrightness()) * brightness + r.sceneManager.savable.getBrightness();
				if (r.sceneManager.savable.getBrightness() < 1) {
					r.fillRect(xPos, yPos, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize, 0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness));
					float blockBrightness = (float)(0.2 * (nightBrightness - r.sceneManager.savable.getBrightness()));
					if (blockBrightness > 0) {
						r.fillRect(xPos, yPos, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize, 1.0f,0.6f,0.05f, blockBrightness);
					}
				}
				yPos += r.sceneManager.settings.blockSize;
			}
			xPos += r.sceneManager.settings.blockSize;
		}
	}
	
	
	
	static void drawMouseSelection(Renderer r) {
		Color.white.bind();
		double coordsOffsetX = r.sceneManager.savable.charX - Double.valueOf(r.sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(r.sceneManager.settings.blockSize);
		double coordsOffsetY = r.sceneManager.savable.charY - Double.valueOf(r.sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(r.sceneManager.settings.blockSize);
		int posX = (int)((r.sceneManager.system.mouseX - coordsOffsetX)*r.sceneManager.settings.blockSize);
		int posY = (int)((r.sceneManager.system.mouseY - coordsOffsetY)*r.sceneManager.settings.blockSize);
		if (r.sceneManager.system.isMouseFar) {
			r.drawTexture(r.textureManager.getTexture("selectFar"), posX, posY, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize);
		} else {
			r.drawTexture(r.textureManager.getTexture("select"), posX, posY, r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize);
		}
		if (r.sceneManager.system.isKeyboardSprint) {
			r.drawTexture(r.textureManager.getTexture("selectFlag"), posX, posY - (int)(0.4375 * r.sceneManager.settings.blockSize), (int)(r.sceneManager.settings.blockSize * 1.25), (int)(r.sceneManager.settings.blockSize*1.4375));
		}
	}
	
	
	
	static void drawCharacter(Renderer r) {
		Color.white.bind();
		r.drawTexture(r.textureManager.getCharacter(r.sceneManager.savable.charDir), (int) ((r.sceneManager.settings.screenWidth- r.sceneManager.settings.blockSize) / 2.0), (int) ((r.sceneManager.settings.screenHeight - r.sceneManager.settings.blockSize) / 2.0), r.sceneManager.settings.blockSize, r.sceneManager.settings.blockSize);

	}
}
