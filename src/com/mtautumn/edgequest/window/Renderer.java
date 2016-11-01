package com.mtautumn.edgequest.window;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.TextureManager;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.managers.LaunchScreenManager;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;

public class Renderer {
	public DataManager dataManager;
	public TextureManager textureManager;
	public LaunchScreenManager launchScreenManager;
	public TrueTypeFont font;
	public TrueTypeFont font2;
	public TrueTypeFont backpackFont;
	public TrueTypeFont buttonFont;

	public Renderer(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void initGL(int width, int height) {
		try {
			DisplayMode displayMode = null;
			DisplayMode[] modes;
			try {
				modes = Display.getAvailableDisplayModes();

				for (int i = 0; i < modes.length; i++)
				{
					displayMode = modes[i];
				}
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			Display.setDisplayMode(displayMode);
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
		dataManager.menuButtonManager = new MenuButtonManager(dataManager);
		Font awtFont = new Font("Arial", Font.BOLD, 12);
		Font awtFont2 = new Font("Helvetica", Font.PLAIN, 36);
		Font awtBackpackFont = new Font("Helvetica", Font.BOLD, 12);
		font = new TrueTypeFont(awtFont, false);
		font2 = new TrueTypeFont(awtFont2, false);
		backpackFont = new TrueTypeFont(awtBackpackFont, false);
		try {
			Font awtButtonFont = Font.createFont(Font.TRUETYPE_FONT, new File("textures/fonts/buttons.otf")).deriveFont(42f);
			buttonFont = new TrueTypeFont(awtButtonFont, true);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double oldX = 800;
	private double oldY = 600;
	private boolean wasVSync = true;
	private DisplayMode oldDisplayMode;
	public void drawFrame() {
		if (dataManager.settings.vSyncOn && !wasVSync) {
			wasVSync = true;
			Display.setVSyncEnabled(true);
		} else if (!dataManager.settings.vSyncOn && wasVSync) {
			wasVSync = false;
			Display.setVSyncEnabled(false);
		}
		try {
			if (dataManager.settings.isFullScreen && !Display.isFullscreen()) {
				Display.setFullscreen(true);
				oldDisplayMode = Display.getDisplayMode();
				int largest = 0;
				int largestPos = 0;
				for (int i = 0; i < Display.getAvailableDisplayModes().length; i++) {
					if (Display.getAvailableDisplayModes()[i].getWidth() > largest) {
						largestPos = i;
						largest = Display.getAvailableDisplayModes()[i].getWidth();
					}
				}
				Display.setDisplayMode(Display.getAvailableDisplayModes()[largestPos]);
			} else if (!dataManager.settings.isFullScreen && Display.isFullscreen()) {
				Display.setFullscreen(false);
				Display.setDisplayMode(oldDisplayMode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		if (oldX != Display.getWidth() || oldY != Display.getHeight()) {
			glScaled(oldX/Display.getWidth(), oldY/Display.getHeight(), 1);
			oldX = Display.getWidth();
			oldY = Display.getHeight();
		}
		glClear(GL_COLOR_BUFFER_BIT);

		Layers.draw(this);

		Display.update();
		if (Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
	}


	public void fillRect(int x, int y, int width, int height, float r, float g, float b, float a) {
		Color.white.bind();
		glColor4f (r,g,b,a);
		glBegin(GL_QUADS);
		glVertex2f(x,y);
		glVertex2f(x+width,y);
		glVertex2f(x+width,y+height);
		glVertex2f(x,y+height);
		glEnd();
	}

	public void drawTexture(Texture texture, float x, float y, float width, float height) {
		Color.white.bind();
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
		Color.white.bind();
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
	private static float nearestPower2(float size) {
		int i = 1;
		for (; i < size; i *= 2);
		return i;
	}


	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
			if (dataManager.world.isGroundBlock(charX, charY)) {
				blockInfo[0] = dataManager.world.getGroundBlock(charX, charY);
			}
			if (dataManager.world.isStructBlock(charX, charY)) {
				blockInfo[1] = dataManager.world.getStructBlock(charX, charY);
			}
			if (dataManager.world.isLight(charX, charY)) {
				blockInfo[3] = dataManager.world.getLight(charX, charY);
			}
		return blockInfo;
	}
}