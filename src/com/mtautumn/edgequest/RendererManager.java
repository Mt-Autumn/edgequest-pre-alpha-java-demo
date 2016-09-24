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
	int[] lastXFPS = new int[20];
	int tempFPS;
	public RendererManager(SceneManager scnMgr, KeyboardInput kybd, MenuButtonManager mbm) {
		sceneManager = scnMgr;
		keyboard = kybd;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.DARK_GRAY);
		window.setBounds(0, 0, sceneManager.settings.screenWidth, sceneManager.settings.screenHeight);
		window.setResizable(true);
		window.setMinimumSize(new Dimension(800, 600));
		window.setName("edgequest");
		window.setTitle("edgequest");
		window.getContentPane().add(new Renderer(sceneManager, mbm));
		JPanel content = (JPanel) window.getContentPane();
		window.addKeyListener(keyboard);
		content.addKeyListener(keyboard);
		content.addMouseListener(
				new MouseAdapter()
				{
					public void mouseClicked(MouseEvent me)
					{
						if (sceneManager.system.isKeyboardMenu) {
							mbm.buttonPressed(me.getX(), me.getY());
						}
					}
				});
	}

	public void run() {
		prepareFPSCounting();
		long lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/sceneManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long lastNanoPause = (int) (1000000000/sceneManager.settings.targetFPS);
		while (true) {
			updateWindowSize();
			tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
			lastNanoTimeFPSGrabber = System.nanoTime();
			updateAverageFPS(tempFPS);
			updateWindow();
			try {
				lastNanoPause += (1.0/Double.valueOf(sceneManager.settings.targetFPS) - 1.0/Double.valueOf(sceneManager.system.averagedFPS)) * 50000000;
				if (lastNanoPause < 0) lastNanoPause = 0;
				Thread.sleep((lastNanoPause) / 1000000,(int) ((lastNanoPause) % 1000000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	public static void updateWindow() {
		if (!wasMenuUp) {
			window.getContentPane().repaint();
			window.setVisible(true);
		}
		wasMenuUp = sceneManager.system.isKeyboardMenu;
	}
	private void updateAverageFPS(int FPS) {
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
}
