package com.mtautumn.edgequest;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
	public System system = new System();
	public Savable savable = new Savable();
	public Settings settings = new Settings();

	public class System{
		public boolean isKeyboardUp = false;
		public boolean isKeyboardRight = false;
		public boolean isKeyboardDown = false;
		public boolean isKeyboardLeft = false;
		public boolean isKeyboardSprint = false;
		public boolean isKeyboardMenu = false;
		public boolean isKeyboardBackpack = false;
		public boolean isGameOnLaunchScreen = true;
		public boolean isLaunchScreenLoaded = false;
		public int animationClock = 0;
		public String timeReadable = "";
		public int menuX = 0;
		public int menuY = 0;
		public int minTileX = 0;
		public int maxTileX = 0;
		public int minTileY = 0;
		public int maxTileY = 0;
		public boolean blockGenerationLastTick = true;
		public boolean characterMoving = false;
		public int averagedFPS = 0;
		public Point mousePosition = new Point();
		public int mouseX = 0;
		public int mouseY = 0;
		public boolean isMouseFar = false;
		public boolean autoWalk = false;
		public int autoWalkX = 0;
		public int autoWalkY = 0;
		public boolean setFullScreen = false;
		public boolean setWindowed = false;
		public boolean hideMouse = false;
		public Map<Short, BlockItem> blockIDMap = new HashMap<Short, BlockItem>();
		public Map<String, BlockItem> blockNameMap = new HashMap<String, BlockItem>();
		public Map<String, Byte> biomeMap = new HashMap<String, Byte>();
	}
	public class Settings{
		public int tickLength = 30;
		public int targetFPS = 60;
		public int chunkSize = 16;
		public boolean showDiag = true;
		public int blockSize = 32;
		public int screenWidth = 800;
		public int screenHeight = 600;
		public boolean isFullScreen = false;

	}

}
