package com.mtautumn.edgequest;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class ConsoleManager {
	DataManager dataManager;
	public ConsoleManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public class Line {
		private long creationTime;
		private String text;

		Line(String text) {
			creationTime = System.currentTimeMillis();
			this.text = text;
		}
		public String getText() { return text; }
		public long getTime() { return creationTime; }
	}	
	public ArrayList<Line> lines = new ArrayList<Line>();


	public void addLine(String text) {
		if (text.startsWith(":"))
			parseCommand(text);
		else
			lines.add(new Line(text));
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
			if (remaining.contains(" "))
				nextArg = remaining.substring(0, remaining.indexOf(" "));
			else
				nextArg = remaining.substring(0, remaining.length());
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
			addLine("Command entered wrong");
		}
	}
	private void runCommand(String cmdName, ArrayList<String> args) throws InterruptedException {
		switch (cmdName) {
		case "time":
			if (args.size() > 0)
				dataManager.savable.time = Integer.parseInt(args.get(0));
			else
				addLine("use the format :time [0-2399]");
			break;
		case "tp":
			if (args.size() > 1) {
				dataManager.savable.charX = Double.parseDouble(args.get(0));
				dataManager.savable.charY = Double.parseDouble(args.get(1));
				dataManager.system.blockGenerationLastTick = true;
			} else
				addLine("use the format :tp posX posY");
			break;
		case "speed":
			if (args.size() > 0)
				dataManager.settings.moveSpeed = Double.parseDouble(args.get(0));
			else
				addLine("use the format :speed value");
			break;
		case "help":
			addLine("Command List: ");
			Thread.sleep(1);
			addLine("     (1) :time [0-2399]");
			Thread.sleep(1);
			addLine("     (2) :tp posX posY");
			Thread.sleep(1);
			addLine("     (3) :speed value");
			break;
		default:
			addLine("unknown command \"" + cmdName + "\"");
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
				else
					newest = lines.get(i);
			}
			return newest;
		}

	}
