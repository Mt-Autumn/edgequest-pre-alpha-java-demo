package com.mtautumn.edgequest.window;

import java.awt.Font;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.TextureManager;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.managers.LaunchScreenManager;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;

public class Renderer {
	public DataManager dataManager;
	public MenuButtonManager menuButtonManager;
	public TextureManager textureManager;
	public LaunchScreenManager launchScreenManager;
	Font awtFont = new Font("Arial", Font.BOLD, 12);
	Font awtFont2 = new Font("Helvetica", Font.PLAIN, 36);
	public TrueTypeFont font;
	public TrueTypeFont font2;

	public Renderer(DataManager dataManager) {
		this.dataManager = dataManager;
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
	public void loadManagers() {
		textureManager = new TextureManager();
		launchScreenManager = new LaunchScreenManager(dataManager);
		menuButtonManager = new MenuButtonManager(dataManager);
		font = new TrueTypeFont(awtFont, false);
		font2 = new TrueTypeFont(awtFont2, false);
	}
	
	private double oldX = 800;
	private double oldY = 600;
	public void drawFrame() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		if (oldX != dataManager.settings.screenWidth || oldY != dataManager.settings.screenHeight) {
			glScaled(oldX/dataManager.settings.screenWidth, oldY/dataManager.settings.screenHeight, 1);
			oldX = dataManager.settings.screenWidth;
			oldY = dataManager.settings.screenHeight;
		}
		glClear(GL_COLOR_BUFFER_BIT);

		Layers.draw(this);

		Display.update();
		Display.sync(100);

		if (Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
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
	
	public void drawTexture(Texture texture, float x, float y, float width, float height) {
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
	public void drawTexture(Texture texture, float x, float y, float width, float height, float angle) {
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
	
	
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(dataManager.savable.charX);
		int charY = (int) Math.floor(dataManager.savable.charY);
		if (dataManager.savable.map.containsKey(charX + "," + charY)) {
			blockInfo[0] = dataManager.savable.map.get(charX + "," + charY);
		}
		if (dataManager.savable.playerStructuresMap.containsKey(charX + "," + charY)) {
			blockInfo[1] = dataManager.savable.playerStructuresMap.get(charX + "," + charY);
		}
		if (dataManager.savable.biomeMapFiltered.containsKey(charX + "," + charY)) {
			blockInfo[2] = dataManager.savable.biomeMapFiltered.get(charX + "," + charY);
		}
		if (dataManager.savable.lightMap.containsKey(charX + "," + charY)) {
			blockInfo[3] = dataManager.savable.lightMap.get(charX + "," + charY);
		}
		return blockInfo;
	}
}