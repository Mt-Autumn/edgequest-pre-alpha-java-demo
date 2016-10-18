package com.mtautumn.edgequest.updates;

import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.data.DataManager;

public class UpdateLighting {
	private int lightDiffuseDistance = 8;
	DataManager dataManager;
	public UpdateLighting(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void update(int x, int y) {
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				updateBlockLighting(i, j);
			}
		}
		antialiasLighting(x, y);
	}
	private void antialiasLighting(int x, int y) {
		Map<String, Double> tempMap = new HashMap<String, Double>();
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				tempMap.put(i + "," + j, getAveragedLighting(i,j));
			}
		}
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				setBrightness(i,j,(tempMap.get(i + "," + j) + getBlockBrightness(i,j))/2.0);
			}
		}
	}
	private double getAveragedLighting(int x, int y) {
		double lighting = 0.0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				lighting += getBlockBrightness(i,j);
			}
		}
		return lighting / 9.0;
	}
	private double getBlockBrightness(int x, int y) {
		if (dataManager.savable.lightMap.containsKey(x + "," + y)) {
			return Double.valueOf(((int) dataManager.savable.lightMap.get(x + "," + y) + 128)) / 255.0;
		}
		return 0.0;
	}
	private void updateBlockLighting(int x, int y) {
		double brightness = 0.0;
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				if (doesContainLightSource(i,j)) {
					if (isLineOfSight(i,j,x,y)) {
						double distance = Math.sqrt(Math.pow(x-i, 2) + Math.pow(y-j, 2));
						double brightnessAdded = 1.0 - distance/Double.valueOf(lightDiffuseDistance);
						if (brightnessAdded < 0) { brightnessAdded = 0; }
						brightness += (1.0 - brightness) * brightnessAdded;
					}
				}
			}
		}
		setBrightness(x, y, brightness);

	}
	private boolean isBlockOpaque(int x, int y) {
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return !dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y)).isPassable;
		}
		return false;
	}
	private boolean isLineOfSight(int x1, int y1, int x2, int y2) {
		double checkingPosX = Double.valueOf(x1) + 0.5;
		double checkingPosY = Double.valueOf(y1) + 0.5;
		double deltaX = (x2-x1);
		double deltaY = (y2-y1);
		boolean answer = true;
		if (deltaX != 0 || deltaY != 0) {
			while(isInBetween(x1 + 0.5,x2 + 0.5,checkingPosX) && isInBetween(y1 + 0.5,y2 + 0.5,checkingPosY)) {
				if (isBlockOpaque((int)Math.floor(checkingPosX), (int)Math.floor(checkingPosY))) {
					if (x2 != Math.floor(checkingPosX) || y2 != Math.floor(checkingPosY)) {
						answer = false;
					}
				}
				double xNextLine;
				double yNextLine;
				if (deltaX > 0) {
					xNextLine = (Math.ceil(checkingPosX) - checkingPosX) / deltaX;
				} else if (deltaX < 0) {
					xNextLine = (checkingPosX - Math.floor(checkingPosX)) / deltaX;
				} else {
					xNextLine = lightDiffuseDistance + 1;
				}
				if (xNextLine == 0) {
					xNextLine = 1.0 / deltaX;
				}
				if (deltaY > 0) {
					yNextLine = (Math.ceil(checkingPosY) - checkingPosY) / deltaY;
				} else if (deltaY < 0) {
					yNextLine = (checkingPosY - Math.floor(checkingPosY)) / deltaY;
				} else {
					yNextLine = lightDiffuseDistance + 1;
				}
				if (yNextLine == 0) {
					yNextLine = 1.0 / deltaY;
				}
				xNextLine = Math.abs(xNextLine);
				yNextLine = Math.abs(yNextLine);
				if (Math.abs(xNextLine) < Math.abs(yNextLine)) {
					checkingPosX += xNextLine * deltaX;
					checkingPosY += xNextLine * deltaY;
				} else {
					checkingPosX += yNextLine * deltaX;
					checkingPosY += yNextLine * deltaY;
				}

			}
		}
		return answer;
	}
	private static boolean isInBetween(double num1, double num2, double numCheck) {
		if (num1 <= numCheck && num2 >= numCheck) { return true; }
		return (num1 >= numCheck && num2 <= numCheck);
	}
	private void setBrightness(int x, int y, double brightness) {
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		dataManager.savable.lightMap.put(x + "," + y, (byte)(brightness*255.0-128.0));
	}
	private boolean doesContainLightSource(int x, int y) {
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y)).isLightSource;
		}
		return false;
	}
}
