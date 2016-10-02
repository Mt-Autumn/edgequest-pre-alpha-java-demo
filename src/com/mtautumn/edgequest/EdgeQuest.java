package com.mtautumn.edgequest;

public class EdgeQuest {
	public static SceneManager sceneManager = new SceneManager();
	public static BackpackManager backpackManager = new BackpackManager(sceneManager);
	public static KeyboardInput keyboard = new KeyboardInput();
	public static BlockUpdateManager blockUpdateManager = new BlockUpdateManager(sceneManager);
	public static CharacterManager characterManager = new CharacterManager(sceneManager, blockUpdateManager);
	//public static KeyboardManager keyboardManager = new KeyboardManager(sceneManager, keyboard, characterManager);
	public static RendererManager rendererManager = new RendererManager(sceneManager, keyboard);
	public static TerrainManager terrainManager = new TerrainManager(sceneManager);
	public static GameClock gameClock = new GameClock(sceneManager);
	public static AnimationClock animationClock = new AnimationClock(sceneManager);
	public static AutoCharacterWalk autoCharacterWalk = new AutoCharacterWalk(sceneManager);
	public static void main(String[] args) throws InterruptedException {
		//keyboardManager.start();
		characterManager.start();
		terrainManager.start();
		rendererManager.start();
		gameClock.start();
		animationClock.start();
		blockUpdateManager.start();
		autoCharacterWalk.run();
		while (true) {
			Thread.sleep(10000);
		}

	}
}
