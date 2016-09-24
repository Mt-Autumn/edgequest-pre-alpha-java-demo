package com.mtautumn.edgequest;

public class EdgeQuest {
	public static SceneManager sceneManager = new SceneManager();
	public static MenuButtonManager menuButtonManager = new MenuButtonManager(sceneManager);
	public static KeyboardInput keyboard = new KeyboardInput();
	public static BlockUpdateManager blockUpdateManager = new BlockUpdateManager(sceneManager);
	public static CharacterManager characterManager = new CharacterManager(sceneManager, blockUpdateManager);
	public static KeyboardManager keyboardManager = new KeyboardManager(sceneManager, keyboard, characterManager);
	public static LaunchScreenManager launchScreenManager = new LaunchScreenManager(sceneManager);
	public static RendererManager rendererManager = new RendererManager(sceneManager, keyboard, menuButtonManager, launchScreenManager);
	public static TerrainManager terrainManager = new TerrainManager(sceneManager);
	public static GameClock gameClock = new GameClock(sceneManager);
	public static AnimationClock animationClock = new AnimationClock(sceneManager);
	public static void main(String[] args) throws InterruptedException {
		keyboardManager.start();
		characterManager.start();
		terrainManager.start();
		rendererManager.start();
		gameClock.start();
		animationClock.start();
		blockUpdateManager.start();
		while (true) {
			Thread.sleep(10000);
		}
		
	}
}
