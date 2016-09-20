package com.mtautumn.edgequest;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TextureManager {
	BufferedImage noTexture;
	BufferedImage grass;
	BufferedImage dirt;
	BufferedImage stone;
	BufferedImage water0;
	BufferedImage water1;
	BufferedImage water2;
	BufferedImage water3;
	
	BufferedImage waterSplash0;
	BufferedImage waterSplash1;
	BufferedImage waterSplash2;
	
	BufferedImage torch;
	
	BufferedImage characterUp;
	BufferedImage characterRight;
	BufferedImage characterDown;
	BufferedImage characterLeft;
	BufferedImage characterUpRight;
	BufferedImage characterUpLeft;
	BufferedImage characterDownRight;
	BufferedImage characterDownLeft;
	public TextureManager() {
		try {
			noTexture = ImageIO.read(new File("textures/noTexture.png"));
			grass = ImageIO.read(new File("textures/grass.png"));
			dirt = ImageIO.read(new File("textures/dirt.png"));
			stone = ImageIO.read(new File("textures/stone.png"));
			water0 = ImageIO.read(new File("textures/water0.png"));
			water1 = ImageIO.read(new File("textures/water1.png"));
			water2 = ImageIO.read(new File("textures/water2.png"));
			water3 = ImageIO.read(new File("textures/water3.png"));
			
			waterSplash0 = ImageIO.read(new File("textures/waterSplash0.png"));
			waterSplash1 = ImageIO.read(new File("textures/waterSplash1.png"));
			waterSplash2 = ImageIO.read(new File("textures/waterSplash2.png"));
			
			torch = ImageIO.read(new File("textures/torch.png"));
			
			characterUp = ImageIO.read(new File("textures/characterUp.png"));
			characterRight = ImageIO.read(new File("textures/characterRight.png"));
			characterDown = ImageIO.read(new File("textures/characterDown.png"));
			characterLeft = ImageIO.read(new File("textures/characterLeft.png"));
			characterUpRight = ImageIO.read(new File("textures/characterUpRight.png"));
			characterUpLeft = ImageIO.read(new File("textures/characterUpLeft.png"));
			characterDownRight = ImageIO.read(new File("textures/characterDownRight.png"));
			characterDownLeft = ImageIO.read(new File("textures/characterDownLeft.png"));
			
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			System.out.println("Error loading textures");
		}
	}
	public BufferedImage getTexture(int textureValue, SceneManager sceneManager) {
		switch (textureValue) {
		case 1:
			return grass;
		
		case 2:
			return dirt;
			
		case 3:
			return stone;
		
		case 4:
			switch (sceneManager.animationClock6Step % 6) {
			case 0:
				return water0;
			case 1:
				return water1;
			case 2:
				return water2;
			case 3:
				return water3;
			case 4:
				return water2;
			case 5:
				return water1;
			default:
				return water0;
			}
		case 5:
			return torch;
		case 6:
			switch ((sceneManager.animationClock6Step/2) % 3) {
			case 0:
				return waterSplash0;
			case 1:
				return waterSplash1;
			case 2:
				return waterSplash2;
			default:
				return waterSplash0;
			}
		default:
			return noTexture;
		}
			
	}
	public BufferedImage getCharacter(int direction) {
		switch (direction) {
		case 0:
			return characterUp;
		
		case 1:
			return characterUpRight;
			
		case 2:
			return characterRight;
		
		case 3:
			return characterDownRight;
			
		case 4:
			return characterDown;
		
		case 5:
			return characterDownLeft;
			
		case 6:
			return characterLeft;
		
		case 7:
			return characterUpLeft;
				
		default:
			return noTexture;
		}
	}
}
