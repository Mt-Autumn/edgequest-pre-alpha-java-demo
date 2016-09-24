package com.mtautumn.edgequest;

import java.awt.event.KeyEvent;

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
			keyboard.poll();
			sceneManager.system.isKeyboardUp = keyboard.keyDown( KeyEvent.VK_UP );
			sceneManager.system.isKeyboardRight = keyboard.keyDown( KeyEvent.VK_RIGHT );
			sceneManager.system.isKeyboardDown = keyboard.keyDown( KeyEvent.VK_DOWN );
			sceneManager.system.isKeyboardLeft = keyboard.keyDown( KeyEvent.VK_LEFT );
			sceneManager.system.isKeyboardSprint = keyboard.keyDown( KeyEvent.VK_SHIFT );
			if (keyboard.keyDownOnce(KeyEvent.VK_W)) {
				sceneManager.settings.blockSize += 1;
				sceneManager.system.blockGenerationLastTick = true;
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_S)) {
				if (sceneManager.settings.blockSize > 1) {
					sceneManager.settings.blockSize -= 1;
					sceneManager.system.blockGenerationLastTick = true;
				}	
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
				sceneManager.settings.showDiag = !sceneManager.settings.showDiag;
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_ESCAPE)) {
				sceneManager.system.isKeyboardMenu = !sceneManager.system.isKeyboardMenu;
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_Q)) {
				characterManager.charPlaceTorch();
				
			}
			try {
				Thread.sleep(sceneManager.settings.tickLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
