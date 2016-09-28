package com.mtautumn.edgequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TextureManager {
	public Map<String, BufferedImage> textureList = new HashMap<String, BufferedImage>();
	public Map<String, int[]> textureAnimations = new HashMap<String, int[]>();
	
	public TextureManager() {
		addTexture("backpack");
		addTexture("character", new int[]{0,1,2,3,4,5,6,7});
		addTexture("cursor");
		addTexture("footsteps");
		addTexture("footsteps2");
		addTexture("footsteps3");
		addTexture("fullScreen");
		addTexture("launchScreenBackground");
		addTexture("launchScreenLogo");
		addTexture("loadGame");
		addTexture("menuBackground");
		addTexture("newGame");
		addTexture("none");
		addTexture("regenWorld");
		addTexture("saveGame");
		addTexture("select");
		addTexture("selectFar");
		addTexture("selectFlag");
		addTexture("setFPS");
		addTexture("setSeed");
		addTexture("waterSplash", new int[]{0,1,2});
		textureAnimations.put("waterSplash", new int[]{0,0,1,1,2,2});
		addTexture("windowed");
	}
	public BufferedImage getTexture(String texture) {
			return textureList.get(texture);
	}
	public BufferedImage getAnimatedTexture(String texture, SceneManager sceneManager) {
		return textureList.get(texture + textureAnimations.get(texture)[sceneManager.system.animationClock % textureAnimations.get(texture).length]);
	}

	public BufferedImage getCharacter(int direction) {
		return getTexture("character" + direction);
	}
	private void addTexture(String name, int[] series) {
		for(int i = 0; i< series.length; i++) {
			addTexture(name + series[i]);
		}
	}
	private void addTexture(String name) {
		try {
			textureList.put(name, (BufferedImage) ImageIO.read(new File("textures/" + name + ".png")));
		} catch (Exception e) {
			System.out.println("Could not load texture: textures/" + name);
		}
	}
}
