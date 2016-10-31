package com.mtautumn.edgequest.window.managers;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.data.DataManager;

public class MenuButtonManager {
	public ArrayList<MenuPane> menus = new ArrayList<MenuPane>();
	public ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	DataManager dataManager;
	public MenuButtonManager(DataManager dataManager) {
		this.dataManager = dataManager;
		MenuPane mainMenu = new MenuPane("Main Menu", null);
		mainMenu.addButton(new MenuButton("game", "Game"));
		mainMenu.addButton(new MenuButton("graphics", "Graphics"));
		mainMenu.addButton(new MenuButton("quit", "Quit"));
		menus.add(mainMenu);
		MenuPane gameMenu = new MenuPane("Game Menu", "Main Menu");
		gameMenu.addButton(new MenuButton("newGame", "New Game"));
		gameMenu.addButton(new MenuButton("saveGame", "Save Game"));
		gameMenu.addButton(new MenuButton("loadGame", "Load Game"));
		menus.add(gameMenu);
		MenuPane graphicsMenu = new MenuPane("Graphics Menu", "Main Menu");
		graphicsMenu.addButton(new MenuButton("vSync", "V-Sync Off"));
		graphicsMenu.addButton(new MenuButton("fullScreen", "Full Screen"));
		menus.add(graphicsMenu);
	}
	public MenuPane getMenu(String name) {
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).name.equals(name)) {
				return menus.get(i);
			}
		}
		return null;
	}
	public MenuPane getCurrentMenu() {
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).name.equals(dataManager.system.currentMenu)) {
				return menus.get(i);
			}
		}
		return null;
	}
	public MenuButton getButtonFromName(String name) {
		MenuButton button = null;
		for (int i = 0; i < buttonIDArray.size(); i++) {
			if (buttonIDArray.get(i).name.equals(name)) {
				button = buttonIDArray.get(i);
			}
		}
		return button;
	}
	public void buttonPressed(int posX, int posY) {
		int adjustedX = posX - dataManager.system.menuX;
		int adjustedY = posY - dataManager.system.menuY;
		if (adjustedX < dataManager.settings.BACK_BUTTON_SIZE + dataManager.settings.BACK_BUTTON_PADDING && adjustedX > 0 && adjustedY < dataManager.settings.BACK_BUTTON_SIZE + dataManager.settings.BACK_BUTTON_PADDING && adjustedY > 0) {
			runButtonAction("Go To Parent");
		}
		for (int i = 0; i < getCurrentMenu().getButtons().size(); i++) {
			MenuButton button = getCurrentMenu().getButtons().get(i);
			if (adjustedX > button.posX && adjustedX < button.posX + button.width && adjustedY > button.posY && adjustedY < button.posY + button.height) {
				if (button.visible) runButtonAction(button.name);
			}
		}
	}
	private void runButtonAction(String name) {
		dataManager.system.buttonActionQueue.add(name);
	}
	public class MenuPane {
		private static final int BUTTON_WIDTH = 197;
		private static final int BUTTON_HEIGHT = 73;
		private static final int SPACING_X = 256;
		private static final int SPACING_Y = 57;
		private static final int START_X = 50;
		private static final int START_Y = 100;
		private int currentX = START_X;
		private int currentY = START_Y;
		private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
		public String name;
		public String parent;
		public MenuPane(String name, String parent) {
			this.name = name;
			this.parent = parent;
		}
		public void addButton(MenuButton button) {
			button.width = BUTTON_WIDTH;
			button.height = BUTTON_HEIGHT;
			button.posX = currentX;
			button.posY = currentY;
			buttons.add(button);
			currentX += (SPACING_X + BUTTON_WIDTH);
			if (currentX > 800 - BUTTON_WIDTH) {
				currentX = START_X;
				currentY += (SPACING_Y + BUTTON_HEIGHT);
			}
		}
		public ArrayList<MenuButton> getButtons() {
			return buttons;
		}
		public int getCount() {
			return buttons.size();
		}
		public MenuButton getButton(String buttonName) {
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).name.equals(buttonName)) {
					return buttons.get(i);
				}
			}
			return null;
		}
	}
	public class MenuButton {
		public int posX = 0;
		public int posY = 0;
		public int width = 0;
		public int height = 0;
		public Texture buttonImage;
		public String name = "";
		public String displayName = "";
		public boolean visible = true;
		public MenuButton(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
			try {
				this.buttonImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/menuButton.png"));
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

