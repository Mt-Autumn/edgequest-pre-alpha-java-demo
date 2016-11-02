package com.mtautumn.edgequest.window.managers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.mtautumn.edgequest.CharacterManager;
import com.mtautumn.edgequest.DefineBlockItems;
import com.mtautumn.edgequest.Dungeon;
import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.KeyboardInput;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.layers.OptionPane;

public class RendererManager extends Thread {
	private DataManager dataManager;
	private Renderer renderer;
	private CharacterManager characterManager;


	KeyboardInput keyboard;
	int[] lastXFPS = new int[5];
	int tempFPS;
	static GraphicsDevice device = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public RendererManager(DataManager dataManager) {
		this.dataManager = dataManager;
		characterManager = dataManager.characterManager;
		keyboard = new KeyboardInput(dataManager);
		renderer = new Renderer(dataManager);
	}
	public void run() {
		prepareFPSCounting();
		long lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/dataManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		double lastNanoPause = (1000000000.0/Double.valueOf(dataManager.settings.targetFPS));
		renderer.initGL(dataManager.settings.screenWidth, dataManager.settings.screenHeight);
		renderer.loadManagers();
		DefineBlockItems.setDefinitions(dataManager);
		dataManager.system.gameLoaded = true;
		while (dataManager.system.running) {
			try {
				updateWindowSize();
				tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
				lastNanoTimeFPSGrabber = System.nanoTime();
				updateAverageFPS(tempFPS);
				if (dataManager.system.setFullScreen) {
					dataManager.settings.isFullScreen = true;
					dataManager.system.setFullScreen = false;

				}
				if (dataManager.system.setWindowed) {
					device.setFullScreenWindow(null);
					dataManager.system.setWindowed = false;
					dataManager.settings.isFullScreen = false;
				}
				if (!dataManager.system.isGameOnLaunchScreen) {
					updateScreenCenter();
					for( int i = 0; i < dataManager.savable.entities.size(); i++) {
						Entity entity = dataManager.savable.entities.get(i);
						entity.frameX = entity.getX();
						entity.frameY = entity.getY();
					}
				}
				updateMouse();
				updateKeys();
				updateWindow();
				lastNanoPause += (1.0/Double.valueOf(dataManager.settings.targetFPS) - 1.0/Double.valueOf(dataManager.system.averagedFPS)) * 50000000.0;
				if (lastNanoPause < 0) lastNanoPause = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Display.destroy();
		System.exit(0);
	}
	private void updateWindow() {
		findViewDimensions();
		renderer.drawFrame();
	}
	private void updateAverageFPS(int FPS) {
		if (FPS / 5.0 > dataManager.system.averagedFPS) FPS = dataManager.system.averagedFPS+1;
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		dataManager.system.averagedFPS = (int) Math.ceil(Double.valueOf(fpsSum) / Double.valueOf(lastXFPS.length));
	}
	private void prepareFPSCounting() {
		dataManager.system.averagedFPS = dataManager.settings.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = dataManager.settings.targetFPS;
		}
	}
	private void updateWindowSize() {
		if (dataManager.settings.screenWidth != Display.getWidth() || dataManager.settings.screenHeight != Display.getHeight()) {
			dataManager.settings.screenWidth = Display.getWidth();
			dataManager.settings.screenHeight = Display.getHeight();
			dataManager.system.blockGenerationLastTick = true;
		}
	}
	private boolean wasMouseDown = false;
	private void updateMouse() {
		try {
			if (dataManager.system.hideMouse) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				if (Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(dataManager.system.mousePosition.x, dataManager.system.mousePosition.y);
					Mouse.updateCursor();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Mouse.poll();
		dataManager.system.leftMouseDown = Mouse.isButtonDown(0);
		dataManager.system.rightMouseDown = Mouse.isButtonDown(1);
		if (dataManager.system.inputText.size() + dataManager.system.noticeText.size() > 0) {
			if (Mouse.isButtonDown(0) && !wasMouseDown) {
				OptionPane.closeOptionPane(dataManager);
			}
		} else {
			dataManager.system.mousePosition = new Point(Mouse.getX(), Display.getHeight() - Mouse.getY());
			int mouseX = dataManager.system.mousePosition.x;
			int mouseY = dataManager.system.mousePosition.y;
			double offsetX = (dataManager.system.screenX * Double.valueOf(dataManager.settings.blockSize) - Double.valueOf(dataManager.settings.screenWidth) / 2.0);
			double offsetY = (dataManager.system.screenY * Double.valueOf(dataManager.settings.blockSize) - Double.valueOf(dataManager.settings.screenHeight) / 2.0);
			dataManager.system.mouseX = (int) Math.floor((offsetX + dataManager.system.mousePosition.getX())/Double.valueOf(dataManager.settings.blockSize));
			dataManager.system.mouseY = (int) Math.floor((offsetY + dataManager.system.mousePosition.getY())/Double.valueOf(dataManager.settings.blockSize));
			dataManager.system.isMouseFar =  (Math.sqrt(Math.pow(Double.valueOf(dataManager.system.mouseX) - Math.floor(dataManager.characterManager.characterEntity.getX()), 2.0)+Math.pow(Double.valueOf(dataManager.system.mouseY) - Math.floor(dataManager.characterManager.characterEntity.getY()), 2.0)) > 3.0);
			if (Mouse.isButtonDown(0) && !wasMouseDown) {
				dataManager.system.autoWalk = false;
				if (dataManager.system.isKeyboardMenu) {
					dataManager.menuButtonManager.buttonPressed(mouseX, mouseY);
				} else if (dataManager.system.isGameOnLaunchScreen) {
					renderer.launchScreenManager.buttonPressed(mouseX, mouseY);
				} else if (dataManager.system.isKeyboardSprint && !dataManager.system.hideMouse){
					dataManager.system.autoWalkX = dataManager.system.mouseX;
					dataManager.system.autoWalkY = dataManager.system.mouseY;
					dataManager.characterManager.characterEntity.setDestination(dataManager.system.autoWalkX, dataManager.system.autoWalkY);
				}
			}
		}
		wasMouseDown = Mouse.isButtonDown(0);
	}
	private void findViewDimensions() {
		if (dataManager.system.characterMoving || dataManager.system.blockGenerationLastTick) {
			double tileWidth = Double.valueOf(dataManager.settings.screenWidth) / dataManager.settings.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(dataManager.settings.screenHeight) / dataManager.settings.blockSize / 2.0 + 1;
			dataManager.system.minTileX = (int) (dataManager.system.screenX - tileWidth - 1);
			dataManager.system.maxTileX = (int) (dataManager.system.screenX + tileWidth);
			dataManager.system.minTileY = (int) (dataManager.system.screenY - tileHeight - 1);
			dataManager.system.maxTileY = (int) (dataManager.system.screenY + tileHeight);
		}
	}
	private static boolean[] wasKeyDown = new boolean[256];
	public void updateKeys() {
		try {
			if (dataManager.system.inputText.size() + dataManager.system.noticeText.size() > 0 || dataManager.system.showConsole) {
				keyboard.poll();
				keyboard.wasConsoleUp = dataManager.system.showConsole;
			} else {
				if (!dataManager.system.isGameOnLaunchScreen) {
					Keyboard.poll();
					boolean keyUp = Keyboard.isKeyDown(dataManager.settings.upKey);
					boolean keyDown = Keyboard.isKeyDown(dataManager.settings.downKey);
					boolean keyLeft = Keyboard.isKeyDown(dataManager.settings.leftKey);
					boolean keyRight = Keyboard.isKeyDown(dataManager.settings.rightKey);
					boolean keySprint = Keyboard.isKeyDown(dataManager.settings.sprintKey);
					boolean keyMenu = Keyboard.isKeyDown(dataManager.settings.menuKey);
					boolean keyBackpack = Keyboard.isKeyDown(dataManager.settings.backpackKey);
					boolean keyZoomIn = Keyboard.isKeyDown(dataManager.settings.zoomInKey);
					boolean keyZoomOut = Keyboard.isKeyDown(dataManager.settings.zoomOutKey);
					boolean keyShowDiag = Keyboard.isKeyDown(dataManager.settings.showDiagKey);
					boolean keyPlaceTorch = Keyboard.isKeyDown(dataManager.settings.placeTorchKey);
					boolean keyConsole = Keyboard.isKeyDown(dataManager.settings.consoleKey);
					boolean keyAction = Keyboard.isKeyDown(dataManager.settings.actionKey);
					dataManager.system.isKeyboardSprint = keySprint;
					
					if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
						dataManager.savable.hotBarSelection = 0;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
						dataManager.savable.hotBarSelection = 1;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
						dataManager.savable.hotBarSelection = 2;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
						dataManager.savable.hotBarSelection = 3;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
						dataManager.savable.hotBarSelection = 4;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
						dataManager.savable.hotBarSelection = 5;
					}
					
					if (!dataManager.system.autoWalk) {
						dataManager.system.isKeyboardUp = keyUp;
						dataManager.system.isKeyboardRight = keyRight;
						dataManager.system.isKeyboardDown = keyDown;
						dataManager.system.isKeyboardLeft = keyLeft;

						if (keyUp || keyDown || keyLeft || keyRight)
							dataManager.system.hideMouse = true;
						else
							dataManager.system.hideMouse = false;

					} else {
						if (keyUp || keyDown || keyRight || keyLeft)
							dataManager.system.autoWalk = false;

					}
					if (keyZoomIn && !wasKeyDown[dataManager.settings.zoomInKey]) {
						if (dataManager.settings.blockSize < 128) {
							dataManager.settings.blockSize *= 2;
							dataManager.system.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut && !wasKeyDown[dataManager.settings.zoomOutKey]) {
						if (dataManager.settings.blockSize > 16) {
							dataManager.settings.blockSize /= 2;
							dataManager.system.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag && !wasKeyDown[dataManager.settings.showDiagKey])
						dataManager.settings.showDiag = !dataManager.settings.showDiag;

					if (keyMenu && !wasKeyDown[dataManager.settings.menuKey])
						dataManager.system.isKeyboardMenu = !dataManager.system.isKeyboardMenu;

					if (keyPlaceTorch && !wasKeyDown[dataManager.settings.placeTorchKey]) 
						characterManager.charPlaceTorch();

					if (keyBackpack && !wasKeyDown[dataManager.settings.backpackKey]) 
						dataManager.system.isKeyboardBackpack = !dataManager.system.isKeyboardBackpack;

					if (keyConsole && !wasKeyDown[dataManager.settings.consoleKey])
						dataManager.system.showConsole = true;

					if (keyAction && !wasKeyDown[dataManager.settings.actionKey]) {
						int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
						int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
						if (dataManager.savable.isInDungeon) {
							Dungeon dungeon = dataManager.savable.dungeonMap.get(dataManager.savable.dungeonX + "," + dataManager.savable.dungeonY);
							if (charX == dungeon.getStairsUp(dataManager.savable.dungeonLevel)[0]&&charY == dungeon.getStairsUp(dataManager.savable.dungeonLevel)[1]) {
								dataManager.savable.dungeonLevel -= 1;
								dataManager.system.updateDungeon = true;
								if (dataManager.savable.dungeonLevel < 0) {
									dataManager.savable.isInDungeon = false;
									dataManager.characterManager.characterEntity.setX(dataManager.savable.dungeonX + 0.5);
									dataManager.characterManager.characterEntity.setY(dataManager.savable.dungeonY + 0.5);
								}
							} else if (charX == dungeon.getStairsDown(dataManager.savable.dungeonLevel)[0]&&charY == dungeon.getStairsDown(dataManager.savable.dungeonLevel)[1]) {
								dataManager.savable.dungeonLevel += 1;
								dataManager.system.updateDungeon = true;
							}
						} else {
							if (dataManager.savable.dungeonMap.containsKey(charX + "," + charY)) {
								dataManager.savable.isInDungeon = true;
								dataManager.savable.dungeonX = charX;
								dataManager.savable.dungeonY = charY;
								dataManager.savable.dungeonLevel = 0;
								dataManager.system.updateDungeon = true;
							}
						}
					}

					wasKeyDown[dataManager.settings.menuKey] = keyMenu;
					wasKeyDown[dataManager.settings.backpackKey] = keyBackpack;
					wasKeyDown[dataManager.settings.zoomInKey] = keyZoomIn;
					wasKeyDown[dataManager.settings.zoomOutKey] = keyZoomOut;
					wasKeyDown[dataManager.settings.showDiagKey] = keyShowDiag;
					wasKeyDown[dataManager.settings.placeTorchKey] = keyPlaceTorch;
					wasKeyDown[dataManager.settings.consoleKey] = keyConsole;
					wasKeyDown[dataManager.settings.actionKey] = keyAction;
					if (keyboard.wasConsoleUp) keyboard.wasConsoleUp = dataManager.system.showConsole;
				}

			}
			Keyboard.poll();
			boolean keyExit = Keyboard.isKeyDown(dataManager.settings.exitKey);
			if (keyExit && !wasKeyDown[dataManager.settings.exitKey]) {
				if (dataManager.system.showConsole)
					dataManager.system.showConsole = false;
			}
			wasKeyDown[dataManager.settings.exitKey] = keyExit;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateScreenCenter() {
		dataManager.system.screenX = dataManager.characterManager.characterEntity.getX();
		dataManager.system.screenY = dataManager.characterManager.characterEntity.getY();
	}
}
