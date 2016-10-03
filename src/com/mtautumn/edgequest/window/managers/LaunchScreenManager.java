package com.mtautumn.edgequest.window.managers;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.SceneManager;


public class LaunchScreenManager {
	public ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	SceneManager sceneManager;
	public LaunchScreenManager(SceneManager scnMgr) {
		sceneManager = scnMgr;
		buttonIDArray.add(new MenuButton(1,-247,-36,197,73,"newGame"));
		buttonIDArray.add(new MenuButton(2,50,-36,197,73,"loadGame"));
	}
	public void buttonPressed(int posX, int posY) {
		for (int i = 0; i < buttonIDArray.size(); i++) {
			MenuButton button = buttonIDArray.get(i);
			if (posX > button.getPosX(sceneManager.settings.screenWidth) && posX < button.getPosX(sceneManager.settings.screenWidth) + button.width && posY > button.getPosY(sceneManager.settings.screenHeight) && posY < button.getPosY(sceneManager.settings.screenHeight) + button.height) {
				runButtonAction(button.id);
			}
		}
	}
	private void runButtonAction(int id) {
		sceneManager.system.buttonActionQueue.add(id);
	}
	public class MenuButton {
		public int posX = 0;
		public int posY = 0;
		public int width = 0;
		public int height = 0;
		public int id = -1;
		public Texture buttonImage;
		public String name = "";
		public MenuButton(int id, int posX, int posY, int width, int height, String name) {
			this.posX = posX;
			this.posY = posY;
			this.width = width;
			this.height = height;
			this.name = name;
			this.id = id;
			try {
				this.buttonImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/" + name + ".png"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		public int getPosX(int screenWidth) {
			return screenWidth / 2 + posX;
		}
		public int getPosY(int screenHeight) {
			return screenHeight / 2 + posY;
		}
	}

}
