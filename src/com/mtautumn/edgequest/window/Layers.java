package com.mtautumn.edgequest.window;


import com.mtautumn.edgequest.window.layers.*;
import com.mtautumn.edgequest.window.layers.Character;

public class Layers {
	static void draw(Renderer r) {
		if (r.sceneManager.system.isGameOnLaunchScreen) {
			LaunchScreen.draw(r);
		} else {
			Terrain.draw(r);
			Footprints.draw(r);
			CharacterEffects.draw(r);
			Character.draw(r);
			r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			Lighting.draw(r);
			if (!r.sceneManager.system.hideMouse) MouseSelection.draw(r);
			r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			if (r.sceneManager.system.isKeyboardBackpack) Backpack.draw(r);
			if (r.sceneManager.system.isKeyboardMenu) Menu.draw(r);
			r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			if (r.sceneManager.settings.showDiag) DiagnosticsWindow.draw(r);
		}
		OptionPane.draw(r);
	}
	
}
