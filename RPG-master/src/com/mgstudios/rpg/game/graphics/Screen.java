package com.mgstudios.rpg.game.graphics;

import java.util.Random;

import com.mgstudios.rpg.game.level.tile.Tile;

public class Screen {
	private Random random = new Random();
	
	public int width, height;
	public int[] pixels;
	public final int MAP_SIZE = 64;
	public final int MAP_MASK = MAP_SIZE - 1;
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
	
	public int xOffset, yOffset;
	
	public Screen (int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = random.nextInt (0xFFFFFF);
		}
	}
	
	public void renderTile (int xPos, int yPos, Tile tile) {
		renderTile (xPos, yPos, tile.sprite);
	}
	
	public void renderTile (int xPos, int yPos, Sprite sprite) {
		xPos -= xOffset;
		yPos -= yOffset;
		
		for (int y = 0; y < sprite.SIZE; y++) {
			int yAbs = y + yPos;
			
			for (int x = 0; x < sprite.SIZE; x++) {
				int xAbs = x + xPos;
				
				if(xAbs < -sprite.SIZE || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				pixels[xAbs + yAbs * width] = sprite.pixels[x + y * sprite.SIZE];
			}
		}
	}
	
	public void renderPlayer (int xPos, int yPos, Sprite sprite) {
		xPos -= xOffset;
		yPos -= yOffset;
		
		for (int y = 0; y < 16; y++) {
			int yAbs = y + yPos;
			
			for (int x = 0; x < 16; x++) {
				int xAbs = x + xPos;
				
				if(xAbs < -16 || xAbs >= width || yAbs < 0 || yAbs >= height) break;
				if(xAbs < 0) xAbs = 0;
				
				int colour = sprite.pixels[x + y * 16];
				if(colour != 0xffFF00FF) pixels[xAbs + yAbs * width] = colour;
			}
		}
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void setOffset (int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
