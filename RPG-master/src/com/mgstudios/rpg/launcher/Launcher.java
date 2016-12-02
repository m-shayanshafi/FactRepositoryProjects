package com.mgstudios.rpg.launcher;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.mgstudios.rpg.game.GameRun;
import com.mgstudios.rpg.input.Mouse;

public class Launcher extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private String path = "/textures/launcher/";
	
	public static final int TRIPLE_BUFFER = 3;
	
	public int width = 800;
	public int height = 400;
	
	private int iconWidth = 150;
	private int iconHeight = 54;
	private int yBuffer = 20;
	private int yPos = 4 * yBuffer;
	
	private int arrowX;
	private double arrowBuffer = 1.5 * yBuffer;
	private int arrowWidth = 50;
	private int arrowHeight = 53;
	
	private Thread thread;
	private boolean running;

	private JFrame frame;
	
	private Mouse mouse;
	
	public Launcher() {
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	public synchronized void start (JFrame frame) {
		if (running) return;
		running = true;
		this.frame = frame;
		thread = new Thread(this, "Launcher");
		thread.start();
	}
	
	public synchronized void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public void run() {
		requestFocus();
		
		while (running) {
			renderMenu();
		}
	}
	
	private void renderMenu() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy (TRIPLE_BUFFER);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor (Color.BLACK);
		g.fillRect (0, 0, width, height);
		
		renderImage (g, "launcher_image", 0, 0, width, height);
		
		int playX = ((width / 4) * 1) - (iconWidth / 2);
		arrowX = ((width / 4) * 1) - (arrowWidth / 2);
		if (Mouse.xPos >= playX && Mouse.xPos <= playX + iconWidth && Mouse.yPos >= yPos && Mouse.yPos <= yPos + iconHeight) {
			renderImage (g, "play_select", playX, yPos, iconWidth, iconHeight);
			renderImage (g, "arrow", arrowX, (int) arrowBuffer, arrowWidth, arrowHeight);
			
			if (Mouse.button == 1) {
				 frame.dispose();
				 new GameRun();
				 stop();
			}
		} else {
			renderImage (g, "play_unselect", playX, yPos, iconWidth, iconHeight);
		}
		
		int optionsX = ((width / 4) * 2) - (iconWidth / 2);
		arrowX = ((width / 4) * 2) - (arrowWidth / 2);
		if (Mouse.xPos >= optionsX && Mouse.xPos <= optionsX + iconWidth && Mouse.yPos >= yPos && Mouse.yPos <= yPos + iconHeight) {
			renderImage (g, "options_select", optionsX, yPos, iconWidth, iconHeight);
			renderImage (g, "arrow", arrowX, (int) arrowBuffer, arrowWidth, arrowHeight);
			
			if (Mouse.button == 1) {
				/* 
				 * TODO
				 * This will open a new window to configure options at a later point
				 */
			}
		} else {
			renderImage (g, "options_unselect", optionsX, yPos, iconWidth, iconHeight);
		}
		
		int exitX = ((width / 4) * 3) - (iconWidth / 2);
		arrowX = ((width / 4) * 3) - (arrowWidth / 2);
		if (Mouse.xPos >= exitX && Mouse.xPos <= exitX + iconWidth && Mouse.yPos >= yPos && Mouse.yPos <= yPos + iconHeight) {
			renderImage (g, "exit_select", exitX, yPos, iconWidth, iconHeight);
			renderImage (g, "arrow", arrowX, (int) arrowBuffer, arrowWidth, arrowHeight);
			
			if (Mouse.button == 1) {
				frame.dispose();
				stop();
			}
		} else {
			renderImage (g, "exit_unselect", exitX, yPos, iconWidth, iconHeight);
		}
		
		g.dispose();
		bs.show();
	}
	
	private void renderImage (Graphics g, String fileName, int xPos, int yPos, int width, int height) {
		try {
			g.drawImage (ImageIO.read (Launcher.class.getResource (path + fileName + ".png")), xPos, yPos, width, height, null);
		} catch (IOException e) {
			System.err.println ("Input-Output Exception caught!");
		}
	}
}
