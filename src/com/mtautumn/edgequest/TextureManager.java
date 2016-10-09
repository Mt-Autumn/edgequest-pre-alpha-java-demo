package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.data.DataManager;

public class TextureManager {
	public Map<String, Texture> textureList = new HashMap<String, Texture>();
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
		addTexture("optionPane");
		addTexture("inputField");
		addTexture("blockHealthBar");
		addTexture("blockHealth");
	}
	public Texture getTexture(String texture) {
			return textureList.get(texture);
	}
	public Texture getAnimatedTexture(String texture, DataManager dataManager) {
		return textureList.get(texture + textureAnimations.get(texture)[dataManager.system.animationClock % textureAnimations.get(texture).length]);
	}

	public Texture getCharacter(int direction) {
		return getTexture("character" + direction);
	}
	private void addTexture(String name, int[] series) {
		for(int i = 0; i< series.length; i++) {
			addTexture(name + series[i]);
		}
	}
	private void addTexture(String name) {
		try {
			textureList.put(name, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/" + name + ".png")));
		} catch (Exception e) {
			System.out.println("Could not load texture: textures/" + name);
		}
	}
}
