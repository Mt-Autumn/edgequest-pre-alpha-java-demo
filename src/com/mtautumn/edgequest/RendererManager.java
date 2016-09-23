package com.mtautumn.edgequest;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RendererManager extends Thread {
	static JFrame window = new JFrame("edgequest");
	private static SceneManager sceneManager;
	KeyboardInput keyboard = new KeyboardInput();
	int[] lastXFPS = new int[20];
	int tempFPS;
	public RendererManager(SceneManager scnMgr, KeyboardInput kybd) {
		sceneManager = scnMgr;
		keyboard = kybd;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.DARK_GRAY);
		window.setBounds(0, 0, sceneManager.screenWidth, sceneManager.screenHeight);
		window.setResizable(false);
		window.setName("edgequest");
		window.setTitle("edgequest");
		window.getContentPane().add(new Renderer(sceneManager));
		JPanel content = (JPanel) window.getContentPane();
		window.addKeyListener(keyboard);
		content.addKeyListener(keyboard);
	}

	public void run() {
		prepareFPSCounting();
		long lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/sceneManager.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long lastNanoPause = (int) (1000000000/sceneManager.targetFPS);
		while (true) {
			tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
			lastNanoTimeFPSGrabber = System.nanoTime();
			updateAverageFPS(tempFPS);
			updateWindow();
			try {
					lastNanoPause += (1.0/Double.valueOf(sceneManager.targetFPS) - 1.0/Double.valueOf(sceneManager.averagedFPS)) * 50000000;
					if (lastNanoPause < 0) lastNanoPause = 0;
					Thread.sleep((lastNanoPause) / 1000000,(int) ((lastNanoPause) % 1000000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void updateWindow() {
		window.getContentPane().repaint();
		window.setVisible(true);
	}
	private void updateAverageFPS(int FPS) {
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		sceneManager.averagedFPS = fpsSum / lastXFPS.length;
	}
	private void prepareFPSCounting() {
		sceneManager.averagedFPS = sceneManager.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = sceneManager.targetFPS;
		}
	}
}
