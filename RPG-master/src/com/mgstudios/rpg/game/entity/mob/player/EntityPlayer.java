package com.mgstudios.rpg.game.entity.mob.player;

import com.mgstudios.rpg.game.Game;
import com.mgstudios.rpg.game.entity.mob.EntityMob;
import com.mgstudios.rpg.game.graphics.Screen;
import com.mgstudios.rpg.game.graphics.Sprite;
import com.mgstudios.rpg.input.Keyboard;
import com.mgstudios.rpg.input.Mouse;

public class EntityPlayer extends EntityMob {
	private Keyboard input;
	private Sprite sprite;
	private int anim = 0;
	private Screen screen;
	
	public EntityPlayer(Keyboard input, Screen screen) {
		this.input = input;
		this.screen = screen;
		sprite = Sprite.player_up[0];
	}
	
	public EntityPlayer (int x, int y, Keyboard input, Screen screen) {
		this.input = input;
		this.screen = screen;
		this.x = x;
		this.y = y;
		sprite = Sprite.player_up[0];
	}
	
	public void update() {
		int xChange = 0, yChange = 0;
		
		if (anim < 7500) anim++;
		else anim = 0;
		
		if (input.up) yChange--;
		if (input.down) yChange++;
		if (input.left) xChange--;
		if (input.right) xChange++;
		
		if (xChange != 0 || yChange != 0) {
			move (xChange, yChange);
			moving = true;
		} else {
			moving = false;
		}
		
		updateShooting();
	}
	
	private void updateShooting() {		
		if (Mouse.button == 1) {
			double dirX = Mouse.xPos - ((screen.width * Game.scale) / 2);
			double dirY = Mouse.yPos - ((screen.height * Game.scale) / 2);
			double dir = Math.atan2(dirY, dirX);
			
			shoot (x, y, dir);
		}
	}
	
	public void render (Screen screen) {
		if (dir == 0) {
			sprite = Sprite.player_up[0];
			if (moving) {
				if (anim % 20 > 10) {
					sprite = Sprite.player_up[1];
				} else {
					sprite = Sprite.player_up[2];
				}
			}
		}
		
		if (dir == 1) {
			sprite = Sprite.player_right[0];
			if (moving) {
				if (anim % 20 > 10) {
					sprite = Sprite.player_right[1];
				} else {
					sprite = Sprite.player_right[2];
				}
			}
		}
		
		if (dir == 2) {
			sprite = Sprite.player_down[0];
			if (moving) {
				if (anim % 20 > 10) {
					sprite = Sprite.player_down[1];
				} else {
					sprite = Sprite.player_down[2];
				}
			}
		}
		
		if (dir == 3) {
			sprite = Sprite.player_left[0];
			if (moving) {
				if (anim % 20 > 10) {
					sprite = Sprite.player_left[1];
				} else {
					sprite = Sprite.player_left[2];
				}
			}
		}
		
		screen.renderPlayer(x, y, sprite);
	}
}
