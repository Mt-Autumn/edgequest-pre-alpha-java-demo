package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.FootPrint;
import com.mtautumn.edgequest.data.DataManager;

public class UpdateFootprints {
	DataManager dataManager;
	double lastFootX = 0.0;
	double lastFootY = 0.0;
	public UpdateFootprints(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void update() {
		int charX = (int) Math.floor(dataManager.savable.charX);
		int charY = (int) Math.floor(dataManager.savable.charY);
		if (dataManager.savable.map.containsKey(charX + "," + charY)) {
			if (dataManager.system.blockIDMap.get(dataManager.savable.map.get(charX + "," + charY)).canHavePrints) {
				if (Math.sqrt(Math.pow(dataManager.savable.charX - lastFootX, 2)+Math.pow(dataManager.savable.charY - lastFootY, 2)) > 0.7) {
					lastFootX = dataManager.savable.charX;
					lastFootY = dataManager.savable.charY;
					dataManager.savable.footPrints.add(new FootPrint(dataManager.savable.charX, dataManager.savable.charY, dataManager.savable.charDir));
				}
			}
		}
		for (int i = 0; i < dataManager.savable.footPrints.size(); i++) {
			dataManager.savable.footPrints.get(i).opacity -= 0.001;
			if (dataManager.savable.footPrints.get(i).opacity <= 0) {
				dataManager.savable.footPrints.remove(i);
			}

		}
	}
}
