package com.mtautumn.edgequest.data;

import com.mtautumn.edgequest.AutoCharacterWalk;
import com.mtautumn.edgequest.BackpackManager;
import com.mtautumn.edgequest.ButtonActionManager;
import com.mtautumn.edgequest.CharacterManager;
import com.mtautumn.edgequest.ConsoleManager;
import com.mtautumn.edgequest.GameClock;
import com.mtautumn.edgequest.TerrainManager;
import com.mtautumn.edgequest.updates.AnimationClock;
import com.mtautumn.edgequest.updates.BlockUpdateManager;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;
import com.mtautumn.edgequest.window.managers.RendererManager;

public class DataManager {
	public SystemData system = new SystemData();
	public SavableData savable = new SavableData();
	public SettingsData settings = new SettingsData();
	
	public MenuButtonManager menuButtonManager;
	public BackpackManager backpackManager = new BackpackManager(this);
	public BlockUpdateManager blockUpdateManager = new BlockUpdateManager(this);
	public CharacterManager characterManager = new CharacterManager(this);
	public RendererManager rendererManager = new RendererManager(this);
	public TerrainManager terrainManager = new TerrainManager(this);
	public GameClock gameClock = new GameClock(this);
	public AnimationClock animationClock = new AnimationClock(this);
	public AutoCharacterWalk autoCharacterWalk = new AutoCharacterWalk(this);
	public ButtonActionManager buttonActionManager = new ButtonActionManager(this);
	public ConsoleManager consoleManager = new ConsoleManager(this);
}
