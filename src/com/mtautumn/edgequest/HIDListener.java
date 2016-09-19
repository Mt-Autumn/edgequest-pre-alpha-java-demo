package com.mtautumn.edgequest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HIDListener implements KeyListener {
	SceneManager sceneManager;
	public HIDListener(SceneManager scnMgr) {
		sceneManager = scnMgr;
	}
	public boolean[] charDir = {false,false,false,false};
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    	switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			sceneManager.isWPressed = true;
			break;
		case KeyEvent.VK_D:
			sceneManager.isDPressed = true;
			break;
		case KeyEvent.VK_S:
			sceneManager.isSPressed = true;
			break;
		case KeyEvent.VK_A:
			sceneManager.isAPressed = true;
			break;
		default:
			break;
		}

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    	switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			sceneManager.isWPressed = false;
			break;
		case KeyEvent.VK_D:
			sceneManager.isDPressed = false;
			break;
		case KeyEvent.VK_S:
			sceneManager.isSPressed = false;
			break;
		case KeyEvent.VK_A:
			sceneManager.isAPressed = false;
			break;
		default:
			break;
		}

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    	switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			sceneManager.isWPressed = true;
			break;
		case KeyEvent.VK_D:
			sceneManager.isDPressed = true;
			break;
		case KeyEvent.VK_S:
			sceneManager.isSPressed = true;
			break;
		case KeyEvent.VK_A:
			sceneManager.isAPressed = true;
			break;
		default:
			break;
		}
    }    
}