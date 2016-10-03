package com.mtautumn.edgequest;

import com.mtautumn.edgequest.window.managers.RendererManager;

public class EdgeQuest {
	public static SceneManager sceneManager = new SceneManager();
	public static BackpackManager backpackManager = new BackpackManager(sceneManager);
	public static BlockUpdateManager blockUpdateManager = new BlockUpdateManager(sceneManager);
	public static CharacterManager characterManager = new CharacterManager(sceneManager, blockUpdateManager);
	public static RendererManager rendererManager = new RendererManager(sceneManager, characterManager);
	public static TerrainManager terrainManager = new TerrainManager(sceneManager);
	public static GameClock gameClock = new GameClock(sceneManager);
	public static AnimationClock animationClock = new AnimationClock(sceneManager);
	public static AutoCharacterWalk autoCharacterWalk = new AutoCharacterWalk(sceneManager);
	public static ButtonActionManager buttonActionManager = new ButtonActionManager(sceneManager);
	public static void main(String[] args) throws InterruptedException {
		characterManager.start();
		terrainManager.start();
		rendererManager.start();
		gameClock.start();
		animationClock.start();
		blockUpdateManager.start();
		autoCharacterWalk.start();
		buttonActionManager.start();
		while (true) {
			Thread.sleep(10000);
		}

	}
}
