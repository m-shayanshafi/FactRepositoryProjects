package com.mgstudios.rpg.game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	private String path;
	public final int SIZE;
	public int[] pixels;
	
	// Characters
	public static SpriteSheet mainCharacter = new SpriteSheet ("characters/Main", 64);
	
	// Outdoors
	public static SpriteSheet outdoorsA = new SpriteSheet ("outdoors/TileSetA", 256);
	
	// Indoors
	public static SpriteSheet indoorsA = new SpriteSheet ("indoors/TileSetA", 256);
	
	public SpriteSheet(String path, int size) {
		this.path = path;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}
	
	private void load() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource("/textures/game/spritesheets/" + path + ".png"));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
