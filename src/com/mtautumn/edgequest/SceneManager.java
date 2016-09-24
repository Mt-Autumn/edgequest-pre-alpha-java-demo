package com.mtautumn.edgequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SceneManager implements Serializable {
	private static final long serialVersionUID = 1L;
	public boolean isWPressed = false;
	public boolean isDPressed = false;
	public boolean isSPressed = false;
	public boolean isAPressed = false;
	public boolean isShiftPressed = false;
	public boolean isEscToggled = false;
	public int time = 800;
	public int animationClock = 0;
	public String timeReadable = "";
	public int tickLength = 30;
	public int targetFPS = 60;
	public int averagedFPS = 0;
	public int screenWidth = 800;
	public int screenHeight = 600;
	public double charX = 5;
	public double charY = 5;
	public int charDir = 0;
	public int blockSize = 32;
	public int chunkSize = 16;
	public int menuX = 0;
	public int menuY = 0;
	public long seed = 7;
	public Map<String, Integer> map = new HashMap<String, Integer>();
	public Map<String, Double> lightMap = new HashMap<String, Double>();
	public Map<String, Boolean> lightSourceMap = new HashMap<String, Boolean>();
	public Map<String, Integer> playerStructuresMap = new HashMap<String, Integer>();
	public Map<String, Integer> biomeMap = new HashMap<String, Integer>();
	public Map<String, Integer> biomeMapFiltered = new HashMap<String, Integer>();
	public ArrayList<FootPrint> footPrints = new ArrayList<FootPrint>();
	public int minTileX = 0;
	public int maxTileX = 0;
	public int minTileY = 0;
	public int maxTileY = 0;
	public boolean showDiag = true;
	public boolean blockGenerationLastTick = true;
	public boolean characterMoving = false;
	
	public double getBrightness() {
		int tempTime = time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) tempTime += 2400;
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		return brightness;
	}
}
