/* Gets called by BlockUpdateManager and checks to see if a new footprint
 * should be added.
 */
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
		int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
		if (dataManager.world.isGroundBlock(charX, charY)) {
			if (dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(charX, charY)).canHavePrints) {
				if (Math.sqrt(Math.pow(dataManager.characterManager.characterEntity.getX() - lastFootX, 2)+Math.pow(dataManager.characterManager.characterEntity.getY() - lastFootY, 2)) > 0.7) {
					lastFootX = dataManager.characterManager.characterEntity.getX();
					lastFootY = dataManager.characterManager.characterEntity.getY();
					dataManager.savable.footPrints.add(new FootPrint(dataManager.characterManager.characterEntity.getX(), dataManager.characterManager.characterEntity.getY(), dataManager.characterManager.characterEntity.getRot()));
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
