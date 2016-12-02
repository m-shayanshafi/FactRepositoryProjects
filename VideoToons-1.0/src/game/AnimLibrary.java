/*
 * VideoToons. A Tribute to old Video Games.
 * Copyright (C) 2001 - Bertrand Le Nistour
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package videotoons.game;

// AnimatedSprite Library

import java.io.Serializable;

public class AnimLibrary implements Serializable
{
  // Sprite name
     public String name;
     
  // Number of images to load
     public int nb_images;

  // Anims when the player is not moving, looking on the right side,
  // left or middle...
     public Anim leftside;
     public Anim middle;
     public Anim rightside;

  // Anim when the sprite is moving to the right
     public Anim move_right;

  // Anim when the sprite is moving to the left     
     public Anim move_left;

  // Images index when jumping
     public Anim jump_right;
     public Anim jump_left;

  // Images index when falling
     public Anim fall_right;
     public Anim fall_left;

  // Images index when we are injured by something...
     public Anim hurt_left;
     public Anim hurt_right;

  // Images index when hitting something on the left, right or up...
     public Anim hit_left;
     public Anim hit_right;
     public Anim hit_up_left;
     public Anim hit_up_right;

  // Images index when we are dead
     public Anim dead;

  // Offsets to add to the Y coordinate to build the jump
     public int jump_offset[];
     public int nb_jump_steps;

  // X Offset when we hit on the right & left
     public int hit_right_offset[];
     public int hit_left_offset[];
     public int nb_hit_steps;

  // Offsets to add to the Y coordinate when we are dead
     public int dead_offset[];
     public int nb_dead_steps;

  // Hurt Y offset
     public int hurt_offset[];
     public int nb_hurt_steps;

  // Number of game cycles during when we can hit something
     public int nb_cycles_hit;

  // Overlap sprite for falling & stones
     public int overlap_sprite;

  // sprite base width to consider for collisions, fall, etc...
     public int std_width;

  // sprite base height to consider for collisions, fall, etc...
     public int std_height;
     
  // Speed when we are moving.
     public byte sprite_basicspeed;     

  // time to wait between two hits !
     public int hit_interval;

  // time interval before respawn
     public long invisible_interval;


  // SOUNDS indexes
     public byte jump_sound;
     public byte land_sound;
     public byte hit_sound;
     public byte hurt_sound;
     public byte dead_sound;     
}