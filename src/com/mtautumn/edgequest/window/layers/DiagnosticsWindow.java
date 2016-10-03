package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class DiagnosticsWindow {
	public static void draw(Renderer r) {
		Color.blue.bind();
		//TODO: Create class for generating texture from string
		r.fillRect(10,10, 215, 220, 0.7f, 0.7f, 0.7f, 0.7f);
		
		int i = 0;
		r.font.drawString(20, i+=20, "FPS: " + r.sceneManager.system.averagedFPS);
		r.font.drawString(20, i+=20, "Time: " + r.sceneManager.savable.time);
		r.font.drawString(20, i+=20, "Time Human: " + r.sceneManager.system.timeReadable);
		r.font.drawString(20, i+=20, "Brightness: " + r.sceneManager.savable.getBrightness());
		r.font.drawString(20, i+=20, "CharX: " + r.sceneManager.savable.charX);
		r.font.drawString(20, i+=20, "CharY: " + r.sceneManager.savable.charY);
		r.font.drawString(20, i+=20, "CharDir: " + r.sceneManager.savable.charDir);
		r.font.drawString(20, i+=20, "CharMove: " + r.sceneManager.system.characterMoving);
		r.font.drawString(20, i+=20, "Terrain Gen: " + r.sceneManager.system.blockGenerationLastTick);
		r.font.drawString(20, i+=20, "Block Size: " + r.sceneManager.settings.blockSize);
	}
}
