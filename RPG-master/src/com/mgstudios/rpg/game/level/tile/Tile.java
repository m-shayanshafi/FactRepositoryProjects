package com.mgstudios.rpg.game.level.tile;

import com.mgstudios.rpg.game.graphics.Screen;
import com.mgstudios.rpg.game.graphics.Sprite;

public class Tile {
	public int x, y;
	public Sprite sprite;
	protected boolean solid = false;
	protected boolean breakable = false;
	
	public static Tile voidTile = new Tile (Sprite.voidSprite);
	
	public static Tile grass = new Tile (Sprite.grass);
	public static final int col_grass = 0xff00FF00;
	public static Tile flower = new Tile (Sprite.flower);
	public static final int col_flower = 0xffFFFF00;
	public static Tile rock = new Tile (Sprite.rock).setSolid(true);
	public static final int col_rock = 0xff7F7F00;
	public static Tile hedge = new Tile (Sprite.hedge).setSolid(true).isBreakable(true);
	public static final int col_hedge = 0; // Unused
	
	public static Tile floor = new Tile (Sprite.floor);
	public static final int col_floor = 0xff800000;
	public static Tile wall_1 = new Tile (Sprite.wall_1).setSolid(true);
	public static final int col_wall_1 = 0xff000000;
	public static Tile wall_2 = new Tile (Sprite.wall_2).setSolid(true);
	public static final int col_wall_2 = 0xff808080;
	
	public Tile (Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void render (int x, int y, Screen screen) {
		screen.renderTile (x << 4, y << 4, this);
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public Tile setSolid(boolean bool) {
		solid = bool;
		return this;
	}
	
	public Tile isBreakable(boolean bool) {
		breakable = bool;
		return this;
	}
}
