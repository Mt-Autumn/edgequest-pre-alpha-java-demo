package com.mtautumn.edgequest;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RendererManager extends Thread {
	static JFrame window = new JFrame("edgequest");
	private static SceneManager sceneManager;
	KeyboardInput keyboard = new KeyboardInput();
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
		while (true) {
			updateWindow();
			try {
				Thread.sleep(sceneManager.tickLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void updateWindow() {
		window.getContentPane().repaint();
		window.setVisible(true);
	}
}
