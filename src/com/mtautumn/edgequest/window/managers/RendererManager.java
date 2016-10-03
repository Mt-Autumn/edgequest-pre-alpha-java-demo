package com.mtautumn.edgequest.window.managers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.mtautumn.edgequest.CharacterManager;
import com.mtautumn.edgequest.DefineBlockItems;
import com.mtautumn.edgequest.KeyboardInput;
import com.mtautumn.edgequest.SceneManager;
import com.mtautumn.edgequest.window.Renderer;

public class RendererManager extends Thread {
	private static SceneManager sceneManager;
	private static Renderer renderer;
	private static CharacterManager characterManager;


	KeyboardInput keyboard;
	int[] lastXFPS = new int[5];
	int tempFPS;
	static GraphicsDevice device = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public RendererManager(SceneManager scnMgr, CharacterManager cm) {
		sceneManager = scnMgr;
		characterManager = cm;
		keyboard = new KeyboardInput(sceneManager);
		renderer = new Renderer(sceneManager);
	}
	public void run() {
		prepareFPSCounting();
		long lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/sceneManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		double lastNanoPause = (1000000000.0/Double.valueOf(sceneManager.settings.targetFPS));
		renderer.initGL(sceneManager.settings.screenWidth, sceneManager.settings.screenHeight);
		renderer.loadManagers();
		DefineBlockItems.setDefinitions(sceneManager);
		while (true) {
			try {
				updateWindowSize();
				tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
				lastNanoTimeFPSGrabber = System.nanoTime();
				updateAverageFPS(tempFPS);
				if (sceneManager.system.setFullScreen) {
					sceneManager.settings.isFullScreen = true;
					sceneManager.system.setFullScreen = false;

				}
				if (sceneManager.system.setWindowed) {
					device.setFullScreenWindow(null);
					sceneManager.system.setWindowed = false;
					sceneManager.settings.isFullScreen = false;
				}
				updateMouse();
				updateKeys();
				updateWindow();
				lastNanoPause += (1.0/Double.valueOf(sceneManager.settings.targetFPS) - 1.0/Double.valueOf(sceneManager.system.averagedFPS)) * 50000000.0;
				if (lastNanoPause < 0) lastNanoPause = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	private static void updateWindow() {
		findViewDimensions();
		renderer.drawFrame();
	}
	private void updateAverageFPS(int FPS) {
		if (FPS / 5.0 > sceneManager.system.averagedFPS) FPS = sceneManager.system.averagedFPS+1;
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		sceneManager.system.averagedFPS = (int) Math.ceil(Double.valueOf(fpsSum) / Double.valueOf(lastXFPS.length));
	}
	private void prepareFPSCounting() {
		sceneManager.system.averagedFPS = sceneManager.settings.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = sceneManager.settings.targetFPS;
		}
	}
	private void updateWindowSize() {
		if (sceneManager.settings.screenWidth != Display.getWidth() || sceneManager.settings.screenHeight != Display.getHeight()) {
			sceneManager.settings.screenWidth = Display.getWidth();
			sceneManager.settings.screenHeight = Display.getHeight();
			sceneManager.system.blockGenerationLastTick = true;
		}
	}
	private boolean wasMouseDown = false;
	private void updateMouse() {
		try {
			if (sceneManager.system.hideMouse) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				if (Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(sceneManager.system.mousePosition.x, sceneManager.system.mousePosition.y);
					Mouse.updateCursor();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Mouse.poll();
		sceneManager.system.mousePosition = new Point(Mouse.getX(), Display.getHeight() - Mouse.getY());
		int mouseX = sceneManager.system.mousePosition.x;
		int mouseY = sceneManager.system.mousePosition.y;
		double offsetX = (sceneManager.savable.charX * Double.valueOf(sceneManager.settings.blockSize) - Double.valueOf(sceneManager.settings.screenWidth) / 2.0);
		double offsetY = (sceneManager.savable.charY * Double.valueOf(sceneManager.settings.blockSize) - Double.valueOf(sceneManager.settings.screenHeight) / 2.0);
		sceneManager.system.mouseX = (int) Math.floor((offsetX + sceneManager.system.mousePosition.getX())/Double.valueOf(sceneManager.settings.blockSize));
		sceneManager.system.mouseY = (int) Math.floor((offsetY + sceneManager.system.mousePosition.getY())/Double.valueOf(sceneManager.settings.blockSize));
		sceneManager.system.isMouseFar =  (Math.sqrt(Math.pow(Double.valueOf(sceneManager.system.mouseX) - Math.floor(sceneManager.savable.charX), 2.0)+Math.pow(Double.valueOf(sceneManager.system.mouseY) - Math.floor(sceneManager.savable.charY), 2.0)) > 3.0);
		if (Mouse.isButtonDown(0) && !wasMouseDown) {
			sceneManager.system.autoWalk = false;
			if (sceneManager.system.isKeyboardMenu) {
				renderer.menuButtonManager.buttonPressed(mouseX, mouseY);
			} else if (sceneManager.system.isGameOnLaunchScreen) {
				renderer.launchScreenManager.buttonPressed(mouseX, mouseY);
			} else if (sceneManager.system.isKeyboardSprint && !sceneManager.system.hideMouse){
				sceneManager.system.autoWalkX = sceneManager.system.mouseX;
				sceneManager.system.autoWalkY = sceneManager.system.mouseY;
				sceneManager.system.autoWalk = true;
			}

		}
		wasMouseDown = Mouse.isButtonDown(0);
	}
	private static void findViewDimensions() {
		if (sceneManager.system.characterMoving || sceneManager.system.blockGenerationLastTick) {
			double tileWidth = Double.valueOf(sceneManager.settings.screenWidth) / sceneManager.settings.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(sceneManager.settings.screenHeight) / sceneManager.settings.blockSize / 2.0 + 1;
			sceneManager.system.minTileX = (int) (sceneManager.savable.charX - tileWidth - 1);
			sceneManager.system.maxTileX = (int) (sceneManager.savable.charX + tileWidth);
			sceneManager.system.minTileY = (int) (sceneManager.savable.charY - tileHeight - 1);
			sceneManager.system.maxTileY = (int) (sceneManager.savable.charY + tileHeight);
		}
	}
	private static boolean[] wasKeyDown = new boolean[256];
	public void updateKeys() {
		try {
			if (sceneManager.system.inputText.size() + sceneManager.system.noticeText.size() > 0) {
				keyboard.poll();
			} else {
				if (!sceneManager.system.isGameOnLaunchScreen) {
					Keyboard.poll();
					boolean keyUp = Keyboard.isKeyDown(sceneManager.settings.upKey);
					boolean keyDown = Keyboard.isKeyDown(sceneManager.settings.downKey);
					boolean keyLeft = Keyboard.isKeyDown(sceneManager.settings.leftKey);
					boolean keyRight = Keyboard.isKeyDown(sceneManager.settings.rightKey);
					boolean keySprint = Keyboard.isKeyDown(sceneManager.settings.sprintKey);
					boolean keyMenu = Keyboard.isKeyDown(sceneManager.settings.menuKey);
					boolean keyBackpack = Keyboard.isKeyDown(sceneManager.settings.backpackKey);
					boolean keyZoomIn = Keyboard.isKeyDown(sceneManager.settings.zoomInKey);
					boolean keyZoomOut = Keyboard.isKeyDown(sceneManager.settings.zoomOutKey);
					boolean keyShowDiag = Keyboard.isKeyDown(sceneManager.settings.showDiagKey);
					boolean keyPlaceTorch = Keyboard.isKeyDown(sceneManager.settings.placeTorchKey);
					if (!sceneManager.system.autoWalk) {
						sceneManager.system.isKeyboardUp = keyUp;
						sceneManager.system.isKeyboardRight = keyRight;
						sceneManager.system.isKeyboardDown = keyDown;
						sceneManager.system.isKeyboardLeft = keyLeft;
						sceneManager.system.isKeyboardSprint = keySprint;
						if (keyUp || keyDown || keyLeft || keyRight) {
							sceneManager.system.hideMouse = true;
						} else {
							sceneManager.system.hideMouse = false;
						}
					} else {
						if (keyUp || keyDown || keyRight || keyLeft) {
							sceneManager.system.autoWalk = false;
						}
					}
					if (keyZoomIn && !wasKeyDown[sceneManager.settings.zoomInKey]) {
						if (sceneManager.settings.blockSize < 128) {
							sceneManager.settings.blockSize *= 2;
							sceneManager.system.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut && !wasKeyDown[sceneManager.settings.zoomOutKey]) {
						if (sceneManager.settings.blockSize > 1) {
							sceneManager.settings.blockSize /= 2;
							sceneManager.system.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag && !wasKeyDown[sceneManager.settings.showDiagKey]) {
						sceneManager.settings.showDiag = !sceneManager.settings.showDiag;
					}
					if (keyMenu && !wasKeyDown[sceneManager.settings.menuKey]) {
						sceneManager.system.isKeyboardMenu = !sceneManager.system.isKeyboardMenu;
					}
					if (keyPlaceTorch && !wasKeyDown[sceneManager.settings.placeTorchKey]) {
						characterManager.charPlaceTorch();

					}
					if (keyBackpack && !wasKeyDown[sceneManager.settings.backpackKey]) {
						sceneManager.system.isKeyboardBackpack = !sceneManager.system.isKeyboardBackpack;
					}

					wasKeyDown[sceneManager.settings.menuKey] = keyMenu;
					wasKeyDown[sceneManager.settings.backpackKey] = keyBackpack;
					wasKeyDown[sceneManager.settings.zoomInKey] = keyZoomIn;
					wasKeyDown[sceneManager.settings.zoomOutKey] = keyZoomOut;
					wasKeyDown[sceneManager.settings.showDiagKey] = keyShowDiag;
					wasKeyDown[sceneManager.settings.placeTorchKey] = keyPlaceTorch;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
