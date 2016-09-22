package com.mtautumn.edgequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TextureManager {
	public Map<String, BufferedImage> textureList = new HashMap<String, BufferedImage>();
	BlockInformation blockInfo = new BlockInformation();
	
	public TextureManager() {
		String[] textureNames = blockInfo.getBlockList();
		for(int i = 0; i < textureNames.length; i++) {
			try {
				textureList.put(textureNames[i], (BufferedImage) ImageIO.read(new File("textures/" + textureNames[i] + ".png")));
			} catch (Exception e) {
				System.out.println("Could not load texture: " + textureNames[i]);
			}
		}
	}
	public BufferedImage getBlockTexture(int textureID, SceneManager sceneManager) {
		if (blockInfo.isBlockAnimated(textureID)) {
			return textureList.get(blockInfo.getBlockName(textureID) + blockInfo.getBlockAnimation(textureID)[(sceneManager.animationClock60Step % blockInfo.getBlockAnimation(textureID).length)]);
		} else {
			return textureList.get(blockInfo.getBlockName(textureID));
		}
	}
	public BufferedImage getBlockTexture(int textureID) {
		if (blockInfo.isBlockAnimated(textureID)) {
			return textureList.get(blockInfo.getBlockName(textureID) + blockInfo.getBlockAnimation(textureID)[0]);
		} else {
			return textureList.get(blockInfo.getBlockName(textureID));
		}
	}
	public BufferedImage getTexture(int textureValue, SceneManager sceneManager) {
		return getBlockTexture(textureValue, sceneManager);
			
	}
	public BufferedImage getCharacter(int direction) {
		return getBlockTexture(direction + 200);
	}
}
