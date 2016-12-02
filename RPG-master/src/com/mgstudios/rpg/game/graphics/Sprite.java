package com.mgstudios.rpg.game.graphics;

public class Sprite {
	public final int SIZE;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;
	
	public static Sprite[] player_up = {
		new Sprite (16, 0, 0, SpriteSheet.mainCharacter), 
		new Sprite (16, 0, 1, SpriteSheet.mainCharacter), 
		new Sprite (16, 0, 2, SpriteSheet.mainCharacter)
	};
	public static Sprite[] player_down = {
		new Sprite (16, 2, 0, SpriteSheet.mainCharacter),
		new Sprite (16, 2, 1, SpriteSheet.mainCharacter),
		new Sprite (16, 2, 2, SpriteSheet.mainCharacter)
	};
	public static Sprite[] player_left = {
		new Sprite (16, 3, 0, SpriteSheet.mainCharacter),
		new Sprite (16, 3, 1, SpriteSheet.mainCharacter),
		new Sprite (16, 3, 2, SpriteSheet.mainCharacter)
	};
	
	public static Sprite[] player_right = {
		new Sprite (16, 1, 0, SpriteSheet.mainCharacter),
		new Sprite (16, 1, 1, SpriteSheet.mainCharacter),
		new Sprite (16, 1, 2, SpriteSheet.mainCharacter)
	};
	
	public static Sprite voidSprite = new Sprite (16, 0x1B87E0);

	public static Sprite grass = new Sprite (16, 0, 0, SpriteSheet.outdoorsA);
	public static Sprite flower = new Sprite (16, 1, 0, SpriteSheet.outdoorsA);
	public static Sprite rock = new Sprite (16, 2, 0, SpriteSheet.outdoorsA);
	public static Sprite hedge = new Sprite (16, 3, 0, SpriteSheet.outdoorsA);
	
	public static Sprite floor = new Sprite (16, 0, 0, SpriteSheet.indoorsA);
	public static Sprite wall_1 = new Sprite (16, 1, 0, SpriteSheet.indoorsA);
	public static Sprite wall_2 = new Sprite (16, 2, 0, SpriteSheet.indoorsA);
	
	public Sprite (int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		load();
	}
	
	public Sprite (int size, int colour) {
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		setColour(colour);
	}
	
	private void setColour (int colour) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = colour;
		}
	}
	
	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				pixels[x + y * SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
			}
		}
	}
}
