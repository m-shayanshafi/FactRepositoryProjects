package com.mgstudios.rpg.game.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpawnLevel extends Level {
	private String fileName;
	
	public SpawnLevel (String path) {
		super(path);
		fileName = path;
	}
	
	@Override
	protected void loadLevel (String path) {
		try {
			BufferedImage image = ImageIO.read (SpawnLevel.class.getResource ("/textures/game/levels/" + path + ".png"));
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			tiles = new int[w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		} catch (IOException e) {
			System.err.println("Could not find level file: " + "/res/textures/game/levels/" + fileName + ".png");
		}
	}
	
	@Override
	protected void generateLevel() {
		
	}
}
