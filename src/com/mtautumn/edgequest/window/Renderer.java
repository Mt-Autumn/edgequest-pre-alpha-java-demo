package com.mtautumn.edgequest.window;

import java.awt.Font;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.LaunchScreenManager;
import com.mtautumn.edgequest.MenuButtonManager;
import com.mtautumn.edgequest.SceneManager;
import com.mtautumn.edgequest.TextureManager;

import static com.mtautumn.edgequest.window.Layers.*;

public class Renderer {
	SceneManager sceneManager;
	MenuButtonManager menuButtonManager;
	TextureManager textureManager;
	LaunchScreenManager launchScreenManager;
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
			drawLaunchScreen(this);
		} else {
			drawTerrain(this);
			drawFootprints(this);
			drawCharacterEffects(this);
			drawCharacter(this);
			drawTexture(textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			drawLighting(this);
			if (!sceneManager.system.hideMouse) drawMouseSelection(this);
			drawTexture(textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			if (sceneManager.system.isKeyboardBackpack) drawBackpack(this);
			if (sceneManager.settings.showDiag) drawDiagnostics(this);
			if (sceneManager.system.isKeyboardMenu) drawMenu(this);
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
	void drawTexture(Texture texture, float x, float y, float width, float height) {
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
	void drawTexture(Texture texture, float x, float y, float width, float height, float angle) {
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
	double[] getCharaterBlockInfo() {
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