package com.mtautumn.edgequest;

import java.awt.Font;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.LaunchScreenManager.MenuButton;

public class Renderer {
	private static SceneManager sceneManager;
	public static MenuButtonManager menuButtonManager;
	private static TextureManager textureManager;
	public static LaunchScreenManager launchScreenManager;
	Font awtFont = new Font("Arial", Font.BOLD, 12);
	TrueTypeFont font;

	public Renderer(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public void fillRect(int x, int y, int width, int height, float r, float g, float b, float a) {
		glColor4f (r,g,b,a);
		glBegin(GL_QUADS);
		glVertex2f(x,y);
		glVertex2f(x+width,y);
		glVertex2f(x+width,y+height);
		glVertex2f(x,y+height);
		glEnd();
	}
	public void loadManagers() {
		textureManager = new TextureManager();
		launchScreenManager = new LaunchScreenManager(sceneManager);
		menuButtonManager = new MenuButtonManager(sceneManager);
		font = new TrueTypeFont(awtFont, false);
	}
	private double oldX = 800;
	private double oldY = 600;
	public void drawFrame() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		if (oldX != sceneManager.settings.screenWidth || oldY != sceneManager.settings.screenHeight) {
			glScaled(oldX/sceneManager.settings.screenWidth, oldY/sceneManager.settings.screenHeight, 1);
			oldX = sceneManager.settings.screenWidth;
			oldY = sceneManager.settings.screenHeight;
		}
		glClear(GL_COLOR_BUFFER_BIT);

		if (sceneManager.system.isGameOnLaunchScreen) {
			renderLaunchScreen();
		} else {
			drawTerrain();
			drawFootprints();
			drawCharacterEffects();
			drawCharacter();
			drawTexture(textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			drawLighting();
			if (!sceneManager.system.hideMouse) drawMouseSelection();
			drawTexture(textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			if (sceneManager.system.isKeyboardBackpack) drawBackpack();
			if (sceneManager.settings.showDiag) drawDiagnostics();
			if (sceneManager.system.isKeyboardMenu) drawMenu();
		}

		Display.update();
		Display.sync(100);

		if (Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
	}
	public void initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.create();
			Display.setVSyncEnabled(true);
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		glEnable(GL_TEXTURE_2D);               

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          
		glEnable(GL_COLOR_ARRAY);
		glColorMask(true, true, true, true);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glViewport(0,0,width,height);
		glMatrixMode(GL_MODELVIEW);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	private void drawTexture(Texture texture, float x, float y, float width, float height) {
		texture.bind();
		float paddingX = texture.getImageWidth();
		paddingX /= nearestPower2(paddingX);
		float paddingY = texture.getImageHeight();
		paddingY /= nearestPower2(paddingY);
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(x,y);
		glTexCoord2f(1f*paddingX,0);
		glVertex2f(x+width,y);
		glTexCoord2f(1f*paddingX,1f*paddingY);
		glVertex2f(x+width,y+height);
		glTexCoord2f(0,1f*paddingY);
		glVertex2f(x,y+height);
		glEnd();
	}
	private void drawTexture(Texture texture, float x, float y, float width, float height, float angle) {
		glPushMatrix();
		float halfWidth = width/2f;
		float halfHeight = height/2f;
		glTranslatef(x+halfWidth,y+halfHeight, 0);
		glRotatef( angle, 0, 0, 1 );
		float paddingX = texture.getImageWidth();
		paddingX /= nearestPower2(paddingX);
		float paddingY = texture.getImageHeight();
		paddingY /= nearestPower2(paddingY);
		texture.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(-halfWidth,-halfHeight);
		glTexCoord2f(1*paddingX,0);
		glVertex2f(+halfWidth,-halfHeight);
		glTexCoord2f(1*paddingX,1*paddingY);
		glVertex2f(+halfWidth,+halfHeight);
		glTexCoord2f(0,1*paddingY);
		glVertex2f(-halfWidth,+halfHeight);
		glEnd();

		glPopMatrix();
	}
	private float nearestPower2(float size) {
		int i = 1;
		for (; i < size; i *= 2);
		return i;
	}
	public void renderLaunchScreen() {
		if (sceneManager.settings.screenWidth > 1.6 * sceneManager.settings.screenHeight) {
			drawTexture(textureManager.getTexture("launchScreenBackground"), 0, (int)(sceneManager.settings.screenHeight - sceneManager.settings.screenWidth / 1.6) / 2, sceneManager.settings.screenWidth,(int)(sceneManager.settings.screenWidth / 1.6));
		} else {
			drawTexture(textureManager.getTexture("launchScreenBackground"), (int)(sceneManager.settings.screenWidth - sceneManager.settings.screenHeight * 1.6)/2, 0, (int)(sceneManager.settings.screenHeight * 1.6),sceneManager.settings.screenHeight);

		}
		drawTexture(textureManager.getTexture("launchScreenLogo"), (sceneManager.settings.screenWidth / 2 - 200), 80, 400, 48);
		for (int i = 0; i<launchScreenManager.buttonIDArray.size(); i++) {
			MenuButton button = launchScreenManager.buttonIDArray.get(i);
			drawTexture(button.buttonImage, button.getPosX(sceneManager.settings.screenWidth), button.getPosY(sceneManager.settings.screenHeight), button.width, button.height);
		}
	}
	private void drawTerrain() {
		Color.white.bind();
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
				drawTexture(sceneManager.system.blockIDMap.get(blockValue).getBlockImg(sceneManager.savable.time),xPos, yPos, sceneManager.settings.blockSize, sceneManager.settings.blockSize);
				if (sceneManager.savable.playerStructuresMap.containsKey(i + "," + j)) {

					drawTexture(sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(i + "," + j)).getBlockImg(sceneManager.savable.time),xPos, yPos - sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(i + "," + j)).blockHeight * sceneManager.settings.blockSize, sceneManager.settings.blockSize, sceneManager.settings.blockSize + sceneManager.system.blockIDMap.get(sceneManager.savable.playerStructuresMap.get(i + "," + j)).blockHeight * sceneManager.settings.blockSize);
				}
				yPos += sceneManager.settings.blockSize;
			}
			xPos += sceneManager.settings.blockSize;
		}
	}
		private void drawFootprints() {
	    Color.white.bind();
		for (int i = 0; i < sceneManager.savable.footPrints.size(); i++) {
			FootPrint fp = sceneManager.savable.footPrints.get(i);
			double coordsOffsetX = sceneManager.savable.charX - Double.valueOf(sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
			double coordsOffsetY = sceneManager.savable.charY - Double.valueOf(sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
			int posX = (int)((fp.posX - coordsOffsetX)*sceneManager.settings.blockSize);
			int posY = (int)((fp.posY - coordsOffsetY)*sceneManager.settings.blockSize);
			if (fp.opacity > 0.4) {
				drawTexture(textureManager.getTexture("footsteps"), posX - (float)sceneManager.settings.blockSize / 6f, posY - (float)sceneManager.settings.blockSize / 3f, (float)sceneManager.settings.blockSize / 3f, (float)sceneManager.settings.blockSize / 1.5f, 45f * Float.valueOf(fp.direction));
			} else if (fp.opacity > 0.2) {
				drawTexture(textureManager.getTexture("footsteps2"), posX - (float)sceneManager.settings.blockSize / 6f, posY - (float)sceneManager.settings.blockSize / 3f, (float)sceneManager.settings.blockSize / 3f, (float)sceneManager.settings.blockSize / 1.5f, 45f * Float.valueOf(fp.direction));

			} else {
				drawTexture(textureManager.getTexture("footsteps3"), posX - (float)sceneManager.settings.blockSize / 6f, posY - (float)sceneManager.settings.blockSize / 3f, (float)sceneManager.settings.blockSize / 3f, (float)sceneManager.settings.blockSize / 1.5f, 45f * Float.valueOf(fp.direction));
			}
		}
	}
	private void drawMouseSelection() {
		Color.white.bind();
		double coordsOffsetX = sceneManager.savable.charX - Double.valueOf(sceneManager.settings.screenWidth) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
		double coordsOffsetY = sceneManager.savable.charY - Double.valueOf(sceneManager.settings.screenHeight) / 2.0 / Double.valueOf(sceneManager.settings.blockSize);
		int posX = (int)((sceneManager.system.mouseX - coordsOffsetX)*sceneManager.settings.blockSize);
		int posY = (int)((sceneManager.system.mouseY - coordsOffsetY)*sceneManager.settings.blockSize);
		if (sceneManager.system.isMouseFar) {
			drawTexture(textureManager.getTexture("selectFar"), posX, posY, sceneManager.settings.blockSize, sceneManager.settings.blockSize);
		} else {
			drawTexture(textureManager.getTexture("select"), posX, posY, sceneManager.settings.blockSize, sceneManager.settings.blockSize);
		}
		if (sceneManager.system.isKeyboardSprint) {
			drawTexture(textureManager.getTexture("selectFlag"), posX, posY - (int)(0.4375 * sceneManager.settings.blockSize), (int)(sceneManager.settings.blockSize * 1.25), (int)(sceneManager.settings.blockSize*1.4375));
		}
	}
	private void drawCharacterEffects() {
		Color.white.bind();
		if (sceneManager.system.blockIDMap.get((short)getCharaterBlockInfo()[0]).isLiquid && getCharaterBlockInfo()[1] == 0.0) {
			drawTexture(textureManager.getAnimatedTexture("waterSplash", sceneManager), (int) ((sceneManager.settings.screenWidth- sceneManager.settings.blockSize) / 2.0), (int) ((sceneManager.settings.screenHeight - sceneManager.settings.blockSize) / 2.0), sceneManager.settings.blockSize, sceneManager.settings.blockSize);
		}
	}
	private void drawCharacter() {
		Color.white.bind();
		drawTexture(textureManager.getCharacter(sceneManager.savable.charDir), (int) ((sceneManager.settings.screenWidth- sceneManager.settings.blockSize) / 2.0), (int) ((sceneManager.settings.screenHeight - sceneManager.settings.blockSize) / 2.0), sceneManager.settings.blockSize, sceneManager.settings.blockSize);

	}
	public void drawLighting() {
		Color.white.bind();
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
					fillRect(xPos, yPos, sceneManager.settings.blockSize, sceneManager.settings.blockSize, 0.01f,0.0f,0.15f,(float) (1.0 - nightBrightness));
					float blockBrightness = (float)(0.2 * (nightBrightness - sceneManager.savable.getBrightness()));
					if (blockBrightness > 0) {
						fillRect(xPos, yPos, sceneManager.settings.blockSize, sceneManager.settings.blockSize, 1.0f,0.6f,0.05f, blockBrightness);
					}
				}
				yPos += sceneManager.settings.blockSize;
			}
			xPos += sceneManager.settings.blockSize;
		}
	}
	private void drawDiagnostics() {
		Color.blue.bind();
		//TODO: Create class for generating texture from string
		fillRect(10,10, 215, 220, 0.7f, 0.7f, 0.7f, 0.7f);
		int i = 0;
		font.drawString(20, i+=20, "FPS: " + sceneManager.system.averagedFPS);
		font.drawString(20, i+=20, "Time: " + sceneManager.savable.time);
		font.drawString(20, i+=20, "Time Human: " + sceneManager.system.timeReadable);
		font.drawString(20, i+=20, "Brightness: " + sceneManager.savable.getBrightness());
		font.drawString(20, i+=20, "CharX: " + sceneManager.savable.charX);
		font.drawString(20, i+=20, "CharY: " + sceneManager.savable.charY);
		font.drawString(20, i+=20, "CharDir: " + sceneManager.savable.charDir);
		font.drawString(20, i+=20, "CharMove: " + sceneManager.system.characterMoving);
		font.drawString(20, i+=20, "Terrain Gen: " + sceneManager.system.blockGenerationLastTick);
		font.drawString(20, i+=20, "Block Size: " + sceneManager.settings.blockSize);
	}
	public void drawMenu() {
		fillRect(0, 0, sceneManager.settings.screenWidth, sceneManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		sceneManager.system.menuX = sceneManager.settings.screenWidth / 2 - 375;
		sceneManager.system.menuY = sceneManager.settings.screenHeight/2 - 250;
		drawTexture(textureManager.getTexture("menuBackground"), sceneManager.system.menuX, sceneManager.system.menuY, 750,500);
		for (int i = 0; i<menuButtonManager.buttonIDArray.size(); i++) {
			MenuButtonManager.MenuButton button = menuButtonManager.buttonIDArray.get(i);
			drawTexture(button.buttonImage, button.getPosX(sceneManager.system.menuX), button.getPosY(sceneManager.system.menuY), button.width, button.height);
		}
	}
	private void drawBackpack() {
		fillRect(0, 0, sceneManager.settings.screenWidth, sceneManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		Color.white.bind();
		sceneManager.system.menuX = sceneManager.settings.screenWidth / 2 - 375;
		sceneManager.system.menuY = sceneManager.settings.screenHeight/2 - 250;
		drawTexture(textureManager.getTexture("backpack"), sceneManager.system.menuX, sceneManager.system.menuY, 750,500);
		for (int i = 0; i < sceneManager.savable.backpackItems.length; i++) {
			int posX = sceneManager.system.menuX + i * 64 + 37;
			for (int j = 0; j < sceneManager.savable.backpackItems[i].length; j++) {
				int posY = sceneManager.system.menuY + j * 65 + 94;
				try {
					drawTexture(sceneManager.system.blockIDMap.get(sceneManager.savable.backpackItems[i][j].getItemID()).getItemImg(sceneManager.savable.time), posX, posY, 48, 48);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private double[] getCharaterBlockInfo() {
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