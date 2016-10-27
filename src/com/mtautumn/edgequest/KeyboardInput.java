/* Solely used for text input
 * When an input menu appears, this is run to add text to that input menu
 * This is not used for key bindings in the game (like walking)
 */
package com.mtautumn.edgequest;

import org.lwjgl.input.Keyboard;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.layers.OptionPane;

public class KeyboardInput {
	DataManager dataManager;
	public boolean wasConsoleUp = false;
	private enum KeyState {

		RELEASED,
		PRESSED,
		ONCE

	}

	private KeyState[] keys = null;

	public KeyboardInput(DataManager dataManager) {
		this.dataManager = dataManager;
		keys = new KeyState[ Keyboard.KEYBOARD_SIZE ];

		for( int i = 0; i < Keyboard.KEYBOARD_SIZE; ++i ) {

			keys[ i ] = KeyState.RELEASED;

		}

	}

	public synchronized void poll() {
		Keyboard.poll();
		for( int i = 0; i < Keyboard.KEYBOARD_SIZE; ++i ) {
			if (Keyboard.isKeyDown(i)) {
				if (keys[i] == KeyState.ONCE) {
					keys[i] = KeyState.PRESSED;
				} else if (keys[i] == KeyState.RELEASED) {
					keys[i] = KeyState.ONCE;
					if (dataManager.system.inputTextResponse.size() != 0 ) {						
						String inputResponse = 	dataManager.system.inputTextResponse.get(dataManager.system.inputTextResponse.size() - 1);
						if (Keyboard.getKeyName(i).equals("BACK") || Keyboard.getKeyName(i).equals("DELETE")) {
							inputResponse = delete(inputResponse);
						}
						dataManager.system.inputTextResponse.set(dataManager.system.inputTextResponse.size() - 1, inputResponse + getKeyString(Keyboard.getKeyName(i)));
						if (Keyboard.getKeyName(i).equals("RETURN") || Keyboard.getKeyName(i).equals("ENTER")) {
							OptionPane.closeOptionPane(dataManager);
						}
					} else {
						if (wasConsoleUp) {
							String inputResponse = dataManager.system.consoleText;
							if (Keyboard.getKeyName(i).equals("BACK") || Keyboard.getKeyName(i).equals("DELETE")) {
								inputResponse = delete(inputResponse);
							}
							dataManager.system.consoleText = inputResponse + getKeyString(Keyboard.getKeyName(i));
							if (Keyboard.getKeyName(i).equals("RETURN") || Keyboard.getKeyName(i).equals("ENTER")) {
								dataManager.consoleManager.addLine(dataManager.system.consoleText);
								dataManager.system.consoleText = "";
							}
						}
					}
				}
			} else {
				keys[i] = KeyState.RELEASED;
			}

		}
	}

	public boolean isKeyDown( int keyCode ) {
		return keys[ keyCode ] == KeyState.ONCE || keys[ keyCode ] == KeyState.PRESSED;
	}



	public boolean isKeyDownOnce( int keyCode ) {
		return keys[ keyCode ] == KeyState.ONCE;
	}

	private static String getKeyString(String keyName) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			if (keyName.length() == 1) {
				return keyName;
			}
			switch (keyName) {
			case "SPACE":
				return " ";
			case "PERIOD":
				return ">";
			case "SEMICOLON":
				return ":";
			case "EQUALS":
				return "+";
			case "MINUS":
				return "_";
			case "LBRACKET":
				return "{";
			case "RBRACKET":
				return "}";
			case "BACKSLASH":
				return "|";
			case "APOSTROPHE":
				return "\"";
			case "COMMA":
				return "<";
			case "SLASH":
				return "?";
			default:
				return "";
			}
		}
		if (keyName.length() == 1) {
			return keyName.toLowerCase();
		}
		switch (keyName) {
		case "SPACE":
			return " ";
		case "PERIOD":
			return ".";
		case "SEMICOLON":
			return ";";
		case "EQUALS":
			return "=";
		case "MINUS":
			return "-";
		case "LBRACKET":
			return "[";
		case "RBRACKET":
			return "]";
		case "BACKSLASH":
			return "\\";
		case "APOSTROPHE":
			return "'";
		case "COMMA":
			return ",";
		case "SLASH":
			return "/";
		default:
			return "";
		}
	}
	public String delete(String str) {
		if (str != null && str.length() > 0) {
			str = str.substring(0, str.length()-1);
		}
		return str;
	}

}