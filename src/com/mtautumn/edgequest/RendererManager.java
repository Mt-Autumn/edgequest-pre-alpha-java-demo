package com.mtautumn.edgequest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RendererManager extends Thread {
	static JFrame window = new JFrame("edgequest");
	private static boolean wasMenuUp = false;
	private static SceneManager sceneManager;
	KeyboardInput keyboard = new KeyboardInput();
	int[] lastXFPS = new int[5];
	int tempFPS;
	public RendererManager(SceneManager scnMgr, KeyboardInput kybd, MenuButtonManager mbm, LaunchScreenManager lsm) {
		sceneManager = scnMgr;
		keyboard = kybd;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.DARK_GRAY);
		window.setBounds(0, 0, sceneManager.settings.screenWidth, sceneManager.settings.screenHeight);
		window.setResizable(true);
		window.setMinimumSize(new Dimension(800, 600));
		window.setName("edgequest");
		window.setTitle("edgequest");
		window.getContentPane().add(new Renderer(sceneManager, mbm, lsm));
		JPanel content = (JPanel) window.getContentPane();
		window.addKeyListener(keyboard);
		content.addKeyListener(keyboard);
		content.addMouseListener(
				new MouseAdapter()
				{
					public void mouseClicked(MouseEvent me)
					{
						sceneManager.system.autoWalk = false;
						if (sceneManager.system.isKeyboardMenu) {
							mbm.buttonPressed(me.getX(), me.getY());
						} else if (sceneManager.system.isGameOnLaunchScreen) {
							lsm.buttonPressed(me.getX(), me.getY());
						} else if (sceneManager.system.isKeyboardSprint){
							sceneManager.system.autoWalkX = sceneManager.system.mouseX;
							sceneManager.system.autoWalkY = sceneManager.system.mouseY;
							sceneManager.system.autoWalk = true;
						}
					}
				});
	}

	public void run() {
		window.setVisible(false);
		prepareFPSCounting();
		long lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/sceneManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		double lastNanoPause = (1000000000.0/Double.valueOf(sceneManager.settings.targetFPS));
		window.setVisible(true);
		while (true) {
			updateWindowSize();
			updateMouse();
			tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
			lastNanoTimeFPSGrabber = System.nanoTime();
			updateAverageFPS(tempFPS);
			updateWindow();
			try {
				lastNanoPause += (1.0/Double.valueOf(sceneManager.settings.targetFPS) - 1.0/Double.valueOf(sceneManager.system.averagedFPS)) * 50000000.0;
				if (lastNanoPause < 0) lastNanoPause = 0;
				Thread.sleep((long)Math.floor(lastNanoPause / 1000000.0),(int) Math.floor(lastNanoPause % 1000000.0));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	public static void updateWindow() {
		if (!wasMenuUp) {
			if (!sceneManager.system.isLaunchScreenLoaded) {
				window.getContentPane().repaint();
				window.setVisible(true);
				if (sceneManager.system.isGameOnLaunchScreen) {
					sceneManager.system.isLaunchScreenLoaded = true;
				}
			}
		}
		wasMenuUp = sceneManager.system.isKeyboardMenu;
	}
	private void updateAverageFPS(int FPS) {
		if (FPS / 5.0 > sceneManager.system.averagedFPS) FPS = sceneManager.system.averagedFPS+1;
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		sceneManager.system.averagedFPS = fpsSum / lastXFPS.length;
	}
	private void prepareFPSCounting() {
		sceneManager.system.averagedFPS = sceneManager.settings.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = sceneManager.settings.targetFPS;
		}
	}
	private void updateWindowSize() {
		if (sceneManager.settings.screenWidth != window.getContentPane().getWidth() || sceneManager.settings.screenHeight != window.getContentPane().getHeight()) {
			sceneManager.settings.screenWidth = window.getContentPane().getWidth();
			sceneManager.settings.screenHeight = window.getContentPane().getHeight();
			sceneManager.system.blockGenerationLastTick = true;
		}
	}
	private void updateMouse() {
		if (((JPanel) window.getContentPane()).getMousePosition() != null) {
		sceneManager.system.mousePosition = ((JPanel) window.getContentPane()).getMousePosition();
		double offsetX = (sceneManager.system.charX * Double.valueOf(sceneManager.settings.blockSize) - Double.valueOf(sceneManager.settings.screenWidth) / 2.0);
		double offsetY = (sceneManager.system.charY * Double.valueOf(sceneManager.settings.blockSize) - Double.valueOf(sceneManager.settings.screenHeight) / 2.0);
		sceneManager.system.mouseX = (int) Math.floor((offsetX + sceneManager.system.mousePosition.getX())/Double.valueOf(sceneManager.settings.blockSize));
		sceneManager.system.mouseY = (int) Math.floor((offsetY + sceneManager.system.mousePosition.getY())/Double.valueOf(sceneManager.settings.blockSize));
		sceneManager.system.isMouseFar =  (Math.sqrt(Math.pow(sceneManager.system.mouseX - Math.floor(sceneManager.system.charX), 2)+Math.pow(sceneManager.system.mouseY - Math.floor(sceneManager.system.charY), 2)) > 3);
		}
	}
}
