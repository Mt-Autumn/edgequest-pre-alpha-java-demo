/* Called by BlockUpdateManager (mostly) and will update lighting when the
 * update method is called (at the position x,y)
 */
package com.mtautumn.edgequest.updates;

import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.data.DataManager;

public class UpdateLighting {
	private int lightDiffuseDistance = 8;
	DataManager dataManager;
	Map<String, Byte> bufferedLightMap = new HashMap<String, Byte>();
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
		lightStructBlocks(x, y);
		pushBuffer(x, y);
		bufferedLightMap.clear();
	}
	private void lightStructBlocks(int x, int y) {
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				if(isBlockOpaque(i, j)) {
					double maxBrightness = 0;
					for( int k = i - 1; k <= i+1; k++) {
						for( int l = j - 1; l <= j+1; l++) {
							if (getBlockBrightness(k, l) > maxBrightness && !isBlockOpaque(k, l)) {
								maxBrightness = getBlockBrightness(k, l);
							}
						}
					}
					setBrightness(i, j, maxBrightness);
				}
			}
		}
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
		if (isBlockOpaque(x, y)) return 0;
		double lighting = 0.0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (!isBlockOpaque(i, j)) {
					lighting += getBlockBrightness(i,j);
				}
			}
		}
		if (getBlockBrightness(x, y) > lighting / 9.0) {
			return getBlockBrightness(x, y);
		}
		return lighting / 9.0;
	}
	private double getBlockBrightness(int x, int y) {
		if (bufferedLightMap.containsKey(x+","+y)) {
			return Double.valueOf(bufferedLightMap.get(x+","+y) + 128) / 255.0;
		}
		if (dataManager.world.isLight(x, y)) {
			return Double.valueOf((dataManager.world.getLight(x, y) + 128)) / 255.0;
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
		if (dataManager.world.isStructBlock(x, y)) {
			return !dataManager.system.blockIDMap.get(dataManager.world.getStructBlock(x, y)).isPassable;
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
					//if (!(Math.abs(checkingPosX % 1.0) < 0.001 && Math.abs(checkingPosY % 1.0) < 0.001)) {
					answer = false;
					//}
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
				if ((Math.abs(checkingPosX % 1.0) < 0.001 && Math.abs(checkingPosY % 1.0) < 0.001)) {
					xNextLine = 0.01;
					yNextLine = 0.01;
				}
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
		bufferedLightMap.put(x+","+y, (byte)(brightness*255.0-128.0));
	}
	private void pushBuffer(int x, int y) {
		for (int i = x - lightDiffuseDistance; i <= x + lightDiffuseDistance; i++) {
			for (int j = y - lightDiffuseDistance; j <= y + lightDiffuseDistance; j++) {
				if (bufferedLightMap.containsKey(i+","+j)) {
					dataManager.world.setLight(i,j,bufferedLightMap.get(i+","+j));
				}
			}
		}
	}
	private boolean doesContainLightSource(int x, int y) {
		if (dataManager.world.isStructBlock(x, y)) {
			return dataManager.system.blockIDMap.get(dataManager.world.getStructBlock(x, y)).isLightSource;
		}
		return false;
	}
}
