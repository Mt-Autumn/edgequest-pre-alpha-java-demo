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
			sceneManager.isWPressed = keyboard.keyDown( KeyEvent.VK_UP );
			sceneManager.isDPressed = keyboard.keyDown( KeyEvent.VK_RIGHT );
			sceneManager.isSPressed = keyboard.keyDown( KeyEvent.VK_DOWN );
			sceneManager.isAPressed = keyboard.keyDown( KeyEvent.VK_LEFT );
			sceneManager.isShiftPressed = keyboard.keyDown( KeyEvent.VK_SHIFT );
			if (keyboard.keyDownOnce(KeyEvent.VK_W)) {
				sceneManager.blockSize += 1;
				sceneManager.blockGenerationLastTick = true;
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_S)) {
				if (sceneManager.blockSize > 1) {
					sceneManager.blockSize -= 1;
					sceneManager.blockGenerationLastTick = true;
				}	
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
				sceneManager.showDiag = !sceneManager.showDiag;
			}
			if (keyboard.keyDownOnce(KeyEvent.VK_Q)) {
				characterManager.charPlaceTorch();
				
			}
			try {
				Thread.sleep(sceneManager.tickLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
