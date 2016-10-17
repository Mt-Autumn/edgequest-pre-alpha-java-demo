package com.mtautumn.edgequest.window;


import com.mtautumn.edgequest.window.layers.*;
import com.mtautumn.edgequest.window.layers.Character;

public class Layers {
	static void draw(Renderer r) {
		if (r.dataManager.system.isGameOnLaunchScreen) {
			LaunchScreen.draw(r);
		} else {
			Terrain.draw(r);
			Footprints.draw(r);
			CharacterEffects.draw(r);
			Character.draw(r);
			BlockDamage.draw(r);
			r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			Lighting.draw(r);
			if (!r.dataManager.system.hideMouse) MouseSelection.draw(r);
			r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			if (r.dataManager.system.showConsole) Console.draw(r);
			if (r.dataManager.system.isKeyboardBackpack) Backpack.draw(r);
			if (r.dataManager.system.isKeyboardMenu) Menu.draw(r);
			r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
			if (r.dataManager.settings.showDiag) DiagnosticsWindow.draw(r);
		}
		OptionPane.draw(r);
	}
	
}
