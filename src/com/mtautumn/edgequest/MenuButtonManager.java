package com.mtautumn.edgequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MenuButtonManager {
	ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	
	public MenuButtonManager(SceneManager scnMgr) {
		buttonIDArray.add(new MenuButton(1,50,100,200,50,"setFPS"));
		buttonIDArray.add(new MenuButton(1,500,100,200,50,"setSeed"));
		buttonIDArray.add(new MenuButton(1,50,200,200,50,"regenWorld"));
	}
	
	
	public class MenuButton {
		public int posX = 0;
		public int posY = 0;
		public int width = 0;
		public int height = 0;
		public int id = -1;
		public BufferedImage buttonImage;
		public String name = "";
		public MenuButton(int id, int posX, int posY, int width, int height, String name) {
			this.posX = posX;
			this.posY = posY;
			this.width = width;
			this.height = height;
			this.name = name;
			try {
				this.buttonImage = ImageIO.read(new File("textures/" + name + ".png"));
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

