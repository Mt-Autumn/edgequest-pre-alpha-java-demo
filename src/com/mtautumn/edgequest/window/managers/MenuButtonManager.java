package com.mtautumn.edgequest.window.managers;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.data.DataManager;

public class MenuButtonManager {
	public ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	DataManager dataManager;
	public MenuButtonManager(DataManager dataManager) {
		this.dataManager = dataManager;
		//buttonIDArray.add(new MenuButton(3,50,100,197,73,"setFPS"));
		buttonIDArray.add(new MenuButton(1,50,100,197,73,"newGame"));
		buttonIDArray.add(new MenuButton(4,50,230,197,73,"saveGame"));
		buttonIDArray.add(new MenuButton(5,503,100,197,73,"loadGame"));
		//buttonIDArray.add(new MenuButton(6,503,360,197,73,"fullScreen"));
	}

	public void buttonPressed(int posX, int posY) {
		int adjustedX = posX - dataManager.system.menuX;
		int adjustedY = posY - dataManager.system.menuY;
		for (int i = 0; i < buttonIDArray.size(); i++) {
			MenuButton button = buttonIDArray.get(i);
			if (adjustedX > button.posX && adjustedX < button.posX + button.width && adjustedY > button.posY && adjustedY < button.posY + button.height) {
				runButtonAction(button.id);
			}
		}
	}
	private void runButtonAction(int id) {
		dataManager.system.buttonActionQueue.add(id);
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
		public int getPosX(int menuStartX) {
			return posX+menuStartX;
		}
		public int getPosY(int menuStartY) {
			return posY+menuStartY;
		}
	}
}

