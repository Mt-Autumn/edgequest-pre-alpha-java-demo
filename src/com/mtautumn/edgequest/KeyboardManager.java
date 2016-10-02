package com.mtautumn.edgequest;

public class KeyboardManager extends Thread {
	KeyboardInput keyboard;
	SceneManager sceneManager;
	CharacterManager characterManager;
	public KeyboardManager(SceneManager scnMgr, KeyboardInput kybd, CharacterManager charMgr) {
		keyboard = kybd;
		sceneManager = scnMgr;
		characterManager = charMgr;
	}
	public void run() {
		while (true) {
			try {
				if (!sceneManager.system.isGameOnLaunchScreen) {
					keyboard.poll();
					boolean keyUp = keyboard.keyDown(sceneManager.settings.upKey);
					boolean keyDown = keyboard.keyDown(sceneManager.settings.downKey);
					boolean keyLeft = keyboard.keyDown(sceneManager.settings.leftKey);
					boolean keyRight = keyboard.keyDown(sceneManager.settings.rightKey);
					boolean keySprint = keyboard.keyDown(sceneManager.settings.sprintKey);
					boolean keyMenu = keyboard.keyDown(sceneManager.settings.menuKey);
					boolean keyBackpack = keyboard.keyDown(sceneManager.settings.backpackKey);
					boolean keyZoomIn = keyboard.keyDown(sceneManager.settings.zoomInKey);
					boolean keyZoomOut = keyboard.keyDown(sceneManager.settings.zoomOutKey);
					boolean keyShowDiag = keyboard.keyDown(sceneManager.settings.showDiagKey);
					boolean keyPlaceTorch = keyboard.keyDown(sceneManager.settings.placeTorchKey);
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
					if (keyZoomIn) {
						if (sceneManager.settings.blockSize < 128) {
							sceneManager.settings.blockSize *= 2;
							sceneManager.system.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut) {
						if (sceneManager.settings.blockSize > 16) {
							sceneManager.settings.blockSize /= 2;
							sceneManager.system.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag) {
						sceneManager.settings.showDiag = !sceneManager.settings.showDiag;
					}
					if (keyMenu) {
						sceneManager.system.isKeyboardMenu = !sceneManager.system.isKeyboardMenu;
					}
					if (keyPlaceTorch) {
						characterManager.charPlaceTorch();

					}
					if (keyBackpack) {
						sceneManager.system.isKeyboardBackpack = !sceneManager.system.isKeyboardBackpack;
					}
				}
				Thread.sleep(sceneManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
