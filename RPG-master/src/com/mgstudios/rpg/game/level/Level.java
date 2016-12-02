package com.mgstudios.rpg.game.level;

import java.util.ArrayList;
import java.util.List;

import com.mgstudios.rpg.game.entity.Entity;
import com.mgstudios.rpg.game.graphics.Screen;
import com.mgstudios.rpg.game.level.tile.Tile;

public class Level {
	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public static Level spawn = new SpawnLevel("spawn/spawn_hub");
	
	public Level (int width, int height) {
		this.width = width;
		this.height = height;
		tilesInt = new int[width * height];
		generateLevel();
	}
	
	public Level (String path) {
		loadLevel(path);
		generateLevel();
	}
	
	protected void generateLevel() {}
	
	protected void loadLevel (String path) {}
	
	protected void time() {}
	
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.width + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height + 16) >> 4;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render (screen);
		}
	}
	
	public void add (Entity e) {
		entities.add (e);
	}
	
	public Tile getTile (int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return Tile.voidTile;
		if (tiles[x + y * width] == Tile.col_grass) return Tile.grass;
		if (tiles[x + y * width] == Tile.col_floor) return Tile.floor;
		if (tiles[x + y * width] == Tile.col_wall_1) return Tile.wall_1;
		if (tiles[x + y * width] == Tile.col_wall_2) return Tile.wall_2;
		return Tile.voidTile;
	}
}
