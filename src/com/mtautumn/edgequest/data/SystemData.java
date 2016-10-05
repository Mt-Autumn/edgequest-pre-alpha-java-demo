package com.mtautumn.edgequest.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.BlockItem;

public class SystemData {
	public ArrayList<String> inputText = new ArrayList<String>();
	public ArrayList<String> inputTextResponse = new ArrayList<String>();
	public ArrayList<String> noticeText = new ArrayList<String>();
	public ArrayList<Integer> buttonActionQueue = new ArrayList<Integer>();
	public String lastInputMessage = "";
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