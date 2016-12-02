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

package videotoons.data;

import videotoons.game.*;


class MonsterRockBall extends AnimMonsterLibrary
{
  public AnimMonsterLibrary anim = new AnimMonsterLibrary();

    public MonsterRockBall()
    {
         anim.name = new String("rockball");
         anim.nb_images = 8;

         anim.behaviour = GameDefinitions.MB_ROLLING;


      // Images offset & indexes

         int array1[] = { 0, 1, 2, 3, 4, 5, 6, 7 };
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 0, 7, 6, 5, 4, 3, 2, 1 };
         anim.move_left = new Anim( array2 );

         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;

         int array4[] = { 7 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 1 };
         anim.rightside = new Anim( array6 );
         
         anim.jump_left = null;
         anim.jump_right = null;
         anim.hit_up_right = null;
         anim.hit_up_left = null;

         int array11[] = { 0, 2, 4, 6 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 0, 6, 4, 2 };
         anim.fall_right = new Anim( array12 );

         anim.dead_offset = null;
         anim.nb_dead_steps = 0;
         anim.dead = null;

         anim.hurt_left = null;
         anim.hurt_right = null;
         anim.hurt_offset = null;
         anim.nb_hurt_steps=0;

         anim.hit_left = null;
         anim.hit_right = null;
         anim.hit_left_offset = null;
         anim.hit_right_offset = null;
         anim.nb_hit_steps = 0;
         
         anim.sprite_basicspeed = 4;
         anim.overlap_sprite = 5;
         anim.std_width = 32;
         anim.std_height = 32;

         anim.hit_interval = 1000;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.HARD_HIT_SOUND;
         anim.hit_sound = SoundLibrary.HARD_HIT_SOUND;
         anim.hurt_sound = SoundLibrary.HARD_HIT_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}


