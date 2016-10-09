package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.data.DataManager;

public class BlockUpdateManager extends Thread {
	DataManager dataManager;
	private int lightDiffuseDistance = 8;
	public BlockUpdateManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		int i = 0;
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					i++;
					if (i % 30 == 0) melt();
					updateMining();
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void updateLighting(int x, int y) {
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
		} else {
			return 0.0;
		}
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
	private boolean isInBetween(double num1, double num2, double numCheck) {
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
	private boolean wasMouseDown = false;
	private void updateMining() {
		if (!dataManager.system.isKeyboardBackpack && !dataManager.system.isKeyboardMenu) {
			if (dataManager.system.leftMouseDown && wasMouseDown && !dataManager.system.isMouseFar) {
				if (dataManager.system.miningX != dataManager.system.mouseX || dataManager.system.miningY != dataManager.system.mouseY) {
					dataManager.system.miningX = dataManager.system.mouseX;
					dataManager.system.miningY = dataManager.system.mouseY;
					dataManager.system.blockDamage = 0;
				}
				dataManager.system.blockDamage += 1.0/getBlockAt(dataManager.system.mouseX, dataManager.system.mouseY).hardness/dataManager.settings.tickLength;
				if (dataManager.system.blockDamage < 0) dataManager.system.blockDamage = 0;
				if (dataManager.system.blockDamage >= 10) {
					dataManager.system.blockDamage = 0;
					breakBlock(dataManager.system.mouseX, dataManager.system.mouseY);
				}
			} else {
				dataManager.system.blockDamage = 0;
			}
		}
		wasMouseDown = dataManager.system.leftMouseDown;
	}
	private BlockItem getBlockAt(int x, int y) {
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			return dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y));
		} else if (dataManager.savable.map.containsKey(x + "," + y)) {
			return dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y));
		} else {
			return null;
		}

	}
	private void breakBlock(int x, int y) {
		BlockItem item = null;
		if (dataManager.savable.playerStructuresMap.containsKey(x + "," + y)) {
			item = dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x + "," + y));
			dataManager.savable.playerStructuresMap.remove(x + "," + y);

		} else if (dataManager.savable.map.containsKey(x + "," + y)) {
			item = dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y));
			String replacement = dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y)).replacedBy;
			dataManager.savable.map.put(x + "," + y,dataManager.system.blockNameMap.get(replacement).getID());
		}
		if (item != null) {
			BlockItem result = dataManager.system.blockNameMap.get(item.breaksInto);
			if (result.getIsItem()) {
				dataManager.backpackManager.addItem(result);
			}
		}
	}
	private void melt() {
		for(int x = dataManager.system.minTileX; x <= dataManager.system.maxTileX; x++) {
			for(int y = dataManager.system.minTileY; y <= dataManager.system.maxTileY; y++) {
				if (dataManager.savable.map.containsKey(x+","+y)) {
					if (dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y)).melts) {
						double brightness = 0;
						if (dataManager.savable.lightMap.containsKey(x+","+y)) {
							brightness = Double.valueOf(((int) dataManager.savable.lightMap.get(x + "," + y) + 128)) / 255.0;
						}
						if (brightness > 0.7) {
							if (1 - Math.random() < (brightness - 0.7) / 50.0) {
								dataManager.savable.map.put(x+","+y, dataManager.system.blockNameMap.get(dataManager.system.blockIDMap.get(dataManager.savable.map.get(x + "," + y)).meltsInto).getID());
							}
						}
					}
				}
			}	
		}
	}
}
