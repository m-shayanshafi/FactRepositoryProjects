package com.mgstudios.rpg.game.entity.mob;

import java.util.ArrayList;
import java.util.List;

import com.mgstudios.rpg.game.entity.Entity;
import com.mgstudios.rpg.game.entity.projectile.EntityGunshot;
import com.mgstudios.rpg.game.entity.projectile.EntityProjectile;
import com.mgstudios.rpg.game.graphics.Sprite;

public abstract class EntityMob extends Entity {
	protected Sprite sprite;
	protected int dir;
	protected boolean moving;
	
	protected List<EntityProjectile> projectiles = new ArrayList<EntityProjectile>();
	
	public void move (int xMove, int yMove) {
		if (xMove != 0 && yMove != 0) {
			move (xMove, 0);
			move (0, yMove);
			return;
		}
		
		if (xMove > 0) dir = 1;
		if (xMove < 0) dir = 3;
		if (yMove > 0) dir = 2;
		if (yMove < 0) dir = 0;
		
		if (!collision (xMove, yMove)) {
			x += xMove;
			y += yMove;
		}
	}
	
	public void update() {
		
	}
	
	protected void shoot (int x, int y, double dir) {
//		dir *= 180 / Math.PI;
		EntityProjectile p = new EntityGunshot (x, y, dir);
		projectiles.add(p);
		level.add(p);
	}
	
	public void render() {
		
	}
	
	private boolean collision (int xMove, int yMove) {
		boolean solid = false;
		
		for (int c = 0; c < 4; c++) {
			int xt = ((x + xMove) + c % 2 * 14 + 1) / 16;
			int yt = ((y + yMove) + c / 2 * 12 + 3) / 16;
			
			if (level.getTile (xt, yt).isSolid()) solid = true;
		}
		
		return solid;
	}
}
