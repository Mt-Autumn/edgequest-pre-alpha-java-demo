package com.mtautumn.edgequest.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.Dungeon;
import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.FootPrint;
import com.mtautumn.edgequest.ItemSlot;

public class SavableData implements Serializable {
	private static final long serialVersionUID = 1L;
	public int time = 800;
	public Map<String, Short> map = new HashMap<String, Short>();
	public Map<String, Byte> lightMap = new HashMap<String, Byte>();
	public Map<String, Short> playerStructuresMap = new HashMap<String, Short>();
	public Map<String, Dungeon> dungeonMap = new HashMap<String, Dungeon>();
	public ArrayList<FootPrint> footPrints = new ArrayList<FootPrint>();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public long seed = 7;
	public boolean isInDungeon = false;
	public int dungeonX = 0;
	public int dungeonY = 0;
	public int dungeonLevel = -1;
	public int lastDungeonLevel = -1;
	public long dungeonCount = 0;
	public int entityID = 0;
	public int hotBarSelection = 0;
	public ItemSlot[][] backpackItems = new ItemSlot[8][6];
	public ItemSlot mouseItem = new ItemSlot();
}