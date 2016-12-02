package com.mgstudios.rpg.game.entity.projectile;

import com.mgstudios.rpg.game.entity.Entity;
import com.mgstudios.rpg.game.graphics.Sprite;

public abstract class EntityProjectile extends Entity {
	protected final int xOrigin, yOrigin;
	protected double angle;
	protected Sprite sprite;
	protected double newX, newY;
	protected double speed, rateOfFire, range, damage;
	
	public EntityProjectile (int x, int y, double dir) {
		this.x = x;
		this.y = y;
		xOrigin = x;
		yOrigin = y;
		angle = dir;
	}
	
	protected void move() {}
}
