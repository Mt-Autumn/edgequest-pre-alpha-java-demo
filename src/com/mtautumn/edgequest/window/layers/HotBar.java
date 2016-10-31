package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class HotBar {
	public static final int HOTBAR_WIDTH = 125;
	public static final int HOTBAR_HEIGHT = 403;
	public static final int START_X = 20;
	public static final int START_Y = 66;
	public static final int DELTA_X = 53;
	public static final int DELTA_Y = 53;
	static int xPos;
	static int yPos;
	public static void draw(Renderer r) {
		Color.white.bind();
		drawBackground(r);
		drawSpaces(r);	
	}
	
	private static void drawBackground(Renderer r) {
		yPos = (r.dataManager.settings.screenHeight - HOTBAR_HEIGHT) - 24;
		xPos = r.dataManager.settings.screenWidth - HOTBAR_WIDTH;
		r.drawTexture(r.textureManager.getTexture("hotBar"), xPos, yPos, HOTBAR_WIDTH, HOTBAR_HEIGHT);
	}
	
	private static void drawSpaces(Renderer r) {
		for (int i = 0; i < 2; i++) {
			int posX = xPos + i * DELTA_X + START_X;
			for (int j = 0; j < 6; j++) {
				int posY = yPos + j * DELTA_Y + START_Y;
				Color.white.bind();
				try {
					r.drawTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.backpackItems[i][j].getItemID()).getItemImg(r.dataManager.savable.time), posX, posY, 38, 38);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (r.dataManager.savable.backpackItems[i][j].getItemCount() != 0) {
					r.backpackFont.drawString(posX, posY, "" + r.dataManager.savable.backpackItems[i][j].getItemCount(), Color.black);
				}
			}
		}
	}
	
}
