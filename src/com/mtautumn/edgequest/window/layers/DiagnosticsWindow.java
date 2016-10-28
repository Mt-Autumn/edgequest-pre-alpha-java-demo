package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class DiagnosticsWindow {
	public static void draw(Renderer r) {
		Color.white.bind();
		r.fillRect(10,10, 215, 240, 0.7f, 0.7f, 0.7f, 0.7f);

		int i = 0;
		r.font.drawString(20, i+=20, "FPS: " + r.dataManager.system.averagedFPS);
		r.font.drawString(20, i+=20, "Time: " + r.dataManager.savable.time);
		r.font.drawString(20, i+=20, "Time Human: " + r.dataManager.system.timeReadable);
		r.font.drawString(20, i+=20, "Brightness: " + r.dataManager.world.getBrightness());
		r.font.drawString(20, i+=20, "CharX: " + r.dataManager.characterManager.characterEntity.getX());
		r.font.drawString(20, i+=20, "CharY: " + r.dataManager.characterManager.characterEntity.getY());
		r.font.drawString(20, i+=20, "CharDir: " + r.dataManager.characterManager.characterEntity.getRot());
		r.font.drawString(20, i+=20, "Dungeon Lvl: " + r.dataManager.savable.dungeonLevel);
		r.font.drawString(20, i+=20, "CharMove: " + r.dataManager.system.characterMoving);
		r.font.drawString(20, i+=20, "Terrain Gen: " + r.dataManager.system.blockGenerationLastTick);
		r.font.drawString(20, i+=20, "Block Size: " + r.dataManager.settings.blockSize);
	}
}
