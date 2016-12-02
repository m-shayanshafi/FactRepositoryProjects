package com.mgstudios.rpg.game.entity;

import java.util.Random;

import com.mgstudios.rpg.game.graphics.Screen;
import com.mgstudios.rpg.game.level.Level;

public abstract class Entity {
	public int x, y;
	private boolean removed = false;
	protected Level level;
	protected final Random random = new Random();
	
	public void init (Level level) {
		this.level = level;
	}
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		
	}
	
	public void remove() {
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
}
