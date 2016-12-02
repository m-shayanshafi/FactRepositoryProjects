package com.mgstudios.rpg.game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mgstudios.rpg.game.entity.mob.player.EntityPlayer;
import com.mgstudios.rpg.game.graphics.Screen;
import com.mgstudios.rpg.game.level.Level;
import com.mgstudios.rpg.game.level.TileCoordinate;
import com.mgstudios.rpg.input.Keyboard;
import com.mgstudios.rpg.input.Mouse;
import com.mgstudios.rpg.launcher.Launcher;
import com.mgstudios.rpg.launcher.LauncherRun;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public int width = 300;
	public int[] aspectRatio = { 16, 9 };
	public double fps = 60.0;
	
	public int height = width / aspectRatio[0] * aspectRatio[1];
	public static int scale = 3;
	
	private Thread thread;
	private boolean running;
	
	private JFrame frame;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private Mouse mouse;
	private Keyboard keyboard;
	
	private Screen screen;
	private Level level;
	private EntityPlayer player;
	private TileCoordinate spawn;
	
	public Game() {
		frame = new JFrame();
		
		mouse = new Mouse();
		Mouse.button = -1;
		keyboard = new Keyboard();
		addMouseListener (mouse);
		addMouseMotionListener (mouse);
		addKeyListener (keyboard);
		addFocusListener (keyboard);
		
		screen = new Screen (width, height);
		level = Level.spawn;
		spawn = new TileCoordinate(20, 20);
		player = new EntityPlayer (spawn.getX(), spawn.getY(), keyboard, screen);
		player.init(level);
	}
	
	public synchronized void start (JFrame frame) {
		if (running) return;
		running = true;
		this.frame = frame;
		thread = new Thread (this, "Display");
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
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double nano = 1000000000.0 / fps;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nano;
			lastTime = now;
			
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
//				System.out.println(updates + " UPS, " + frames + " FPS");
				frame.setTitle(LauncherRun.TITLE + "  |  " + updates + " UPS, " + frames + " FPS");
				updates = 0;
				frames = 0;
			}
		}
	}
	
	private void update() {
		keyboard.update();
		player.update();
		level.update();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy (Launcher.TRIPLE_BUFFER);
			return;
		}
		
		screen.clear();
		int xScroll = player.x - (screen.width / 2);
		int yScroll = player.y - (screen.height / 2);
		level.render(xScroll, yScroll, screen);
		player.render(screen);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage (image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
}
