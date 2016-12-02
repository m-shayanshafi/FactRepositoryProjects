package com.mgstudios.rpg.game.entity.projectile;

import com.mgstudios.rpg.game.graphics.Screen;
import com.mgstudios.rpg.game.graphics.Sprite;

public class EntityGunshot extends EntityProjectile {
	public EntityGunshot(int x, int y, double dir) {
		super(x, y, dir);
		range = 200.0;
		speed = 2.0;
		damage = 20.0;
		rateOfFire = 15.0;
		
		sprite = Sprite.grass;
		
		newX = speed * Math.cos(angle);
		newY = speed * Math.sin(angle);
	}
	
	public void update() {
		move();
	}
	
	protected void move() {
		x += newX;
		y += newY;
	}
	
	public void render (Screen screen) {
		screen.renderTile(x, y, sprite);
	}
}
