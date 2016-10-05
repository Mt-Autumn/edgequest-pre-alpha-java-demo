package com.mtautumn.edgequest.data;

import org.lwjgl.input.Keyboard;

public class SettingsData {
	public int tickLength = 30;
	public int targetFPS = 60;
	public int chunkSize = 16;
	public boolean showDiag = false;
	public int blockSize = 32;
	public int screenWidth = 800;
	public int screenHeight = 600;
	public boolean isFullScreen = false;

	public int upKey = Keyboard.KEY_UP;
	public int downKey = Keyboard.KEY_DOWN;
	public int rightKey = Keyboard.KEY_RIGHT;
	public int leftKey = Keyboard.KEY_LEFT;
	public int sprintKey = Keyboard.KEY_LSHIFT;
	public int menuKey = Keyboard.KEY_ESCAPE;
	public int backpackKey = Keyboard.KEY_E;
	public int zoomInKey = Keyboard.KEY_W;
	public int zoomOutKey = Keyboard.KEY_S;
	public int showDiagKey = Keyboard.KEY_SPACE;
	public int placeTorchKey = Keyboard.KEY_Q;
}