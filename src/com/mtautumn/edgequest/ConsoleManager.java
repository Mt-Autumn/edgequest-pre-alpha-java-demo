/* Keeps track of the lines visible in the game console. Will also
 * parse new lines for commands and run such commands if they exist.
 */
package com.mtautumn.edgequest;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;

public class ConsoleManager {
	DataManager dataManager;
	public ConsoleManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public class Line {
		private long creationTime;
		private String text;
		public Color color = new Color(Color.white);
		
		Line(String text) {
			creationTime = System.currentTimeMillis();
			this.text = text;
		}
		Line(String text, Color color) {
			creationTime = System.currentTimeMillis();
			this.text = text;
			this.color = color;
		}
		public String getText() { return text; }
		public long getTime() { return creationTime; }
	}	
	public ArrayList<Line> lines = new ArrayList<Line>();
	
	
	public void addLine(String text) {
		// Linux doesn't like colons
		if (text.startsWith("/"))
			parseCommand(text);
		else
			lines.add(new Line(text));
	}
	public void addLine(String text, int type) {
		// Linux doesn't like colons
		if (text.startsWith("/"))
			parseCommand(text);
		else
			switch (type) {
			case 1:
				lines.add(new Line(text,Color.red));
				break;
			case 2:
				lines.add(new Line(text,Color.blue));
				break;
			default:
				lines.add(new Line(text));
				break;
			}
	}
	private void parseCommand(String text) {
		String cmdName;
		boolean moreArgs;
		if (text.contains(" ")) {
			cmdName = text.substring(1, text.indexOf(" "));
			moreArgs = text.indexOf(" ") + 1 < text.length();
		} else {
			cmdName = text.substring(1, text.length());
			moreArgs = false;
		}
		ArrayList<String> args = new ArrayList<String>();
		String remaining = text.substring(text.indexOf(" ") + 1, text.length());
		while (moreArgs) {
			String nextArg;
			if (remaining.contains(" ")) {
				nextArg = remaining.substring(0, remaining.indexOf(" "));
			} else {
				nextArg = remaining.substring(0, remaining.length());
			}
			args.add(nextArg);			
			moreArgs = false;
			if (remaining.contains(" ")) {
				if (remaining.length() > remaining.indexOf(" ") + 1) {
				remaining = remaining.substring(remaining.indexOf(" ") + 1, remaining.length());
				moreArgs = true;
				}
			}
		}
		try {
		runCommand(cmdName, args);
		} catch (Exception e) {
			addLine("Command entered wrong", 1);
		}
	}
	private void runCommand(String cmdName, ArrayList<String> args) throws InterruptedException {
		switch (cmdName) {
		case "time":
			if (args.size() == 1) {
				dataManager.savable.time = Integer.parseInt(args.get(0));
				addLine("Time set to: " + dataManager.savable.time, 2);
			} else if (args.size() == 0)
				addLine("Time: " + dataManager.savable.time, 2);
			else
				addLine("use the format /time [0-2399]", 1);
			break;
		case "tp":
			if (args.size() == 2) {
				dataManager.characterManager.characterEntity.setX(Double.parseDouble(args.get(0)));
				dataManager.characterManager.characterEntity.setX(Double.parseDouble(args.get(1)));
				dataManager.system.blockGenerationLastTick = true;
				addLine("Teleported to: " + args.get(0) + ", " + args.get(1), 2);
			} else
				addLine("use the format /tp <posX> <posY>", 1);
			break;
		case "speed":
			if (args.size() == 1) {
				dataManager.settings.moveSpeed = Double.parseDouble(args.get(0));
				addLine("Speed set to: " + dataManager.settings.moveSpeed, 2);
			} else if (args.size() == 0)
				addLine("Speed is: " + dataManager.settings.moveSpeed, 2);
			else
				addLine("use the format /speed [value]", 1);
			break;
		case "reseed":
			if (args.size() > 0) {
				dataManager.savable.seed = (long) Double.parseDouble(args.get(0));
				dataManager.resetTerrain();
				addLine("reseeded to seed: " + args.get(0), 2);
			} else
				addLine("use the format /reseed <seed>", 1);
			break;
		case "seed":
			if (args.size() == 0)
				addLine("seed: " + dataManager.savable.seed, 2);
			else
				addLine("To change the current seed, type /reseed <seed>", 1);
			break;
		case "help":
			addLine("Command List: ", 2);
			Thread.sleep(1);
			addLine("     (1) /time [0-2399]", 2);
			Thread.sleep(1);
			addLine("     (2) /tp <posX> <posY>", 2);
			Thread.sleep(1);
			addLine("     (3) /seed", 2);
			Thread.sleep(1);
			addLine("     (4) /speed [value]", 2);
			Thread.sleep(1);
			addLine("     (5) /reseed <seed>", 2);
			break;
		default:
			addLine("unknown command \"" + cmdName + "\"", 1);
			break;
		}
	}
	public Line[] getNewestLines(int count) {
		Line[] lines = new Line[count];
		@SuppressWarnings("unchecked")
		ArrayList<Line> lineDB = (ArrayList<Line>) this.lines.clone();
		for (int i = 0; i < count; i++) {
			Line newest = getNewest(lineDB);
			lines[i] = newest;
			if (newest != null)
				lineDB.remove(newest);
		}
		return lines;
	}
	
	private static Line getNewest(ArrayList<Line> lines) {
		Line newest = null;
		for (int i = 0; i < lines.size(); i++) {
			if (newest != null) {
				if (newest.creationTime < lines.get(i).creationTime)
					newest = lines.get(i);
			} else {
				newest = lines.get(i);
			}
		}
		return newest;
	}

}
