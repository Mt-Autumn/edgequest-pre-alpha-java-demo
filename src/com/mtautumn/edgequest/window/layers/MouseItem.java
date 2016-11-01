package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class MouseItem {
	public static void draw(Renderer r) {
		Color.white.bind();
		drawItem(r);
	}
	private static void drawItem(Renderer r) {
		if (r.dataManager.savable.mouseItem.getItemCount() > 0) {
			int posX = (int) (r.dataManager.system.mousePosition.getX() - 24);
			int posY = (int) (r.dataManager.system.mousePosition.getY() - 24);
			r.drawTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.mouseItem.getItemID()).getItemImg(r.dataManager.savable.time), posX, posY, 48, 48);
			r.backpackFont.drawString(posX, posY, "" + r.dataManager.savable.mouseItem.getItemCount(), Color.black);
		}
		
	}
	
}
